package org.academiadecodigo.enuminatti.ihouse.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


/**
 * Created by codecadet on 31/10/17.
 */
public class Client implements Runnable {

    Socket clientSocket;
    Thread thread;
    boolean keepRead = true;

    public Client() {

        try {
            clientSocket = new Socket("localhost", 8080);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write() {

        Scanner scanner = new Scanner(System.in);

        String sentence;

        sentence = scanner.nextLine();

        BufferedWriter outToServer = null;
        try {
            outToServer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            outToServer.write(sentence);
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
