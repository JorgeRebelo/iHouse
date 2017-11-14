package org.academiadecodigo.enuminatti.ihouse.server;

import org.academiadecodigo.enuminatti.ihouse.server.model.House;
import org.academiadecodigo.enuminatti.ihouse.server.model.ReadWrite;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
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
    private ReadWrite readWrite = new ReadWrite();

    public static void main(String[] args) {

        //Initialize a server, a thread pool and a worker list. Give him an imaginary house to work with.
        Server server = new Server();
        server.threadPool = Executors.newCachedThreadPool();
        server.workerList = new LinkedList<>();
        server.house = new House();

        try {
            server.svSocket = new ServerSocket(8081);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Create a new thread accepting clients
        AcceptClients acceptThread = server.new AcceptClients(server.svSocket);
        server.threadPool.submit(acceptThread);

    }


    class AcceptClients implements Runnable {

        ServerSocket svSocket;

        public AcceptClients(ServerSocket svSocket) {
            this.svSocket = svSocket;
        }

        @Override
        public void run() {

            while (true) {

                try {
                    //Create a socket, accept that socket, create a new thread and submit the worker to a thread pool
                    //and a worker list
                    Socket clientSocket;
                    clientSocket = svSocket.accept();
                    System.out.println(">Client connected");

                    ServerWorker svWorker = new ServerWorker(clientSocket);
                    threadPool.submit(svWorker);
                    workerList.add(svWorker);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void broadcast(String message) {

        //Iterate all workers to write to their sockets
        for (ServerWorker svWorker : workerList) {
            svWorker.write(message);
        }

    }

    class ServerWorker implements Runnable {

        private Socket clientSocket;
        private String clientCMD;
        private String houseState;
        private BufferedReader reader;
        private PrintWriter writer;

        //Constructor, recieves the client's socket and instantiates a new reader and writer
        public ServerWorker(Socket socket) {
            this.clientSocket = socket;
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(clientSocket.getOutputStream(), true);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run() {

            //Send the first update of how the server is currently
            houseState = readWrite.read("resources/saveFile");
            writer.println(houseState);
            System.out.println(">First update sent");


            while (true) {

                try {
                    //read command from client
                    clientCMD = reader.readLine();
                    if (clientCMD == null) {
                        System.out.println(">Client disconnected");
                        disconnect();
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("\n" + "------------NEW UPDATE-----------");

                //House receives update from client and updates itself
                house.receiveUpdate(clientCMD);
                System.out.println("Server command: " + clientCMD);

                //A new string is attributed to houseState to broadcast it to all clients
                houseState = house.sendUpdate();
                broadcast(houseState);
                System.out.println("Command broadcast to all clients");

            }
        }

        public void write(String content) {
            writer.println(content);
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


}




