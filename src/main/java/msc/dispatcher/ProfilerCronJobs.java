package msc.dispatcher;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

public class ProfilerCronJobs {

    private Logger logger = LoggerFactory.getLogger(ProfilerCacheStore.class);

    // Every 1 minute
    @Scheduled(cron = "0 7/1 * * * ?")
    public void triggerDbClear() {
        logger.info("Should clear DB");
    }

    // Every 1 minute
    @Scheduled(cron = "0 7/1 * * * ?")
    public void triggerDataDispatch() {
        logger.info("Should dispatch data to external endpoint");
    }
}
