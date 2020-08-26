package msc.dispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

public class ProfilerCronJobs {

    private static final Logger logger = LoggerFactory.getLogger(ProfilerCacheStore.class);
    private static boolean isRunning = false;

    private ProfilerExecutor profilerExecutor;
    private DispatcherExecutor dispatcherExecutor;

    public ProfilerCronJobs(ProfilerExecutor profilerExecutor, DispatcherExecutor dispatcherExecutor) {
        this.profilerExecutor = profilerExecutor;
        this.dispatcherExecutor = dispatcherExecutor;
    }

    // Every 1 second
    @Scheduled(cron = "*/1 * * * * *")
    public void triggerDataDispatch() {
        logger.info("Data dispatch initiated.");
        Thread dispatcherExecutorThread = new Thread(dispatcherExecutor);
        dispatcherExecutorThread.start();
    }

    // Every 2 seconds
    @Scheduled(cron = "*/2 * * * * *")
    public void checkApplicationState() {
        logger.info("Application state check.");
        if (!isRunning) {
            isRunning = true;
            Thread profilerExecutorThread = new Thread(profilerExecutor);
            profilerExecutorThread.start();
        }
    }
}
