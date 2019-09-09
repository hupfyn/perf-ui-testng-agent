package perf.ui.testng.agent;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;

import java.io.File;
import java.io.IOException;

public class PerfUIMetricGrabber implements ITestListener {

    private JavascriptExecutor jsExecutor;


    public void startAudit(WebDriver driver){
       this.jsExecutor = getJsExecutor(driver);
       String result = getResult();
    }

    public static String loadScript(){
        String result = "";
        try {
            result = FileUtils.readFileToString(new File("src/main/java/perf/ui/testng/agent/helper/audit_script_source.js"), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private JavascriptExecutor getJsExecutor(WebDriver driver){
        return (JavascriptExecutor) driver;
    }

    private String getResult(){
        return (String) jsExecutor.executeScript("return "+loadScript());
    }
}
