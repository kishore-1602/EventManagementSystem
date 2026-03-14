import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.*;

public class UITheme {

    // ── Premium Color Palette ──────────────────────────────────────────
    // Backgrounds
    public static final Color BG_DARK       = new Color(13, 14, 22);
    public static final Color BG_CARD       = new Color(20, 22, 35);
    public static final Color BG_INPUT      = new Color(26, 29, 46);
    public static final Color BG_SIDEBAR    = new Color(16, 18, 28);

    // Accent — Indigo / Violet family
    public static final Color ACCENT        = new Color(108, 99, 255);    // Vibrant indigo
    public static final Color ACCENT_HOVER  = new Color(130, 122, 255);
    public static final Color ACCENT_LIGHT  = new Color(108, 99, 255, 30);
    public static final Color ACCENT2       = new Color(168, 85, 247);    // Purple

    // Semantic colors
    public static final Color ACCENT_GREEN  = new Color(52, 211, 153);
    public static final Color ACCENT_RED    = new Color(251, 113, 133);
    public static final Color ACCENT_AMBER  = new Color(251, 191, 36);

    // Text
    public static final Color TEXT_PRIMARY   = new Color(237, 238, 245);
    public static final Color TEXT_SECONDARY = new Color(120, 123, 150);
    public static final Color TEXT_DIMMED    = new Color(75, 78, 100);

    // Borders & surfaces
    public static final Color BORDER        = new Color(40, 43, 65);
    public static final Color HIGHLIGHT     = new Color(108, 99, 255, 25);

    // ── Fonts ──────────────────────────────────────────────────────────
    public static Font FONT_TITLE  = new Font("Segoe UI", Font.BOLD, 26);
    public static Font FONT_HEADER = new Font("Segoe UI Semibold", Font.BOLD, 14);
    public static Font FONT_BODY   = new Font("Segoe UI", Font.PLAIN, 13);
    public static Font FONT_SMALL  = new Font("Segoe UI", Font.PLAIN, 11);
    public static Font FONT_MONO   = new Font("Consolas", Font.PLAIN, 13);

    // ── Apply global theme ─────────────────────────────────────────────
    public static void applyTheme() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { /* fallback */ }

        UIManager.put("Panel.background", BG_DARK);
        UIManager.put("OptionPane.background", BG_CARD);
        UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
        UIManager.put("OptionPane.messageFont", FONT_BODY);
        UIManager.put("Button.font", FONT_BODY);

