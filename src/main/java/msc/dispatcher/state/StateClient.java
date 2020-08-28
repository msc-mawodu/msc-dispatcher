package msc.dispatcher.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static msc.dispatcher.DispatcherApplication.applicationState;

public class StateClient {

    private static final Logger logger = LoggerFactory.getLogger(StateClient.class);

    @Value("${hub_domain}")
    private String HUB_DOMAIN;

    @Value("${hub_state_report_endpoint}")
    private String HUB_STATE_REPORT_ENDPOINT;

    @Value("${application_id}")
    private String APP_ID;

    @Value("${application_ip}")
    private String APP_IP;

    @Value("${application_name}")
    private String APP_NAME;

    @Value("${application_description}")
    private String APP_DESCRIPTION;



    public StateClient() {}

    public String get(String requestEndpoint)  {

        logger.info("Attempting ping the main hub.");

        String url = String.format("%s/%s", HUB_DOMAIN, requestEndpoint);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class);

    }

    public void post() {
        logger.info("Attempting send state data to the main hub.");

        String url = String.format("%s/%s", HUB_DOMAIN, HUB_STATE_REPORT_ENDPOINT);
        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> map = new HashMap<>();
        map.put("id", APP_ID);
        map.put("state", applicationState.toString());
        map.put("ip", APP_IP);
        map.put("name", APP_NAME);
        map.put("description", APP_DESCRIPTION);

        ResponseEntity<Void> response = restTemplate.postForEntity(url, map, Void.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            logger.info("Successfully sent state data to the main hub.");
        } else {
            logger.error(String.format("Error sending state data to the main hub, response code: %s", response.getStatusCode()));
        }
    }
}
