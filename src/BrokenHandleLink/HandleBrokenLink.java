package BrokenHandleLink;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HandleBrokenLink {
	
	public static void clickOn(WebDriver driver,WebElement locator,int timeout)
	{
		new WebDriverWait(driver,timeout).ignoring(StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(locator));
		locator.click();
	}

	public static void main(String[] args) throws MalformedURLException, IOException {
		
		
		System.setProperty("webdriver.chrome.driver", "E:\\chromedriver_77\\chromedriver.exe");
		WebDriver driver=new ChromeDriver();
		
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
		driver.get("https://classic.crmpro.com/");
		
		driver.findElement(By.name("username")).sendKeys("aravinds25");
		driver.findElement(By.name("password")).sendKeys("aravind@1");
		//driver.findElement(By.xpath("//input[@type='submit' and @value='Login']")).click();
		
		HandleBrokenLink.clickOn(driver, driver.findElement(By.xpath("//input[@type='submit' and @value='Login']")), 20);
		
		
		
		
		WebDriver wait =
		driver.switchTo().frame("mainpanel");
		
		//links--//a tag
		//image ---//img
		
		//href---link property
		
		
		//1.get the list of all the links and images
		
		List <WebElement> list =driver.findElements(By.tagName("a"));
		
		list.addAll(driver.findElements(By.tagName("img")));	
		
		
		System.out.println("All link"+list.size());
		System.out.println(list);
		
		List <WebElement> activelinks= new ArrayList<WebElement>();
		
		
		//2.Iterate the link list// exclude all the links and image which are not having href
		
		for (int i=0;i<list.size();i++)
		{
			if (list.get(i).getAttribute("href")!=null &&(!list.get(i).getAttribute("href").contains("javascript")) )
			{
				activelinks.add(list.get(i));
			}
		}
		
		
		//get the size of active links
		System.out.println("active"+activelinks.size());
		
		//3.check the href url with http connection api
		
		for (int j=0;j<activelinks.size();j++)
		{
			HttpURLConnection connection = (HttpURLConnection) new URL(activelinks.get(j).getAttribute("href")).openConnection();
			connection.connect();
			String response=connection.getResponseMessage();
			connection.disconnect();
			
			System.out.println(activelinks.get(j).getAttribute("href")+"----------->"+response);
		}
	}

}
