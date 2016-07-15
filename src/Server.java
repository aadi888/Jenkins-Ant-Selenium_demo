import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server {

	private static final int PORT = 8888;
	private ServerSocket serverSocket;
	private StaticThreadPool pool;
	private static Session session = Session.getSession();

	public static Session getSession() {
		return session;
	}

	public void init() {
		try {
			try {
				serverSocket = new ServerSocket(PORT);
				System.out.println("Socket created.");
				pool = StaticThreadPool.getInstance(10, true);
				System.out.println("Thread Pool Started 10 threads .... ");
				System.out
						.println("Listening to a connection on the local port "
								+ serverSocket.getLocalPort() + "...");

				while (true) {

					HttpExchangeRequest request1 = null;
					HttpExchangeRequest request = null;
					Socket client = serverSocket.accept();

					request1 = new HttpExchangeRequest();
					request = request1.parseRequest(client, serverSocket);

					if (request != null) {
						String httpVersion = request.getRequestMethod().split(
								" ")[2];
						String connection = request.getConnection();

						if (connection.isEmpty()
								&& httpVersion.equals("HTTP/1.0")) {
							System.out
									.println("\nA connection established with the remote port "
											+ client.getPort()
											+ " at "
											+ client.getInetAddress()
													.toString());
							pool.execute(new Http1_0Thread(client, request));
							continue;

						} else {

							System.out.println(client.getInetAddress());

							System.out
									.println("\nA connection established with the remote port "
											+ client.getPort()
											+ " at "
											+ client.getInetAddress()
													.toString());

							pool.execute(new Http1_1Thread(client, request));

							continue;
						}

					}

				}

			} finally {
				System.out.println("serversocket closed");

				serverSocket.close();

			}
		} catch (IOException exception) {

		}
	}

	public static void main(String[] args) {

		Server s = new Server();
		s.init();

	}

}
