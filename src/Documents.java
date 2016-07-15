import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;

public class Documents {

	public void process(HttpExchangeRequest request, Socket client)
			throws IOException {

		PrintStream out = new PrintStream(client.getOutputStream());
		URL location = TinyHttpd2.class.getProtectionDomain().getCodeSource()
				.getLocation();
		String path = location.getFile().split("bin")[0];
		File file = new File(path + "" + "pdf.pdf");
		DataInputStream fin = new DataInputStream(new FileInputStream(file));

		try {

			out.flush();

			out.println(Command.send200());
			out.println("Date:" + new Date());
			out.println("Server:" + InetAddress.getLocalHost());

			out.println("Content-Type: application/pdf");

			int len = (int) file.length();
			out.println("Content-Length: " + len);
			out.println("Last-Modified: 39 minutes ago");
			if (request.getConnection() != null) {
				out.println("Connection: keep-alive");
			}
			out.println("");

			byte buf[] = new byte[len];
			fin.readFully(buf);
			out.write(buf, 0, len);

			out.flush();

		} finally {
			out.close();
		}

	}

}
