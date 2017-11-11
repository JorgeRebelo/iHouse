package org.academiadecodigo.enuminatti.ihouse.client.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.academiadecodigo.enuminatti.ihouse.client.utils.Navigation;

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

    @FXML // fx:id="logOutButton"
    private Button logOutButton; // Value injected by FXMLLoader

    @FXML
    void onBathroomLightButton(ActionEvent event) {

        if (bathroomLightButton.getText().equals("OFF")) {
            bathroomLightButton.setText("ON");
            bathroomLightButton.getStyleClass().add("ON");
            return;
        }

        if (bathroomLightButton.getText().equals("ON")) {
            bathroomLightButton.setText("OFF");
            bathroomLightButton.getStyleClass().add("OFF");
            return;
        }
    }

    @FXML
    void onBedroomLightButton(ActionEvent event) {

        if (bedroomLightButton.getText().equals("OFF")) {
            bedroomLightButton.setText("ON");
            bedroomLightButton.getStyleClass().add("ON");
            return;
        }

        if (bedroomLightButton.getText().equals("ON")) {
            bedroomLightButton.setText("OFF");
            bedroomLightButton.getStyleClass().add("OFF");
            return;
        }
    }

    @FXML
    void onExitButton(ActionEvent event) {
        //Close all communications opened*******************************************************************************
        Platform.exit();
    }

    @FXML
    void onKitchenLightButton(ActionEvent event) {

        if (kitchenLightButton.getText().equals("OFF")) {
            kitchenLightButton.setText("ON");
            kitchenLightButton.getStyleClass().add("ON");
            return;
        }

        if (kitchenLightButton.getText().equals("ON")) {
            kitchenLightButton.setText("OFF");
            kitchenLightButton.getStyleClass().add("OFF");
            return;
        }

    }

    @FXML
    void onLivingroomLightButton(ActionEvent event) {

        if (livingroomLightButton.getText().equals("OFF")) {
            livingroomLightButton.setText("ON");
            livingroomLightButton.getStyleClass().add("ON");
            return;
        }

        if (livingroomLightButton.getText().equals("ON")) {
            livingroomLightButton.setText("OFF");
            livingroomLightButton.getStyleClass().add("OFF");
            return;
        }

    }

    @FXML
    void onLogoutButton(ActionEvent event) {

        Navigation.getInstance().back();
    }

    @FXML
    void onMasterBedroomLightButton(ActionEvent event) {

        if (masterBedroomLightButton.getText().equals("OFF")) {
            masterBedroomLightButton.setText("ON");
            masterBedroomLightButton.getStyleClass().add("ON");
            return;
        }

        if (masterBedroomLightButton.getText().equals("ON")) {
            masterBedroomLightButton.setText("OFF");
            masterBedroomLightButton.getStyleClass().add("OFF");
            return;
        }

    }

    public void doAction(String status){
        System.out.println("Cheguei");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                String[] lamp = status.split("/");
                System.out.println("Reached recieveUpdate");

                for (int i = 0; i < lamp.length ; i++) {

                    String[] status = lamp[i].split("=");

                    switch(status[0]){
                        case "masterBedroomLightButton":
                            masterBedroomLightButton.setText(status[1]);
                            break;
                    }
                }
            }
        });
    }
}
