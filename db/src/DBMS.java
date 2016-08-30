

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

public interface DBMS {
	/**
	 * @return 
	 * @throws TransformerException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * 
	 */
	public String CreateDatabase(String dataBaseName) throws IOException, ParserConfigurationException, SAXException, TransformerException;

	/**
	 * @return 
	 * @throws TransformerException
	 * @throws ParserConfigurationException
	 * @throws IOException 
	 * @throws SAXException 
	 * 
	 */
	public String CreateTable(String tableName, String[][] attributes)
			throws ParserConfigurationException, TransformerException, SAXException, IOException;

	/**
	 * @return 
	 * 
	 */

	public String insertRow(String tableName, String[] fields, String[] values);

	// load table and insert in it and save then print table
	public String update(String tableName, String[] fields, String[] values);

	public String update(String tableName, String[] fields_to_be_updated,
			String[] values_to_be_updated, String[] fields_condition,
			String[] values_condition, int comp);

	// load table and update in it and save then print table
	public String delete(String tableName, String[] fields, String[] values,
			int comp);

	// load table and delete in it and save then print table
	public String execute(String SQLCommand)
			throws ParserConfigurationException, TransformerException,
			SAXException, IOException;

	/**
	 * 
	 */
	public void loadDatabase(String dataBaseName);// use command

	public Database getCurrentDataBase();

	boolean findDataBase(String dataBaseName);// use command

	boolean findTable(String tableName);

	public String selectAllTable(String tableName)
			throws ParserConfigurationException, TransformerException,
			SAXException, IOException;

	public String setcurrentDataBase(String str);

	public String selectColumnTable(String tableName, String[] columnName)
			throws ParserConfigurationException, TransformerException,
			SAXException, IOException;

	public String selectColumn(String tableName, String[] columnName,
			String[] condition) throws ParserConfigurationException,
			TransformerException, SAXException, IOException;

}
