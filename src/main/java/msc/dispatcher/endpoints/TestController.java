package msc.dispatcher.endpoints;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

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

    // NB. Endpoint primarily used for development purposes
    @PostMapping("/mock-file-paths")
    public String mockFilePaths(HttpServletRequest request, @RequestBody String requestBody) {
        logger.info(requestBody);
        return "FILE PATHS RECEIVED OK";
    }

    @PostMapping(value = "/mock-file/{id}", headers = ("content-type=multipart/*") , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String mockFile(HttpServletRequest request, @PathVariable String id, @RequestParam("file") MultipartFile file) {
        logger.info(String.format("Received file: %s, size: %s, on id: ", file.getOriginalFilename(), file.getSize(), id));
        return "FILE RECEIVED OK";
    }
}
