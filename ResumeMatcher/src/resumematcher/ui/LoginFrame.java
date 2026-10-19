package resumematcher.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import resumematcher.dao.Dao;
import resumematcher.model.User;

/** Login and Register screen. */
public class LoginFrame extends JFrame {
    private final Dao dao = new Dao();

    private final JTextField loginEmail = new JTextField(18);
    private final JPasswordField loginPassword = new JPasswordField(18);

    private final JTextField regName = new JTextField(18);
    private final JTextField regEmail = new JTextField(18);
    private final JPasswordField regPassword = new JPasswordField(18);

    public LoginFrame() {
        super("Resume Matcher");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Login", buildLogin());
        tabs.addTab("Register", buildRegister());
        add(tabs);
        setSize(400, 300);
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(null);
    }

    private JPanel buildLogin() {
        JPanel p = new JPanel(new GridBagLayout());
        addRow(p, 0, "Email", loginEmail);
        addRow(p, 1, "Password", loginPassword);
        JButton btn = new JButton("Login");
        btn.addActionListener(e -> doLogin());
        addButton(p, 2, btn);
        return p;
    }

    private JPanel buildRegister() {
        JPanel p = new JPanel(new GridBagLayout());
        addRow(p, 0, "Full name", regName);
        addRow(p, 1, "Email", regEmail);
        addRow(p, 2, "Password (min 6)", regPassword);
        JButton btn = new JButton("Create account");
        btn.addActionListener(e -> doRegister());
        addButton(p, 3, btn);
        return p;
    }

    private void addRow(JPanel p, int row, String label, java.awt.Component field) {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 8, 6, 8);
        c.gridy = row;
        c.gridx = 0;
        c.anchor = GridBagConstraints.WEST;
        p.add(new JLabel(label), c);
        c.gridx = 1;
        p.add(field, c);
    }

    private void addButton(JPanel p, int row, JButton btn) {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(12, 8, 6, 8);
        c.gridy = row;
        c.gridx = 0;
        c.gridwidth = 2;
        p.add(btn, c);
    }

    private void doLogin() {
        String email = loginEmail.getText().trim();
        String password = new String(loginPassword.getPassword());
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter email and password.");
            return;
        }
        try {
            User user = dao.login(email, password);
            if (user == null) {
                JOptionPane.showMessageDialog(this, "Wrong email or password.", "Login failed",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            new MainFrame(user, dao).setVisible(true);
            dispose();
        } catch (SQLException ex) {
            dbError(ex);
        }
    }

    private void doRegister() {
        String name = regName.getText().trim();
        String email = regEmail.getText().trim();
        String password = new String(regPassword.getPassword());
        if (name.isEmpty() || !email.contains("@") || password.length() < 6) {
            JOptionPane.showMessageDialog(this,
                    "Enter your name, a valid email and a password of at least 6 characters.");
            return;
        }
        try {
            if (dao.register(name, email, password)) {
                JOptionPane.showMessageDialog(this, "Account created. You can log in now.");
                regName.setText("");
                regEmail.setText("");
                regPassword.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "This email is already registered.");
            }
        } catch (SQLException ex) {
            dbError(ex);
        }
    }

    private void dbError(SQLException ex) {
        JOptionPane.showMessageDialog(this,
                "Database error: " + ex.getMessage()
                + "\n\nCheck that MySQL is running and db.properties is correct.",
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}
