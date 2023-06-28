package view;

import model.Controller;
import model.board.Board;
import model.board.BoardUtilities;
import model.board.Move;
import model.player.PlayerType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static view.GameFrame.defaultImagesPath;

public class PlayerPanel extends JPanel {
    private static final Dimension PLAYER_PANEL_DIMENSION = new Dimension(530, 100);
    private final Image topPanelImage;
    private int roundNumber = 1;
    private Timer timerNormalMode;
    private int initialTimerSeconds = 60;
    private int timerSeconds = initialTimerSeconds;
    private boolean normalModeWithTimer = true;
    private boolean stopTimerInNormalMode = false;
    private boolean normalModeGameOver = false;
    private Timer blueTimerBlitzMode;
    private Timer redTimerBlitzMode;
    private int initialTimerSecondsBlitzMode = 300;
    private int blueInitialTimerSecondsBlitzMode = initialTimerSecondsBlitzMode;
    private int redInitialTimerSecondsBlitzMode = initialTimerSecondsBlitzMode;
    private boolean blitzMode = false;
    private boolean blitzModeGameOver = false;

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
            String timerText = "Time Remaining";
            int timerX = (getWidth() - g.getFontMetrics().stringWidth(timerText)) / 2;
            int timerY = y + 26;
            g.drawString(timerText, timerX, timerY);

