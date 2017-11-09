package org.academiadecodigo.enuminatti.ihouse.client;

import java.io.*;
import java.net.Socket;

/**
 * Created by codecadet on 07/11/17.
 */
public class Client {

    public static void main(String[] args) {
        try {
            Socket clientSocket = new Socket("localhost", 8080);
            SendThread sendThread = new SendThread(clientSocket);
            Thread thread1 = new Thread(sendThread);
            thread1.start();
            ReceiveThread receiveThread = new ReceiveThread(clientSocket);
            Thread thread2 = new Thread(receiveThread);
            thread2.start();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //Socket clientSocket = new Socket("localhost", 8080);
    //SendThread sendThread = new SendThread(clientSocket);


}

class SendThread implements Runnable {

    Socket socket;
    BufferedReader reader;
    PrintWriter writer;

    public SendThread(Socket socket) {
        this.socket = socket;
    }


    @Override
    public void run() {


        try {

            while (true) {
                writer = new PrintWriter(socket.getOutputStream());

                reader = new BufferedReader(new InputStreamReader(System.in));

                String command = null;


                try {
                    command = reader.readLine();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                if (command != null) {
                    System.out.println("Client: " + command);
                    writer.println(command);
                    writer.flush();
                }
            }

        } catch (IOException e) {

            System.out.println("Send thread failed " + e.getMessage());
        }
    }


    public void tryClose() {
        try {
            if (writer != null) {
                writer.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ReceiveThread implements Runnable {

    Socket socket;
    BufferedReader reader;

    public ReceiveThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String command;

            while ((command = reader.readLine()) != null) {
                System.out.println(command);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

