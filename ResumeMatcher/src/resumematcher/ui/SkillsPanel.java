package resumematcher.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.SQLException;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import resumematcher.dao.Dao;
import resumematcher.model.User;
import resumematcher.util.Matcher;

/** The user adds or removes their skills here. These are compared with each job's required skills. */
public class SkillsPanel extends JPanel implements Refreshable {
    private final User user;
    private final Dao dao;
    private final DefaultListModel<String> model = new DefaultListModel<>();
    private final JList<String> list = new JList<>(model);
    private final JTextField input = new JTextField(20);

    public SkillsPanel(User user, Dao dao) {
        this.user = user;
        this.dao = dao;
        setLayout(new BorderLayout(8, 8));

        add(new JScrollPane(list), BorderLayout.CENTER);

        JButton add = new JButton("Add skill");
        add.addActionListener(e -> addSkill());
        input.addActionListener(e -> addSkill());
        JButton remove = new JButton("Remove selected");
        remove.addActionListener(e -> removeSelected());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(new JLabel("Skill:"));
        bottom.add(input);
        bottom.add(add);
        bottom.add(remove);
        add(bottom, BorderLayout.SOUTH);

        refresh();
    }

    @Override
    public void refresh() {
        try {
            model.clear();
            for (String s : dao.getUserSkills(user.getId())) model.addElement(s);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Could not load skills: " + ex.getMessage());
        }
    }

    private void addSkill() {
        String skill = Matcher.normalize(input.getText());
        if (skill.isEmpty()) return;
        try {
            dao.addSkill(user.getId(), skill);
            input.setText("");
            refresh();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Could not add skill: " + ex.getMessage());
        }
    }

    private void removeSelected() {
        String skill = list.getSelectedValue();
        if (skill == null) return;
        try {
            dao.removeSkill(user.getId(), skill);
            refresh();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Could not remove skill: " + ex.getMessage());
        }
    }
}
