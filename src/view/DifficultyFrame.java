package view;

import javax.swing.*;
import java.awt.*;

class DifficultyFrame extends JFrame {
    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(530, 850);
    private JButton easyButton;
    private JButton mediumButton;
    private JButton hardButton;

    DifficultyFrame() {
        initializeFrame();
        createButtons();
        addButtonsToFrame();
    }

    private void initializeFrame() {
        setTitle("Difficulty Selection");
        setSize(new Dimension(OUTER_FRAME_DIMENSION));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());
        setContentPane(new JLabel(new ImageIcon("background_image.jpg"))); // Replace "background_image.jpg" with your image file path
    }

    private void createButtons() {
        easyButton = new JButton();
        easyButton.setText("Easy");
        easyButton.setSize(300, 55);
        easyButton.setLocation(100, 300);
        easyButton.addActionListener((e) -> {
            this.setVisible(false);
        });

        // easyButton.setIcon(new ImageIcon("easy_icon.png")); // Replace "easy_icon.png" with the icon image for the Easy button
        mediumButton = new JButton();
        mediumButton.setText("Medium");
        mediumButton.setSize(300, 60);
        mediumButton.setLocation(100, 410);
        mediumButton.addActionListener((e) -> {
            this.setVisible(false);
            GameFrame.get().getGameConfiguration().promptUser();
            GameFrame.get().setupUpdate(GameFrame.get().getGameConfiguration());
        });
        // mediumButton.setIcon(new ImageIcon("medium_icon.png")); // Replace "medium_icon.png" with the icon image for the Medium button
        hardButton = new JButton("Hard");
        hardButton.setSize(300, 60);
        hardButton.setLocation(100, 520);
        hardButton.addActionListener((e) -> {
            this.setVisible(false);
            GameFrame.get().getGameConfiguration().promptUser();
            GameFrame.get().setupUpdate(GameFrame.get().getGameConfiguration());
        });
        //  hardButton.setIcon(new ImageIcon("hard_icon.png")); // Replace "hard_icon.png" with the icon image for the Hard button
    }

    private void addButtonsToFrame() {
        this.add(easyButton);
        this.add(mediumButton);
        this.add(hardButton);
    }

    public static void main(String[] args) {
        //SwingUtilities.invokeLater(() -> new DifficultyFrame().setVisible(true));
        new DifficultyFrame().setVisible(true);
    }
}
