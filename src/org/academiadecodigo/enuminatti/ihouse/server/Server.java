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
                Socket clientSocket;
                System.out.println("One more client...");
                clientSocket = socket.accept();
                System.out.println("connect " + clientSocket.getPort());
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
        private String input;
        private BufferedReader reader;
        private BufferedWriter writer;

        public ServerWorker(Socket socket) {
            this.clientSocket = socket;
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new BufferedWriter(new PrintWriter(clientSocket.getOutputStream(), true));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        @Override
        public void run() {
            try {
                while ((input = reader.readLine()) != null) {
                    System.out.println("Server: " + input);
                    for (int i = 0; i < workerList.size(); i++) {
                            //workerList.get(i).clientSocket.getOutputStream().write(input.getBytes(),0,input.length());
                            workerList.get(i).writer.write(input);
                        //clientList.get(i).outFromClient.write(message,0,message.length());
                        System.out.println("Sent " + (i + 1) + " message");
                        System.out.println(workerList);
                        if (input.equals("Bye."))
                            break;
                    /*input = reader.readLine();
                    System.out.println(input + " is the message");
                    writer.write(input, 0, input.length());*/
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

