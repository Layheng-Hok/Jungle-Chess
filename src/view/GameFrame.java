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
    public final JFrame gameFrame;
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
    private boolean animationInProgress = false;
    private boolean reversedRedSide = true;
    private boolean reversedBlueSide = false;
    private boolean normalModeGameOver = false;
    private boolean glitchMode = false;
    boolean firstGlitchModeEncountered = true;
    private boolean blitzMode = false;
    private boolean blitzModeGameOver = false;
    private boolean gameResigned = false;
    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(530, 850);
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(500, 650);
    private static final Dimension TERRAIN_PANEL_DIMENSION = new Dimension(10, 10);
    public final ImageIcon logo = new ImageIcon(defaultImagesPath + "junglechesslogo.jpg");
    public static final String defaultImagesPath = "resources/images/";
    private static final GameFrame INSTANCE = new GameFrame();

    public GameFrame() {
        this.gameFrame = new JFrame("Jungle Chess (斗兽棋)");
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
        this.gameFrame.setLayout(new BorderLayout());
        this.gameFrame.getContentPane().setBackground(new Color(117, 137, 120));
        this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.gameFrame.setLocationRelativeTo(null);
        this.gameFrame.setIconImage(logo.getImage());
        this.gameFrame.setResizable(false);
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

        public void removeAllBackgrounds() {
            for (final TerrainPanel terrainPanel : boardTerrains) {
                terrainPanel.setBackground(null);
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

    private static class LeftPanel extends JPanel {
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

    private static class RightPanel extends JPanel {
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
        private final Border blueSelectedBorder = BorderFactory.createLineBorder(new Color(1, 41, 161), 3);
        private final Border redSelectedBorder = BorderFactory.createLineBorder(new Color(180, 23, 23), 3);
        private final Border blueHoveringBorder = BorderFactory.createLineBorder(new Color(1, 41, 161), 3);
        private final Border redHoveringBorder = BorderFactory.createLineBorder(new Color(180, 23, 23), 3);
        private final Border capturedPieceBorder = BorderFactory.createLineBorder(new Color(14, 74, 17), 5);
        private final Color blueBackground = new Color(0, 71, 210, 90);
        private final Color redBackground = new Color(180, 23, 23, 90);
        private final Color greenBackground = new Color(5, 138, 15);

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
                        if (isGameOverScenario(GameFrame.get().getChessBoard())) {
                            deselectLeftMouseButton();
                            return;
                        }
                        if (replayMovesInProgress) {
                            JOptionPane.showMessageDialog(GameFrame.get().getBoardPanel(),
                                    "Replay is in progress. Please wait.");
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
                                chessBoard = transition.getToBoard();
                                playerPanel.redo(chessBoard);
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
                    if (!animationInProgress && !replayMovesInProgress && (border == null || !(border.equals(blueSelectedBorder)
                            || border.equals(redSelectedBorder)
                            || border.equals(capturedPieceBorder)))) {
                        if (GameFrame.get().getGameConfiguration().getBluePlayerType() == PlayerType.HUMAN
                                && GameFrame.get().getGameConfiguration().getRedPlayerType() == PlayerType.HUMAN) {
                            if (GameFrame.get().getChessBoard().getCurrentPlayer().getAllyColor().isBlue()) {
                                setBorder(blueHoveringBorder);
                            } else if (GameFrame.get().getChessBoard().getCurrentPlayer().getAllyColor().isRed()) {
                                setBorder(redHoveringBorder);
                            }
                        } else if (GameFrame.get().getGameConfiguration().getBluePlayerType() == PlayerType.HUMAN
                                && GameFrame.get().getGameConfiguration().getRedPlayerType() == PlayerType.AI) {
                            setBorder(blueHoveringBorder);
                        } else if (GameFrame.get().getGameConfiguration().getBluePlayerType() == PlayerType.AI
                                && GameFrame.get().getGameConfiguration().getRedPlayerType() == PlayerType.HUMAN) {
                            setBorder(redHoveringBorder);
                        }
                    }
                }

                @Override
                public void mouseExited(final MouseEvent e) {
                    Border border = getBorder();
                    if (border != null && border == blueHoveringBorder || border == redHoveringBorder) {
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
            if (thisBorder == null && otherBorder == null) {
                return true;
            }
            if (thisBorder != null && otherBorder != null) {
                if (thisBorder.equals(otherBorder)) {
                    return true;
                }
                if (thisBorder instanceof LineBorder && otherBorder instanceof LineBorder) {
                    Color thisBorderColor = ((LineBorder) thisBorder).getLineColor();
                    Color otherBorderColor = ((LineBorder) otherBorder).getLineColor();
                    int thisBorderThickness = ((LineBorder) thisBorder).getThickness();
                    int otherBorderThickness = ((LineBorder) otherBorder).getThickness();
                    return thisBorderColor.equals(otherBorderColor) && thisBorderThickness == otherBorderThickness;
                }
            }
            return false;
        }


        public void drawTerrain(final Board board) {
            assignTerrainColor(terrainCoordinate);
            assignTerrainPieceIcon(board);
            drawSelectedBorder();
            highlightLastMove();
            if (glitchMode) {
                highlightValidMovesGlitchMode(board);
            } else {
                highlightValidMoves(board);
            }
            validate();
            repaint();
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
                    assert imagePath != null;
                    image = ImageIO.read(new File(imagePath));
                    ImageIcon icon = new ImageIcon(image);
                    int labelWidthAndHeight = 65;
                    Image scaledImage = icon.getImage().getScaledInstance(labelWidthAndHeight, labelWidthAndHeight, Image.SCALE_SMOOTH);
                    icon = new ImageIcon(scaledImage);
                    JLabel label = new JLabel(icon);
                    add(label);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void drawSelectedBorder() {
            if (humanMovedPiece != null
                    && humanMovedPiece.getPieceColor() == chessBoard.getCurrentPlayer().getAllyColor()
                    && humanMovedPiece.getPieceCoordinate() == this.terrainCoordinate) {
                if (chessBoard.getCurrentPlayer().getAllyColor().isBlue()) {
                    setBorder(blueSelectedBorder);
                    setBackground(blueBackground);
                } else if (chessBoard.getCurrentPlayer().getAllyColor().isRed()) {
                    setBorder(redSelectedBorder);
                    setBackground(redBackground);
                }
                setOpaque(true);
            } else {
                setBorder(BorderFactory.createEmptyBorder());
            }
        }

        private void highlightLastMove() {
            if (lastMove != null) {
                if (this.terrainCoordinate == lastMove.getCurrentCoordinate() || this.terrainCoordinate == lastMove.getDestinationCoordinate()) {
                    if (chessBoard.getCurrentPlayer().getAllyColor().isRed()) {
                        setBorder(blueSelectedBorder);
                        setBackground(blueBackground);
                    } else if (chessBoard.getCurrentPlayer().getAllyColor().isBlue()) {
                        setBorder(redSelectedBorder);
                        setBackground(redBackground);
                    }
                    setOpaque(true);
                }
            }
        }

        private void highlightValidMoves(final Board board) {
            for (final Move move : selectedPieceValidMoves(board)) {
                if (!move.isCaptureMove() && move.getDestinationCoordinate() == this.terrainCoordinate) {
                    try {
                        String dotColor = GameFrame.get().getChessBoard().getCurrentPlayer().getAllyColor().isBlue() ? "blue" : "red";
                        ImageIcon dotIcon = new ImageIcon(ImageIO.read(new File(defaultImagesPath + dotColor + "dot.png")));
                        Image resizedImage = dotIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                        add(new JLabel(new ImageIcon(resizedImage)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (move.isCaptureMove() && move.getDestinationCoordinate() == this.terrainCoordinate) {
                    setBorder(BorderFactory.createEmptyBorder());
                    setBackground(new Color(0, 0, 0, 0));
                    setBorder(capturedPieceBorder);
                    setBackground(greenBackground);
                    setOpaque(true);
                }
            }
        }

        private void highlightValidMovesGlitchMode(final Board board) {
            for (final Move move : selectedPieceValidMoves(board)) {
                if (move.isCaptureMove() && move.getDestinationCoordinate() == this.terrainCoordinate
                        && firstGlitchModeEncountered) {
                    String message = "You must win without capturing your enemy's pieces!\n" +
                            "Click on the screen continuously nonstop until the glitch stops!";
                    JOptionPane.showMessageDialog(GameFrame.get().getBoardPanel(),
                            message,
                            "Welcome to Glitch Mode", JOptionPane.INFORMATION_MESSAGE);
                }
                if (!move.isCaptureMove() && move.getDestinationCoordinate() == this.terrainCoordinate) {
                    try {
                        String dotColor = GameFrame.get().getChessBoard().getCurrentPlayer().getAllyColor().isBlue() ? "blue" : "red";
                        ImageIcon dotIcon = new ImageIcon(ImageIO.read(new File(defaultImagesPath + dotColor + "dot.png")));
                        Image resizedImage = dotIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                        add(new JLabel(new ImageIcon(resizedImage)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (move.isCaptureMove() && move.getDestinationCoordinate() == this.terrainCoordinate) {
                    setFirstGlitchModeEncountered(false);
                    setBorder(capturedPieceBorder);
                    setBackground(new Color(12, 211, 28, 90));
                    setOpaque(true);
                    SwingWorker<Void, Void> worker = new SwingWorker<>() {
                        @Override
                        protected Void doInBackground() {
                            setBorder(null);
                            setBackground(null);
                            GameFrame.get().getBoardPanel().drawBoard(chessBoard);
                            return null;
                        }

                        @Override
                        protected void done() {
                            setBorder(capturedPieceBorder);
                            setBackground(new Color(5, 138, 15));
                            setOpaque(true);
                        }
                    };
                    worker.execute();
                }
            }
        }

        private Collection<Move> selectedPieceValidMoves(final Board board) {
            if (humanMovedPiece != null && humanMovedPiece.getPieceColor() == board.getCurrentPlayer().getAllyColor()) {
                return humanMovedPiece.determineValidMoves(board);
            }
            return Collections.emptyList();
        }

        public void deselectLeftMouseButton() {
            sourceTerrain = null;
            humanMovedPiece = null;
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
                    !isGameOverScenario(GameFrame.get().getChessBoard())) {
                final IntelligenceHub intelligenceHub = new IntelligenceHub();
                intelligenceHub.execute();
            }
        }
    }

    public static class IntelligenceHub extends SwingWorker<Move, String> {
        boolean isMinimaxRunning = false;

        @Override
        protected Move doInBackground() {
            if (DifficultyFrame.getDifficulty() == DifficultyFrame.Difficulty.EASY) {
                isMinimaxRunning = true;
                final MoveStrategy minimax = new MinimaxAlgorithm(4, PoorBoardEvaluator.get());
                System.out.println(PoorBoardEvaluator.get().evaluationDetails(GameFrame.get().getChessBoard(), GameFrame.get().gameConfiguration.getSearchDepth()));
                return minimax.execute(GameFrame.get().getChessBoard());
            }
            if (DifficultyFrame.getDifficulty() == DifficultyFrame.Difficulty.MEDIUM) {
                isMinimaxRunning = true;
                final MoveStrategy minimax = new MinimaxAlgorithm(3, ConcreteBoardEvaluator.get());
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
                GameFrame.get().updateGameBoard(GameFrame.get().getChessBoard().getCurrentPlayer().makeMove(optimalMove).getToBoard());
                GameFrame.get().getMoveLog().addMove(optimalMove);
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
        restartGame();
        if (!MainMenu.get().isGrayScaleBGMButton()) {
            AudioPlayer.LoopPlayer.playGameBGM();
        }
        GameFrame.get().setVisible(true);
    }

    public void restartGame() {
        chessBoard = Board.constructStandardBoard();
        GameFrame.get().setNormalModeGameOver(false);
        GameFrame.get().setBlitzModeGameOver(false);
        gameResigned = false;
        moveLog.clear();
        lastMove = null;
        computerMove = null;
        boardPanel.removeAllBorders();
        boardPanel.removeAllBackgrounds();
        boardPanel.drawBoard(chessBoard);
        playerPanel.reset();
        capturedPiecesPanel.reset();
        GameFrame.get().getGameConfiguration().setReady(false);
        setChanged();
        notifyObservers();
    }

    public void checkWin() {
        if (GameFrame.get().getChessBoard().getCurrentPlayer().isDenPenetrated()) {
            if (!GameFrame.get().isBlitzMode()) {
                GameFrame.get().setNormalModeGameOver(true);
            }
            if (!GameFrame.get().isBlitzMode()
                    && GameFrame.get().getPlayerPanel().isNormalModeWithTimer()) {
                GameFrame.get().getPlayerPanel().getTimerNormalMode().stop();
            }
            if (GameFrame.get().isBlitzMode()) {
                GameFrame.get().setBlitzModeGameOver(true);
                GameFrame.get().getPlayerPanel().getBlueTimerBlitzMode().stop();
                GameFrame.get().getPlayerPanel().getRedTimerBlitzMode().stop();
            }
            Controller.handleWinningStateForDenPenetratedCondition();
            return;
        }
        if (GameFrame.get().getChessBoard().getCurrentPlayer().getActivePieces().isEmpty()) {
            if (!GameFrame.get().isBlitzMode()) {
                GameFrame.get().setNormalModeGameOver(true);
            }
            if (!GameFrame.get().isBlitzMode()
                    && GameFrame.get().getPlayerPanel().isNormalModeWithTimer()) {
                GameFrame.get().getPlayerPanel().getTimerNormalMode().stop();
            }
            if (GameFrame.get().isBlitzMode()) {
                GameFrame.get().setBlitzModeGameOver(true);
                GameFrame.get().getPlayerPanel().getBlueTimerBlitzMode().stop();
                GameFrame.get().getPlayerPanel().getRedTimerBlitzMode().stop();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (GameFrame.get().getGameConfiguration().getBluePlayerType() == GameFrame.get().gameConfiguration.getRedPlayerType()
                    || GameFrame.get().getGameConfiguration().isAIPlayer(GameFrame.get().getChessBoard().getCurrentPlayer())) {
                AudioPlayer.SinglePlayer.playSoundEffect("winning.wav");
            } else {
                AudioPlayer.SinglePlayer.playSoundEffect("losing.wav");
            }
            Controller.handleWinningStateForHavingNoMoreActivePiecesCondition();
            return;
        }
        if (GameFrame.get().getChessBoard().getCurrentPlayer().getValidMoves().isEmpty()) {
            if (!GameFrame.get().isBlitzMode()) {
                GameFrame.get().setNormalModeGameOver(true);
            }
            if (!GameFrame.get().isBlitzMode()
                    && GameFrame.get().getPlayerPanel().isNormalModeWithTimer()) {
                GameFrame.get().getPlayerPanel().getTimerNormalMode().stop();
            }
            if (GameFrame.get().isBlitzMode()) {
                GameFrame.get().setBlitzModeGameOver(true);
                GameFrame.get().getPlayerPanel().getBlueTimerBlitzMode().stop();
                GameFrame.get().getPlayerPanel().getRedTimerBlitzMode().stop();
            }
            Controller.handleWinningStateForHavingNoMoreValidMovesConditions();
        }
    }

    public static boolean isGameOverScenario(final Board board) {
        return board.getCurrentPlayer().isDenPenetrated()
                || board.getCurrentPlayer().getActivePieces().isEmpty()
                || board.getCurrentPlayer().getValidMoves().isEmpty()
                || GameFrame.get().getPlayerPanel().getBlueCurrentTimerSecondsBlitzMode() == 0
                || GameFrame.get().getPlayerPanel().getRedCurrentTimerSecondsBlitzMode() == 0
                || GameFrame.get().isGameResigned();
    }

    public void setLoadBoard(Board loadBoard, MoveLog loadMoveLog, int roundNumber) {
        this.chessBoard = loadBoard;
        this.moveLog = loadMoveLog;
        boardPanel.drawBoard(chessBoard);
        boardPanel.removeAllBorders();
        capturedPiecesPanel.redo(moveLog);
        playerPanel.setRoundNumber(roundNumber);
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

    public boolean isAnimationInProgress() {
        return animationInProgress;
    }

    public void setAnimationInProgress(boolean animationInProgress) {
        this.animationInProgress = animationInProgress;
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

    public void setNormalModeGameOver(boolean normalModeGameOver) {
        this.normalModeGameOver = normalModeGameOver;
    }

    public void setGlitchMode(boolean glitchMode) {
        this.glitchMode = glitchMode;
    }

    public void setFirstGlitchModeEncountered(boolean firstGlitchModeEncountered) {
        this.firstGlitchModeEncountered = firstGlitchModeEncountered;
    }

    public boolean isBlitzMode() {
        return blitzMode;
    }

    public void setBlitzMode(boolean blitzMode) {
        this.blitzMode = blitzMode;
    }

    public void setBlitzModeGameOver(boolean blitzModeGameOver) {
        this.blitzModeGameOver = blitzModeGameOver;
    }

    public boolean isGameResigned() {
        return gameResigned;
    }

    public void setGameResigned(boolean gameResigned) {
        this.gameResigned = gameResigned;
    }

    public static GameFrame get() {
        return INSTANCE;
    }
}
