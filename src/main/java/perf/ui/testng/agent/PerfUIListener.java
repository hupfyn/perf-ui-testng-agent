package perf.ui.testng.agent;

import com.automation.remarks.video.RecorderFactory;
import com.automation.remarks.video.enums.RecorderType;
import com.automation.remarks.video.recorder.IVideoRecorder;
import org.aeonbits.owner.ConfigFactory;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import perf.ui.testng.agent.config.PerfUIConfig;
import perf.ui.testng.agent.helper.PerfUIHelper;
import perf.ui.testng.agent.helper.PerfUIVideoHelper;
import perf.ui.testng.agent.http.PerfUIMetricSender;

public class PerfUIListener extends TestListenerAdapter {

    private PerfUIMetricSender metricSender;
    private IVideoRecorder recorder;
    private long testStartTime;

    public PerfUIListener() {
        this.recorder = RecorderFactory.getRecorder(RecorderType.FFMPEG);
        this.metricSender = new PerfUIMetricSender(ConfigFactory.create(PerfUIConfig.class));
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
        runAudit(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        runAudit(result);
    }

    private void runAudit(ITestResult result){
        if(PerfUIHelper.checkIsAnnotation(result)){
            String auditResult = PerfUIHelper.getAuditResult(PerfUIHelper.getDriver(result),this.testStartTime);
            String videoPath = PerfUIVideoHelper.stopRecordig(result, this.recorder);
            metricSender.sendMetric(auditResult, videoPath,PerfUIHelper.getTestName(result));
        }
    }
}
