public class DigestAuthenticator {

	private static DigestAuthenticator authenticator = new DigestAuthenticator();

	private DigestAuthenticator() {
	}

	public static DigestAuthenticator getDigestAuthenticator() {

		return authenticator;

	}

	public boolean authenticateRequest(HttpExchangeRequest request) {
		boolean flag = false;
		String requestType = request.getRequestURI().split("/1")[0];

		if (requestType.contains("HTTP")) {
			flag = true;
		}
		return flag;

	}
}
