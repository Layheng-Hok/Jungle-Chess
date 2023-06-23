package view;

import model.Controller;
import model.artificialintelligence.ConcreteBoardEvaluator;
import model.artificialintelligence.MinimaxAlgorithm;
import model.artificialintelligence.MoveStrategy;
import model.artificialintelligence.PoorBoardEvaluator;
import model.board.*;
import model.piece.Piece;
import model.player.PlayerColor;
import model.player.PlayerType;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;
import static model.board.Move.NULL_MOVE;
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
    private Move lastMove;
    private Move computerMove;
    private Piece sourceTerrain;
    private Piece humanMovedPiece;
    public Controller.BoardDirection boardDirection;
    private boolean isBoard1 = true;
    private boolean replayMovesInProgress = false;
    private boolean reversedRedSide = true;
    private boolean reversedBlueSide = false;
    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(530, 850);
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(500, 650);
    private static final Dimension TERRAIN_PANEL_DIMENSION = new Dimension(10, 10);
    final ImageIcon logo = new ImageIcon(defaultImagesPath + "junglechesslogo.jpg");
    static final String defaultImagesPath = "resource/images/";
    private static final GameFrame INSTANCE = new GameFrame();

    public GameFrame() {
        this.gameFrame = new JFrame("Jungle Chess (斗兽棋) - Game");
        setBasicGameFrameAttributes();
        initGameState();
        this.leftPanel = new LeftPanel();
        this.rightPanel = new RightPanel();
        this.playerPanel = new PlayerPanel();
        this.capturedPiecesPanel = new CapturedPiecesPanel();
        this.boardPanel = new BoardPanel();
        setMenuBarAndPanels();
        this.gameConfiguration = new GameConfiguration(this.gameFrame, true);
    }

    private void setBasicGameFrameAttributes() {
        this.gameFrame.setLayout(new BorderLayout(2, 2));
        this.gameFrame.getContentPane().setBackground(new Color(117, 137, 120));
        this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.gameFrame.setLocationRelativeTo(null);
        this.gameFrame.setIconImage(logo.getImage());
        this.gameFrame.setResizable(false);
    }

    public void defineBorderLayout() {
        if (isBoard1) {
            this.gameFrame.setLayout(new BorderLayout(2, 2));
        } else {
            this.gameFrame.setLayout(new BorderLayout());
        }
        this.gameFrame.add(this.leftPanel, BorderLayout.WEST);
        this.gameFrame.add(this.rightPanel, BorderLayout.EAST);
        this.gameFrame.add(this.playerPanel, BorderLayout.NORTH);
        this.gameFrame.add(this.capturedPiecesPanel, BorderLayout.SOUTH);
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.revalidate();
        this.gameFrame.repaint();
    }

    private void setMenuBarAndPanels() {
        final JMenuBar gameFrameMenuBar = createGameFrameMenuBar();
        this.gameFrame.setJMenuBar(gameFrameMenuBar);
        this.gameFrame.add(this.leftPanel, BorderLayout.WEST);
        this.gameFrame.add(this.rightPanel, BorderLayout.EAST);
        this.gameFrame.add(this.playerPanel, BorderLayout.NORTH);
        this.gameFrame.add(this.capturedPiecesPanel, BorderLayout.SOUTH);
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
    }

    private void initGameState() {
        this.chessBoard = Board.constructStandardBoard();
        this.moveLog = new MoveLog();
        this.addObserver(new AIGameObserver());
        this.boardDirection = Controller.BoardDirection.NORMAL;
    }

    public class BoardPanel extends JPanel {
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

        public List<TerrainPanel> getBoardTerrains() {
            return boardTerrains;
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

    public class TerrainPanel extends JPanel {
        private final int terrainCoordinate;
        private final Border mouseEnteredBorder = BorderFactory.createLineBorder(new Color(195, 80, 170), 3);
        private final Border blueSelectedBorder = BorderFactory.createLineBorder(new Color(1, 41, 161), 3);
        private final Border redSelectedBorder = BorderFactory.createLineBorder(new Color(180, 23, 23), 3);
        private final Border capturedPieceBorder = BorderFactory.createLineBorder(new Color(14, 74, 17), 5);

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
                            JOptionPane.showMessageDialog(null, "Replay is in progress. Please wait.");
                            return;
                        }
                        if (GameFrame.get().getGameConfiguration().isAIPlayer(GameFrame.get().getChessBoard().getCurrentPlayer())) {
                            return;
                        }
                        AudioPlayer.SinglePlayer.playSoundEffect("click.wav");
                        if (sourceTerrain == null) {
                            sourceTerrain = chessBoard.getPiece(terrainCoordinate);
                            if (sourceTerrain != null && sourceTerrain.getPieceColor() != chessBoard.getCurrentPlayer().getAllyColor()) {
                                sourceTerrain = null;
                            } else {
                                humanMovedPiece = sourceTerrain;
                            }
                        } else {
                            final Move move = Move.MoveFactory.createMove(chessBoard, sourceTerrain.getPieceCoordinate(), terrainCoordinate);
                            if (!(move.equals(NULL_MOVE))) {
                                lastMove = move;
                            }
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
                        SwingUtilities.invokeLater(() -> {
                            capturedPiecesPanel.redo(moveLog);
                            if (gameConfiguration.isAIPlayer(chessBoard.getCurrentPlayer())) {
                                GameFrame.get().moveMadeUpdate(PlayerType.HUMAN);
                            }
                            boardPanel.drawBoard(chessBoard);
                            checkWin();
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
                    Border border = getBorder();
                    if (border == null || !(border.equals(blueSelectedBorder)
                            || border.equals(redSelectedBorder)
                            || border.equals(capturedPieceBorder))) {
                        setBorder(mouseEnteredBorder);
                    }
                }

                @Override
                public void mouseExited(final MouseEvent e) {
                    Border border = getBorder();
                    if (border != null && border.equals(mouseEnteredBorder)) {
                        setBorder(null);
                    }
                }
            });
            validate();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            TerrainPanel other = (TerrainPanel) obj;
            Border thisBorder = getBorder();
            Border otherBorder = other.getBorder();
            if (thisBorder instanceof LineBorder && otherBorder instanceof LineBorder) {
                Color thisBorderColor = ((LineBorder) thisBorder).getLineColor();
                Color otherBorderColor = ((LineBorder) otherBorder).getLineColor();
                int thisBorderThickness = ((LineBorder) thisBorder).getThickness();
                int otherBorderThickness = ((LineBorder) otherBorder).getThickness();
                return thisBorderColor.equals(otherBorderColor) && thisBorderThickness == otherBorderThickness;
            }
            return thisBorder == null && otherBorder == null;
        }

        public void drawTerrain(final Board board) {
            assignTerrainColor(terrainCoordinate);
            assignTerrainPieceIcon(board);
            drawSelectedBorder();
            highlightValidMoves(board);
            highlightLastMove();
            validate();
            repaint();
        }

        private void drawSelectedBorder() {
            if (humanMovedPiece != null
                    && humanMovedPiece.getPieceColor() == chessBoard.getCurrentPlayer().getAllyColor()
                    && humanMovedPiece.getPieceCoordinate() == this.terrainCoordinate) {
                if (chessBoard.getCurrentPlayer().getAllyColor() == PlayerColor.BLUE) {
                    setBorder(blueSelectedBorder);
                    setBackground(new Color(0, 71, 210, 90));
                } else {
                    setBorder(redSelectedBorder);
                    setBackground(new Color(180, 23, 23, 90));
                }
                setOpaque(true);
            } else {
                setBorder(BorderFactory.createEmptyBorder());
            }
        }

        private void highlightValidMoves(final Board board) {
            for (final Move move : pieceValidMoves(board)) {
                if (!move.isCaptureMove() && move.getDestinationCoordinate() == this.terrainCoordinate) {
                    try {
                        String dotColor = GameFrame.get().getChessBoard().getCurrentPlayer().getAllyColor() == PlayerColor.BLUE ? "blue" : "red";
                        ImageIcon dotIcon = new ImageIcon(ImageIO.read(new File(defaultImagesPath + dotColor + "dot.png")));
                        Image resizedImage = dotIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                        add(new JLabel(new ImageIcon(resizedImage)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (move.isCaptureMove() && move.getDestinationCoordinate() == this.terrainCoordinate) {
                    setBorder(capturedPieceBorder);
                    setBackground(new Color(12, 211, 28, 90));
                    setOpaque(true);
                }
            }
        }

        private void highlightLastMove() {
            if (lastMove != null) {
                if (this.terrainCoordinate == lastMove.getCurrentCoordinate() || this.terrainCoordinate == lastMove.getDestinationCoordinate()) {
                    if (chessBoard.getCurrentPlayer().getAllyColor() == PlayerColor.RED) {
                        setBorder(blueSelectedBorder);
                        setBackground(new Color(0, 71, 210, 90));
                    } else {
                        setBorder(redSelectedBorder);
                        setBackground(new Color(180, 23, 23, 90));
                    }
                    setOpaque(true);
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
                    final BufferedImage image;
                    String imagePath = null;
                    if (!reversedBlueSide && reversedRedSide) {
                        imagePath = defaultImagesPath
                                + board.getTerrain(this.terrainCoordinate).getPiece().getPieceColor().toString().toLowerCase()
                                + board.getTerrain(this.terrainCoordinate).getPiece().toString().toLowerCase() + ".png";
                    } else if (!reversedBlueSide && !reversedRedSide) {
                        if (board.getTerrain(this.terrainCoordinate).getPiece().getPieceColor() == PlayerColor.BLUE) {
                            imagePath = defaultImagesPath
                                    + board.getTerrain(this.terrainCoordinate).getPiece().getPieceColor().toString().toLowerCase()
                                    + board.getTerrain(this.terrainCoordinate).getPiece().toString().toLowerCase() + ".png";
                        } else {
                            imagePath = defaultImagesPath + "unreversed"
                                    + board.getTerrain(this.terrainCoordinate).getPiece().getPieceColor().toString().toLowerCase()
                                    + board.getTerrain(this.terrainCoordinate).getPiece().toString().toLowerCase() + ".png";
                        }
                    } else if (reversedBlueSide && !reversedRedSide) {
                        if (board.getTerrain(this.terrainCoordinate).getPiece().getPieceColor() == PlayerColor.BLUE) {
                            imagePath = defaultImagesPath + "reversed"
                                    + board.getTerrain(this.terrainCoordinate).getPiece().getPieceColor().toString().toLowerCase()
                                    + board.getTerrain(this.terrainCoordinate).getPiece().toString().toLowerCase() + ".png";
                        } else {
                            imagePath = defaultImagesPath + "unreversed"
                                    + board.getTerrain(this.terrainCoordinate).getPiece().getPieceColor().toString().toLowerCase()
                                    + board.getTerrain(this.terrainCoordinate).getPiece().toString().toLowerCase() + ".png";
                        }
                    }
                    image = ImageIO.read(new File(imagePath));
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
                setBackground(new Color(40, 180, 99));
            } else if (BoardUtilities.isRiver(coordinate)) {
                setBackground(new Color(99, 184, 255));
            } else if (BoardUtilities.isEnemyTrap(coordinate, PlayerColor.BLUE)) {
                setBackground(new Color(230, 126, 34));
            } else if (BoardUtilities.isEnemyTrap(coordinate, PlayerColor.RED)) {
                setBackground(new Color(230, 126, 34));
            } else if (BoardUtilities.isDen(coordinate, PlayerColor.BLUE)) {
                setBackground(new Color(52, 152, 219));
            } else if (BoardUtilities.isDen(coordinate, PlayerColor.RED)) {
                setBackground(new Color(236, 112, 99));
            }
            setOpaque(false);
        }
    }

    public void setupUpdate(final GameConfiguration AIGameConfiguration) {
        setChanged();
        notifyObservers(AIGameConfiguration);
    }

    public void moveMadeUpdate(final PlayerType playerType) {
        setChanged();
        notifyObservers(playerType);
    }

    void updateGameBoard(final Board board) {
        this.chessBoard = board;
    }

    private void updateComputerMove(final Move move) {
        this.lastMove = move;
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

        @Override
        protected Move doInBackground() throws Exception {
            if (DifficultyFrame.getDifficulty() == DifficultyFrame.Difficulty.EASY) {
                isMinimaxRunning = true;
                final MoveStrategy minimax = new MinimaxAlgorithm(3, PoorBoardEvaluator.get());
                System.out.println(ConcreteBoardEvaluator.get().evaluationDetails(GameFrame.get().getChessBoard(), GameFrame.get().gameConfiguration.getSearchDepth()));
                return minimax.execute(GameFrame.get().getChessBoard());
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
                } else {
                    AudioPlayer.SinglePlayer.playSoundEffect("click.wav");
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

    public void show() {
        GameFrame.get().getMoveLog().clear();
        GameFrame.get().getPlayerPanel().reset();
        GameFrame.get().getCapturedPiecesPanel().redo(GameFrame.get().getMoveLog());
        GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
        GameFrame.get().getBoardPanel().removeAllBorders();
        if (!MainMenu.get().isGrayScaleBGMButton()) {
            AudioPlayer.LoopPlayer.playGameBGM();
        }
        GameFrame.get().setVisible(true);
    }

    public void restartGame() {
        for (int i = 0; i < 2; i++) {
            chessBoard = Board.constructStandardBoard();
            boardPanel.drawBoard(chessBoard);
            boardPanel.removeAllBorders();
            lastMove = null;
            computerMove = null;
            playerPanel.reset();
            capturedPiecesPanel.reset();
            moveLog.clear();
        }
        GameFrame.get().getGameConfiguration().setReady(false);
        setChanged();
        notifyObservers();
    }

    void checkWin() {
        if (GameFrame.get().getChessBoard().getCurrentPlayer().isDenPenetrated()) {
            if (GameFrame.get().getGameConfiguration().getBluePlayerType() == GameFrame.get().gameConfiguration.getRedPlayerType()
                    || GameFrame.get().getGameConfiguration().isAIPlayer(GameFrame.get().getChessBoard().getCurrentPlayer())) {
                AudioPlayer.SinglePlayer.playSoundEffect("winning.wav");
            } else {
                AudioPlayer.SinglePlayer.playSoundEffect("losing.wav");
            }
            GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
            ImageIcon gameOverIcon = new ImageIcon(defaultImagesPath + "gameover.png");
            Image resizedImage = gameOverIcon.getImage().getScaledInstance(35, 35, Image.SCALE_DEFAULT);
            Icon resizedIcon = new ImageIcon(resizedImage);
            JOptionPane.showMessageDialog(null,
                    "Game Over: " + GameFrame.get().getChessBoard().getCurrentPlayer().getEnemyPlayer() + " Player wins.\n"
                            + GameFrame.get().getChessBoard().getCurrentPlayer() + " Player" + "'s den is penetrated by the enemy!",
                    "Game Over",
                    JOptionPane.INFORMATION_MESSAGE,
                    resizedIcon);

            System.out.println("Game Over: " + GameFrame.get().getChessBoard().getCurrentPlayer().getEnemyPlayer() + " Player wins.\n"
                    + GameFrame.get().getChessBoard().getCurrentPlayer() + " Player" + "'s den is penetrated by the enemy!");
            GameFrame.get().restartGame();
            System.out.println("Game Restarted");
        }
        if (GameFrame.get().getChessBoard().getCurrentPlayer().getActivePieces().isEmpty()) {
            if (GameFrame.get().getGameConfiguration().getBluePlayerType() == GameFrame.get().gameConfiguration.getRedPlayerType()
                    || GameFrame.get().getGameConfiguration().isAIPlayer(GameFrame.get().getChessBoard().getCurrentPlayer())) {
                AudioPlayer.SinglePlayer.playSoundEffect("winning.wav");
            } else {
                AudioPlayer.SinglePlayer.playSoundEffect("losing.wav");
            }
            GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
            ImageIcon gameOverIcon = new ImageIcon(defaultImagesPath + "gameover.png");
            Image resizedImage = gameOverIcon.getImage().getScaledInstance(35, 35, Image.SCALE_DEFAULT);
            Icon resizedIcon = new ImageIcon(resizedImage);
            JOptionPane.showMessageDialog(null,
                    "Game Over: " + GameFrame.get().getChessBoard().getCurrentPlayer().getEnemyPlayer() + " Player wins.\n"
                            + GameFrame.get().getChessBoard().getCurrentPlayer() + " Player" + " has no more pieces!",
                    "Game Over",
                    JOptionPane.INFORMATION_MESSAGE,
                    resizedIcon);

            System.out.println("Game Over: " + GameFrame.get().getChessBoard().getCurrentPlayer().getEnemyPlayer() + " Player wins.\n"
                    + GameFrame.get().getChessBoard().getCurrentPlayer() + " Player" + " has no more pieces!");
            GameFrame.get().restartGame();
            System.out.println("Game Restarted");
        }
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

    public Move getLastMove() {
        return lastMove;
    }

    public void setLastMove(Move lastMove) {
        this.lastMove = lastMove;
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

    public boolean isReversedRedSide() {
        return reversedRedSide;
    }

    public void setReversedRedSide(boolean reversedRedSide) {
        this.reversedRedSide = reversedRedSide;
    }

    public boolean isReversedBlueSide() {
        return reversedBlueSide;
    }

    public void setReversedBlueSide(boolean reversedBlueSide) {
        this.reversedBlueSide = reversedBlueSide;
    }

    public static GameFrame get() {
        return INSTANCE;
    }
}
