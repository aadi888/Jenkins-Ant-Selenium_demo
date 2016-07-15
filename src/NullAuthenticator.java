public class NullAuthenticator {

	private static NullAuthenticator authenticator = new NullAuthenticator();

	private NullAuthenticator() {
	}

	public static NullAuthenticator getNullAuthenticator() {

		return authenticator;

	}

	public boolean authenticateRequest(HttpExchangeRequest request) {
		boolean flag = false;

		if (request.hostName != null)
			flag = true;
		if (request.protocolName != null)
			flag = true;
		if (request.protocolVersion != null)
			flag = true;
		if (request.requestMethod != null)
			flag = true;
		if (request.requestURI != null)
			flag = true;

		return flag;

	}

}
