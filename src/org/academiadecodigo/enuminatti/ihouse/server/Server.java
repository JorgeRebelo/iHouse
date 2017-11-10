package org.academiadecodigo.enuminatti.ihouse.server;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by codecadet on 07/11/17.
 */
public class Server extends Application{

    //will extend application

    private ExecutorService threadPool;
    private LinkedList<ServerWorker> workerList;

    public Server() {

    }

    @Override
    public void start(Stage primaryStage) throws Exception {



    }

    public static void main(String[] args) {


        Server server = new Server();
        server.threadPool = Executors.newCachedThreadPool();
        server.workerList = new LinkedList<>();

        ServerSocket svSocket;

        try {
            svSocket = new ServerSocket(8080);
            AcceptClients acceptThread = server.new AcceptClients(svSocket);
            server.threadPool.submit(acceptThread);
            while (true) {
                System.out.println("tas na boa");
                Thread.sleep(2000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException o) {
            System.out.println("Interrupted..");
        }
    }


    class AcceptClients implements Runnable {

        ServerSocket svSocket;

        public AcceptClients(ServerSocket svSocket) {
            this.svSocket = svSocket;
        }

        @Override
        public void run() {


            while (true) {

                //Blocking method accepting clients
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

        public ServerWorker(Socket socket) {
            this.clientSocket = socket;
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public void tryClose() {

            try {
                reader.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {

            while (true) {

                try {
                    clientMessage = reader.readLine();
                    if (clientMessage.equals("null")) {
                        System.out.println("Client disconnected");
                        tryClose();
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
    }

    public void broadcast(byte[] message) {

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




