package org.academiadecodigo.enuminatti.ihouse.server.model;

import java.util.LinkedList;

/**
 * Created by codecadet on 16/11/17.
 */
public class UserManager {

    private LinkedList<User> users;

    public UserManager(){
        users = new LinkedList<>();
        users.add(new User("Drake", "lol"));
    }

    public boolean authenticate(String username, String password){
        for(User user: users){
            if(username.equals(user.getUsername()) & password.equals(user.getPassword())){
                return true;
            }
        }

        return false;
    }


    public void addUser(String username, String password){
        users.add(new User(username, password));
    }


    public void decodeInput(String input){
        //this method will translate the input it receives into
    }

}
