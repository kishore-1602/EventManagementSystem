import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class AddEventForm extends JDialog {

    private JTextField nameField, orgField, venueField, dateField, partField;
    private final Dashboard parent;
    private Event existingEvent = null;

    // Constructor for ADD
    public AddEventForm(Dashboard parent) {
        super(parent, "Add New Event", true);
        this.parent = parent;
        buildUI("Add New Event", "Save Event");
    }

    // Constructor for EDIT
    public AddEventForm(Dashboard parent, Event event) {
        super(parent, "Edit Event", true);
        this.parent = parent;
        this.existingEvent = event;
        buildUI("Edit Event", "Update Event");
        populateFields(event);
    }

    private void buildUI(String title, String btnText) {
        setSize(500, 560);
        setLocationRelativeTo(parent);
        setResizable(false);

        // ── Root with gradient ──
        JPanel root = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(UITheme.BG_DARK);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        root.setBorder(BorderFactory.createEmptyBorder(30, 34, 30, 34));

        // ── Title bar ──
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setOpaque(false);
        titleBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 24, 0));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(UITheme.TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel("Fill in the details below");
        subtitleLabel.setFont(UITheme.FONT_SMALL);
        subtitleLabel.setForeground(UITheme.TEXT_SECONDARY);

        JPanel titleTexts = new JPanel();
        titleTexts.setOpaque(false);
        titleTexts.setLayout(new BoxLayout(titleTexts, BoxLayout.Y_AXIS));
        titleTexts.add(titleLabel);
        titleTexts.add(Box.createVerticalStrut(3));
        titleTexts.add(subtitleLabel);

        titleBar.add(titleTexts, BorderLayout.WEST);

        // ── Form card ──
        JPanel form = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                g2.setColor(UITheme.BG_CARD);
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 16, 16));
                g2.setColor(UITheme.BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 1, h - 1, 16, 16));
                g2.dispose();
            }
        };
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        nameField  = addField(form, "Event Name", "e.g. Annual Tech Fest");
        orgField   = addField(form, "Organizer",  "e.g. CS Department");
        venueField = addField(form, "Venue",       "e.g. Main Auditorium");
        dateField  = addField(form, "Date (YYYY-MM-DD)", "e.g. 2025-08-15");
        partField  = addField(form, "Max Participants",  "e.g. 200");

        // ── Button row ──
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setOpaque(false);
        btnRow.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton cancelBtn = UITheme.createSecondaryButton("Cancel");
        JButton saveBtn   = UITheme.createPrimaryButton(btnText);

        cancelBtn.addActionListener(e -> dispose());
        saveBtn.addActionListener(e -> saveEvent());

        btnRow.add(cancelBtn);
        btnRow.add(saveBtn);

        root.add(titleBar, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);
        root.add(btnRow, BorderLayout.SOUTH);

        add(root);
        setVisible(true);
    }

    private JTextField addField(JPanel form, String label, String placeholder) {
        JLabel lbl = UITheme.createLabel(label);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lbl);
        form.add(Box.createVerticalStrut(5));

        JTextField tf = UITheme.createStyledTextField();
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        tf.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Placeholder
        tf.setForeground(UITheme.TEXT_DIMMED);
        tf.setText(placeholder);
        tf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (tf.getText().equals(placeholder)) {
                    tf.setText("");
                    tf.setForeground(UITheme.TEXT_PRIMARY);
                }
            }
            public void focusLost(FocusEvent e) {
                if (tf.getText().isEmpty()) {
                    tf.setForeground(UITheme.TEXT_DIMMED);
                    tf.setText(placeholder);
                }
            }
        });

        form.add(tf);
        form.add(Box.createVerticalStrut(12));
        return tf;
    }

    private void populateFields(Event e) {
        nameField.setText(e.getName());
        nameField.setForeground(UITheme.TEXT_PRIMARY);
        orgField.setText(e.getOrganizer());
        orgField.setForeground(UITheme.TEXT_PRIMARY);
        venueField.setText(e.getVenue());
        venueField.setForeground(UITheme.TEXT_PRIMARY);
        dateField.setText(e.getDate());
        dateField.setForeground(UITheme.TEXT_PRIMARY);
        partField.setText(String.valueOf(e.getParticipants()));
        partField.setForeground(UITheme.TEXT_PRIMARY);
    }

    private String getCleanText(JTextField tf, String placeholder) {
        String text = tf.getText().trim();
        return text.equals(placeholder) ? "" : text;
    }

    private void saveEvent() {
        String name  = getCleanText(nameField,  "e.g. Annual Tech Fest");
        String org   = getCleanText(orgField,   "e.g. CS Department");
        String venue = getCleanText(venueField, "e.g. Main Auditorium");
        String date  = getCleanText(dateField,  "e.g. 2025-08-15");
        String partStr = getCleanText(partField, "e.g. 200");

        if (name.isEmpty() || org.isEmpty() || venue.isEmpty() || date.isEmpty() || partStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Date must be in YYYY-MM-DD format.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int participants;
        try {
            participants = Integer.parseInt(partStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Participants must be a number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success;
        if (existingEvent == null) {
            Event ev = new Event(name, org, venue, date, participants);
            success = EventDAO.addEvent(ev);
        } else {
            existingEvent.setName(name);
            existingEvent.setOrganizer(org);
            existingEvent.setVenue(venue);
            existingEvent.setDate(date);
            existingEvent.setParticipants(participants);
            success = EventDAO.updateEvent(existingEvent);
        }

        if (success) {
            JOptionPane.showMessageDialog(this,
                existingEvent == null ? "Event added successfully!" : "Event updated successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            if (parent != null) parent.refreshStats();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Operation failed. Check DB connection.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
