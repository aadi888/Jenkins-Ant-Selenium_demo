import org.testng.Assert;
import org.testng.annotations.Test;


public class TestNgTest {
	
	
	@Test
	public void testCommands()
	{
		
	String actual= Command.send200();
	
	 String expected="HTTP/1.0 200 (OK)";
	 
	 Assert.assertEquals(actual, expected); 
	 
	}

	
	
	
}
