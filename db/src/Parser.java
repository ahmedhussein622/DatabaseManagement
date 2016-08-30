/**
 * 
 */

import java.awt.PageAttributes.OriginType;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

/**
 * 
 */
public class Parser {
	public static String execute(DBMS dbms, String command)throws ParserConfigurationException, TransformerException,SAXException, IOException {
		String arr[] = command.split(" ");
		if (arr.length >= 2) {
			if (arr[0].equalsIgnoreCase("CREATE")) {// mahmoud
				return creation(dbms, arr);
			} else if (arr[0].equalsIgnoreCase("USE")) {// mahmoud(update)
				return use(dbms, arr);
			} else if (arr[0].equalsIgnoreCase("INSERT")&& dbms.getCurrentDataBase() != null) {// hegazy
				return insertion(dbms, command);
			} else if (arr[0].equalsIgnoreCase("UPDATE")&& dbms.getCurrentDataBase() != null) {// hegazy
				return update(dbms, command);
			} else if (arr[0].equalsIgnoreCase("SELECT")&& dbms.getCurrentDataBase() != null) {// mahmoud
		        return selection(dbms, arr, command);
			} else if (arr[0].equalsIgnoreCase("DELETE")&& dbms.getCurrentDataBase() != null) {// hegazzy
				return deletion(dbms, command);
			}
		}
		return "not valid";
	}
	public static String deletion(DBMS dbms, String command) {
		String split_low[] = command.toLowerCase().split(" ");
		String split_original[] = command.split(" ");
		if (split_low.length == 4 && split_low[2].equals("from")&& split_low[1].equals("*")) {
			String tableName = split_original[3];
			return dbms.delete(tableName, null, null, 0);
		} else if (split_low.length > 4 && split_low[1].equals("from")&& split_low[3].equals("where") && checkOnePair(command)) {
			String tableName = split_original[2];
			String res[] = evaluateCondition(command.substring(command.indexOf('('), command.indexOf(')') + 1));
			return dbms.delete(tableName, new String[] { res[0] },new String[] { res[2] }, Integer.parseInt(res[1]));
		}
		return "not valid";
	}
	private static String update(DBMS dbms, String command) {
		// TODO Auto-generated method stub
		String split_low[] = command.toLowerCase().split(" ");
		String split_original[] = command.split(" ");
		if (split_low.length == 4 && split_low[2].equals("set")&& checkOnePair(command)) {
			return updateWithoutSelection(split_original, command, dbms);
		} else if (split_low.length > 4 && split_low[2].equals("set")&& checkTwoPair(command) && split_low[4].equals("where")) {
			return updateWithSelection(split_original, command, dbms);
		}
		return "not valid";
	}
	private static String insertion(DBMS dbms, String command) {
		String split_low[] = command.toLowerCase().split(" ");
		String split_original[] = command.split(" ");
		if (split_low.length > 4 && checkOnePair(command)&& split_low[3].equals("values") && split_low[1].equals("into")) {
			String tableName = split_original[2];
			String values = command.substring(command.indexOf('(') + 1,command.indexOf(')'));
			String[] attributes = values.split(",");
			return dbms.insertRow(tableName, null, attributes);
		} else if (split_low.length > 5 && checkTwoPair(command)&& split_low[4].equals("values") && split_low[1].equals("into")) {
			String tableName = split_original[2];
			String cols = command.substring(command.indexOf('(') + 1,command.indexOf(')'));
			String columns[] = cols.split(",");
			String without_columns = command.replace(command.substring(command.indexOf('('),command.indexOf(')') + 1), "");
			String vals = without_columns.substring(without_columns.indexOf('(') + 1,without_columns.indexOf(')'));
			String values[] = vals.split(",");
			return dbms.insertRow(tableName, columns, values);
		}
		return "not valid";
	}
	private static boolean checkOnePair(String command) {
		String temp = command.replace("(", "");
		String temp2 = command.replace(")", "");
		if (command.length() - temp.length() != 1
				&& command.length() - temp2.length() != 1)
			return false;
		return true;
	}

