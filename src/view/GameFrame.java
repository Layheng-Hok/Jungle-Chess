package view;

import model.artificialintelligence.ConcreteBoardEvaluator;
import model.artificialintelligence.MinimaxAlgorithm;
import model.artificialintelligence.PoorBoardEvaluator;
import model.artificialintelligence.MoveStrategy;
import model.board.*;
import model.piece.Piece;
import model.player.PlayerColor;
import model.player.PlayerType;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;
import static view.MenuBar.createGameFrameMenuBar;

public class GameFrame extends Observable {
    private final JFrame gameFrame;
    private final LeftPanel leftPanel;
    private final RightPanel rightPanel;
    private final PlayerPanel playerPanel;
    private final CapturedPiecesPanel capturedPiecesPanel;
    private final BoardPanel boardPanel;
    private final GameConfiguration gameConfiguration;
    private MoveLog moveLog;
    private Board chessBoard;
    private Move computerMove;
    private Piece sourceTerrain;
    private Piece humanMovedPiece;
    MenuBar.BoardDirection boardDirection;
    private boolean isBoard1 = true;
    private boolean replayMovesInProgress = false;
    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(530, 850);
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(500, 650);
    private static final Dimension TERRAIN_PANEL_DIMENSION = new Dimension(10, 10);
    private final ImageIcon logo = new ImageIcon(defaultImagesPath + "junglechesslogo.jpg");
    static final String defaultImagesPath = "resource/images/";
    private static final GameFrame INSTANCE = new GameFrame();

