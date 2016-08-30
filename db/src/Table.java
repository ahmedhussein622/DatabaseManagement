
import java.util.ArrayList;

/**
 * @author ahmed
 * 
 */
public class Table implements TableInterface {

	public static final int bigger = 1;
	public static final int smaller = -1;
	public static final int equale = 0;
	public static final int unequale = -2;

	private String tableName;

	private int rowsNumber;
	private int columnsNumber;
	private ArrayList<ArrayList<String>> rows;
	private ArrayList<Field> fields;
	private Table parentTable;
	private int fieldNum;
	private int rowNumberForLooping;

	public Table(String tableName) {
		this.tableName = tableName;
		rows = new ArrayList<ArrayList<String>>();
		fields = new ArrayList<Field>();

		columnsNumber = 0;
		rowsNumber = 0;
		parentTable = null;
		rowNumberForLooping = -1;
	}

	/**
	 * add new field to table a new empty string is added in each row to keep
	 * the order if a new element is added it will be added in it's correct
	 * order
	 * 
	 * @throws if
	 *             field already exists exception will be thrown
	 * @param filed
	 *            to added
	 * */
	public void addNewField(Field field) {
		if (containsFiled(field.getFieldName())) {// check if the field already
													// exists
			throw new IllegalArgumentException();// throw illegal argument
													// exception
		}
		fields.add(field);
		columnsNumber++;

		for (int i = 0; i < rows.size(); i++) {// add null to save location for
												// the field
			rows.get(i).add(null);
		}
	}

	/**
	 * removes a field it also removes all its values from rows
	 * 
	 * @param field
	 * @throws illegal
	 *             argument exception if field doesn't exist
	 */
	public void removeField(String fieldName) {

		int columNumber = getFieldNumber(fieldName);
		if (columNumber == -1) {// no such a field then throw illegal argument
								// exception
			throw new IllegalArgumentException();
		}

		fields.set(columNumber, null);
		columnsNumber--;

	}

	/**
	 * check if a filed is in the table
	 * 
	 * @param fieldName
	 *            the field to check
	 * @return true if the field exists in table
	 */
	public boolean containsFiled(String fieldName) {
		if (getFieldNumber(fieldName) != -1)
			return true;

		return false;
	}

	/**
	 * check if fields exist and check if values match the type of corresponding
	 * fields in this case order of fields doesn't matter. if the fields
	 * parameter is null then values will be checked for all the values in the
	 * in order corresponding to fields in order too, it values length is bigger
	 * than Fields number false will be returned false will be returned
	 * 
	 * @param fields
	 *            to be checked
	 * @param values
	 *            the values corresponding to fields
	 * @return true iff the all fields exist and their the values are valid for
	 *         them
	 */
	public boolean checkData(String[] fields, String[] values) {
		if (values == null)
			return false;
		if (values.length > columnsNumber)// number of values it bigger than
											// number of fields
			return false;

		if (fields == null) {// validate for all fields in order
			int i = getFirstField();
			int j = 0;
			while (i != -1) {
				if (!this.fields.get(i).check(values[j]))// check for valid data
															// for every field
					return false;
				i = getNextField();
				j++;
			}
		}

		else {// there is specific fields to check order doesn't matter here
			if (fields.length != values.length)// number of fields doesn't equal
												// numbers of values to be
												// checked
				return false;
			int fieldLoc;
			for (int i = 0; i < fields.length; i++) {
				fieldLoc = getFieldNumber(fields[i]);
				if (fieldLoc == -1)// no such a field
					return false;

				if (!this.fields.get(fieldLoc).check(values[i]))// value is not
																// valid for
																// that field
					return false;
			}
		}

		// check is done and every thing is correct
		return true;
	}

	/**
	 * insert new row in the table . fields that are not specified will be
	 * filled with null if fields is null then strings will be filed in order
	 * 
	 * @param fields
	 *            , the fields corresponding to the values. null if values to be
	 *            added in order
	 * @param values
	 *            , values to be added in the new row
	 * @throws illegale
	 *             argument exception if parameter have something wrong
	 */

	public void insertRow(String[] fields, String[] values) {
		if (!checkData(fields, values)) {// check if the data is valid
			throw new IllegalArgumentException();
		}
		// data is valid start inserting
		ArrayList<String> newRow = new ArrayList<String>();// the new row
		for (int i = 0; i < this.fields.size(); i++)
			// fill row with empty values
			newRow.add(null);

		if (fields == null) {// add values in order
			int i = getFirstField();
			while (i != -1) {
				newRow.set(i, values[i]);
				i = getNextField();
			}

		} else {// there is a specific fields
			int fieldLoc;
			// for(int i=0;i<this.fields.size();i++){
			// if(this.fields.get(i).getType()==Field.Int)
			// newRow.set(i, "0");
			// else{
			// newRow.set(i, " ");
			// }
			//
			// }
			for (int i = 0; i < fields.length; i++) {
				fieldLoc = getFieldNumber(fields[i]);
				newRow.set(fieldLoc, values[i]);
			}

		}

		rows.add(newRow);// add the new row the rows list
		rowsNumber++;
	}

