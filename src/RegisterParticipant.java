import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.sql.*;
import java.util.List;

public class RegisterParticipant extends JDialog {

    private JTextField nameField, emailField;
    private JComboBox<String> eventCombo;
    private int[] eventIds;
    private final Dashboard dashRef;

    public RegisterParticipant(Dashboard parent) {
        super(parent, "Register Participant", true);
        this.dashRef = parent;
        setSize(500, 480);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BG_DARK);
        root.setBorder(BorderFactory.createEmptyBorder(30, 34, 30, 34));

        // ── Title bar ──
        JPanel titleBar = new JPanel();
        titleBar.setOpaque(false);
        titleBar.setLayout(new BoxLayout(titleBar, BoxLayout.Y_AXIS));
        titleBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 24, 0));

        JLabel titleLabel = new JLabel("Register Participant");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(UITheme.TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel("Add a new participant to an event");
        subtitleLabel.setFont(UITheme.FONT_SMALL);
        subtitleLabel.setForeground(UITheme.TEXT_SECONDARY);

        titleBar.add(titleLabel);
        titleBar.add(Box.createVerticalStrut(3));
        titleBar.add(subtitleLabel);

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

        // Name
        JLabel nameLabel = UITheme.createLabel("Full Name");
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(nameLabel);
        form.add(Box.createVerticalStrut(5));
        nameField = UITheme.createStyledTextField();
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(nameField);
        form.add(Box.createVerticalStrut(14));

        // Email
        JLabel emailLabel = UITheme.createLabel("Email Address");
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(emailLabel);
        form.add(Box.createVerticalStrut(5));
        emailField = UITheme.createStyledTextField();
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(emailField);
        form.add(Box.createVerticalStrut(14));

        // Event dropdown
        JLabel eventLabel = UITheme.createLabel("Select Event");
        eventLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(eventLabel);
        form.add(Box.createVerticalStrut(5));
        eventCombo = UITheme.createStyledComboBox();
        eventCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        eventCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        loadEvents();
        form.add(eventCombo);
        form.add(Box.createVerticalStrut(24));

        // ── Button row ──
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setOpaque(false);
        btnRow.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton cancelBtn = UITheme.createSecondaryButton("Cancel");
        JButton registerBtn = UITheme.createPrimaryButton("Register");

        cancelBtn.addActionListener(e -> dispose());
        registerBtn.addActionListener(e -> doRegister());

        btnRow.add(cancelBtn);
        btnRow.add(registerBtn);

        root.add(titleBar, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);
        root.add(btnRow, BorderLayout.SOUTH);

        add(root);
        setVisible(true);
    }

    private void loadEvents() {
        List<Event> events = EventDAO.getAllEvents();
        eventIds = new int[events.size()];
        for (int i = 0; i < events.size(); i++) {
            Event e = events.get(i);
            eventIds[i] = e.getId();
            eventCombo.addItem("[" + e.getId() + "] " + e.getName() + " — " + e.getDate());
        }
        if (events.isEmpty()) {
            eventCombo.addItem("No events available");
        }
    }

    private void doRegister() {
        String name  = nameField.getText().trim();
        String email = emailField.getText().trim();

        if (name.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Email are required.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (eventIds == null || eventIds.length == 0) {
            JOptionPane.showMessageDialog(this, "No events to register for.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int selectedEventId = eventIds[eventCombo.getSelectedIndex()];

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String sql = "INSERT INTO participants(name, email, event_id) VALUES(?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setInt(3, selectedEventId);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,
                "✅ " + name + " registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            if (dashRef != null) dashRef.refreshStats();
            nameField.setText("");
            emailField.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Registration failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
