import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.File;

public class TinyHttpd2 {
	private static final int PORT = 8888;
	private ServerSocket serverSocket;

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
					executeCommand(client);

				}
			} finally {
				serverSocket.close();
			}
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public static void executeCommand(Socket client) {
		try {
			client.setSoTimeout(30000);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
			PrintStream out = new PrintStream(client.getOutputStream());
			try {
				System.out.println("I/O setup done");

				String line = in.readLine();
				String requestedFile = new String();

				Pattern pattern = Pattern
						.compile("(?:[^/][\\d\\w\\.]+)((?:.jpg)|(?:.pdf)|(?:.gif)|(?:.jpeg)|(?:.html)|(?:.ico))");

				Matcher m = pattern.matcher(line);
				if (m.find()) {
					requestedFile = m.group();
					while (line != null) {
						System.out.println(line);
						if (line.equals(""))
							break;
						line = in.readLine();
					}
					System.out.println(line);
				}
				// while ( in.ready() && line != null ) {

				if (!requestedFile.equals("") && requestedFile != null) {

					URL location = TinyHttpd2.class.getProtectionDomain()
							.getCodeSource().getLocation();
					String path = location.getFile().split("bin")[0];
					File file = new File(path + "" + requestedFile);
					System.out.println(file.getName() + " requested.");
					sendFile(out, file);

					out.flush();

				}

			} finally {
				in.close();
				out.close();

				client.close();

				System.out.println("A connection is closed.");
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public static void sendFile(PrintStream out, File file) {
		try {
			DataInputStream fin = new DataInputStream(new FileInputStream(file));
			try {
				out.println("HTTP/1.0 200 OK");
				out.println("Content-Type: text/html");

				int len = (int) file.length();
				out.println("Content-Length: " + len);
				out.println("");

				byte buf[] = new byte[len];
				fin.readFully(buf);
				out.write(buf, 0, len);
				out.flush();
			} finally {
				fin.close();
			}
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TinyHttpd2 server = new TinyHttpd2();
		server.init();
	}

}
