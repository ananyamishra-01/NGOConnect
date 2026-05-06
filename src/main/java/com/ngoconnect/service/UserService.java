package com.ngoconnect.service;

import com.ngoconnect.exception.NGOConnectException;
import com.ngoconnect.model.User;
import com.ngoconnect.repository.UserRepository;
import com.ngoconnect.util.Validator;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public User register(String username, String email, String password, User.UserRole role) {
        Validator.requireNonEmpty(username, "Username");
        Validator.requireNonEmpty(password, "Password");
        Validator.requireValidEmail(email);
        if (password.length() < 6) throw new NGOConnectException("Password must be at least 6 characters.");

        String hash = hash(password);
        User user = new User(username, email, hash, role);
        repo.add(user);
        return user;
    }

    public Optional<User> login(String username, String password) {
        return repo.authenticate(username, hash(password));
    }

    public void updateInterests(User user) {
        repo.update(user);
    }

    public void recordVolunteering(User user, String orgName, String eventTitle) {
        user.addVolunteering(orgName + ": " + eventTitle);
        repo.update(user);
    }

    public void recordDonation(User user, double amount) {
        if (amount <= 0) throw new NGOConnectException("Donation amount must be positive.");
        user.addDonation(amount);
        repo.update(user);
    }

    private String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new NGOConnectException("Hashing failed.", e);
        }
    }
}
