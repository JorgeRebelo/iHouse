package org.academiadecodigo.enuminatti.ihouse.client;

import javafx.application.Application;
import javafx.stage.Stage;

import org.academiadecodigo.enuminatti.ihouse.client.controller.LoginController;
import org.academiadecodigo.enuminatti.ihouse.client.model.User;
import org.academiadecodigo.enuminatti.ihouse.client.service.MockUserService;
import org.academiadecodigo.enuminatti.ihouse.client.service.UserService;
import org.academiadecodigo.enuminatti.ihouse.client.utils.Navigation;
import org.academiadecodigo.enuminatti.ihouse.client.utils.Security;

public class Main extends Application {

    private UserService userService;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {


    }

}

