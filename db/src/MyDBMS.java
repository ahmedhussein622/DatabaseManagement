

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

public class MyDBMS implements DBMS {
	Database current;

	public MyDBMS() {
		current = null;
	}
	@Override
	public String CreateDatabase(String dataBaseName) throws IOException, ParserConfigurationException, SAXException, TransformerException {
		// TODO Auto-generated method stub
		return FileManager.CreateDataBase(dataBaseName);
	}
	@Override
	public String CreateTable(String tableName, String attributes[][])
			throws ParserConfigurationException, TransformerException, SAXException, IOException {
		// TODO Auto-generated method stub
		return this.getCurrentDataBase().createTable(tableName, attributes);
	}
	@Override
	public String insertRow(String tableName, String[] fields, String[] values) {
		// TODO Auto-generated method stub
		try {
			return current.insertRow(tableName, fields, values);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "row wasn't inserted";
	}

	@Override
	public String delete(String tableName, String[] fields, String[] values,
			int comp) {
		// TODO Auto-generated method stub
		try {
			return current.delete(tableName, fields, values, comp);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "row(s) wasn't deleted";
	}
	@Override
	public String execute(String SQLCommand)
			throws ParserConfigurationException, TransformerException,
			SAXException, IOException {
		return Parser.execute(this, SQLCommand);
	}

	@Override
	public void loadDatabase(String databaseName) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean findDataBase(String dataBaseName) {
		// TODO Auto-generated method stub
		return FileManager.findDataBase(dataBaseName);
	}

	@Override
	public boolean findTable(String tableName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String update(String tableName, String[] fields, String[] values) {
		// TODO Auto-generated method stub
		try {
			return current.update(tableName, fields, values);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "row(s) wasn't updated";
	}

	@Override
	public String update(String tableName, String[] fields_to_be_updated,
			String[] values_to_be_updated, String[] fields_condition,
			String[] values_condition, int comp) {
		// TODO Auto-generated method stub
		try {
			return current.update(tableName, fields_to_be_updated,
					values_to_be_updated, fields_condition, values_condition,
					comp);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "row(s) wasn't updated";
	}

	@Override
	public String selectColumnTable(String tableName, String[] columnName)
			throws ParserConfigurationException, TransformerException,
			SAXException, IOException {

		return this.getCurrentDataBase().selectColumnTable(tableName, columnName);

	}

	@Override
	public String selectColumn(String tableName, String[] columnName,
			String[] condition) throws ParserConfigurationException,
			TransformerException, SAXException, IOException {

		return this.getCurrentDataBase()
				.selectColumn(tableName, columnName, condition);

	}

	@Override
	public String selectAllTable(String tableName)
			throws ParserConfigurationException, TransformerException,
			SAXException, IOException {
		return this.getCurrentDataBase().selectAllTable(tableName);

	}

	@Override
	public Database getCurrentDataBase() {
		// TODO Auto-generated method stub
		return current;
	}

	@Override
	public String setcurrentDataBase(String str) {
		// TODO Auto-generated method stub
		current = new Database(str);
		return str+" in use";
	}

}