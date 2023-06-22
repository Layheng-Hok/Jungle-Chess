package view;

import model.Controller;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuBar {
    static JMenuBar createGameFrameMenuBar() {
        final JMenuBar gameFrameMenuBar = new JMenuBar();
        gameFrameMenuBar.add(createSettingMenu());
        gameFrameMenuBar.add(createGameplayOptions());
        return gameFrameMenuBar;
    }

    private static JMenu createSettingMenu() {
        final JMenu settingMenu = new JMenu("⚙\uFE0F  Game Setting");
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
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
                Controller.saveGame();
            }
        });
        settingMenu.add(saveMenuItem);

        final JMenuItem bgmAudioControlMenuItem = new JMenuItem("\uD83C\uDFB5  Mute/Unmute BGM");
        bgmAudioControlMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenu.get().bgmButton.doClick();
                if (!MainMenu.get().isGrayScaleBGMButton()) {
                    AudioPlayer.LoopPlayer.playGameBGM();
                }
            }
        });
        settingMenu.add(bgmAudioControlMenuItem);

        final JMenuItem soundEffectAudioControlMenuItem = new JMenuItem("\uD83D\uDD0A  Mute/Unmute Sound Effect");
        soundEffectAudioControlMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenu.get().soundEffectButton.doClick();
            }
        });
        settingMenu.add(soundEffectAudioControlMenuItem);

        final JMenuItem backMenuItem = new JMenuItem("\uD83D\uDD19  Back To Main Menu");
        backMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
                Controller.backToMainMenu();
            }
        });
        settingMenu.add(backMenuItem);

        final JMenuItem exitMenuItem = new JMenuItem("❌  Exit Game");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
                Controller.exitGame();
            }
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
        restartMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
                Controller.restartGame();
            }
        });
        gameplayOptionsMenu.add(restartMenuItem);

        final JMenuItem undoMenuItem = new JMenuItem("↩\uFE0F   Undo");
        undoMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
                Controller.undoMove();
            }
        });
        gameplayOptionsMenu.add(undoMenuItem);

        final JMenuItem replayAllMovesMenuItem = new JMenuItem(" ⏯\uFE0F   Playback Moves");
        replayAllMovesMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
                Controller.replayMoves();
            }
        });
        gameplayOptionsMenu.add(replayAllMovesMenuItem);

        final JMenuItem changeBoardMenuItem = new JMenuItem("\uD83D\uDDBC  Change Board");
        changeBoardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
                Controller.changeBoard();
            }
        });
        gameplayOptionsMenu.add(changeBoardMenuItem);

        final JMenuItem switchSide = new JMenuItem("⇅   Switch Side");
        switchSide.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
                Controller.switchSide();
            }
        });
        gameplayOptionsMenu.add(switchSide);

        final JMenuItem flipPieceIcons = new JMenuItem(" ↕️\uFE0F   Flip Piece Icons");
        flipPieceIcons.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
                Controller.flipPiecesIcon();
            }
        });
        gameplayOptionsMenu.add(flipPieceIcons);

        return gameplayOptionsMenu;
    }
}
