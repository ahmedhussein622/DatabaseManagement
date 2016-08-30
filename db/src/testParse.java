import static org.junit.Assert.*;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.junit.Test;
import org.xml.sax.SAXException;

public class testParse {

	@Test
	public void testExecute() throws ParserConfigurationException,
			TransformerException, SAXException, IOException {
		DBMS dbms = new MyDBMS();
		dbms.CreateDatabase("test");
		assertEquals(dbms.execute("create database test"),
				"database already exist"); // no duplicate database names
		assertEquals(dbms.execute("use test"), "test in use");
		dbms.CreateTable("test", new String[][] { { "name", "string" },
				{ "age", "integer" } });
		assertEquals(
				dbms.execute("create table test (name string,age integer)"),
				"table already exists"); // Table already exists
		assertEquals(dbms.execute("insert into z values (4,5,6)"),
				"table not found"); // not valid query because table doesn't
									// exist
		assertEquals(dbms.execute("insert into test values (mohamed,5)"),
				"row inserted");
		assertEquals(dbms.execute("delete * from test"), "deletion done");
		assertEquals(dbms.execute("insert into test values (ali,6)"),
				"row inserted");
		assertEquals(dbms.execute("insert into test values (ahmed,9)"),
				"row inserted");
		assertEquals(dbms.execute("insert into test values (mohamed,9)"),
				"row inserted");
		assertEquals(dbms.execute("select (age) from test"), "table selected");
		assertEquals(
				dbms.execute("update test set (name=hassan) where (age=9)"),
				"update done");
		assertEquals(dbms.execute("select (name) from test where (age=9)"),
				"table selected");
		assertEquals(dbms.execute("delete from test where (age=9)"),
				"deletion done");
	}
}
