package perf.ui.testng.agent.helper;

import com.automation.remarks.video.RecordingUtils;
import com.automation.remarks.video.recorder.IVideoRecorder;
import org.testng.ITestResult;

import java.io.File;

public class PerfUIVideoHelper {

    public static String stopRecordig(ITestResult result, IVideoRecorder recorder){
        String fileName = getFileName(result);
        File file = recorder.stopAndSave(fileName);
        return RecordingUtils.doVideoProcessing(false,file);

    }
    private static String getFileName(ITestResult result){
        return RecordingUtils.getVideoFileName(null,result.getName());
    }


}
