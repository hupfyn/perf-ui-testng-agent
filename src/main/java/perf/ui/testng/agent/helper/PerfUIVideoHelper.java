package perf.ui.testng.agent.helper;

import com.automation.remarks.video.RecordingUtils;
import com.automation.remarks.video.recorder.IVideoRecorder;
import org.testng.ITestResult;
import perf.ui.testng.agent.config.PerfUIConfig;

import java.io.File;
import java.util.Objects;

public class PerfUIVideoHelper {

    public static String stopRecordig(ITestResult result, IVideoRecorder recorder){
        String fileName = getFileName(result);
        File file = recorder.stopAndSave(fileName);
        return RecordingUtils.doVideoProcessing(false,file);

    }
    private static String getFileName(ITestResult result){
        return RecordingUtils.getVideoFileName(null,result.getName());
    }

    public static void setConfigValueForRecorder(PerfUIConfig config){
        System.setProperty("video.folder","PerfUiVideo");
        System.setProperty("video.save.mode","ALL");
        System.setProperty("video.frame.rate",config.frameRate());
        if(Objects.nonNull(config.videoDisplay())){
            System.setProperty("ffmpeg.display",config.videoDisplay());
        }
    }
}
