package msc.dispatcher.filesystem;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilesystemClient {

    private static final Logger logger = LoggerFactory.getLogger(FilesystemClient.class);

    @Value("${application_id}")
    private String APP_ID;

    @Value("${hub_domain}")
    private String HUB_DOMAIN;

    @Value("${hub_file_paths_endpoint}")
    private String HUB_FILE_PATHS_ENDPOINT;

    @Value("${hub_file_endpoint}")
    private String HUB_FILE_ENDPOINT;

    public void sendFilePaths(List<String> filePaths) {
        logger.info("Attempting send report file paths update to the main hub.");

        String url = String.format("%s/%s", HUB_DOMAIN, HUB_FILE_PATHS_ENDPOINT);
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> map = new HashMap<>();
        map.put("id", APP_ID);
        map.put("paths", filePaths);

        restTemplate.postForEntity(url, map, Void.class);
    }

    public boolean sendMultipartFile(File file) throws IOException {
        logger.info(String.format("Attempting send report file %s to the main hub.", file.getName()));

        String url = String.format("%s/%s/%s", HUB_DOMAIN, HUB_FILE_ENDPOINT, APP_ID);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", new FileSystemResource(file));

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Void> response = restTemplate.postForEntity(url, request, Void.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return true;
        } else {
            return false;
        }
    }
}
