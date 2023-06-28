package view;

import model.Controller;
import model.board.BoardUtilities;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class MenuBar {
    public static JCheckBoxMenuItem blitzModeCheckBoxMenuItem;

    static JMenuBar createGameFrameMenuBar() {
        final JMenuBar gameFrameMenuBar = new JMenuBar();
        gameFrameMenuBar.add(createSettingMenu());
        gameFrameMenuBar.add(createGameplayOptions());
        return gameFrameMenuBar;
    }

    private static JMenu createSettingMenu() {
        final JMenu settingMenu = new JMenu("⚙️  Game Setting");
        settingMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            }

            @Override
            public void menuDeselected(MenuEvent e) {
            }

            @Override
            public void menuCanceled(MenuEvent e) {
            }
        });

        final JMenuItem saveMenuItem = new JMenuItem("\uD83D\uDCBE  Save Game");
        saveMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.saveGame();
        });
        settingMenu.add(saveMenuItem);

        final JMenu gameModeMenu = new JMenu("\uD83D\uDCDA  Game Mode");
        final JMenu secretDevModeMenuItem = new JMenu("㊙  Secret Dev Mode");
        final JMenuItem question1 = new JMenuItem("Are");
        final JMenuItem question2 = new JMenuItem("you");
        final JMenuItem question3 = new JMenuItem("sure");
        final JMenuItem question4 = new JMenuItem("you");
        final JMenuItem question5 = new JMenuItem("want");
        final JMenuItem question6 = new JMenuItem("to");
        final JMenuItem question7 = new JMenuItem("check");
        final JMenu question8 = new JMenu("this?");
        final JMenuItem i1 = new JMenuItem("You");
        final JMenuItem i2 = new JMenuItem("have");
        final JMenuItem i3 = new JMenuItem("to");
        final JMenuItem i4 = new JMenuItem("figure");
        final JMenuItem i5 = new JMenuItem("it");
        final JMenuItem i6 = new JMenuItem("out");
        final JMenuItem i7 = new JMenuItem("yourself");
        final JMenu i8 = new JMenu("...");
        blitzModeCheckBoxMenuItem = new JCheckBoxMenuItem("⏱   Blitz Mode");
        blitzModeCheckBoxMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            if (blitzModeCheckBoxMenuItem.isSelected()) {
                GameFrame.get().getPlayerPanel().setStopTimerInNormalMode(true);
                GameFrame.get().getPlayerPanel().getTimerNormalMode().stop();
                GameFrame.get().setBlitzMode(true);
                GameFrame.get().setBlitzModeGameOver(false);
                Controller.restartGameWithAnimation();
                GameFrame.get().getPlayerPanel().setBlueCurrentTimerSecondsBlitzMode(GameFrame.get().getPlayerPanel().getRedCurrentTimerSecondsBlitzMode());
                GameFrame.get().getPlayerPanel().setRedCurrentTimerSecondsBlitzMode(GameFrame.get().getPlayerPanel().getBlueCurrentTimerSecondsBlitzMode());
                GameFrame.get().getPlayerPanel().initTimerForBlueBlitzMode();
                GameFrame.get().getPlayerPanel().initTimerForRedBlitzMode();
            } else {
                GameFrame.get().getPlayerPanel().getBlueTimerBlitzMode().stop();
                GameFrame.get().getPlayerPanel().getRedTimerBlitzMode().stop();
                GameFrame.get().setBlitzMode(false);
                Controller.restartGameWithAnimation();
                if (GameFrame.get().getPlayerPanel().isNormalModeWithTimer()) {
                    GameFrame.get().getPlayerPanel().initTimerForNormalMode();
                }
                GameFrame.get().getPlayerPanel().setStopTimerInNormalMode(false);
            }
        });
        final JCheckBoxMenuItem glitchEffectCheckBoxMenuItem = new JCheckBoxMenuItem("\uD83C\uDF0C  Glitch in the Matrix");
        glitchEffectCheckBoxMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            for (int i = 0; i < BoardUtilities.NUM_TERRAINS; i++) {
                GameFrame.get().getBoardPanel().getBoardTerrains().get(i).deselectLeftMouseButton();
            }
            GameFrame.get().setGlitchMode(glitchEffectCheckBoxMenuItem.isSelected());
        });
        settingMenu.add(gameModeMenu);
        gameModeMenu.add(blitzModeCheckBoxMenuItem);
        gameModeMenu.add(secretDevModeMenuItem);
        secretDevModeMenuItem.add(question1);
        secretDevModeMenuItem.add(question2);
        secretDevModeMenuItem.add(question3);
        secretDevModeMenuItem.add(question4);
        secretDevModeMenuItem.add(question5);
        secretDevModeMenuItem.add(question6);
        secretDevModeMenuItem.add(question7);
        secretDevModeMenuItem.add(question8);
        question8.add(i1);
        question8.add(i2);
        question8.add(i3);
        question8.add(i4);
        question8.add(i5);
        question8.add(i6);
        question8.add(i7);
        question8.add(i8);
        i8.add(glitchEffectCheckBoxMenuItem);

        final JMenuItem bgmAudioControlMenuItem = new JMenuItem("\uD83C\uDFB5  Mute/Unmute BGM");
        bgmAudioControlMenuItem.addActionListener(e -> {
            MainMenu.get().bgmButton.doClick();
            if (!MainMenu.get().isGrayScaleBGMButton()) {
                AudioPlayer.LoopPlayer.playGameBGM();
            }
        });
        settingMenu.add(bgmAudioControlMenuItem);

        final JMenuItem soundEffectAudioControlMenuItem = new JMenuItem("\uD83D\uDD0A  Mute/Unmute Sound Effect");
        soundEffectAudioControlMenuItem.addActionListener(e -> MainMenu.get().soundEffectButton.doClick());
        settingMenu.add(soundEffectAudioControlMenuItem);

        final JMenuItem backMenuItem = new JMenuItem("\uD83D\uDD19  Back To Main Menu");
        backMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.backToMainMenu();
        });
        settingMenu.add(backMenuItem);

        final JMenuItem exitMenuItem = new JMenuItem("❌  Exit Game");
        exitMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.exitGame();
        });
        settingMenu.add(exitMenuItem);
        return settingMenu;
    }

    private static JMenu createGameplayOptions() {
        final JMenu gameplayOptionsMenu = new JMenu("\uD83C\uDFAE  Gameplay Options");
        gameplayOptionsMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            }

            @Override
            public void menuDeselected(MenuEvent e) {
            }

            @Override
            public void menuCanceled(MenuEvent e) {
            }
        });

        final JMenuItem restartMenuItem = new JMenuItem("\uD83D\uDD04  Restart");
        restartMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.restartGame();
        });
        gameplayOptionsMenu.add(restartMenuItem);

        final JMenuItem undoMenuItem = new JMenuItem("↩️   Undo");
        undoMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.undoMove();
        });
        gameplayOptionsMenu.add(undoMenuItem);

        final JMenuItem replayAllMovesMenuItem = new JMenuItem(" ⏯️   Playback Moves");
        replayAllMovesMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.replayMoves();
        });
        gameplayOptionsMenu.add(replayAllMovesMenuItem);

        final JMenuItem changeBoardMenuItem = new JMenuItem("\uD83D\uDDBC  Change Board");
        changeBoardMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.changeBoard();
        });
        gameplayOptionsMenu.add(changeBoardMenuItem);

        final JMenuItem switchSide = new JMenuItem("⇅   Switch Side");
        switchSide.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.switchSide();
        });
        gameplayOptionsMenu.add(switchSide);

        final JMenuItem flipPieceIcons = new JMenuItem(" ↕️️   Flip Piece Icons");
        flipPieceIcons.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.flipPiecesIcon();
        });
        gameplayOptionsMenu.add(flipPieceIcons);

        final JMenuItem setTimerMenuItem = new JMenuItem(" ⏳   Set Timer");
        setTimerMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.setTimer();
        });
        gameplayOptionsMenu.add(setTimerMenuItem);

        return gameplayOptionsMenu;
    }
}
