package view;

import model.artificialintelligence.ConcreteBoardEvaluator;
import model.artificialintelligence.MinimaxAlgorithm;
import model.artificialintelligence.PoorBoardEvaluator;
import model.artificialintelligence.Strategy;
import model.board.Board;
import model.board.Move;
import model.board.BoardTransition;
import model.board.Utilities;
import model.piece.Piece;
import model.player.PlayerColor;
import model.player.PlayerType;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.*;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

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
    private BoardDirection boardDirection;
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
        this.boardDirection = BoardDirection.NORMAL;
        this.gameFrame.add(this.leftPanel, BorderLayout.WEST);
        this.gameFrame.add(this.rightPanel, BorderLayout.EAST);
        this.gameFrame.add(this.playerPanel, BorderLayout.NORTH);
        this.gameFrame.add(this.capturedPiecesPanel, BorderLayout.SOUTH);
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.setIconImage(logo.getImage());
        this.gameFrame.setResizable(false);
    }

    private JMenuBar createGameFrameMenuBar() {
        final JMenuBar gameFrameMenuBar = new JMenuBar();
        gameFrameMenuBar.add(createSettingMenu());
        return gameFrameMenuBar;
    }

    private JMenu createSettingMenu() {
        final JMenu settingMenu = new JMenu("Alt + S to open Setting Menu");
        settingMenu.setMnemonic(KeyEvent.VK_S);

        final JMenuItem saveMenuItem = new JMenuItem("Save Game");
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (replayMovesInProgress) {
                    JOptionPane.showMessageDialog(null, "Replay is in progress. Please wait.");
                    return;
                }
                if (GameFrame.get().gameConfiguration.isAIPlayer(chessBoard.getCurrentPlayer())) {
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
        });
        saveMenuItem.setMnemonic(KeyEvent.VK_S);
        settingMenu.add(saveMenuItem);

        final JMenuItem restartMenuItem = new JMenuItem("Restart");
        restartMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (replayMovesInProgress) {
                    JOptionPane.showMessageDialog(null, "Replay is in progress. Please wait.");
                    return;
                }
                if (GameFrame.get().gameConfiguration.isAIPlayer(chessBoard.getCurrentPlayer())) {
                    JOptionPane.showMessageDialog(null, "AI is still thinking. Please wait.");
                    return;
                }
                restartGame();
                System.out.println("Game Restarted");
            }
        });
        restartMenuItem.setMnemonic(KeyEvent.VK_R);
        restartMenuItem.setMnemonic(KeyEvent.VK_R);
        settingMenu.add(restartMenuItem);

        final JMenuItem undoMenuItem = new JMenuItem("Undo");
        undoMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (replayMovesInProgress) {
                    JOptionPane.showMessageDialog(null, "Replay is in progress. Please wait.");
                    return;
                }
                if (GameFrame.get().gameConfiguration.isAIPlayer(chessBoard.getCurrentPlayer())) {
                    JOptionPane.showMessageDialog(null, "AI is still thinking. Please wait.");
                    return;
                }
                if (GameFrame.get().getGameConfiguration().getBluePlayerType() == PlayerType.AI &&
                        GameFrame.get().getGameConfiguration().getRedPlayerType() == PlayerType.HUMAN) {
                    if (moveLog.size() > 0) {
                        Move lastMove = moveLog.removeMove(moveLog.size() - 1);
                        chessBoard = lastMove.undo();
                        if (lastMove == computerMove && moveLog.size() > 0) {
                            Move secondLastMove = moveLog.removeMove(moveLog.size() - 1);
                            chessBoard = secondLastMove.undo();
                            computerMove = moveLog.getMove(moveLog.size() - 1);
                        } else {
                            restartGame();
                            return;
                        }
                        playerPanel.undo();
                        boardPanel.drawBoard(chessBoard);
                        capturedPiecesPanel.redo(moveLog);
                        System.out.println("Undo");
                    }
                } else if (GameFrame.get().getGameConfiguration().getBluePlayerType() == PlayerType.HUMAN && GameFrame.get().getGameConfiguration().getRedPlayerType() == PlayerType.AI
                        || GameFrame.get().getGameConfiguration().getBluePlayerType() == PlayerType.HUMAN && GameFrame.get().getGameConfiguration().getRedPlayerType() == PlayerType.HUMAN) {
                    if (moveLog.size() > 0) {
                        Move lastMove = moveLog.removeMove(moveLog.size() - 1);
                        chessBoard = lastMove.undo();
                        if (lastMove == computerMove) {
                            Move secondLastMove = moveLog.removeMove(moveLog.size() - 1);
                            chessBoard = secondLastMove.undo();
                            if (moveLog.size() > 0) {
                                computerMove = moveLog.getMove(moveLog.size() - 1);
                            } else {
                                computerMove = null;
                            }
                            playerPanel.undo();
                            setChanged();
                            notifyObservers();
                        }
                        boardPanel.drawBoard(chessBoard);
                        playerPanel.undo();
                        capturedPiecesPanel.redo(moveLog);
                        System.out.println("Undo");
                    }
                }
            }
        });
        undoMenuItem.setMnemonic(KeyEvent.VK_U);
        settingMenu.add(undoMenuItem);

        final JMenuItem replayAllMovesMenuItem = new JMenuItem("Playback Moves");
        replayAllMovesMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (replayMovesInProgress) {
                    JOptionPane.showMessageDialog(null, "Replay is already in progress. Please wait for the next replay.");
                    return;
                }
                if (GameFrame.get().gameConfiguration.isAIPlayer(chessBoard.getCurrentPlayer())) {
                    JOptionPane.showMessageDialog(null, "AI is still thinking. Please wait.");
                    return;
                }
                replayMovesInProgress = true;
                playerPanel.setRoundNumber(1);
                capturedPiecesPanel.reset();
                MoveLog seperateMoveLog = new MoveLog();
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        chessBoard = Board.constructStandardBoard();
                        boardPanel.drawBoard(chessBoard);
                        boardPanel.removeAllBorders();
                        Thread.sleep(1000);
                        for (int i = 0; i < moveLog.size(); i++) {
                            Move move = moveLog.getMove(i);
                            chessBoard = move.execute();
                            System.out.println(move);
                            seperateMoveLog.addMove(move);
                            computerMove = move;
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
                        if (gameConfiguration.isAIPlayer(chessBoard.getCurrentPlayer())) {
                            GameFrame.get().moveMadeUpdate(PlayerType.HUMAN);
                        }
                        boardPanel.drawBoard(chessBoard);
                        replayMovesInProgress = false;
                    }
                };
                worker.execute();
                System.out.println("Replay Previous Moves");
            }
        });
        replayAllMovesMenuItem.setMnemonic(KeyEvent.VK_P);
        settingMenu.add(replayAllMovesMenuItem);

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
        changeBoardMenuItem.setMnemonic(KeyEvent.VK_C);
        settingMenu.add(changeBoardMenuItem);

        final JMenuItem rotateBoard = new JMenuItem("Toggle Board Orientation");
        rotateBoard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardDirection = boardDirection.opposite();
                boardPanel.drawBoard(chessBoard);
                System.out.println("Board Rotated");
            }
        });
        rotateBoard.setMnemonic(KeyEvent.VK_T);
        settingMenu.add(rotateBoard);

        final JMenuItem backMenuItem = new JMenuItem("Back To Main Menu");
        backMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (GameFrame.get().gameConfiguration.isAIPlayer(chessBoard.getCurrentPlayer())) {
                    JOptionPane.showMessageDialog(null, "AI is still thinking. Please wait.");
                    return;
                }
                GameFrame.get().gameConfiguration.setBluePlayerType(PlayerType.HUMAN);
                GameFrame.get().gameConfiguration.setRedPlayerType(PlayerType.HUMAN);
                restartGame();
                GameFrame.get().dispose();
                new MainMenu().setVisible(true);
                System.out.println("Back To Main Menu");
            }
        });
        backMenuItem.setMnemonic(KeyEvent.VK_B);
        settingMenu.add(backMenuItem);

        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameFrame.dispose();
                System.exit(0);
            }
        });
        exitMenuItem.setMnemonic(KeyEvent.VK_E);
        settingMenu.addSeparator();
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
                        if (replayMovesInProgress) {
                            JOptionPane.showMessageDialog(null, "Replay is already in progress. Please wait for the next replay.");
                            return;
                        }
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
                            computerMove = null;
                            final Move move = Move.MoveCreator.createMove(chessBoard, sourceTerrain.getPieceCoordinate(), terrainCoordinate);
                            final BoardTransition transition = chessBoard.getCurrentPlayer().makeMove(move);
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

        public Move getMove(int index) {
            return this.moves.get(index);
        }
    }

    public void setupUpdate(final GameConfiguration AIGameConfiguration) {
        setChanged();
        notifyObservers(AIGameConfiguration);
    }

    private void moveMadeUpdate(final PlayerType playerType) {
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
            GameFrame.get().checkWin();
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
                final Strategy minimax = new MinimaxAlgorithm(4, PoorBoardEvaluator.get());
                System.out.println(ConcreteBoardEvaluator.get().evaluationDetails(GameFrame.get().getChessBoard(), GameFrame.get().gameConfiguration.getSearchDepth()));
                return minimax.execute(GameFrame.get().getChessBoard());
            }
            if (DifficultyFrame.getDifficulty() == DifficultyFrame.Difficulty.HARD) {
                isMinimaxRunning = true;
                final Strategy minimax = new MinimaxAlgorithm(4, ConcreteBoardEvaluator.get());
                System.out.println(ConcreteBoardEvaluator.get().evaluationDetails(GameFrame.get().getChessBoard(), GameFrame.get().gameConfiguration.getSearchDepth()));
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
                GameFrame.get().getPlayerPanel().update();
                GameFrame.get().getCapturedPiecesPanel().redo(GameFrame.get().getMoveLog());
                GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
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
        GameFrame.get().setVisible(true);
    }

    private void writeGame(String fileName) {
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
            new ProgressFrame();
            System.out.println("Game Saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public MoveLog getMoveLog() {
        return moveLog;
    }

    public void setGameBoard(final Board chessBoard) {
        this.chessBoard = chessBoard;
        boardPanel.drawBoard(chessBoard);
        capturedPiecesPanel.redo(moveLog);
        if (GameFrame.get().getGameConfiguration().isAIPlayer(chessBoard.getCurrentPlayer())) {
            moveMadeUpdate(PlayerType.HUMAN);
        }
    }

    public void setMoveLog(MoveLog moveLog) {
        this.moveLog = moveLog;
    }

    public void setVisible(boolean b) {
        this.gameFrame.setVisible(b);
    }

    public void dispose() {
        gameFrame.dispose();
    }
}
