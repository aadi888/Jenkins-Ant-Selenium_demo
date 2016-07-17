import org.testng.Assert;
import org.testng.annotations.Test;


public class TestNgTest {
	
	
	@Test
	public void testCommand200()
	{
		
	String actual= Command.send200();
	
	 String expected="HTTP/1.0 200 (OK)";
	 
	 Assert.assertEquals(actual, expected); 
	 
	}
	
	@Test
	public void testCommand404()
	{
		
	String actual= Command.send404();
	
	 String expected="HTTP/1.0 404 NOT FOUND";
	 
	 Assert.assertEquals(actual, expected); 
	 
	}
	
	@Test
	public void testCommand501()
	{
		
	String actual= Command.send501();
	
	 String expected="HTTP/1.0 501 INTERNAL ERROR";
	 
	 Assert.assertEquals(actual, expected); 
	 
	}

	
	
	
}
