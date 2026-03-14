import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {

    public static boolean addEvent(Event e) {
        String sql = "INSERT INTO events(event_name, organizer, venue, event_date, participants) VALUES(?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getName());
            ps.setString(2, e.getOrganizer());
            ps.setString(3, e.getVenue());
            ps.setString(4, e.getDate());
            ps.setInt(5, e.getParticipants());
            ps.executeUpdate();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean updateEvent(Event e) {
        String sql = "UPDATE events SET event_name=?, organizer=?, venue=?, event_date=?, participants=? WHERE event_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getName());
            ps.setString(2, e.getOrganizer());
            ps.setString(3, e.getVenue());
            ps.setString(4, e.getDate());
            ps.setInt(5, e.getParticipants());
            ps.setInt(6, e.getId());
            ps.executeUpdate();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean deleteEvent(int id) {
        // First delete related participants
        String delPart = "DELETE FROM participants WHERE event_id=?";
        String delEvent = "DELETE FROM events WHERE event_id=?";
        try (Connection con = DBConnection.getConnection()) {
            try (PreparedStatement ps1 = con.prepareStatement(delPart)) {
                ps1.setInt(1, id);
                ps1.executeUpdate();
            }
            try (PreparedStatement ps2 = con.prepareStatement(delEvent)) {
                ps2.setInt(1, id);
                ps2.executeUpdate();
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                events.add(new Event(
                        rs.getInt("event_id"),
                        rs.getString("event_name"),
                        rs.getString("organizer"),
                        rs.getString("venue"),
                        rs.getString("event_date"),
                        rs.getInt("participants")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return events;
    }

    public static int getTotalEvents() {
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM events")) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public static int getTotalParticipants() {
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM participants")) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }
}
