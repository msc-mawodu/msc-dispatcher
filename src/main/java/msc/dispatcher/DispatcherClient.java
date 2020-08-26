package msc.dispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.AsyncResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Future;

public class DispatcherClient {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherClient.class);

    @Value("${hub_domain}")
    private String HUB_DOMAIN;

    public DispatcherClient() {}

    public void post(String data) {
        logger.info("Sending data to specified endpoint.");
        // todo: implement
    }

    public Future<String> makeGetRequest(String requestEndpoint)  {

        logger.info("Attempting send performance data to the main hub.");
        int status = 0;
        try {

            String urlStr = new StringBuilder(String.format("%s/%s", HUB_DOMAIN, requestEndpoint))
                    .toString();

            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setConnectTimeout(3000);
            con.setReadTimeout(3000);
            con.setInstanceFollowRedirects(true);

            status = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            logger.info("Successfully sent performance data to the main hub.");
            return new AsyncResult<>(content.toString());

        } catch (IOException e) {
            // todo: handle if fails... re-try
            logger.error(String.format("Failed to perform get request"));
            return new AsyncResult<>(String.format("ERROR %s", status));
        }
    }
}
