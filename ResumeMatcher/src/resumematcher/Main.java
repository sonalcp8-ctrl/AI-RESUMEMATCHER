package resumematcher;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import resumematcher.ui.LoginFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) { }
            new LoginFrame().setVisible(true);
        });
    }
}
