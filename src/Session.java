import java.util.HashMap;

public class Session {

	private static Session session = new Session();
	private static HashMap<String, Object> hm = new HashMap<String, Object>();

	private Session() {

	}

	public static Session getSession() {
		return session;
	}

	public void setAttribute(String key, Object value) {
		hm.put(key, value);
	}

	public Object getAttribute(String key) {
		return hm.get(key);
	}

}
