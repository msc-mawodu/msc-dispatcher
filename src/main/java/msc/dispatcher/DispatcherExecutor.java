package msc.dispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherExecutor implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherExecutor.class);

    ProfilerCacheStore cacheStore;
    DispatcherClient client;

    public DispatcherExecutor(ProfilerCacheStore cacheStore, DispatcherClient client) {
        this.cacheStore = cacheStore;
        this.client = client;
    }

    @Override
    public void run() {
        logger.info("Dispatching profiling data data.");
        String profilingBatchData = cacheStore.fetchAllProfilerEntries();
        client.post(profilingBatchData);
    }

}
