package perf.ui.testng.agent.helper;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import perf.ui.testng.agent.IPerfUIBaseTestClass;
import perf.ui.testng.agent.annotations.PerfUI;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class PerfUIHelper {

    private static File auditScriptFile = new File("src/main/java/perf/ui/testng/agent/scripts/check_ui_performance.js.js");
    private static File polyFillFile = new File("src/main/java/perf/ui/testng/agent/scripts/polyfill_ie11.js");
    private static File isPageLoadScript = new File("src/main/java/perf/ui/testng/agent/scripts/page_really_loaded.js");

    public static boolean checkIsAnnotation(ITestResult result) {
        return result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(PerfUI.class) != null;
    }

    public static String getTestName(ITestResult result) {
        String annotationName = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(PerfUI.class).name();
        return annotationName.equals("") ? result.getName() : annotationName;
    }

    private static String getReportName(String testName, String folder) {
        return String.format("%s/%s_%d.html", folder, testName, getTime());
    }

    public static long getTime() {
        return new Date().getTime();
    }

    private static String getScriptCode(File file) {
        String script = "";
        try {
            script = FileUtils.readFileToString(file, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return script;
    }


    public static WebDriver getDriver(ITestResult result) {
        return ((IPerfUIBaseTestClass) result.getInstance()).getDriver();
    }

    private static void checkIsPageReallyLoaded(WebDriver driver, int timeOut) {
        Wait<WebDriver> wait = new WebDriverWait(driver, timeOut);
        wait.until(webDriver -> String
                .valueOf(((JavascriptExecutor) webDriver).executeScript(String.format("return %s", getScriptCode(isPageLoadScript))))
                .equals("true"));
    }

    public static String getAuditResult(WebDriver driver, long startTime, int loadTimeOut) {
        checkIsPageReallyLoaded(driver, loadTimeOut);
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        if (driver instanceof InternetExplorerDriver) {
            jsExecutor.executeScript(getScriptCode(polyFillFile));
        }
        return (String) jsExecutor.executeScript(String.format("var testStartTimestamp=%d; return %s", startTime, getScriptCode(auditScriptFile)));
    }


    public static void writeHtmlToFile(HttpResponse response, String testName, String folder) {
        try {
            FileUtils.copyInputStreamToFile(response.getEntity().getContent(), new File(getReportName(testName, folder)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void syncTimeout(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
