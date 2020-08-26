package msc.dispatcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DispatcherApplication {

    public static boolean keepGatheringProfilingData = false;
    public static boolean hasNewlyAddedFilesInReportsFolder = false;

	public static void main(String[] args) {
		SpringApplication.run(DispatcherApplication.class, args);
	}

}
