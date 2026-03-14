public class Event {
    private int id;
    private String name;
    private String organizer;
    private String venue;
    private String date;
    private int participants;

    public Event(String name, String organizer, String venue, String date, int participants) {
        this.name = name;
        this.organizer = organizer;
        this.venue = venue;
        this.date = date;
        this.participants = participants;
    }

    public Event(int id, String name, String organizer, String venue, String date, int participants) {
        this.id = id;
        this.name = name;
        this.organizer = organizer;
        this.venue = venue;
        this.date = date;
        this.participants = participants;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getOrganizer() { return organizer; }
    public String getVenue() { return venue; }
    public String getDate() { return date; }
    public int getParticipants() { return participants; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setOrganizer(String organizer) { this.organizer = organizer; }
    public void setVenue(String venue) { this.venue = venue; }
    public void setDate(String date) { this.date = date; }
    public void setParticipants(int participants) { this.participants = participants; }
}
