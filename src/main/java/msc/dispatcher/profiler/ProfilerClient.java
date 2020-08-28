package msc.dispatcher.profiler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class ProfilerClient {

    private static final Logger logger = LoggerFactory.getLogger(ProfilerClient.class);

    @Value("${hub_domain}")
    private String HUB_DOMAIN;

    @Value("${hub_profiling_endpoint}")
    private String HUB_PROFILING_ENDPOINT;


    public ProfilerClient() {}

    public String makeGetRequest(String requestEndpoint)  {

        logger.info("Attempting ping the main hub.");

        String url = String.format("%s/%s", HUB_DOMAIN, requestEndpoint);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class);

    }

    public void makePostRequestWithProfilingData(String data) {
        logger.info("Attempting send performance data to the main hub.");

        String url = String.format("%s/%s", HUB_DOMAIN, HUB_PROFILING_ENDPOINT);
        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> map = new HashMap<>();
        map.put("ps-data", data);

        ResponseEntity<Void> response = restTemplate.postForEntity(url, map, Void.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            logger.info("Successfully sent performance data to the main hub.");
        } else {
            logger.error(String.format("Error performance data to the main hub, response code: %s", response.getStatusCode()));
        }
    }
}
