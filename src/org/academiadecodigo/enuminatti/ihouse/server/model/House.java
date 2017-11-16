package org.academiadecodigo.enuminatti.ihouse.server.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by codecadet on 10/11/17.
 */
public class House {

    private Map<String, Integer> elements;


    private ReadWrite readWrite;

    public House() {
        this.elements = new HashMap<>();
        this.readWrite = new ReadWrite();
        addElements();
    }

    // Add all interective elementes in the house
    public void addElements() {
        elements.put("masterBedroomLightButton", 0);
        elements.put("bedroomLightButton", 0);
        elements.put("livingroomLightButton", 0);
        elements.put("kitchenLightButton", 0);
        elements.put("bathroomLightButton", 0);
        elements.put("masterBedroomBlind", 0);
        elements.put("kitchenBlind", 0);
        elements.put("livingroomBlind", 0);
        elements.put("bedroomBlind", 0);

    }

    public String sendUpdate() {

        String state = "";
        for (String key : elements.keySet()) {
            state += key + "=" + String.valueOf(elements.get(key).byteValue()) + "/";
        }

        readWrite.write(state, "resources/saveFile");

        return state;
    }

    public void receiveUpdate(String state) {

        String[] elements = state.split("/");
        System.out.println("Updating the house...");

        for (int i = 0; i < elements.length ; i++) {

            String[] status = elements[i].split("=");
            this.elements.put(status[0], Integer.parseInt(status[1]));
            System.out.println("House update> " + status[0] + status[1]);
        }

        System.out.println("Updated!");
    }
}
