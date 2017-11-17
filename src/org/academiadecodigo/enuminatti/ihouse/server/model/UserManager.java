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
        users.add(new User("Luis", "naosei"));
        users.add(new User("Joao", "muitocomplexa"));
        users.add(new User("lol", "lol"));
        users.add(new User("123", "123"));
        users.add(new User("aaa", "aaa"));
    }

    public boolean authenticate(String username, String password){
        for(User user: users){
            if(username.equals(user.getUsername()) & password.equals(user.getPassword())){
                return true;
            }
        }
        return false;
    }


    public void addUser(User user){
        users.add(user);
    }

}
