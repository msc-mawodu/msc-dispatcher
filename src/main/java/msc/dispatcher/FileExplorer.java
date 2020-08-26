package msc.dispatcher;

import java.io.File;

public class FileExplorer {

    private static String ROOT_PATH = "/Users/mwd/Desktop/Computer_Science/MSC_PROJECT/dissertation/";

    public FileExplorer() {}

    public String listFilesAndFolders() {

        File rootFolder = new File(ROOT_PATH);

        StringBuilder filesReference = new StringBuilder();

        File[] listOfFiles = rootFolder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                filesReference.append("File " + listOfFiles[i].getName() + "\n");
            } else if (listOfFiles[i].isDirectory()) {
                filesReference.append("Directory " + listOfFiles[i].getName() + "\n");
            }
        }

        return filesReference.toString();
    }
}
