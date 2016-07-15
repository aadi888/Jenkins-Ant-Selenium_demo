import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Stream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.File;

public class TinyHttpd3 {
	private static final int PORT = 8888;
	private ServerSocket serverSocket;
	private static boolean fileNotFound = false;
	private static boolean internalServerError = false;
	private static File tempFile;
	private static boolean successFlag = false;
	private static boolean http1_1Flag = false;

	private static ReentrantLock lock = new ReentrantLock();

	public void init() {
		try {
			try {
				serverSocket = new ServerSocket(PORT);
				System.out.println("Socket created.");

				while (true) {
					System.out
							.println("Listening to a connection on the local port "
									+ serverSocket.getLocalPort() + "...");

					Socket client = serverSocket.accept();
					System.out
							.println("\nA connection established with the remote port "
									+ client.getPort()
									+ " at "
									+ client.getInetAddress().toString());

					new Thread(new ThreadPerConnection(client)).start();

				}
			} finally {
				serverSocket.close();
			}
		} catch (IOException exception) {

		}
	}

	public static void executeCommand(Socket client) {
		try {
			client.setSoTimeout(30000);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					client.getInputStream()));

			PrintStream out = new PrintStream(client.getOutputStream());
			out.flush();

			try {
				System.out.println("I/O setup done");

				String line = in.readLine();

				System.out.println(line);

				String requestType = line.split(" ")[0];
				String httpVersion = line.split(" ")[2];
				String connection = "";

				if (requestType.equals("GET")) {

					String requestedFile = new String();

					Pattern pattern = Pattern
							.compile("(?:[^/][\\d\\w\\.]+)((?:.jpg)|(?:.pdf)|(?:.gif)|(?:.jpeg)|(?:.html)|(?:.ico))");

					if (line.equals("GET / HTTP/1.0")) {
						line = "GET /index.html HTTP/1.0";
					}
					if (line.equals("GET / HTTP/1.1")) {
						line = "GET /index.html HTTP/1.1";
					}

					Matcher m = pattern.matcher(line);
					if (m.find()) {
						requestedFile = m.group();
						while (line != null) {
							System.out.println(line);
							if (line.equals(""))
								break;
							if (line.contains("Connection"))
								connection = line;
							line = in.readLine();
						}
						System.out.println(line);
						if (connection.equals("")
								&& httpVersion.equals("HTTP/1.0")) {

							http1_1Flag = false;
						}
						if (connection
								.equalsIgnoreCase("Connection: keep-alive")
								&& httpVersion.equals("HTTP/1.0")) {

							http1_1Flag = true;
						}
						if (httpVersion.equalsIgnoreCase("HTTP/1.1")) {

							http1_1Flag = true;
						}

					}
					// while ( in.ready() && line != null ) {

					if (!requestedFile.equals("") && requestedFile != null) {

						URL location = TinyHttpd2.class.getProtectionDomain()
								.getCodeSource().getLocation();
						String path = location.getFile().split("bin")[0];
						File file = new File(path + "" + requestedFile);
						tempFile = file;
						System.out.println(file.getName() + " requested.");
						sendFile(out, file, http1_1Flag);

						out.flush();

						if (http1_1Flag == true) {
							while (!client.isClosed()) {
								String line1 = in.readLine();
								while (line1 != null) {
									System.out.println("********" + line1);
									if (line1.equals(""))
										break;
									line1 = in.readLine();

								}

							}
						}

					}
				} else if (requestType.equals("POST")) {

					List<String> headers = new ArrayList<String>();
					String str;
					String temp = "";
					while ((str = in.readLine()) != null) {
						System.out.println(str);
						headers.add(str);
						if (str.startsWith("Content-Length: ")) {
							temp = str;
						}
						if (str.equals(""))
							break;
					}

					int contentLength = Integer.parseInt(temp
							.substring("Content-Length: ".length()));

					StringBuilder requestContent = new StringBuilder();
					int ch;
					for (int i = 0; i < contentLength; i++) {
						requestContent.append((char) in.read());
					}

					System.out.println("POST REQUEST DATA FROM CLIENT "
							+ requestContent);
					successFlag = true;
					sendFile(out, tempFile, false);

					out.flush();

				} else if (requestType.equals("HEAD")) {

					out.println(Command.send200());
					out.println("Date:" + new Date());
					out.println("Server:" + InetAddress.getLocalHost());

					out.println("Content-Type: text/html");

					String s1 = "<h1>SUCCESS</h1>";
					out.println("Content-Length: 1000");
					out.println("Last-Modified: 39 minutes ago");
					out.println("");
					// not doing flush here :)

					out.println("<h1>HEADERS ARE </h1>");

					String str;

					while ((str = in.readLine()) != null) {
						out.println("********" + str + "********");

						if (str.equals("")) {
							out.flush();
							break;
						}
						;
					}
					out.flush();

				}
			} finally {

				in.close();

				out.close();

				client.close();

				System.out.println("A connection is closed.");

			}
		} catch (Exception e) {
			internalServerError = true;

		}
	}

	public static void sendFile(PrintStream out, File file, boolean http1_1Flag)
			throws UnknownHostException {
		try {
			DataInputStream fin = new DataInputStream(new FileInputStream(file));
			try {

				if (successFlag == true) {
					out.println(Command.send200());
					out.println("Date:" + new Date());
					out.println("Server:" + InetAddress.getLocalHost());

					out.println("Content-Type: text/html");

					String s1 = "<h1>SUCCESS</h1>";
					out.println("Content-Length: " + s1.length());
					out.println("Last-Modified: 39 minutes ago");
					if (http1_1Flag == true) {
						out.println("Connection: keep-alive");
					}
					out.println("");

					out.println(s1);
					out.flush();
				} else {
					out.println(Command.send200());
					out.println("Date:" + new Date());
					out.println("Server:" + InetAddress.getLocalHost());

					out.println("Content-Type: text/html");

					int len = (int) file.length();
					out.println("Content-Length: " + len);
					out.println("Last-Modified: 39 minutes ago");
					if (http1_1Flag == true) {
						out.println("Connection: keep-alive");
					}
					out.println("");

					byte buf[] = new byte[len];
					fin.readFully(buf);
					out.write(buf, 0, len);

					out.flush();
				}
			} finally {
				successFlag = false;
				fin.close();
			}
		} catch (FileNotFoundException exception) {
			fileNotFound = true;
			out.println(Command.send404());
			out.println("Date:" + new Date());
			out.println("Server:" + InetAddress.getLocalHost());
			out.println("Content-Type: text/html");

			out.println("Content-Length: " + 0);
			out.println("Last-Modified: 39 minutes ago");
			out.println("");
			out.flush();

		} catch (Exception e) {
			internalServerError = true;
			out.println(Command.send501());
			out.println("Date:" + new Date());
			out.println("Server:" + InetAddress.getLocalHost());
			out.println("Content-Type: text/html");

			out.println("Content-Length: " + 0);
			out.println("Last-Modified: 39 minutes ago");
			out.println("");
			out.flush();
		}
	}

	public static void main(String[] args) {
		TinyHttpd3 server = new TinyHttpd3();
		server.init();
	}

}
