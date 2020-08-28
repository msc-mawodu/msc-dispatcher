package msc.dispatcher;

import msc.dispatcher.state.ApplicationState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import static msc.dispatcher.DispatcherApplication.applicationState;

public class Scheduler {

    private static final Logger logger = LoggerFactory.getLogger(ProfilerCacheStore.class);
    private static boolean isRunning = false;

    private ProfilerExecutor profilerExecutor;
    private DispatcherExecutor dispatcherExecutor;
    private FileWatcherExecutor fileWatcherExecutor;
    private StateReporterExecutor stateReporterExecutor;

    public Scheduler(ProfilerExecutor profilerExecutor, DispatcherExecutor dispatcherExecutor, FileWatcherExecutor fileWatcherExecutor, StateReporterExecutor stateReporterExecutor) {
        this.profilerExecutor = profilerExecutor;
        this.dispatcherExecutor = dispatcherExecutor;
        this.fileWatcherExecutor = fileWatcherExecutor;
        this.stateReporterExecutor = stateReporterExecutor;
    }
    // Every 5 seconds
    @Scheduled(cron = "*/5 * * * * *")
    public void healthCheck() {
        logger.info(String.format("App currently is %s, and performance batch runner is set to: %s", applicationState.toString(), isRunning));
    }

    // Every 10 seconds
    @Scheduled(cron = "*/10 * * * * *")
    public void triggerDataDispatch() {
        if (applicationState.equals(ApplicationState.RUNNING)) {
            logger.info("Data dispatch initiated.");
            Thread dispatcherExecutorThread = new Thread(dispatcherExecutor);
            dispatcherExecutorThread.start();
        }
    }

    // Every 15 seconds
    @Scheduled(cron = "*/15 * * * * *")
    public void checkPerformanceMonitorTriggering() {
        if (applicationState.equals(ApplicationState.RUNNING)) {
            if (!isRunning) {
                logger.info("Initiating performance data collection.");
                isRunning = true;
                Thread profilerExecutorThread = new Thread(profilerExecutor);
                profilerExecutorThread.start();
            }
        } else {
            isRunning = false;
        }
    }

    // Every 20 seconds
    @Scheduled(cron = "*/20 * * * * *")
    public void triggerFileWatcher() {
        if (applicationState.equals(ApplicationState.RUNNING)) {
            logger.info("Report files check start.");
            Thread fileWatcherExecutorThread = new Thread(fileWatcherExecutor);
            fileWatcherExecutorThread.start();
        }
    }

    // Every 5 seconds
    @Scheduled(cron = "*/5 * * * * *")
    public void triggerStatusReportDispatch() {
        logger.info("Pinging hub server with application status.");
        Thread stateReporterExecutorThread = new Thread(stateReporterExecutor);
        stateReporterExecutorThread .start();
    }
}
