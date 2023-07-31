package view;

import model.Controller;
import model.player.PlayerType;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static view.GameFrame.defaultImagesPath;

public class MainMenuFrame extends JFrame {
    public JButton bgmButton;
    public JButton soundEffectButton;
    public final ImageIcon soundEffectIcon = new ImageIcon(new ImageIcon(defaultImagesPath + "soundeffect.png").getImage().getScaledInstance
            (107, 48, Image.SCALE_DEFAULT));
    private final Image graySoundEffectImage = toGrayScale(soundEffectIcon.getImage());
    public final ImageIcon graySoundEffectIcon = new ImageIcon(graySoundEffectImage);
    public final ImageIcon bgmIcon = new ImageIcon(new ImageIcon(defaultImagesPath + "music.png").getImage().getScaledInstance
            (107, 48, Image.SCALE_DEFAULT));
    private final Image grayBGMImage = toGrayScale(bgmIcon.getImage());
    public final ImageIcon grayBGMIcon = new ImageIcon(grayBGMImage);
    private boolean grayScaleSoundEffectButton = true;
    private boolean grayScaleBGMButton = true;
    private static final MainMenuFrame INSTANCE = new MainMenuFrame();

    public MainMenuFrame() {
        this.setTitle("Jungle Chess (斗兽棋) - Main Menu");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setBounds(630, 180, 530, 850);
        this.setIconImage(GameFrame.get().logo.getImage());
        this.getContentPane().setBackground(Color.GRAY);
        this.createOnePlayerButton();
        this.createTwoPlayerButton();
        this.createSavedGamesButton();
        this.createBotModeButton();
        this.createSoundEffectButton();
        this.createBackgroundMusicButton();
        this.setBackground();
        if (!grayScaleBGMButton) {
            AudioPlayer.LoopPlayer.playMenuBGM();
        }
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }

    private void setBackground() {
        final String jungleChessIconPath = defaultImagesPath + "logo.png";
        ImageIcon jungleChessIcon = new ImageIcon(new ImageIcon(jungleChessIconPath).getImage().getScaledInstance
                (390, 390, Image.SCALE_DEFAULT));
        JLabel logo = new JLabel(jungleChessIcon);
        logo.setBounds(70, 0, 400, 300);
        this.add(logo);

        String backgroundIconPath = defaultImagesPath + "background.png";
        ImageIcon backgroundIcon = new ImageIcon(new ImageIcon(backgroundIconPath).getImage().getScaledInstance
                (530, 850, Image.SCALE_DEFAULT));
        JLabel background = new JLabel(backgroundIcon);
        background.setBounds(0, 0, 530, 850);
        this.add(background);
    }

