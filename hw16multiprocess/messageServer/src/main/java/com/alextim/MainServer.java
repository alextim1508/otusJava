package com.alextim;

import com.alextim.runner.ProcessRunner;
import com.alextim.runner.ProcessRunnerImpl;
import com.alextim.server.IdClient;
import com.alextim.server.SocketMessageServer;
import com.alextim.server.SocketMessageServerImpl;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainServer {

    private static final String CLIENT_FRONTEND_START_COMMAND_1 = String.format("java -jar ./hw16multiprocess/frontend/target/frontend.jar --server.port=%d -host localhost -port 5050 -id %s", 8090, IdClient.FRONTEND_1.toString());
    private static final String CLIENT_FRONTEND_START_COMMAND_2 = String.format("java -jar ./hw16multiprocess/frontend/target/frontend.jar --server.port=%d -host localhost -port 5050 -id %s", 8080, IdClient.FRONTEND_2.toString());
    private static final String CLIENT_BD_SERVICE_START_COMMAND_1 = String.format("java -jar ./hw16multiprocess/dataBaseServer/target/dataBaseServer.jar -host localhost -port 5050 -id %s", IdClient.BACKEND_1.toString());
    private static final String CLIENT_BD_SERVICE_START_COMMAND_2 = String.format("java -jar ./hw16multiprocess/dataBaseServer/target/dataBaseServer.jar -host localhost -port 5050 -id %s", IdClient.BACKEND_2.toString());
    private static final int CLIENT_START_DELAY_SEC = 2;

    public static void main( String[] args ) throws Exception {
             new MainServer().start();
    }

    private void start() throws Exception {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        startClient(executorService, CLIENT_FRONTEND_START_COMMAND_1);
        startClient(executorService, CLIENT_FRONTEND_START_COMMAND_2);

        startClient(executorService, CLIENT_BD_SERVICE_START_COMMAND_1);
        startClient(executorService, CLIENT_BD_SERVICE_START_COMMAND_2);

        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = new ObjectName("com.alextim:type=Server");

        SocketMessageServer server = new SocketMessageServerImpl();

        mBeanServer.registerMBean(server, objectName);

        server.start();

        executorService.shutdown();
    }

    private void startClient(ScheduledExecutorService executorService, String command) {
        executorService.schedule(() -> {
                try {
                    ProcessRunner process = new ProcessRunnerImpl();
                    process.start(command);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }, CLIENT_START_DELAY_SEC, TimeUnit.SECONDS);
    }
}
