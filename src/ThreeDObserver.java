import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class ThreeDObserver implements StockEvObserver {
	public ThreeDObserver(StockEventObservable o) {
		o.observers.add(this);
	}

	@Override
	public void updateStock(StockEvent arg, Socket client) throws IOException {
		// TODO Auto-generated method stub
		PrintStream out = new PrintStream(client.getOutputStream());
		try {
			StockEvent s = (StockEvent) arg;
			out.println("<h1>Updating ThreeD Observer  .. </h1>");
			out.println("<p>Stock In 3d " + s.getTicker() + "</p>");
			out.println("<p>Stock In 3d position" + s.getQuote() + "</p>");
			out.println("<h1>Updating ThreeD Observer  .. SUCCESS</h1>");

			out.flush();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block

			}
		} finally {
			out.close();
		}

	}

}
