package msc.dispatcher.profiler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProfilerDispatcherExecutor implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ProfilerDispatcherExecutor.class);

    ProfilerCacheStore cacheStore;
    ProfilerClient client;

    public ProfilerDispatcherExecutor(ProfilerCacheStore cacheStore, ProfilerClient client) {
        this.cacheStore = cacheStore;
        this.client = client;
    }

    @Override
    public void run() {
        logger.info("Dispatching profiling data data.");
        String profilingBatchData = cacheStore.fetchAllProfilerEntries();
        client.makePostRequestWithProfilingData(profilingBatchData);
    }

}
