import java.io.IOException;
import java.io.PrintStream;
import java.io.ObjectInputStream.GetField;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;

public class Servlet extends HttpServlet {

	@Override
	public void doGet(HttpExchangeRequest request, Socket client)
			throws IOException {
		// TODO Auto-generated method stub

		PrintStream out = new PrintStream(client.getOutputStream());

		try {
			String successMessage = "<h1>Hey Jun I have Added Cookie In Your Browser . You can check it by going through your browsers cookie ... </h1>";
			String p = "<p>";
			String pEnd = "</p>";
			String goBack = "<a href='http://localhost:8888/index.html'>Go Back To HomePage</a>";
			out.println(Command.send200());
			out.println("Date:" + new Date());
			out.println("Server:" + InetAddress.getLocalHost());
			out.println("Content-Type: text/html");
			int len = successMessage.length() + p.length() + pEnd.length()
					+ goBack.length();

			out.println("Content-Length: " + len);
			out.println("Last-Modified: 39 minutes ago");
			if (request.getConnection() != null) {
				out.println("Connection: keep-alive");
			}
			out.println("Set-Cookie:Jun_Website=Jun Logged Into Website Last at "
					+ new Date()
					+ ";expires=Sat, 01-Jan-2020 00:00:00 GMT;path=/;");
			out.println("");

			out.println(successMessage);
			out.println(p + "" + goBack + "" + pEnd);

			out.flush();

		} catch (Exception e) {
			return;
		} finally {
			out.close();
		}

	}

	// downcasting logic
	public Servlet downCast(Class c) {
		Servlet s = null;
		// TODO Auto-generated method stub

		if (this.getClass().getName().equals(c.getName()))

		{
			System.out.println("Downcasting Object ...");
			s = (Servlet) c.cast(new Servlet());

		}
		return s;
	}
}