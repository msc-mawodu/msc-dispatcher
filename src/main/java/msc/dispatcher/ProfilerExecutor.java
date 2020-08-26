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
    private StateCacheStore stateCache;

    public ProfilerExecutor(Profiler profiler, ProfilerCacheStore dataCache, StateCacheStore stateCache) {
        this.profiler = profiler;
        this.dataCache = dataCache;
        this.stateCache = stateCache;
    }

    @Override
    public void run() {
        try {
            this.poll();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void poll() throws ExecutionException, InterruptedException {
        System.out.println("GATHER ++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        logger.info("App poll");

        Future<String> profilerData = null;
        try {
            profilerData = profiler.getMonitoringDataBatch();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (null != profilerData) {
            dataCache.save(profilerData.get());
        }

        poll(); // Recursively call itself, as a main app loop.
    }
}
