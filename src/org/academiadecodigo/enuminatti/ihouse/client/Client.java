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
public class Client extends Application implements Runnable{

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("view/house.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private ExecutorService executor;
    private Socket clientSocket;
    private Thread thread;
    boolean keepRead = true;

    public Client() {

        try {
            clientSocket = new Socket("localhost", 8080);
            executor = Executors.newFixedThreadPool(3);
            executor.submit(this);
            System.out.println(Thread.currentThread().getName() + " on constructor");
            System.out.println(Thread.activeCount());
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void read() {

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String sentence = bufferedReader.readLine();
            System.out.println(Thread.currentThread().getName() + " on read");

            if (sentence == null) {

                clientSocket.close();
                thread.interrupt();
                keepRead = false;

            } else {

                System.out.println(sentence);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        Client client = new Client();

        client.thread = new Thread(client);
        client.thread.start();
        //launch();

        while (true) {

            client.write();
        }
    }

    @Override
    public void run() {

        while (keepRead) {
            read();
        }
    }
}
