import model.Controller;
import view.MainMenu;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Controller.loadGameSetting();
        SwingUtilities.invokeLater(() -> MainMenu.get().setVisible(true));
    }
}
