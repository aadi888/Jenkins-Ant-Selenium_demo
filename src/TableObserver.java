import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class TableObserver implements StockEvObserver {

	public TableObserver(StockEventObservable o) {
		o.observers.add(this);
	}

	@Override
	public void updateStock(StockEvent arg, Socket client) throws IOException {
		// TODO Auto-generated method stub
		PrintStream out = new PrintStream(client.getOutputStream());
		try {
			StockEvent s = (StockEvent) arg;
			out.println("<h1>Updating Table Observer  .. </h1>");
			out.println("<p>Stock In Table " + s.getTicker() + "</p>");
			out.println("<p>Stock In Table  position" + s.getQuote() + "</p>");
			out.println("<h1>Updating Table Observer  .. SUCCESS</h1>");

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		} finally {

		}
	}

}
