package msc.dispatcher;

import javax.servlet.ServletContext;
import java.io.File;

public class FileExplorer {

    private ServletContext servletContext;

    // TODO: this will come from a configuration file.
    private static String ROOT_PATH = "/Users/mwd/Desktop/Computer_Science/MSC_PROJECT/dissertation/";

    public FileExplorer() {}

    public String listFiles() {

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
