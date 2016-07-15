import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Database {

	public String requestMethod;
	public Socket client;
	public HttpExchangeRequest request;
	public List<String> list = new ArrayList<String>();
	public String[] stringArr;
	String path;

	public Database(String requestMethod, Socket client,
			HttpExchangeRequest request) {
		// TODO Auto-generated constructor stub

		this.requestMethod = requestMethod;
		this.client = client;
		this.request = request;
		URL location = TinyHttpd2.class.getProtectionDomain().getCodeSource()
				.getLocation();
		path = location.getFile().split("bin")[0];
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(path + "studentDb.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String str;

		try {
			while ((str = in.readLine()) != null) {
				list.add(str);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		stringArr = list.toArray(new String[0]);

	}

	public boolean executeUpdate(String query) throws IOException {
		boolean flag = false;

		if (requestMethod.contains("showAllStudent")
				&& query.contains("select *")) {

			if (stringArr != null) {

				PrintStream out = new PrintStream(client.getOutputStream());

				try {
					String successMessage = "<h1>Hey Jun Here Is Your Student List Coming From Database ... </h1>";
					String form = "<br/><br/><center><h1>You Can Add New Student Here , Dont worry it will be saved in database and yes it's working :)  </h1>"
							+ "<h3><form name='addNewStudentForm' method='post' action='http://localhost:8888/AddNewStudent'></h3>"
							+ "<h3>Full Name : <input type='text' name='fullName' value=''></h3>"
							+ "<h3>Enrollment Number : <input type='text' name='rollNo' value=''></h3>"
							+ "<h3>Degree : <input type='text' name='degree' value=''></h3>"
							+ "<h3><input type='submit' name='Add Student' value='Add Student'></h3>"

							+ "<h3></form></h3>"

							+ "</center>";
					String p = "<p>";
					String pEnd = "</p>";
					String goBack = "<a href='http://localhost:8888/index.html'>Go Back To HomePage</a>";
					out.println(Command.send200());
					out.println("Date:" + new Date());
					out.println("Server:" + InetAddress.getLocalHost());
					out.println("Content-Type: text/html");
					int len = 0;
					for (int i = 0; i < stringArr.length; i++) {
						len = len + stringArr[i].length() + 1 + p.length()
								+ pEnd.length();
					}
					len = len + successMessage.length() + 1;
					len = len + goBack.length() + 1 + 4;
					len = len + form.length() + 1;

					out.println("Content-Length: " + len);
					out.println("Last-Modified: 39 minutes ago");
					if (request.getConnection() != null) {
						out.println("Connection: keep-alive");
					}
					out.println("");

					out.println(successMessage);
					for (int i = 0; i < stringArr.length; i++) {
						out.println("<p>" + stringArr[i] + "</p>");

					}
					out.println(goBack);
					out.println(form);

					out.flush();
					flag = true;
				} finally {
					out.close();
				}
			}

		}
		if (requestMethod.contains("AddNewStudent")
				&& query.contains("insert into")) {
			String append = query.split("&")[0].split(" ")[4].replace("+", " ")
					.split("=")[1]
					+ " "
					+ query.split("&")[1].split("=")[1]
					+ " " + query.split("&")[2].split("=")[1].replace("+", " ");

			URL location = TinyHttpd2.class.getProtectionDomain()
					.getCodeSource().getLocation();
			path = location.getFile().split("bin")[0];
			BufferedWriter in = null;
			try {
				in = new BufferedWriter(new FileWriter(path + "studentDb.txt",
						true));
				in.newLine();
				in.write(append);
				in.flush();

				Database d = new Database("showAllStudent", client, request);

				return d.executeUpdate("select * from Students");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				return false;
			} finally {
				in.close();
			}

		}

		if (requestMethod.contains("updateStudent")) {

		}

		return flag;

	}
}
