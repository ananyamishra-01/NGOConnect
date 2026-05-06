package com.ngoconnect.repository;

import com.ngoconnect.exception.NGOConnectException;
import com.ngoconnect.model.User;

import java.util.List;
import java.util.Optional;

public class UserRepository {

    private List<User> store;

    public UserRepository() {
        this.store = FileRepository.loadUsers();
    }

    public void add(User user) {
        if (findByUsername(user.getUsername()).isPresent()) {
            throw new NGOConnectException("Username '" + user.getUsername() + "' already exists.");
        }
        store.add(user);
        persist();
    }

    public Optional<User> findByUsername(String username) {
        return store.stream().filter(u -> u.getUsername().equalsIgnoreCase(username)).findFirst();
    }

    public Optional<User> authenticate(String username, String passwordHash) {
        return store.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username)
                          && u.getPasswordHash().equals(passwordHash))
                .findFirst();
    }

    public void update(User user) { persist(); }

    public List<User> findAll() { return store; }

    private void persist() { FileRepository.saveUsers(store); }
}
