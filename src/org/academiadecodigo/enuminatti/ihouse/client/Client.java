package org.academiadecodigo.enuminatti.ihouse.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by codecadet on 31/10/17.
 */
public class Client {

    /*@Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("view/house.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/
    public static void main(String[] args) throws IOException {

        try {
            Socket clientSocket = new Socket("localhost", 8080);
            SendThread sendThread = new SendThread(clientSocket);
            Thread thread = new Thread(sendThread);
            thread.start();
            ReceiveThread receiveThread = new ReceiveThread(clientSocket);
            Thread thread2 = new Thread(receiveThread);
            thread2.start();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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

