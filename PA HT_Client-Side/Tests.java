import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.v104.css.model.Value;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Random;
import org.openqa.selenium.support.FindBy;


public class Tests {
/*
    @FindBy(xpath = "//a[@href='/computers']")
    private WebElement computersItem;

    @FindBy(xpath = "//li[@class='inactive']/a[@href='/desktops']")
    private WebElement desktopItem;

    @FindBy(xpath = "//h2[@class='product-title']")
    private WebElement anyDesktop; */

    String computersURL = "https://demo.nopcommerce.com/computers";
    String desktopsURL = "https://demo.nopcommerce.com/desktops";


    private WebDriver driver;

        @BeforeTest
        public void profileSetup() {
            System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        }

        @BeforeTest
        public void testSetup(){
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
            driver = new ChromeDriver(chromeOptions);
       /*     Random random = new Random();
            int randomPause = random.nextInt(5) + 3;
            Wait wait= new WebDriverWait(driver, Duration.ofSeconds(randomPause));*/
            driver.manage().window().maximize();
            driver.get("https://demo.nopcommerce.com");
       //     wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@class='master-wrapper-page']")));
        }
        @Test
        public void PagesNavigate() throws InterruptedException {

            driver.findElement(By.xpath("//a[@href=\"/computers\"]")).click();
            Assert.assertTrue(driver.getCurrentUrl().equals(computersURL));

            driver.findElement(By.xpath("//li[@class=\"inactive\"]/a[@href=\"/desktops\"]")).click();
            Assert.assertTrue(driver.getCurrentUrl().equals(desktopsURL));

            int dQuantity = driver.findElements(By.xpath( "//h2[@class=\"product-title\"]")).size();
            Random rr = new Random();
            int ind = rr.nextInt(dQuantity-1);
            if (ind<0) ind = ind+1;

            String prodURL = driver.findElement(By.xpath("(//h2[@class=\"product-title\"]/a)["+ind+"]")).getAttribute("href");
            driver.findElement(By.xpath("(//h2[@class=\"product-title\"]/a)["+ind+"]")).click();
            Assert.assertTrue(driver.getCurrentUrl().equals(prodURL ));
        }

/*

        public void clickElement(WebElement element) {
            element.click();
        }

        @AfterMethod
        public void tearDown() {
            driver.quit();
        }

        public boolean isElementPresent(By locator) {
            int count = driver.findElements(locator).size();
            return count > 0;
        }



        */


}


