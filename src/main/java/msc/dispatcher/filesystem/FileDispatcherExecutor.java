package msc.dispatcher.filesystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileDispatcherExecutor implements Runnable {

     private static final Logger logger = LoggerFactory.getLogger(FileDispatcherExecutor.class);

    private FilenameCacheStore cache;
    private FilesystemClient client;

    public FileDispatcherExecutor(FilenameCacheStore filenameCacheStore, FilesystemClient client) {
        this.cache = filenameCacheStore;
        this.client = client;
    }

    @Override
    public void run() {
        logger.info("Attempting to dispatch files generated during pipeline execution.");
        List<String> filePaths = cache.fetchAllUndispatched();
        if (null != filePaths && 0 != filePaths.size()) {

            List<String> dispatchedFiles = new ArrayList<>();
            for(String filePath : filePaths) {
                boolean uploadSuccessfull = false;
                try {
                    uploadSuccessfull = client.sendMultipartFile(new File(filePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (uploadSuccessfull) {
                    dispatchedFiles.add(filePath);
                }
            }
            cache.markAsDispatched(dispatchedFiles);

        }
    }
}
