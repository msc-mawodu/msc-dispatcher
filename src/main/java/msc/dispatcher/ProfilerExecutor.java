package msc.dispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
            this.poll();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void poll() throws ExecutionException, InterruptedException, IOException {
        logger.info("Gathering a new batch of performance data.");

        Future<String> profilerData = null;
        profilerData = profiler.getMonitoringDataBatch();

        while(!profilerData.isDone()) {
            Thread.sleep(500);
        }

        if (null != profilerData) {
            dataCache.save(profilerData.get());
        }

        poll(); // NB. Recursively call itself, as a main app loop.
    }
}
