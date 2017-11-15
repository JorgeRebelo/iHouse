package org.academiadecodigo.enuminatti.ihouse.client.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.academiadecodigo.enuminatti.ihouse.client.Client;
import org.academiadecodigo.enuminatti.ihouse.client.utils.Navigation;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by codecadet on 08/11/17.
 */
public class HouseController implements Controller {

    private String houseStatus;
    private List<Button> lights;
    private Client client;


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


    //----------BUTTON METHODS----------//

    @FXML
    void onBathroomLightButton(ActionEvent event) {

        updateLights(bathroomLightButton, bathroomLightButton.getText());

        client.write(getHouseStatus());

    }

    @FXML
    void onBedroomLightButton(ActionEvent event) {

        updateLights(bedroomLightButton, bedroomLightButton.getText());

        client.write(getHouseStatus());

    }

    @FXML
    void onKitchenLightButton(ActionEvent event) {

        updateLights(kitchenLightButton, kitchenLightButton.getText());

        client.write(getHouseStatus());

    }

    @FXML
    void onLivingroomLightButton(ActionEvent event) {

        updateLights(livingroomLightButton, livingroomLightButton.getText());

        client.write(getHouseStatus());

    }

    @FXML
    void onMasterBedroomLightButton(ActionEvent event) {

        updateLights(masterBedroomLightButton, masterBedroomLightButton.getText());

        client.write(getHouseStatus());

    }

    @FXML
    void onLogoutButton(ActionEvent event) {

        Navigation.getInstance().back();
    }

    @FXML
    void onExitButton(ActionEvent event) {
        client.disconnect();
        //Close all communications opened*******************************************************************************
        Platform.exit();
    }



    //---------CONTROLLER METHODS----------//

    @FXML
    void initialize() {
        assert masterBedroomLightButton != null : "fx:id=\"masterBedroomLightButton\" was not injected: check your FXML file 'house.fxml'.";
        assert bedroomLightButton != null : "fx:id=\"bedroomLightButton\" was not injected: check your FXML file 'house.fxml'.";
        assert livingroomLightButton != null : "fx:id=\"livingroomLightButton\" was not injected: check your FXML file 'house.fxml'.";
        assert kitchenLightButton != null : "fx:id=\"kitchenLightButton\" was not injected: check your FXML file 'house.fxml'.";
        assert bathroomLightButton != null : "fx:id=\"bathroomLightButton\" was not injected: check your FXML file 'house.fxml'.";
        assert exitButton != null : "fx:id=\"exitButton\" was not injected: check your FXML file 'house.fxml'.";
        assert logOutButton != null : "fx:id=\"logOutButton\" was not injected: check your FXML file 'house.fxml'.";

        lights = new LinkedList<>();
        lights.add(masterBedroomLightButton);
        lights.add(bedroomLightButton);
        lights.add(livingroomLightButton);
        lights.add(kitchenLightButton);
        lights.add(bathroomLightButton);

    }

    //Get command from server
    public void getCommand(String serverCommand) {

        System.out.println("getCommand() executing command: " + serverCommand);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                System.out.println("Platform Runlater: " + Thread.currentThread().getName());

                String[] lamp = serverCommand.split("/");


                for (int i = 0; i < lamp.length; i++) {

                    String[] status = lamp[i].split("=");
                    System.out.println("Lamp :  " + status[0] + status[1] + " ");

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
                    }
                }
            }
        });

    }

    //Update the lights
    private void updateLights(Button button, String status) {
        if(status.equals("1") || status.equals("OFF")) {
            button.setText("ON");
            button.setStyle("-fx-background-color: yellow");
            return;
        }
        button.setText("OFF");
        button.setStyle("-fx-background-color: lightgray");
    }

    //Get message to send to server
    private String getHouseStatus(){

        houseStatus = "";

        for (int button = 0; button < lights.size(); button++) {
            String lightStatus = null;

            //for every iteration, know how the light is right now
            if(lights.get(button).getText().equals("OFF")){
                lightStatus = "0";
            } else { lightStatus = "1";}


            houseStatus += lights.get(button).getId() + "=" + lightStatus + "/";
        }

        System.out.println("Client house updated!");
        System.out.println("-----------------------" + "\n");
        return houseStatus;
    }

    public void setClient(Client client){
        this.client = client;
    }
}
