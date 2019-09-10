package perf.ui.testng.agent.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.Sources;


@LoadPolicy(Config.LoadType.FIRST)
@Sources({ "file:agentTemp/testInfo.properties"})
public interface AuditConfig extends Config {
    @Key("test.startTime")
    long startTime();

    @Key("test.name")
    String testName();

    @Key("test.videoPath")
    String videoPath();

}
