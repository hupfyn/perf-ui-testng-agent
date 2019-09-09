package perf.ui.testng.agent.helper;

import org.apache.commons.io.FileUtils;
import org.testng.ITestResult;
import perf.ui.testng.agent.annotations.PerfUI;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class PerfUIHelper {


    public static boolean checkIsAnnotation(ITestResult result){
        return result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(PerfUI.class) != null;
    }

    public static String getTestName(ITestResult result){
        String annotationName = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(PerfUI.class).name();
        return annotationName.equals("")?result.getName():annotationName;
    }


    public static long getTime() {
        return new Date().getTime();
    }


    public static String generateAgentInfo(long startTime, boolean status, String testName) {
        return String.format("{\"startTime\":%d,\"status\":%s,\"pageName\":\"%s\",\"agent\":\"TestNG\"}", startTime, status, testName);
    }

    public static void writeTestInfo(long startTime,boolean status, ITestResult result){
        String dataToWrite = generateAgentInfo(startTime,status,getTestName(result));
        try {
            FileUtils.writeStringToFile(new File("agentTemp/testInfo.json"),dataToWrite,"utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
