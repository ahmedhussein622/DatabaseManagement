/**
 * 
 */


/**
 * @author ahmed
 * 
 */
public class Field {

	public static final int string = 0;
	public static final int Int = 1;
	public static final int character = 2;

	private String fieldName;
	private int type;
	private boolean primary;
	private int columnNumber;

	public Field(String fieldName, int type) {
		this.fieldName = fieldName;
		this.type = type;
	}

	/**
	 * check if an input type is valid compared to the field type
	 * 
	 * @param data
	 *            the input to check it's validity
	 * @return true if input match type of filed
	 * */
	boolean check(String data) {
		if (type == Int) {// check integer
			try {
				Integer.parseInt(data);

				return true;
			} catch (Exception e) {
				return false;
			}
		}

		else if (type == character) {
			if (data.length() != 1)
				return false;
		}

		return true;
	}

	public int compare(String s1, String s2) {
		if (type == character) {
			char a = s1.charAt(0);
			char b = s2.charAt(0);
			if (a < b)
				return -1;
			if (a > b)
				return 1;
			return 0;
		}

		if (type == Int) {
			int a = Integer.parseInt(s1);
			int b = Integer.parseInt(s2);
			if (a < b)
				return -1;
			if (a > b)
				return 1;
			return 0;
		}

		if (type == string) {
			return s1.compareTo(s2);
		}

		return 0;
	}

	public String getFieldName() {
		return fieldName;
	}

	public int getType() {
		return type;
	}

	public boolean isPrimary() {
		return primary;
	}

}
