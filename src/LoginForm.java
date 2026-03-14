import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.sql.*;

public class LoginForm extends JFrame {

    private JTextField userField;
    private JPasswordField passField;
    private JLabel statusLabel;

    public LoginForm() {
        UITheme.applyTheme();
        setTitle("EventPro — Sign In");
        setSize(460, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // ── Gradient background panel ──
        JPanel root = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, new Color(13, 14, 26),
                                                     w, h, new Color(30, 22, 56));
                g2.setPaint(gp);
                g2.fillRect(0, 0, w, h);

                // Subtle decorative circles
                g2.setColor(new Color(108, 99, 255, 15));
                g2.fillOval(-60, -60, 260, 260);
                g2.fillOval(w - 120, h - 160, 280, 280);
                g2.setColor(new Color(168, 85, 247, 10));
                g2.fillOval(w - 200, -80, 300, 300);
                g2.dispose();
            }
        };

        // ── Glassmorphic card ──
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();

                // Card fill — semi-transparent
                g2.setColor(new Color(20, 22, 38, 200));
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 24, 24));

                // Subtle top highlight
                g2.setPaint(new GradientPaint(0, 0, new Color(108, 99, 255, 30), 0, 6, new Color(0, 0, 0, 0)));
                g2.fill(new RoundRectangle2D.Float(0, 0, w, 8, 24, 24));

                // Border
                g2.setColor(new Color(60, 63, 90, 120));
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 1, h - 1, 24, 24));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(36, 36, 32, 36));
        card.setPreferredSize(new Dimension(370, 480));

        // ── Logo icon ──
        JLabel logo = new JLabel("⬡", SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI", Font.PLAIN, 50));
        logo.setForeground(UITheme.ACCENT);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ── Brand name ──
        JLabel titleLabel = new JLabel("EventPro");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(UITheme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ── Subtitle ──
        JLabel subtitleLabel = new JLabel("Sign in to your account");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(UITheme.TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(logo);
        card.add(Box.createVerticalStrut(4));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(subtitleLabel);
        card.add(Box.createVerticalStrut(28));

        // ── Username ──
        JLabel userLabel = UITheme.createLabel("Username");
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        userField = UITheme.createStyledTextField();
        userField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        userField.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(userLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(userField);
        card.add(Box.createVerticalStrut(16));

        // ── Password ──
        JLabel passLabel = UITheme.createLabel("Password");
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passField = UITheme.createStyledPasswordField();
        passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passField.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(passLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(passField);
        card.add(Box.createVerticalStrut(24));

        // ── Sign In button ──
        JButton loginBtn = UITheme.createPrimaryButton("Sign In");
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(14));

        // ── Status ──
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(UITheme.FONT_SMALL);
        statusLabel.setForeground(UITheme.ACCENT_RED);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(statusLabel);

        // ── Hint ──
        card.add(Box.createVerticalStrut(10));
        JLabel hint = new JLabel("Default credentials: admin / admin123");
        hint.setFont(UITheme.FONT_SMALL);
        hint.setForeground(UITheme.TEXT_DIMMED);
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(hint);

        root.add(card);
        add(root);

        // ── Actions ──
        loginBtn.addActionListener(e -> doLogin());
        passField.addActionListener(e -> doLogin());
        userField.addActionListener(e -> passField.requestFocus());

        setVisible(true);
    }

    private void doLogin() {
        String username = userField.getText().trim();
        String password = String.valueOf(passField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter username and password.");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                statusLabel.setText("Database connection failed.");
                return;
            }
            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                new Dashboard();
                dispose();
            } else {
                statusLabel.setText("Invalid username or password.");
                passField.setText("");
            }
        } catch (Exception ex) {
            statusLabel.setText("Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginForm::new);
    }
}
