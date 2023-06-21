package view;

import model.Controller;

import javax.swing.*;
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

        final JMenuItem saveMenuItem = new JMenuItem("\uD83D\uDCBE  Save Game");
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              Controller.saveGame();
            }
        });
        settingMenu.add(saveMenuItem);

        final JMenuItem backMenuItem = new JMenuItem("\uD83D\uDD19  Back To Main Menu");
        backMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.backToMainMenu();
            }
        });
        settingMenu.add(backMenuItem);

        final JMenuItem exitMenuItem = new JMenuItem("❌  Exit Game");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.exitGame();
            }
        });
        settingMenu.add(exitMenuItem);
        return settingMenu;
    }

    private static JMenu createGameplayOptions() {
        final JMenu gameplayOptionsMenu = new JMenu("\uD83C\uDFAE  Gameplay Options");

        final JMenuItem restartMenuItem = new JMenuItem("\uD83D\uDD04  Restart");
        restartMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.restartGame();
            }
        });
        gameplayOptionsMenu.add(restartMenuItem);

        final JMenuItem undoMenuItem = new JMenuItem("↩\uFE0F   Undo");
        undoMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.undoMove();
            }
        });
        gameplayOptionsMenu.add(undoMenuItem);

        final JMenuItem replayAllMovesMenuItem = new JMenuItem(" ⏯\uFE0F   Playback Moves");
        replayAllMovesMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.replayMoves();
            }
        });
        gameplayOptionsMenu.add(replayAllMovesMenuItem);

        final JMenuItem changeBoardMenuItem = new JMenuItem("\uD83D\uDDBC  Change Board");
        changeBoardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.changeBoard();
            }
        });
        gameplayOptionsMenu.add(changeBoardMenuItem);

        final JMenuItem flipBoard = new JMenuItem("⇅   Flip Board");
        flipBoard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.flipBoard();
            }
        });
        gameplayOptionsMenu.add(flipBoard);

        final JMenuItem flipRedSide = new JMenuItem(" ↕️\uFE0F   Flip Red Side");
        flipRedSide.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.flipRedSide();
            }
        });
        gameplayOptionsMenu.add(flipRedSide);

        return gameplayOptionsMenu;
    }
}
