import java.io.IOException;
import java.net.Socket;

public abstract class HttpServlet {

	public abstract void doGet(HttpExchangeRequest request, Socket client)
			throws IOException;

}
