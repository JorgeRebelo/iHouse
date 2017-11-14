package org.academiadecodigo.enuminatti.ihouse.client;

import javafx.application.Application;
import javafx.stage.Stage;
import org.academiadecodigo.enuminatti.ihouse.client.controller.HouseController;
import org.academiadecodigo.enuminatti.ihouse.client.model.User;
import org.academiadecodigo.enuminatti.ihouse.client.service.MockUserService;
import org.academiadecodigo.enuminatti.ihouse.client.service.UserService;
import org.academiadecodigo.enuminatti.ihouse.client.utils.Navigation;
import org.academiadecodigo.enuminatti.ihouse.client.utils.Security;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by codecadet on 31/10/17.
 */
public class Client extends Application {

    private UserService userService;
    private Socket clientSocket;
    private ExecutorService executors = Executors.newFixedThreadPool(2);
    private HouseController controller;
    private ReceiveThread receiveThread;


    @Override
    public void init() {

        //Initialize service
        userService = new MockUserService();
        userService.addUser(new User("admin", Security.getHash("admin")));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //Show UI
        Navigation.getInstance().setStage(primaryStage);
        Navigation.getInstance().loadScreen("house");
        HouseController houseController = (HouseController) Navigation.getInstance().getController("house");
        //loginController.setUserService(userService);

        //We need access to the controller from the class, so we store it
        controller = houseController;
        controller.setClient(this);

        try {
            //Initialize threads, sockets
            clientSocket = new Socket("localhost", 8081);
            receiveThread = new ReceiveThread();

            //Add threads to threadpool
            executors.submit(receiveThread);


        } catch (Exception e) {
            System.out.println("Couldn't connect.");
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

    public void write(String command) {

        System.out.println("--WRITE BLOCK--");
        BufferedWriter outToServer;
        try {

            ///THIS SOCKET IS FUCKIN EMPTY
            outToServer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            System.out.println("Buffered Writer created.");

            //send command to server
            outToServer.write(command);
            System.out.println(Thread.currentThread().getName() + " on write()");
            outToServer.newLine();
            outToServer.flush();

            System.out.println("Data flushed to server");

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("--WRITE BLOCK END--");
    }


//-------------------- THREAD --------------------//

    class ReceiveThread implements Runnable {
        BufferedReader bufferedReader = null;


        public ReceiveThread() {
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //receive status
        public void read() {

            System.out.println("--READ BLOCK--" + Thread.currentThread().getName());

            try {

                String sentence = null;
                sentence = bufferedReader.readLine();

                if (sentence == null) {
                    clientSocket.close();
                }

                System.out.println("Command read: " + sentence);


                System.out.println(Thread.currentThread().getName() + " on read()");
                controller.getCommand(sentence);
                System.out.println("Client House Updated!");


            } catch (IOException e1) {
                e1.printStackTrace();
            }

            System.out.println("----------" + "\n");
        }

        @Override
        public void run() {
            System.out.println("invoking the run " + Thread.currentThread().getName());
            while (true) {
                System.out.println("in the while");
                read();
            }
        }
    }
}

