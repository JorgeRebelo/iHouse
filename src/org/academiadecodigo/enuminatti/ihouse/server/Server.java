package org.academiadecodigo.enuminatti.ihouse.server;

import javafx.application.Application;
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

    private ServerSocket svSocket;

    @Override
    public void init() throws Exception {

        //Initialize threads, sockets, lists and add threads to threadpool
        threadPool = Executors.newCachedThreadPool();
        workerList = new LinkedList<>();
        svSocket = new ServerSocket(8080);
        AcceptClients acceptThread = new AcceptClients(svSocket);
        threadPool.submit(acceptThread);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        try {
            //Show UI
            Parent root = FXMLLoader.load(getClass().getResource("view/house.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.show();



        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public static void main(String[] args) {


        launch();
    }


    class AcceptClients implements Runnable {

        ServerSocket svSocket;

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

        public ServerWorker(Socket socket) {
            this.clientSocket = socket;
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        //close socket/buffer
        public void close() {

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
                    //read command from client
                    clientMessage = reader.readLine();
                    if (clientMessage.equals("null")) {
                        System.out.println("Client disconnected");
                        close();
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
    //send commands to all clients/ BROADCAST
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




