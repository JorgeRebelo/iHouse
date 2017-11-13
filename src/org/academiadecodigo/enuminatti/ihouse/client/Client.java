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
import java.util.Scanner;
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

        BufferedWriter outToServer;
        try {

            ///THIS SOCKET IS FUCKIN EMPTY
            outToServer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            //send command to server
            outToServer.write(command);
            System.out.println(Thread.currentThread().getName() + " on write");
            outToServer.newLine();
            outToServer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("RETURNING FROM WRITE");
    }


//-------------------- THREAD --------------------//

    class ReceiveThread implements Runnable {

        //receive status
        public void read() {

            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            String sentence = null;
            try {
                sentence = bufferedReader.readLine();

            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " on read");
            System.out.println("Received from server: " + sentence);
            System.out.println("READING NOW");
            controller.doAction(sentence);


            if (sentence == null) {

                try {
                    clientSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        @Override
        public void run() {
            while (true) {
                read();
            }
        }
    }
}

