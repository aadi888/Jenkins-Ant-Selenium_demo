import java.io.IOException;
import java.net.Socket;
import java.util.EventListener;

public interface StockEvObserver extends EventListener {

	public void updateStock(StockEvent arg, Socket client) throws IOException;
}
