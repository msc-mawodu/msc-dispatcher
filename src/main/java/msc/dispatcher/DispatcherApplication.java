package msc.dispatcher;

import msc.dispatcher.state.ApplicationState;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DispatcherApplication {

    public static boolean hasNewlyAddedFilesInReportsFolder = false;
    public static ApplicationState applicationState = ApplicationState.IDLE;

	public static void main(String[] args) {
		SpringApplication.run(DispatcherApplication.class, args);
	}

}
