package perf.ui.testng.agent.http;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import perf.ui.testng.agent.config.PerfUIConfig;
import perf.ui.testng.agent.helper.PerfUIHelper;
import perf.ui.testng.agent.helper.PerfUIVideoHelper;

import java.io.File;
import java.io.IOException;

public class PerfUIMetricSender {

    private PerfUIConfig config;

    public PerfUIMetricSender(PerfUIConfig config) {
        this.config = config;
        PerfUIVideoHelper.setConfigValueForRecorder(config);
    }

    public void sendMetric(String dataToSend, String videoPath, String testName) {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(getHostAddress());
        httpPost.setEntity(generateRequestBody(dataToSend,new File(videoPath)));
        try {
            FileUtils.writeStringToFile(new File(String.format("PerfUiDebug/send_data_%d.json", PerfUIHelper.getTime())),dataToSend,"utf-8");
            PerfUIHelper.writeHtmlToFile(client.execute(httpPost),testName,config.folder());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getHostAddress(){
        return config.webHook();
    }

    private HttpEntity generateRequestBody(String dataToSend, File file){
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addBinaryBody("video",file, ContentType.create("video/mp4"),"video.mp4");
        builder.addTextBody("data",dataToSend,ContentType.APPLICATION_JSON);
        return builder.build();
    }

}
