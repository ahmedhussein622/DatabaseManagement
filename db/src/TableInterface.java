import java.util.ArrayList;


public interface  TableInterface {

	
	public void addNewField(Field field);
	public void removeField(String fieldName);
	public boolean containsFiled(String fieldName);
	public boolean checkData(String[] fields, String[] values);
	public void insertRow(String[] fields, String[] values);
	public void update(String[] fields, String[] values);
	public void update(String[] fields, String[] values, Table t);
	public Table selectRows(String fieldName, String value, int comp);
	public Table selectFields(String[] fields);
	public void delete(Table t);
	public void concatinate(Table t);
	public ArrayList<Field> getFields();
	public ArrayList<String> startRowLooping();
	public ArrayList<String> getNextRow();
	public int getRowsNumber();
	public int getColumnsNumber();
	
}

	