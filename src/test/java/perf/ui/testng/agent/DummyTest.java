package perf.ui.testng.agent;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import perf.ui.testng.agent.annotations.PerfUI;

import java.util.Objects;

@Listeners(PerfUIListener.class)
public class DummyTest implements IPerfUIBaseTestClass {

    private WebDriver driver;
    private WebDriverWait waiter;

    @Override
    public WebDriver getDriver() {
        return driver;
    }

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        waiter = new WebDriverWait(driver, 10);
    }

    @Test
    @PerfUI
    public void AmazonSearchWithParameters_1() {
        driver.get("https://www.amazon.com/s?k=Fender+Jazz+Bass");
        waiter.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(),'results')]")));
        waiter.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'results')]")));
    }

    @Test
    @PerfUI
    public void AmazonSearchWithParameters_2() {
        driver.get("https://www.amazon.com/s?k=Fender+Jaguar");
        waiter.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(),'results')]")));
        waiter.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'results')]")));
    }

    @Test
    @PerfUI
    public void AmazonSearchWithParameters_3() {
        driver.get("https://www.amazon.com/s?k=Fender+Stratocaster");
        waiter.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(),'results')]")));
        waiter.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'results')]")));
    }

    @AfterClass
    public void shutDown() {
        if (!Objects.isNull(driver)) {
            driver.quit();
        }
    }
}
