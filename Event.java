package edu.depaul;

import java.time.LocalDateTime;

public class Event {
    private String eventType;
    private String username;
    private LocalDateTime timestamp;
    private String details;

    public Event(String eventType, String username, LocalDateTime timestamp, String details) {
        this.eventType = eventType;
        this.username = username;
        this.timestamp = timestamp;
        this.details = details;
    }

    // Generate getters and setters

    @Override
    public String toString() {
        return "Event{" +
                "eventType='" + eventType + '\'' +
                ", username='" + username + '\'' +
                ", timestamp=" + timestamp +
                ", details='" + details + '\'' +
                '}';
    }
}

