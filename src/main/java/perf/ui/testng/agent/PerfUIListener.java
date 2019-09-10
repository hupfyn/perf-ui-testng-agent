package perf.ui.testng.agent;

import com.automation.remarks.video.RecorderFactory;
import com.automation.remarks.video.enums.RecorderType;
import com.automation.remarks.video.recorder.IVideoRecorder;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import perf.ui.testng.agent.helper.PerfUIHelper;
import perf.ui.testng.agent.helper.PerfUIVideoHelper;

public class PerfUIListener extends TestListenerAdapter {

    private IVideoRecorder recorder;
    private String videoPath;
    private long testStartTime;

    public PerfUIListener() {
        this.recorder = RecorderFactory.getRecorder(RecorderType.FFMPEG);
    }

    @Override
    public void onTestStart(ITestResult result) {
        if (PerfUIHelper.checkIsAnnotation(result)) {
            this.testStartTime = PerfUIHelper.getTime();
            this.recorder.start();
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        if (PerfUIHelper.checkIsAnnotation(result)) {
           this.videoPath = PerfUIVideoHelper.stopRecordig(result,this.recorder,true);
           PerfUIHelper.writeTestInfo(testStartTime,result,this.videoPath);
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        if (PerfUIHelper.checkIsAnnotation(result)) {
            this.videoPath = PerfUIVideoHelper.stopRecordig(result,this.recorder,false);
            PerfUIHelper.writeTestInfo(testStartTime,result,this.videoPath);
        }
    }
}
