package msc.dispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.AsyncResult;

import java.io.*;
import java.util.concurrent.Future;

public class Profiler {

    private static final Logger logger = LoggerFactory.getLogger(Profiler.class);

    private static int BATCH_SIZE = 15;

    public Profiler() {}

    public Future<String> getMonitoringDataBatch() throws InterruptedException, IOException {

        logger.info("Attempting to gather performance data");

        File tempScript = bashScriptToRun();

        ProcessBuilder pb = new ProcessBuilder("bash", tempScript.toString());
        Process process = pb.start();
        String profilingData = redirectConsoleOutputToString(process);
        process.waitFor();
        tempScript.delete();
        return new AsyncResult<>(profilingData);
    }

    private String redirectConsoleOutputToString(Process process) throws IOException {
        BufferedReader consoleBuffer = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder consoleOutputBuffer = new StringBuilder();
        String line;
        while ( (line = consoleBuffer.readLine()) != null) {
            consoleOutputBuffer.append(line);
            consoleOutputBuffer.append(System.getProperty("line.separator"));
        }
        return consoleOutputBuffer.toString();
    }

    private File bashScriptToRun() throws IOException {
        // todo: to be used as a separate profiling tool.
        // "watch -n 1 'ps -eo pid,ppid,cmd,%mem,%cpu'";
        // "mpstat -P ALL 1"; // NB. requires: sudo apt install sysstat

        File tempScriptFile = File.createTempFile("script", null);

        Writer streamWriter = new OutputStreamWriter(new FileOutputStream(tempScriptFile));
        PrintWriter printWriter = new PrintWriter(streamWriter);

        printWriter.println("#!/bin/bash");
        printWriter.println(String.format("for((i=1;i<=%s;i+=2))", BATCH_SIZE));
        printWriter.println("do date");
        printWriter.println("ps -af");
        printWriter.println("sleep 1");
        printWriter.println("done");

        printWriter.close();

        return tempScriptFile;
    }
}

