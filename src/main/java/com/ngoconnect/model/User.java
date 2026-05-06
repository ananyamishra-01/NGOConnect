package com.ngoconnect.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String email;
    private String passwordHash;
    private UserRole role;
    private List<Category> interests;
    private List<String> connectedOrgIds;
    private List<String> volunteeringHistory; // orgId:eventTitle
    private double totalDonations;

    public enum UserRole { VOLUNTEER, ADMIN }

    public User(String username, String email, String passwordHash, UserRole role) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.interests = new ArrayList<>();
        this.connectedOrgIds = new ArrayList<>();
        this.volunteeringHistory = new ArrayList<>();
        this.totalDonations = 0.0;
    }

    public String getUsername()           { return username; }
    public String getEmail()              { return email; }
    public String getPasswordHash()       { return passwordHash; }
    public UserRole getRole()             { return role; }
    public List<Category> getInterests()  { return interests; }
    public List<String> getConnectedOrgIds() { return connectedOrgIds; }
    public List<String> getVolunteeringHistory() { return volunteeringHistory; }
    public double getTotalDonations()     { return totalDonations; }

    public boolean isAdmin()              { return role == UserRole.ADMIN; }

    public void addInterest(Category c)   { if (!interests.contains(c)) interests.add(c); }
    public void connectToOrg(String id)   { if (!connectedOrgIds.contains(id)) connectedOrgIds.add(id); }
    public void addVolunteering(String entry) { volunteeringHistory.add(entry); }
    public void addDonation(double amount){ totalDonations += amount; }

    @Override
    public String toString() {
        return String.format("%s (%s) — %s", username, role, email);
    }
}
