package tsk;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
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
public class TestCaseCustomerRegistration {
    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @Parameterized.Parameter(value = 0)
    public String login;
    @Parameterized.Parameter(value = 1)
    public String name;
    @Parameterized.Parameter(value = 2)
    public String surname;
    @Parameterized.Parameter(value = 3)
    public String email;
    @Parameterized.Parameter(value = 4)
    public boolean expectingErrForm;
    @Parameterized.Parameter(value = 5)
    public boolean expectionEmailError;



    @Parameterized.Parameters(name = "{index}: login:{0} name:{1} surname:{2} email:{3} err form:{4} emailErr:{5}")
    public static Iterable<Object[]> data1() {
        return Arrays.asList(new Object[][]{
                {new RandomTextCreator().randomText(10), "Michal", "Max", "michal@dev.cz", false, false},
                {" Dev", "Michal", "Max", "michal@dev.cz", true, false},
                {"9ev", "Michal", "Max", "michal@dev.cz", true, false},
                {"_ev", "Michal", "Max", "michal@dev.cz", true, false},
                {new RandomTextCreator().randomText(10), "++++", "----", "michal@dev.cz", false, false},
                {"dev", "`SELECT * from DUAL;+  - * ", ",, + -", "michaldev.cz", true, true},
                {new RandomTextCreator().randomText(10), ";+  - * ", ",, + -", "michal@devcz", false, false},
        });
    }



    @Before
    public void setUp() throws Exception {
        driver = new FirefoxDriver();
        baseUrl = "http://demo.proeshop.cz/";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

    }

    @Test
    public void testCaseCustomerRegistration() throws Exception {
        driver.get(baseUrl + "/admin/login");
        driver.findElement(By.id("login")).clear();
        driver.findElement(By.id("login")).sendKeys("admin");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("admin");
        driver.findElement(By.id("submit")).click();
        driver.get(baseUrl + "/admin/zakaznik/pridat");
//    driver.findElement(By.cssSelector("li.zakaznici_vlozit > a")).click();
        driver.findElement(By.id("login")).clear();
        driver.findElement(By.id("login")).sendKeys(login);
        driver.findElement(By.id("jmeno")).clear();
        driver.findElement(By.id("jmeno")).sendKeys(name);
        driver.findElement(By.id("prijmeni")).clear();
        driver.findElement(By.id("prijmeni")).sendKeys(surname);
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("submit")).click();

        if (expectingErrForm) {
            assertTrue(driver.findElement(By.cssSelector("div.info_box")).getText().contains("chyby"));
            if (expectionEmailError) {
                assertTrue(driver.findElement(By.cssSelector("div.info_box")).getText().contains(email));
            }
        } else {
            assertEquals("Zákazník byl uložen.", driver.findElement(By.cssSelector("div.success_box")).getText());
        }

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
