package view;

import javax.swing.*;
import java.awt.*;

class DifficultyFrame extends JFrame {
    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(530, 850);
    private JButton easyButton;
    private JButton mediumButton;
    private JButton hardButton;
    private static String difficulty = null;

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
        setContentPane(new JLabel(new ImageIcon("background.png")));
    }

    private void createButtons() {
        easyButton = new JButton();
        easyButton.setText("Easy");
        easyButton.setSize(300, 55);
        easyButton.setLocation(120, 300);
        easyButton.addActionListener((e) -> {
            this.setVisible(false);
            setDifficulty("easy");
            GameFrame.get().getGameConfiguration().promptUser();
            GameFrame.get().setupUpdate(GameFrame.get().getGameConfiguration());
        });

        // easyButton.setIcon(new ImageIcon("easy_icon.png")); // Replace "easy_icon.png" with the icon image for the Easy button
        mediumButton = new JButton();
        mediumButton.setText("Medium");
        mediumButton.setSize(300, 60);
        mediumButton.setLocation(120, 410);
        mediumButton.addActionListener((e) -> {
            this.setVisible(false);
            setDifficulty("medium");
            GameFrame.get().getGameConfiguration().promptUser();
            GameFrame.get().setupUpdate(GameFrame.get().getGameConfiguration());
        });
        // mediumButton.setIcon(new ImageIcon("medium_icon.png")); // Replace "medium_icon.png" with the icon image for the Medium button
        hardButton = new JButton("Hard");
        hardButton.setSize(300, 60);
        hardButton.setLocation(120, 520);
        hardButton.addActionListener((e) -> {
            this.setVisible(false);
            setDifficulty("hard");
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

    private static void setDifficulty(String difficulty) {
        DifficultyFrame.difficulty = difficulty;
    }

    static String getDifficulty() {
        return difficulty;
    }

    public static void main(String[] args) {
        //SwingUtilities.invokeLater(() -> new DifficultyFrame().setVisible(true));
        new DifficultyFrame().setVisible(true);
    }
}
