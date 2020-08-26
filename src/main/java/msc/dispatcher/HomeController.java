package msc.dispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    public HomeController() {}

    @GetMapping("/")
    public String index() {
        return "OK";
    }

    @GetMapping("/mock")
    public String mock(HttpServletRequest request) {
        logger.info("Receiving calls on mock-endpoint");
        return "OK";
    }
}
