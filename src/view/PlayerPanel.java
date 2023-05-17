package view;

import model.board.Board;

import javax.swing.*;
import java.awt.*;

import static view.GameFrame.defaultImagesPath;

 class PlayerPanel extends JPanel {
    private static final Dimension PLAYER_PANEL_DIMENSION = new Dimension(530, 100);
    private final Image topPanelImage;
    private int roundNumber = 1;
    private String currentPlayer = "Blue";

    public PlayerPanel() {
        super(new BorderLayout());
        this.topPanelImage = new ImageIcon(defaultImagesPath + "toppanel.png").getImage();
        setPreferredSize(PLAYER_PANEL_DIMENSION);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(topPanelImage, 0, 0, getWidth(), getHeight(), this);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        String text = "Round " + roundNumber + ": " + currentPlayer.toUpperCase();
        int x = (getWidth() - g.getFontMetrics().stringWidth(text)) / 2;
        int y = 33;
        g.drawString(text, x, y);

        int imageWidth = 60;
        int imageHeight = 60;
        int padding = 250;
        int leftImageX = (getWidth() - (2 * imageWidth) - padding) / 2;
        int rightImageX = leftImageX + imageWidth + padding;
        int imageY = (getHeight() - imageHeight) / 2;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(0, 0, 0, 127));
        g2d.fillRect(leftImageX - 5, imageY - 5, imageWidth + 10, imageHeight + 10);
        if (currentPlayer.equals("Blue")) {
            g2d.setStroke(new BasicStroke(3));
            g2d.setColor(new Color(0x00FF00));
        }
        g2d.drawRoundRect(leftImageX - 5, imageY - 5, imageWidth + 10, imageHeight + 10, 10, 10);
        Image leftImage = new ImageIcon(defaultImagesPath + "player1.png").getImage();
        g.drawImage(leftImage, leftImageX, imageY, imageWidth, imageHeight, this);

        g2d.setColor(new Color(0, 0, 0, 127));
        g2d.fillRect(rightImageX - 5, imageY - 5, imageWidth + 10, imageHeight + 10);
        if (currentPlayer.equals("Red")) {
            g2d.setStroke(new BasicStroke(3));
            g2d.setColor(new Color(0x00FF00));
        }
        g2d.drawRoundRect(rightImageX - 5, imageY - 5, imageWidth + 10, imageHeight + 10, 10, 10);
        Image rightImage = new ImageIcon(defaultImagesPath + "player2.png").getImage();
        g.drawImage(rightImage, rightImageX, imageY, imageWidth, imageHeight, this);

        validate();
    }

    public void reset() {
        roundNumber = 1;
        currentPlayer = "Blue";
        repaint();
    }

    public int getRoundNumber() {
        return this.roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

     public void undo() {
            if (currentPlayer.equals("Blue")) {
                currentPlayer = "Red";
                setRoundNumber(getRoundNumber() - 1);
            } else {
                currentPlayer = "Blue";
            }
            repaint();
     }

     public void update(Board chessBoard) {
            if (chessBoard.getCurrentPlayer().getAllyColor().isBlue()) {
                currentPlayer = "Blue";
            } else {
                currentPlayer = "Red";
            }
            roundNumber = getRoundNumber();
            repaint();
     }
 }
