package msc.dispatcher.endpoints;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    // NB. Endpoint primarily used for development purposes
    @GetMapping("/mock")
    public String mock(HttpServletRequest request) {
        logger.info("Receiving calls on mock-endpoint");
        return "TEST OK";
    }

    // NB. Endpoint primarily used for development purposes
    @PostMapping("/mock-json")
    public String mockJson(HttpServletRequest request, @RequestBody String requestBody) {
        logger.info(requestBody.substring(0, requestBody.length()/100));
        return "JSON RECEIVED OK";
    }

    // NB. Endpoint primarily used for development purposes
    @PostMapping("/mock-state")
    public String mockState(HttpServletRequest request, @RequestBody String requestBody) {
        logger.info(requestBody);
        return "STATE RECEIVED OK";
    }
}