	/**
	 * update fields of all rows in this table
	 * 
	 * @param fields
	 *            the fields to be updated
	 * @param values
	 *            new values to replace the previous ones
	 * @throws IllegalArgumentException
	 *             if fields or types miss match fields are checked by reference
	 *             not by value
	 */
	public void update(String[] fields, String[] values) {

		if (!checkData(fields, values))
			throw new IllegalArgumentException("fields or types don't match");

		int j;

		for (int i = 0; i < rows.size() && this.rows.size() > 0; i++) {
			for (j = 0; j < fields.length; j++) {
				rows.get(i).set(getFieldNumber(fields[j]), values[j]);
			}

		}
	}

	/**
	 * update fields of all rows in this table that are in t table
	 * 
	 * @param fields
	 *            the fields to be updated
	 * @param values
	 *            new values
	 * @param t
	 *            : table that is a result of search from this table
	 * @throws IllegalArgumentException
	 *             if fields or types miss match fields are checked by reference
	 *             not by value
	 */
	public void update(String[] fields, String[] values, Table t) {
		if (this.fields.size() != t.fields.size())
			throw new IllegalArgumentException("fields don't match");
		if (!checkData(fields, values))
			throw new IllegalArgumentException("fields or types don't match");

		int j;
		for (int i = 0; i < t.rows.size() && this.rows.size() > 0; i++) {
			int x = this.rows.indexOf(t.rows.get(i));
			if (x == -1)
				continue;
			for (j = 0; j < fields.length; j++) {
				rows.get(x).set(getFieldNumber(fields[j]), values[j]);
			}
		}
	}

	/**
	 * returns a table that has references of rows and fields which satisfy the
	 * selected operations so changing any value in it will case that value to
	 * be changed in all other tables that hold reference to same row, removing
	 * a row or column is OK >>>> BE CAREFUL! <<<< the comparison is field value
	 * comp value for example selectRows(fieldName,value, Table.bigger) will
	 * return row such that the value of field of that row > value
	 * 
	 * @param fieldName
	 *            name of the field to select by
	 * @param value
	 *            the value to be compared
	 * @param comp
	 *            the operation to be performed >,<,=,!= specified by the static
	 *            numbers bigger,smaller,equal,unequal
	 * @return returns a table that has references of rows which satisfy the
	 *         selected operations
	 */
	public Table selectRows(String fieldName, String value, int comp) {
		if (!checkData(new String[] { fieldName }, new String[] { value })) {
			throw new IllegalArgumentException();// invalid data
		}
		Table result = new Table("result");
		result.columnsNumber = this.columnsNumber;
		for (int j = 0; j < fields.size(); j++) {
			result.fields.add(this.fields.get(j));
		}

		int fieldLoc = getFieldNumber(fieldName);// location of field to be
													// compared
		String toCompare;
		int compResult;
		Field field = fields.get(fieldLoc);// field to be compared
		for (int i = 0; i < rows.size(); i++) {
			toCompare = rows.get(i).get(fieldLoc);
			if (toCompare == null)
				continue;
			compResult = field.compare(toCompare, value);

			if (comp == compResult || comp == unequale && compResult != equale) {
				result.rows.add(this.rows.get(i));
				result.rowsNumber++;
			}

		}

		return result;
	}

	/**
	 * selects specific fields from the table the resulting table has references
	 * of the rows and fields of this table so any change will occur in all
	 * tables. removing a row or column is OK >>>> BE CAREFUL ! <<<<.
	 * 
	 * @param fields
	 *            : fields which will be selected in the resulting table
	 * @return table that contains the specified fields;
	 */
	public Table selectFields(String[] fields) {

		for (int i = 0; i < fields.length; i++) {// checking that the fields
													// exist
			if (getFieldNumber(fields[i]) == -1) {
				throw new IllegalArgumentException();
			}
		}

		Table result = new Table("result");
		result.rowsNumber = this.rowsNumber;
		result.columnsNumber = fields.length;

		for (int i = 0; i < rows.size(); i++)
			// copy rows references
			result.rows.add(this.rows.get(i));

		for (int i = 0; i < this.fields.size(); i++)
			result.fields.add(null);

		int loc;
		for (int i = 0; i < fields.length; i++) {
			loc = getFieldNumber(fields[i]);
			result.fields.set(loc, this.fields.get(loc));
		}

		return result;
	}

