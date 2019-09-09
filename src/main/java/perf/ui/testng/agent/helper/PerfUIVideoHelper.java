package perf.ui.testng.agent.helper;

import com.automation.remarks.video.RecorderFactory;
import com.automation.remarks.video.RecordingUtils;
import com.automation.remarks.video.enums.RecorderType;
import com.automation.remarks.video.recorder.IVideoRecorder;
import org.testng.ITestResult;

import java.io.File;

public class PerfUIVideoHelper {

    public static IVideoRecorder getRecorder(){
        return RecorderFactory.getRecorder(RecorderType.FFMPEG);
    }

    public static String stopRecordig(ITestResult result, IVideoRecorder recorder,boolean testStatus){
        String fileName = getFileName(result);
        File file = recorder.stopAndSave(fileName);
        return RecordingUtils.doVideoProcessing(testStatus,file);

    }
    private static String getFileName(ITestResult result){
        return RecordingUtils.getVideoFileName(null,result.getName());
    }


}
