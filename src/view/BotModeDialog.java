package view;

import model.player.PlayerType;

import javax.swing.*;
import java.awt.*;

import static view.GameFrame.defaultImagesPath;

class BotModeDialog extends JDialog {
    private static final String BOT1 = "Bot 1: Minimax ( 4)";
    private static final String BOT2 = "Bot 2: Bot 1 + Alpha Beta + Move Ordering (6)";
    private static final String BOT3 = "Bot 3: Bot 2 + Quiescence Search (Dynamic Depth)";
    private static final JRadioButton blueBot1Button = new JRadioButton(BOT1);
    private static final JRadioButton blueBot2Button = new JRadioButton(BOT2);
    private static final JRadioButton blueBot3Button = new JRadioButton(BOT3);
    private static final JRadioButton redBot1Button = new JRadioButton(BOT1);
    private static final JRadioButton redBot2Button = new JRadioButton(BOT2);
    private static final JRadioButton redBot3Button = new JRadioButton(BOT3);
    private static boolean botGame = false;

    BotModeDialog(final JFrame frame, final boolean modal) {
        super(frame, modal);
        this.setTitle("Select Bot Types");
        final JPanel botModePanel = new JPanel(new GridLayout(0, 1));
        blueBot1Button.setActionCommand(BOT1);

        final ButtonGroup blueGroup = new ButtonGroup();
        blueGroup.add(blueBot1Button);
        blueGroup.add(blueBot2Button);
        blueGroup.add(blueBot3Button);
        blueBot1Button.setSelected(true);

        final ButtonGroup redGroup = new ButtonGroup();
        redGroup.add(redBot1Button);
        redGroup.add(redBot2Button);
        redGroup.add(redBot3Button);
        redBot1Button.setSelected(true);

        getContentPane().add(botModePanel);
        botModePanel.add(new JLabel("      Blue Side"));
        botModePanel.add(blueBot1Button);
        botModePanel.add(blueBot2Button);
        botModePanel.add(blueBot3Button);
        botModePanel.add(new JLabel("      Red Side"));
        botModePanel.add(redBot1Button);
        botModePanel.add(redBot2Button);
        botModePanel.add(redBot3Button);

        final JButton okButton = new JButton("Get Started");

        okButton.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            botGame = true;
            setBlueBotDifficulty();
            GameFrame.get().getGameConfiguration().setBluePlayerType(PlayerType.AI);
            GameFrame.get().getGameConfiguration().setRedPlayerType(PlayerType.AI);
            GameFrame.get().getGameConfiguration().setReady(true);
            GameFrame.get().setupUpdate(GameFrame.get().getGameConfiguration());
            if (GameFrame.get().getGameConfiguration().isReady()) {
                if (!GameFrame.get().isBlitzMode()
                        && GameFrame.get().getPlayerPanel().isNormalModeWithTimer()) {
                    GameFrame.get().getPlayerPanel().initTimerForNormalMode();
                    GameFrame.get().getPlayerPanel().setStopTimerInNormalMode(false);
                    GameFrame.get().getPlayerPanel().getTimerNormalMode().start();
                } else if (GameFrame.get().isBlitzMode()) {
                    GameFrame.get().setBlitzModeGameOver(false);
                    GameFrame.get().getPlayerPanel().initBlitzMode();
                }
                if (!MainMenuFrame.get().isGrayScaleBGMButton()) {
                    AudioPlayer.LoopPlayer.playGameBGM();
                }
                MainMenuFrame.get().setVisible(false);
                BotModeDialog.this.setVisible(false);
                GameFrame.get().setVisible(true);
            }
            System.out.println("Set Up Game");
        });

        botModePanel.add(okButton);
        ImageIcon logo = new ImageIcon(defaultImagesPath + "junglechesslogo.jpg");
        this.setIconImage(logo.getImage());
        this.setSize(320, 320);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(false);
    }

    void promptUser() {
        setVisible(true);
        repaint();
    }

    static void setBlueBotDifficulty() {
        if (blueBot1Button.isSelected()) {
            DifficultyFrame.setDifficulty(DifficultyFrame.Difficulty.EASY);
        } else if (blueBot2Button.isSelected()) {
            DifficultyFrame.setDifficulty(DifficultyFrame.Difficulty.MEDIUM);
        } else if (blueBot3Button.isSelected()) {
            DifficultyFrame.setDifficulty(DifficultyFrame.Difficulty.HARD);
        }
    }

    static void setRedBotDifficulty() {
        if (redBot1Button.isSelected()) {
            DifficultyFrame.setDifficulty(DifficultyFrame.Difficulty.EASY);
        } else if (redBot2Button.isSelected()) {
            DifficultyFrame.setDifficulty(DifficultyFrame.Difficulty.MEDIUM);
        } else if (redBot3Button.isSelected()) {
            DifficultyFrame.setDifficulty(DifficultyFrame.Difficulty.HARD);
        }
    }

    public static boolean isBotGame() {
        return botGame;
    }
}
