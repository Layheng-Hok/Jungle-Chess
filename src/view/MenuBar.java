package view;

import model.Controller;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
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

        final JMenu glitchEffectMenuItem = new JMenu("\u3299  Secret Dev Mode");
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
        final JCheckBoxMenuItem glitchEffectCheckBoxMenuItem = new JCheckBoxMenuItem("Glitch in the Matrix");
        glitchEffectCheckBoxMenuItem.addActionListener(e -> GameFrame.get().setGlitchMode(glitchEffectCheckBoxMenuItem.isSelected()));
        settingMenu.add(glitchEffectMenuItem);
        glitchEffectMenuItem.add(question1);
        glitchEffectMenuItem.add(question2);
        glitchEffectMenuItem.add(question3);
        glitchEffectMenuItem.add(question4);
        glitchEffectMenuItem.add(question5);
        glitchEffectMenuItem.add(question6);
        glitchEffectMenuItem.add(question7);
        glitchEffectMenuItem.add(question8);
        question8.add(i1);
        question8.add(i2);
        question8.add(i3);
        question8.add(i4);
        question8.add(i5);
        question8.add(i6);
        question8.add(i7);
        question8.add(i8);
        i8.add(glitchEffectCheckBoxMenuItem);

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
