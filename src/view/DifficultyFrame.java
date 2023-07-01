package view;

import javax.swing.*;
import java.awt.*;

import static view.GameFrame.defaultImagesPath;

public class DifficultyFrame extends JFrame {
    private JButton easyButton;
    private JLabel background;
    private static Difficulty difficulty = null;
    private static final DifficultyFrame INSTANCE = new DifficultyFrame();

    DifficultyFrame() {
        this.setTitle("Jungle Chess (斗兽棋) - Difficulty Selection");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setBounds(630, 180, 530, 850);
        this.setIconImage(GameFrame.get().logo.getImage());
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
        final String backgroundIconPath = defaultImagesPath + "background.png";
        final ImageIcon backgroundIcon = new ImageIcon(new ImageIcon(backgroundIconPath).getImage().getScaledInstance
                (530, 850, Image.SCALE_DEFAULT));
        background = new JLabel(backgroundIcon);
        background.setBounds(0, 0, 530, 850);

        final JLabel chooseLevelLabel = new JLabel("CHOOSE LEVEL");
        chooseLevelLabel.setFont(new Font("Consolas", Font.BOLD, 38));
        chooseLevelLabel.setForeground(Color.BLACK);
        chooseLevelLabel.setHorizontalAlignment(SwingConstants.CENTER);
        final int labelWidth = 300;
        final int labelHeight = 50;
        final int labelX = easyButton.getX() + (easyButton.getWidth() - labelWidth) / 2;
        final int labelY = easyButton.getY() - labelHeight - 10;
        chooseLevelLabel.setBounds(labelX, labelY - 55, labelWidth, labelHeight);
        background.add(chooseLevelLabel);
    }

    private void createEasyButton() {
        final String easyIconPath = defaultImagesPath + "easy.png";
        final ImageIcon easyIcon = new ImageIcon(new ImageIcon(easyIconPath).getImage().getScaledInstance
                (180, 80, Image.SCALE_DEFAULT));
        easyButton = new JButton(easyIcon);
        easyButton.setBounds(170, 200, 180, 80);
        this.add(easyButton);
        easyButton.addActionListener((e) -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            setDifficulty(Difficulty.EASY);
            GameFrame.get().getGameConfiguration().promptUser();
            GameFrame.get().setupUpdate(GameFrame.get().getGameConfiguration());
            if (GameFrame.get().getGameConfiguration().isReady()) {
                if (!GameFrame.get().isBlitzMode()
                        && GameFrame.get().getPlayerPanel().isNormalModeWithTimer()) {
                    GameFrame.get().getPlayerPanel().initTimerForNormalMode();
                    GameFrame.get().getPlayerPanel().setStopTimerInNormalMode(false);
                } else if (GameFrame.get().isBlitzMode()) {
                    GameFrame.get().setBlitzModeGameOver(false);
                    GameFrame.get().getPlayerPanel().initBlitzMode();
                }
                this.setVisible(false);
                GameFrame.get().setVisible(true);
            }
        });
    }

    private void createMediumButton() {
        final String mediumIconPath = defaultImagesPath + "medium.png";
        final ImageIcon mediumIcon = new ImageIcon(new ImageIcon(mediumIconPath).getImage().getScaledInstance
                (180, 80, Image.SCALE_DEFAULT));
        JButton mediumButton = new JButton(mediumIcon);
        mediumButton.setBounds(170, 350, 180, 80);
        this.add(mediumButton);
        mediumButton.addActionListener((e) -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            setDifficulty(Difficulty.MEDIUM);
            GameFrame.get().getGameConfiguration().promptUser();
            GameFrame.get().setupUpdate(GameFrame.get().getGameConfiguration());
            if (GameFrame.get().getGameConfiguration().isReady()) {
                if (!GameFrame.get().isBlitzMode()
                        && GameFrame.get().getPlayerPanel().isNormalModeWithTimer()) {
                    GameFrame.get().getPlayerPanel().initTimerForNormalMode();
                    GameFrame.get().getPlayerPanel().setStopTimerInNormalMode(false);
                } else if (GameFrame.get().isBlitzMode()) {
                    GameFrame.get().setBlitzModeGameOver(false);
                    GameFrame.get().getPlayerPanel().initBlitzMode();
                }
                this.setVisible(false);
                GameFrame.get().setVisible(true);
            }
        });
    }

    private void createHardButton() {
        final String hardIconPath = defaultImagesPath + "hard.png";
        final ImageIcon hardIcon = new ImageIcon(new ImageIcon(hardIconPath).getImage().getScaledInstance
                (180, 80, Image.SCALE_DEFAULT));
        final JButton hardButton = new JButton(hardIcon);
        hardButton.setBounds(170, 500, 180, 80);
        this.add(hardButton);
        hardButton.addActionListener((e) -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            setDifficulty(Difficulty.HARD);
            GameFrame.get().getGameConfiguration().promptUser();
            GameFrame.get().setupUpdate(GameFrame.get().getGameConfiguration());
            if (GameFrame.get().getGameConfiguration().isReady()) {
                if (!GameFrame.get().isBlitzMode()
                        && GameFrame.get().getPlayerPanel().isNormalModeWithTimer()) {
                    GameFrame.get().getPlayerPanel().initTimerForNormalMode();
                    GameFrame.get().getPlayerPanel().setStopTimerInNormalMode(false);
                } else if (GameFrame.get().isBlitzMode()) {
                    GameFrame.get().setBlitzModeGameOver(false);
                    GameFrame.get().getPlayerPanel().initBlitzMode();
                }
                this.setVisible(false);
                GameFrame.get().setVisible(true);
            }
        });
    }

    private void createBackButton() {
        final String backIconPath = defaultImagesPath + "back.png";
        final ImageIcon backIcon = new ImageIcon(new ImageIcon(backIconPath).getImage().getScaledInstance
                (128, 57, Image.SCALE_DEFAULT));
        final JButton backButton = new JButton(backIcon);
        backButton.setBounds(50, 650, 128, 57);
        this.add(backButton);
        backButton.addActionListener((e) -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            this.setVisible(false);
            MainMenu.get().setVisible(true);
            System.out.println("Back To Main Menu");
        });
    }

    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD
    }

    public static void setDifficulty(Difficulty difficulty) {
        DifficultyFrame.difficulty = difficulty;
    }

    public static Difficulty getDifficulty() {
        return difficulty;
    }

    public static DifficultyFrame get() {
        return INSTANCE;
    }
}
