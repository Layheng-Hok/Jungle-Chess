package view;

import model.Controller;
import model.board.BoardUtilities;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuBar {
    public static JCheckBoxMenuItem blitzModeCheckBoxMenuItem;
    public static JCheckBoxMenuItem bgmAudioControlMenuItem;
    public static JCheckBoxMenuItem soundEffectAudioControlMenuItem;
    public static JRadioButtonMenuItem arcDarkRadioButtonMenuItem;
    public static JRadioButtonMenuItem arcDarkOrangeRadioButtonMenuItem;
    public static JRadioButtonMenuItem arcRadioButtonMenuItem;
    public static JRadioButtonMenuItem arcOrangeRadioButtonMenuItem;
    public static JRadioButtonMenuItem carbonRadioButtonMenuItem;
    public static JRadioButtonMenuItem cobalt2RadioButtonMenuItem;
    public static JRadioButtonMenuItem cyanLightRadioButtonMenuItem;
    public static JRadioButtonMenuItem darkFlatRadioButtonMenuItem;
    public static JRadioButtonMenuItem darkPurpleRadioButtonMenuItem;
    public static JRadioButtonMenuItem draculaRadioButtonMenuItem;
    public static JRadioButtonMenuItem gradiantoDarkFuchsiaRadioButtonMenuItem;
    public static JRadioButtonMenuItem gradiantoDeepOceanRadioButtonMenuItem;
    public static JRadioButtonMenuItem gradiantoMidnightBlueRadioButtonMenuItem;
    public static JRadioButtonMenuItem gradiantoNatureGreenRadioButtonMenuItem;
    public static JRadioButtonMenuItem grayRadioButtonMenuItem;
    public static JRadioButtonMenuItem gruvboxDarkHardRadioButtonMenuItem;
    public static JRadioButtonMenuItem gruvboxDarkMediumRadioButtonMenuItem;
    public static JRadioButtonMenuItem gruvboxDarkSoftRadioButtonMenuItem;
    public static JRadioButtonMenuItem hiberbeeDarkRadioButtonMenuItem;
    public static JRadioButtonMenuItem highContrastRadioButtonMenuItem;
    public static JRadioButtonMenuItem lightFlatRadioButtonMenuItem;
    public static JRadioButtonMenuItem materialDesignDarkRadioButtonMenuItem;
    public static JRadioButtonMenuItem monokaiRadioButtonMenuItem;
    public static JRadioButtonMenuItem monokaiProRadioButtonMenuItem;
    public static JRadioButtonMenuItem nordRadioButtonMenuItem;
    public static JRadioButtonMenuItem oneDarkRadioButtonMenuItem;
    public static JRadioButtonMenuItem solarizedDarkRadioButtonMenuItem;
    public static JRadioButtonMenuItem solarizedLightRadioButtonMenuItem;
    public static JRadioButtonMenuItem spacegrayRadioButtonMenuItem;
    public static JRadioButtonMenuItem vuesionRadioButtonMenuItem;
    public static JRadioButtonMenuItem xcodeDarkRadioButtonMenuItem;

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
            if (GameFrame.get().isReplayMovesInProgress()) {
                JOptionPane.showMessageDialog(GameFrame.get().getBoardPanel(),
                        "Replay is in progress. Please wait.");
                return;
            }
            if (GameFrame.get().getGameConfiguration().isAIPlayer(GameFrame.get().getChessBoard().getCurrentPlayer())) {
                JOptionPane.showMessageDialog(GameFrame.get().getBoardPanel(),
                        "AI is still thinking. Please wait.");
                return;
            }
            if (GameFrame.isGameOverScenario(GameFrame.get().getChessBoard())) {
                return;
            }
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
            if (GameFrame.get().isReplayMovesInProgress()) {
                JOptionPane.showMessageDialog(GameFrame.get().getBoardPanel(),
                        "Replay is in progress. Please wait.");
                return;
            }
            if (GameFrame.get().getGameConfiguration().isAIPlayer(GameFrame.get().getChessBoard().getCurrentPlayer())) {
                JOptionPane.showMessageDialog(GameFrame.get().getBoardPanel(),
                        "AI is still thinking. Please wait.");
                return;
            }
            if (GameFrame.isGameOverScenario(GameFrame.get().getChessBoard())) {
                return;
            }
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

        final JMenu uiThemeMenu = new JMenu("\uD83C\uDFA8  UI Theme");
        arcDarkRadioButtonMenuItem = new JRadioButtonMenuItem("Arc Dark");
        arcDarkOrangeRadioButtonMenuItem = new JRadioButtonMenuItem("Arc Dark Orange");
        arcRadioButtonMenuItem = new JRadioButtonMenuItem("Arc");
        arcOrangeRadioButtonMenuItem = new JRadioButtonMenuItem("Arc Orange");
        carbonRadioButtonMenuItem = new JRadioButtonMenuItem("Carbon");
        cobalt2RadioButtonMenuItem = new JRadioButtonMenuItem("Cobalt 2");
        cyanLightRadioButtonMenuItem = new JRadioButtonMenuItem("Cyan Light");
        darkFlatRadioButtonMenuItem = new JRadioButtonMenuItem("Dark Flat");
        darkPurpleRadioButtonMenuItem = new JRadioButtonMenuItem("Dark Purple");
        draculaRadioButtonMenuItem = new JRadioButtonMenuItem("Dracula");
        gradiantoDarkFuchsiaRadioButtonMenuItem = new JRadioButtonMenuItem("Gradianto Dark Fuchsia");
        gradiantoDeepOceanRadioButtonMenuItem = new JRadioButtonMenuItem("Gradianto Deep Ocean");
        gradiantoMidnightBlueRadioButtonMenuItem = new JRadioButtonMenuItem("Gradianto Midnight Blue");
        gradiantoNatureGreenRadioButtonMenuItem = new JRadioButtonMenuItem("Gradianto Nature Green");
        grayRadioButtonMenuItem = new JRadioButtonMenuItem("Gray");
        gruvboxDarkHardRadioButtonMenuItem = new JRadioButtonMenuItem("Gruvbox Dark Hard");
        gruvboxDarkMediumRadioButtonMenuItem = new JRadioButtonMenuItem("Gruvbox Dark Medium");
        gruvboxDarkSoftRadioButtonMenuItem = new JRadioButtonMenuItem("Gruvbox Dark Soft");
        hiberbeeDarkRadioButtonMenuItem = new JRadioButtonMenuItem("Hiberbee Dark");
        highContrastRadioButtonMenuItem = new JRadioButtonMenuItem("High Contrast");
        lightFlatRadioButtonMenuItem = new JRadioButtonMenuItem("Light Flat");
        materialDesignDarkRadioButtonMenuItem = new JRadioButtonMenuItem("Material Design Dark");
        monokaiRadioButtonMenuItem = new JRadioButtonMenuItem("Monokai");
        monokaiProRadioButtonMenuItem = new JRadioButtonMenuItem("Monokai Pro");
        nordRadioButtonMenuItem = new JRadioButtonMenuItem("Nord");
        oneDarkRadioButtonMenuItem = new JRadioButtonMenuItem("One Dark");
        solarizedDarkRadioButtonMenuItem = new JRadioButtonMenuItem("Solarized Dark");
        solarizedLightRadioButtonMenuItem = new JRadioButtonMenuItem("Solarized Light");
        spacegrayRadioButtonMenuItem = new JRadioButtonMenuItem("Space Gray");
        vuesionRadioButtonMenuItem = new JRadioButtonMenuItem("Vuesion");
        xcodeDarkRadioButtonMenuItem = new JRadioButtonMenuItem("Xcode Dark");
        final ButtonGroup uiThemeButtonGroup = new ButtonGroup();
        uiThemeButtonGroup.add(arcDarkRadioButtonMenuItem);
        uiThemeButtonGroup.add(arcDarkOrangeRadioButtonMenuItem);
        uiThemeButtonGroup.add(arcRadioButtonMenuItem);
        uiThemeButtonGroup.add(arcOrangeRadioButtonMenuItem);
        uiThemeButtonGroup.add(carbonRadioButtonMenuItem);
        uiThemeButtonGroup.add(cobalt2RadioButtonMenuItem);
        uiThemeButtonGroup.add(cyanLightRadioButtonMenuItem);
        uiThemeButtonGroup.add(darkFlatRadioButtonMenuItem);
        uiThemeButtonGroup.add(darkPurpleRadioButtonMenuItem);
        uiThemeButtonGroup.add(draculaRadioButtonMenuItem);
        uiThemeButtonGroup.add(gradiantoDarkFuchsiaRadioButtonMenuItem);
        uiThemeButtonGroup.add(gradiantoDeepOceanRadioButtonMenuItem);
        uiThemeButtonGroup.add(gradiantoMidnightBlueRadioButtonMenuItem);
        uiThemeButtonGroup.add(gradiantoNatureGreenRadioButtonMenuItem);
        uiThemeButtonGroup.add(grayRadioButtonMenuItem);
        uiThemeButtonGroup.add(gruvboxDarkHardRadioButtonMenuItem);
        uiThemeButtonGroup.add(gruvboxDarkMediumRadioButtonMenuItem);
        uiThemeButtonGroup.add(gruvboxDarkSoftRadioButtonMenuItem);
        uiThemeButtonGroup.add(hiberbeeDarkRadioButtonMenuItem);
        uiThemeButtonGroup.add(highContrastRadioButtonMenuItem);
        uiThemeButtonGroup.add(lightFlatRadioButtonMenuItem);
        uiThemeButtonGroup.add(materialDesignDarkRadioButtonMenuItem);
        uiThemeButtonGroup.add(monokaiRadioButtonMenuItem);
        uiThemeButtonGroup.add(monokaiProRadioButtonMenuItem);
        uiThemeButtonGroup.add(nordRadioButtonMenuItem);
        uiThemeButtonGroup.add(oneDarkRadioButtonMenuItem);
        uiThemeButtonGroup.add(solarizedDarkRadioButtonMenuItem);
        uiThemeButtonGroup.add(solarizedLightRadioButtonMenuItem);
        uiThemeButtonGroup.add(spacegrayRadioButtonMenuItem);
        uiThemeButtonGroup.add(vuesionRadioButtonMenuItem);
        uiThemeButtonGroup.add(xcodeDarkRadioButtonMenuItem);
        uiThemeMenu.add(arcDarkRadioButtonMenuItem);
        uiThemeMenu.add(arcDarkOrangeRadioButtonMenuItem);
        uiThemeMenu.add(arcRadioButtonMenuItem);
        uiThemeMenu.add(arcOrangeRadioButtonMenuItem);
        uiThemeMenu.add(carbonRadioButtonMenuItem);
        uiThemeMenu.add(cobalt2RadioButtonMenuItem);
        uiThemeMenu.add(cyanLightRadioButtonMenuItem);
        uiThemeMenu.add(darkFlatRadioButtonMenuItem);
        uiThemeMenu.add(darkPurpleRadioButtonMenuItem);
        uiThemeMenu.add(draculaRadioButtonMenuItem);
        uiThemeMenu.add(gradiantoDarkFuchsiaRadioButtonMenuItem);
        uiThemeMenu.add(gradiantoDeepOceanRadioButtonMenuItem);
        uiThemeMenu.add(gradiantoMidnightBlueRadioButtonMenuItem);
        uiThemeMenu.add(gradiantoNatureGreenRadioButtonMenuItem);
        uiThemeMenu.add(grayRadioButtonMenuItem);
        uiThemeMenu.add(gruvboxDarkHardRadioButtonMenuItem);
        uiThemeMenu.add(gruvboxDarkMediumRadioButtonMenuItem);
        uiThemeMenu.add(gruvboxDarkSoftRadioButtonMenuItem);
        uiThemeMenu.add(hiberbeeDarkRadioButtonMenuItem);
        uiThemeMenu.add(highContrastRadioButtonMenuItem);
        uiThemeMenu.add(lightFlatRadioButtonMenuItem);
        uiThemeMenu.add(materialDesignDarkRadioButtonMenuItem);
        uiThemeMenu.add(monokaiRadioButtonMenuItem);
        uiThemeMenu.add(monokaiProRadioButtonMenuItem);
        uiThemeMenu.add(nordRadioButtonMenuItem);
        uiThemeMenu.add(oneDarkRadioButtonMenuItem);
        uiThemeMenu.add(solarizedDarkRadioButtonMenuItem);
        uiThemeMenu.add(solarizedLightRadioButtonMenuItem);
        uiThemeMenu.add(spacegrayRadioButtonMenuItem);
        uiThemeMenu.add(vuesionRadioButtonMenuItem);
        uiThemeMenu.add(xcodeDarkRadioButtonMenuItem);
        arcDarkRadioButtonMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.setUITheme(0);
        });
        arcDarkOrangeRadioButtonMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.setUITheme(1);
        });
        arcRadioButtonMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.setUITheme(2);
        });
        arcOrangeRadioButtonMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.setUITheme(3);
        });
        carbonRadioButtonMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.setUITheme(4);
        });
        cobalt2RadioButtonMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.setUITheme(5);
        });
        cyanLightRadioButtonMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.setUITheme(6);
        });
        darkFlatRadioButtonMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.setUITheme(7);
        });
        darkPurpleRadioButtonMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.setUITheme(8);
        });
        draculaRadioButtonMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.setUITheme(9);
        });
        gradiantoDarkFuchsiaRadioButtonMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.setUITheme(10);
        });
        gradiantoDeepOceanRadioButtonMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.setUITheme(11);
        });
        gradiantoMidnightBlueRadioButtonMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.setUITheme(12);
        });
        gradiantoNatureGreenRadioButtonMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.setUITheme(13);
        });
        grayRadioButtonMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.setUITheme(14);
        });
        gruvboxDarkHardRadioButtonMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.setUITheme(15);
        });
        gruvboxDarkMediumRadioButtonMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.setUITheme(16);
        });
        gruvboxDarkSoftRadioButtonMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.setUITheme(17);
        });
        hiberbeeDarkRadioButtonMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.setUITheme(18);
        });
        highContrastRadioButtonMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.setUITheme(19);
        });
        lightFlatRadioButtonMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.setUITheme(20);
        });
        materialDesignDarkRadioButtonMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.setUITheme(21);
        });
        monokaiRadioButtonMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.setUITheme(22);
        });
        monokaiProRadioButtonMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.setUITheme(23);
        });
        nordRadioButtonMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.setUITheme(24);
        });
        oneDarkRadioButtonMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.setUITheme(25);
        });
        solarizedDarkRadioButtonMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.setUITheme(26);
        });
        solarizedLightRadioButtonMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.setUITheme(27);
        });
        spacegrayRadioButtonMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.setUITheme(28);
        });
        vuesionRadioButtonMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.setUITheme(29);
        });
        xcodeDarkRadioButtonMenuItem.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            Controller.setUITheme(30);
        });
        settingMenu.add(uiThemeMenu);

        soundEffectAudioControlMenuItem = new JCheckBoxMenuItem("\uD83D\uDD0A  Sound Effect");
        soundEffectAudioControlMenuItem.setSelected(false);
        soundEffectAudioControlMenuItem.addActionListener(e -> MainMenu.get().soundEffectButton.doClick());
        settingMenu.add(soundEffectAudioControlMenuItem);

        bgmAudioControlMenuItem = new JCheckBoxMenuItem("\uD83C\uDFB5  Background Music");
        bgmAudioControlMenuItem.setSelected(false);
        bgmAudioControlMenuItem.addActionListener(e -> {
            MainMenu.get().bgmButton.doClick();
            if (!MainMenu.get().isGrayScaleBGMButton()) {
                AudioPlayer.LoopPlayer.playGameBGM();
            }
        });
        settingMenu.add(bgmAudioControlMenuItem);

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
