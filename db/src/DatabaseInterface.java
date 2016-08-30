import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;


public interface DatabaseInterface {
	
	
	
	public String createTable(String name, String attribute[][])
			throws ParserConfigurationException, TransformerException,
			SAXException, IOException ;
				
	public String insertRow(String tableName, String[] fields, String[] values)
			throws ParserConfigurationException, TransformerException,
			SAXException, IOException ;

	public String update(String tableName, String[] fields, String[] values)
			throws ParserConfigurationException, TransformerException,
			SAXException, IOException;

	public String update(String tableName, String[] fields_to_be_updated,
			String[] values_to_be_updated, String[] fields_condition,
			String[] values_condition, int comp)
			throws ParserConfigurationException, TransformerException,
			SAXException, IOException ;

	public String delete(String tableName, String[] fields, String[] values,
			int comp) throws ParserConfigurationException,
			TransformerException, SAXException, IOException ;
	public Table getTable(String tableName)
			throws ParserConfigurationException, TransformerException,
			SAXException, IOException ;

	public String selectAllTable(String tableName)
			throws ParserConfigurationException, TransformerException,
			SAXException, IOException ;

	public String selectColumnTable(String tableName, String[] columnName)
			throws ParserConfigurationException, TransformerException,
			SAXException, IOException;

	public String selectColumn(String tableName, String[] columnName,
			String[] condition) throws ParserConfigurationException,
			TransformerException, SAXException, IOException;

}
