

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumTest {
	
	public static void main(String[] args) {
		
		System.setProperty("webdriver.chrome.driver","C://Users//user//Downloads//chromedriver_win32//chromedriver.exe");
	    WebDriver driver=new ChromeDriver();
	    
	    driver.get("https://www.facebook.com/");
	    
	   WebElement username= driver.findElement(By.name("email"));
	   
	   username.sendKeys("adish888@gmail.com");
	   
	   WebElement password= driver.findElement(By.name("pass"));
	   
	   password.sendKeys("jaijinendra");
	   
	   
	   
	   
	   WebElement submitForm= driver.findElement(By.id("loginbutton"));
	   
	   submitForm.submit();
	   
	   
	   
	   
	   
	    
	    
		
	}

}
