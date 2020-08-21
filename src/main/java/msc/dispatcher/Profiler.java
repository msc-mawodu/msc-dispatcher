package msc.dispatcher;

import java.io.*;

public class Profiler {

//    private static String CMD = "watch -n 1 'ps -eo pid,ppid,cmd,%mem,%cpu'";

    // todo: to be used as a separate profiling tool.
//    private static final String CPU_PROFILE = "mpstat -P ALL 1"; // NB. requires: sudo apt install sysstat
    private static int BATCH_SIZE = 100;

    public void monitor() throws InterruptedException, IOException {
        File tempScript = bashScriptToRun();

        ProcessBuilder pb = new ProcessBuilder("bash", tempScript.toString());
        Process process = pb.start();
        String output = redirectConsoleOutputToString(process);
        process.waitFor();
        tempScript.delete();
        System.out.println(output);
    }

    private String redirectConsoleOutputToString(Process process) throws IOException {
        BufferedReader consoleBuffer = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder consoleOut = new StringBuilder();
        String line;
        while ( (line = consoleBuffer.readLine()) != null) {
            consoleOut.append(line);
            consoleOut.append(System.getProperty("line.separator"));
        }
        return consoleOut.toString();
    }

    private File bashScriptToRun() throws IOException {
        File tempScriptFile = File.createTempFile("script", null);

        Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
                tempScriptFile));
        PrintWriter printWriter = new PrintWriter(streamWriter);

        printWriter.println("#!/bin/bash");
        printWriter.println("for((i=1;i<=10;i+=2))");
        printWriter.println("do ps -af");
        printWriter.println("sleep 1");
        printWriter.println("done");

        printWriter.close();

        return tempScriptFile;
    }
}

