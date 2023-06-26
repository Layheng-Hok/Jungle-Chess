package view;

import model.board.Board;
import model.board.Move;
import model.player.PlayerType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import static view.GameFrame.defaultImagesPath;

public class PlayerPanel extends JPanel {
    private static final Dimension PLAYER_PANEL_DIMENSION = new Dimension(530, 100);
    private final Image topPanelImage;
    private int roundNumber = 1;
    private Timer timer;
    private int initialTimerSeconds = 60;
    private int timerSeconds = initialTimerSeconds;
    private boolean normalModeWithTimer = true;
    private boolean stopTimer = false;
    private boolean blitzMode = false;

    public PlayerPanel() {
        super(new BorderLayout());
        this.topPanelImage = new ImageIcon(defaultImagesPath + "toppanel.png").getImage();
        setPreferredSize(PLAYER_PANEL_DIMENSION);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (blitzMode) {
            super.paintComponent(g);
            g.drawImage(topPanelImage, 0, 0, getWidth(), getHeight(), this);

            g.setColor(Color.BLACK);
            g.setFont(new Font("Consolas", Font.BOLD, 20));
            String text = "Round " + roundNumber + ": " + GameFrame.get().getChessBoard().getCurrentPlayer().getAllyColor().toString().toUpperCase();
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
            if (GameFrame.get().getChessBoard().getCurrentPlayer().getAllyColor().isBlue()) {
                g2d.setStroke(new BasicStroke(3));
                g2d.setColor(new Color(0x00FF00));
            }

            g2d.drawRoundRect(leftImageX - 5, imageY - 5, imageWidth + 10, imageHeight + 10, 10, 10);
            Image originalLeftImage = new ImageIcon(defaultImagesPath + "player1.png").getImage();
            Image scaledLeftImage = originalLeftImage.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
            ImageIcon leftImageIcon = new ImageIcon(scaledLeftImage);
            g.drawImage(leftImageIcon.getImage(), leftImageX, imageY, imageWidth, imageHeight, this);


            g2d.setColor(new Color(0, 0, 0, 127));
            g2d.fillRect(rightImageX - 5, imageY - 5, imageWidth + 10, imageHeight + 10);
            if (GameFrame.get().getChessBoard().getCurrentPlayer().getAllyColor().isRed()) {
                g2d.setStroke(new BasicStroke(3));
                g2d.setColor(new Color(0x00FF00));
            }

            g2d.drawRoundRect(rightImageX - 5, imageY - 5, imageWidth + 10, imageHeight + 10, 10, 10);
            Image originalRightImage = new ImageIcon(defaultImagesPath + "player2.png").getImage();
            Image scaledRightImage = originalRightImage.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
            ImageIcon rightImageIcon = new ImageIcon(scaledRightImage);
            g.drawImage(rightImageIcon.getImage(), rightImageX, imageY, imageWidth, imageHeight, this);

            validate();
        } else {
            super.paintComponent(g);
            g.drawImage(topPanelImage, 0, 0, getWidth(), getHeight(), this);

            g.setColor(Color.BLACK);
            g.setFont(new Font("Consolas", Font.BOLD, 20));
            String text = "Round " + roundNumber + ": " + GameFrame.get().getChessBoard().getCurrentPlayer().getAllyColor().toString().toUpperCase();
            int x = (getWidth() - g.getFontMetrics().stringWidth(text)) / 2;
            int y = 33;
            g.drawString(text, x, y);

            if (normalModeWithTimer) {
                g.setFont(new Font("Consolas", Font.BOLD, 20));
                g.setColor(Color.BLACK);
                String timerText = "Timer: " + timerSeconds + "s";
                int timerX = (getWidth() - g.getFontMetrics().stringWidth(timerText)) / 2;
                int timerY = y + 30;
                g.drawString(timerText, timerX, timerY);
            }

            int imageWidth = 60;
            int imageHeight = 60;
            int padding = 250;
            int leftImageX = (getWidth() - (2 * imageWidth) - padding) / 2;
            int rightImageX = leftImageX + imageWidth + padding;
            int imageY = (getHeight() - imageHeight) / 2;

            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(0, 0, 0, 127));
            g2d.fillRect(leftImageX - 5, imageY - 5, imageWidth + 10, imageHeight + 10);
            if (GameFrame.get().getChessBoard().getCurrentPlayer().getAllyColor().isBlue()) {
                g2d.setStroke(new BasicStroke(3));
                g2d.setColor(new Color(0x00FF00));
            }

            g2d.drawRoundRect(leftImageX - 5, imageY - 5, imageWidth + 10, imageHeight + 10, 10, 10);
            Image originalLeftImage = new ImageIcon(defaultImagesPath + "player1.png").getImage();
            Image scaledLeftImage = originalLeftImage.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
            ImageIcon leftImageIcon = new ImageIcon(scaledLeftImage);
            g.drawImage(leftImageIcon.getImage(), leftImageX, imageY, imageWidth, imageHeight, this);


            g2d.setColor(new Color(0, 0, 0, 127));
            g2d.fillRect(rightImageX - 5, imageY - 5, imageWidth + 10, imageHeight + 10);
            if (GameFrame.get().getChessBoard().getCurrentPlayer().getAllyColor().isRed()) {
                g2d.setStroke(new BasicStroke(3));
                g2d.setColor(new Color(0x00FF00));
            }

            g2d.drawRoundRect(rightImageX - 5, imageY - 5, imageWidth + 10, imageHeight + 10, 10, 10);
            Image originalRightImage = new ImageIcon(defaultImagesPath + "player2.png").getImage();
            Image scaledRightImage = originalRightImage.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
            ImageIcon rightImageIcon = new ImageIcon(scaledRightImage);
            g.drawImage(rightImageIcon.getImage(), rightImageX, imageY, imageWidth, imageHeight, this);

            validate();
        }
    }

    public void initTimerForNormalMode() {
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
                        if (!blitzMode && normalModeWithTimer && !stopTimer) {
                            redo(GameFrame.get().getChessBoard());
                        }
                    }
                    repaint();
                }
            });
    }

    public void reset() {
        roundNumber = 1;
        timerSeconds = initialTimerSeconds;
        if (!blitzMode && normalModeWithTimer && !stopTimer) {
            timer.restart();
        }
        repaint();
    }

    public void undo() {
        if (GameFrame.get().getGameConfiguration().getBluePlayerType() == PlayerType.AI
                || GameFrame.get().getGameConfiguration().getRedPlayerType() == PlayerType.AI) {
            setRoundNumber(getRoundNumber() - 1);
        }
        if ((GameFrame.get().getGameConfiguration().getBluePlayerType() == PlayerType.HUMAN
                && GameFrame.get().getGameConfiguration().getRedPlayerType() == PlayerType.HUMAN)
                && GameFrame.get().getChessBoard().getCurrentPlayer().getAllyColor().isRed()) {
            setRoundNumber(getRoundNumber() - 1);
        }
        if (!blitzMode && normalModeWithTimer && !stopTimer) {
            timerSeconds = initialTimerSeconds;
            timer.restart();
        }
        repaint();
    }

    public void redo(Board chessBoard) {
        if (chessBoard.getCurrentPlayer().getAllyColor().isBlue()) {
            setRoundNumber(getRoundNumber() + 1);
        }
        if (!blitzMode && normalModeWithTimer && !stopTimer) {
            timerSeconds = initialTimerSeconds;
            timer.restart();
        }
        repaint();
    }

    public Timer getTimer() {
        return timer;
    }

    public int getRoundNumber() {
        return this.roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public void setTimerSeconds(int timerSeconds) {
        this.timerSeconds = timerSeconds;
    }

    public int getInitialTimerSeconds() {
        return initialTimerSeconds;
    }

    public void setInitialTimerSeconds(int initialTimerSeconds) {
        this.initialTimerSeconds = initialTimerSeconds;
    }

    public boolean isNormalModeWithTimer() {
        return normalModeWithTimer;
    }

    public void setNormalModeWithTimer(boolean normalModeWithTimer) {
        this.normalModeWithTimer = normalModeWithTimer;
    }

    public boolean isStopTimer() {
        return stopTimer;
    }

    public void setStopTimer(boolean stopTimer) {
        this.stopTimer = stopTimer;
    }

    public boolean isBlitzMode() {
        return blitzMode;
    }

    public void setBlitzMode(boolean blitzMode) {
        this.blitzMode = blitzMode;
    }
}
