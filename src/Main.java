import model.Controller;
import view.MainMenuFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Controller.loadGameSetting();
        SwingUtilities.invokeLater(() -> MainMenuFrame.get().setVisible(true));
    }
}
