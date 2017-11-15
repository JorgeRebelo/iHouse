package org.academiadecodigo.enuminatti.ihouse.client.service;

import org.academiadecodigo.enuminatti.ihouse.client.utils.Security;
import org.academiadecodigo.enuminatti.ihouse.server.model.User;

import java.util.HashMap;
import java.util.Map;

public class MockUserService implements UserService {

    private Map<String, User> users;

    public MockUserService() {
        users = new HashMap<>();
    }

    public MockUserService(Map<String, User> users) {
        this.users = users;
    }

    @Override
    public boolean authenticate(String username, String password) {
        return users.containsKey(username) &&
                users.get(username).getPassword().equals(Security.getHash(password));
    }

    @Override
    public void addUser(User user) {
        if (!users.containsKey(user.getUsername())) {
            users.put(user.getUsername(), user);
        }
    }

    @Override
    public User findByName(String username) {
        return users.get(username);
    }

    @Override
    public int count() {
        return users.size();
    }
}
