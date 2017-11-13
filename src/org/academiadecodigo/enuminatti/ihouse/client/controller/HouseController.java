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

    /*public HouseController (Client client) {
        this.client = client;
    }*/

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

    @FXML // fx:id="masterBedroomLightButton"
    private Button masterBedroomLightButton; // Value injected by FXMLLoader

    @FXML // fx:id="bedroomLightButton"
    private Button bedroomLightButton; // Value injected by FXMLLoader

    @FXML // fx:id="livingroomLightButton"
    private Button livingroomLightButton; // Value injected by FXMLLoader

    @FXML // fx:id="kitchenLightButton"
    private Button kitchenLightButton; // Value injected by FXMLLoader

    @FXML // fx:id="bathroomLightButton"
    private Button bathroomLightButton; // Value injected by FXMLLoader

    @FXML // fx:id="exitButton"
    private Button exitButton; // Value injected by FXMLLoader

    @FXML // fx:id="logOutButton"
    private Button logOutButton; // Value injected by FXMLLoader

    @FXML
    void onBathroomLightButton(ActionEvent event) {

        if (bathroomLightButton.getText().equals("OFF")) {
            bathroomLightButton.setText("ON");
            bathroomLightButton.setStyle("-fx-background-color: yellow");
        } else {
            bathroomLightButton.setText("OFF");
            bathroomLightButton.setStyle("-fx-background-color: lightgray");
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                client.write(getHouseStatus());
            }
        });
    }

    @FXML
    void onBedroomLightButton(ActionEvent event) {

        if (bedroomLightButton.getText().equals("OFF")) {
            bedroomLightButton.setText("ON");
            bedroomLightButton.setStyle("-fx-background-color: yellow");

        } else {
            bedroomLightButton.setText("OFF");
            bedroomLightButton.setStyle("-fx-background-color: lightgray");
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                client.write(getHouseStatus());
            }
        });

    }


    @FXML
    void onKitchenLightButton(ActionEvent event) {

        if (kitchenLightButton.getText().equals("OFF")) {
            kitchenLightButton.setText("ON");
            kitchenLightButton.setStyle("-fx-background-color: yellow");

        } else {
            kitchenLightButton.setText("OFF");
            kitchenLightButton.setStyle("-fx-background-color: lightgray");
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                client.write(getHouseStatus());
            }
        });
    }


    @FXML
    void onLivingroomLightButton(ActionEvent event) {

        if (livingroomLightButton.getText().equals("OFF")) {
            livingroomLightButton.setText("ON");
            livingroomLightButton.setStyle("-fx-background-color: yellow");

        } else {
            livingroomLightButton.setText("OFF");
            livingroomLightButton.setStyle("-fx-background-color: lightgray");
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                client.write(getHouseStatus());
            }
        });
    }

    @FXML
    void onMasterBedroomLightButton(ActionEvent event) {

        if (masterBedroomLightButton.getText().equals("OFF")) {
            masterBedroomLightButton.setText("ON");
            masterBedroomLightButton.setStyle("-fx-background-color: yellow");
        } else {
            masterBedroomLightButton.setText("OFF");
            masterBedroomLightButton.setStyle("-fx-background-color: lightgray");
        }


        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                client.write(getHouseStatus());
            }
        });

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



    public void doAction(String status) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                System.out.println("received status 1 : " + status);
                String[] lamp = status.split("/");
                System.out.println("Received status: " + status);

                for (int i = 0; i < lamp.length; i++) {

                    String[] status = lamp[i].split("=");
                    System.out.println("Lamp :  " + status[0] + status[1] + " ");

                    switch (status[0]) {
                        case "masterBedroomLightButton":
                            if (status[1].equals("1")) {
                                masterBedroomLightButton.setText("ON");
                                masterBedroomLightButton.setStyle("-fx-background-color: yellow");
                            } else {
                                masterBedroomLightButton.setText("OFF");
                                masterBedroomLightButton.setStyle("-fx-background-color: lightgray");
                            }
                            break;
                        case "livingroomLightButton":
                            if (status[1].equals("1")) {
                                livingroomLightButton.setText("ON");
                                livingroomLightButton.setStyle("-fx-background-color: yellow");
                            } else {
                                livingroomLightButton.setText("OFF");
                                livingroomLightButton.setStyle("-fx-background-color: lightgray");
                            }
                            break;
                        case "bedroomLightButton":
                            if (status[1].equals("1")) {
                                bedroomLightButton.setText("ON");
                                bedroomLightButton.setStyle("-fx-background-color: yellow");
                            } else {
                                bedroomLightButton.setText("OFF");
                                bedroomLightButton.setStyle("-fx-background-color: lightgray");
                            }
                            break;
                        case "bathroomLightButton":
                            if (status[1].equals("1")) {
                                bathroomLightButton.setText("ON");
                                bathroomLightButton.setStyle("-fx-background-color: yellow");
                            } else {
                                bathroomLightButton.setText("OFF");
                                bathroomLightButton.setStyle("-fx-background-color: lightgray");
                            }
                            break;
                        case "kitchenLightButton":
                            if (status[1].equals("1")) {
                                kitchenLightButton.setText("ON");
                                kitchenLightButton.setStyle("-fx-background-color: yellow");
                            } else {
                                kitchenLightButton.setText("OFF");
                                kitchenLightButton.setStyle("-fx-background-color: lightgray");
                            }
                            break;
                    }
                }
            }
        });

    }

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

        System.out.println("Click message: " + houseStatus);
        return houseStatus;
    }

    public void setClient(Client client){
        this.client = client;
    }
}
