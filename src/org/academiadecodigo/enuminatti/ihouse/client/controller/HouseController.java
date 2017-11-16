package org.academiadecodigo.enuminatti.ihouse.client.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.academiadecodigo.enuminatti.ihouse.client.Client;
import org.academiadecodigo.enuminatti.ihouse.client.service.ServiceCommunication;
import org.academiadecodigo.enuminatti.ihouse.client.service.ServiceRegistry;
import org.academiadecodigo.enuminatti.ihouse.client.utils.Navigation;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by codecadet on 08/11/17.
 */
public class HouseController implements Controller {

    private static final String NAME = "house";

    private String houseStatus;
    static ServiceCommunication serviceCommunication1;
    private List<Button> elements;

    @FXML
    private Button masterBedroomLightButton;

    @FXML
    private Button bedroomLightButton;

    @FXML
    private Button livingroomLightButton;

    @FXML
    private Button kitchenLightButton;

    @FXML
    private Button bathroomLightButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button logOutButton;

    @FXML
    private Button masterBedroomBlind;

    @FXML
    private Button kitchenBlind;

    @FXML
    private Button livingroomBlind;

    @FXML
    private Button bedroomBlind;


    //----------BUTTON METHODS----------//
    @FXML
    void onBathroomLightButton(ActionEvent event) {

        updateLights(bathroomLightButton, bathroomLightButton.getText());
        //bathroomLightButton.setStyle("-fx-background-radius: 5em");

        serviceCommunication1.write(getHouseStatus());

    }

    @FXML
    void onBedroomLightButton(ActionEvent event) {

        updateLights(bedroomLightButton, bedroomLightButton.getText());
        //bedroomLightButton.setStyle("-fx-background-radius: 5em");
        serviceCommunication1.write(getHouseStatus());

    }

    @FXML
    void onKitchenLightButton(ActionEvent event) {

        updateLights(kitchenLightButton, kitchenLightButton.getText());
        //kitchenLightButton.setStyle("-fx-background-radius: 5em");
        System.out.println(getHouseStatus() + "  " + serviceCommunication1);
        serviceCommunication1.write(getHouseStatus());

    }

    @FXML
    void onLivingroomLightButton(ActionEvent event) {

        updateLights(livingroomLightButton, livingroomLightButton.getText());
        //livingroomLightButton.setStyle("-fx-background-radius: 5em");
        serviceCommunication1.write(getHouseStatus());

    }

    @FXML
    void onMasterBedroomLightButton(ActionEvent event) {

        updateLights(masterBedroomLightButton, masterBedroomLightButton.getText());
        //masterBedroomLightButton.setStyle("-fx-background-radius: 5em");
        serviceCommunication1.write(getHouseStatus());

    }

    @FXML
    void onLogoutButton(ActionEvent event) {

        Navigation.getInstance().back();
    }

    @FXML
    void onExitButton(ActionEvent event) {

        //Close all communications opened*******************************************************************************
        Platform.exit();
    }
    //---------BLIND BUTTON METHODS----------//

    @FXML
    void onBedroomBlind(ActionEvent event) {
        updateBlinds(bedroomBlind, bedroomBlind.getText());
        serviceCommunication1.write(getHouseStatus());
    }

    @FXML
    void onKitchenBlind(ActionEvent event) {
        updateBlinds(kitchenBlind, kitchenBlind.getText());
        serviceCommunication1.write(getHouseStatus());

    }

    @FXML
    void onLivingroomBlind(ActionEvent event) {
        updateBlinds(livingroomBlind, livingroomBlind.getText());
        serviceCommunication1.write(getHouseStatus());
    }

    @FXML
    void onMasterBedroomBlind(ActionEvent event) {
        updateBlinds(masterBedroomBlind, masterBedroomBlind.getText());
        serviceCommunication1.write(getHouseStatus());
    }


    //---------CONTROLLER METHODS----------//
    @FXML
    public void initialize() {

        elements = new LinkedList<>();
        elements.add(masterBedroomLightButton);
        elements.add(bedroomLightButton);
        elements.add(livingroomLightButton);
        elements.add(kitchenLightButton);
        elements.add(bathroomLightButton);
        elements.add(masterBedroomBlind);
        elements.add(kitchenBlind);
        elements.add(livingroomBlind);
        elements.add(bedroomBlind);

    }

    //Get command from server
    public void getCommand(String serverCommand) {

        System.out.println("getCommand() called");

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                System.out.println("Platform Runlater: " + Thread.currentThread().getName());

                String[] element = serverCommand.split("/");
                System.out.println("getCommand() executing command: " + serverCommand);

                for (int i = 0; i < element.length; i++) {

                    String[] status = element[i].split("=");
                    System.out.println("Element :  " + status[0] + " -> " + status[1] + " ");

                    //this is not good yet..
                    switch (status[0]) {
                        case "masterBedroomLightButton":
                            updateLights(masterBedroomLightButton, status[1]);
                            break;
                        case "livingroomLightButton":
                            updateLights(livingroomLightButton, status[1]);
                            break;
                        case "bedroomLightButton":
                            updateLights(bedroomLightButton, status[1]);
                            break;
                        case "bathroomLightButton":
                            updateLights(bathroomLightButton, status[1]);
                            break;
                        case "kitchenLightButton":
                            updateLights(kitchenLightButton, status[1]);
                            break;
                        case "livingroomBlind":
                            updateBlinds(livingroomBlind, status[1]);
                            break;
                        case "kitchenBlind":
                            updateBlinds(kitchenBlind, status[1]);
                            break;
                        case "masterBedroomBlind":
                            updateBlinds(masterBedroomBlind, status[1]);
                            break;
                        case "bedroomBlind":
                            updateBlinds(bedroomBlind, status[1]);
                            break;
                    }
                }
            }
        });
    }

    //Update the lights
    private void updateLights(Button button, String status) {
        if (status.equals("1") || status.equals("OFF")) {
            button.setText("ON");
            button.setStyle("-fx-background-color: yellow");
            //button.setStyle("-fx-background-radius: 10em");
            return;
        }
        button.setText("OFF");
        button.setStyle("-fx-background-color: lightgray");
    }

    //Update the blinds
    private void updateBlinds(Button button, String status) {
        if (status.equals("1") || status.equals("CLOSE")) {
            button.setText("OPEN");
            button.setStyle("-fx-background-color: deepskyblue");
            return;
        }
        button.setText("CLOSE");
        button.setStyle("-fx-background-color: white");
    }

    //Get message to send to server
    private String getHouseStatus() {

        houseStatus = "";

        for (Button element : elements) {
            String elementStatus;

            //for every iteration, know how the light is right now
            if (element.getText().equals("OFF") || element.getText().equals("CLOSE")) {
                elementStatus = "0";
            } else {
                elementStatus = "1";
            }

            houseStatus += element.getId() + "=" + elementStatus + "/";

        }

        System.out.println("ESTOU NA HOUSE");
        System.out.println("Client house updated!");
        System.out.println("-----------------------" + "\n");
        return houseStatus;
    }


    public static String getNAME() {
        return NAME;
    }

    public static void setServiceCommunication(ServiceCommunication serviceCommunication) {
        serviceCommunication1 = serviceCommunication;
    }
}
