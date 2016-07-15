import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;

public class ProcessSessionRequest {

	ProcessSessionRequest(HttpExchangeRequest request, Socket client)
			throws IOException {

		Session session = Server.getSession();
		String login = (String) session.getAttribute("login");
		String password = (String) session.getAttribute("password");
		PrintStream out = new PrintStream(client.getOutputStream());

		out.println(Command.send200());
		out.println("Date:" + new Date());
		out.println("Server:" + InetAddress.getLocalHost());

		out.println("Content-Type: text/html");

		StringBuilder builder = new StringBuilder();
		builder.append("<h1>Hey Jun , We Have Stored Your Login Password in Session. You can access it anywhere . Here are your credentials.</h1>");
		builder.append("<p>LOGIN : ");
		builder.append(login);
		builder.append("</p>");
		builder.append("<p>PASSWORD : ");
		builder.append(password);
		builder.append("</p>");
		builder.append("<a href='http://localhost:8888/index.html'>Go Back To HomePage</a>");

		out.println("Content-Length: " + builder.length());
		out.println("Last-Modified: 39 minutes ago");
		if (request.getConnection() != null) {
			out.println("Connection: keep-alive");
		}
		out.println("");

		out.println(builder);

	}

}
