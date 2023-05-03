package view;

import model.board.BoardUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class GameFrame {
    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(1000, 800);
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(450, 350);
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(12, 12);
    ImageIcon logo = new ImageIcon("resource/images/JungleChessLogo.jpg");

    public GameFrame() {
        this.gameFrame = new JFrame("Jungle Chess (斗兽棋)");
        this.gameFrame.setLayout(new BorderLayout());
        this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final JMenuBar gameFrameMenuBar = createGameFrameMenuBar();
        this.gameFrame.setJMenuBar(gameFrameMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
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
        openPGN.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Open up that pgn file!");
            }
        });
        fileMenu.add(openPGN);
        return fileMenu;
    }

    private class BoardPanel extends JPanel {
        final List<TerrainPanel> boardTerrain;

        BoardPanel() {
            super(new GridLayout(9, 7));
            this.boardTerrain = new ArrayList<>();
            for (int i = 0; i < BoardUtils.NUM_TERRAINS; i++) {
                final TerrainPanel terrainPanel = new TerrainPanel(this, i);
                this.boardTerrain.add(terrainPanel);
                add(terrainPanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }
    }

    private class TerrainPanel extends JPanel {
        private final int terrainCoordinate;

        TerrainPanel(final BoardPanel boardPanel, final int terrainCoordinate) {
            super(new GridBagLayout());
            this.terrainCoordinate = terrainCoordinate;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTerrainImage();
            validate();
        }

        private void assignTerrainImage() {
        }
    }
}
