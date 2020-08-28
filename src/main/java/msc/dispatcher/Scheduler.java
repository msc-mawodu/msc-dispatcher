package msc.dispatcher;

import msc.dispatcher.filesystem.FileDispatcherExecutor;
import msc.dispatcher.filesystem.FileWatcherExecutor;
import msc.dispatcher.profiler.ProfilerDispatcherExecutor;
import msc.dispatcher.profiler.ProfilerGathererExecutor;
import msc.dispatcher.state.ApplicationState;
import msc.dispatcher.state.StateReporterExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import static msc.dispatcher.DispatcherApplication.applicationState;

public class Scheduler {

    private static final Logger logger = LoggerFactory.getLogger(Scheduler.class);
    private static boolean isRunning = false;

    private ProfilerGathererExecutor profilerGathererExecutor;
    private ProfilerDispatcherExecutor profilerDispatcherExecutor;
    private FileWatcherExecutor fileWatcherExecutor;
    private StateReporterExecutor stateReporterExecutor;
    private FileDispatcherExecutor fileDispatcherExecutor;

    public Scheduler(ProfilerGathererExecutor profilerGathererExecutor, ProfilerDispatcherExecutor profilerDispatcherExecutor, FileWatcherExecutor fileWatcherExecutor, StateReporterExecutor stateReporterExecutor, FileDispatcherExecutor fileDispatcherExecutor) {
        this.profilerGathererExecutor = profilerGathererExecutor;
        this.profilerDispatcherExecutor = profilerDispatcherExecutor;
        this.fileWatcherExecutor = fileWatcherExecutor;
        this.stateReporterExecutor = stateReporterExecutor;
        this.fileDispatcherExecutor = fileDispatcherExecutor;
    }
    // Every 5 seconds
    @Scheduled(cron = "*/5 * * * * *")
    public void healthCheck() {
        logger.info(String.format("App currently is %s, and performance batch runner is set to: %s", applicationState.toString(), isRunning));
    }

    // Every 30 seconds
    @Scheduled(cron = "*/30 * * * * *")
    public void triggerDataDispatch() {
        if (applicationState.equals(ApplicationState.RUNNING)) {
            logger.info("Data dispatch initiated.");
            Thread dispatcherExecutorThread = new Thread(profilerDispatcherExecutor);
            dispatcherExecutorThread.start();
        }
    }

    // Every 30 seconds
    @Scheduled(cron = "*/30 * * * * *")
    public void checkPerformanceMonitorTriggering() {
        if (applicationState.equals(ApplicationState.RUNNING)) {
            if (!isRunning) {
                logger.info("Initiating performance data collection.");
                isRunning = true;
                Thread profilerExecutorThread = new Thread(profilerGathererExecutor);
                profilerExecutorThread.start();
            }
        } else {
            isRunning = false;
        }
    }

    // Every 5 seconds
    @Scheduled(cron = "*/5 * * * * *")
    public void triggerFileWatcher() {
        if (applicationState.equals(ApplicationState.RUNNING)) {
            logger.info("Report files check start.");
            Thread fileWatcherExecutorThread = new Thread(fileWatcherExecutor);
            fileWatcherExecutorThread.start();
        }
    }

    // Every 10 seconds
    @Scheduled(cron = "*/10 * * * * *")
    public void triggerFileDispatch() {
        if (applicationState.equals(ApplicationState.RUNNING)) {
            logger.info("Dispatch undispatched files to the hub.");
            Thread fileDispatcherExecutorThread = new Thread(fileDispatcherExecutor);
            fileDispatcherExecutorThread.start();
        }
    }

    // Every 30 seconds
    @Scheduled(cron = "*/30 * * * * *")
    public void triggerStatusReportDispatch() {
        logger.info("Pinging hub server with application status.");
        Thread stateReporterExecutorThread = new Thread(stateReporterExecutor);
        stateReporterExecutorThread .start();
    }
}
