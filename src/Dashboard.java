import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Dashboard extends JFrame {

    private JPanel contentArea;
    private JLabel statEvents, statParticipants;

    public Dashboard() {
        setTitle("EventPro — Dashboard");
        setSize(1040, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BG_DARK);

        // ── Sidebar ──
        root.add(buildSidebar(), BorderLayout.WEST);

        // ── Main Content ──
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                g2.setColor(UITheme.BG_DARK);
                g2.fillRect(0, 0, w, h);
                // Subtle radial accent glow
                g2.setColor(new Color(108, 99, 255, 8));
                g2.fillOval(w / 2 - 300, -200, 600, 400);
                g2.dispose();
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(32, 36, 32, 36));

        // ── Header ──
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 28, 0));

        JPanel headerLeft = new JPanel();
        headerLeft.setOpaque(false);
        headerLeft.setLayout(new BoxLayout(headerLeft, BoxLayout.Y_AXIS));

        JLabel welcomeLabel = new JLabel("Welcome back, Admin");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(UITheme.TEXT_PRIMARY);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"));
        JLabel dateLabel = new JLabel(formattedDate);
        dateLabel.setFont(UITheme.FONT_SMALL);
        dateLabel.setForeground(UITheme.TEXT_SECONDARY);
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerLeft.add(welcomeLabel);
        headerLeft.add(Box.createVerticalStrut(4));
        headerLeft.add(dateLabel);

        header.add(headerLeft, BorderLayout.WEST);

        // ── Stats Row ──
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 18, 0));
        statsPanel.setOpaque(false);

        statEvents = new JLabel(String.valueOf(EventDAO.getTotalEvents()));
        statParticipants = new JLabel(String.valueOf(EventDAO.getTotalParticipants()));

        JPanel evCard = UITheme.createStatCard(
            statEvents.getText(), "Total Events", UITheme.ACCENT);
        JPanel partCard = UITheme.createStatCard(
            statParticipants.getText(), "Registered Participants", UITheme.ACCENT_GREEN);
        JPanel dbCard = UITheme.createStatCard(
            "MySQL", "Database Status", UITheme.ACCENT_AMBER);

        statsPanel.add(evCard);
        statsPanel.add(partCard);
        statsPanel.add(dbCard);

        // ── Quick Actions Card ──
        JPanel actionsCard = new JPanel(new BorderLayout()) {
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
        actionsCard.setOpaque(false);
        actionsCard.setBorder(BorderFactory.createEmptyBorder(22, 24, 22, 24));

        JLabel actionsTitle = new JLabel("Quick Actions");
        actionsTitle.setFont(UITheme.FONT_HEADER);
        actionsTitle.setForeground(UITheme.TEXT_PRIMARY);
        actionsTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));

        JPanel actionsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        actionsRow.setOpaque(false);

        JButton addEventBtn = UITheme.createPrimaryButton("➕  New Event");
        addEventBtn.addActionListener(e -> new AddEventForm(this));

        JButton viewEventsBtn = UITheme.createSecondaryButton("📋  View Events");
        viewEventsBtn.addActionListener(e -> new ViewEvents(this));

        JButton addPartBtn = UITheme.createSecondaryButton("👤  Register Participant");
        addPartBtn.addActionListener(e -> new RegisterParticipant(this));

        actionsRow.add(addEventBtn);
        actionsRow.add(viewEventsBtn);
        actionsRow.add(addPartBtn);

        actionsCard.add(actionsTitle, BorderLayout.NORTH);
        actionsCard.add(actionsRow, BorderLayout.CENTER);

        // ── Tip Card ──
        JPanel tipCard = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                g2.setColor(new Color(108, 99, 255, 12));
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 14, 14));
                g2.setColor(new Color(108, 99, 255, 40));
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 1, h - 1, 14, 14));
                g2.dispose();
            }
        };
        tipCard.setOpaque(false);
        tipCard.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));

        JLabel tipLabel = new JLabel(
            "<html><span style='color:#6C63FF; font-weight:bold;'>💡 Tip:</span> "
            + "<span style='color:#787B96;'>Use the sidebar to navigate between sections. "
            + "Select a row in View Events to edit or delete it.</span></html>"
        );
        tipLabel.setFont(UITheme.FONT_BODY);
        tipCard.add(tipLabel, BorderLayout.CENTER);

        // ── Assemble center ──
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(statsPanel);
        center.add(Box.createVerticalStrut(22));
        center.add(actionsCard);
        center.add(Box.createVerticalStrut(16));
        center.add(tipCard);

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(center, BorderLayout.CENTER);

        root.add(mainPanel, BorderLayout.CENTER);
        add(root);
        setVisible(true);
    }

    // ── Sidebar ────────────────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                int w = getWidth(), h = getHeight();
                g2.setColor(UITheme.BG_SIDEBAR);
                g2.fillRect(0, 0, w, h);
                // Right border
                g2.setColor(UITheme.BORDER);
                g2.fillRect(w - 1, 0, 1, h);
                g2.dispose();
            }
        };
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(28, 16, 24, 16));

        // Brand
        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        brandPanel.setOpaque(false);
        brandPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        brandPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel brandIcon = new JLabel("⬡");
        brandIcon.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        brandIcon.setForeground(UITheme.ACCENT);

        JLabel brandText = new JLabel("EventPro");
        brandText.setFont(new Font("Segoe UI", Font.BOLD, 18));
        brandText.setForeground(UITheme.TEXT_PRIMARY);

        brandPanel.add(brandIcon);
        brandPanel.add(brandText);

        sidebar.add(brandPanel);
        sidebar.add(Box.createVerticalStrut(28));

        // Nav section label
        JLabel navLabel = new JLabel("NAVIGATION");
        navLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        navLabel.setForeground(UITheme.TEXT_DIMMED);
        navLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        navLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 0));
        sidebar.add(navLabel);

        addNavItem(sidebar, "📊  Dashboard",       () -> {}, true);
        addNavItem(sidebar, "➕  Add Event",        () -> new AddEventForm(this), false);
        addNavItem(sidebar, "📋  View Events",      () -> new ViewEvents(this), false);
        addNavItem(sidebar, "👤  Add Participant",   () -> new RegisterParticipant(this), false);

        sidebar.add(Box.createVerticalGlue());

        // Logout
        JButton logoutBtn = UITheme.createDangerButton("Logout");
        logoutBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        logoutBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginForm();
        });
        sidebar.add(logoutBtn);

        return sidebar;
    }

    private void addNavItem(JPanel parent, String text, Runnable action, boolean active) {
        JButton btn = new JButton(text) {
            private boolean hovered = false;
            {
                setContentAreaFilled(false);
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                    public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();

                if (active || hovered) {
                    g2.setColor(active ? UITheme.ACCENT_LIGHT : new Color(40, 43, 65, 100));
                    g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 10, 10));
                }

                // Left accent bar for active
                if (active) {
                    g2.setColor(UITheme.ACCENT);
                    g2.fill(new RoundRectangle2D.Float(0, 4, 3, h - 8, 3, 3));
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(UITheme.FONT_BODY);
        btn.setForeground(active ? UITheme.ACCENT : UITheme.TEXT_PRIMARY);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 8));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(false);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);

        btn.addActionListener(e -> action.run());
        parent.add(btn);
        parent.add(Box.createVerticalStrut(4));
    }

    public void refreshStats() {
        statEvents.setText(String.valueOf(EventDAO.getTotalEvents()));
        statParticipants.setText(String.valueOf(EventDAO.getTotalParticipants()));
        repaint();
    }
}
