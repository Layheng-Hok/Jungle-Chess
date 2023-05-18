package view;

import model.artificialintelligence.ConcreteBoardEvaluator;
import model.artificialintelligence.MinimaxAlgorithm;
import model.artificialintelligence.PoorBoardEvaluator;
import model.artificialintelligence.Strategy;
import model.board.Board;
import model.board.Utilities;
import model.board.Move;
import model.piece.Piece;
import model.board.MoveTransition;
import model.player.PlayerColor;
import model.player.PlayerType;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class GameFrame extends Observable {
    private final JFrame gameFrame;
    private final LeftPanel leftPanel;
    private final RightPanel rightPanel;
    private final PlayerPanel playerPanel;
    private final CapturedPiecesPanel capturedPiecesPanel;
    private final BoardPanel boardPanel;
    private final AIGameConfiguration AIGameConfiguration;
    private MoveLog moveLog;
    private Board chessBoard;
    private Move computerMove;
    private Piece sourceTerrain;
    private Piece humanMovedPiece;
    private BoardDirection boardDirection;
    private boolean isBoard1 = true;
    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(530, 850);
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(500, 650);
    private static final Dimension TERRAIN_PANEL_DIMENSION = new Dimension(10, 10);
    private final ImageIcon logo = new ImageIcon(defaultImagesPath + "junglechesslogo.jpg");
    public static final String defaultImagesPath = "resource/images/";
    private static final GameFrame INSTANCE = new GameFrame();

    public GameFrame() {
        this.gameFrame = new JFrame("Jungle Chess (斗兽棋)");
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
        this.addObserver(new GameAIObserver());
        this.AIGameConfiguration = new AIGameConfiguration(this.gameFrame, true);
        this.boardDirection = BoardDirection.NORMAL;
        this.gameFrame.add(this.leftPanel, BorderLayout.WEST);
        this.gameFrame.add(this.rightPanel, BorderLayout.EAST);
        this.gameFrame.add(this.playerPanel, BorderLayout.NORTH);
        this.gameFrame.add(this.capturedPiecesPanel, BorderLayout.SOUTH);
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.setIconImage(logo.getImage());
        this.gameFrame.setVisible(true);
        this.gameFrame.setResizable(false);
    }

    private JMenuBar createGameFrameMenuBar() {
        final JMenuBar gameFrameMenuBar = new JMenuBar();
        gameFrameMenuBar.add(createSettingMenu());
        return gameFrameMenuBar;
    }

    private JMenu createSettingMenu() {
        final JMenu settingMenu = new JMenu("| | |");

        final JMenuItem save = new JMenuItem("Save Game");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String path = JOptionPane.showInputDialog("File Name");
                while (path.equals("")) {
                    JOptionPane.showMessageDialog(null, "Name cannot be empty. Please enter again.");
                    path = JOptionPane.showInputDialog("File Name");
                }
                System.out.println("Game Saved");
            }
        });
        settingMenu.add(save);

        final JMenuItem restart = new JMenuItem("Restart");
        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chessBoard = Board.constructStandardBoard();
                boardPanel.drawBoard(chessBoard);
                playerPanel.reset();
                capturedPiecesPanel.reset();
                moveLog.clear();
                System.out.println("Game Restarted");
            }
        });
        settingMenu.add(restart);

        final JMenuItem undo = new JMenuItem("Undo");
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (moveLog.size() > 0) {
                    Move lastMove = moveLog.removeMove(moveLog.size() - 1);
                    chessBoard = lastMove.undo();
                    boardPanel.drawBoard(chessBoard);
                    playerPanel.undo();
                    capturedPiecesPanel.redo(moveLog);
                    System.out.println("Undo");
                }
            }
        });
        settingMenu.add(undo);

        final JMenuItem replayMoveLog = new JMenuItem("Replay Previous Moves");
        replayMoveLog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerPanel.setRoundNumber(1);
                capturedPiecesPanel.reset();
                MoveLog seperateMoveLog = new MoveLog();
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        chessBoard = Board.constructStandardBoard();
                        boardPanel.drawBoard(chessBoard);
                        Thread.sleep(1000);
                        for (int i = 0; i < moveLog.size(); i++) {
                            Move move = moveLog.getMove(i);
                            chessBoard = move.execute();
                            seperateMoveLog.addMove(move);
                            publish();
                            Thread.sleep(1000);
                        }
                        return null;
                    }

                    @Override
                    protected void process(List<Void> chunks) {
                        boardPanel.drawBoard(chessBoard);
                        playerPanel.redo(chessBoard);
                        capturedPiecesPanel.redo(seperateMoveLog);
                    }

                    @Override
                    protected void done() {
                        if (AIGameConfiguration.isAIPlayer(chessBoard.getCurrentPlayer())) {
                            GameFrame.get().moveMadeUpdate(PlayerType.HUMAN);
                        }
                        boardPanel.drawBoard(chessBoard);
                    }
                };
                worker.execute();
                System.out.println("Replay Previous Moves");
            }
        });
        settingMenu.add(replayMoveLog);

        final JMenuItem changeBoardMenuItem = new JMenuItem("Change Board");
        changeBoardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isBoard1 = !isBoard1;
                String boardImageFileName = isBoard1 ? "chessboard1.png" : "chessboard2.png";
                boardPanel.setBoardImage(boardImageFileName);
                boardPanel.drawBoard(chessBoard);
                System.out.println("Board Changed");
            }
        });
        settingMenu.add(changeBoardMenuItem);

        final JMenuItem changeSideMenuItem = new JMenuItem("Change Side");
        changeSideMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardDirection = boardDirection.opposite();
                boardPanel.drawBoard(chessBoard);
                System.out.println("Side Changed");
            }
        });
        settingMenu.add(changeSideMenuItem);

        final JMenuItem backMenuItem = new JMenuItem("Back To Main Menu");
        backMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restart.doClick();
                GameFrame.get().dispose();
                new MainMenu().setVisible(true);
                System.out.println("Back To Main Menu");
            }
        });
        settingMenu.add(backMenuItem);

        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameFrame.dispose();
                System.exit(0);
            }
        });
        settingMenu.add(exitMenuItem);
        return settingMenu;
    }

    private class BoardPanel extends JPanel {
        final List<TerrainPanel> boardTerrains;
        private Image boardImage;

        BoardPanel() {
            super(new GridLayout(9, 7));
            this.boardTerrains = new ArrayList<>();
            setBoardImage("chessboard1.png");
            for (int i = 0; i < Utilities.NUM_TERRAINS; i++) {
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
                terrainPanel.drawEmptyTerrainBorder();
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

    private class TerrainPanel extends JPanel {
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
                        if (GameFrame.get().getGameConfiguration().isAIPlayer(GameFrame.get().getChessBoard().getCurrentPlayer())) {
                            return;
                        }
                        if (sourceTerrain == null) {
                            sourceTerrain = chessBoard.getPiece(terrainCoordinate);
                            if (sourceTerrain != null && sourceTerrain.getPieceColor() != chessBoard.getCurrentPlayer().getAllyColor()) {
                                sourceTerrain = null;
                            } else {
                                humanMovedPiece = sourceTerrain;
                            }
                        } else {
                            final Move move = Move.MoveCreator.createMove(chessBoard, sourceTerrain.getPieceCoordinate(), terrainCoordinate);
                            final MoveTransition transition = chessBoard.getCurrentPlayer().makeMove(move);
                            if (transition.getMoveStatus().isDone()) {
                                chessBoard = transition.getTransitionBoard();
                                playerPanel.setCurrentPlayer(chessBoard.getCurrentPlayer().toString());
                                if (chessBoard.getCurrentPlayer().getAllyColor() == PlayerColor.BLUE) {
                                    playerPanel.setRoundNumber(playerPanel.getRoundNumber() + 1);
                                }
                                playerPanel.repaint();
                                moveLog.addMove(move);
                            }
                            sourceTerrain = null;
                            humanMovedPiece = null;
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                capturedPiecesPanel.redo(moveLog);
                                if (AIGameConfiguration.isAIPlayer(chessBoard.getCurrentPlayer())) {
                                    GameFrame.get().moveMadeUpdate(PlayerType.HUMAN);
                                }
                                boardPanel.drawBoard(chessBoard);
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
                }

                @Override
                public void mouseExited(final MouseEvent e) {
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

        public void drawEmptyTerrainBorder() {
            setBorder(BorderFactory.createEmptyBorder());
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
                    setBorder(BorderFactory.createLineBorder(Color.PINK, 2));
                } else if (this.terrainCoordinate == computerMove.getDestinationCoordinate()) {
                    setBorder(BorderFactory.createLineBorder(Color.RED, 2));
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
            if (Utilities.isLand(coordinate)) {
                setBackground(new Color(0x28B463));
            } else if (Utilities.isRiver(coordinate)) {
                setBackground(new Color(0x63B8FF));
            } else if (Utilities.isEnemyTrap(coordinate, PlayerColor.BLUE)) {
                setBackground(new Color(0xE67E22));
            } else if (Utilities.isEnemyTrap(coordinate, PlayerColor.RED)) {
                setBackground(new Color(0xE67E22));
            } else if (Utilities.isDen(coordinate, PlayerColor.BLUE)) {
                setBackground(new Color(0x3498DB));
            } else if (Utilities.isDen(coordinate, PlayerColor.RED)) {
                setBackground(new Color(0xEC7063));
            }
            setOpaque(false);
        }
    }

    enum BoardDirection {
        NORMAL {
            @Override
            List<TerrainPanel> traverse(final List<TerrainPanel> boardTiles) {
                return boardTiles;
            }

            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED {
            @Override
            List<TerrainPanel> traverse(final List<TerrainPanel> boardTiles) {
                List<TerrainPanel> reversedTiles = new ArrayList<>(boardTiles);
                Collections.reverse(reversedTiles);
                return reversedTiles;
            }

            @Override
            BoardDirection opposite() {
                return NORMAL;
            }
        };

        abstract List<TerrainPanel> traverse(final List<TerrainPanel> boardTiles);

        abstract BoardDirection opposite();

    }

    public static class MoveLog {
        private final List<Move> moves;

        MoveLog() {
            this.moves = new ArrayList<>();
        }

        public List<Move> getMoves() {
            return this.moves;
        }

        void addMove(final Move move) {
            this.moves.add(move);
        }

        public int size() {
            return this.moves.size();
        }

        void clear() {
            this.moves.clear();
        }

        Move removeMove(final int index) {
            return this.moves.remove(index);
        }

        boolean removeMove(final Move move) {
            return this.moves.remove(move);
        }

        public Move getMove(int i) {
            return this.moves.get(i);
        }
    }

    public void setupUpdate(final AIGameConfiguration AIGameConfiguration) {
        setChanged();
        notifyObservers(AIGameConfiguration);
    }

    private void moveMadeUpdate(final PlayerType playerType) {
        setChanged();
        notifyObservers(playerType);
    }

    private void updateGameBoard(final Board board) {
        this.chessBoard = board;
    }

    private void updateComputerMove(final Move move) {
        this.computerMove = move;
    }

    public static class GameAIObserver implements Observer {
        @Override
        public void update(final Observable o, final Object arg) {
            if (GameFrame.get().AIGameConfiguration.isAIPlayer(GameFrame.get().getChessBoard().getCurrentPlayer()) &&
                    !GameFrame.get().getChessBoard().getCurrentPlayer().isDenPenetrated()) {
                final IntelligenceHub intelligenceHub = new IntelligenceHub();
                intelligenceHub.execute();
            }
            if (GameFrame.get().getChessBoard().getCurrentPlayer().isDenPenetrated()) {
                System.out.println("Game Over, " + GameFrame.get().getChessBoard().getCurrentPlayer() + "'s den is penetrated by the enemy!");
            }
            if (GameFrame.get().getChessBoard().getCurrentPlayer().getActivePieces() == null) {
                System.out.println("Game Over, " + GameFrame.get().getChessBoard().getCurrentPlayer() + " has no more pieces!");
            }
        }
    }

    public static class IntelligenceHub extends SwingWorker<Move, String> {
        private IntelligenceHub() {
        }

        @Override
        protected Move doInBackground() throws Exception {
            if (DifficultyFrame.getDifficulty().equals("easy")) {
                final Strategy minimax = new MinimaxAlgorithm(4, PoorBoardEvaluator.get());
                System.out.println(PoorBoardEvaluator.get().evaluationDetails(GameFrame.get().getChessBoard(), GameFrame.get().AIGameConfiguration.getSearchDepth()));
                return minimax.execute(GameFrame.get().getChessBoard());
            }
            if (DifficultyFrame.getDifficulty().equals("medium")) {
                final Strategy minimax = new MinimaxAlgorithm(4, ConcreteBoardEvaluator.get());
                System.out.println(ConcreteBoardEvaluator.get().evaluationDetails(GameFrame.get().getChessBoard(), GameFrame.get().AIGameConfiguration.getSearchDepth()));
                return minimax.execute(GameFrame.get().getChessBoard());
            }
            if (DifficultyFrame.getDifficulty().equals("hard")) {
                final Strategy minimax = new MinimaxAlgorithm(5, ConcreteBoardEvaluator.get());
                System.out.println(ConcreteBoardEvaluator.get().evaluationDetails(GameFrame.get().getChessBoard(), GameFrame.get().AIGameConfiguration.getSearchDepth()));
                return minimax.execute(GameFrame.get().getChessBoard());
            }
            return null;
        }

        @Override
        public void done() {
            try {
                final Move optimalMove = get();
                GameFrame.get().updateComputerMove(optimalMove);
                GameFrame.get().updateGameBoard(GameFrame.get().getChessBoard().getCurrentPlayer().makeMove(optimalMove).getTransitionBoard());
                GameFrame.get().getMoveLog().addMove(optimalMove);
                GameFrame.get().getPlayerPanel().setCurrentPlayer(GameFrame.get().getChessBoard().getCurrentPlayer().toString());
                if (GameFrame.get().getChessBoard().getCurrentPlayer().getAllyColor() == PlayerColor.BLUE) {
                    GameFrame.get().getPlayerPanel().setRoundNumber(GameFrame.get().getPlayerPanel().getRoundNumber() + 1);
                }
                GameFrame.get().getPlayerPanel().repaint();
                GameFrame.get().getCapturedPiecesPanel().redo(GameFrame.get().getMoveLog());
                GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
                GameFrame.get().moveMadeUpdate(PlayerType.COMPUTER);
            } catch (final Exception e) {
                e.printStackTrace();
            }
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
    }

    public AIGameConfiguration getGameConfiguration() {
        return this.AIGameConfiguration;
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

    public MoveLog getMoveLog() {
        return moveLog;
    }

    public void setVisible(boolean b) {
        this.gameFrame.setVisible(b);
    }

    public void dispose() {
        gameFrame.dispose();
    }
}
