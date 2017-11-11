package org.academiadecodigo.enuminatti.ihouse.server;

import org.academiadecodigo.enuminatti.ihouse.server.model.House;

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

    //will extend application, someday..

    private ExecutorService threadPool;
    private LinkedList<ServerWorker> workerList;
    private ServerSocket svSocket;
    private House house;

    public static void main(String[] args) {

        //Initialize a server, a thread pool and a worker list
        Server server = new Server();
        server.threadPool = Executors.newCachedThreadPool();
        server.workerList = new LinkedList<>();
        server.house = new House();

        try {
            server.svSocket = new ServerSocket(8080);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Create a new thread accepting clients
        AcceptClients acceptThread = server.new AcceptClients(server.svSocket);
        server.threadPool.submit(acceptThread);

    }


    class AcceptClients implements Runnable {

        ServerSocket svSocket;

        //Constructor
        public AcceptClients(ServerSocket svSocket) {
            this.svSocket = svSocket;
        }


        @Override
        public void run() {


            while (true) {

                //Blocking method. Accepting clients
                try {
                    Socket clientSocket;
                    clientSocket = svSocket.accept();
                    System.out.println("Client connected");
                    ServerWorker svWorker = new ServerWorker(clientSocket);
                    threadPool.submit(svWorker);
                    workerList.add(svWorker);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class ServerWorker implements Runnable {

        private Socket clientSocket;
        private String clientMessage;
        private BufferedReader reader;
        private PrintWriter writer;

        public ServerWorker(Socket socket) {
            this.clientSocket = socket;
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(clientSocket.getOutputStream(),true);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run() {

            clientMessage = house.sendUpdate();
            writer.println(clientMessage);

            while (true) {

                try {
                    //read command from client
                    clientMessage = reader.readLine();
                    clientMessage = house.sendUpdate();
                    if (clientMessage.equals("null")) {
                        System.out.println("Client disconnected");
                        disconnect();
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("Server: " + clientMessage);
                broadcast((clientMessage + "\n").getBytes());
                System.out.println("Sent message from server: " + clientMessage);

            }
        }

        //Close socket/buffer
        private void disconnect() {

            try {
                reader.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    //send commands to all clients/ BROADCAST
    private void broadcast(byte[] message) {

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




