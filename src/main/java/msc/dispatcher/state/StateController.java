package msc.dispatcher.state;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static msc.dispatcher.DispatcherApplication.applicationState;

@RestController
public class StateController {

    @GetMapping("/app-start")
    public String start() {
        applicationState = ApplicationState.RUNNING;
        return "OK";
    }

    @GetMapping("/app-stop")
    public String stop() {
        applicationState = ApplicationState.IDLE;
        return "OK";
    }

    @GetMapping("/app-done")
    public String done() {
        applicationState = ApplicationState.COMPLETED;
        return "OK";
    }

}
