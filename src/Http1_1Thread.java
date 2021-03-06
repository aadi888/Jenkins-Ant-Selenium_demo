import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class Http1_1Thread implements Runnable {

	private Socket client;
	private HttpExchangeRequest request;
	private NullAuthenticator nullauthenticator = NullAuthenticator
			.getNullAuthenticator();
	private BasicAuthenticator basicauthenticator = BasicAuthenticator
			.getBasicAuthenticator();
	private DigestAuthenticator digestauthenticator = DigestAuthenticator
			.getDigestAuthenticator();

	public Http1_1Thread(Socket client, HttpExchangeRequest request) {
		// TODO Auto-generated constructor stub
		this.client = client;
		this.request = request;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		boolean nullflag = nullauthenticator.authenticateRequest(request);
		boolean basicflag = basicauthenticator.authenticateRequest(request);
		boolean digestflag = digestauthenticator.authenticateRequest(request);
		if (nullflag == true || basicflag == true || digestflag == true) {
			Http1_1RequestHandler.executeCommand(client, request);
		}

	}

}
