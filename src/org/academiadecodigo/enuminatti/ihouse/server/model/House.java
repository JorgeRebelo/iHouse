package org.academiadecodigo.enuminatti.ihouse.server.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by codecadet on 10/11/17.
 */
public class House {

    private Map<String, Integer> lamps;

    public House() {
        this.lamps = new HashMap<>();
        putLamps();
    }

    public void createLamps(String idButton, Integer state) {
        lamps.put(idButton, state);
    }

    public void putLamps() {
        lamps.put("masterBedroomLightButton", 0);
        lamps.put("bedroomLightButton", 0);
        lamps.put("livingroomLightButton", 1);
        lamps.put("kitchenLightButton", 0);
        lamps.put("bathroomLightButton", 0);
    }

    public String sendUpdate() {
        String state = "";
        for (String key : lamps.keySet()) {
            state += key;
            state += "=";
            state += String.valueOf(lamps.get(key).byteValue()) + "\n";
        }
        System.out.println(state);
        return state;
    }
}
