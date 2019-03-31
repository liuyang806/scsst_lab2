package test;

import java.util.regex.Pattern;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.*;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;


import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class TestBaidu {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  
  static List<String> ids;
  static List<String> names;
  static List<String> urls;

  private String stuId = "";
  private String stuName = "";
  private String gitUrl = "";

  public TestBaidu(String stuId,String stuName,String gitUrl){
	  this.stuId = stuId;
	  this.stuName = stuName;
	  this.gitUrl = gitUrl;
  }
  static {
	  
      ids = new ArrayList<>();
      urls = new ArrayList<>();
      names = new ArrayList<>();
      Row row = null;
	  String stuId,stuName, gitUrl;
	  List<String[]> list = new ArrayList<String[]>();
      try {
    	  InputStream is = new FileInputStream("src/resources/»Ìº˛≤‚ ‘√˚µ•.xlsx");
		  Workbook wb = new XSSFWorkbook(is);
		  Sheet sheet = wb.getSheetAt(0);
		  int rowNum = sheet.getPhysicalNumberOfRows();
		  
		  for(int i = 2; i < rowNum; i++) {
		    	row = sheet.getRow(i);
		    	row.getCell(1).setCellType(CellType.STRING);
		    	stuId = row.getCell(1).getStringCellValue();
		    	stuName = row.getCell(2).getStringCellValue();
		    	gitUrl = row.getCell(3).getStringCellValue();
		    	
		    	ids.add(stuId);
		    	names.add(stuName);
		    	urls.add(gitUrl);
		  }
      } catch (Exception e) {
          e.printStackTrace();
      }
}
  @Parameterized.Parameters
  public static Collection<Object[]> getData(){
      ArrayList<Object[]> ret = new ArrayList<>();
      for(int i = 0; i < ids.size(); i++){
          ArrayList<Object> temp = new ArrayList<>();
          temp.add(ids.get(i));
          temp.add(names.get(i));
          temp.add(urls.get(i));
          ret.add(temp.toArray());
      }
      return ret;
  }
  
  @Before
  public void setUp() throws Exception {
	  String driverPath = System.getProperty("user.dir") + "/src/resources/driver/geckodriver.exe";
	  System.setProperty("webdriver.gecko.driver", driverPath);
	  driver = new FirefoxDriver();
	  baseUrl = "http://121.193.130.195:8800/";
	  driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
	  driver.get(baseUrl + "/");
  }

  @Test
  public void testBaidu() throws Exception {
	  //driver.get(baseUrl + "/");
	  driver.findElement(By.name("id")).clear();
      driver.findElement(By.name("id")).sendKeys(stuId);
      driver.findElement(By.name("password")).sendKeys(stuId.substring(4));
      driver.findElement(By.id("btn_login")).click();
      assertEquals(stuId, driver.findElement(By.id("student-id")).getText());
      assertEquals(stuName, driver.findElement(By.id("student-name")).getText());
      assertEquals(gitUrl, driver.findElement(By.id("student-git")).getText());
      //driver.findElement(By.id("btn_logout")).click();
      //driver.findElement(By.id("btn_return")).click();
  }

  @After
  public void tearDown() throws Exception {
	  driver.quit();
	  String verificationErrorString = verificationErrors.toString();
	  if (!"".equals(verificationErrorString)) {
		  fail(verificationErrorString);
	  }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}


