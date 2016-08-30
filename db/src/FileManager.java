
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;

public class FileManager {
	private static String DBMS_Directory = "";
	
	public static boolean findDataBase(String name){
		File[] listOfFiles = (new File(DBMS_Directory.substring(0, DBMS_Directory.length()-1))).listFiles();
		for(int i = 0; i < listOfFiles.length; i++){
		    if(listOfFiles[i].getName().equals(name)){
		    	return true;
		    }
		}
		return false;
	}
	
	public static boolean findTable(String dataBaseName, String tableName){
		String URL = DBMS_Directory + dataBaseName;
		File[] listOfFiles = (new File(URL)).listFiles();
		for(int i = 0; i < listOfFiles.length; i++){
		    if(listOfFiles[i].getName().equals(tableName + ".XML")){
		    	return true;
		    }
		}
		return false;
	}
	
	public static void saveTable(String dataBaseName,Table table) throws ParserConfigurationException, TransformerException, IOException, SAXException{
		String URL = DBMS_Directory + dataBaseName + "/" + table.getTableName()  + ".XML";
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
		Document doc = docBuilder.newDocument();
		
		parseTable(table , doc);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(URL));
		transformer.transform(source, result);
		saveDataBase(dataBaseName);
		DTDGenerator app = new DTDGenerator();
        app.run(URL);
        app.printDTD(DBMS_Directory + dataBaseName + "/" + table.getTableName()+".dtd");
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));;
	}
	
	private static void parseTable(Table table , Document doc){
		
		Element rootElement = doc.createElement("Table");
		doc.appendChild(rootElement);
		rootElement.setAttribute("RowNumber", Integer.toString(table.getRowsNumber()));
		rootElement.setAttribute("ColumnNumber", Integer.toString(table.getColumnsNumber()));
		ArrayList<Field> fields = table.getFields();
		saveTableFields(table , doc , fields , rootElement);
		saveTableRecords(table , doc , fields , rootElement);
		
	}
	
	private static void saveTableFields(Table table , Document doc , ArrayList<Field> fields , Element rootElement){
		Element tableFields = doc.createElement("TableFields");
		
		for(int i = 0 ; i < table.getColumnsNumber() ; i++){
			Element Field = doc.createElement(fields.get(i).getFieldName());//fields.get(j).getFieldName()
			Field.setAttribute("type", Integer.toString(fields.get(i).getType()));//fields.get(j).getType()
			tableFields.appendChild(Field);
		}
		
		rootElement.appendChild(tableFields);
	}
	
	private static void saveTableRecords(Table table , Document doc , ArrayList<Field> fields , Element rootElement){
		Element tableRecords = doc.createElement("TableRecords");
		ArrayList<String> X;
		for(int i = 0 ; i < table.getRowsNumber() ; i++){		
			Element record = doc.createElement("record");
			if(i == 0){
				X = table.startRowLooping();
			}else{
				X = table.getNextRow();
			}
			for(int j = 0 ; j < table.getColumnsNumber() ; j++){
				if(X.get(j) != null){
					Element Field = doc.createElement(fields.get(j).getFieldName());
					Field.appendChild(doc.createTextNode(X.get(j)));
					record.appendChild(Field);
				}
			}	
			tableRecords.appendChild(record);
	 	}
		rootElement.appendChild(tableRecords);
	}
	
	public static Table loadTable(String dataBaseName,String tableName) throws IOException, ParserConfigurationException, SAXException{
		String URL = DBMS_Directory + dataBaseName + "/" + tableName +".XML"; 
		Table table = new Table(tableName);
		File fXmlFile = new File(URL);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();
		Element rootElement = doc.getDocumentElement();
		loadTableFields(table , doc ,rootElement);
		loadTableRecords(table , doc ,rootElement);
		return table;
	}
	
	private static void loadTableFields(Table table , Document doc , Element rootElement){
		NodeList tableFields = doc.getElementsByTagName("TableFields").item(0).getChildNodes();
		
		String[] fields = new String[Integer.parseInt(rootElement.getAttribute("ColumnNumber"))];
		for(int i = 0 ; i < Integer.parseInt(rootElement.getAttribute("ColumnNumber")) ; i++){
			Node subNode = tableFields.item(i);
			if (subNode.getNodeType() == Node.ELEMENT_NODE) {
				Element subElement = (Element) subNode;
				fields[i]  = subElement.getNodeName();
				table.addNewField(new Field(fields[i] , Integer.parseInt(subElement.getAttribute("type"))));		
			}
		}
	}
	
	private static void loadTableRecords(Table table , Document doc , Element rootElement){
		NodeList tableRecords = doc.getElementsByTagName("record");
		for(int i = 0 ; i < Integer.parseInt(rootElement.getAttribute("RowNumber")) ; i++){
			Node nNode = tableRecords.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				loadNewRecord(table, tableRecords, rootElement, (Element) nNode);
			}
		}
	}
	
	private static void loadNewRecord(Table table , NodeList tableRecords , Element rootElement , Element eElement){
		NodeList childList = eElement.getChildNodes();
		String[] values = new String[childList.getLength()];
		String[] fields = new String[childList.getLength()];
		for(int j = 0 ; j < childList.getLength() ; j++){
			Node subNode = childList.item(j);
			if (subNode.getNodeType() == Node.ELEMENT_NODE) {
				Element subElement = (Element) subNode;
				values[j]  = subElement.getFirstChild().getTextContent();
				fields[j]  = subElement.getNodeName();
			}		
		}	
		table.insertRow(fields,values);
	}
		
	public static String CreateDataBase(String name) throws IOException, ParserConfigurationException, SAXException, TransformerException{
		boolean dirFlag = false;
		String URL = DBMS_Directory + name;
		try {
		   dirFlag = new File(URL).mkdir();
		} catch (SecurityException Se) {
		return "Error while creating directory in Java";
		}
		saveDataBase(name);
		if (dirFlag)
		   return "Directory created successfully";
		else
		   return "Directory was not created successfully";
	}
	
	
	

	public static String createDirectory() {
		String s = System.getProperty("user.home") + "/Desktop";
		DBMS_Directory = s + "/DBMS/";
		

		s = s.replace("\\", "/");
		File[] listOfFiles = (new File(s).listFiles());
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].getName().equals("DBMS")) {
				return "Directory exists";
			}
		}
		s = s + "/DBMS";
		boolean dirFlag = false;
		try {
			dirFlag = new File(s).mkdir();
		} catch (SecurityException Se) {
			return "Error while creating directory in Java";
		}
		if (dirFlag)
			return "Directory of database created successfully";
		else
			return "Directory of database was not created successfully";
	}

	

	private static void saveDataBase(String name) throws IOException, ParserConfigurationException, SAXException, TransformerException{
		String URL = DBMS_Directory + name;
		File[] listOfFiles = (new File(URL)).listFiles();
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		getFields(doc, name, listOfFiles);
		URL = URL + "/schema.XML";
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(URL));
		transformer.transform(source, result);
	}
	private static void getFields(Document doc , String name , File[] listOfFiles) throws IOException, ParserConfigurationException, SAXException{
		Element rootElement = doc.createElement("DataBase");
		rootElement.setAttribute("name",name);
		doc.appendChild(rootElement);
		for(int i = 0; i < listOfFiles.length; i++){
		    if(!listOfFiles[i].getName().equals("schema.XML") && !listOfFiles[i].getName().endsWith(".dtd")){
		    	Table table = loadTable(name, listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length()-4));
		    	Element newTable = doc.createElement("Table");
				rootElement.appendChild(newTable);
				newTable.setAttribute("name",table.getTableName());
		    	ArrayList<Field> fields = table.getFields();
		    	for(int j = 0 ; j < fields.size() ; j++){
					Element Field = doc.createElement(fields.get(j).getFieldName());//fields.get(j).getFieldName()
					Field.setAttribute("type", Integer.toString(fields.get(j).getType()));//fields.get(j).getType()
					newTable.appendChild(Field);
				}
		    }
		}
	}
}