    private void createOnePlayerButton() {
        final String onePlayerIconPath = defaultImagesPath + "oneplayer.png";
        final ImageIcon onePlayerIcon = new ImageIcon(new ImageIcon(onePlayerIconPath).getImage().getScaledInstance
                (178, 74, Image.SCALE_DEFAULT));
        final JButton onePlayerButton = new JButton(onePlayerIcon);
        onePlayerButton.setBounds(170, 340, 180, 80);
        this.add(onePlayerButton);
        onePlayerButton.setIcon(onePlayerIcon);
        onePlayerButton.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            this.setVisible(false);
            DifficultyFrame.get().setVisible(true);
            System.out.println("Load Difficulty Frame");
        });
    }

    private void createTwoPlayerButton() {
        final String twoPlayersIconPath = defaultImagesPath + "twoplayers.png";
        final ImageIcon twoPlayersIcon = new ImageIcon(new ImageIcon(twoPlayersIconPath).getImage().getScaledInstance
                (178, 74, Image.SCALE_DEFAULT));
        final JButton twoPlayersButton = new JButton(twoPlayersIcon);
        twoPlayersButton.setBounds(170, 440, 180, 80);
        this.add(twoPlayersButton);
        twoPlayersButton.setIcon(twoPlayersIcon);
        twoPlayersButton.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            this.setVisible(false);
            if (!GameFrame.get().isBlitzMode()
                    && GameFrame.get().getPlayerPanel().isNormalModeWithTimer()) {
                GameFrame.get().getPlayerPanel().initTimerForNormalMode();
                GameFrame.get().getPlayerPanel().setStopTimerInNormalMode(false);
            } else if (GameFrame.get().isBlitzMode()) {
                GameFrame.get().setBlitzModeGameOver(false);
                GameFrame.get().getPlayerPanel().initBlitzMode();
            }
            GameFrame.get().show();
            GameFrame.get().getGameConfiguration().setBluePlayerType(PlayerType.HUMAN);
            GameFrame.get().getGameConfiguration().setRedPlayerType(PlayerType.HUMAN);
            System.out.println("Load a Two-Player Game");
        });
    }

    private void createSavedGamesButton() {
        final String savedGameIconPath = defaultImagesPath + "savedgames.png";
        final ImageIcon savedGameIcon = new ImageIcon(new ImageIcon(savedGameIconPath).getImage().getScaledInstance
                (178, 74, Image.SCALE_DEFAULT));
        final JButton savedGamesButton = new JButton(savedGameIcon);
        savedGamesButton.setBounds(170, 540, 180, 80);
        this.add(savedGamesButton);
        savedGamesButton.setIcon(savedGameIcon);
        savedGamesButton.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.loadSavedGame();
        });
    }

    private void createBotModeButton() {
        final String botModeImagePath = defaultImagesPath + "botMode.png";
        final ImageIcon botModeImage = new ImageIcon(new ImageIcon(botModeImagePath).getImage().getScaledInstance
                (178, 74, Image.SCALE_DEFAULT));
        final JButton botModeButton = new JButton(botModeImage);
        botModeButton.setBounds(170, 640, 180, 80);
        this.add(botModeButton);
        botModeButton.setIcon(botModeImage);
        botModeButton.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            final BotModeDialog botModeDialog = new BotModeDialog(this, true);
            botModeDialog.promptUser();
        });
    }

    private void createSoundEffectButton() {
        soundEffectButton = new JButton(soundEffectIcon);
        soundEffectButton.setBounds(25, 745, 107, 48);
        this.add(soundEffectButton);
        soundEffectButton.setIcon(graySoundEffectIcon);
        soundEffectButton.addActionListener(e -> {
            grayScaleSoundEffectButton = !grayScaleSoundEffectButton;
            if (grayScaleSoundEffectButton) {
                soundEffectButton.setIcon(graySoundEffectIcon);
                MenuBar.soundEffectAudioControlMenuItem.setSelected(false);
                Controller.updateGameSetting();
                System.out.println("Sound Effect is muted");
            } else {
                AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
                soundEffectButton.setIcon(soundEffectIcon);
                MenuBar.soundEffectAudioControlMenuItem.setSelected(true);
                Controller.updateGameSetting();
                System.out.println("Sound Effect is on");
            }
        });
    }

    private void createBackgroundMusicButton() {
        bgmButton = new JButton(bgmIcon);
        bgmButton.setBounds(385, 745, 107, 48);
        this.add(bgmButton);
        bgmButton.setIcon(grayBGMIcon);
        bgmButton.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            grayScaleBGMButton = !grayScaleBGMButton;
            if (grayScaleBGMButton) {
                bgmButton.setIcon(grayBGMIcon);
                MenuBar.bgmAudioControlMenuItem.setSelected(false);
                AudioPlayer.LoopPlayer.setMenuBGMPlaying(false);
                AudioPlayer.LoopPlayer.stopLoopAudio();
                Controller.updateGameSetting();
                System.out.println("BGM is muted");
            } else {
                bgmButton.setIcon(bgmIcon);
                MenuBar.bgmAudioControlMenuItem.setSelected(true);
                AudioPlayer.LoopPlayer.playMenuBGM();
                Controller.updateGameSetting();
                System.out.println("BGM is on");
            }
        });
    }

    private Image toGrayScale(Image image) {
        BufferedImage grayScaleImage = new BufferedImage(
                image.getWidth(null),
                image.getHeight(null),
                BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2d = grayScaleImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return grayScaleImage;
    }

    public boolean isGrayScaleSoundEffectButton() {
        return grayScaleSoundEffectButton;
    }

    public void setGrayScaleSoundEffectButton(boolean grayScaleSoundEffectButton) {
        this.grayScaleSoundEffectButton = grayScaleSoundEffectButton;
    }

    public boolean isGrayScaleBGMButton() {
        return grayScaleBGMButton;
    }

    public void setGrayScaleBGMButton(boolean grayScaleBGMButton) {
        this.grayScaleBGMButton = grayScaleBGMButton;
    }

    public static MainMenuFrame get() {
        return INSTANCE;
    }
}
