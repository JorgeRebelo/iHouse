package org.academiadecodigo.enuminatti.ihouse.client;

import javafx.application.Application;
import javafx.stage.Stage;
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
public class Client extends Application{

    private UserService userService;
    private Socket clientSocket;
    private ExecutorService executors = Executors.newFixedThreadPool(2);

    @Override
    public void init() {

        try {
            Client client = new Client();
            client.clientSocket = new Socket("localhost", 8080);
            SendThread sendThread = new SendThread(client.clientSocket);
            ReceiveThread receiveThread = new ReceiveThread(client.clientSocket);
            client.executors.submit(sendThread);
            client.executors.submit(receiveThread);

        } catch (Exception e) {
            System.out.println("Couldn't connect.");
        }

        userService = new MockUserService();
        userService.addUser(new User("admin", Security.getHash("admin")));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Navigation.getInstance().setStage(primaryStage);
        Navigation.getInstance().loadScreen("login");
        LoginController loginController = (LoginController) Navigation.getInstance().getController("login");
        loginController.setUserService(userService);

    }

    public static void main(String[] args) throws IOException {

        launch(args);
    }
}

class SendThread implements Runnable {

    private Socket clientSocket;

    public SendThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void write() {

        Scanner scanner = new Scanner(System.in);

        String sentence;

        sentence = scanner.nextLine();

        BufferedWriter outToServer;
        try {
            outToServer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            outToServer.write(sentence);
            System.out.println(Thread.currentThread().getName() + " on write");
            outToServer.newLine();
            outToServer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
        this.clientSocket=clientSocket;
    }


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

        if (sentence == null) {

            try {
                clientSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            //thread.interrupt();


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

