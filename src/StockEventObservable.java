import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class StockEventObservable {

	boolean changeFlag = false;
	ArrayList<StockEvObserver> observers = new ArrayList<StockEvObserver>();

	public void addObserver(StockEvObserver o) {
		this.observers.add(o);
	}

	public boolean hasChanged() {
		return changeFlag;
	}

	public void setChanged() {
		this.changeFlag = true;
	}

	public void clearChanged() {
		this.changeFlag = false;
	}

	public void notifyObservers() throws IOException {
		notifyObservers(null, null);
	}

	public void notifyObservers(StockEvent arg, Socket client)
			throws IOException {
		if (hasChanged()) {
			for (StockEvObserver o : observers) {
				o.updateStock(arg, client);
			}
			clearChanged();
		}
	}

	public void changeQuote(String t, float q, Socket client)
			throws IOException {

		setChanged();
		notifyObservers(new StockEvent(t, q), client);
	}

}
