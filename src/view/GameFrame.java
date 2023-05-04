package view;

import model.board.Board;
import model.board.BoardUtils;
import model.player.PlayerColor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameFrame {
    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private final Board chessBoard;
    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(525, 675);
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(525, 675);
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(15, 15);
    private final ImageIcon logo = new ImageIcon("resource/images/junglechesslogo.jpg");
    private static final String defaultPieceImagesPath = "resource/images/";

    public GameFrame() {
        this.gameFrame = new JFrame("Jungle Chess (斗兽棋)");
        this.gameFrame.setLayout(new BorderLayout());
        this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final JMenuBar gameFrameMenuBar = createGameFrameMenuBar();
        this.gameFrame.setJMenuBar(gameFrameMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.chessBoard = Board.constructStandardBoard();
        this.gameFrame.setLocationRelativeTo(null);
        this.boardPanel = new BoardPanel();
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.setVisible(true);
        this.gameFrame.setIconImage(logo.getImage());
    }

    private JMenuBar createGameFrameMenuBar() {
        final JMenuBar gameFrameMenuBar = new JMenuBar();
        gameFrameMenuBar.add(createFileMenu());
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

    private class BoardPanel extends JPanel {
        final List<TerrainPanel> boardTerrain;
        private final Image boardImage;

        BoardPanel() {
            super(new GridLayout(9, 7));
            this.boardTerrain = new ArrayList<>();
            this.boardImage = new ImageIcon("resource/images/chessboard.png").getImage();
            for (int i = 0; i < BoardUtils.NUM_TERRAINS; i++) {
                final TerrainPanel terrainPanel = new TerrainPanel(this, i);
                this.boardTerrain.add(terrainPanel);
                add(terrainPanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            setMaximumSize(getPreferredSize());
            validate();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(boardImage, 0, 0, getWidth(), getHeight(), this);
        }
    }


    private class TerrainPanel extends JPanel {
        private final int terrainCoordinate;

        TerrainPanel(final BoardPanel boardPanel, final int terrainCoordinate) {
            super(new GridBagLayout());
            this.terrainCoordinate = terrainCoordinate;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTerrainColor( terrainCoordinate);
            assignTerrainPieceIcon(chessBoard);
            validate();
        }

        private void assignTerrainPieceIcon(final Board board) {
            this.removeAll();
            if (board.getTerrain(this.terrainCoordinate).isTerrainOccupied()) {
                try {
                    final BufferedImage image = ImageIO.read(new File(defaultPieceImagesPath
                            + board.getTerrain(this.terrainCoordinate).getPiece().getPieceColor().toString().substring(0, 1)
                            + board.getTerrain(this.terrainCoordinate).getPiece().toString() + ".png"));
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
