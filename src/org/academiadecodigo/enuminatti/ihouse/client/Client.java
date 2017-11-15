package org.academiadecodigo.enuminatti.ihouse.client;

import javafx.application.Application;
import javafx.stage.Stage;
import org.academiadecodigo.enuminatti.ihouse.client.controller.HouseController;
import org.academiadecodigo.enuminatti.ihouse.client.controller.LoginController;
import org.academiadecodigo.enuminatti.ihouse.client.service.ServiceRegistry;
import org.academiadecodigo.enuminatti.ihouse.server.model.User;
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

    private Socket clientSocket;
    private ExecutorService executors = Executors.newFixedThreadPool(2);
    private HouseController controller;


    public static void main(String[] args) {
        launch(args);
    }


    //----------------JAVA FX-----------------//


    @Override
    public void init() {

        //Initialize service
        UserService userService = new MockUserService();
        userService.addUser(new User("admin", Security.getHash("admin")));

        ServiceRegistry.getServiceRegistry().registerService(UserService.class.getSimpleName(), userService);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //navigation singleton and set stage
        Navigation navigation = Navigation.getInstance();
        navigation.getInstance().setStage(primaryStage);

        //load screen LOGIN
        navigation.loadScreen(LoginController.getNAME());

        //show the the LOGIN INTERFACE
        primaryStage.setTitle("iHouse");
        primaryStage.show();

        //CALL COMMUNICATION SERVICE

    }
}
