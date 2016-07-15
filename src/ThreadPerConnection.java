import java.net.Socket;

public class ThreadPerConnection implements Runnable {

	private Socket client;

	public ThreadPerConnection(Socket client) {
		// TODO Auto-generated constructor stub
		this.client = client;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		TinyHttpd3.executeCommand(client);
	}

}
