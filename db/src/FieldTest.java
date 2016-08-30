
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FieldTest {

	public FieldTest() {
		
	}

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
	public void testField() {
		Field f = new Field("koko", Field.Int);
		assertEquals("koko", f.getFieldName());
		assertEquals(Field.Int, f.getType());
	}

	@Test
	public void testCheck() {
		Field f = new Field("koko", Field.Int);
		assertEquals(false, f.check("g"));
		assertEquals(true, f.check("5577"));
		f = new Field("koko", Field.character);
		assertEquals(true, f.check("g"));
		assertEquals(false, f.check("5577"));
	}

	@Test
	public void testCompare() {
		Field f = new Field("koko", Field.Int);
		assertEquals(0, f.compare("5","5"));
		assertEquals(-1, f.compare("4","11"));
		assertEquals(1, f.compare("45","11"));
		f = new Field("koko", Field.character);
		assertEquals(0, f.compare("k", "k"));
	}

	@Test
	public void testGetFieldName() {
		Field f = new Field("koko", Field.Int);
		assertEquals("koko", f.getFieldName());
	}

	@Test
	public void testGetType() {
		Field f = new Field("koko", Field.Int);
		assertEquals(Field.Int,f.getType());
	}

}
