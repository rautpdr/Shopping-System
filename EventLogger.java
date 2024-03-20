package edu.depaul;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventLogger {
    private static List<Event> events = new ArrayList<>();

    public static void logEvent(String eventType, String username, String details) {
        Event event = new Event(eventType, username, LocalDateTime.now(), details);
        events.add(event);
        System.out.println(event); // For demonstration, printing the event. You can also write it to a file or database.
    }
}
