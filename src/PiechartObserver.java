import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class PiechartObserver implements StockEvObserver {

	PiechartObserver(StockEventObservable o) {
		o.observers.add(this);
	}

	@Override
	public void updateStock(StockEvent arg, Socket client) throws IOException {
		// TODO Auto-generated method stub
		PrintStream out = new PrintStream(client.getOutputStream());

		StockEvent s = (StockEvent) arg;
		out.println("<h1>Updating PieChart Observer  .. </h1> ");
		out.println("<p>Stock In Piechart" + s.getTicker() + "</p>");
		out.println("<p>Stock In Piechart position" + s.getQuote() + "</p>");
		out.println("<h1>Updating PieChart Observer  .. SUCCESS </h1>");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

	}

}