	private static boolean checkTwoPair(String command) {
		String temp = command.replace("(", "");
		String temp2 = command.replace(")", "");
		if (command.length() - temp.length() != 2
				&& command.length() - temp2.length() != 2)
			return false;
		return true;
	}
	private static String updateWithSelection(String split_original[],
			String command, DBMS dbms) {
		String tableName = split_original[1];
		String temp1 = command.substring(command.indexOf('(') + 1,command.indexOf(')'));
		String temp[] = command.substring(command.indexOf('(') + 1,command.indexOf(')')).split(",");
		String attribute[][] = new String[temp.length][2];
		for (int i = 0; i < temp.length; i++) {
			String args[] = temp[i].split("=");
			if (args.length == 2) {
				attribute[i][0] = args[0];
				attribute[i][1] = args[1];
			} else {
				return "not valid";
			}
		}
		command = command.replace("(" + temp1 + ")", "");
		String res[] = evaluateCondition(command.substring(command.indexOf('('), command.indexOf(')') + 1));
		return dbms.update(tableName, getFieldColumn(attribute),getValueColumn(attribute), new String[] { res[0] },new String[] { res[2] }, Integer.parseInt(res[1]));
	}
	private static String updateWithoutSelection(String split_original[],String command, DBMS dbms) {
		String tableName = split_original[1];
		String temp[] = command.substring(command.indexOf('(') + 1,command.indexOf(')')).split(",");
		String attribute[][] = new String[temp.length][2];
		for (int i = 0; i < temp.length; i++) {
			String args[] = temp[i].split("=");
			if (args.length == 2) {
				attribute[i][0] = args[0];
				attribute[i][1] = args[1];
			} else {
				return "not valid";
			}
		}
		return dbms.update(tableName, getFieldColumn(attribute),
				getValueColumn(attribute));
	}

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 *
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * */

	private static String selection(DBMS dbms, String arr[], String str)throws ParserConfigurationException, TransformerException,SAXException, IOException {
		if (arr[1].equals("*") && arr[2].equalsIgnoreCase("FROM")
				&& arr.length == 4) {
			String tableName = arr[3];
//			boolean cont = false;
//			for (int i = 0; i < dbms.getCurrentDataBase().tableNames.size()
//					&& !cont; i++) {
//				if (tableName.equals(dbms.getCurrentDataBase().tableNames
//						.get(i)))
//					cont = true;
//			}
//			if (cont = true) {
				return dbms.selectAllTable(tableName);
//			}
		} else if (str.contains("from") && str.contains("where")) {
			return specifiedSelect(dbms, str);
		} else if (str.contains("from") && !str.contains("where")) {
			return selectColumn(dbms, str);
		}
			return "not valid";
	}

	private static String selectColumn(DBMS dbms, String command)throws ParserConfigurationException, TransformerException,SAXException, IOException {
		String str = command.replaceFirst("select", "");
		str = str.replaceFirst("from", "*");
		String arr[] = str.split("\\*");
		arr[1] = arr[1].trim();
		boolean cont = true;
		if (str.contains("from") || str.contains("select") && arr.length == 2) {
			return "not valid";
		} else {
			String columnName[] = findColumn(arr[0]);
			String tableName = arr[1];
			if (columnName != null)
				return dbms.selectColumnTable(tableName, columnName);// /////////////////////////////////////////////////////////////////////////////////////////////////////////////
			else{
				return "not valid";
			}
		}
	}

	private static String specifiedSelect(DBMS dbms, String command)throws ParserConfigurationException, TransformerException,SAXException, IOException {
		String str = command.replaceFirst("select", "");
		str = str.replaceFirst("from", "*");
		str = str.replaceFirst("where", "*");
		String arr[] = str.split("\\*");
		arr[1] = arr[1].trim();
		boolean cont = true;
		if (str.contains("from") || str.contains("select")|| str.contains("where") && arr.length == 3) {
			return "not valid";
		} else {
			String columnName[] = findColumn(arr[0]);
			String tableName = arr[1];
			String condition[] = evaluateCondition(arr[2]);
			if (columnName != null && condition != null)
				return dbms.selectColumn(tableName, columnName, condition);//
			else
				return "not valid";
		}
	}

	private static String[] findColumn(String str) {
		boolean cont = true;
		String condition = "";
		try {
			if (str.indexOf('(') > 0)
				condition = str.substring(str.indexOf('(') + 1,
						str.lastIndexOf(')'));
			else
				condition = str.substring(str.indexOf('('),
						str.lastIndexOf(')'));
			condition = condition.replaceAll(" ", "");
			String column[] = condition.split(",");
			return column;
		} catch (Exception e) {
			cont = false;
			// notvalid();
		}
		return null;
	}

