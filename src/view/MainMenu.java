package view;

import model.Controller;
import model.player.PlayerType;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static view.GameFrame.defaultImagesPath;

public class MainMenu extends JFrame {
    JButton bgmButton;
    JButton soundEffectButton;
    private static boolean isGrayScaleSoundEffectButton = false;
    private static boolean isGrayScaleBGMButton = false;
    private static final MainMenu INSTANCE = new MainMenu();

    public MainMenu() {
        setTitle("Jungle Chess (斗兽棋) - Main Menu");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setBounds(630, 180, 530, 850);
        this.setIconImage(GameFrame.get().logo.getImage());
        this.getContentPane().setBackground(Color.GRAY);
        createOnePlayerButton();
        createTwoPlayerButton();
        createSavedGamesButton();
        createGoOnlineButton();
        createSoundEffectButton();
        createBackgroundMusicButton();
        setBackground();
        AudioPlayer.LoopPlayer.playMenuBGM();
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

    private void createGoOnlineButton() {
        final String goOnlineImagePath = defaultImagesPath + "goonline.png";
        final ImageIcon goOnlineImage = new ImageIcon(new ImageIcon(goOnlineImagePath).getImage().getScaledInstance
                (178, 74, Image.SCALE_DEFAULT));
        final JButton goOnlineButton = new JButton(goOnlineImage);
        goOnlineButton.setBounds(170, 640, 180, 80);
        this.add(goOnlineButton);
        goOnlineButton.setIcon(goOnlineImage);
        goOnlineButton.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
        });
    }

    private void createSoundEffectButton() {
        final String soundEffectImagePath = defaultImagesPath + "soundeffect.png";
        final ImageIcon soundEffectIcon = new ImageIcon(new ImageIcon(soundEffectImagePath).getImage().getScaledInstance
                (107, 48, Image.SCALE_DEFAULT));
        final Image graySoundEffectImage = toGrayScale(soundEffectIcon.getImage());
        final ImageIcon graySoundEffectIcon = new ImageIcon(graySoundEffectImage);
        soundEffectButton = new JButton(soundEffectIcon);
        soundEffectButton.setBounds(25, 745, 107, 48);
        this.add(soundEffectButton);
        soundEffectButton.setIcon(soundEffectIcon);
        soundEffectButton.addActionListener(e -> {
            isGrayScaleSoundEffectButton = !isGrayScaleSoundEffectButton;
            if (isGrayScaleSoundEffectButton) {
                soundEffectButton.setIcon(graySoundEffectIcon);
                System.out.println("Sound Effect is muted");
            } else {
                AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
                soundEffectButton.setIcon(soundEffectIcon);
                System.out.println("Sound Effect is on");
            }
        });
    }

    private void createBackgroundMusicButton() {
        final String bgmIconPath = defaultImagesPath + "music.png";
        final ImageIcon bgmIcon = new ImageIcon(new ImageIcon(bgmIconPath).getImage().getScaledInstance
                (107, 48, Image.SCALE_DEFAULT));
        final Image grayBGMImage = toGrayScale(bgmIcon.getImage());
        final ImageIcon grayBGMIcon = new ImageIcon(grayBGMImage);
        bgmButton = new JButton(bgmIcon);
        bgmButton.setBounds(385, 745, 107, 48);
        this.add(bgmButton);
        bgmButton.setIcon(bgmIcon);
        bgmButton.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            isGrayScaleBGMButton = !isGrayScaleBGMButton;
            if (isGrayScaleBGMButton) {
                bgmButton.setIcon(grayBGMIcon);
                AudioPlayer.LoopPlayer.setMenuBGMPlaying(false);
                AudioPlayer.LoopPlayer.stopLoopAudio();
                System.out.println("BGM is muted");
            } else {
                bgmButton.setIcon(bgmIcon);
                AudioPlayer.LoopPlayer.playMenuBGM();
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
        return isGrayScaleSoundEffectButton;
    }

    public boolean isGrayScaleBGMButton() {
        return isGrayScaleBGMButton;
    }

    public static MainMenu get() {
        return INSTANCE;
    }
}
