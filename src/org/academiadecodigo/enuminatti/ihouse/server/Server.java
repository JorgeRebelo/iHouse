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
                clientSocket = socket.accept();
                System.out.println("One more client...");
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
        private String clientMessage;
        private BufferedReader reader;
        private PrintWriter writer;

        public ServerWorker(Socket socket) {
            this.clientSocket = socket;
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                //writer = new PrintWriter((clientSocket.getOutputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        @Override
        public void run() {
            while (true) {
                try {
                    clientMessage = reader.readLine();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                System.out.println("Server: " + clientMessage);
                broadcast((clientMessage + "\n").getBytes());
                //clientList.get(i).outFromClient.write(message,0,message.length());
                System.out.println("Sent message from server: " + clientMessage);
                if (clientMessage.equals("Bye."))
                    break;
                /*input = reader.readLine();
                System.out.println(input + " is the message");
                writer.write(input, 0, input.length());*/
            }
        }
    }




    public void broadcast(byte[] message){
        for (int i = 0; i < workerList.size(); i++) {
            try {
                workerList.get(i).clientSocket.getOutputStream().write(message);
                workerList.get(i).clientSocket.getOutputStream().flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

