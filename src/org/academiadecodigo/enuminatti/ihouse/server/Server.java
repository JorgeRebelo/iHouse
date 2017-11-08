package org.academiadecodigo.enuminatti.ihouse.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by codecadet on 07/11/17.
 */
public class Server {


    private ExecutorService threadPool;
    private LinkedList<ServerWorker> workerList;

    public Server() {

    }

    public static void main(String[] args) {


        Server server = new Server();
        server.threadPool = Executors.newCachedThreadPool();
        server.workerList = new LinkedList<>();

        ServerSocket svSocket;

        try {
            //We need to accept with various IPs
            svSocket = new ServerSocket(8080);
            server.start(svSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }






    }

    public void start(ServerSocket socket){

        while(true){
            try {
                Socket clientSocket = socket.accept();
                ServerWorker svWorker = new ServerWorker(clientSocket);
                threadPool.submit(svWorker);
                workerList.add(svWorker);

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }




    public class ServerWorker implements Runnable {

        Socket clientSocket;

        public ServerWorker(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {

        }
    }
}
