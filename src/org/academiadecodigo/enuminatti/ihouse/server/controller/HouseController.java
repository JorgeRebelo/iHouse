package org.academiadecodigo.enuminatti.ihouse.server.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.academiadecodigo.enuminatti.ihouse.server.Server;

/**
 * Created by codecadet on 08/11/17.
 */
public class HouseController implements Controller {


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

    @FXML
    private Label cenasLabel;

    @FXML
    void onBathroomLightButton(ActionEvent event) {

    }

    @FXML
    void onBedroomLightButton(ActionEvent event) {

    }

    @FXML
    void onExitButton(ActionEvent event) {
        //Close all communications opened*******************************************************************************
        Platform.exit();
    }

    @FXML
    void onKitchenLightButton(ActionEvent event) {

    }

    @FXML
    void onLivingroomLightButton(ActionEvent event) {

    }

    @FXML
    void onMasterBedroomLightButton(ActionEvent event) {

    }

    public void changeButtonText(){
        masterBedroomLightButton.setText("cona");

    }


}
