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

public class SoapService {

	public String requestMethod;
	public Socket client;
	public HttpExchangeRequest request;
	public HashMap<String, String> hm = new HashMap<String, String>();

	public SoapService(String requestMethod, Socket client,
			HttpExchangeRequest request) {
		// TODO Auto-generated constructor stub

		this.requestMethod = requestMethod;
		this.client = client;
		this.request = request;

	}

	public boolean produces() throws IOException {
		boolean flag = false;

		hm.put("name", "Jun Suzuki");
		hm.put("profession", "Assistant Professor");
		hm.put("department", "Computer Science");
		hm.put("school", "University of Massachusetts, Boston");
		hm.put("city_state", "Boston, MA");
		hm.put("zipcode", "02125-3393");
		hm.put("country", "USA");

		if (hm != null) {

			PrintStream out = new PrintStream(client.getOutputStream());

			try {
				out.println(Command.send200());
				out.println("Date:" + new Date());
				out.println("Server:" + InetAddress.getLocalHost());
				out.println("Content-Type: text/xml");
				int len = 0;
				StringBuilder sb = new StringBuilder("<");
				sb.append("root");
				sb.append(">");

				for (Map.Entry<String, String> e : hm.entrySet()) {
					sb.append("<");
					sb.append(e.getKey());
					sb.append(">");

					sb.append(e.getValue());
					sb.append("</");
					sb.append(e.getKey());
					sb.append(">");
				}

				sb.append("</");
				sb.append("root");
				sb.append(">");

				len = len + sb.length();

				out.println("Content-Length: " + len);
				out.println("Last-Modified: 39 minutes ago");
				if (request.getConnection() != null) {
					out.println("Connection: keep-alive");
				}
				out.println("");

				out.println(sb);

				out.flush();
				flag = true;
			} finally {
				out.close();
			}
		}

		return flag;

	}
}
