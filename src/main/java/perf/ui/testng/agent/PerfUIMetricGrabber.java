package perf.ui.testng.agent;

import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.WebDriver;
import perf.ui.testng.agent.config.AuditConfig;
import perf.ui.testng.agent.config.PerfUIConfig;
import perf.ui.testng.agent.helper.PerfUIHelper;
import perf.ui.testng.agent.http.PerfUIMetricSender;

public class PerfUIMetricGrabber {

    private PerfUIMetricSender metricSender;

    public PerfUIMetricGrabber() {
        metricSender = new PerfUIMetricSender(ConfigFactory.create(PerfUIConfig.class));
    }

    public void startAudit(WebDriver driver){
        if(PerfUIHelper.isNeedRunAudit()){
            AuditConfig auditConfig = PerfUIHelper.getAuditConfig();
            String auditResult = PerfUIHelper.getAuditResult(driver,auditConfig.startTime());
            metricSender.sendMetric(auditResult,auditConfig.videoPath(),auditConfig.testName());
        }
    }
}