            if (blueInitialTimerSecondsBlitzMode <= 60) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.BLACK);
            }
            int blueTimerMinutes = blueInitialTimerSecondsBlitzMode / 60;
            int blueTimerSeconds = blueInitialTimerSecondsBlitzMode % 60;
            String blueTimerText = String.format("%02d:%02d", blueTimerMinutes, blueTimerSeconds);
            int blueTimerX = ((getWidth() - g.getFontMetrics().stringWidth(blueTimerText)) / 2) - 50;
            int blueTimerY = timerY + 26;
            g.drawString(blueTimerText, blueTimerX, blueTimerY);


            if (redInitialTimerSecondsBlitzMode <= 60) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.BLACK);
            }
            int redTimerMinutes = redInitialTimerSecondsBlitzMode / 60;
            int redTimerSeconds = redInitialTimerSecondsBlitzMode % 60;
            String redTimerText = String.format("%02d:%02d", redTimerMinutes, redTimerSeconds);
            int redTimerX = ((getWidth() - g.getFontMetrics().stringWidth(redTimerText)) / 2) + 50;
            int redTimerY = timerY + 26;
            g.drawString(redTimerText, redTimerX, redTimerY);

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
                g2d.setColor(new Color(0, 255, 0));
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
                g2d.setColor(new Color(0, 255, 0));
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
                if (timerSeconds <= 10) {
                    g.setFont(new Font("Consolas", Font.BOLD, 20));
                    g.setColor(Color.RED);
                    String timerText = "Timer: " + timerSeconds + "s";
                    int timerX = (getWidth() - g.getFontMetrics().stringWidth(timerText)) / 2;
                    int timerY = y + 30;
                    g.drawString(timerText, timerX, timerY);
                } else {
                    g.setFont(new Font("Consolas", Font.BOLD, 20));
                    g.setColor(Color.BLACK);
                    String timerText = "Timer: " + timerSeconds + "s";
                    int timerX = (getWidth() - g.getFontMetrics().stringWidth(timerText)) / 2;
                    int timerY = y + 30;
                    g.drawString(timerText, timerX, timerY);
                }
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
                g2d.setColor(new Color(0, 255, 0));
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
                g2d.setColor(new Color(0, 255, 0));
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
        timerNormalMode = new Timer(1000, e -> {
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
                timerNormalMode.stop();
                if (!blitzMode && normalModeWithTimer && !stopTimerInNormalMode) {
                    redo(GameFrame.get().getChessBoard());
                }
            }
            repaint();
        });
    }

    public void initTimerForBlueBlitzMode() {
        blueTimerBlitzMode = new Timer(1000, e -> {
            if (GameFrame.get().getChessBoard().getCurrentPlayer().getAllyColor().isBlue()) {
                blueInitialTimerSecondsBlitzMode--;
                if (GameFrame.get().getPlayerPanel().isBlitzMode()
                        && (GameFrame.get().getPlayerPanel().getBlueInitialTimerSecondsBlitzMode() == 0
                        || GameFrame.get().getPlayerPanel().getRedInitialTimerSecondsBlitzMode() == 0)) {
                    GameFrame.get().getPlayerPanel().getBlueTimerBlitzMode().stop();
                    GameFrame.get().getPlayerPanel().getRedTimerBlitzMode().stop();
                    blitzModeGameOver = true;
                    for (int i = 0; i < BoardUtilities.NUM_TERRAINS; i++) {
                        GameFrame.get().getBoardPanel().getBoardTerrains().get(i).deselectLeftMouseButton();
                    }
                    GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
                    Controller.handleWinningStateForBlitzModeCondition();
                }
            }
            repaint();
        });
    }

    public void initTimerForRedBlitzMode() {
        redTimerBlitzMode = new Timer(1000, e -> {
            if (GameFrame.get().getChessBoard().getCurrentPlayer().getAllyColor().isRed()) {
                redInitialTimerSecondsBlitzMode--;
                if (GameFrame.get().getPlayerPanel().isBlitzMode()
                        && (GameFrame.get().getPlayerPanel().getBlueInitialTimerSecondsBlitzMode() == 0
                        || GameFrame.get().getPlayerPanel().getRedInitialTimerSecondsBlitzMode() == 0)) {
                    GameFrame.get().getPlayerPanel().getBlueTimerBlitzMode().stop();
                    GameFrame.get().getPlayerPanel().getRedTimerBlitzMode().stop();
                    blitzModeGameOver = true;
                    for (int i = 0; i < BoardUtilities.NUM_TERRAINS; i++) {
                        GameFrame.get().getBoardPanel().getBoardTerrains().get(i).deselectLeftMouseButton();
                    }
                    GameFrame.get().getBoardPanel().drawBoard(GameFrame.get().getChessBoard());
                    Controller.handleWinningStateForBlitzModeCondition();
                }
            }
            repaint();
        });
    }

    public void initBlitzMode(){
        blueInitialTimerSecondsBlitzMode = initialTimerSecondsBlitzMode;
        redInitialTimerSecondsBlitzMode = initialTimerSecondsBlitzMode;
        initTimerForBlueBlitzMode();
        initTimerForRedBlitzMode();
        blueTimerBlitzMode.restart();
        redTimerBlitzMode.restart();
    }

    public void reset() {
        roundNumber = 1;
        timerSeconds = initialTimerSeconds;
        if (!blitzMode && normalModeWithTimer && !stopTimerInNormalMode) {
            timerNormalMode.restart();
        } else if (blitzMode && !GameFrame.get().isReplayMovesInProgress()) {
            blueInitialTimerSecondsBlitzMode = initialTimerSecondsBlitzMode;
            redInitialTimerSecondsBlitzMode = initialTimerSecondsBlitzMode;
            blueTimerBlitzMode.restart();
            redTimerBlitzMode.restart();
            blitzModeGameOver = false;
        } else if (blitzMode && GameFrame.get().isReplayMovesInProgress()) {
            blueTimerBlitzMode.stop();
            redTimerBlitzMode.stop();
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
        if (!blitzMode && normalModeWithTimer && !stopTimerInNormalMode) {
            timerSeconds = initialTimerSeconds;
            timerNormalMode.restart();
        }
        repaint();
    }

    public void redo(Board chessBoard) {
        if (chessBoard.getCurrentPlayer().getAllyColor().isBlue()) {
            setRoundNumber(getRoundNumber() + 1);
        }
        if (!blitzMode && normalModeWithTimer && !stopTimerInNormalMode) {
            timerSeconds = initialTimerSeconds;
            timerNormalMode.restart();
        }
        repaint();
    }

    public int getRoundNumber() {
        return this.roundNumber;
    }

    public boolean isNormalModeWithTimer() {
        return normalModeWithTimer;
    }

    public void setNormalModeWithTimer(boolean normalModeWithTimer) {
        this.normalModeWithTimer = normalModeWithTimer;
    }

    public Timer getTimerNormalMode() {
        return timerNormalMode;
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

    public boolean isStopTimerInNormalMode() {
        return stopTimerInNormalMode;
    }

    public void setStopTimerInNormalMode(boolean stopTimerInNormalMode) {
        this.stopTimerInNormalMode = stopTimerInNormalMode;
    }

    public boolean isNormalModeGameOver() {
        return normalModeGameOver;
    }

    public void setNormalModeGameOver(boolean normalModeGameOver) {
        this.normalModeGameOver = normalModeGameOver;
    }

    public boolean isBlitzMode() {
        return blitzMode;
    }

    public void setBlitzMode(boolean blitzMode) {
        this.blitzMode = blitzMode;
    }

    public Timer getBlueTimerBlitzMode() {
        return blueTimerBlitzMode;
    }

    public Timer getRedTimerBlitzMode() {
        return redTimerBlitzMode;
    }

    public int getInitialTimerSecondsBlitzMode() {
        return initialTimerSecondsBlitzMode;
    }

    public void setInitialTimerSecondsBlitzMode(int initialTimerSecondsBlitzMode) {
        this.initialTimerSecondsBlitzMode = initialTimerSecondsBlitzMode;
    }

    public int getBlueInitialTimerSecondsBlitzMode() {
        return blueInitialTimerSecondsBlitzMode;
    }

    public void setBlueInitialTimerSecondsBlitzMode(int blueInitialTimerSecondsBlitzMode) {
        this.blueInitialTimerSecondsBlitzMode = blueInitialTimerSecondsBlitzMode;
    }

    public int getRedInitialTimerSecondsBlitzMode() {
        return redInitialTimerSecondsBlitzMode;
    }

    public void setRedInitialTimerSecondsBlitzMode(int redInitialTimerSecondsBlitzMode) {
        this.redInitialTimerSecondsBlitzMode = redInitialTimerSecondsBlitzMode;
    }

    public boolean isBlitzModeGameOver() {
        return blitzModeGameOver;
    }

    public void setBlitzModeGameOver(boolean blitzModeGameOver) {
        this.blitzModeGameOver = blitzModeGameOver;
    }
}
