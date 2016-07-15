import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;

public class Http1_0RequestHandler {
	private static boolean fileNotFound = false;
	private static boolean internalServerError = false;
	private static File tempFile;
	private static boolean successFlag = false;
	private static boolean http1_1Flag = false;

	public static void executeCommand(Socket client, HttpExchangeRequest request) {

		try {
			client.setSoTimeout(30000);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
			PrintStream out = new PrintStream(client.getOutputStream());
			out.flush();
			try {
				System.out.println("I/O setup done");
				String requestMethod = request.getRequestMethod();
				String requestType = request.getRequestMethod().split(" ")[0];
				String acceptLanguage = request.getAcceptLanguage();
				String cache = request.getCache();
				String connection = request.getConnection();
				String hostName = request.getHostName();

				if (requestType.equals("GET")) {

					String requestedFile = "";

					if (requestMethod.contains("showAllStudents")) {

						Database dao = new Database(requestMethod, client,
								request);
						boolean flag = dao
								.executeUpdate("select * from Students");
						if (flag == false) {
							System.out.println("Something went wrong .. ");
						}
						return;
					}
					if (requestMethod.contains("RestService")) {

						RestService service = new RestService(requestMethod,
								client, request);
						boolean flag = service.produces();
						if (flag == false) {
							System.out.println("Something went wrong .. ");
						}
						return;
					}
					if (requestMethod.contains("SoapService")) {

						SoapService service = new SoapService(requestMethod,
								client, request);
						boolean flag = service.produces();
						if (flag == false) {
							System.out.println("Something went wrong .. ");
						}
						return;
					}

					if (requestMethod.contains("CookieController")) {

						Class c = Class.forName("Servlet"); // java reflection
															// API

						Servlet s = new Servlet().downCast(c);
						s.doGet(request, client);

						return;
					}
					if (requestMethod.contains("FilterRequest")) {

						FilterRequest filter = new FilterRequest();
						filter.doFilter(request, client);

						return;
					}
					if (requestMethod.contains("Documents")) {

						Documents document = new Documents();
						document.process(request, client);

						return;
					}

					if (requestMethod.contains("Ical")) {

						URL location = TinyHttpd2.class.getProtectionDomain()
								.getCodeSource().getLocation();
						String path = location.getFile().split("bin")[0];
						System.out.println(path);
						FileInputStream fin = new FileInputStream(
								"C:/Users/user/Desktop/lastEclipse/Cs681(IndividualProject)/"
										+ "MyCalender.ics");

						CalendarBuilder builder = new CalendarBuilder();

						Calendar calendar = builder.build(fin);
						if (calendar != null) {
							out.println(Command.send200());
							out.println("Date:" + new Date());
							out.println("Server:" + InetAddress.getLocalHost());

							out.println("Content-Type: text/html");

							String goBack = "<a href='http://localhost:8888/index.html'>Go Back To HomePage</a>";

							out.println("Last-Modified: 39 minutes ago");
							// out.println("Connection: keep-alive");
							out.println("");

							for (Iterator i = calendar.getComponents()
									.iterator(); i.hasNext();) {
								Component component = (Component) i.next();
								out.println("<h2>Component ["
										+ component.getName() + "]</h2>");

								for (Iterator j = component.getProperties()
										.iterator(); j.hasNext();) {
									Property property = (Property) j.next();
									out.println("<h2><strong>Property ["
											+ property.getName() + ", "
											+ property.getValue()
											+ "]</strong></h2>");
								}
							}
							out.println(goBack);
							out.flush();
							out.close();
						}

						return;
					}
					if (requestMethod.contains("Session")) {

						Session session = Server.getSession();
						session.setAttribute("login", "jun123");
						session.setAttribute("password", "suzuki");

						new ProcessSessionRequest(request, client);

						return;
					}
					if (requestMethod.contains("Listeners")) {

						StockEventObservable stock = new StockEventObservable();
						new PiechartObserver(stock);
						new TableObserver(stock);
						new ThreeDObserver(stock);

						String t = "walmart";
						float q = 1000;
						System.out.println("*******************************");
						System.out.println("Updating Value for " + t);
						out.println(Command.send200());
						out.println("Date:" + new Date());
						out.println("Server:" + InetAddress.getLocalHost());

						out.println("Content-Type: text/html");

						String goBack = "<a href='http://localhost:8888/index.html'>Go Back To HomePage</a>";

						out.println("Last-Modified: 39 minutes ago");
						// out.println("Connection: keep-alive");
						out.println("");

						out.println(goBack);

						stock.changeQuote(t, q, client);
						System.out.println("*******************************");

						return;
					}

					Pattern pattern = Pattern
							.compile("(?:[^/][\\d\\w\\.]+)((?:.jpg)|(?:.pdf)|(?:.gif)|(?:.jpeg)|(?:.html)|(?:.ico))");

					if (requestMethod.equals("GET / HTTP/1.0")) {
						requestMethod = "GET /index.html HTTP/1.0";
					}

					Matcher m = pattern.matcher(requestMethod);
					if (m.find()) {
						requestedFile = m.group();

					}
					// while ( in.ready() && line != null ) {

					if (!requestedFile.equals("") && requestedFile != null) {

						URL location = TinyHttpd2.class.getProtectionDomain()
								.getCodeSource().getLocation();
						String path = location.getFile().split("bin")[0];
						/* File Caching and counter logic */

						System.out.println("File Caching and counter logic "
								+ path + "" + requestedFile);
						Path paths = Paths.get(requestedFile);
						if (Files.exists(paths)) {
							// LOGIC FOR FILCACHING AND FILTERS ...
							CachingCounterMethods c = new CachingCounterMethods(
									paths);
							c.applyFilters();
						}

						File file = new File(path + "" + requestedFile);
						tempFile = file;
						System.out.println(file.getName() + " requested.");
						sendFile(out, file);

						out.flush();

					}
				} else if (requestType.equals("POST")) {
					System.out.println("request handler post "
							+ request.getParamValues());

					if (request.getRequestURI().contains("AddNewStudent")) {

						Database dao = new Database(requestMethod, client,
								request);
						boolean flag = dao
								.executeUpdate("insert into Student values "
										+ request.getParamValues());
						if (flag == false) {
							System.out.println("Something went wrong .. ");
						}
						return;
					} else {
						successFlag = true;
						sendFile(out, tempFile);
					}

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
				System.out
						.println("Client you spent 30 seconds on our server . That's all . I am closing your connection . ");

				client.close();

			}
		}

		catch (Exception e) {
			try {
				System.out
						.println("Client you spent 30 seconds on our server . That's all . I am closing your connection . ");

				client.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			internalServerError = true;

		}

	}

	public static void sendFile(PrintStream out, File file)
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
					String goBack = "<a href='http://localhost:8888/index.html'>Go Back To HomePage</a>";
					out.println("Content-Length: "
							+ (s1.length() + goBack.length()));
					out.println("Last-Modified: 39 minutes ago");
					// out.println("Connection: keep-alive");
					out.println("");

					out.println(s1);
					out.println(goBack);
					out.flush();
				} else {
					out.println(Command.send200());
					out.println("Date:" + new Date());
					out.println("Server:" + InetAddress.getLocalHost());

					out.println("Content-Type: text/html");

					int len = (int) file.length();
					out.println("Content-Length: " + len);
					out.println("Last-Modified: 39 minutes ago");
					// out.println("Connection: keep-alive");
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
			// out.println("Connection: keep-alive");
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
			// out.println("Connection: keep-alive");
			out.println("");
			out.flush();
		}
	}

}
