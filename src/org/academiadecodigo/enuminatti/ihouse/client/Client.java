package org.academiadecodigo.enuminatti.ihouse.client;

import javafx.application.Application;
import javafx.stage.Stage;
import org.academiadecodigo.enuminatti.ihouse.client.controller.HouseController;
import org.academiadecodigo.enuminatti.ihouse.client.controller.LoginController;
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

    @Override
    public void init() {

        /*try {
            //Initialize threads, sockets
            clientSocket = new Socket("localhost", 8080);
            SendThread sendThread = new SendThread(clientSocket);
            ReceiveThread receiveThread = new ReceiveThread(clientSocket);
            //add threads to threadpool
            executors.submit(sendThread);
            executors.submit(receiveThread);


        } catch (Exception e) {
            System.out.println("Couldn't connect.");
        }*/

        //Initialize service
        userService = new MockUserService();
        userService.addUser(new User("admin", Security.getHash("admin")));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //show UI
        Navigation.getInstance().setStage(primaryStage);
        Navigation.getInstance().loadScreen("house");
        HouseController houseController = (HouseController) Navigation.getInstance().getController("house");
        //loginController.setUserService(userService);
        controller = houseController;
        System.out.println(controller);
        try {
            //Initialize threads, sockets
            clientSocket = new Socket("localhost", 8080);
            SendThread sendThread = new SendThread(clientSocket);
            ReceiveThread receiveThread = new ReceiveThread(clientSocket);
            //add threads to threadpool
            executors.submit(sendThread);
            executors.submit(receiveThread);


        } catch (Exception e) {
            System.out.println("Couldn't connect.");
        }

    }

    public static void main(String[] args) throws IOException {

        launch(args);
    }

    class SendThread implements Runnable {

        private Socket clientSocket;

        public SendThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        //write command to server
        public void write() {

            Scanner scanner = new Scanner(System.in);

            String sentence;
            //read from command
            sentence = scanner.nextLine();

            BufferedWriter outToServer;
            try {
                outToServer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                //send command to server
                outToServer.write(sentence);
                System.out.println(Thread.currentThread().getName() + " on write");
                outToServer.newLine();
                outToServer.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("RETURNING FROM WRITE");
        }

        @Override
        public void run() {
            while (true) {
                write();
            }
        }
    }

    class ReceiveThread implements Runnable {

        private Socket clientSocket;
        boolean keepRead;

        public ReceiveThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

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

            System.out.println("READING NOW");
            controller.doAction(sentence);


            if (sentence == null) {

                try {
                    clientSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }


            } else {

                System.out.println(sentence);

            }
        }

        @Override
        public void run() {
            while (!keepRead) {
                read();
            }
        }
    }
}

