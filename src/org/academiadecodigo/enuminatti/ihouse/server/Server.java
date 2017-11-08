package org.academiadecodigo.enuminatti.ihouse.server;

import java.io.*;
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

    public void start(ServerSocket socket) {

        while (true) {
            try {
                Socket clientSocket = socket.accept();
                System.out.println("connect");
                ServerWorker svWorker = new ServerWorker(clientSocket);
                threadPool.submit(svWorker);
                workerList.add(svWorker);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public class ServerWorker implements Runnable {

        private Socket clientSocket;
        private BufferedReader reader;
        private BufferedWriter writer;

        public ServerWorker(Socket socket) {
            this.clientSocket = socket;
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new BufferedWriter(new PrintWriter(clientSocket.getOutputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    String input = reader.readLine();
                    System.out.println(input);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
