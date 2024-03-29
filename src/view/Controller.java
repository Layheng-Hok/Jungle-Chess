package view;

import model.board.*;
import model.player.PlayerColor;
import model.player.PlayerType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;

import static view.GameFrame.defaultImagesPath;

public class Controller {
    private static boolean firstReplay = true;
    private static boolean callFromSaveReplay = false;
    private static final ImageIcon gameOverIcon = new ImageIcon(defaultImagesPath + "gameover.png");
    private static final ImageIcon resizedGameOverIcon = new ImageIcon(gameOverIcon.getImage().getScaledInstance(35, 35, Image.SCALE_DEFAULT));

    private Controller() {
        throw new RuntimeException("You cannot instantiate an object of \"Controller\" class.");
    }

    public static void saveGame() {
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
        if (BoardUtils.isGameOverScenario(GameFrame.get().getChessBoard())) {
            return;
        }
        String fileName = JOptionPane.showInputDialog("File Name");
        while (fileName.equals("")) {
            JOptionPane.showMessageDialog(GameFrame.get().getBoardPanel(),
                    "Name cannot be empty. Please enter again.");
            fileName = JOptionPane.showInputDialog("File Name");
        }
        writeGame(fileName);
    }

    private static void writeGame(String fileName) {
        if (BotModeDialog.isBotGame()) {
            JOptionPane.showMessageDialog(GameFrame.get().getBoardPanel(),
                    "Feature not supported.",
                    "Error Message",
                    JOptionPane.ERROR_MESSAGE);
        }
        String location = "database\\" + fileName + ".txt";
        if (!fileName.matches("[^\\\\/:*?\"<>|]+")) {
            JOptionPane.showMessageDialog(GameFrame.get().getBoardPanel(),
                    "A file name cannot contain any illegal characters.",
                    "Invalid File Name",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        File file = new File(location);
        try {
            if (file.exists()) {
                int response = JOptionPane.showConfirmDialog(GameFrame.get().getBoardPanel(),
                        "The file already exists, do you want to overwrite it?",
                        "Overlapped File Name",
                        JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.NO_OPTION) {
                    return;
                }
            }
            FileWriter fileWriter = new FileWriter(file);
            String difficulty = "nu";
            if (GameFrame.get().getGameConfiguration().getBluePlayerType() == PlayerType.HUMAN &&
                    GameFrame.get().getGameConfiguration().getRedPlayerType() == PlayerType.AI
                    || GameFrame.get().getGameConfiguration().getBluePlayerType() == PlayerType.AI &&
                    GameFrame.get().getGameConfiguration().getRedPlayerType() == PlayerType.HUMAN) {
                difficulty = DifficultyFrame.getDifficulty().toString().toLowerCase().substring(0, 2);
            }
            fileWriter.write(GameFrame.get().getGameConfiguration().getBluePlayerType().toString().toLowerCase().substring(0, 2) + " "
                    + GameFrame.get().getGameConfiguration().getRedPlayerType().toString().toLowerCase().substring(0, 2) + " "
                    + difficulty + "\n");
            fileWriter.write(GameFrame.get().getMoveLog().size() + "\n");
            for (int i = 0; i < GameFrame.get().getMoveLog().size(); i++) {
                fileWriter.write(GameFrame.get().getMoveLog().getMove(i).toString() + "\n");
            }
            fileWriter.write(GameFrame.get().getChessBoard().getCurrentPlayer().toString().toLowerCase().substring(0, 2) + "\n");
            fileWriter.write(GameFrame.get().getChessBoard().toString());
            if (GameFrame.get().isBlitzMode()) {
                fileWriter.write("blitz ");
                if (GameFrame.get().getPlayerPanel().getBlueCurrentTimerSecondsBlitzMode() <= 0) {
                    fileWriter.write("0 ");
                } else {
                    fileWriter.write(GameFrame.get().getPlayerPanel().getBlueCurrentTimerSecondsBlitzMode() + " ");
                }
                if (GameFrame.get().getPlayerPanel().getRedCurrentTimerSecondsBlitzMode() <= 0) {
                    fileWriter.write("0\n");
                } else {
                    fileWriter.write(GameFrame.get().getPlayerPanel().getRedCurrentTimerSecondsBlitzMode() + "\n");
                }
            } else {
                fileWriter.write("normal ");
                if (GameFrame.get().getPlayerPanel().isNormalModeWithTimer()) {
                    fileWriter.write(GameFrame.get().getPlayerPanel().getInitialTimerSecondsNormalMode() + "\n");
                } else {
                    fileWriter.write("0\n");
                }
            }
            if (callFromSaveReplay && GameFrame.get().isGameResigned()) {
                fileWriter.write("resign\n");
            }
            fileWriter.close();
            ProgressFrame progressFrame = new ProgressFrame("Saving");
            progressFrame.addProgressListener(() -> {
                String[] options = {"Continue", "Back"};
                int choice = JOptionPane.showOptionDialog(GameFrame.get().getBoardPanel(),
                        "Game saved successfully.\nWould you like to continue playing or go back to the main menu?",
                        "Game Saved",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]);
                if (callFromSaveReplay && (choice == 0 || choice == JOptionPane.CLOSED_OPTION)) {
                    restartGameWithAnimation();
                    callFromSaveReplay = false;
                    System.out.println("Game Restarted");
                }
                if (choice == 1) {
                    GameFrame.get().getGameConfiguration().setBluePlayerType(PlayerType.HUMAN);
                    GameFrame.get().getGameConfiguration().setRedPlayerType(PlayerType.HUMAN);
                    GameFrame.get().restartGame();
                    GameFrame.get().dispose();
                    MainMenuFrame.get().setVisible(true);
                    GameFrame.get().getPlayerPanel().setStopTimerInNormalMode(true);
                    if (!GameFrame.get().isBlitzMode()) {
                        GameFrame.get().getPlayerPanel().getTimerNormalMode().stop();
                    }
                    if (GameFrame.get().isBlitzMode()) {
                        GameFrame.get().getPlayerPanel().getBlueTimerBlitzMode().stop();
                        GameFrame.get().getPlayerPanel().getRedTimerBlitzMode().stop();
                    }
                    System.out.println("Back To Main Menu");
                }
            });
            System.out.println("Game Saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadSavedGame() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("database"));
        fileChooser.showOpenDialog(MainMenuFrame.get());
        File file = fileChooser.getSelectedFile();

        if (!file.getName().endsWith(".txt")) {
            JOptionPane.showMessageDialog(MainMenuFrame.get(),
                    "The file extension is either missing or not supported.",
                    "File Load Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        ArrayList<String> readList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                readList.add(line);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(MainMenuFrame.get(),
                    "Error reading file.",
                    "File Read Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Board loadedBoard = Board.constructStandardBoard();
        MoveLog moveLog = new MoveLog();
        ArrayList<String> playerTypeList;
        int numMoves;
        ArrayList<String> playerList = new ArrayList<>();
        ArrayList<Integer> currentCoordinateList = new ArrayList<>();
        ArrayList<Integer> destinationCoordinateList = new ArrayList<>();
        String playerTypeLine = readList.remove(0);
        try {
            playerTypeList = new ArrayList<>(Arrays.asList(playerTypeLine.split(" ")));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(MainMenuFrame.get(),
                    "The file is either corrupted or invalid.",
                    "File Load Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            numMoves = Integer.parseInt(readList.remove(0));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(MainMenuFrame.get(),
                    "The file is either corrupted or invalid.",
                    "File Load Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (int i = 0; i < numMoves; i++) {
            String moveLine = readList.remove(0);
            String[] moveTokens = moveLine.split(" ");
            try {
                if (moveTokens.length != 3) {
                    JOptionPane.showMessageDialog(MainMenuFrame.get(),
                            "The file is either corrupted or invalid.",
                            "File Load Error",
                            JOptionPane.ERROR_MESSAGE);
                    System.out.println("Error in move " + i);
                    return;
                }
                playerList.add(moveTokens[0]);
                try {
                    currentCoordinateList.add(Integer.parseInt(moveTokens[1]));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(MainMenuFrame.get(),
                            "The file is either corrupted or invalid.",
                            "File Load Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    destinationCoordinateList.add(Integer.parseInt(moveTokens[2]));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(MainMenuFrame.get(),
                            "The file is either corrupted or invalid.",
                            "File Load Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(MainMenuFrame.get(),
                        "The file is either corrupted or invalid.",
                        "File Load Error",
                        JOptionPane.ERROR_MESSAGE);
                System.out.println("Number Format Exception: Error in move " + i);
                return;
            }
        }

        if (numMoves == currentCoordinateList.size()
                && numMoves == destinationCoordinateList.size()
                && numMoves == playerList.size()) {
            for (int i = 0; i < currentCoordinateList.size(); i++) {
                Move move = Move.MoveFactory.createMove(loadedBoard, currentCoordinateList.get(i), destinationCoordinateList.get(i));
                MoveTransition transition = loadedBoard.getCurrentPlayer().makeMove(move);
                if (transition.getMoveStatus().isDone()) {
                    loadedBoard = transition.getToBoard();
                    moveLog.addMove(move);
                }
            }
        } else {
            JOptionPane.showMessageDialog(MainMenuFrame.get(),
                    "The file is either corrupted or invalid.",
                    "File Load Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nextPlayer = readList.get(0);
        readList.remove(0);
        playerList.add(nextPlayer);
        for (int i = 0; i < playerList.size(); i++) {
            if (i % 2 == 0) {
                if (!playerList.get(i).equals("bl")) {
                    JOptionPane.showMessageDialog(MainMenuFrame.get(),
                            "The file is either corrupted or invalid.",
                            "File Load Error",
                            JOptionPane.ERROR_MESSAGE);
                    System.out.println("Wrong player turns.");
                    System.out.println("Should be blue at index " + i + " but it gives " + playerList.get(i));
                    return;
                }
            } else {
                if (!playerList.get(i).equals("re")) {
                    JOptionPane.showMessageDialog(MainMenuFrame.get(),
                            "The file is either corrupted or invalid.",
                            "File Load Error",
                            JOptionPane.ERROR_MESSAGE);
                    System.out.println("Wrong player turns.");
                    System.out.println("Should be red at index " + i + " but it gives " + playerList.get(i));
                    return;
                }
            }
        }
        int roundNumber = playerList.size() % 2 == 0 ? playerList.size() / 2 : playerList.size() / 2 + 1;
        GameFrame.get().setLoadBoard(loadedBoard, moveLog, roundNumber);
        GameFrame.get().setMoveLog(moveLog);

        if (playerTypeList.get(0).equals("ai") && playerTypeList.get(1).equals("hu")) {
            GameFrame.get().getGameConfiguration().setBluePlayerType(PlayerType.AI);
            GameFrame.get().getGameConfiguration().setRedPlayerType(PlayerType.HUMAN);
            switch (playerTypeList.get(2)) {
                case "ea" -> DifficultyFrame.setDifficulty(DifficultyFrame.Difficulty.EASY);
                case "me" -> DifficultyFrame.setDifficulty(DifficultyFrame.Difficulty.MEDIUM);
                case "ha" -> DifficultyFrame.setDifficulty(DifficultyFrame.Difficulty.HARD);
            }
        } else if (playerTypeList.get(0).equals("hu") && playerTypeList.get(1).equals("ai")) {
            GameFrame.get().getGameConfiguration().setBluePlayerType(PlayerType.HUMAN);
            GameFrame.get().getGameConfiguration().setRedPlayerType(PlayerType.AI);
            switch (playerTypeList.get(2)) {
                case "ea" -> DifficultyFrame.setDifficulty(DifficultyFrame.Difficulty.EASY);
                case "me" -> DifficultyFrame.setDifficulty(DifficultyFrame.Difficulty.MEDIUM);
                case "ha" -> DifficultyFrame.setDifficulty(DifficultyFrame.Difficulty.HARD);
            }
        } else if (playerTypeList.get(0).equals("hu") && playerTypeList.get(1).equals("hu")) {
            GameFrame.get().getGameConfiguration().setBluePlayerType(PlayerType.HUMAN);
            GameFrame.get().getGameConfiguration().setRedPlayerType(PlayerType.HUMAN);
        } else {
            JOptionPane.showMessageDialog(MainMenuFrame.get(),
                    "The file is either corrupted or invalid.",
                    "File Load Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!BoardUtils.isGameOverScenario(loadedBoard) && (playerTypeList.get(0).equals("ai") && nextPlayer.equals("bl")
                || playerTypeList.get(1).equals("ai") && nextPlayer.equals("re"))) {
            JOptionPane.showMessageDialog(MainMenuFrame.get(),
                    "The file is either corrupted or invalid.",
                    "File Load Error",
                    JOptionPane.ERROR_MESSAGE);
            System.out.println("AI player is not supposed to move first after loading a game board.");
            return;
        }

        ArrayList<String> animalList = new ArrayList<>();
        ArrayList<Integer> coordinateList = new ArrayList<>();

        int position = 0;
        int index = 0;
        for (String line : readList) {
            if (index == 9) {
                String[] tokens = line.split("\\s+");
                if (tokens.length != 2 && tokens.length != 3) {
                    JOptionPane.showMessageDialog(MainMenuFrame.get(),
                            "The file is either corrupted or invalid.",
                            "File Load Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (tokens.length == 2) {
                    String mode = tokens[0];
                    int timer;
                    try {
                        timer = Integer.parseInt(tokens[1]);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(MainMenuFrame.get(),
                                "The file is either corrupted or invalid.",
                                "File Load Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (!mode.equals("normal") || (timer < 10 && timer != 0) || timer > 100) {
                        JOptionPane.showMessageDialog(MainMenuFrame.get(),
                                "The file is either corrupted or invalid.",
                                "File Load Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    GameFrame.get().setBlitzMode(false);
                    MenuBar.blitzModeCheckBoxMenuItem.setSelected(false);
                    if (timer == 0) {
                        GameFrame.get().getPlayerPanel().setNormalModeWithTimer(false);
                    } else {
                        GameFrame.get().getPlayerPanel().setNormalModeWithTimer(true);
                        GameFrame.get().getPlayerPanel().setInitialTimerSecondsNormalMode(timer);
                    }
                }
                if (tokens.length == 3) {
                    String mode = tokens[0];
                    int blueTimer;
                    int redTimer;
                    try {
                        blueTimer = Integer.parseInt(tokens[1]);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(MainMenuFrame.get(),
                                "The file is either corrupted or invalid.",
                                "File Load Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    try {
                        redTimer = Integer.parseInt(tokens[2]);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(MainMenuFrame.get(),
                                "The file is either corrupted or invalid.",
                                "File Load Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (!mode.equals("blitz") || blueTimer < 0 || blueTimer > 900 || redTimer < 0 || redTimer > 900) {
                        JOptionPane.showMessageDialog(MainMenuFrame.get(),
                                "The file is either corrupted or invalid.",
                                "File Load Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    GameFrame.get().setBlitzMode(true);
                    MenuBar.blitzModeCheckBoxMenuItem.setSelected(true);
                    GameFrame.get().getPlayerPanel().setBlueCurrentTimerSecondsBlitzMode(blueTimer);
                    GameFrame.get().getPlayerPanel().setRedCurrentTimerSecondsBlitzMode(redTimer);
                }
            }
            if (index == 10) {
                if (line == null) {
                    break;
                } else if (line.equals("resign")) {
                    GameFrame.get().setGameResigned(true);
                } else {
                    JOptionPane.showMessageDialog(MainMenuFrame.get(),
                            "The file is either corrupted or invalid.",
                            "File Load Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                break;
            }
            String[] tokens = line.split("\\s+");
            for (String token : tokens) {
                if (token.length() == 2 && Character.isLetter(token.charAt(0)) && Character.isLetter(token.charAt(1))) {
                    animalList.add(token);
                    coordinateList.add(position);
                }
                position++;
            }
            index++;
        }

        Board expectedBoard = Board.constructSpecificBoard(animalList, coordinateList, nextPlayer);
        System.out.println(loadedBoard);
        System.out.println(expectedBoard);
        if (!expectedBoard.equals(loadedBoard)) {
            System.out.println("Board is incorrect");
            JOptionPane.showMessageDialog(MainMenuFrame.get(),
                    "The file is either corrupted or invalid.",
                    "File Load Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            if (BoardUtils.isGameOverScenario(GameFrame.get().getChessBoard())) {
                firstReplay = true;
                if (!GameFrame.get().isBlitzMode()
                        && GameFrame.get().getPlayerPanel().isNormalModeWithTimer()) {
                    GameFrame.get().getPlayerPanel().initTimerForNormalMode();
                    GameFrame.get().getPlayerPanel().setStopTimerInNormalMode(false);
                    GameFrame.get().getPlayerPanel().setCurrentTimerSecondsNormalMode(GameFrame.get().getPlayerPanel().getInitialTimerSecondsNormalMode());
                    GameFrame.get().getPlayerPanel().getTimerNormalMode().stop();
                } else if (!GameFrame.get().isBlitzMode()
                        && !GameFrame.get().getPlayerPanel().isNormalModeWithTimer()) {
                    GameFrame.get().getPlayerPanel().setStopTimerInNormalMode(true);
                } else if (GameFrame.get().isBlitzMode()) {
                    GameFrame.get().getPlayerPanel().initTimerForBlueBlitzMode();
                    GameFrame.get().getPlayerPanel().initTimerForRedBlitzMode();
                    GameFrame.get().getPlayerPanel().getBlueTimerBlitzMode().stop();
                    GameFrame.get().getPlayerPanel().getRedTimerBlitzMode().stop();
                }
                ProgressFrame progressFrame = new ProgressFrame("Replay");
                progressFrame.addProgressListener(() -> {
                    MainMenuFrame.get().setVisible(false);
                    GameFrame.get().setVisible(true);
                    loadReplay();
                });
            } else {
                System.out.println("Board is correct");
                ProgressFrame progressFrame = new ProgressFrame("Loading");
                progressFrame.addProgressListener(() -> {
                    MainMenuFrame.get().setVisible(false);
                    GameFrame.get().setVisible(true);
                    if (!GameFrame.get().isBlitzMode()
                            && GameFrame.get().getPlayerPanel().isNormalModeWithTimer()) {
                        GameFrame.get().getPlayerPanel().initTimerForNormalMode();
                        GameFrame.get().getPlayerPanel().setStopTimerInNormalMode(false);
                        GameFrame.get().getPlayerPanel().setCurrentTimerSecondsNormalMode(GameFrame.get().getPlayerPanel().getInitialTimerSecondsNormalMode());
                        GameFrame.get().getPlayerPanel().getTimerNormalMode().start();
                    } else if (!GameFrame.get().isBlitzMode()
                            && !GameFrame.get().getPlayerPanel().isNormalModeWithTimer()) {
                        GameFrame.get().getPlayerPanel().setStopTimerInNormalMode(true);
                    } else if (GameFrame.get().isBlitzMode()) {
                        GameFrame.get().getPlayerPanel().initTimerForBlueBlitzMode();
                        GameFrame.get().getPlayerPanel().initTimerForRedBlitzMode();
                        GameFrame.get().getPlayerPanel().getBlueTimerBlitzMode().start();
                        GameFrame.get().getPlayerPanel().getRedTimerBlitzMode().start();
                    }
                    if (!MainMenuFrame.get().isGrayScaleBGMButton()) {
                        AudioPlayer.LoopPlayer.playGameBGM();
                    }
                    System.out.println("Load a Saved Game");
                });
            }
        }
    }

    public static void loadReplay() {
        System.out.println("Replay loaded");
        if (firstReplay) {
            if (!MainMenuFrame.get().isGrayScaleBGMButton()) {
                AudioPlayer.LoopPlayer.playGameBGM();
            }
        }
        firstReplay = false;
        GameFrame.get().setReplayMovesInProgress(true);
        GameFrame.get().getPlayerPanel().reset();
        GameFrame.get().getCapturedPiecesPanel().reset();
        final MoveLog originalMoveLog = GameFrame.get().getMoveLog();
        MoveLog seperateMoveLog = new MoveLog();
        final int[] choice = new int[1];
        choice[0] = 2;
        final int[] index = {-1};
        GameFrame.get().setChessBoard(Board.constructStandardBoard());
        GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
        GameFrame.get().getBoardPanel().removeAllBorders();
        GameFrame.get().getPlayerPanel().reset();
        InputMap inputMap = GameFrame.get().getBoardPanel().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = GameFrame.get().getBoardPanel().getActionMap();
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "moveForward");
        actionMap.put("moveForward", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (index[0] < originalMoveLog.size() - 1) {
                    System.out.println("Next move:");
                    index[0]++;
                    final Move move = originalMoveLog.getMove(index[0]);
                    GameFrame.get().setChessBoard(move.execute());
                    System.out.println(move);
                    seperateMoveLog.addMove(move);
                    GameFrame.get().setLastMove(move);
                    AudioPlayer.SinglePlayer.playSoundEffect("click.wav");
                    GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
                    GameFrame.get().getPlayerPanel().redo(GameFrame.get().getChessBoard());
                    GameFrame.get().getCapturedPiecesPanel().redo(seperateMoveLog);
                    if (index[0] == originalMoveLog.size() - 1) {
                        GameFrame.get().setLastMove(null);
                        if (!GameFrame.get().isBlitzMode()
                                && GameFrame.get().getPlayerPanel().isNormalModeWithTimer()) {
                            GameFrame.get().getPlayerPanel().getTimerNormalMode().stop();
                        } else if (GameFrame.get().isBlitzMode()) {
                            GameFrame.get().getPlayerPanel().getBlueTimerBlitzMode().stop();
                            GameFrame.get().getPlayerPanel().getRedTimerBlitzMode().stop();
                        }
                        GameFrame.get().setReplayMovesInProgress(false);
                        String[] options = {"Rewatch", "New Game"};
                        choice[0] = JOptionPane.showOptionDialog(GameFrame.get().getBoardPanel(),
                                "Replay has ended.",
                                "Replay",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.INFORMATION_MESSAGE,
                                null,
                                options,
                                options[0]);
                        if (choice[0] == 0) {
                            loadReplay();
                        } else if (choice[0] == 1 || choice[0] == JOptionPane.CLOSED_OPTION) {
                            restartGameWithAnimation();
                            if (!GameFrame.get().isBlitzMode()
                                    && GameFrame.get().getPlayerPanel().isNormalModeWithTimer()) {
                                GameFrame.get().getPlayerPanel().getTimerNormalMode().start();
                            } else if (GameFrame.get().isBlitzMode()) {
                                GameFrame.get().getPlayerPanel().getBlueTimerBlitzMode().start();
                                GameFrame.get().getPlayerPanel().getRedTimerBlitzMode().start();
                            }
                        }
                    }
                }
            }
        });
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "moveBackward");
        actionMap.put("moveBackward", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (seperateMoveLog.size() > 1) {
                    index[0]--;
                    final Move lastMove = seperateMoveLog.removeMove(seperateMoveLog.size() - 1);
                    GameFrame.get().setLastMove(seperateMoveLog.getMove(seperateMoveLog.size() - 1));
                    GameFrame.get().setChessBoard(GameFrame.get().getChessBoard().getCurrentPlayer().unmakeMove(lastMove).getToBoard());
                    GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
                    GameFrame.get().getPlayerPanel().undo();
                    GameFrame.get().getCapturedPiecesPanel().redo(seperateMoveLog);
                    System.out.println("Undo move");
                } else if (seperateMoveLog.size() == 1) {
                    index[0] = -1;
                    GameFrame.get().setLastMove(null);
                    GameFrame.get().setChessBoard(Board.constructStandardBoard());
                    GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
                    System.out.println("Undo move");
                }
            }
        });
    }

    public static void updateGameSetting(int themeIndex) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("database/setting.txt"))) {
            if (MainMenuFrame.get().isGrayScaleSoundEffectButton()) {
                writer.write("off");
            } else {
                writer.write("on");
            }
            writer.newLine();
            if (MainMenuFrame.get().isGrayScaleBGMButton()) {
                writer.write("off");
            } else {
                writer.write("on");
            }
            writer.newLine();
            writer.write(String.valueOf(themeIndex));
        } catch (IOException e) {
            System.out.println("Setting file not found");
        }
    }

    public static void updateGameSetting() {
        String[] setting = new String[3];
        int themeIndex = 13;
        try (BufferedReader reader = new BufferedReader(new FileReader("database/setting.txt"))) {
            for (int i = 0; i < 3; i++) {
                setting[i] = reader.readLine();
            }
        } catch (IOException ignored) {
            System.out.println("Setting file not found");
        }
        try {
            if (setting[2] == null) {
                setUITheme(13);
            }
            assert setting[2] != null;
            try {
                themeIndex = Integer.parseInt(setting[2]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid theme index: " + setting[2]);
            }
            if (themeIndex < 0 || themeIndex > 30) {
                setUITheme(13);
            }
            setUITheme(themeIndex);
        } catch (NumberFormatException e) {
            System.out.println("Invalid theme index: " + setting[2]);
        } catch (NullPointerException e) {
            System.out.println("Look and feel not found");
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("database/setting.txt"))) {
            if (MainMenuFrame.get().isGrayScaleSoundEffectButton()) {
                writer.write("off");
            } else {
                writer.write("on");
            }
            writer.newLine();
            if (MainMenuFrame.get().isGrayScaleBGMButton()) {
                writer.write("off");
            } else {
                writer.write("on");
            }
            writer.newLine();
            writer.write(String.valueOf(themeIndex));
        } catch (IOException e) {
            System.out.println("Setting file not found");
        }
    }

    public static void loadGameSetting() {
        String[] setting = new String[3];
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.intellijthemes.FlatGradiantoNatureGreenIJTheme");
            MenuBar.gradiantoNatureGreenRadioButtonMenuItem.setSelected(true);
        } catch (Exception ignored) {
        }
        try (BufferedReader reader = new BufferedReader(new FileReader("database/setting.txt"))) {
            for (int i = 0; i < 3; i++) {
                setting[i] = reader.readLine();
            }
        } catch (IOException ignored) {
            System.out.println("Setting file not found");
        }
        try {
            if (setting[0].equals("on")) {
                MainMenuFrame.get().setGrayScaleSoundEffectButton(false);
                MainMenuFrame.get().soundEffectButton.setIcon(MainMenuFrame.get().soundEffectIcon);
                MenuBar.soundEffectAudioControlMenuItem.setSelected(true);
                Controller.updateGameSetting();
                System.out.println("Sound Effect is on");
            } else if (setting[0].equals("off")) {
                MainMenuFrame.get().setGrayScaleSoundEffectButton(true);
                MainMenuFrame.get().soundEffectButton.setIcon(MainMenuFrame.get().graySoundEffectIcon);
                MenuBar.soundEffectAudioControlMenuItem.setSelected(false);
                Controller.updateGameSetting();
                System.out.println("Sound Effect is off");
            }
            if (setting[1].equals("on")) {
                MainMenuFrame.get().setGrayScaleBGMButton(false);
                MainMenuFrame.get().bgmButton.setIcon(MainMenuFrame.get().bgmIcon);
                MenuBar.bgmAudioControlMenuItem.setSelected(true);
                AudioPlayer.LoopPlayer.playMenuBGM();
                Controller.updateGameSetting();
                System.out.println("BGM is on");
            } else if (setting[1].equals("off")) {
                MainMenuFrame.get().setGrayScaleBGMButton(true);
                MainMenuFrame.get().bgmButton.setIcon(MainMenuFrame.get().grayBGMIcon);
                MenuBar.bgmAudioControlMenuItem.setSelected(false);
                Controller.updateGameSetting();
                System.out.println("BGM is off");
            }
        } catch (NullPointerException ignored) {
            MainMenuFrame.get().setGrayScaleSoundEffectButton(false);
            MainMenuFrame.get().soundEffectButton.setIcon(MainMenuFrame.get().soundEffectIcon);
            MenuBar.soundEffectAudioControlMenuItem.setSelected(true);
            Controller.updateGameSetting();
            System.out.println("Sound Effect is on");
            MainMenuFrame.get().setGrayScaleBGMButton(false);
            MainMenuFrame.get().bgmButton.setIcon(MainMenuFrame.get().bgmIcon);
            MenuBar.bgmAudioControlMenuItem.setSelected(true);
            AudioPlayer.LoopPlayer.playMenuBGM();
            Controller.updateGameSetting();
            System.out.println("BGM is on");
        }
        try {
            if (setting[2] == null) {
                setUITheme(13);
            }
            assert setting[2] != null;
            int themeIndex = 13;
            try {
                themeIndex = Integer.parseInt(setting[2]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid theme index: " + setting[2]);
            }
            if (themeIndex < 0 || themeIndex > 30) {
                setUITheme(13);
            }
            setUITheme(themeIndex);
        } catch (NumberFormatException e) {
            System.out.println("Invalid theme index: " + setting[2]);
        } catch (NullPointerException e) {
            System.out.println("Look and feel not found");
        }
    }

    public static void setUITheme(int themeIndex) {
        try {
            Themes theme = Themes.values()[themeIndex];
            UIManager.setLookAndFeel(theme.getThemeClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.updateComponentTreeUI(GameFrame.get().gameFrame);
        SwingUtilities.updateComponentTreeUI(MainMenuFrame.get());
        SwingUtilities.updateComponentTreeUI(DifficultyFrame.get());
        SwingUtilities.updateComponentTreeUI(GameFrame.get().getGameConfiguration());
        switch (themeIndex) {
            case 0 -> MenuBar.arcDarkRadioButtonMenuItem.setSelected(true);
            case 1 -> MenuBar.arcDarkOrangeRadioButtonMenuItem.setSelected(true);
            case 2 -> MenuBar.arcRadioButtonMenuItem.setSelected(true);
            case 3 -> MenuBar.arcOrangeRadioButtonMenuItem.setSelected(true);
            case 4 -> MenuBar.carbonRadioButtonMenuItem.setSelected(true);
            case 5 -> MenuBar.cobalt2RadioButtonMenuItem.setSelected(true);
            case 6 -> MenuBar.cyanLightRadioButtonMenuItem.setSelected(true);
            case 7 -> MenuBar.darkFlatRadioButtonMenuItem.setSelected(true);
            case 8 -> MenuBar.darkPurpleRadioButtonMenuItem.setSelected(true);
            case 9 -> MenuBar.draculaRadioButtonMenuItem.setSelected(true);
            case 10 -> MenuBar.gradiantoDarkFuchsiaRadioButtonMenuItem.setSelected(true);
            case 11 -> MenuBar.gradiantoDeepOceanRadioButtonMenuItem.setSelected(true);
            case 12 -> MenuBar.gradiantoMidnightBlueRadioButtonMenuItem.setSelected(true);
            case 13 -> MenuBar.gradiantoNatureGreenRadioButtonMenuItem.setSelected(true);
            case 14 -> MenuBar.grayRadioButtonMenuItem.setSelected(true);
            case 15 -> MenuBar.gruvboxDarkHardRadioButtonMenuItem.setSelected(true);
            case 16 -> MenuBar.gruvboxDarkMediumRadioButtonMenuItem.setSelected(true);
            case 17 -> MenuBar.gruvboxDarkSoftRadioButtonMenuItem.setSelected(true);
            case 18 -> MenuBar.hiberbeeDarkRadioButtonMenuItem.setSelected(true);
            case 19 -> MenuBar.highContrastRadioButtonMenuItem.setSelected(true);
            case 20 -> MenuBar.lightFlatRadioButtonMenuItem.setSelected(true);
            case 21 -> MenuBar.materialDesignDarkRadioButtonMenuItem.setSelected(true);
            case 22 -> MenuBar.monokaiRadioButtonMenuItem.setSelected(true);
            case 23 -> MenuBar.monokaiProRadioButtonMenuItem.setSelected(true);
            case 24 -> MenuBar.nordRadioButtonMenuItem.setSelected(true);
            case 25 -> MenuBar.oneDarkRadioButtonMenuItem.setSelected(true);
            case 26 -> MenuBar.solarizedDarkRadioButtonMenuItem.setSelected(true);
            case 27 -> MenuBar.solarizedLightRadioButtonMenuItem.setSelected(true);
            case 28 -> MenuBar.spacegrayRadioButtonMenuItem.setSelected(true);
            case 29 -> MenuBar.vuesionRadioButtonMenuItem.setSelected(true);
            case 30 -> MenuBar.xcodeDarkRadioButtonMenuItem.setSelected(true);
        }
        updateGameSetting(themeIndex);
        System.out.println("Theme: " + Themes.values()[themeIndex]);
    }

    public static void showInstructionsDialog() {
        final JDialog dialog = new JDialog(GameFrame.get().gameFrame, "Instructions", false);
        dialog.setLayout(new BorderLayout());

        final JEditorPane instructionsPane = new JEditorPane();
        instructionsPane.setContentType("text/html");
        instructionsPane.setEditable(false);
        instructionsPane.setPreferredSize(new Dimension(600, 400));

        final String htmlContent = """
                <html>
                <body>
                <h2>Controls:</h2>
                <p>
                • Left click on a piece to select it.<br>
                • Left click on a highlighted square to move the piece.<br>
                • Left click on any non-highlighted square or left click on the same piece again to deselect it.<br>
                • If playing against bot, wait a moment for A.I. to make its move.
                </p>
                <br>
                        
                <h2>Gameplay:</h2>
                <h3>Pieces</h3>
                <p>
                Each player owns 8 animal pieces representing different animals of various ranks, and higher ranked animals can capture the animals of lower or equal rank. But there is a special case that elephant cannot capture the rat while the rat can capture the elephant.
                </p>
                <br>
                <table border='1' cellpadding='5'>
                <tr>
                    <th>Rank</th>
                    <th>Piece</th>
                    <th>Special Movement</th>
                </tr>
                <tr>
                    <td>1</td>
                    <td>Rat (鼠, shǔ)</td>
                    <td>Entering the river</td>
                </tr>
                <tr>
                    <td>2</td>
                    <td>Cat (猫, māo)</td>
                    <td>None</td>
                </tr>
                <tr>
                    <td>3</td>
                    <td>Dog (狗, gǒu)</td>
                    <td>None</td>
                </tr>
                <tr>
                    <td>4</td>
                    <td>Wolf (狼, láng)</td>
                    <td>None</td>
                </tr>
                <tr>
                    <td>5</td>
                    <td>Leopard (豹, bào)</td>
                    <td>None</td>
                </tr>
                <tr>
                    <td>6</td>
                    <td>Tiger (虎, hǔ)</td>
                    <td>Jumping across the river</td>
                </tr>
                <tr>
                    <td>7</td>
                    <td>Lion (狮, shī))</td>
                    <td>Jumping across the river</td>
                </tr>
                <tr>
                    <td>8</td>
                    <td>Elephant (象, xiàng)</td>
                    <td>None</td>
                </tr>
                </table>
                <br>

                <h3>Chessboard</h3>
                <p>
                Jungle game has an interesting chessboard with different terrains including dens, traps and rivers.<br>
                <br>
                The three kinds of special terrains are explained as:<br>          
                   • Dens (兽穴): It is not allowed that a piece enters its own den.<br>
                   • Trap (陷阱): If a piece entering the opponent's trap, then its rank is reduced into 0 temporarily before exiting. The trapped piece could be captured by any pieces of the defensing side.<br>
                   • River (河流): They are located in the center of the chessboard, each comprising 6 squares in a 3×2 rectangle. Only rats could enter the river, and lions and tigers could jump across the river.<br>
                </p>
                <br>

                <h3>Rules</h3>
                <p>
                1. Game Initialization: At the beginning, all 16 pieces are put into the chessboard as the initial state.<br>
                <br>
                2. Game Progress: The player with blue color moves first. Two players take the turn alternatively until the game is finished. When a player takes turn, he/she can select one of his pieces and do one of the following moves:<br>
                   • Moving one square horizontally or vertically. For lion, tiger and rat, they also have special movements related to the river squares, which have been introduced previously.<br>
                   • Capturing an opposing piece. In all captures, the captured pieces is removed from the board and the square is occupied by the attacking piece. A piece can capture any enemy piece following the rules introduced in "Pieces".<br>
                <br>
                3. Game Termination:<br>
                   • A player loses the game if one of the following conditions is met:<br>
                      ◦ The den is entered by his/her opponent.<br>
                      ◦ All of his/her pieces are captured.<br>
                      ◦ The player runs out of valid moves in his/her turn.<br>
                      ◦ His or her timer hits 0 first during blitz mode<br>
                      ◦ A player clicks the resign button during their turn for Human-Human mode. In Human-AI mode, the human player loses whenever the resign button is clicked.<br>
                   • A game is considered a draw if none of winning conditions is met after 150 rounds. In other words, each player has played up to 150 moves each and there is still no winner after 300 moves in total.<br>
                   <br>
                   Note for threefold repetition: If a player moves the same piece to the same board position for more than three times consecutively, any valid moves of that same piece to the repeated position will be banned temporarily until the player moves any piece to a new valid position.<br>
                   <br>
                </p>
                        
                </body>
                </html>
                """;
        instructionsPane.setText(htmlContent);

        final JScrollPane scrollPane = new JScrollPane(instructionsPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        dialog.add(scrollPane, BorderLayout.CENTER);

        dialog.setSize(500, 700);
        dialog.setLocationRelativeTo(GameFrame.get().getBoardPanel());
        dialog.setVisible(true);
    }

    public static void backToMainMenu() {
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
        if (BoardUtils.isGameOverScenario(GameFrame.get().getChessBoard())) {
            return;
        }
        GameFrame.get().getGameConfiguration().setBluePlayerType(PlayerType.HUMAN);
        GameFrame.get().getGameConfiguration().setRedPlayerType(PlayerType.HUMAN);
        GameFrame.get().restartGame();
        GameFrame.get().dispose();
        MainMenuFrame.get().setVisible(true);
        if (!MainMenuFrame.get().isGrayScaleBGMButton()) {
            AudioPlayer.LoopPlayer.playMenuBGM();
        }
        GameFrame.get().getPlayerPanel().setStopTimerInNormalMode(true);
        if (!GameFrame.get().isBlitzMode() && GameFrame.get().getPlayerPanel().isNormalModeWithTimer()) {
            GameFrame.get().getPlayerPanel().getTimerNormalMode().stop();
        }
        if (GameFrame.get().isBlitzMode()) {
            GameFrame.get().getPlayerPanel().getBlueTimerBlitzMode().stop();
            GameFrame.get().getPlayerPanel().getRedTimerBlitzMode().stop();
        }
        System.out.println("Back To Main Menu");
    }

    public static void exitGame() {
        GameFrame.get().dispose();
        System.out.println("Game Exited");
        System.exit(0);
    }

    public static void restartGame() {
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
        if (BoardUtils.isGameOverScenario(GameFrame.get().getChessBoard())) {
            return;
        }
        if (GameFrame.get().getMoveLog().size() == 0) {
            GameFrame.get().restartGame();
            System.out.println("Game Restarted");
            return;
        }
        restartGameWithAnimation();
    }

    public static void restartGameWithAnimation() {
        GameFrame.get().setAnimationInProgress(true);
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                for (int i = 0; i < BoardUtils.NUM_TERRAINS; i++) {
                    GameFrame.get().getBoardPanel().getBoardTerrains().get(i).deselectLeftMouseButton();
                }
                GameFrame.get().setLastMove(null);
                GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
                coloringTerrainsAnimationThread();
                return null;
            }

            @Override
            protected void done() {
                GameFrame.get().restartGame();
                GameFrame.get().setAnimationInProgress(false);
                System.out.println("Game Restarted");
            }
        };
        worker.execute();
    }

    public static void undoMove() {
        if (GameFrame.get().isBlitzMode()) {
            JOptionPane.showMessageDialog(GameFrame.get().getBoardPanel(),
                    "Undo is not allowed in Blitz Mode!\nIf you blunder, that is your bad. Cheers!");
            return;
        }
        if (GameFrame.get().getMoveLog().size() == 0) {
            JOptionPane.showMessageDialog(GameFrame.get().getBoardPanel(),
                    "No moves to undo.");
            return;
        }
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
        if (BoardUtils.isGameOverScenario(GameFrame.get().getChessBoard())) {
            return;
        }
        for (int i = 0; i < BoardUtils.NUM_TERRAINS; i++) {
            GameFrame.get().getBoardPanel().getBoardTerrains().get(i).deselectLeftMouseButton();
        }
        if (GameFrame.get().getGameConfiguration().getBluePlayerType() == PlayerType.HUMAN && GameFrame.get().getGameConfiguration().getRedPlayerType() == PlayerType.HUMAN) {
            if (GameFrame.get().getMoveLog().size() > 1) {
                final Move lastMove = GameFrame.get().getMoveLog().removeMove(GameFrame.get().getMoveLog().size() - 1);
                if (GameFrame.get().getChessBoard().getCurrentPlayer().getAllyColor().isBlue()) {
                    GameFrame.get().getBlueMoveLog().removeMove(GameFrame.get().getBlueMoveLog().size() - 1);
                } else if (GameFrame.get().getChessBoard().getCurrentPlayer().getAllyColor().isRed()) {
                    GameFrame.get().getRedMoveLog().removeMove(GameFrame.get().getRedMoveLog().size() - 1);
                }
                GameFrame.get().setLastMove(GameFrame.get().getMoveLog().getMove(GameFrame.get().getMoveLog().size() - 1));
                GameFrame.get().setChessBoard(GameFrame.get().getChessBoard().getCurrentPlayer().unmakeMove(lastMove).getToBoard());
                GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
                GameFrame.get().getPlayerPanel().undo();
                GameFrame.get().getCapturedPiecesPanel().redo(GameFrame.get().getMoveLog());
            } else if (GameFrame.get().getMoveLog().size() == 1) {
                GameFrame.get().setLastMove(null);
                GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
                GameFrame.get().restartGame();
            }
            System.out.println("Undo");
        } else {
            if (GameFrame.get().getGameConfiguration().getRedPlayerType() == PlayerType.AI
                    && GameFrame.get().getMoveLog().size() == 2) {
                GameFrame.get().setLastMove(null);
                GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
                GameFrame.get().restartGame();
                System.out.println("Undo");
                return;
            }
            if (GameFrame.get().getMoveLog().size() > 1) {
                final Move lastMove = GameFrame.get().getMoveLog().removeMove(GameFrame.get().getMoveLog().size() - 1);
                if (GameFrame.get().getChessBoard().getCurrentPlayer().getAllyColor().isBlue()) {
                    GameFrame.get().getBlueMoveLog().removeMove(GameFrame.get().getBlueMoveLog().size() - 1);
                } else if (GameFrame.get().getChessBoard().getCurrentPlayer().getAllyColor().isRed()) {
                    GameFrame.get().getRedMoveLog().removeMove(GameFrame.get().getRedMoveLog().size() - 1);
                }
                GameFrame.get().setChessBoard(GameFrame.get().getChessBoard().getCurrentPlayer().unmakeMove(lastMove).getToBoard());
                if (lastMove.equals(GameFrame.get().getComputerMove())) {
                    final Move secondLastMove = GameFrame.get().getMoveLog().removeMove(GameFrame.get().getMoveLog().size() - 1);
                    if (GameFrame.get().getChessBoard().getCurrentPlayer().getAllyColor().isBlue()) {
                        GameFrame.get().getRedMoveLog().removeMove(GameFrame.get().getRedMoveLog().size() - 1);
                    } else if (GameFrame.get().getChessBoard().getCurrentPlayer().getAllyColor().isRed()) {
                        GameFrame.get().getBlueMoveLog().removeMove(GameFrame.get().getBlueMoveLog().size() - 1);
                    }
                    GameFrame.get().setChessBoard(GameFrame.get().getChessBoard().getCurrentPlayer().unmakeMove(secondLastMove).getToBoard());
                    GameFrame.get().setLastMove(GameFrame.get().getMoveLog().getMove(GameFrame.get().getMoveLog().size() - 1));
                    GameFrame.get().setComputerMove(GameFrame.get().getMoveLog().getMove(GameFrame.get().getMoveLog().size() - 1));
                }
                GameFrame.get().getPlayerPanel().undo();
                GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
                GameFrame.get().getCapturedPiecesPanel().redo(GameFrame.get().getMoveLog());
            } else if (GameFrame.get().getMoveLog().size() == 1) {
                GameFrame.get().setLastMove(null);
                GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
                GameFrame.get().restartGame();
            }
            System.out.println("Undo");
        }
    }

    public static void replayMoves() {
        if (GameFrame.get().isReplayMovesInProgress()) {
            JOptionPane.showMessageDialog(GameFrame.get().getBoardPanel(),
                    "Replay is already in progress. Please wait for the next replay.");
            return;
        }
        if (GameFrame.get().getGameConfiguration().isAIPlayer(GameFrame.get().getChessBoard().getCurrentPlayer())) {
            JOptionPane.showMessageDialog(GameFrame.get().getBoardPanel(),
                    "AI is still thinking. Please wait.");
            return;
        }
        if (GameFrame.get().getMoveLog().size() == 0) {
            JOptionPane.showMessageDialog(GameFrame.get().getBoardPanel(),
                    "No moves to replay.");
            return;
        }
        if (BoardUtils.isGameOverScenario(GameFrame.get().getChessBoard())) {
            return;
        }
        GameFrame.get().setReplayMovesInProgress(true);
        GameFrame.get().setLastMove(null);
        GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
        GameFrame.get().getPlayerPanel().reset();
        GameFrame.get().getCapturedPiecesPanel().reset();
        MoveLog seperateMoveLog = new MoveLog();
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                for (int i = 0; i < BoardUtils.NUM_TERRAINS; i++) {
                    GameFrame.get().getBoardPanel().getBoardTerrains().get(i).deselectLeftMouseButton();
                }
                GameFrame.get().setLastMove(null);
                GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
                coloringTerrainsAnimationThread();
                GameFrame.get().setChessBoard(Board.constructStandardBoard());
                GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
                GameFrame.get().getBoardPanel().removeAllBorders();
                GameFrame.get().getPlayerPanel().reset();
                Thread.sleep(1000);
                for (int i = 0; i < GameFrame.get().getMoveLog().size(); i++) {
                    final Move move = GameFrame.get().getMoveLog().getMove(i);
                    GameFrame.get().setChessBoard(move.execute());
                    System.out.println(move);
                    seperateMoveLog.addMove(move);
                    GameFrame.get().setLastMove(move);
                    publish();
                    Thread.sleep(1000);
                }
                return null;
            }

            @Override
            protected void process(List<Void> chunks) {
                AudioPlayer.SinglePlayer.playSoundEffect("click.wav");
                GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
                GameFrame.get().getPlayerPanel().redo(GameFrame.get().getChessBoard());
                GameFrame.get().getCapturedPiecesPanel().redo(seperateMoveLog);
            }

            @Override
            protected void done() {
                if (GameFrame.get().getGameConfiguration().isAIPlayer(GameFrame.get().getChessBoard().getCurrentPlayer())) {
                    GameFrame.get().moveMadeUpdate(PlayerType.HUMAN);
                }
                GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
                GameFrame.get().setReplayMovesInProgress(false);
                if (GameFrame.get().isBlitzMode()) {
                    GameFrame.get().getPlayerPanel().getBlueTimerBlitzMode().start();
                    GameFrame.get().getPlayerPanel().getRedTimerBlitzMode().start();
                }
            }
        };
        worker.execute();
        System.out.println("Replay Previous Moves");
    }

    public static void changeBoard() {
        GameFrame.get().setBoard1(!GameFrame.get().isBoard1());
        String boardImageFileName = GameFrame.get().isBoard1() ? "chessboard1.png" : "chessboard2.png";
        GameFrame.get().getBoardPanel().setBoardImage(boardImageFileName);
        GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
        System.out.println("Board Changed");
    }

    public static void switchSide() {
        if (GameFrame.get().boardDirection == BoardDirection.FLIPPED) {
            GameFrame.get().setReversedBlueSide(false);
            GameFrame.get().setReversedRedSide(true);
        } else if (GameFrame.get().boardDirection == BoardDirection.NORMAL) {
            GameFrame.get().setReversedBlueSide(true);
            GameFrame.get().setReversedRedSide(false);
        }
        GameFrame.get().boardDirection = GameFrame.get().boardDirection.opposite();
        GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
        System.out.println("Side Switched");
    }


    public static void flipPiecesIcon() {
        if (GameFrame.get().boardDirection == BoardDirection.NORMAL) {
            GameFrame.get().setReversedRedSide(!GameFrame.get().isReversedRedSide());
            GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
        } else if (GameFrame.get().boardDirection == BoardDirection.FLIPPED) {
            GameFrame.get().setReversedBlueSide(!GameFrame.get().isReversedBlueSide());
            GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
        }
        System.out.println("Piece Icons Flipped");
    }

    public static void setTimer() {
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
        if (BoardUtils.isGameOverScenario(GameFrame.get().getChessBoard())) {
            return;
        }
        if (!GameFrame.get().isBlitzMode()) {
            final JDialog dialog = new JDialog();
            dialog.setTitle("Set Timer");
            dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            dialog.setSize(280, 150);
            dialog.setLayout(new FlowLayout());
            dialog.setLocationRelativeTo(GameFrame.get().getBoardPanel());
            dialog.setIconImage(GameFrame.get().logo.getImage());
            dialog.setResizable(false);
            dialog.setModal(true);

            final JLabel valueLabel = new JLabel("Enter the timer value in seconds (15 - 100):");
            JTextField textField = new JTextField(16);
            final JButton setTimerButton = new JButton("Set Timer");
            final JButton removeTimer = new JButton("Remove Timer");
            final JLabel restartLabel = new JLabel("The game will restart once the timer is set.");

            dialog.add(valueLabel);
            dialog.add(restartLabel);
            dialog.add(textField);
            dialog.add(setTimerButton);
            dialog.add(removeTimer);
            dialog.add(restartLabel);

            setTimerButton.addActionListener(e -> {
                try {
                    int timerValue = Integer.parseInt(textField.getText());
                    if (timerValue >= 15 && timerValue <= 100) {
                        GameFrame.get().getPlayerPanel().setInitialTimerSecondsNormalMode(timerValue);
                        if (GameFrame.get().getMoveLog().size() == 0) {
                            GameFrame.get().restartGame();
                        } else if (GameFrame.get().getMoveLog().size() > 0) {
                            restartGameWithAnimation();
                        }
                        GameFrame.get().getPlayerPanel().setNormalModeWithTimer(true);
                        GameFrame.get().getPlayerPanel().getTimerNormalMode().start();
                        System.out.println("Timer value: " + timerValue + " seconds");
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(GameFrame.get().getBoardPanel(),
                                "Invalid input.\nPlease enter an integer value between 15 and 100.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(GameFrame.get().getBoardPanel(),
                            "Invalid input.\nPlease enter an integer value.");
                }
            });

            removeTimer.addActionListener(e -> {
                GameFrame.get().getPlayerPanel().setNormalModeWithTimer(false);
                if (GameFrame.get().getMoveLog().size() == 0) {
                    GameFrame.get().restartGame();
                } else if (GameFrame.get().getMoveLog().size() > 0) {
                    restartGameWithAnimation();
                }
                GameFrame.get().getPlayerPanel().getTimerNormalMode().stop();
                System.out.println("Timer removed");
                dialog.dispose();
            });

            dialog.setVisible(true);
        } else {
            final JDialog dialog = new JDialog();
            dialog.setTitle("Set Timer");
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setSize(380, 280);
            dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
            dialog.setLocationRelativeTo(GameFrame.get().getBoardPanel());
            dialog.setIconImage(GameFrame.get().logo.getImage());
            dialog.setResizable(false);
            dialog.setModal(true);

            final JPanel optionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            final JLabel optionLabel = new JLabel("Options:");
            optionPanel.add(optionLabel);

            final JRadioButton fiveMinutesButton = new JRadioButton("5 minutes");
            final JRadioButton tenMinutesButton = new JRadioButton("10 minutes");
            final JRadioButton fifteenMinutesButton = new JRadioButton("15 minutes");

            final ButtonGroup buttonGroup = new ButtonGroup();
            buttonGroup.add(fiveMinutesButton);
            buttonGroup.add(tenMinutesButton);
            buttonGroup.add(fifteenMinutesButton);

            final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            final JButton setTimerButton = new JButton("Set Timer");
            buttonPanel.add(setTimerButton);

            final JLabel restartLabel = new JLabel("The game will restart once the timer is set.");

            dialog.add(Box.createVerticalGlue());
            dialog.add(optionPanel);
            dialog.add(Box.createVerticalGlue());
            dialog.add(fiveMinutesButton);
            dialog.add(Box.createVerticalGlue());
            dialog.add(tenMinutesButton);
            dialog.add(Box.createVerticalGlue());
            dialog.add(fifteenMinutesButton);
            dialog.add(Box.createVerticalGlue());
            dialog.add(buttonPanel);
            dialog.add(Box.createVerticalGlue());
            dialog.add(restartLabel);
            dialog.add(Box.createVerticalGlue());

            if (GameFrame.get().getPlayerPanel().getInitialTimerSecondsBlitzMode() == 300) {
                fiveMinutesButton.setSelected(true);
            } else if (GameFrame.get().getPlayerPanel().getInitialTimerSecondsBlitzMode() == 600) {
                tenMinutesButton.setSelected(true);
            } else if (GameFrame.get().getPlayerPanel().getInitialTimerSecondsBlitzMode() == 900) {
                fifteenMinutesButton.setSelected(true);
            }

            setTimerButton.addActionListener(e -> {
                if (fiveMinutesButton.isSelected()) {
                    if (GameFrame.get().getMoveLog().size() == 0) {
                        GameFrame.get().restartGame();
                    } else if (GameFrame.get().getMoveLog().size() > 0) {
                        restartGameWithAnimation();
                    }
                    GameFrame.get().setBlitzMode(true);
                    GameFrame.get().setBlitzModeGameOver(false);
                    GameFrame.get().getPlayerPanel().getBlueTimerBlitzMode().stop();
                    GameFrame.get().getPlayerPanel().getRedTimerBlitzMode().stop();
                    GameFrame.get().getPlayerPanel().setInitialTimerSecondsBlitzMode(300);
                    GameFrame.get().getPlayerPanel().setBlueCurrentTimerSecondsBlitzMode(300);
                    GameFrame.get().getPlayerPanel().setRedCurrentTimerSecondsBlitzMode(300);
                    GameFrame.get().getPlayerPanel().initTimerForBlueBlitzMode();
                    GameFrame.get().getPlayerPanel().initTimerForRedBlitzMode();
                    if (!GameFrame.get().isAnimationInProgress()) {
                        GameFrame.get().getPlayerPanel().getBlueTimerBlitzMode().start();
                        GameFrame.get().getPlayerPanel().getRedTimerBlitzMode().start();
                    }
                    System.out.println("Timer value: 5 minutes");
                    dialog.dispose();
                } else if (tenMinutesButton.isSelected()) {
                    if (GameFrame.get().getMoveLog().size() == 0) {
                        GameFrame.get().restartGame();
                    } else if (GameFrame.get().getMoveLog().size() > 0) {
                        restartGameWithAnimation();
                    }
                    GameFrame.get().getPlayerPanel().getBlueTimerBlitzMode().stop();
                    GameFrame.get().getPlayerPanel().getRedTimerBlitzMode().stop();
                    GameFrame.get().setBlitzMode(true);
                    GameFrame.get().setBlitzModeGameOver(false);
                    GameFrame.get().getPlayerPanel().setInitialTimerSecondsBlitzMode(600);
                    GameFrame.get().getPlayerPanel().setBlueCurrentTimerSecondsBlitzMode(600);
                    GameFrame.get().getPlayerPanel().setRedCurrentTimerSecondsBlitzMode(600);
                    GameFrame.get().getPlayerPanel().initTimerForBlueBlitzMode();
                    GameFrame.get().getPlayerPanel().initTimerForRedBlitzMode();
                    if (!GameFrame.get().isAnimationInProgress()) {
                        GameFrame.get().getPlayerPanel().getBlueTimerBlitzMode().start();
                        GameFrame.get().getPlayerPanel().getRedTimerBlitzMode().start();
                    }
                    System.out.println("Timer value: 10 minutes");
                    dialog.dispose();
                } else if (fifteenMinutesButton.isSelected()) {
                    if (GameFrame.get().getMoveLog().size() == 0) {
                        GameFrame.get().restartGame();
                    } else if (GameFrame.get().getMoveLog().size() > 0) {
                        restartGameWithAnimation();
                    }
                    GameFrame.get().getPlayerPanel().getBlueTimerBlitzMode().stop();
                    GameFrame.get().getPlayerPanel().getRedTimerBlitzMode().stop();
                    GameFrame.get().setBlitzMode(true);
                    GameFrame.get().setBlitzModeGameOver(false);
                    GameFrame.get().getPlayerPanel().setInitialTimerSecondsBlitzMode(900);
                    GameFrame.get().getPlayerPanel().setBlueCurrentTimerSecondsBlitzMode(900);
                    GameFrame.get().getPlayerPanel().setRedCurrentTimerSecondsBlitzMode(900);
                    GameFrame.get().getPlayerPanel().initTimerForBlueBlitzMode();
                    GameFrame.get().getPlayerPanel().initTimerForRedBlitzMode();
                    if (!GameFrame.get().isAnimationInProgress()) {
                        GameFrame.get().getPlayerPanel().getBlueTimerBlitzMode().start();
                        GameFrame.get().getPlayerPanel().getRedTimerBlitzMode().start();
                    }
                    System.out.println("Timer value: 15 minutes");
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(GameFrame.get().getBoardPanel(),
                            "Please select an option.");
                }
            });

            dialog.setVisible(true);
        }
    }

    public static void handleWinningStateForDenPenetratedCondition() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws InterruptedException {
                for (int i = 0; i < BoardUtils.NUM_TERRAINS; i++) {
                    if (GameFrame.get().getChessBoard().getTerrain(i).isTerrainOccupied()
                            && GameFrame.get().getChessBoard().getTerrain(i).getPiece().getPieceColor() == GameFrame.get().getChessBoard().getCurrentPlayer().getAllyColor()) {
                        publish();
                        Thread.sleep(500);
                        GameFrame.get().getBoardPanel().getBoardTerrains().get(i).removeAll();
                        GameFrame.get().getBoardPanel().getBoardTerrains().get(i).revalidate();
                        GameFrame.get().getBoardPanel().getBoardTerrains().get(i).repaint();
                    }
                }
                return null;
            }

            @Override
            protected void process(List<Void> chunks) {
                AudioPlayer.SinglePlayer.playSoundEffect("popping.wav");
            }

            @Override
            protected void done() {
                if (GameFrame.get().getGameConfiguration().getBluePlayerType() == GameFrame.get().getGameConfiguration().getRedPlayerType()
                        || GameFrame.get().getGameConfiguration().isAIPlayer(GameFrame.get().getChessBoard().getCurrentPlayer())) {
                    AudioPlayer.SinglePlayer.playSoundEffect("winning.wav");
                } else {
                    AudioPlayer.SinglePlayer.playSoundEffect("losing.wav");
                }
                ImageIcon gameOverIcon = new ImageIcon(defaultImagesPath + "gameover.png");
                Image resizedImage = gameOverIcon.getImage().getScaledInstance(35, 35, Image.SCALE_DEFAULT);
                Icon resizedIcon = new ImageIcon(resizedImage);
                String[] options = {"Save Replay", "Restart"};
                int choice = JOptionPane.showOptionDialog(GameFrame.get().getBoardPanel(),
                        "Game Over: " + GameFrame.get().getChessBoard().getCurrentPlayer().getEnemyPlayer() + " Player wins!\n"
                                + GameFrame.get().getChessBoard().getCurrentPlayer() + " Player's den is penetrated by the enemy!",
                        "Game Over",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        resizedIcon,
                        options,
                        options[0]);
                System.out.println("Game Over: " + GameFrame.get().getChessBoard().getCurrentPlayer().getEnemyPlayer() + " Player wins!\n"
                        + GameFrame.get().getChessBoard().getCurrentPlayer() + " Player's den is penetrated by the enemy!");

                if (choice == 0) {
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedDateTime = currentDateTime.format(formatter);
                    String fileName = "Replay_" + formattedDateTime.replace(":", "-");
                    callFromSaveReplay = true;
                    writeGame(fileName);
                } else if (choice == 1 || choice == JOptionPane.CLOSED_OPTION) {
                    restartGameWithAnimation();
                    System.out.println("Game Restarted");
                }
            }
        };
        worker.execute();
    }

    public static void handleWinningStateForHavingNoMoreActivePiecesCondition() {
        String[] options = {"Save Replay", "Restart"};
        int choice = JOptionPane.showOptionDialog(GameFrame.get().getBoardPanel(),
                "Game Over: " + GameFrame.get().getChessBoard().getCurrentPlayer().getEnemyPlayer() + " Player wins!\n"
                        + GameFrame.get().getChessBoard().getCurrentPlayer() + " Player has no more pieces!",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                resizedGameOverIcon,
                options,
                options[0]);
        System.out.println("Game Over: " + GameFrame.get().getChessBoard().getCurrentPlayer().getEnemyPlayer() + " Player wins!\n"
                + GameFrame.get().getChessBoard().getCurrentPlayer() + " Player has no more pieces!");

        if (choice == 0) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = currentDateTime.format(formatter);
            String fileName = "Replay_" + formattedDateTime.replace(":", "-");
            callFromSaveReplay = true;
            writeGame(fileName);
        } else if (choice == 1 || choice == JOptionPane.CLOSED_OPTION) {
            restartGameWithAnimation();
            System.out.println("Game Restarted");
        }
    }

    public static void handleWinningStateForHavingNoMoreValidMovesConditions() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws InterruptedException {
                for (int i = 0; i < BoardUtils.NUM_TERRAINS; i++) {
                    if (GameFrame.get().getChessBoard().getTerrain(i).isTerrainOccupied()
                            && GameFrame.get().getChessBoard().getTerrain(i).getPiece().getPieceColor() == GameFrame.get().getChessBoard().getCurrentPlayer().getAllyColor()) {
                        publish();
                        Thread.sleep(500);
                        GameFrame.get().getBoardPanel().getBoardTerrains().get(i).removeAll();
                        GameFrame.get().getBoardPanel().getBoardTerrains().get(i).revalidate();
                        GameFrame.get().getBoardPanel().getBoardTerrains().get(i).repaint();
                    }
                }
                return null;
            }

            @Override
            protected void process(List<Void> chunks) {
                AudioPlayer.SinglePlayer.playSoundEffect("popping.wav");
            }

            @Override
            protected void done() {
                if (GameFrame.get().getGameConfiguration().getBluePlayerType() == GameFrame.get().getGameConfiguration().getRedPlayerType()
                        || GameFrame.get().getGameConfiguration().isAIPlayer(GameFrame.get().getChessBoard().getCurrentPlayer())) {
                    AudioPlayer.SinglePlayer.playSoundEffect("winning.wav");
                } else {
                    AudioPlayer.SinglePlayer.playSoundEffect("losing.wav");
                }
                String[] options = {"Save Replay", "Restart"};
                int choice = JOptionPane.showOptionDialog(GameFrame.get().getBoardPanel(),
                        "Game Over: " + GameFrame.get().getChessBoard().getCurrentPlayer().getEnemyPlayer() + " Player wins!\n"
                                + GameFrame.get().getChessBoard().getCurrentPlayer() + " Player has no more valid moves!",
                        "Game Over",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        resizedGameOverIcon,
                        options,
                        options[0]);
                System.out.println("Game Over: " + GameFrame.get().getChessBoard().getCurrentPlayer().getEnemyPlayer() + " Player wins!\n"
                        + GameFrame.get().getChessBoard().getCurrentPlayer() + " Player has no more valid moves!");

                if (choice == 0) {
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedDateTime = currentDateTime.format(formatter);
                    String fileName = "Replay_" + formattedDateTime.replace(":", "-");
                    callFromSaveReplay = true;
                    writeGame(fileName);
                } else if (choice == 1 || choice == JOptionPane.CLOSED_OPTION) {
                    restartGameWithAnimation();
                    System.out.println("Game Restarted");
                }
            }
        };
        worker.execute();
    }

    public static void handleWinningStateForBlitzModeCondition() {
        GameFrame.get().setBlitzModeGameOver(true);
        GameFrame.get().getPlayerPanel().getBlueTimerBlitzMode().stop();
        GameFrame.get().getPlayerPanel().getRedTimerBlitzMode().stop();
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws InterruptedException {
                for (int i = 0; i < BoardUtils.NUM_TERRAINS; i++) {
                    if (GameFrame.get().getChessBoard().getTerrain(i).isTerrainOccupied()
                            && GameFrame.get().getChessBoard().getTerrain(i).getPiece().getPieceColor() == GameFrame.get().getChessBoard().getCurrentPlayer().getAllyColor()) {
                        publish();
                        Thread.sleep(500);
                        GameFrame.get().getBoardPanel().getBoardTerrains().get(i).removeAll();
                        GameFrame.get().getBoardPanel().getBoardTerrains().get(i).revalidate();
                        GameFrame.get().getBoardPanel().getBoardTerrains().get(i).repaint();
                    }
                }
                return null;
            }

            @Override
            protected void process(List<Void> chunks) {
                AudioPlayer.SinglePlayer.playSoundEffect("popping.wav");
            }

            @Override
            protected void done() {
                if (GameFrame.get().getGameConfiguration().getBluePlayerType() == GameFrame.get().getGameConfiguration().getRedPlayerType()
                        || GameFrame.get().getGameConfiguration().isAIPlayer(GameFrame.get().getChessBoard().getCurrentPlayer())) {
                    AudioPlayer.SinglePlayer.playSoundEffect("winning.wav");
                } else {
                    AudioPlayer.SinglePlayer.playSoundEffect("losing.wav");
                }
                String[] options = {"Save Replay", "Restart"};
                int choice = JOptionPane.showOptionDialog(GameFrame.get().getBoardPanel(),
                        "Game Over: " + GameFrame.get().getChessBoard().getCurrentPlayer().getEnemyPlayer() + " Player wins!\n"
                                + GameFrame.get().getChessBoard().getCurrentPlayer() + " Player runs out of time in Blitz Mode!",
                        "Game Over",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        resizedGameOverIcon,
                        options,
                        options[0]);
                System.out.println("Game Over: " + GameFrame.get().getChessBoard().getCurrentPlayer().getEnemyPlayer() + " Player wins!\n"
                        + GameFrame.get().getChessBoard().getCurrentPlayer() + " Player runs out of time in Blitz Mode!");

                if (choice == 0) {
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedDateTime = currentDateTime.format(formatter);
                    String fileName = "Replay_" + formattedDateTime.replace(":", "-");
                    callFromSaveReplay = true;
                    writeGame(fileName);
                } else if (choice == 1 || choice == JOptionPane.CLOSED_OPTION) {
                    restartGameWithAnimation();
                    System.out.println("Game Restarted");
                }
            }
        };
        worker.execute();
    }

    public static void handleWinningStateForGameResignedCondition() {
        if (GameFrame.get().isReplayMovesInProgress()) {
            JOptionPane.showMessageDialog(GameFrame.get().getBoardPanel(),
                    "Replay is in progress. Please wait.");
            return;
        }
        if (BoardUtils.isGameOverScenario(GameFrame.get().getChessBoard())) {
            return;
        }
        GameFrame.get().setGameResigned(true);
        final String[] message = new String[1];
        final String[] soundPath = new String[1];
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws InterruptedException {
                if (GameFrame.get().getGameConfiguration().getBluePlayerType() == PlayerType.HUMAN
                        && GameFrame.get().getGameConfiguration().getRedPlayerType() == PlayerType.HUMAN) {
                    message[0] = "Game Over: " + GameFrame.get().getChessBoard().getCurrentPlayer().getEnemyPlayer() + " Player wins!\n"
                            + GameFrame.get().getChessBoard().getCurrentPlayer() + " Player has resigned!";
                    soundPath[0] = "winning.wav";
                    for (int i = 0; i < BoardUtils.NUM_TERRAINS; i++) {
                        if (GameFrame.get().getChessBoard().getTerrain(i).isTerrainOccupied()
                                && GameFrame.get().getChessBoard().getTerrain(i).getPiece().getPieceColor() == GameFrame.get().getChessBoard().getCurrentPlayer().getAllyColor()) {
                            publish();
                            Thread.sleep(500);
                            GameFrame.get().getBoardPanel().getBoardTerrains().get(i).removeAll();
                            GameFrame.get().getBoardPanel().getBoardTerrains().get(i).revalidate();
                            GameFrame.get().getBoardPanel().getBoardTerrains().get(i).repaint();
                        }
                    }
                }
                if (GameFrame.get().getGameConfiguration().getBluePlayerType() == PlayerType.HUMAN
                        && GameFrame.get().getGameConfiguration().getRedPlayerType() == PlayerType.AI) {
                    message[0] = "Game Over: Red Player wins!\n"
                            + "Blue Player has resigned!";
                    soundPath[0] = "losing.wav";
                    for (int i = 0; i < BoardUtils.NUM_TERRAINS; i++) {
                        if (GameFrame.get().getChessBoard().getTerrain(i).isTerrainOccupied()
                                && GameFrame.get().getChessBoard().getTerrain(i).getPiece().getPieceColor() == PlayerColor.BLUE) {
                            publish();
                            Thread.sleep(500);
                            GameFrame.get().getBoardPanel().getBoardTerrains().get(i).removeAll();
                            GameFrame.get().getBoardPanel().getBoardTerrains().get(i).revalidate();
                            GameFrame.get().getBoardPanel().getBoardTerrains().get(i).repaint();
                        }
                    }
                }
                if (GameFrame.get().getGameConfiguration().getBluePlayerType() == PlayerType.AI
                        && GameFrame.get().getGameConfiguration().getRedPlayerType() == PlayerType.HUMAN) {
                    message[0] = "Game Over: Blue Player wins!\n"
                            + "Red Player has resigned!";
                    soundPath[0] = "losing.wav";
                    for (int i = 0; i < BoardUtils.NUM_TERRAINS; i++) {
                        if (GameFrame.get().getChessBoard().getTerrain(i).isTerrainOccupied()
                                && GameFrame.get().getChessBoard().getTerrain(i).getPiece().getPieceColor() == PlayerColor.RED) {
                            publish();
                            Thread.sleep(500);
                            GameFrame.get().getBoardPanel().getBoardTerrains().get(i).removeAll();
                            GameFrame.get().getBoardPanel().getBoardTerrains().get(i).revalidate();
                            GameFrame.get().getBoardPanel().getBoardTerrains().get(i).repaint();
                        }
                    }
                }
                return null;
            }

            @Override
            protected void process(List<Void> chunks) {
                AudioPlayer.SinglePlayer.playSoundEffect("popping.wav");
            }

            @Override
            protected void done() {
                AudioPlayer.SinglePlayer.playSoundEffect(soundPath[0]);
                String[] options = {"Save Replay", "Restart"};
                int choice = JOptionPane.showOptionDialog(GameFrame.get().getBoardPanel(),
                        message[0],
                        "Game Over",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        resizedGameOverIcon,
                        options,
                        options[0]);
                System.out.println(message[0]);

                if (choice == 0) {
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedDateTime = currentDateTime.format(formatter);
                    String fileName = "Replay_" + formattedDateTime.replace(":", "-");
                    callFromSaveReplay = true;
                    writeGame(fileName);
                } else if (choice == 1 || choice == JOptionPane.CLOSED_OPTION) {
                    restartGameWithAnimation();
                    System.out.println("Game Restarted");
                }
            }
        };
        worker.execute();
    }

    public static void handleGameDrawnScenario() {
        AudioPlayer.SinglePlayer.playSoundEffect("losing.wav");
        String[] options = {"Save Replay", "Restart"};
        int choice = JOptionPane.showOptionDialog(GameFrame.get().getBoardPanel(),
                "Game Over: Game is considered a draw after 150 moves!",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                resizedGameOverIcon,
                options,
                options[0]);
        System.out.println("Game Over: Game is considered drawn after 150 moves!");

        if (choice == 0) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = currentDateTime.format(formatter);
            String fileName = "Replay_" + formattedDateTime.replace(":", "-");
            callFromSaveReplay = true;
            writeGame(fileName);
        } else if (choice == 1 || choice == JOptionPane.CLOSED_OPTION) {
            restartGameWithAnimation();
            System.out.println("Game Restarted");
        }
    }

    public static void coloringTerrainsAnimationThread() {
        List<Color> colorList = new ArrayList<>();
        for (int i = 0; i < BoardUtils.NUM_TERRAINS / 2 + 1; i++) {
            Random random = new Random();
            int red = random.nextInt(256);
            int green = random.nextInt(256);
            int blue = random.nextInt(256);
            Color randomColor = new Color(red, green, blue);
            colorList.add(randomColor);
        }
        List<Color> reversedColorList = new ArrayList<>(colorList);
        Collections.reverse(reversedColorList);
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < BoardUtils.NUM_TERRAINS / 2 + 1; i++) {
                GameFrame.get().getBoardPanel().getBoardTerrains().get(i).setOpaque(true);
                GameFrame.get().getBoardPanel().getBoardTerrains().get(i).setBackground(colorList.get(i));
                try {
                    Thread.sleep(30);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = BoardUtils.NUM_TERRAINS - 1, colorIndex = BoardUtils.NUM_TERRAINS / 2; i > BoardUtils.NUM_TERRAINS / 2; i--, colorIndex--) {
                GameFrame.get().getBoardPanel().getBoardTerrains().get(i).setOpaque(true);
                GameFrame.get().getBoardPanel().getBoardTerrains().get(i).setBackground(reversedColorList.get(colorIndex));
                try {
                    Thread.sleep(30);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
        thread1.start();
        thread2.start();
        try {
            Thread.sleep(1350);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public enum BoardDirection {
        NORMAL {
            @Override
            public List<GameFrame.TerrainPanel> traverse(final List<GameFrame.TerrainPanel> boardTiles) {
                return boardTiles;
            }

            @Override
            public BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED {
            @Override
            public List<GameFrame.TerrainPanel> traverse(final List<GameFrame.TerrainPanel> boardTiles) {
                List<GameFrame.TerrainPanel> reversedTiles = new ArrayList<>(boardTiles);
                Collections.reverse(reversedTiles);
                return reversedTiles;
            }

            @Override
            public BoardDirection opposite() {
                return NORMAL;
            }
        };

        public abstract List<GameFrame.TerrainPanel> traverse(final List<GameFrame.TerrainPanel> boardTiles);

        public abstract BoardDirection opposite();
    }
}