        // Render hints
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
    }

    // ── Gradient Button (Primary) ──────────────────────────────────────
    public static JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text) {
            private float hoverProgress = 0f;
            {
                setContentAreaFilled(false);
                Timer timer = new Timer(16, null);
                timer.addActionListener(e -> {
                    repaint();
                });
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        timer.start();
                        hoverProgress = 1f;
                    }
                    public void mouseExited(MouseEvent e) {
                        hoverProgress = 0f;
                        repaint();
                        timer.stop();
                    }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth(), h = getHeight();
                Color c1 = ACCENT;
                Color c2 = ACCENT2;
                if (hoverProgress > 0) {
                    c1 = brighter(c1, 0.15f);
                    c2 = brighter(c2, 0.15f);
                }
                GradientPaint gp = new GradientPaint(0, 0, c1, w, 0, c2);
                g2.setPaint(gp);
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 12, 12));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_HEADER);
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(11, 26, 11, 26));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(false);
        return btn;
    }

    // ── Danger Button ──────────────────────────────────────────────────
    public static JButton createDangerButton(String text) {
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
                Color c1 = new Color(220, 38, 38);
                Color c2 = new Color(190, 18, 60);
                if (hovered) { c1 = brighter(c1, 0.12f); c2 = brighter(c2, 0.12f); }
                GradientPaint gp = new GradientPaint(0, 0, c1, w, 0, c2);
                g2.setPaint(gp);
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 12, 12));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_HEADER);
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(11, 26, 11, 26));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(false);
        return btn;
    }

    // ── Secondary Button (outline) ─────────────────────────────────────
    public static JButton createSecondaryButton(String text) {
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
                g2.setColor(hovered ? new Color(40, 43, 65) : BG_INPUT);
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 12, 12));
                g2.setColor(BORDER);
                g2.setStroke(new BasicStroke(1.2f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 1, h - 1, 12, 12));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BODY);
        btn.setForeground(TEXT_PRIMARY);
        btn.setBorder(BorderFactory.createEmptyBorder(9, 20, 9, 20));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(false);
        return btn;
    }

    // ── Rounded Text Field with focus glow ─────────────────────────────
    public static JTextField createStyledTextField() {
        JTextField tf = new JTextField() {
            private boolean focused = false;
            {
                setOpaque(false);
                addFocusListener(new FocusAdapter() {
                    public void focusGained(FocusEvent e) { focused = true; repaint(); }
                    public void focusLost(FocusEvent e)   { focused = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();

                // Background
                g2.setColor(BG_INPUT);
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 10, 10));

                // Border
                if (focused) {
                    g2.setColor(ACCENT);
                    g2.setStroke(new BasicStroke(1.5f));
                } else {
                    g2.setColor(BORDER);
                    g2.setStroke(new BasicStroke(1f));
                }
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 1, h - 1, 10, 10));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        tf.setFont(FONT_BODY);
        tf.setForeground(TEXT_PRIMARY);
        tf.setCaretColor(ACCENT);
        tf.setBorder(BorderFactory.createEmptyBorder(9, 14, 9, 14));
        tf.setBackground(new Color(0, 0, 0, 0));
        return tf;
    }

    // ── Rounded Password Field with focus glow ─────────────────────────
    public static JPasswordField createStyledPasswordField() {
        JPasswordField pf = new JPasswordField() {
            private boolean focused = false;
            {
                setOpaque(false);
                addFocusListener(new FocusAdapter() {
                    public void focusGained(FocusEvent e) { focused = true; repaint(); }
                    public void focusLost(FocusEvent e)   { focused = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                g2.setColor(BG_INPUT);
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 10, 10));
                if (focused) {
                    g2.setColor(ACCENT);
                    g2.setStroke(new BasicStroke(1.5f));
                } else {
                    g2.setColor(BORDER);
                    g2.setStroke(new BasicStroke(1f));
                }
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 1, h - 1, 10, 10));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        pf.setFont(FONT_BODY);
        pf.setForeground(TEXT_PRIMARY);
        pf.setCaretColor(ACCENT);
        pf.setBorder(BorderFactory.createEmptyBorder(9, 14, 9, 14));
        pf.setBackground(new Color(0, 0, 0, 0));
        return pf;
    }

    // ── Labels ─────────────────────────────────────────────────────────
    public static JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(TEXT_SECONDARY);
        return lbl;
    }

    public static JLabel createHeaderLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_HEADER);
        lbl.setForeground(TEXT_PRIMARY);
        return lbl;
    }

    // ── Card Panel ─────────────────────────────────────────────────────
    public static JPanel createCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.setColor(BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1, getHeight() - 1, 16, 16));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));
        return card;
    }

    // ── Stat Card with colored accent stripe ───────────────────────────
    public static JPanel createStatCard(String value, String label, Color color) {
        JPanel card = new JPanel(new BorderLayout(0, 4)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();

                // Card background
                g2.setColor(BG_CARD);
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 16, 16));

                // Top accent gradient stripe
                g2.setPaint(new GradientPaint(0, 0, color, w, 0, new Color(color.getRed(), color.getGreen(), color.getBlue(), 80)));
                g2.fill(new RoundRectangle2D.Float(0, 0, w, 4, 16, 16));
                g2.fillRect(0, 2, w, 4);

                // Border
                g2.setColor(BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 1, h - 1, 16, 16));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(22, 22, 18, 22));

        JLabel valLabel = new JLabel(value, SwingConstants.CENTER);
        valLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valLabel.setForeground(color);

        JLabel nameLabel = new JLabel(label, SwingConstants.CENTER);
        nameLabel.setFont(FONT_SMALL);
        nameLabel.setForeground(TEXT_SECONDARY);

        card.add(valLabel, BorderLayout.CENTER);
        card.add(nameLabel, BorderLayout.SOUTH);
        return card;
    }

    // ── Styled ComboBox ────────────────────────────────────────────────
    public static JComboBox<String> createStyledComboBox() {
        JComboBox<String> combo = new JComboBox<>();
        combo.setFont(FONT_BODY);
        combo.setBackground(BG_INPUT);
        combo.setForeground(TEXT_PRIMARY);
        combo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? ACCENT : BG_INPUT);
                setForeground(isSelected ? Color.WHITE : TEXT_PRIMARY);
                setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
                setFont(FONT_BODY);
                return this;
            }
        });
        return combo;
    }

    // ── Frame Utility ──────────────────────────────────────────────────
    public static void styleFrame(JFrame frame, int w, int h) {
        frame.setSize(w, h);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(BG_DARK);
    }

    // ── Utility: brighten a color ──────────────────────────────────────
    public static Color brighter(Color c, float factor) {
        int r = Math.min(255, (int)(c.getRed()   + 255 * factor));
        int g = Math.min(255, (int)(c.getGreen() + 255 * factor));
        int b = Math.min(255, (int)(c.getBlue()  + 255 * factor));
        return new Color(r, g, b, c.getAlpha());
    }
}