	/**
	 * delete rows from table
	 * 
	 * @param t
	 *            : table that is a result of search from this table
	 * @throws illegal
	 *             argument exception if fields miss match the fields are
	 *             checked by reference
	 */

	public void delete(Table t) {
		if (t == this) {
			this.rows = new ArrayList<ArrayList<String>>();
			this.rowsNumber = 0;
			return;
		}

		if (this.fields.size() != t.fields.size())
			throw new IllegalArgumentException("fields don't match");
		for (int i = 0; i < this.fields.size(); i++) {
			if (this.fields.get(i) != t.fields.get(i))
				throw new IllegalArgumentException("fields don't match");
		}

		for (int i = 0; i < t.rows.size() && this.rows.size() > 0; i++) {
			int x = this.rows.indexOf(t.rows.get(i));
			if (x == -1)
				continue;
			this.rows.remove(x);
			rowsNumber--;
		}

	}

	public void concatinate(Table t) {

	}

	public ArrayList<Field> getFields() {

		if (columnsNumber == 0)
			return null;

		ArrayList<Field> result = new ArrayList<Field>();

		int i = getFirstField();
		while (i != -1) {
			result.add(fields.get(i));
			i = getNextField();
		}
		return result;
	}

	public ArrayList<String> startRowLooping() {

		if (rows.size() == 0) {
			rowNumberForLooping = -1;
			return null;
		}

		ArrayList<String> result = new ArrayList<String>();
		if (columnsNumber == 0)
			return result;
		int j;
		ArrayList<String> currentRow;
		currentRow = rows.get(0);
		j = getFirstField();
		while (j != -1) {
			result.add(currentRow.get(j));
			j = getNextField();
		}

		rowNumberForLooping = 0;
		return result;
	}

	public ArrayList<String> getNextRow() {

		if (rowNumberForLooping == -1)
			return null;

		rowNumberForLooping++;
		if (rowNumberForLooping == rows.size()) {
			rowNumberForLooping = -1;
			return null;
		}

		ArrayList<String> result = new ArrayList<String>();
		int j;
		ArrayList<String> currentRow = rows.get(rowNumberForLooping);
		j = getFirstField();
		while (j != -1) {
			result.add(currentRow.get(j));
			j = getNextField();
		}

		return result;
	}

	public int getRowsNumber() {
		return rowsNumber;
	}

	public int getColumnsNumber() {
		return columnsNumber;
	}

	@Override
	public String toString() {

		if (columnsNumber == 0)// table is empty nothing to print
			return "Table is Empty";

		String result = "";
		String value;
		int maxWidth = 15;

		int i, j, m;

		i = getFirstField();
		while (i != -1) {

			value = fields.get(i).getFieldName();
			result += " ";
			for (j = 0; j < value.length() && j < maxWidth; j++) {
				result += value.charAt(j);

			}
			while (j < maxWidth) {
				result += " ";
				j++;
			}
			i = getNextField();

		}

		result += "\n";

		for (m = 0; m < rows.size(); m++) {
			i = getFirstField();
			while (i != -1) {

				value = rows.get(m).get(i);
				if (value == null) {

					for (j = 0; j < maxWidth + 1; j++)
						result += " ";
					i = getNextField();
					continue;
				}
				result += " ";
				for (j = 0; j < value.length() && j < maxWidth; j++) {
					result += value.charAt(j);

				}
				while (j < maxWidth) {
					result += " ";
					j++;
				}
				i = getNextField();
			}
			result += "\n";
		}

		return result;
	}

	public String getTableName() {
		return tableName;
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	// private functions - it's not your business

	private int getFieldNumber(String fieldName) {
		for (int i = 0; i < fields.size(); i++) {
			if (fields.get(i) == null)
				continue;
			if (fields.get(i).getFieldName().equals(fieldName))
				return i;
		}
		return -1;
	}

	private int getFirstField() {
		if (columnsNumber == 0) {
			fieldNum = -1;
			return -1;
		}

		for (int i = 0; i < fields.size(); i++) {
			if (fields.get(i) != null) {
				fieldNum = i;
				return i;
			}
		}
		return -1;
	}

	private int getNextField() {
		if (fieldNum == -1)
			return -1;

		for (int i = fieldNum + 1; i < fields.size(); i++) {
			if (fields.get(i) != null) {
				fieldNum = i;
				return i;
			}
		}

		fieldNum = -1;
		return -1;
	}

}// end of table class

