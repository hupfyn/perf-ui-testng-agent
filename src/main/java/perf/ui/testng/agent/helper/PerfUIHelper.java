package perf.ui.testng.agent.helper;

import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import perf.ui.testng.agent.config.AuditConfig;
import perf.ui.testng.agent.annotations.PerfUI;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class PerfUIHelper {


    public static boolean checkIsAnnotation(ITestResult result){
        return result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(PerfUI.class) != null;
    }

    private static String getTestName(ITestResult result){
        String annotationName = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(PerfUI.class).name();
        return annotationName.equals("")?result.getName():annotationName;
    }


    public static long getTime() {
        return new Date().getTime();
    }


    private static String generateTestInfo(long startTime, String testName, String videoPath) {
        return String.format("test.startTime=%d\ntest.name=%s\ntest.videoPath=%s", startTime, testName, videoPath);
    }

    public static void writeTestInfo(long startTime,ITestResult result,String path){
        String dataToWrite = generateTestInfo(startTime,getTestName(result),path);
        try {
            FileUtils.writeStringToFile(new File("agentTemp/testInfo.properties"),dataToWrite,"utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public static String getAuditResult(WebDriver driver,long startTime){
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        return (String) jsExecutor.executeScript(String.format("var testStartTime=%d; return %s", startTime, getScript()));
    }

    public static boolean isNeedRunAudit(){
        return new File("agentTemp/testInfo.properties").exists();
    }

    public static AuditConfig getAuditConfig(){
        AuditConfig config = ConfigFactory.create(AuditConfig.class);
        try {
            FileUtils.forceDelete(new File("agentTemp/testInfo.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }

    private static String getReportName(String testName,String folder){
        return folder+"/"+testName+"_"+getTime()+".html";
    }

    public static void writeHtmlToFile(HttpResponse response, String testName, String folder){
        try {
            FileUtils.copyInputStreamToFile(response.getEntity().getContent(),new File(getReportName(testName,folder)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
