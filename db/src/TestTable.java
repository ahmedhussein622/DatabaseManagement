
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestTable {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	
	@Test
	public void testTable() {
		Table t = new Table("dodo");
		assertEquals("dodo", t.getTableName());
		assertEquals(0, t.getColumnsNumber());
		assertEquals(0, t.getRowsNumber());
	}

	@Test
	public void testAddNewField() {
		Table t = new Table("dodo");
		t.addNewField(new Field("num", Field.string));
		assertEquals(1, t.getColumnsNumber());
		assertEquals(true, t.containsFiled("num"));
		t.addNewField(new Field("bob", Field.string));
		assertEquals(2, t.getColumnsNumber());
		assertEquals(true, t.containsFiled("bob"));
	}

	@Test
	public void testRemoveField() {
		Table t = new Table("dodo");
		t.addNewField(new Field("bob", Field.string));
		t.addNewField(new Field("num", Field.string));
		t.removeField("bob");
		assertEquals(1, t.getColumnsNumber());
		assertEquals(false, t.containsFiled("bob"));
	}

	@Test
	public void testContainsFiled() {
		Table t = new Table("dodo");
		t.addNewField(new Field("bob", Field.string));
		t.addNewField(new Field("num", Field.string));
		t.removeField("bob");
		assertEquals(false, t.containsFiled("bob"));
		assertEquals(true, t.containsFiled("num"));
	}

	@Test
	public void testCheckData() {
		Table t = new Table("dodo");
		t.addNewField(new Field("bob", Field.string));
		t.addNewField(new Field("num", Field.string));
		t.addNewField(new Field("lol", Field.Int));
		assertEquals(false, t.checkData(new String[]{"bob","lol"},
				new String[]{"adf","dd"}));
		assertEquals(true, t.checkData(new String[]{"bob","lol"},
				new String[]{"adf","456"}));
	}

	@Test
	public void testInsertRow() {
		Table t = new Table("dodo");
		t.addNewField(new Field("bob", Field.string));
		t.addNewField(new Field("num", Field.string));
		assertEquals(0, t.getRowsNumber());
		t.insertRow(new String[]{"bob","num"}, new String[]{"tot",null});
		assertEquals(1, t.getRowsNumber());
		
	}

	@Test
	public void testUpdateStringArrayStringArray() {
		Table t = new Table("dodo");
		t.addNewField(new Field("bob", Field.string));
		t.addNewField(new Field("num", Field.string));
		t.insertRow(new String[]{"bob","num"}, new String[]{"tot","dd"});
		t.update(new String[]{"bob"}, new String[]{"koko"});
		ArrayList<String> k = new ArrayList<String>();
		k.add("koko");
		k.add("dd");
		assertEquals(k, t.startRowLooping());
	}

	
	@Test
	public void testSelectRows() {
		Table t = new Table("dodo");
		t.addNewField(new Field("bob", Field.string));
		t.addNewField(new Field("num", Field.string));
		t.insertRow(new String[]{"bob","num"}, new String[]{"tot","dd"});
		t.insertRow(new String[]{"bob","num"}, new String[]{"bl","foo"});
		Table y = t.selectRows("bob", "tot", Table.equale);
		assertEquals(1, y.getRowsNumber());
	}

	@Test
	public void testSelectFields() {
		Table t = new Table("dodo");
		t.addNewField(new Field("bob", Field.string));
		t.addNewField(new Field("num", Field.string));
		t.insertRow(new String[]{"bob","num"}, new String[]{"tot","dd"});
		t.insertRow(new String[]{"bob","num"}, new String[]{"bl","foo"});
		Table y = t.selectFields(new String[]{"bob"});
		assertEquals(1, y.getColumnsNumber());
	}

	@Test
	public void testDelete() {
		Table t = new Table("dodo");
		t.addNewField(new Field("bob", Field.string));
		t.addNewField(new Field("num", Field.string));
		t.insertRow(new String[]{"bob","num"}, new String[]{"tot","dd"});
		t.insertRow(new String[]{"bob","num"}, new String[]{"bl","foo"});
		Table y = t.selectRows("bob", "tot", Table.equale);
		assertEquals(2, t.getRowsNumber());
		t.delete(y);
		assertEquals(1, t.getRowsNumber());
	}

	
	@Test
	public void testGetFields() {
		
		Table t = new Table("dodo");
		t.addNewField(new Field("bob", Field.string));
		t.addNewField(new Field("num", Field.string));
		t.addNewField(new Field("some", Field.Int));
		assertEquals(3, t.getFields().size());
	}

	@Test
	public void testStartRowLooping() {
		Table t = new Table("dodo");
		t.addNewField(new Field("bob", Field.string));
		t.addNewField(new Field("num", Field.string));
		t.addNewField(new Field("some", Field.Int));
		ArrayList<String> k ;
		k = t.startRowLooping();
		assertEquals(null, k);
		
		t.insertRow(new String[]{"bob","num"}, new String[]{"tot","dd"});
		t.insertRow(new String[]{"bob","num"}, new String[]{"bl","foo"});
		
		k = t.startRowLooping();
		ArrayList<String> b = new ArrayList<String>();
		b.add("tot");
		b.add("dd");
		b.add(null);
		assertEquals(k,b);
		
	}

	@Test
	public void testGetNextRow() {
		Table t = new Table("dodo");
		t.addNewField(new Field("bob", Field.string));
		t.addNewField(new Field("num", Field.string));
		t.addNewField(new Field("some", Field.Int));
		ArrayList<String> k ;
		
		t.insertRow(new String[]{"bob","num"}, new String[]{"tot","dd"});
		t.insertRow(new String[]{"bob","num"}, new String[]{"bl","foo"});
		t.startRowLooping();
		k = t.getNextRow();
		ArrayList<String> b = new ArrayList<String>();
		b.add("bl");
		b.add("foo");
		b.add(null);
		assertEquals(k,b);
	}

	@Test
	public void testGetRowsNumber() {
		Table t = new Table("dodo");
		t.addNewField(new Field("bob", Field.string));
		t.addNewField(new Field("num", Field.string));
		t.addNewField(new Field("some", Field.Int));
		t.insertRow(new String[]{"bob","num"}, new String[]{"tot","dd"});
		t.insertRow(new String[]{"bob","num"}, new String[]{"bl","foo"});
		assertEquals(2, t.getRowsNumber());
		t.delete(t);
		assertEquals(0, t.getRowsNumber());
	}

	@Test
	public void testGetColumnsNumber() {
		Table t = new Table("dodo");
		assertEquals(0, t.getColumnsNumber());
		t.addNewField(new Field("bob", Field.string));
		assertEquals(1, t.getColumnsNumber());
		t.addNewField(new Field("num", Field.string));
		assertEquals(2, t.getColumnsNumber());
		t.addNewField(new Field("some", Field.Int));
		assertEquals(3, t.getColumnsNumber());
		t.removeField("some");
		assertEquals(2, t.getColumnsNumber());
	}

}