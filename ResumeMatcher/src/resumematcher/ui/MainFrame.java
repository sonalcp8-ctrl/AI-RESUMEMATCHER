package resumematcher.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import resumematcher.dao.Dao;
import resumematcher.model.User;

/** Main window shown after login: Dashboard, My Skills, My Applications, My Profile. */
public class MainFrame extends JFrame {

    public MainFrame(User user, Dao dao) {
        super("Resume Matcher");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Dashboard", new DashboardPanel(user, dao));
        tabs.addTab("My Skills", new SkillsPanel(user, dao));
        tabs.addTab("My Applications", new ApplicationsPanel(user, dao));
        tabs.addTab("My Profile", new ProfilePanel(user, dao));
        tabs.addChangeListener(e -> {
            Component c = tabs.getSelectedComponent();
            if (c instanceof Refreshable) ((Refreshable) c).refresh();
        });

        JLabel welcome = new JLabel("Welcome, " + user.getName());
        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        top.add(welcome, BorderLayout.WEST);
        top.add(logout, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
        setSize(850, 520);
        setLocationRelativeTo(null);
    }
}
