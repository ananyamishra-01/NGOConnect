package com.ngoconnect.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Organisation implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private String name;
    private String description;
    private OrgType type;
    private List<Category> categories;
    private String location;
    private String state;
    private ContactInfo contactInfo;
    private int foundedYear;
    private String areaOfWork;
    private List<String> recentActivities;
    private List<Event> events;
    private List<Review> reviews;
    private boolean verified;
    private int volunteerCount;
    private String registrationNumber;

    public Organisation(String name, String description, OrgType type,
                        String location, String state, ContactInfo contactInfo,
                        int foundedYear, String areaOfWork) {
        this.id = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.name = name;
        this.description = description;
        this.type = type;
        this.location = location;
        this.state = state;
        this.contactInfo = contactInfo;
        this.foundedYear = foundedYear;
        this.areaOfWork = areaOfWork;
        this.categories = new ArrayList<>();
        this.recentActivities = new ArrayList<>();
        this.events = new ArrayList<>();
        this.reviews = new ArrayList<>();
        this.verified = false;
    }

    // ── Getters ──────────────────────────────────────────────
    public String getId()               { return id; }
    public String getName()             { return name; }
    public String getDescription()      { return description; }
    public OrgType getType()            { return type; }
    public List<Category> getCategories() { return categories; }
    public String getLocation()         { return location; }
    public String getState()            { return state; }
    public ContactInfo getContactInfo() { return contactInfo; }
    public int getFoundedYear()         { return foundedYear; }
    public String getAreaOfWork()       { return areaOfWork; }
    public List<String> getRecentActivities() { return recentActivities; }
    public List<Event> getEvents()      { return events; }
    public List<Review> getReviews()    { return reviews; }
    public boolean isVerified()         { return verified; }
    public int getVolunteerCount()      { return volunteerCount; }
    public String getRegistrationNumber() { return registrationNumber; }

    // ── Setters ──────────────────────────────────────────────
    public void setName(String name)              { this.name = name; }
    public void setDescription(String desc)       { this.description = desc; }
    public void setVerified(boolean verified)     { this.verified = verified; }
    public void setVolunteerCount(int count)      { this.volunteerCount = count; }
    public void setRegistrationNumber(String reg) { this.registrationNumber = reg; }

    public void addCategory(Category c)           { categories.add(c); }
    public void addRecentActivity(String activity){ recentActivities.add(activity); }
    public void addEvent(Event event)             { events.add(event); }
    public void addReview(Review review)          { reviews.add(review); }

    public int getTenureYears() {
        return java.time.Year.now().getValue() - foundedYear;
    }

    public double getAverageRating() {
        if (reviews.isEmpty()) return 0.0;
        return reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
    }

    public String getSdgAlignment() {
        if (categories.isEmpty()) return "N/A";
        StringBuilder sb = new StringBuilder();
        for (Category c : categories) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(c.getSdgAlignment());
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s) — %s, %s", id, name, type.getLabel(), location, state);
    }
}
