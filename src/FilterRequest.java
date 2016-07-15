import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;
import java.util.Map;

public class FilterRequest implements Filter {

	@Override
	public void doFilter(HttpExchangeRequest request, Socket client)
			throws IOException {

		PrintStream out = new PrintStream(client.getOutputStream());
		String s = "<h1>Hey jun ! your process is in filter . We will take 5 seconds to forward your request </h1>";

		try {

			out.println(s);
			out.flush();

			try {
				Thread.sleep(5000);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			RequestDispatcher.getRequestDispatcher().forward(request, client);

		} finally {
			out.close();
		}

	}

}
