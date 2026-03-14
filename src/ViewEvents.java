import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.List;

public class ViewEvents extends JDialog {

    private JTable table;
    private DefaultTableModel model;
    private final Dashboard dashRef;

    public ViewEvents(Dashboard parent) {
        super(parent, "View All Events", false);
        this.dashRef = parent;
        setSize(920, 560);
        setLocationRelativeTo(parent);
        setResizable(true);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BG_DARK);
        root.setBorder(BorderFactory.createEmptyBorder(26, 28, 26, 28));

        // ── Header ──
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));

        JPanel headerLeft = new JPanel();
        headerLeft.setOpaque(false);
        headerLeft.setLayout(new BoxLayout(headerLeft, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("All Events");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(UITheme.TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Manage and track all your events");
        subtitle.setFont(UITheme.FONT_SMALL);
        subtitle.setForeground(UITheme.TEXT_SECONDARY);

        headerLeft.add(title);
        headerLeft.add(Box.createVerticalStrut(3));
        headerLeft.add(subtitle);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setOpaque(false);

        JButton editBtn   = UITheme.createSecondaryButton("✏ Edit");
        JButton deleteBtn = UITheme.createDangerButton("🗑 Delete");
        JButton refreshBtn = UITheme.createPrimaryButton("↻ Refresh");

        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(refreshBtn);

        header.add(headerLeft, BorderLayout.WEST);
        header.add(btnPanel, BorderLayout.EAST);

        // ── Table ──
        String[] cols = {"ID", "Event Name", "Organizer", "Venue", "Date", "Participants"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBackground(UITheme.BG_CARD);
        scroll.getViewport().setBackground(UITheme.BG_CARD);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));

        // Dark scrollbar
        scroll.getVerticalScrollBar().setBackground(UITheme.BG_DARK);
        scroll.getHorizontalScrollBar().setBackground(UITheme.BG_DARK);

        // ── Status bar ──
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        statusBar.setOpaque(false);
        statusBar.setBorder(BorderFactory.createEmptyBorder(10, 4, 0, 0));

        JLabel statusLabel = new JLabel("Select a row to edit or delete");
        statusLabel.setFont(UITheme.FONT_SMALL);
        statusLabel.setForeground(UITheme.TEXT_DIMMED);
        statusBar.add(statusLabel);

        // ── Button actions ──
        refreshBtn.addActionListener(e -> loadData());
        editBtn.addActionListener(e -> editSelected());
        deleteBtn.addActionListener(e -> deleteSelected());

        root.add(header, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
        root.add(statusBar, BorderLayout.SOUTH);

        add(root);
        loadData();
        setVisible(true);
    }

    private void styleTable(JTable t) {
        t.setFont(UITheme.FONT_BODY);
        t.setForeground(UITheme.TEXT_PRIMARY);
        t.setBackground(UITheme.BG_CARD);
        t.setGridColor(new Color(35, 38, 58));
        t.setRowHeight(42);
        t.setShowGrid(false);
        t.setShowHorizontalLines(true);
        t.setFocusable(true);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.setSelectionBackground(new Color(108, 99, 255, 40));
        t.setSelectionForeground(UITheme.TEXT_PRIMARY);
        t.setIntercellSpacing(new Dimension(0, 1));

        // Header styling
        JTableHeader header = t.getTableHeader();
        header.setBackground(new Color(16, 18, 28));
        header.setForeground(UITheme.TEXT_SECONDARY);
        header.setFont(new Font("Segoe UI", Font.BOLD, 11));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, UITheme.ACCENT));
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));

        // Header renderer
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val,
                    boolean sel, boolean focus, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(tbl, val, sel, focus, row, col);
                lbl.setBackground(new Color(16, 18, 28));
                lbl.setForeground(UITheme.TEXT_SECONDARY);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
                lbl.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 2, 0, UITheme.ACCENT),
                    BorderFactory.createEmptyBorder(0, 12, 0, 12)
                ));
                lbl.setHorizontalAlignment(SwingConstants.LEFT);
                return lbl;
            }
        });

        // Column widths
        t.getColumnModel().getColumn(0).setMaxWidth(50);
        t.getColumnModel().getColumn(0).setMinWidth(50);
        t.getColumnModel().getColumn(4).setPreferredWidth(110);
        t.getColumnModel().getColumn(5).setPreferredWidth(100);

        // Cell renderer for alternating rows
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable tbl, Object val,
                    boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, val, sel, focus, row, col);
                if (!sel) {
                    c.setBackground(row % 2 == 0 ? UITheme.BG_CARD : new Color(24, 27, 42));
                    c.setForeground(UITheme.TEXT_PRIMARY);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                return c;
            }
        };
        for (int i = 0; i < t.getColumnCount(); i++) {
            t.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }

    private void loadData() {
        model.setRowCount(0);
        List<Event> events = EventDAO.getAllEvents();
        for (Event e : events) {
            model.addRow(new Object[]{
                e.getId(), e.getName(), e.getOrganizer(),
                e.getVenue(), e.getDate(), e.getParticipants()
            });
        }
    }

    private void editSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an event to edit.");
            return;
        }
        Event e = new Event(
            (int) model.getValueAt(row, 0),
            (String) model.getValueAt(row, 1),
            (String) model.getValueAt(row, 2),
            (String) model.getValueAt(row, 3),
            (String) model.getValueAt(row, 4),
            (int) model.getValueAt(row, 5)
        );
        new AddEventForm(dashRef, e);
        loadData();
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an event to delete.");
            return;
        }
        int id = (int) model.getValueAt(row, 0);
        String name = (String) model.getValueAt(row, 1);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete event \"" + name + "\"?\nThis will also remove all registered participants.",
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            if (EventDAO.deleteEvent(id)) {
                JOptionPane.showMessageDialog(this, "Event deleted.");
                loadData();
                if (dashRef != null) dashRef.refreshStats();
            } else {
                JOptionPane.showMessageDialog(this, "Delete failed.");
            }
        }
    }
}
