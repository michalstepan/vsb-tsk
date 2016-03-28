package tsk;

import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestCase02 {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "http://pcfeia406a.vsb.cz/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testCase02() throws Exception {
    driver.get(baseUrl + "/shop2/index.php?main_page=shopping_cart&product_id=28");
    driver.findElement(By.cssSelector("img[alt=\"Die Hard With A Vengeance Linked\"]")).click();
    driver.findElement(By.cssSelector("input[type=\"image\"]")).click();
    driver.findElement(By.name("cart_quantity[]")).clear();
    driver.findElement(By.name("cart_quantity[]")).sendKeys("2");
    driver.findElement(By.cssSelector("input[type=\"image\"]")).click();
    driver.findElement(By.linkText("Software->")).click();
    driver.findElement(By.cssSelector("img[alt=\"Unreal Tournament Linked\"]")).click();
    driver.findElement(By.cssSelector("input[type=\"image\"]")).click();
    driver.findElement(By.xpath("(//a[contains(text(),'Big Linked')])[2]")).click();
    driver.findElement(By.linkText("A Bug's Life \"Multi Pak\" Special 2003 Collectors Edition")).click();
    driver.findElement(By.cssSelector("input[type=\"image\"]")).click();
    assertEquals("Sub-Total: €206.96", driver.findElement(By.id("cartSubTotal")).getText());
    assertTrue(driver.findElement(By.id("cartSubTotal")).getText().contains("€206.96"));
    driver.findElement(By.xpath("(//img[@alt='Delete this item from the cart by clicking this icon.'])[3]")).click();
    driver.findElement(By.cssSelector("img[alt=\"Delete this item from the cart by clicking this icon.\"]")).click();
    driver.findElement(By.cssSelector("img[alt=\"Delete this item from the cart by clicking this icon.\"]")).click();
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
