package view;

import model.board.Board;
import model.board.Move;
import model.player.PlayerColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

import static view.GameFrame.defaultImagesPath;

public class PlayerPanel extends JPanel {
    private static final Dimension PLAYER_PANEL_DIMENSION = new Dimension(530, 100);
    private final Image topPanelImage;
    private int roundNumber = 1;
    private String currentPlayer = "Blue";
    private Timer timer;
    private int timerSeconds = 30;

    public PlayerPanel() {
        super(new BorderLayout());
        this.topPanelImage = new ImageIcon(defaultImagesPath + "toppanel.png").getImage();
        setPreferredSize(PLAYER_PANEL_DIMENSION);
        initTimer();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(topPanelImage, 0, 0, getWidth(), getHeight(), this);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Consolas", Font.BOLD, 20));
        String text = "Round " + roundNumber + ": " + currentPlayer.toUpperCase();
        int x = (getWidth() - g.getFontMetrics().stringWidth(text)) / 2;
        int y = 33;
        g.drawString(text, x, y);

        g.setFont(new Font("Consolas", Font.BOLD, 20));
        g.setColor(Color.BLACK);
        String timerText = "Timer: " + timerSeconds + "s";
        int timerX = (getWidth() - g.getFontMetrics().stringWidth(timerText)) / 2;
        int timerY = y + 30;
        g.drawString(timerText, timerX, timerY);

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

    void initTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timerSeconds--;
                if (timerSeconds == 0) {
                    Board chessBoard = GameFrame.get().getChessBoard();
                    List<Move> validMoves = new ArrayList<>(chessBoard.getCurrentPlayer().getValidMoves());
                    Move selectedMove = null;
                    for (Move move : validMoves) {
                        if (move.isCaptureMove()) {
                            selectedMove = move;
                            break;
                        }
                    }
                    if (selectedMove == null && !validMoves.isEmpty()) {
                        int randomIndex = (int) (Math.random() * validMoves.size());
                        selectedMove = validMoves.get(randomIndex);
                    }
                    if (selectedMove != null) {
                        AudioPlayer.SinglePlayer.playSoundEffect("click.wav");
                        chessBoard = selectedMove.execute();
                        GameFrame.get().setLastMove(selectedMove);
                        GameFrame.get().getMoveLog().addMove(selectedMove);
                        GameFrame.get().setGameBoard(chessBoard);
                    }
                    timer.stop();
                    update();
                }
                repaint();
            }
        });
        timer.start();
    }

    public void update() {
        if (currentPlayer.equals("Blue")) {
            currentPlayer = "Red";
        } else {
            currentPlayer = "Blue";
            setRoundNumber(getRoundNumber() + 1);
        }
        timerSeconds = 30;
        timer.restart();
        repaint();
    }

    public void reset() {
        roundNumber = 1;
        currentPlayer = "Blue";
        timerSeconds = 30;
        timer.restart();
        repaint();
    }

    public void undo() {
        if (GameFrame.get().getChessBoard().getCurrentPlayer().getAllyColor() == PlayerColor.BLUE) {
            currentPlayer = "Blue";
        } else if (GameFrame.get().getChessBoard().getCurrentPlayer().getAllyColor() == PlayerColor.RED){
            currentPlayer = "Red";

        }
        timerSeconds = 30;
        timer.restart();
        repaint();
    }

    public void undoAIBlue() {
        currentPlayer = "Red";
        setRoundNumber(getRoundNumber() - 1);
        timerSeconds = 30;
        timer.restart();
        repaint();
    }

    public void undoAIRed() {
        currentPlayer = "Blue";
        setRoundNumber(getRoundNumber() - 1);
        timerSeconds = 30;
        timer.restart();
        repaint();
    }

    public void redo(Board chessBoard) {
        if (chessBoard.getCurrentPlayer().getAllyColor().isBlue()) {
            currentPlayer = "Blue";
            setRoundNumber(getRoundNumber() + 1);
        } else {
            currentPlayer = "Red";
        }
        timerSeconds = 30;
        timer.restart();
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

    public void setTimerSeconds(int timerSeconds) {
        this.timerSeconds = timerSeconds;
    }
}
