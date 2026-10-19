package resumematcher.ui;

import java.awt.BorderLayout;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import resumematcher.dao.Dao;
import resumematcher.model.User;

/** Lists the jobs the user has applied to. */
public class ApplicationsPanel extends JPanel implements Refreshable {
    private final User user;
    private final Dao dao;
    private final DefaultTableModel model = new DefaultTableModel(
            new String[] { "Job title", "Company", "Match", "Status", "Applied on" }, 0) {
        @Override
        public boolean isCellEditable(int row, int col) { return false; }
    };

    public ApplicationsPanel(User user, Dao dao) {
        this.user = user;
        this.dao = dao;
        setLayout(new BorderLayout());
        JTable table = new JTable(model);
        table.setRowHeight(24);
        add(new JScrollPane(table), BorderLayout.CENTER);
        refresh();
    }

    @Override
    public void refresh() {
        try {
            model.setRowCount(0);
            for (String[] row : dao.getApplications(user.getId())) model.addRow(row);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Could not load applications: " + ex.getMessage());
        }
    }
}
