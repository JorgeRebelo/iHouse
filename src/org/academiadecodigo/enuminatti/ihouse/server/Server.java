package org.academiadecodigo.enuminatti.ihouse.server;

import org.academiadecodigo.enuminatti.ihouse.server.model.House;
import org.academiadecodigo.enuminatti.ihouse.server.model.ReadWrite;
import org.academiadecodigo.enuminatti.ihouse.server.model.User;
import org.academiadecodigo.enuminatti.ihouse.server.model.UserManager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by codecadet on 07/11/17.
 */
public class Server {

    //will extend application, someday..

    private ExecutorService threadPool;
    private LinkedList<ServerWorker> workerList;
    private ServerSocket svSocket;
    private House house;
    private ReadWrite readWrite;

    public static void main(String[] args) {

        //Initialize a server, a thread pool and a worker list. Give him an imaginary house to work with.
        Server server = new Server();


        try {
            server.svSocket = new ServerSocket(8081);
            server.threadPool = Executors.newCachedThreadPool();
            server.workerList = new LinkedList<>();
            server.house = new House();
            server.readWrite = new ReadWrite();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Create a new thread accepting clients
        ConnectionThread acceptThread = server.new ConnectionThread(server.svSocket);
        server.threadPool.submit(acceptThread);


    }

    private void broadcast(String message) {

        //Iterate all workers to write to their sockets
        for (ServerWorker svWorker : workerList) {
            svWorker.write(message);
        }

    }

    class ConnectionThread implements Runnable {

        ServerSocket svSocket;
        UserManager userManager;

        ConnectionThread(ServerSocket svSocket) {
            this.svSocket = svSocket;
        }


        @Override
        public void run() {

            while (true) {

                try {
                    //Create a socket, accept that socket, create a new thread and submit the worker to a thread pool
                    //and a worker list
                    Socket clientSocket;
                    clientSocket = svSocket.accept();
                    System.out.println("\n" + "--------------------" + "\n" + ">Client connected");

                    UserAuthenticator validator = new UserAuthenticator(clientSocket);
                    threadPool.submit(validator);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class UserAuthenticator implements Runnable {

        Socket clientSocket;
        BufferedReader reader;
        UserManager userManager;
        String username;
        String password;

        UserAuthenticator(Socket socket){
            clientSocket = socket;
            userManager = new UserManager();
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException e) {
                System.out.println("Can't create reader to authenticate user");
            }
        }

        private void validate(){

            String input = null;

            try {
                input = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //needs regex of protocol
            String[] splitInput = input.split("-");

            username = splitInput[0];
            password = splitInput[1];
        }




        @Override
        public void run() {
            while(!userManager.authenticate(username, password)){
                validate();
            }

            User user = new User(username, password);
            ServerWorker svWorker = new ServerWorker(clientSocket, user);
            threadPool.submit(svWorker);
            workerList.add(svWorker);
        }
    }

    class ServerWorker implements Runnable {

        private User user;
        private Socket clientSocket;
        private String clientCMD;
        private String houseState;
        private BufferedReader reader;
        private PrintWriter writer;

        //Constructor, recieves the client's socket and instantiates a new reader and writer
        public ServerWorker(Socket socket, User user) {
            clientSocket = socket;
            this.user = user;
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(clientSocket.getOutputStream(), true);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run() {



            /**
             *
             *             TO-DO:   - Server database (file)
             *                      - User class setters
             *                      - Iterator for searching in user database
             *
             *
             *             We will read the next(basically first) line and associate
             *             the message to the user name and password of this socket.
             *
             *             After that, the server enters a loop where it won't let you
             *             do actions(go on to the next infinite loop) until you enter
             *             a valid login (from the server database).
             *             *hint: if(file).contains*
             *
             *             When we finally leave the loop (and the house view from the client
             *             is already loaded), this server worker starts reading a different
             *             type of string, which we already did down below.
             *
             */





            //Send the first update of how the server is currently
            houseState = readWrite.read("resources/saveFile");
            writer.println(houseState);
            System.out.println(">First update sent" + "\n" + "--------------------");


            while (true) {

                try {
                    //read command from client
                    clientCMD = reader.readLine();
                    if (clientCMD == null) {
                        System.out.println("\n" + ">Client disconnected" + "\n" + "------------------");
                        disconnect();
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("\n" + "------------NEW UPDATE-----------");

                //House receives update from client and updates itself
                house.receiveUpdate(clientCMD);
                System.out.println("Server command: " + clientCMD);

                //A new string is attributed to houseState to broadcast it to all clients
                houseState = house.sendUpdate();
                broadcast(houseState);
                System.out.println("Command broadcast to all clients");

            }
        }

        public void write(String content) {
            writer.println(content);
        }

        //Close socket/buffer
        private void disconnect() {

            try {
                reader.close();
                clientSocket.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}




