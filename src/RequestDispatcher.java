import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

public class RequestDispatcher {

	private static RequestDispatcher rd = new RequestDispatcher();

	private RequestDispatcher() {

	}

	public static RequestDispatcher getRequestDispatcher() {
		return rd;
	}

	public void forward(HttpExchangeRequest request, Socket client)
			throws IOException {
		PrintStream out = new PrintStream(client.getOutputStream());
		String s = "<h1>Request Filtered Successfully . </h1>";
		String goBack = "<a href='http://localhost:8888/index.html'>Go Back To HomePage</a>";

		try {
			out.println(Command.send200());
			out.println("Date:" + new Date());
			out.println("Server:" + InetAddress.getLocalHost());
			out.println("Content-Type: text/html");
			int len = 0;

			len = s.length() + 1 + goBack.length() + 1;

			out.println("Content-Length: " + len);
			out.println("Last-Modified: 39 minutes ago");
			if (request.getConnection() != null) {
				out.println("Connection: keep-alive");
			}
			out.println("");

			out.println(s);
			out.println(goBack);

			out.flush();
		} finally {
			out.close();
		}
	}

}
