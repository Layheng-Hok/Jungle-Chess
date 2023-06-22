import view.MainMenu;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> MainMenu.get().setVisible(true));
    }
}
