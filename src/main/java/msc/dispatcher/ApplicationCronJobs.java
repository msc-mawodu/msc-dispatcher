package msc.dispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import static msc.dispatcher.DispatcherApplication.keepGatheringProfilingData;

public class ApplicationCronJobs {

    private static final Logger logger = LoggerFactory.getLogger(ProfilerCacheStore.class);
    private static boolean isRunning = false;

    private ProfilerExecutor profilerExecutor;
    private DispatcherExecutor dispatcherExecutor;
    private FileWatcherExecutor fileWatcherExecutor;

    public ApplicationCronJobs(ProfilerExecutor profilerExecutor, DispatcherExecutor dispatcherExecutor, FileWatcherExecutor fileWatcherExecutor) {
        this.profilerExecutor = profilerExecutor;
        this.dispatcherExecutor = dispatcherExecutor;
        this.fileWatcherExecutor = fileWatcherExecutor;
    }

    // Every 10 seconds
    @Scheduled(cron = "*/10 * * * * *")
    public void triggerDataDispatch() {
        logger.info("Data dispatch initiated.");
        Thread dispatcherExecutorThread = new Thread(dispatcherExecutor);
        dispatcherExecutorThread.start();
    }

    // Every 15 seconds
    @Scheduled(cron = "*/15 * * * * *")
    public void checkApplicationState() {
        logger.info("Application state check.");
        if (!isRunning) {
            isRunning = true;
            keepGatheringProfilingData = true;
            Thread profilerExecutorThread = new Thread(profilerExecutor);
            profilerExecutorThread.start();
        }
    }

    // Every 5 seconds
    @Scheduled(cron = "*/5 * * * * *")
    public void checkReportFiles() {
        logger.info("Report files check start.");
        Thread fileWatcherExecutorThread = new Thread(fileWatcherExecutor);
        fileWatcherExecutorThread.start();
    }
}
