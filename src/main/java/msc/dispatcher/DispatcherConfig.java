package msc.dispatcher;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DispatcherConfig {

    @Bean
    FileExplorer fileExplorer() {
        return new FileExplorer();
    }
}
