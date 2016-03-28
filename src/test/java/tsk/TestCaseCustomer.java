package tsk;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class TestCaseCustomer {
    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    private String loginName;
    private String name;
    private String password;
    private String email;

    @Before
    public void setUp() throws Exception {
        driver = new FirefoxDriver();
        baseUrl = "http://demo.proeshop.cz/";
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        loginName = randomText(10);
        name = randomText(5);
        email = randomText(10) + "@dev.cz";
    }

    @Test
    public void testCaseCustomerSendEmailYes() throws Exception {
        driver.get(baseUrl + "/admin/login/index/last_auth_url/%2Fadmin");
        driver.findElement(By.id("login")).clear();
        driver.findElement(By.id("login")).sendKeys("admin");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("admin");
        driver.findElement(By.id("submit")).click();
        driver.get(baseUrl + "/admin/zakaznik/pridat");
//        driver.findElement(By.cssSelector("li.zakaznici_vlozit > a")).click();
        driver.findElement(By.id("login")).clear();
        driver.findElement(By.id("login")).sendKeys(loginName);
        driver.findElement(By.id("jmeno")).clear();
        driver.findElement(By.id("jmeno")).sendKeys(name);
        driver.findElement(By.id("prijmeni")).clear();
        driver.findElement(By.id("prijmeni")).sendKeys("LOGIN");
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("zasilat_reklamy")).click();
        assertTrue(closeAlertAndGetItsText().matches("^Opravdu chcete změnit stav zasílaní reklamy[\\s\\S]$"));
        driver.findElement(By.id("submit")).click();
        driver.get(baseUrl + "/admin/zakaznik/index/zasilat_reklamy/1");
//        driver.findElement(By.linkText("Export emailových adres")).click();
//        driver.findElement(By.linkText("Přejít na seznam zákazníků")).click();
        assertTrue(driver.findElement(By.cssSelector("table.datagrid")).getText().contains(loginName));
        driver.get(baseUrl + "/admin/zakaznik/index/zasilat_reklamy/1");
//        driver.findElement(By.linkText("Export emailových adres")).click();
//        driver.findElement(By.linkText("Přejít na seznam zákazníků")).click();
        assertTrue(driver.findElement(By.cssSelector("table.datagrid")).getText().contains(loginName));
        new Select(driver.findElement(By.id("zasilat_reklamy"))).selectByVisibleText("Ne");
        driver.findElement(By.id("submit")).click();
        driver.get(baseUrl + "/admin/zakaznik/index/jmeno//prijmeni//login//preferovany_jazyk//zasilat_reklamy/0/submit/Filtrovat");
//        driver.findElement(By.linkText("Export emailových adres")).click();
//        driver.findElement(By.linkText("Přejít na seznam zákazníků")).click();
        assertThat(loginName, is(not(driver.findElement(By.cssSelector("table.datagrid")).getText())));
//        driver.findElement(By.linkText("Export emailových adres")).click();
    }

    @Test
    public void testCaseCustomerSendEmailNo() throws Exception {

        driver.get(baseUrl + "/admin/login/index/last_auth_url/%2Fadmin");
        driver.findElement(By.id("login")).clear();
        driver.findElement(By.id("login")).sendKeys("admin");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("admin");
        driver.findElement(By.id("submit")).click();
        driver.get(baseUrl + "/admin/zakaznik/pridat");
//        driver.findElement(By.cssSelector("li.zakaznici_vlozit > a")).click();
        driver.findElement(By.id("login")).clear();
        driver.findElement(By.id("login")).sendKeys(loginName);
        driver.findElement(By.id("jmeno")).clear();
        driver.findElement(By.id("jmeno")).sendKeys(name);
        driver.findElement(By.id("prijmeni")).clear();
        driver.findElement(By.id("prijmeni")).sendKeys("LOGIN");
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys(email);
        driver.get(baseUrl + "/admin/zakaznik/index/zasilat_reklamy/1");
        assertFalse(driver.findElement(By.cssSelector("table.datagrid")).getText().contains(loginName));
        driver.get(baseUrl + "/admin/zakaznik/index/zasilat_reklamy/1");
        new Select(driver.findElement(By.id("zasilat_reklamy"))).selectByVisibleText("Ne");
        driver.findElement(By.id("submit")).click();
        driver.get(baseUrl + "/admin/zakaznik/index/jmeno//prijmeni//login//preferovany_jazyk//zasilat_reklamy/0/submit/Filtrovat");
//        driver.findElement(By.linkText("Export emailových adres")).click();
//        driver.findElement(By.linkText("Přejít na seznam zákazníků")).click();
        assertThat(loginName, is(not(driver.findElement(By.cssSelector("table.datagrid")).getText())));
//        driver.findElement(By.linkText("Export emailových adres")).click();
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

    private String randomText(int size) {
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }
}
