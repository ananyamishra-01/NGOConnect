package com.ngoconnect.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Event implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String description;
    private LocalDate date;
    private String location;
    private EventType type;

    public enum EventType {
        DRIVE("Drive"), CAMPAIGN("Campaign"), WORKSHOP("Workshop"),
        AWARENESS("Awareness Programme"), FUNDRAISER("Fundraiser"), TRAINING("Training");

        private final String label;
        EventType(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    public Event(String title, String description, LocalDate date, String location, EventType type) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.location = location;
        this.type = type;
    }

    public String getTitle()       { return title; }
    public String getDescription() { return description; }
    public LocalDate getDate()     { return date; }
    public String getLocation()    { return location; }
    public EventType getType()     { return type; }

    public boolean isUpcoming() {
        return date != null && !date.isBefore(LocalDate.now());
    }

    @Override
    public String toString() {
        return String.format("[%s] %s — %s (%s)", type.getLabel(), title, location,
                date != null ? date.toString() : "Date TBD");
    }
}
