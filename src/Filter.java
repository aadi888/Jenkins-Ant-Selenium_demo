import java.io.IOException;
import java.net.Socket;

public interface Filter {

	void doFilter(HttpExchangeRequest request, Socket client)
			throws IOException;

}
