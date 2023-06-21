package model;

import model.board.Board;
import model.board.BoardUtilities;
import model.board.Move;
import model.board.MoveLog;
import model.player.PlayerType;
import view.DifficultyFrame;
import view.GameFrame;
import view.MainMenu;
import view.ProgressFrame;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
            fileWriter.write(String.valueOf(GameFrame.get().getMoveLog().size()) + "\n");
            for (int i = 0; i < GameFrame.get().getMoveLog().size(); i++) {
                fileWriter.write(GameFrame.get().getMoveLog().getMove(i).toString() + "\n");
            }
            fileWriter.write(GameFrame.get().getChessBoard().getCurrentPlayer().toString().toLowerCase().substring(0, 2) + "\n");
            fileWriter.write(GameFrame.get().getChessBoard().toString());
            fileWriter.close();
            ProgressFrame progressFrame = new ProgressFrame();
            progressFrame.addProgressListener(new ProgressFrame.ProgressListener() {
                @Override
                public void onProgressComplete() {
                    int continueResponse = JOptionPane.showOptionDialog(null, "Game saved successfully.\nWould you want to continue playing or go back to the main menu?", "Game Saved", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Continue", "Back"}, "Continue");
                    if (continueResponse == JOptionPane.NO_OPTION) {
                        GameFrame.get().getGameConfiguration().setBluePlayerType(PlayerType.HUMAN);
                        GameFrame.get().getGameConfiguration().setRedPlayerType(PlayerType.HUMAN);
                        GameFrame.get().restartGame();
                        GameFrame.get().dispose();
                        new MainMenu().setVisible(true);
                        System.out.println("Back To Main Menu");
                    }
                }
            });
            System.out.println("Game Saved");
        } catch (IOException e) {
            e.printStackTrace();
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
        new MainMenu().setVisible(true);
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
        GameFrame.get().restartGame();
        System.out.println("Game Restarted");
    }

    public static void undoMove() {
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
            if (GameFrame.get().getMoveLog().size() > 0) {
                Move lastMove = GameFrame.get().getMoveLog().removeMove(GameFrame.get().getMoveLog().size() - 1);
                GameFrame.get().setChessBoard(lastMove.undo());
                if (lastMove.equals(GameFrame.get().getLastMove()) && GameFrame.get().getMoveLog().size() > 0) {
                    Move secondLastMove = GameFrame.get().getMoveLog().removeMove(GameFrame.get().getMoveLog().size() - 1);
                    GameFrame.get().setChessBoard(secondLastMove.undo());
                    GameFrame.get().setLastMove(GameFrame.get().getMoveLog().getMove(GameFrame.get().getMoveLog().size() - 1));
                } else {
                    GameFrame.get().restartGame();
                    return;
                }
                GameFrame.get().getPlayerPanel().undoAIBlue();
                GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
                GameFrame.get().getCapturedPiecesPanel().redo(GameFrame.get().getMoveLog());
                System.out.println("Undo");
            }
        } else if (GameFrame.get().getGameConfiguration().getBluePlayerType() == PlayerType.HUMAN && GameFrame.get().getGameConfiguration().getRedPlayerType() == PlayerType.AI
                || GameFrame.get().getGameConfiguration().getBluePlayerType() == PlayerType.HUMAN && GameFrame.get().getGameConfiguration().getRedPlayerType() == PlayerType.HUMAN) {
            if (GameFrame.get().getMoveLog().size() > 0) {
                Move lastMove = GameFrame.get().getMoveLog().removeMove(GameFrame.get().getMoveLog().size() - 1);
                GameFrame.get().setChessBoard(lastMove.undo());
                if (lastMove.equals(GameFrame.get().getLastMove())) {
                    Move secondLastMove = GameFrame.get().getMoveLog().removeMove(GameFrame.get().getMoveLog().size() - 1);
                    GameFrame.get().setChessBoard(secondLastMove.undo());
                    if (GameFrame.get().getMoveLog().size() > 0) {
                        GameFrame.get().setLastMove(GameFrame.get().getMoveLog().getMove(GameFrame.get().getMoveLog().size() - 1));
                    } else {
                        GameFrame.get().setLastMove(null);
                    }
                }
                GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
                if (GameFrame.get().getGameConfiguration().getBluePlayerType() == PlayerType.HUMAN && GameFrame.get().getGameConfiguration().getRedPlayerType() == PlayerType.AI) {
                    GameFrame.get().getPlayerPanel().undoAIRed();
                }
                GameFrame.get().getPlayerPanel().undo();
                GameFrame.get().getCapturedPiecesPanel().redo(GameFrame.get().getMoveLog());
                System.out.println("Undo");
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
        System.out.println(reversedColorList.size());
        GameFrame.get().setReplayMovesInProgress(true);
        GameFrame.get().getPlayerPanel().setRoundNumber(1);
        GameFrame.get().getCapturedPiecesPanel().reset();
        MoveLog seperateMoveLog = new MoveLog();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Thread thread1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < BoardUtilities.NUM_TERRAINS / 2 + 1; i++) {

                            GameFrame.get().getBoardPanel().getBoardTerrains().get(i).setOpaque(true);
                            GameFrame.get().getBoardPanel().getBoardTerrains().get(i).setBackground(colorList.get(i));
                            try {
                                Thread.sleep(30);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });
                Thread thread2 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = BoardUtilities.NUM_TERRAINS - 1, colorIndex = BoardUtilities.NUM_TERRAINS / 2; i > BoardUtilities.NUM_TERRAINS / 2; i--, colorIndex--) {
                            GameFrame.get().getBoardPanel().getBoardTerrains().get(i).setOpaque(true);
                            GameFrame.get().getBoardPanel().getBoardTerrains().get(i).setBackground(reversedColorList.get(colorIndex));
                            try {
                                Thread.sleep(30);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
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
                GameFrame.get().setChessBoard(Board.constructStandardBoard());
                GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
                GameFrame.get().getBoardPanel().removeAllBorders();
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
        GameFrame.get().defineBorderLayout();
        String boardImageFileName = GameFrame.get().isBoard1() ? "chessboard1.png" : "chessboard2.png";
        GameFrame.get().getBoardPanel().setBoardImage(boardImageFileName);
        GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
        System.out.println("Board Changed");
    }

    public static void flipBoard() {
        GameFrame.get().boardDirection = GameFrame.get().boardDirection.opposite();
        GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
        System.out.println("Board Rotated");
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

    public static void flipRedSide() {
        GameFrame.get().setReversedRedSide(!GameFrame.get().isReversedRedSide());
        GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
        System.out.println("Red Side Flipped");
    }
}
