package msc.dispatcher;

import java.io.IOException;

public class ApplicationState {

    private Profiler profiler;
    private ProfilerCacheStore dataCache;
    private StateCacheStore stateCache;

    public ApplicationState(Profiler profiler, ProfilerCacheStore dataCache, StateCacheStore stateCache) {
        this.profiler = profiler;
        this.dataCache = dataCache;
        this.stateCache = stateCache;
    }

    public void poll() {
        String profilerData = null;
        try {
            profilerData = profiler.getMonitoringDataBatch();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (null != profilerData) {
            dataCache.save(profilerData);
        }

        poll(); // Recursively call itself, as a main app loop.
    }
}
