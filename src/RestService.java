import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestService {

	public String requestMethod;
	public Socket client;
	public HttpExchangeRequest request;
	public HashMap<String, String> hm = new HashMap<String, String>();

	public RestService(String requestMethod, Socket client,
			HttpExchangeRequest request) {
		// TODO Auto-generated constructor stub

		this.requestMethod = requestMethod;
		this.client = client;
		this.request = request;

	}

	public boolean produces() throws IOException {
		boolean flag = false;
		hm.put("CS680", "Obj-Orient DsgnPrgm");
		hm.put("CS681", "Obj-Orient Sftwr Dev");
		hm.put("CS682", "Software Devlp Lab I");
		hm.put("CS683", "Sftware Dvlpm Lab II");

		if (hm != null) {

			PrintStream out = new PrintStream(client.getOutputStream());

			try {
				out.println(Command.send200());
				out.println("Date:" + new Date());
				out.println("Server:" + InetAddress.getLocalHost());
				out.println("Content-Type: application/json");
				int len = 0;

				for (Map.Entry<String, String> entry : hm.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					len = len + key.length() + value.length() + 2;
					// do stuff
				}
				len = len + 4;

				out.println("Content-Length: " + len);
				out.println("Last-Modified: 39 minutes ago");
				if (request.getConnection() != null) {
					out.println("Connection: keep-alive");
				}
				out.println("");

				out.println(hm);

				out.flush();
				flag = true;
			} finally {
				out.close();
			}
		}

		return flag;

	}
}
