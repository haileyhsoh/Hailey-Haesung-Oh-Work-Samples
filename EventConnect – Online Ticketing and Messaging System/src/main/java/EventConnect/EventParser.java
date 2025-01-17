
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set; //imports set

import com.google.zxing.WriterException;

public class EventParser {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/events";
    private static final String USER = "your_username";
    private static final String PASS = "your_password";

    
    // Search events by date
    public Set<Event> searchEventsByDate(String date) throws WriterException, IOException {
        return searchEvents("DATETIME", date);
    }

    // Search events by location
    public Set<Event> searchEventsByLocation(String location) throws WriterException, IOException {
        return searchEvents("location", location);
    }

    // Search events by name
    public Set<Event> searchEventsByName(String eventName) throws WriterException, IOException {
        return searchEvents("eventName", eventName);
    }

    // Generic search method
    private Set<Event> searchEvents(String fieldName, String value) throws WriterException, IOException {
        Set<Event> events = new HashSet<>();
        String query = "SELECT eventName, location, DATETIME, description FROM Events WHERE " + fieldName + " = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setString(1, value);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                events.add(new Event(
                    rs.getString("eventName"),
                    rs.getString("DATETIME"),
                    rs.getString("location"),
                    rs.getString("description")
                ));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error accessing the database: " + e.getMessage());
        }
        return events;
    }
}