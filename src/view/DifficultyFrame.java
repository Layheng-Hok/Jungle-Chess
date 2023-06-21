package view;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import static view.GameFrame.defaultImagesPath;

public class DifficultyFrame extends JFrame {
    private JButton easy;
    private JButton medium;
    private JButton hard;
    private JButton back;
    private JLabel background;
    private final String iconsFolder = "resource/images" + File.separator;
    private final ImageIcon logo = new ImageIcon(defaultImagesPath + "junglechesslogo.jpg");
    private final String backgroundIcon = iconsFolder + "background.png";
    private final String easyIcon = iconsFolder + "easy.png";
    private final String mediumIcon = iconsFolder + "medium.png";
    private final String hardIcon = iconsFolder + "hard.png";
    private final String backIcon = iconsFolder + "back.png";
    private static Difficulty difficulty = null;

    DifficultyFrame() {
        this.setTitle("Jungle Chess (斗兽棋) - Difficulty Selection");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setBounds(630, 180, 530, 850);
        this.setIconImage(logo.getImage());
        createEasyButton();
        createMediumButton();
        createHardButton();
        createEasyButton();
        createBackButton();
        setBackground();
        this.setLocationRelativeTo(null);
        this.add(background);
        this.setResizable(false);
    }

    private void setBackground() {
        ImageIcon backgroundI = new ImageIcon(new ImageIcon(backgroundIcon).getImage().getScaledInstance
                (530, 850, Image.SCALE_DEFAULT));
        background = new JLabel(backgroundI);
        background.setBounds(0, 0, 530, 850);

        JLabel chooseLevelLabel = new JLabel("CHOOSE LEVEL");
        chooseLevelLabel.setFont(new Font("Consolas", Font.BOLD, 38));
        chooseLevelLabel.setForeground(Color.BLACK);
        chooseLevelLabel.setHorizontalAlignment(SwingConstants.CENTER);
        int labelWidth = 300;
        int labelHeight = 50;
        int labelX = easy.getX() + (easy.getWidth() - labelWidth) / 2;
        int labelY = easy.getY() - labelHeight - 10;
        chooseLevelLabel.setBounds(labelX, labelY - 55, labelWidth, labelHeight);
        background.add(chooseLevelLabel);
    }

    private void createEasyButton() {
        ImageIcon easyI = new ImageIcon(new ImageIcon(easyIcon).getImage().getScaledInstance
                (180, 80, Image.SCALE_DEFAULT));
        easy = new JButton(easyI);
        easy.setBounds(170, 200, 180, 80);
        this.add(easy);
        easy.addActionListener((e) -> {
            setDifficulty(Difficulty.EASY);
            GameFrame.get().getGameConfiguration().promptUser();
            GameFrame.get().setupUpdate(GameFrame.get().getGameConfiguration());
            if (GameFrame.get().getGameConfiguration().isReady()) {
                this.setVisible(false);
                GameFrame.get().show();
            }
        });
    }

    private void createMediumButton() {
        ImageIcon mediumI = new ImageIcon(new ImageIcon(mediumIcon).getImage().getScaledInstance
                (180, 80, Image.SCALE_DEFAULT));
        medium = new JButton(mediumI);
        medium.setBounds(170, 350, 180, 80);
        this.add(medium);
        medium.addActionListener((e) -> {
            setDifficulty(Difficulty.MEDIUM);
            GameFrame.get().getGameConfiguration().promptUser();
            GameFrame.get().setupUpdate(GameFrame.get().getGameConfiguration());
            if (GameFrame.get().getGameConfiguration().isReady()) {
                this.setVisible(false);
                GameFrame.get().show();
            }
        });
    }

    private void createHardButton() {
        ImageIcon hardI = new ImageIcon(new ImageIcon(hardIcon).getImage().getScaledInstance
                (180, 80, Image.SCALE_DEFAULT));
        hard = new JButton(hardI);
        hard.setBounds(170, 500, 180, 80);
        this.add(hard);
        hard.addActionListener((e) -> {
            setDifficulty(Difficulty.HARD);
            GameFrame.get().getGameConfiguration().promptUser();
            GameFrame.get().setupUpdate(GameFrame.get().getGameConfiguration());
            if (GameFrame.get().getGameConfiguration().isReady()) {
                this.setVisible(false);
                GameFrame.get().show();
            }
        });
    }

    private void createBackButton() {
        ImageIcon backI = new ImageIcon(new ImageIcon(backIcon).getImage().getScaledInstance
                (128, 57, Image.SCALE_DEFAULT));
        back = new JButton(backI);
        back.setBounds(50, 650, 128, 57);
        this.add(back);
        back.addActionListener((e) -> {
            this.setVisible(false);
            new MainMenu().setVisible(true);
            System.out.println("Back To Main Menu");
        });
    }

    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD
    }

    DifficultyFrame getDifficultyFrame() {
        return this;
    }

    static void setDifficulty(Difficulty difficulty) {
        DifficultyFrame.difficulty = difficulty;
    }

    public static Difficulty getDifficulty() {
        return difficulty;
    }
}
