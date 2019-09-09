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
public class DummyTest {

    private WebDriver driver;
    private WebDriverWait waiter;
    private PerfUIMetricGrabber perfUIMetricGrabber = new PerfUIMetricGrabber();


    @BeforeSuite
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
    public void AmazonSearchWithParameters_2() {
        driver.get("https://www.amazon.com/s?k=Fender+Jaguar");
        waiter.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(),'fail')]")));
        waiter.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'fail')]")));
    }

    @Test
    public void AmazonSearchWithParameters_3() {
        driver.get("https://www.amazon.com/s?k=Fender+Stratocaster");
        waiter.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(),'results')]")));
        waiter.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'results')]")));
    }

    @AfterTest
    public void perfUiAnalise(){
        perfUIMetricGrabber.startAudit(driver);
    }

    @AfterSuite
    public void shutDown() {
        if (!Objects.isNull(driver)) {
            driver.quit();
        }
    }

}
