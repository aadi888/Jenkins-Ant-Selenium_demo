import java.util.EventObject;

public class StockEvent extends EventObject {
	public StockEvent() {
		super("temp");
	}

	public StockEvent(String t, float q) {

		super("temp");
		this.ticker = t;
		this.quote = q;
		// TODO Auto-generated constructor stub
	}

	private String ticker;
	private float quote;

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public float getQuote() {
		return quote;
	}

	public void setQuote(float quote) {
		this.quote = quote;
	}

}
