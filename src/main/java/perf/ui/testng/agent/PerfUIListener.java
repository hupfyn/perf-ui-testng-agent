package perf.ui.testng.agent;

import com.automation.remarks.video.recorder.IVideoRecorder;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import perf.ui.testng.agent.helper.PerfUIHelper;
import perf.ui.testng.agent.helper.PerfUIVideoHelper;

public class PerfUIListener extends TestListenerAdapter {

    private long testStartTime;
    private IVideoRecorder recoder;
    private String videoPath;

    @Override
    public void onTestStart(ITestResult result) {
        if (PerfUIHelper.checkIsAnnotation(result)) {
            this.testStartTime = PerfUIHelper.getTime();
            this.recoder = PerfUIVideoHelper.getRecorder();
            this.recoder.start();
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        if (PerfUIHelper.checkIsAnnotation(result)) {
           this.videoPath = PerfUIVideoHelper.stopRecordig(result,this.recoder,true);
           PerfUIHelper.writeTestInfo(testStartTime,true,result);
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        if (PerfUIHelper.checkIsAnnotation(result)) {
            this.videoPath = PerfUIVideoHelper.stopRecordig(result,this.recoder,false);
            PerfUIHelper.writeTestInfo(testStartTime,true,result);
        }
    }
}
