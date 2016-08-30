import java.io.IOException;
import java.sql.Statement;
import java.util.Scanner;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;
public class Main {
	@SuppressWarnings("resource")
	public static void main(String[] args) throws ParserConfigurationException,
			TransformerException, SAXException, IOException {
		DBMS databaseManeger = new MyDBMS();
		String a;
		Scanner scan = new Scanner(System.in);
		FileManager.createDirectory();
		System.out.println("-----Welcome To DBMS Program-----");
		while (true) {
			a = scan.nextLine();
			if (a.equalsIgnoreCase("exit"))
				System.exit(0);
			try {
				System.out.println(databaseManeger.execute(a));
			} catch (Exception e) {
				System.out.println("execute failed");
			}
		}
	}
}
