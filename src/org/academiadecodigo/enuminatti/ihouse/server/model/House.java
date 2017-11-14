package org.academiadecodigo.enuminatti.ihouse.server.model;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by codecadet on 10/11/17.
 */
public class House {

    private Map<String, Integer> lamps;

    private ReadWrite readWrite;

    public House() {
        this.lamps = new HashMap<>();
        this.readWrite = new ReadWrite();
        putLamps();
    }

    public void createLamps(String idButton, Integer state) {
        lamps.put(idButton, state);
    }

    public void putLamps() {
        lamps.put("masterBedroomLightButton", 0);
        lamps.put("bedroomLightButton", 0);
        lamps.put("livingroomLightButton", 0);
        lamps.put("kitchenLightButton", 0);
        lamps.put("bathroomLightButton", 0);
    }

    public String sendUpdate() {
        String state = "";
        for (String key : lamps.keySet()) {
            state += key + "=" + String.valueOf(lamps.get(key).byteValue()) + "/";
        }

        readWrite.write(state, "resources/saveFile");

        return state;
    }

    public void receiveUpdate(String state) {

        String[] lamp = state.split("/");
        System.out.println("Updating the house...");

        for (int i = 0; i < lamp.length ; i++) {

            String[] status = lamp[i].split("=");
            lamps.put(status[0], Integer.parseInt(status[1]));
            System.out.println("House update> " + status[0] + status[1]);
        }

        System.out.println("Updated!");
    }
}
