package org.academiadecodigo.enuminatti.ihouse.client.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.academiadecodigo.enuminatti.ihouse.client.service.UserService;
import org.academiadecodigo.enuminatti.ihouse.client.utils.Navigation;

public class LoginController implements Controller {

    private UserService userService;

    @FXML // fx:id="introLoginButton"
    private Button introLoginButton; // Value injected by FXMLLoader

    @FXML // fx:id="introExitButton"
    private Button introExitButton; // Value injected by FXMLLoader

    @FXML // fx:id="passwordField"
    private PasswordField passwordField; // Value injected by FXMLLoader

    @FXML // fx:id="ipField"
    private TextField ipField; // Value injected by FXMLLoader

    @FXML // fx:id="usernameField"
    private TextField usernameField; // Value injected by FXMLLoader

    @FXML // fx:id="ipNotFoundLabel"
    private Label ipNotFoundLabel; // Value injected by FXMLLoader

    @FXML // fx:id="usernameNotFoundLabel"
    private Label usernameNotFoundLabel; // Value injected by FXMLLoader

    @FXML // fx:id="wrongPasswordLabel"
    private Label wrongPasswordLabel; // Value injected by FXMLLoader

    @FXML
    void onIntroExitButton(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void onIntroLoginButton(ActionEvent event) {

        if (ipField.getText().isEmpty()) {
            ipNotFoundLabel.setVisible(true);
            return;
        }

        if (usernameField.getText().isEmpty()) {
            usernameNotFoundLabel.setVisible(true);
            return;
        }

        if (passwordField.getText().isEmpty()) {
            wrongPasswordLabel.setVisible(true);
            return;
        }

        if (userService.authenticate(usernameField.getText(), passwordField.getText())) {
            Navigation.getInstance().loadScreen("house");

        } else {
            wrongPasswordLabel.setVisible(true);
        }

    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