	private static String use(DBMS dbms, String arr[]) {
		String str = arr[1];
		for (int i = 3; i < arr.length; i++)
			str = str + " " + arr[i];
		if (!dbms.findDataBase(str)) {
			return "not valid";
		} else {
			return dbms.setcurrentDataBase(str);
		}
	}

	private static String[] getFieldColumn(String[][] attribute) {
		String res[] = new String[attribute.length];
		for (int i = 0; i < attribute.length; i++) {
			res[i] = attribute[i][0];
		}
		return res;
	}

	private static String[] getValueColumn(String[][] attribute) {
		String res[] = new String[attribute.length];
		for (int i = 0; i < attribute.length; i++) {
			res[i] = attribute[i][1];
		}
		return res;
	}

	private static String[] evaluateCondition(String str) {
		boolean cont = true;
		try {
			str = str.substring(str.indexOf('(') + 1, str.lastIndexOf(')'));
		} catch (Exception e) {
			// System.out.println("not valid1");
			cont = false;
		}
		str = str.replaceAll(" ", "");
		if (str.contains("=")) {
			str = str.replaceAll("=", " 0 ");
		} else if (str.contains(">")) {
			str = str.replaceAll(">", " 1 ");
		} else if (str.contains("<")) {
			str = str.replaceAll("<", " -1 ");
		}
		String[] array = str.split(" ");
		if (array.length == 3 && cont)
			return array;
		return null;
	}
	private static String creationOfTable(DBMS dbms, String str, String table)throws ParserConfigurationException, TransformerException,SAXException, IOException {
		boolean cont = true;
		String condition = "";
		try {
			condition = str.substring(str.indexOf('(') + 1,
					str.lastIndexOf(')'));
		} catch (Exception e) {
			cont = false;
			// notvalid();
		}
		String temp[] = condition.split(",");
		String attribute[][] = new String[temp.length][2];
		cont = constructAttribute(attribute, temp, cont);
		if (!checkDuplicate(temp) && cont && checkExistance(dbms, str)) {
			return dbms.CreateTable(table, attribute);
		} else {
			return "not valid";
		}
	}

	private static boolean checkExistance(DBMS dbms, String str) {
		String arr[] = str.split(" ");
		if (arr.length < 3)
			return false;

		String tableName = arr[2];
		for (int i = 0; i < dbms.getCurrentDataBase().tableNames.size(); i++) {
			if (tableName.equals(dbms.getCurrentDataBase().tableNames.get(i)))
				return false;
		}
		return true;
	}

	private static boolean constructAttribute(String[][] attribute,
			String[] temp, boolean cont) {
		for (int i = 0; i < temp.length && cont; i++) {
			String args[] = temp[i].split(" ");
			if (args.length == 2) {
				try {
					attribute[i][0] = args[0];
					attribute[i][1] = args[1];
					temp[i] = attribute[i][0];
				} catch (Exception e) {
					cont = false;
				}
			} else {
				cont = false;
			}
		}
		return cont;
	}

	private static String creationOfDataBase(DBMS dbms, String[] arr) throws IOException, ParserConfigurationException, SAXException, TransformerException {
		String str = arr[2];
		for (int i = 3; i < arr.length; i++)
			str = str + " " + arr[i];
		if (dbms.findDataBase(str)) {
			return "database already exist";
		} else {
			return dbms.CreateDatabase(str);
		}
	}
	private static boolean checkDuplicate(String[] arr) {// check the duplicate
															// in the condition
		for (int i = 0; i < arr.length - 1; i++)
			for (int j = i + 1; j < arr.length; j++)
				if (arr[i].equals(arr[j]))
					return true;
		return false;
	}
	private static String creation(DBMS dbms, String[] arr)throws ParserConfigurationException, TransformerException,SAXException, IOException {
		if (arr[1].equalsIgnoreCase("Table") && dbms.getCurrentDataBase()!=null) {
			String str = arr[0];
			for (int i = 1; i < arr.length; i++)
				str = str + " " + arr[i];
			return creationOfTable(dbms, str, arr[2]);
		} else if (arr[1].equalsIgnoreCase("Database")) {
			return creationOfDataBase(dbms, arr);

		} else {
			return "not valid";
		}
	}
}
