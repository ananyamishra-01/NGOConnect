package com.ngoconnect.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Review implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reviewerName;
    private int rating; // 1-5
    private String comment;
    private LocalDateTime timestamp;

    public Review(String reviewerName, int rating, String comment) {
        if (rating < 1 || rating > 5) throw new IllegalArgumentException("Rating must be between 1 and 5.");
        this.reviewerName = reviewerName;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = LocalDateTime.now();
    }

    public String getReviewerName()  { return reviewerName; }
    public int getRating()           { return rating; }
    public String getComment()       { return comment; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public String getStars() {
        return "★".repeat(rating) + "☆".repeat(5 - rating);
    }

    @Override
    public String toString() {
        return String.format("%s  [%s]%n%s  — %s", getStars(), reviewerName, comment,
                timestamp.toLocalDate());
    }
}
