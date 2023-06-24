package model;

import model.board.*;
import model.player.PlayerType;
import view.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class Controller {
    public static void saveGame() {
        if (GameFrame.get().isReplayMovesInProgress()) {
            JOptionPane.showMessageDialog(null, "Replay is in progress. Please wait.");
            return;
        }
        if (GameFrame.get().getGameConfiguration().isAIPlayer(GameFrame.get().getChessBoard().getCurrentPlayer())) {
            JOptionPane.showMessageDialog(null, "AI is still thinking. Please wait.");
            return;
        }
        String fileName = JOptionPane.showInputDialog("File Name");
        while (fileName.equals("")) {
            JOptionPane.showMessageDialog(null, "Name cannot be empty. Please enter again.");
            fileName = JOptionPane.showInputDialog("File Name");
        }
        writeGame(fileName);
    }

    private static void writeGame(String fileName) {
        String location = "database\\" + fileName + ".txt";
        if (!fileName.matches("[^\\\\/:*?\"<>|]+")) {
            JOptionPane.showMessageDialog(null, "A file name cannot contain any illegal characters.", "Invalid File Name", JOptionPane.ERROR_MESSAGE);
            return;
        }
        File file = new File(location);
        try {
            if (file.exists()) {
                int response = JOptionPane.showConfirmDialog(null, "The file already exists, do you want to overwrite it?",
                        "Overlapped File Name", JOptionPane.YES_NO_OPTION);
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
            fileWriter.close();
            ProgressFrame progressFrame = new ProgressFrame();
            progressFrame.addProgressListener(() -> {
                int continueResponse = JOptionPane.showOptionDialog(null, "Game saved successfully.\nWould you want to continue playing or go back to the main menu?", "Game Saved", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Continue", "Back"}, "Continue");
                if (continueResponse == JOptionPane.NO_OPTION) {
                    GameFrame.get().getGameConfiguration().setBluePlayerType(PlayerType.HUMAN);
                    GameFrame.get().getGameConfiguration().setRedPlayerType(PlayerType.HUMAN);
                    GameFrame.get().restartGame();
                    GameFrame.get().dispose();
                    MainMenu.get().setVisible(true);
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
        fileChooser.showOpenDialog(null);
        File file = fileChooser.getSelectedFile();

        if (!file.getName().endsWith(".txt")) {
            JOptionPane.showMessageDialog(null, "The file extension is either missing or not supported.",
                    "File Load Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ArrayList<String> readList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                readList.add(line);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error reading file.",
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
            JOptionPane.showMessageDialog(null, "The file is corrupted.",
                    "File Load Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        numMoves = Integer.parseInt(readList.remove(0));

        for (int i = 0; i < numMoves; i++) {
            String moveLine = readList.remove(0);
            String[] moveTokens = moveLine.split(" ");
            try {
                if (moveTokens.length != 3) {
                    JOptionPane.showMessageDialog(null, "The file is corrupted.",
                            "File Load Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                playerList.add(moveTokens[0]);
                currentCoordinateList.add(Integer.parseInt(moveTokens[1]));
                destinationCoordinateList.add(Integer.parseInt(moveTokens[2]));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "The file is corrupted.",
                        "File Load Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if (numMoves == currentCoordinateList.size() && numMoves == destinationCoordinateList.size() && numMoves == playerList.size()) {
            for (int i = 0; i < currentCoordinateList.size(); i++) {
                Move move = Move.MoveFactory.createMove(loadedBoard, currentCoordinateList.get(i), destinationCoordinateList.get(i));
                MoveTransition transition = loadedBoard.getCurrentPlayer().makeMove(move);
                if (transition.getMoveStatus().isDone()) {
                    loadedBoard = transition.getTransitionBoard();
                    moveLog.addMove(move);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "The file is corrupted.",
                    "File Load Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String lastTurn = readList.get(0);
        readList.remove(0);
        playerList.add(lastTurn);
        for (int i = 0; i < playerList.size(); i++) {
            if (i % 2 == 0) {
                if (!playerList.get(i).equals("bl")) {
                    JOptionPane.showMessageDialog(null, "The file is corrupted.",
                            "File Load Error", JOptionPane.ERROR_MESSAGE);
                    System.out.println("Wrong player turns.");
                    return;
                }
            } else {
                if (!playerList.get(i).equals("re")) {
                    JOptionPane.showMessageDialog(null, "The file is corrupted.",
                            "File Load Error", JOptionPane.ERROR_MESSAGE);
                    System.out.println("Wrong player turns.");
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
            JOptionPane.showMessageDialog(null, "The file is corrupted.",
                    "File Load Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ArrayList<String> animalList = new ArrayList<>();
        ArrayList<Integer> coordinateList = new ArrayList<>();

        int position = 0;
        for (String line : readList) {
            String[] tokens = line.split("\\s+");
            for (String token : tokens) {
                if (token.length() == 2 && Character.isLetter(token.charAt(0)) && Character.isLetter(token.charAt(1))) {
                    animalList.add(token);
                    coordinateList.add(position);
                } else if (token.length() == 2 && Character.isDigit(token.charAt(0)) && Character.isDigit(token.charAt(1))) {
                    position = Integer.parseInt(token);
                }
                position++;
            }
        }

        Board expectedBoard = Board.constructSpecificBoard(animalList, coordinateList, lastTurn);
        System.out.println(loadedBoard);
        System.out.println(expectedBoard);
        if (!expectedBoard.equals(loadedBoard)) {
            System.out.println("Board is incorrect");
            JOptionPane.showMessageDialog(null, "The file is corrupted.",
                    "File Load Error", JOptionPane.ERROR_MESSAGE);
        } else {
            System.out.println("Board is correct");
            ProgressFrame progressFrame = new ProgressFrame();
            progressFrame.addProgressListener(() -> {
                MainMenu.get().setVisible(false);
                GameFrame.get().setVisible(true);
                if (!MainMenu.get().isGrayScaleBGMButton()) {
                    AudioPlayer.LoopPlayer.playGameBGM();
                }
                GameFrame.get().getPlayerPanel().setTimerSeconds(30);
                System.out.println("Load a Saved Game");
            });
        }
    }

    public static void backToMainMenu() {
        if (GameFrame.get().isReplayMovesInProgress()) {
            JOptionPane.showMessageDialog(null, "Replay is in progress. Please wait.");
            return;
        }
        if (GameFrame.get().getGameConfiguration().isAIPlayer(GameFrame.get().getChessBoard().getCurrentPlayer())) {
            JOptionPane.showMessageDialog(null, "AI is still thinking. Please wait.");
            return;
        }
        GameFrame.get().getGameConfiguration().setBluePlayerType(PlayerType.HUMAN);
        GameFrame.get().getGameConfiguration().setRedPlayerType(PlayerType.HUMAN);
        GameFrame.get().restartGame();
        GameFrame.get().dispose();
        MainMenu.get().setVisible(true);
        if (!MainMenu.get().isGrayScaleBGMButton()) {
            AudioPlayer.LoopPlayer.playMenuBGM();
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
            JOptionPane.showMessageDialog(null, "Replay is in progress. Please wait.");
            return;
        }
        if (GameFrame.get().getGameConfiguration().isAIPlayer(GameFrame.get().getChessBoard().getCurrentPlayer())) {
            JOptionPane.showMessageDialog(null, "AI is still thinking. Please wait.");
            return;
        }
        if (GameFrame.get().getMoveLog().size() == 0) {
            for (int i = 0; i < BoardUtilities.NUM_TERRAINS; i++) {
                GameFrame.get().getBoardPanel().getBoardTerrains().get(i).deselectLeftMouseButton();
            }
            GameFrame.get().restartGame();
            System.out.println("Game Restarted");
            return;
        }
        GameFrame.get().setAnimationInProgress(true);
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                for (int i = 0; i < BoardUtilities.NUM_TERRAINS; i++) {
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
        if (GameFrame.get().getMoveLog().size() == 0) {
            JOptionPane.showMessageDialog(null, "No moves to undo.");
            return;
        }
        if (GameFrame.get().isReplayMovesInProgress()) {
            JOptionPane.showMessageDialog(null, "Replay is in progress. Please wait.");
            return;
        }
        if (GameFrame.get().getGameConfiguration().isAIPlayer(GameFrame.get().getChessBoard().getCurrentPlayer())) {
            JOptionPane.showMessageDialog(null, "AI is still thinking. Please wait.");
            return;
        }
        if (GameFrame.get().getGameConfiguration().getBluePlayerType() == PlayerType.AI &&
                GameFrame.get().getGameConfiguration().getRedPlayerType() == PlayerType.HUMAN) {
            if (GameFrame.get().getMoveLog().size() > 1) {
                Move lastMove = GameFrame.get().getMoveLog().removeMove(GameFrame.get().getMoveLog().size() - 1);
                GameFrame.get().setChessBoard(lastMove.undo());
                if (lastMove.equals(GameFrame.get().getComputerMove()) && GameFrame.get().getMoveLog().size() > 0) {
                    Move secondLastMove = GameFrame.get().getMoveLog().removeMove(GameFrame.get().getMoveLog().size() - 1);
                    GameFrame.get().setChessBoard(secondLastMove.undo());
                    GameFrame.get().setLastMove(GameFrame.get().getMoveLog().getMove(GameFrame.get().getMoveLog().size() - 1));
                    GameFrame.get().setComputerMove(GameFrame.get().getMoveLog().getMove(GameFrame.get().getMoveLog().size() - 1));
                }
                GameFrame.get().getPlayerPanel().undo();
                GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
                GameFrame.get().getCapturedPiecesPanel().redo(GameFrame.get().getMoveLog());
            } else if (GameFrame.get().getMoveLog().size() == 1) {
                for (int i = 0; i < BoardUtilities.NUM_TERRAINS; i++) {
                    GameFrame.get().getBoardPanel().getBoardTerrains().get(i).deselectLeftMouseButton();
                }
                GameFrame.get().setLastMove(null);
                GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
                GameFrame.get().restartGame();
            }
            System.out.println(GameFrame.get().getMoveLog().size());
            System.out.println("Undo");
        } else if (GameFrame.get().getGameConfiguration().getBluePlayerType() == PlayerType.HUMAN && GameFrame.get().getGameConfiguration().getRedPlayerType() == PlayerType.AI) {
            if (GameFrame.get().getMoveLog().size() > 0) {
                Move lastMove = GameFrame.get().getMoveLog().removeMove(GameFrame.get().getMoveLog().size() - 1);
                GameFrame.get().setChessBoard(lastMove.undo());
                if (lastMove.equals(GameFrame.get().getComputerMove())) {
                    Move secondLastMove = GameFrame.get().getMoveLog().removeMove(GameFrame.get().getMoveLog().size() - 1);
                    GameFrame.get().setChessBoard(secondLastMove.undo());
                    if (GameFrame.get().getMoveLog().size() > 0) {
                        GameFrame.get().setLastMove(GameFrame.get().getMoveLog().getMove(GameFrame.get().getMoveLog().size() - 1));
                        GameFrame.get().setComputerMove(GameFrame.get().getMoveLog().getMove(GameFrame.get().getMoveLog().size() - 1));
                    } else {
                        GameFrame.get().setLastMove(null);
                        GameFrame.get().setComputerMove(null);
                    }
                }
                GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
                if (GameFrame.get().getGameConfiguration().getBluePlayerType() == PlayerType.HUMAN && GameFrame.get().getGameConfiguration().getRedPlayerType() == PlayerType.AI) {
                    GameFrame.get().getPlayerPanel().undo();
                }
                GameFrame.get().getCapturedPiecesPanel().redo(GameFrame.get().getMoveLog());
                System.out.println("Undo");
            }
        } else if (GameFrame.get().getGameConfiguration().getBluePlayerType() == PlayerType.HUMAN && GameFrame.get().getGameConfiguration().getRedPlayerType() == PlayerType.HUMAN) {
            if (GameFrame.get().getMoveLog().size() > 1) {
                Move lastMove = GameFrame.get().getMoveLog().removeMove(GameFrame.get().getMoveLog().size() - 1);
                GameFrame.get().setLastMove(GameFrame.get().getMoveLog().getMove(GameFrame.get().getMoveLog().size() - 1));
                GameFrame.get().setChessBoard(lastMove.undo());
                GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
                GameFrame.get().getPlayerPanel().undo();
                GameFrame.get().getCapturedPiecesPanel().redo(GameFrame.get().getMoveLog());
                System.out.println("Undo");
            } else if (GameFrame.get().getMoveLog().size() == 1) {
                for (int i = 0; i < BoardUtilities.NUM_TERRAINS; i++) {
                    GameFrame.get().getBoardPanel().getBoardTerrains().get(i).deselectLeftMouseButton();
                }
                GameFrame.get().setLastMove(null);
                GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
                GameFrame.get().restartGame();
            }
        }
    }

    public static void replayMoves() {
        if (GameFrame.get().isReplayMovesInProgress()) {
            JOptionPane.showMessageDialog(null, "Replay is already in progress. Please wait for the next replay.");
            return;
        }
        if (GameFrame.get().getGameConfiguration().isAIPlayer(GameFrame.get().getChessBoard().getCurrentPlayer())) {
            JOptionPane.showMessageDialog(null, "AI is still thinking. Please wait.");
            return;
        }
        if (GameFrame.get().getMoveLog().size() == 0) {
            JOptionPane.showMessageDialog(null, "No moves to replay.");
            return;
        }
        GameFrame.get().setLastMove(null);
        GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
        GameFrame.get().setReplayMovesInProgress(true);
        GameFrame.get().getPlayerPanel().setRoundNumber(1);
        GameFrame.get().getCapturedPiecesPanel().reset();
        MoveLog seperateMoveLog = new MoveLog();
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                for (int i = 0; i < BoardUtilities.NUM_TERRAINS; i++) {
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
                    Move move = GameFrame.get().getMoveLog().getMove(i);
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

    public static void coloringTerrainsAnimationThread() {
        List<Color> colorList = new ArrayList<>();
        for (int i = 0; i < BoardUtilities.NUM_TERRAINS / 2 + 1; i++) {
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
            for (int i = 0; i < BoardUtilities.NUM_TERRAINS / 2 + 1; i++) {
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
            for (int i = BoardUtilities.NUM_TERRAINS - 1, colorIndex = BoardUtilities.NUM_TERRAINS / 2; i > BoardUtilities.NUM_TERRAINS / 2; i--, colorIndex--) {
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
}
