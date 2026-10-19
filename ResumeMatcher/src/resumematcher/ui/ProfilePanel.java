package resumematcher.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import resumematcher.dao.Dao;
import resumematcher.model.User;

/** Profile screen: edit name and upload the resume (PDF). */
public class ProfilePanel extends JPanel implements Refreshable {
    private final User user;
    private final Dao dao;
    private final JTextField nameField = new JTextField(24);
    private final JLabel resumeLabel = new JLabel();

    public ProfilePanel(User user, Dao dao) {
        this.user = user;
        this.dao = dao;
        setLayout(new GridBagLayout());

        row(0, "Name", nameField);
        row(1, "Email", new JLabel(user.getEmail()));
        row(2, "Resume", resumeLabel);

        JButton upload = new JButton("Upload resume (PDF)");
        upload.addActionListener(e -> uploadResume());
        JButton save = new JButton("Save name");
        save.addActionListener(e -> saveName());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(12, 8, 8, 8);
        c.gridy = 3;
        c.gridx = 0;
        add(upload, c);
        c.gridx = 1;
        add(save, c);

        refresh();
    }

    private void row(int y, String label, java.awt.Component field) {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.anchor = GridBagConstraints.WEST;
        c.gridy = y;
        c.gridx = 0;
        add(new JLabel(label), c);
        c.gridx = 1;
        add(field, c);
    }

    @Override
    public void refresh() {
        nameField.setText(user.getName());
        String path = user.getResumePath();
        resumeLabel.setText(path == null || path.isEmpty() ? "No resume uploaded" : new File(path).getName());
    }

    private void saveName() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name cannot be empty.");
            return;
        }
        try {
            dao.updateName(user.getId(), name);
            user.setName(name);
            JOptionPane.showMessageDialog(this, "Name updated.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Could not save: " + ex.getMessage());
        }
    }

    private void uploadResume() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("PDF files", "pdf"));
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File source = chooser.getSelectedFile();
        try {
            File dir = new File("resumes");
            dir.mkdirs();
            File dest = new File(dir, user.getId() + "_" + source.getName());
            Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            dao.updateResumePath(user.getId(), dest.getPath());
            user.setResumePath(dest.getPath());
            refresh();
            JOptionPane.showMessageDialog(this, "Resume uploaded.");
        } catch (IOException | SQLException ex) {
            JOptionPane.showMessageDialog(this, "Could not upload resume: " + ex.getMessage());
        }
    }
}
