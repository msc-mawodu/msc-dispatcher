package msc.dispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static msc.dispatcher.DispatcherApplication.applicationState;


@RestController
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    public HomeController() {}

    @GetMapping("/")
    public String index() {
        return String.format("This application is currently %s; For testing purposes uses /mock, /mock-json, /mock-state endpoints", applicationState.toString());
    }
}
