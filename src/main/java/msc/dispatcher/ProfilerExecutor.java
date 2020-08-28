package msc.dispatcher;

import msc.dispatcher.state.ApplicationState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static msc.dispatcher.DispatcherApplication.applicationState;

public class ProfilerExecutor implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ProfilerExecutor.class);

    private Profiler profiler;
    private ProfilerCacheStore dataCache;

    public ProfilerExecutor(Profiler profiler, ProfilerCacheStore dataCache) {
        this.profiler = profiler;
        this.dataCache = dataCache;
    }

    @Override
    public void run() {
        try {
            this.continuouslyGatherPerformanceMetrics();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void continuouslyGatherPerformanceMetrics() throws ExecutionException, InterruptedException, IOException {
        while(applicationState.equals(ApplicationState.RUNNING)) {
            logger.info("Gathering a new batch of performance data.");

            Future<String> profilerData = null;
            profilerData = profiler.getMonitoringDataBatch();

            while(!profilerData.isDone()) {
                Thread.sleep(500);
            }

            if (null != profilerData) {
                dataCache.save(profilerData.get());
            }
        }
    }
}
