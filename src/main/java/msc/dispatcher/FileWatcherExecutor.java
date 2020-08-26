package msc.dispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileWatcherExecutor implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(FileWatcherExecutor.class);

    @Value("${reports_path}")
    private String REPORTS_PATH;

    private FilenameCacheStore filenameCacheStore;

    @Override
    public void run() {
        logger.info(String.format("Report files check executing @%s", Thread.currentThread().getName()));
        List<String> files = listFilesAndFolders();
    }

    private List<String> listFilesAndFolders() {
        return listFilesAndFolders(REPORTS_PATH);
    }

    private List<String> listFilesAndFolders(String path) {

        File rootFolder = new File(path);

        if (null == rootFolder) {
            logger.error(String.format("No such directory: %s", path));
            return new ArrayList<>();
        }

        File[] filesInRootDirectory = rootFolder.listFiles();

        if(null == filesInRootDirectory || 0 == filesInRootDirectory.length) {
            return new ArrayList<>();
        }

        List<String> fileNames = new ArrayList<>();
        for(File file : filesInRootDirectory) {
            if (file.isFile()) {
                fileNames.add(file.getAbsolutePath());
            } else if (file.isDirectory()) {
                fileNames.addAll(listFilesAndFolders(file.getAbsolutePath()));
            }
        }

        return fileNames;
    }
}
