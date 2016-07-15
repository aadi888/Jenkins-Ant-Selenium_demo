public class Command {

	public static String send200() {
		return "HTTP/1.0 200 (OK)";
	}

	public static String send404() {
		return "HTTP/1.0 404 NOT FOUND";
	}

	public static String send501() {
		return "HTTP/1.0 501 INTERNAL ERROR";
	}

}
