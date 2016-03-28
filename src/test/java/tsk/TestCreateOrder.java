package tsk;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

@RunWith(Parameterized.class)
public class TestCreateOrder {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Parameterized.Parameter(value = 0)
  public String transportType;

  @Parameterized.Parameter(value = 1)
  public String transportTypeFullText;

  @Parameterized.Parameter(value = 2)
  public String transportTypeFullTextType;

  @Parameterized.Parameters(name = "{index}: type:{0} shall be:{1} and:{2}")
  public static Iterable<Object[]> data1() {
    return Arrays.asList(new Object[][]{
            {"doprava_nastaveni_typ-1Dobirkou", "Česká pošta - Balík na poštu", "Dobirkou", },
            {"doprava_nastaveni_typ-7NaPokladne", "Heureka InPost", "osobni", },
            {"doprava_nastaveni_typ-6NaPokladne", "Zásilkovna.cz", "osobni", },
            {"doprava_nastaveni_typ-5NaPokladne", "Uloženka.cz/Heureka Point", "osobni", },
            {"doprava_nastaveni_typ-2NaPokladne", "Pobočka Brno", "osobni", },
            {"doprava_nastaveni_typ-4Dobirkou", "Česká pošta - Balík do ruky", "Dobirkou", }
    });
  }

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "http://demo.proeshop.cz/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testCreateOrder() throws Exception {
    driver.get(baseUrl + "/admin/login");
    driver.findElement(By.id("login")).clear();
    driver.findElement(By.id("login")).sendKeys("admin");
    driver.findElement(By.id("password")).clear();
    driver.findElement(By.id("password")).sendKeys("admin");
    driver.findElement(By.id("submit")).click();
    driver.get(baseUrl + "/admin/objednavka-vlozeni/krok1");
//    driver.findElement(By.linkText("/admin/objednavka-vlozeni/krok1")).click();
    driver.findElement(By.xpath("(//input[@id='additem'])[4]")).click();
    driver.findElement(By.id("submit")).click();
    driver.findElement(By.id("submit")).click();
    assertTrue(driver.findElement(By.cssSelector("div.info_box")).getText().contains("Zákazník"));
    new Select(driver.findElement(By.id("select_user-zakaznik_id"))).selectByVisibleText("admin, Test test, info@proeshop.cz");
    driver.findElement(By.id("submit")).click();
    driver.findElement(By.id("submit")).click();
    driver.findElement(By.id("submit")).click();
    driver.findElement(By.id("submit")).click();
    assertTrue(driver.findElement(By.cssSelector("div.info_box")).getText().contains("Přeprava"));
    assertTrue(driver.findElement(By.cssSelector("ul.errors > li")).getText().contains("Tato položka nesmí být prázdná"));
    driver.findElement(By.id(transportType)).click();
    driver.findElement(By.id("submit")).click();
    assertTrue(driver.findElement(By.cssSelector("div.success_box")).getText().contains("Objednávka byla vložena."));
    assertTrue(driver.findElement(By.cssSelector("#doprava > div.form > fieldset > table.form_table")).getText().contains(transportTypeFullText));
    assertTrue(driver.findElement(By.cssSelector("#doprava > div.form > fieldset > table.form_table")).getText().contains(transportTypeFullTextType));
//    driver.findElement(By.linkText("Objednávky")).click();
    driver.get(baseUrl + "/admin/objednavka");
    assertTrue(driver.findElement(By.cssSelector("table.datagrid")).getText().contains("info@proeshop.cz"));
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
