package resumematcher.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import resumematcher.dao.Dao;
import resumematcher.model.Job;
import resumematcher.model.User;

/** Shows all jobs sorted by match score (best first). The user can apply to a selected job. */
public class DashboardPanel extends JPanel implements Refreshable {
    private final User user;
    private final Dao dao;
    private final DefaultTableModel model = new DefaultTableModel(
            new String[] { "Job title", "Company", "Location", "Required skills", "Match" }, 0) {
        @Override
        public boolean isCellEditable(int row, int col) { return false; }
    };
    private final JTable table = new JTable(model);
    private List<Job> jobs = new ArrayList<>();

    public DashboardPanel(User user, Dao dao) {
        this.user = user;
        this.dao = dao;
        setLayout(new BorderLayout(8, 8));

        table.setRowHeight(24);
        table.getColumnModel().getColumn(3).setPreferredWidth(280);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton apply = new JButton("Apply to selected job");
        apply.addActionListener(e -> applySelected());
        JButton reload = new JButton("Refresh");
        reload.addActionListener(e -> refresh());
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(apply);
        bottom.add(reload);
        bottom.add(new JLabel("  Add your skills in 'My Skills' to improve your matches."));
        add(bottom, BorderLayout.SOUTH);

        refresh();
    }

    @Override
    public void refresh() {
        try {
            jobs = dao.getJobMatches(user.getId());
            model.setRowCount(0);
            for (Job j : jobs) {
                model.addRow(new Object[] { j.getTitle(), j.getCompany(), j.getLocation(),
                        String.join(", ", j.getSkills()), j.getScore() + "%" });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Could not load jobs: " + ex.getMessage());
        }
    }

    private void applySelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a job first.");
            return;
        }
        Job job = jobs.get(row);
        try {
            if (dao.apply(user.getId(), job.getId(), job.getScore())) {
                JOptionPane.showMessageDialog(this, "Applied to " + job.getTitle() + " at " + job.getCompany());
            } else {
                JOptionPane.showMessageDialog(this, "You have already applied to this job.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Could not apply: " + ex.getMessage());
        }
    }
}
