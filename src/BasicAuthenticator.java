public class BasicAuthenticator {

	private static BasicAuthenticator authenticator = new BasicAuthenticator();

	private BasicAuthenticator() {
	}

	public static BasicAuthenticator getBasicAuthenticator() {

		return authenticator;

	}

	public boolean authenticateRequest(HttpExchangeRequest request) {
		boolean flag = false;
		String requestType = request.getRequestURI().split(" ")[0];

		if (requestType.equals("GET") || requestType.equals("HEAD")
				|| requestType.equals("POST")) {
			flag = true;
		}
		return flag;

	}
}
