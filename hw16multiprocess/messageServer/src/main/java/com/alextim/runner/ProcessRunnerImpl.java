package com.alextim.runner;


import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ProcessRunnerImpl implements ProcessRunner {

    private final StringBuffer stringBuffer = new StringBuffer();

    private Process process;

    @Override
    public void start(String command) throws IOException {
        process = runProcess(command);
        try {
            Thread.sleep( TimeUnit.SECONDS.toMillis(10));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("Process status: {}", process.isAlive());
    }

    @Override
    public void stop() {
        process.destroy();
    }

    @Override
    public String getOutput() {
        return stringBuffer.toString();
    }

    private Process runProcess(String command) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        StreamListener output = new StreamListener(process.getInputStream(), "OUTPUT");
        output.start();

        return process;
    }


    private class StreamListener extends Thread {
        private final InputStream inputStream;
        private final String type;

        private StreamListener(InputStream is, String type) {
            this.inputStream = is;
            this.type = type;
        }

        @Override
        public void run() {
            try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream)){
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while((line = bufferedReader.readLine()) !=  null){
                    stringBuffer.append(type).append(">").append(line).append("\n");
                    log.info("> {}", line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}