    public GameFrame() {
        this.gameFrame = new JFrame("Jungle Chess (斗兽棋) - Game");
        this.gameFrame.setLayout(new BorderLayout());
        this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final JMenuBar gameFrameMenuBar = createGameFrameMenuBar();
        this.gameFrame.setJMenuBar(gameFrameMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.chessBoard = Board.constructStandardBoard();
        this.gameFrame.setLocationRelativeTo(null);
        this.leftPanel = new LeftPanel();
        this.rightPanel = new RightPanel();
        this.playerPanel = new PlayerPanel();
        this.capturedPiecesPanel = new CapturedPiecesPanel();
        this.boardPanel = new BoardPanel();
        this.moveLog = new MoveLog();
        this.addObserver(new AIGameObserver());
        this.gameConfiguration = new GameConfiguration(this.gameFrame, true);
        this.boardDirection = MenuBar.BoardDirection.NORMAL;
        this.gameFrame.add(this.leftPanel, BorderLayout.WEST);
        this.gameFrame.add(this.rightPanel, BorderLayout.EAST);
        this.gameFrame.add(this.playerPanel, BorderLayout.NORTH);
        this.gameFrame.add(this.capturedPiecesPanel, BorderLayout.SOUTH);
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.setIconImage(logo.getImage());
        this.gameFrame.setResizable(false);
    }

     class BoardPanel extends JPanel {
        final List<TerrainPanel> boardTerrains;
        private Image boardImage;

        BoardPanel() {
            super(new GridLayout(9, 7));
            this.boardTerrains = new ArrayList<>();
            setBoardImage("chessboard1.png");
            for (int i = 0; i < BoardUtilities.NUM_TERRAINS; i++) {
                final TerrainPanel terrainPanel = new TerrainPanel(this, i);
                this.boardTerrains.add(terrainPanel);
                add(terrainPanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            setMaximumSize(getPreferredSize());
            validate();
        }

        public void drawBoard(final Board board) {
            removeAll();
            for (final TerrainPanel terrainPanel : boardDirection.traverse(boardTerrains)) {
                terrainPanel.drawTerrain(board);
                add(terrainPanel);
            }
            validate();
            repaint();
        }

        public void setBoardImage(String imageFileName) {
            try {
                this.boardImage = ImageIO.read(new File(defaultImagesPath + imageFileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void removeAllBorders() {
            for (final TerrainPanel terrainPanel : boardTerrains) {
                terrainPanel.setBorder(BorderFactory.createEmptyBorder());
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(boardImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private class LeftPanel extends JPanel {
        private final Image leftPanelImage;

        LeftPanel() {
            super(new BorderLayout());
            this.leftPanelImage = new ImageIcon(defaultImagesPath + "leftpanel.png").getImage();
            setPreferredSize(new Dimension(25, 675));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(leftPanelImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private class RightPanel extends JPanel {
        private final Image rightPanelImage;

        RightPanel() {
            super(new BorderLayout());
            this.rightPanelImage = new ImageIcon(defaultImagesPath + "rightpanel.png").getImage();
            setPreferredSize(new Dimension(25, 675));

        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(rightPanelImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    class TerrainPanel extends JPanel {
        private final int terrainCoordinate;

        TerrainPanel(final BoardPanel boardPanel, final int terrainCoordinate) {
            super(new GridBagLayout());
            this.terrainCoordinate = terrainCoordinate;
            setPreferredSize(TERRAIN_PANEL_DIMENSION);
            assignTerrainColor(terrainCoordinate);
            assignTerrainPieceIcon(chessBoard);
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    if (isRightMouseButton(e)) {
                        sourceTerrain = null;
                        humanMovedPiece = null;
                    } else if (isLeftMouseButton(e)) {
                        if (replayMovesInProgress) {
                            JOptionPane.showMessageDialog(null, "Replay is already in progress. Please wait for the next replay.");
                            return;
                        }
                        if (GameFrame.get().getGameConfiguration().isAIPlayer(GameFrame.get().getChessBoard().getCurrentPlayer())) {
                            return;
                        }
                        AudioPlayer.SinglePlayer.playClickEffect();
                        if (sourceTerrain == null) {
                            sourceTerrain = chessBoard.getPiece(terrainCoordinate);
                            if (sourceTerrain != null && sourceTerrain.getPieceColor() != chessBoard.getCurrentPlayer().getAllyColor()) {
                                sourceTerrain = null;
                            } else {
                                humanMovedPiece = sourceTerrain;
                            }
                        } else {
                            computerMove = null;
                            final Move move = Move.MoveCreator.createMove(chessBoard, sourceTerrain.getPieceCoordinate(), terrainCoordinate);
                            if (move.isCaptureMove()) {
                                AudioPlayer.SinglePlayer.playAnimalSoundEffect(move.getMovedPiece());
                            }
                            final MoveTransition transition = chessBoard.getCurrentPlayer().makeMove(move);
                            if (transition.getMoveStatus().isDone()) {
                                chessBoard = transition.getTransitionBoard();
                                playerPanel.update();
                                moveLog.addMove(move);
                            }
                            sourceTerrain = null;
                            humanMovedPiece = null;
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                capturedPiecesPanel.redo(moveLog);
                                if (gameConfiguration.isAIPlayer(chessBoard.getCurrentPlayer())) {
                                    GameFrame.get().moveMadeUpdate(PlayerType.HUMAN);
                                }
                                boardPanel.drawBoard(chessBoard);
                                checkWin();
                            }
                        });
                    }
                }

                @Override
                public void mousePressed(final MouseEvent e) {
                }

                @Override
                public void mouseReleased(final MouseEvent e) {
                }

                @Override
                public void mouseEntered(final MouseEvent e) {
                    setBorder(BorderFactory.createLineBorder(new Color(195, 80, 170), 2));
                }

                @Override
                public void mouseExited(final MouseEvent e) {
                    setBorder(BorderFactory.createEmptyBorder());
                }
            });

            validate();
        }

        public void drawTerrain(final Board board) {
            assignTerrainColor(terrainCoordinate);
            assignTerrainPieceIcon(board);
            drawTerrainBorder();
            highlightValidMoves(board);
            highlightAIMoves();
            validate();
            repaint();
        }

        private void drawTerrainBorder() {
            if (humanMovedPiece != null
                    && humanMovedPiece.getPieceColor() == chessBoard.getCurrentPlayer().getAllyColor()
                    && humanMovedPiece.getPieceCoordinate() == this.terrainCoordinate) {
                setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
            } else {
                setBorder(BorderFactory.createEmptyBorder());
            }
        }

        private void highlightValidMoves(final Board board) {
            for (final Move move : pieceValidMoves(board)) {
                if (move.getDestinationCoordinate() == this.terrainCoordinate) {
                    try {
                        String dotColor = isBoard1 ? "yellow" : "green";
                        add(new JLabel(new ImageIcon(ImageIO.read(new File(defaultImagesPath + dotColor + "dot.png")))));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private void highlightAIMoves() {
            if (computerMove != null) {
                if (this.terrainCoordinate == computerMove.getCurrentCoordinate()) {
                    setBorder(BorderFactory.createLineBorder(new Color(14, 74, 17), 2));
                } else if (this.terrainCoordinate == computerMove.getDestinationCoordinate()) {
                    setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
                }
            }
        }

        private Collection<Move> pieceValidMoves(final Board board) {
            if (humanMovedPiece != null && humanMovedPiece.getPieceColor() == board.getCurrentPlayer().getAllyColor()) {
                return humanMovedPiece.determineValidMoves(board);
            }
            return Collections.emptyList();
        }

        private void assignTerrainPieceIcon(final Board board) {
            this.removeAll();
            if (board.getPiece(this.terrainCoordinate) != null) {
                try {
                    final BufferedImage image = ImageIO.read(new File(defaultImagesPath
                            + board.getTerrain(this.terrainCoordinate).getPiece().getPieceColor().toString().toLowerCase()
                            + board.getTerrain(this.terrainCoordinate).getPiece().toString().toLowerCase() + ".png"));
                    ImageIcon icon = new ImageIcon(image);
                    int labelWidth = 65;
                    int labelHeight = 65;
                    Image scaledImage = icon.getImage().getScaledInstance(labelWidth, labelHeight, Image.SCALE_SMOOTH);
                    icon = new ImageIcon(scaledImage);
                    JLabel label = new JLabel(icon);
                    add(label);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void assignTerrainColor(final int coordinate) {
            if (BoardUtilities.isLand(coordinate)) {
                setBackground(new Color(0x28B463));
            } else if (BoardUtilities.isRiver(coordinate)) {
                setBackground(new Color(0x63B8FF));
            } else if (BoardUtilities.isEnemyTrap(coordinate, PlayerColor.BLUE)) {
                setBackground(new Color(0xE67E22));
            } else if (BoardUtilities.isEnemyTrap(coordinate, PlayerColor.RED)) {
                setBackground(new Color(0xE67E22));
            } else if (BoardUtilities.isDen(coordinate, PlayerColor.BLUE)) {
                setBackground(new Color(0x3498DB));
            } else if (BoardUtilities.isDen(coordinate, PlayerColor.RED)) {
                setBackground(new Color(0xEC7063));
            }
            setOpaque(false);
        }
    }

    public void setupUpdate(final GameConfiguration AIGameConfiguration) {
        setChanged();
        notifyObservers(AIGameConfiguration);
    }

    void moveMadeUpdate(final PlayerType playerType) {
        setChanged();
        notifyObservers(playerType);
    }

    void updateGameBoard(final Board board) {
        this.chessBoard = board;
    }

    private void updateComputerMove(final Move move) {
        this.computerMove = move;
    }

    public static class AIGameObserver implements Observer {
        @Override
        public void update(final Observable o, final Object arg) {
            if (GameFrame.get().gameConfiguration.isAIPlayer(GameFrame.get().getChessBoard().getCurrentPlayer()) &&
                    !GameFrame.get().getChessBoard().getCurrentPlayer().isDenPenetrated()) {
                final IntelligenceHub intelligenceHub = new IntelligenceHub();
                intelligenceHub.execute();
            }
        }
    }

    public static class IntelligenceHub extends SwingWorker<Move, String> {
        boolean isMinimaxRunning = false;

        private IntelligenceHub() {
        }

        @Override
        protected Move doInBackground() throws Exception {
            if (DifficultyFrame.getDifficulty() == DifficultyFrame.Difficulty.EASY) {
                isMinimaxRunning = true;
                final List<Move> validMoves = new ArrayList<>(GameFrame.get().getChessBoard().getCurrentPlayer().getValidMoves());
                for (Move move : validMoves) {
                    if (move.isCaptureMove()) {
                        return move;
                    }
                }
                if (validMoves.size() == 0) {
                    return null;
                }
                return validMoves.get(new Random().nextInt(validMoves.size()));
            }
            if (DifficultyFrame.getDifficulty() == DifficultyFrame.Difficulty.MEDIUM) {
                isMinimaxRunning = true;
                final MoveStrategy minimax = new MinimaxAlgorithm(4, PoorBoardEvaluator.get());
                System.out.println(ConcreteBoardEvaluator.get().evaluationDetails(GameFrame.get().getChessBoard(), GameFrame.get().gameConfiguration.getSearchDepth()));
                return minimax.execute(GameFrame.get().getChessBoard());
            }
            if (DifficultyFrame.getDifficulty() == DifficultyFrame.Difficulty.HARD) {
                isMinimaxRunning = true;
                final MoveStrategy minimax = new MinimaxAlgorithm(4, ConcreteBoardEvaluator.get());
                System.out.println(ConcreteBoardEvaluator.get().evaluationDetails(GameFrame.get().getChessBoard(), GameFrame.get().gameConfiguration.getSearchDepth()));
                return minimax.execute(GameFrame.get().getChessBoard());
            }
            return null;
        }

        @Override
        public void done() {
            try {
                final Move optimalMove = get();
                if (optimalMove.isCaptureMove()) {
                    AudioPlayer.SinglePlayer.playAnimalSoundEffect(optimalMove.getMovedPiece());
                }
                GameFrame.get().updateComputerMove(optimalMove);
                GameFrame.get().updateGameBoard(GameFrame.get().getChessBoard().getCurrentPlayer().makeMove(optimalMove).getTransitionBoard());
                GameFrame.get().getMoveLog().addMove(optimalMove);
                GameFrame.get().getPlayerPanel().setCurrentPlayer(GameFrame.get().getChessBoard().getCurrentPlayer().toString());
                GameFrame.get().getPlayerPanel().redo(GameFrame.get().chessBoard);
                GameFrame.get().getCapturedPiecesPanel().redo(GameFrame.get().getMoveLog());
                GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
                GameFrame.get().checkWin();
            } catch (final Exception e) {
                e.printStackTrace();
            }
            isMinimaxRunning = false;
        }
    }

    public static GameFrame get() {
        return INSTANCE;
    }

    public void show() {
        GameFrame.get().getMoveLog().clear();
        GameFrame.get().getPlayerPanel().reset();
        GameFrame.get().getCapturedPiecesPanel().redo(GameFrame.get().getMoveLog());
        GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
        GameFrame.get().getBoardPanel().removeAllBorders();
        AudioPlayer.playGameBGM();
        GameFrame.get().setVisible(true);
    }

    public void restartGame() {
        chessBoard = Board.constructStandardBoard();
        boardPanel.drawBoard(chessBoard);
        boardPanel.removeAllBorders();
        computerMove = null;
        playerPanel.reset();
        capturedPiecesPanel.reset();
        moveLog.clear();
        setChanged();
        notifyObservers();
        System.out.println("Game Restarted");
    }

    public void setLoadBoard(Board loadBoard, MoveLog loadMoveLog, int roundNumber) {
        this.chessBoard = loadBoard;
        this.moveLog = loadMoveLog;
        boardPanel.drawBoard(chessBoard);
        boardPanel.removeAllBorders();
        capturedPiecesPanel.redo(moveLog);
        playerPanel.setRoundNumber(roundNumber);
        playerPanel.setCurrentPlayer(chessBoard.getCurrentPlayer().toString());
        playerPanel.repaint();
        System.out.println("Game Loaded");
    }

    public void checkWin() {
        if (GameFrame.get().getChessBoard().getCurrentPlayer().isDenPenetrated()) {
            GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
            JOptionPane.showMessageDialog(null,
                    "Game Over: Player " + GameFrame.get().getChessBoard().getCurrentPlayer() + "'s den is penetrated by the enemy!", "Game Over",
                    JOptionPane.INFORMATION_MESSAGE);
            System.out.println("Game Over: Player " + GameFrame.get().getChessBoard().getCurrentPlayer() + "'s den is penetrated by the enemy!");
            GameFrame.get().restartGame();
            System.out.println("Game Restarted");
        }
        if (GameFrame.get().getChessBoard().getCurrentPlayer().getActivePieces().isEmpty()) {
            GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
            JOptionPane.showMessageDialog(null,
                    "Game Over: " + GameFrame.get().getChessBoard().getCurrentPlayer() + " Player has no more pieces!", "Game Over",
                    JOptionPane.INFORMATION_MESSAGE);
            System.out.println("Game Over: " + GameFrame.get().getChessBoard().getCurrentPlayer() + " Player has no more pieces!");
            GameFrame.get().restartGame();
            System.out.println("Game Restarted");
        }
    }

    public GameConfiguration getGameConfiguration() {
        return this.gameConfiguration;
    }

    public BoardPanel getBoardPanel() {
        return this.boardPanel;
    }

    public PlayerPanel getPlayerPanel() {
        return this.playerPanel;
    }

    public CapturedPiecesPanel getCapturedPiecesPanel() {
        return this.capturedPiecesPanel;
    }

    public Board getChessBoard() {
        return this.chessBoard;
    }

    public void setChessBoard(Board chessBoard) {
        this.chessBoard = chessBoard;
    }

    public Move getComputerMove() {
        return computerMove;
    }

    public void setComputerMove(Move computerMove) {
        this.computerMove = computerMove;
    }

    public MoveLog getMoveLog() {
        return moveLog;
    }

    public void setMoveLog(MoveLog moveLog) {
        this.moveLog = moveLog;
    }

    public boolean isReplayMovesInProgress() {
        return replayMovesInProgress;
    }

    public void setReplayMovesInProgress(boolean replayMovesInProgress) {
        this.replayMovesInProgress = replayMovesInProgress;
    }

    public boolean isBoard1() {
        return isBoard1;
    }

    public void setBoard1(boolean isBoard1) {
        this.isBoard1 = isBoard1;
    }

    public void setGameBoard(final Board chessBoard) {
        this.chessBoard = chessBoard;
        boardPanel.drawBoard(chessBoard);
        capturedPiecesPanel.redo(moveLog);
        if (GameFrame.get().getGameConfiguration().isAIPlayer(chessBoard.getCurrentPlayer())) {
            moveMadeUpdate(PlayerType.HUMAN);
        }
    }

    public void setVisible(boolean b) {
        this.gameFrame.setVisible(b);
    }

    public void dispose() {
        gameFrame.dispose();
    }
}
