package org.academiadecodigo.enuminatti.ihouse.client.service;

import org.academiadecodigo.enuminatti.ihouse.client.controller.Controller;
import org.academiadecodigo.enuminatti.ihouse.client.controller.HouseController;
import org.academiadecodigo.enuminatti.ihouse.client.controller.LoginController;
import org.academiadecodigo.enuminatti.ihouse.client.utils.Navigation;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by codecadet on 15/11/17.
 */
public class ServiceCommunication {

    private Socket clientSocket;
    private ExecutorService executors = Executors.newFixedThreadPool(2);
    private ServiceCommunication.ReceiveThread receiveThread;

    public void initiateConnection(String ip) {

        try {
            //Initialize threads, sockets
            clientSocket = new Socket(ip, 8080);
            receiveThread = new ReceiveThread();

            //Add threads to threadpool
            executors.submit(receiveThread);


        } catch (Exception e) {
            System.out.println("Couldn't connect.");
        }
    }

    //------------- WRITE TO SERVER ---------------//
    public void write(String command) {

        System.out.println("-----WRITE BLOCK------");
        System.out.println(command);
        BufferedWriter outToServer;
        try {

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

        System.out.println("-----------------------" + "\n");
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

            String sentence;

            try {

                while ((sentence = bufferedReader.readLine()) != null) {
                    System.out.println("------READ BLOCK------");
                    System.out.println(sentence);
                    Navigation.getInstance().getController(HouseController.getNAME()).getCommand(sentence);
                    //Navigation.getInstance().getController("house").getCommand(sentence);
                }
                disconnect();

            } catch (IOException e1) {
                e1.printStackTrace();
            }

            System.out.println("--------------------" + "\n");
        }

        @Override
        public void run() {
            System.out.println("Invoking the read: " + Thread.currentThread().getName() + "\n");
            read();
        }

    }

    public void disconnect() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


