package view;

import model.board.Board;
import model.board.BoardUtils;
import model.board.Move;
import model.piece.Piece;
import model.board.MoveTransition;
import model.player.PlayerColor;

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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class GameFrame {
    private final JFrame gameFrame;
    private final LeftPanel leftPanel;
    private final RightPanel rightPanel;
    private final PlayerPanel playerPanel;
    private final CapturedPiecesPanel capturedPiecesPanel;
    private final MoveLog moveLog;
    private final BoardPanel boardPanel;
    private Board chessBoard;
    private Piece sourceTerrain;
    private Piece humanMovedPiece;
    private BoardDirection boardDirection;
    private boolean highlightValidMoves;
    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(530, 850);
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(500, 650);
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);
    private final ImageIcon logo = new ImageIcon(defaultImagesPath + "junglechesslogo.jpg");
    public static final String defaultImagesPath = "resource/images/";

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
        this.boardDirection = BoardDirection.NORMAL;
        this.gameFrame.add(this.leftPanel, BorderLayout.WEST);
        this.gameFrame.add(this.rightPanel, BorderLayout.EAST);
        this.gameFrame.add(this.playerPanel, BorderLayout.NORTH);
        this.gameFrame.add(this.capturedPiecesPanel, BorderLayout.SOUTH);
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.setIconImage(logo.getImage());
        this.highlightValidMoves = true;
        this.gameFrame.setVisible(true);
        this.gameFrame.setResizable(false);
    }

    private JMenuBar createGameFrameMenuBar() {
        final JMenuBar gameFrameMenuBar = new JMenuBar();
        gameFrameMenuBar.add(createFileMenu());
        gameFrameMenuBar.add(createPreferencesMenu());
        return gameFrameMenuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPGN = new JMenuItem("Load PGN File");
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Open up that pgn file!");
            }
        });
        fileMenu.add(openPGN);
        final JMenuItem exitMenuItem = new JMenuItem("Save & Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameFrame.dispose();
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);
        return fileMenu;
    }

    private JMenu createPreferencesMenu() {
        final JMenu preferencesMenu = new JMenu("Preferences");
        final JMenuItem flipBoardMenuItem = new JMenuItem("Change Side");
        flipBoardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardDirection = boardDirection.opposite();
                boardPanel.drawBoard(chessBoard);
            }
        });
        preferencesMenu.add(flipBoardMenuItem);
        preferencesMenu.addSeparator();
        final JCheckBoxMenuItem validMoveHighlighterCheckbox = new JCheckBoxMenuItem("Highlight Valid Moves", true);
        validMoveHighlighterCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highlightValidMoves = validMoveHighlighterCheckbox.isSelected();
            }
        });
        preferencesMenu.add(validMoveHighlighterCheckbox);
        return preferencesMenu;
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

    private class BoardPanel extends JPanel {
        final List<TerrainPanel> boardTerrains;
        private final Image boardImage;

        BoardPanel() {
            super(new GridLayout(9, 7));
            this.boardTerrains = new ArrayList<>();
            this.boardImage = new ImageIcon(defaultImagesPath + "chessboard1.png").getImage();
            for (int i = 0; i < BoardUtils.NUM_TERRAINS; i++) {
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

    }

    private class TerrainPanel extends JPanel {
        private final int terrainCoordinate;

        TerrainPanel(final BoardPanel boardPanel, final int terrainCoordinate) {
            super(new GridBagLayout());
            this.terrainCoordinate = terrainCoordinate;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTerrainColor(terrainCoordinate);
            assignTerrainPieceIcon(chessBoard);
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    if (isRightMouseButton(e)) {
                        sourceTerrain = null;
                        humanMovedPiece = null;
                    } else if (isLeftMouseButton(e)) {
                        if (sourceTerrain == null) {
                            sourceTerrain = chessBoard.getPiece(terrainCoordinate);
                            if (sourceTerrain != null && sourceTerrain.getPieceColor() != chessBoard.currentPlayer().getAllyColor()) {

                                sourceTerrain = null;
                            } else {
                                humanMovedPiece = sourceTerrain;
                            }
                        } else {
                            final Move move = Move.MoveCreator.createMove(chessBoard, sourceTerrain.getPieceCoordinate(), terrainCoordinate);
                            final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                            if (transition.getMoveStatus().isDone()) {
                                chessBoard = transition.getTransitionBoard();
                                moveLog.addMove(move);
                            }
                            sourceTerrain = null;
                            humanMovedPiece = null;
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                capturedPiecesPanel.redo(moveLog);
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
            highlightValidMoves(board);
            validate();
            repaint();
        }

        private void highlightValidMoves(final Board board) {
            if (highlightValidMoves) {
                for (final Move move : pieceValidMoves(board)) {
                    if (move.getDestinationCoordinate() == this.terrainCoordinate) {
                        try {
                            add(new JLabel(new ImageIcon(ImageIO.read(new File(defaultImagesPath + "yellowdot.png")))));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        private Collection<Move> pieceValidMoves(final Board board) {
            if (humanMovedPiece != null && humanMovedPiece.getPieceColor() == board.currentPlayer().getAllyColor()) {
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
            if (BoardUtils.isLand(coordinate)) {
                setBackground(new Color(0x28B463));
            } else if (BoardUtils.isRiver(coordinate)) {
                setBackground(new Color(0x63B8FF));
            } else if (BoardUtils.isEnemyTrap(coordinate, PlayerColor.BLUE)) {
                setBackground(new Color(0xE67E22));
            } else if (BoardUtils.isEnemyTrap(coordinate, PlayerColor.RED)) {
                setBackground(new Color(0xE67E22));
            } else if (BoardUtils.isDen(coordinate, PlayerColor.BLUE)) {
                setBackground(new Color(0x3498DB));
            } else if (BoardUtils.isDen(coordinate, PlayerColor.RED)) {
                setBackground(new Color(0xEC7063));
            }
            // setBorder(BorderFactory.createLineBorder(Color.BLACK));
            setOpaque(false);
        }
    }
}
