package perf.ui.testng.agent.helper;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import perf.ui.testng.agent.IPerfUIBaseTestClass;
import perf.ui.testng.agent.annotations.PerfUI;
import perf.ui.testng.agent.config.PerfUIConfig;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

public class PerfUIHelper {


    public static boolean checkIsAnnotation(ITestResult result){
        return result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(PerfUI.class) != null;
    }

    public static String getTestName(ITestResult result){
        String annotationName = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(PerfUI.class).name();
        return annotationName.equals("")?result.getName():annotationName;
    }

    private static String getReportName(String testName,String folder){
        return String.format("%s/%s_%d.html", folder, testName, getTime());
    }

    public static long getTime() {
        return new Date().getTime();
    }

    private static String getScript(){
        String result = "";
        try {
            result = FileUtils.readFileToString(new File("src/main/java/perf/ui/testng/agent/scripts/check_ui_performance.js.js"), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static WebDriver getDriver(ITestResult result){
        return ((IPerfUIBaseTestClass)result.getInstance()).getDriver();
    }

    public static String getAuditResult(WebDriver driver,long startTime){
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        return (String) jsExecutor.executeScript(String.format("var testStartTimestamp=%d; return %s", startTime, getScript()));
    }


    public static void writeHtmlToFile(HttpResponse response, String testName, String folder){
        try {
            FileUtils.copyInputStreamToFile(response.getEntity().getContent(),new File(getReportName(testName,folder)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setConfigValueForRecorder(PerfUIConfig config){
        System.setProperty("video.save.mode","ALL");
        System.setProperty("video.frame.rate",config.frameRate());
        if(Objects.nonNull(config.videoDisplay())){
            System.setProperty("ffmpeg.display",config.videoDisplay());
        }
    }
}
