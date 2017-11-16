package org.academiadecodigo.enuminatti.ihouse.client.service;

import org.academiadecodigo.enuminatti.ihouse.server.model.User;

public interface UserService {

    boolean authenticate(String username, String password);

    void addUser(User user);

    User findByName(String username);

    int count();

}
