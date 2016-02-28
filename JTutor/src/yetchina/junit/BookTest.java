package yetchina.junit;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class BookTest extends TestCase {
 
	private Book book1;
	private Book book2;
	private Book book3;
	
	public BookTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		book1 = new Book("ES",12.99);
		book2 = new Book("The Gate",11.99);
		book3 = new Book("ES",12.99);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		book1 = null;
		book2 = null;
		book3 = null;
	}
	
	public void testEquals(){
		//assertFalse(book3.equals(book1));
		assertEquals(book3,book1);
		assertTrue(book2.equals(book1));
	}
	
	public static Test suite(){
		TestSuite suite = new TestSuite();
		suite.addTest(new BookTest("testEquals"));
		return suite;
		
	}

}
