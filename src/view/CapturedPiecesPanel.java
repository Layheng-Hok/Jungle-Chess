package view;

import model.board.Move;
import model.piece.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static view.GameFrame.MoveLog;
import static view.GameFrame.defaultImagesPath;

public class CapturedPiecesPanel extends JPanel {
    private final JPanel westPanel;
    private final JPanel eastPanel;
    private static final Dimension CAPTURED_PIECES_PANEL_DIMENSION = new Dimension(530, 100);
    private final Image bottomPanelImage;

    public CapturedPiecesPanel() {
        super(new BorderLayout());
        this.bottomPanelImage = new ImageIcon(defaultImagesPath + "bottompanel.png").getImage();
        this.eastPanel = new JPanel(new GridLayout(2, 4));
        this.westPanel = new JPanel(new GridLayout(2, 4));
        this.add(this.eastPanel, BorderLayout.EAST);
        this.add(this.westPanel, BorderLayout.WEST);
        setPreferredSize(CAPTURED_PIECES_PANEL_DIMENSION);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bottomPanelImage, 0, 0, getWidth(), getHeight(), this);
    }


    public void redo(final MoveLog moveLog) {
        this.eastPanel.removeAll();
        this.westPanel.removeAll();
        final List<Piece> blueCapturedPieces = new ArrayList<>();
        final List<Piece> redCapturedPieces = new ArrayList<>();
        for (final Move move : moveLog.getMoves()) {
            if (move.isCapture()) {
                final Piece capturedPiece = move.getCapturedPiece();
                if (capturedPiece.getPieceColor().isBlue()) {
                    blueCapturedPieces.add(capturedPiece);
                } else if (capturedPiece.getPieceColor().isRed()) {
                    redCapturedPieces.add(capturedPiece);
                } else {
                    throw new RuntimeException("Should not reach here.");
                }
            }
        }
        Collections.sort(blueCapturedPieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece o1, Piece o2) {
                return Integer.compare(o1.getAttackPieceRank(), o2.getAttackPieceRank());
            }
        });
        Collections.sort(redCapturedPieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece o1, Piece o2) {
                return Integer.compare(o1.getAttackPieceRank(), o2.getAttackPieceRank());
            }
        });
        for (final Piece capturedPiece : blueCapturedPieces) {
            try {
                final BufferedImage image = ImageIO.read(new File(defaultImagesPath +
                        capturedPiece.getPieceColor().toString().toLowerCase() +
                        capturedPiece.toString() + ".png"));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel();
                this.westPanel.add(imageLabel);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        for (final Piece capturedPiece : redCapturedPieces) {
            try {
                final BufferedImage image = ImageIO.read(new File(defaultImagesPath +
                        capturedPiece.getPieceColor().toString().toLowerCase() +
                        capturedPiece.toString() + ".png"));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel();
                this.eastPanel.add(imageLabel);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        validate();
    }
}
