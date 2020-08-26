package msc.dispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

public class ProfilerCronJobs {

    private static final Logger logger = LoggerFactory.getLogger(ProfilerCacheStore.class);
    private static boolean isRunning = false;

    private ProfilerExecutor executor;

    public ProfilerCronJobs(ProfilerExecutor executor) {
        this.executor = executor;
    }

    // Every 1 second
    @Scheduled(cron = "*/1 * * * * *")
    public void triggerDataDispatch() {
//        String profilingBatchData = cacheStore.fetchAllProfilerEntries();
        System.out.println("DISPATCH -------------------------------------------------");
        logger.info("Should dispatch data to external endpoint");
    }

    @Scheduled(cron = "*/2 * * * * *")
    public void checkApplicationState() {
        System.out.println("STATE -------------------------------------------------");
        if (!isRunning) {
            isRunning = true;
            Thread profilerExecutorThread = new Thread(executor);
            profilerExecutorThread.start();
        }
    }
}
