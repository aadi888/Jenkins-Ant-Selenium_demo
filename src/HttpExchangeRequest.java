import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class HttpExchangeRequest {
	String requestMethod;
	String hostName;
	String connection;
	String contentLength;
	StringBuilder paramValues;
	String requestURI;
	String protocolName;
	String protocolVersion;
	List<String> requestBody = new ArrayList<String>();

	public String getRequestURI() {
		return requestURI;
	}

	public String getProtocolName() {
		return protocolName;
	}

	public String getProtocolVersion() {
		return protocolVersion;
	}

	public List<String> getRequestBody() {
		return requestBody;
	}

	public StringBuilder getParamValues() {
		return paramValues;
	}

	public String getContentLength() {
		return contentLength;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public String getHostName() {
		return hostName;
	}

	public String getConnection() {
		return connection;
	}

	public String getCache() {
		return cache;
	}

	public String getAcceptLanguage() {
		return acceptLanguage;
	}

	String cache;
	String acceptLanguage;

	public HttpExchangeRequest parseRequest(Socket client,
			ServerSocket serverSocket) throws IOException {
		HttpExchangeRequest r = new HttpExchangeRequest();
		boolean flag = false;
		BufferedReader in = new BufferedReader(new InputStreamReader(
				client.getInputStream()));

		/*
				 * 
	
	
	
	

	
	
				 * 
				 * */

		String line = in.readLine();
		if (line != null) {

			if (line.contains("GET") || line.contains("HEAD")) {
				System.out.println("***************REQUEST****************");

				while (line != null) {
					r.requestBody.add(line);
					System.out.println(line);
					if (line.contains("GET") || line.contains("POST")
							|| line.contains("HEAD")) {
						flag = true;
						r.requestMethod = line;
						r.requestURI = line;
						r.protocolName = line.split(" ")[1];
						r.protocolVersion = line.split(" ")[1];
					}
					if (line.contains("HOST")) {
						flag = true;
						r.hostName = line;
					}
					if (line.contains("Connection")) {
						flag = true;
						r.connection = line;
					}
					if (line.contains("Cache")) {
						flag = true;
						r.cache = line;
					}
					if (line.contains("Accept")) {
						flag = true;
						r.acceptLanguage = line;
					}
					if (line.contains("Content-Length")) {
						flag = true;
						r.contentLength = line;
					}
					if (line.equals(""))
						break;

					line = in.readLine();
				}
			}
			if (line.contains("POST")) {
				System.out.println("***************REQUEST****************");
				List<String> headers = new ArrayList<String>();
				String str;
				String temp = "";
				System.out.println(line);
				if (line.contains("GET") || line.contains("POST")
						|| line.contains("HEAD")) {
					flag = true;
					r.requestMethod = line;
					r.requestBody.add(line);
					r.requestURI = line;
					r.protocolName = line.split(" ")[1];
					r.protocolVersion = line.split("/")[1];
				}
				while ((str = in.readLine()) != null) {
					System.out.println(str);
					r.requestBody.add(str);

					if (str.contains("Host")) {
						flag = true;
						r.hostName = str;
					}
					if (str.contains("Connection")) {
						flag = true;
						r.connection = str;
					}
					if (str.contains("Cache")) {
						flag = true;
						r.cache = str;
					}
					if (str.contains("Accept")) {
						flag = true;
						r.acceptLanguage = str;
					}
					if (str.contains("Content-Length")) {
						flag = true;
						r.contentLength = str;
					}
					headers.add(str);
					if (str.startsWith("Content-Length: ")) {
						temp = str;
					}
					if (str.equals(""))
						break;
				}

				int contentLength = Integer.parseInt(temp
						.substring("Content-Length: ".length()));

				StringBuilder requestContent = new StringBuilder();
				int ch;
				for (int i = 0; i < contentLength; i++) {
					requestContent.append((char) in.read());
				}

				if (requestContent != null) {
					r.paramValues = requestContent;
				}
				System.out.println("POST REQUEST DATA FROM CLIENT "
						+ r.paramValues);

			}
		}
		if (flag == true) {
			return r;
		} else {
			return null;
		}

	}

}
