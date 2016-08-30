/**
 * 
 */

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

/**
 * 
 */
public class Database implements DatabaseInterface{

	private String dataBaseName;
	ArrayList<String> tableNames;

	public Database(String dataBaseName) {
		this.dataBaseName = dataBaseName;
		tableNames = new ArrayList<String>();
	}

	public String createTable(String name, String attribute[][])
			throws ParserConfigurationException, TransformerException,
			SAXException, IOException {
		boolean search = FileManager.findTable(dataBaseName, name);
		if (!search) {
			tableNames.add(name);
			Table t = new Table(name);
			for (int i = 0; i < attribute.length; i++) {
				int key = 0;
				if (attribute[i][1].equals("integer")) {
					key = 1;
				} else if (attribute[i][1].equals("character")) {
					key = 2;
				} else if (attribute[i][1].equals("string")) {
					key = 0;
				}
				t.addNewField(new Field(attribute[i][0], key));
			}
			FileManager.saveTable(dataBaseName, t);
			return "table created";
		}
		return "table already exists";

	}

	public String insertRow(String tableName, String[] fields, String[] values)
			throws ParserConfigurationException, TransformerException,
			SAXException, IOException {
		// load table and insert in it and save then print table
		boolean search = FileManager.findTable(dataBaseName, tableName);
		if (search) {
			Table my_table = FileManager.loadTable(dataBaseName, tableName);
			my_table.insertRow(fields, values);
			System.out.println(my_table);
			FileManager.saveTable(dataBaseName, my_table);
			return "row inserted";
		}
		return "table not found";
	}

	public String update(String tableName, String[] fields, String[] values)
			throws ParserConfigurationException, TransformerException,
			SAXException, IOException {
		// load table and update in it and save then print table
		boolean search = FileManager.findTable(dataBaseName, tableName);
		if (search) {
			Table my_table = FileManager.loadTable(dataBaseName, tableName);
			my_table.update(fields, values);
			System.out.println(my_table);
			FileManager.saveTable(dataBaseName, my_table);
			return "update done";
		}
		return "table not found";
	}

	public String update(String tableName, String[] fields_to_be_updated,
			String[] values_to_be_updated, String[] fields_condition,
			String[] values_condition, int comp)
			throws ParserConfigurationException, TransformerException,
			SAXException, IOException {
		// load table and update in it and save then print table
		boolean search = FileManager.findTable(dataBaseName, tableName);
		if (search) {
			Table my_table = FileManager.loadTable(dataBaseName, tableName);
			Table result = my_table.selectRows(fields_condition[0],
					values_condition[0], comp);
			my_table.update(fields_to_be_updated, values_to_be_updated, result);
			System.out.println(my_table);
			FileManager.saveTable(dataBaseName, my_table);
			return "update done";
		}
		return "table not found";
	}

	public String delete(String tableName, String[] fields, String[] values,
			int comp) throws ParserConfigurationException,
			TransformerException, SAXException, IOException {

		// load table and delete in it and save then print table
		boolean search = FileManager.findTable(dataBaseName, tableName);
		if (search) {
			if (fields == null && values == null) {
				Table my_table = FileManager.loadTable(dataBaseName, tableName);
				my_table.delete(my_table);
				System.out.println(my_table);
				FileManager.saveTable(dataBaseName, my_table);
				return "deletion done";
			} else {
				Table my_table = FileManager.loadTable(dataBaseName, tableName);
				Table result = my_table.selectRows(fields[0], values[0], comp);
				my_table.delete(result);
				System.out.println(my_table);
				FileManager.saveTable(dataBaseName, my_table);
				return "deletion done";
			}

		}
		return "table not found";
	}

	public Table getTable(String tableName)
			throws ParserConfigurationException, TransformerException,
			SAXException, IOException {
		return FileManager.loadTable(dataBaseName, tableName.trim());

	}

	public String selectAllTable(String tableName)
			throws ParserConfigurationException, TransformerException,
			SAXException, IOException {
		Table t = FileManager.loadTable(dataBaseName, tableName);
		System.out.println(t);
		return "table selected";
	}

	public String selectColumnTable(String tableName, String[] columnName)
			throws ParserConfigurationException, TransformerException,
			SAXException, IOException {// select (col_1,col_2) from table_name
		Table tab = FileManager.loadTable(dataBaseName, tableName);
		Table t = tab.selectFields(columnName);
		System.out.println(t);
		return "table selected";
	}

	public String selectColumn(String tableName, String[] columnName,
			String[] condition) throws ParserConfigurationException,
			TransformerException, SAXException, IOException {// select
																// (col_1,col_2)
																// from
																// table_name
																// where
																// (col_1=value_1)
		Table tab = FileManager.loadTable(dataBaseName, tableName);
		Table t = tab.selectRows(condition[0], condition[2],
				Integer.parseInt(condition[1]));
		Table x = t.selectFields(columnName);
		System.out.println(x);
		return "table selected";
	}
}
