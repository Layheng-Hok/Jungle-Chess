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
    private int initialTimerSecondsNormalMode = 60;
    private int currentTimerSecondsNormalMode = initialTimerSecondsNormalMode;
    private boolean normalModeWithTimer = true;
    private boolean stopTimerInNormalMode = false;
    private Timer blueTimerBlitzMode;
    private Timer redTimerBlitzMode;
    private int initialTimerSecondsBlitzMode = 300;
    private int blueCurrentTimerSecondsBlitzMode = initialTimerSecondsBlitzMode;
    private int redCurrentTimerSecondsBlitzMode = initialTimerSecondsBlitzMode;

    public PlayerPanel() {
        super(new BorderLayout());
        this.topPanelImage = new ImageIcon(defaultImagesPath + "toppanel.png").getImage();
        this.setLayout(null);
        assignResignButton();
        this.setPreferredSize(PLAYER_PANEL_DIMENSION);
    }

    private void assignResignButton() {
        ImageIcon originalResignIcon = new ImageIcon(defaultImagesPath + "resign.jpg");
        Image resizedResignImage = originalResignIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        ImageIcon originalHoverResignIcon = new ImageIcon(defaultImagesPath + "hoverresign.jpg");
        Image resizedHoverResignImage = originalHoverResignIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);

        JButton resignButton = new JButton(new ImageIcon(resizedResignImage));
        resignButton.setBorderPainted(false);
        resignButton.setContentAreaFilled(false);
        resignButton.setFocusPainted(false);
        resignButton.setBounds(244, 70, 25, 25);
        resignButton.setRolloverIcon(new ImageIcon(resizedHoverResignImage));

        resignButton.addActionListener(e -> {
            if (!GameFrame.isGameOverScenario(GameFrame.get().getChessBoard())) {
                AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
                if (!GameFrame.get().isBlitzMode()) {
                    GameFrame.get().setNormalModeGameOver(true);
                }
                if (!GameFrame.get().isBlitzMode()
                        && GameFrame.get().getPlayerPanel().isNormalModeWithTimer()) {
                    GameFrame.get().getPlayerPanel().getTimerNormalMode().stop();
                }
                if (GameFrame.get().isBlitzMode()) {
                    GameFrame.get().setBlitzModeGameOver(true);
                    GameFrame.get().getPlayerPanel().getBlueTimerBlitzMode().stop();
                    GameFrame.get().getPlayerPanel().getRedTimerBlitzMode().stop();
                }
                Controller.handleWinningStateForGameResignedCondition();
                System.out.println("Resign button clicked");
            }
        });

        this.add(resignButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (GameFrame.get().isBlitzMode()) {
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

            if (blueCurrentTimerSecondsBlitzMode <= 60) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.BLACK);
            }
            int blueTimerMinutes = blueCurrentTimerSecondsBlitzMode / 60;
            int blueTimerSeconds = blueCurrentTimerSecondsBlitzMode % 60;
            String blueTimerText = String.format("%02d:%02d", blueTimerMinutes, blueTimerSeconds);
            int blueTimerX = ((getWidth() - g.getFontMetrics().stringWidth(blueTimerText)) / 2) - 50;
            int blueTimerY = timerY + 26;
            g.drawString(blueTimerText, blueTimerX, blueTimerY);


            if (redCurrentTimerSecondsBlitzMode <= 60) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.BLACK);
            }
            int redTimerMinutes = redCurrentTimerSecondsBlitzMode / 60;
            int redTimerSeconds = redCurrentTimerSecondsBlitzMode % 60;
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
                if (currentTimerSecondsNormalMode <= 10) {
                    g.setFont(new Font("Consolas", Font.BOLD, 20));
                    g.setColor(Color.RED);
                    String timerText = "Timer: " + currentTimerSecondsNormalMode + "s";
                    int timerX = (getWidth() - g.getFontMetrics().stringWidth(timerText)) / 2;
                    int timerY = y + 30;
                    g.drawString(timerText, timerX, timerY);
                } else {
                    g.setFont(new Font("Consolas", Font.BOLD, 20));
                    g.setColor(Color.BLACK);
                    String timerText = "Timer: " + currentTimerSecondsNormalMode + "s";
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
            currentTimerSecondsNormalMode--;
            if (currentTimerSecondsNormalMode == 0) {
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
                    GameFrame.get().setChessBoard(chessBoard);
                    GameFrame.get().getBoardPanel().drawBoard(chessBoard);
                    GameFrame.get().getCapturedPiecesPanel().redo(GameFrame.get().getMoveLog());
                    if (GameFrame.get().getGameConfiguration().isAIPlayer(chessBoard.getCurrentPlayer())) {
                        GameFrame.get().moveMadeUpdate(PlayerType.HUMAN);
                    }
                }
                timerNormalMode.stop();
                if (!GameFrame.get().isBlitzMode() && normalModeWithTimer && !stopTimerInNormalMode) {
                    redo(GameFrame.get().getChessBoard());
                }
            }
            repaint();
        });
    }

    public void initTimerForBlueBlitzMode() {
        blueTimerBlitzMode = new Timer(1000, e -> {
            if (GameFrame.get().getChessBoard().getCurrentPlayer().getAllyColor().isBlue()) {
                blueCurrentTimerSecondsBlitzMode--;
                if (GameFrame.get().isBlitzMode()
                        && (GameFrame.get().getPlayerPanel().getBlueCurrentTimerSecondsBlitzMode() == 0
                        || GameFrame.get().getPlayerPanel().getRedCurrentTimerSecondsBlitzMode() == 0)) {
                    GameFrame.get().getPlayerPanel().getBlueTimerBlitzMode().stop();
                    GameFrame.get().getPlayerPanel().getRedTimerBlitzMode().stop();
                    GameFrame.get().setBlitzModeGameOver(true);
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
                redCurrentTimerSecondsBlitzMode--;
                if (GameFrame.get().isBlitzMode()
                        && (GameFrame.get().getPlayerPanel().getBlueCurrentTimerSecondsBlitzMode() == 0
                        || GameFrame.get().getPlayerPanel().getRedCurrentTimerSecondsBlitzMode() == 0)) {
                    GameFrame.get().getPlayerPanel().getBlueTimerBlitzMode().stop();
                    GameFrame.get().getPlayerPanel().getRedTimerBlitzMode().stop();
                    GameFrame.get().setBlitzModeGameOver(true);
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

    public void initBlitzMode() {
        blueCurrentTimerSecondsBlitzMode = initialTimerSecondsBlitzMode;
        redCurrentTimerSecondsBlitzMode = initialTimerSecondsBlitzMode;
        initTimerForBlueBlitzMode();
        initTimerForRedBlitzMode();
        blueTimerBlitzMode.restart();
        redTimerBlitzMode.restart();
    }

    public void reset() {
        roundNumber = 1;
        currentTimerSecondsNormalMode = initialTimerSecondsNormalMode;
        if (!GameFrame.get().isBlitzMode() && normalModeWithTimer && !stopTimerInNormalMode) {
            timerNormalMode.restart();
        } else if (GameFrame.get().isBlitzMode() && !GameFrame.get().isReplayMovesInProgress()) {
            blueCurrentTimerSecondsBlitzMode = initialTimerSecondsBlitzMode;
            redCurrentTimerSecondsBlitzMode = initialTimerSecondsBlitzMode;
            blueTimerBlitzMode.restart();
            redTimerBlitzMode.restart();
            GameFrame.get().setBlitzModeGameOver(false);
        } else if (GameFrame.get().isBlitzMode() && GameFrame.get().isReplayMovesInProgress()) {
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
        if (!GameFrame.get().isBlitzMode() && normalModeWithTimer && !stopTimerInNormalMode) {
            currentTimerSecondsNormalMode = initialTimerSecondsNormalMode;
            timerNormalMode.restart();
        }
        repaint();
    }

    public void redo(Board chessBoard) {
        if (chessBoard.getCurrentPlayer().getAllyColor().isBlue()) {
            setRoundNumber(getRoundNumber() + 1);
        }
        if (!GameFrame.get().isBlitzMode() && normalModeWithTimer && !stopTimerInNormalMode) {
            currentTimerSecondsNormalMode = initialTimerSecondsNormalMode;
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

    public void setCurrentTimerSecondsNormalMode(int currentTimerSecondsNormalMode) {
        this.currentTimerSecondsNormalMode = currentTimerSecondsNormalMode;
    }

    public int getInitialTimerSecondsNormalMode() {
        return initialTimerSecondsNormalMode;
    }

    public void setInitialTimerSecondsNormalMode(int initialTimerSecondsNormalMode) {
        this.initialTimerSecondsNormalMode = initialTimerSecondsNormalMode;
    }

    public void setStopTimerInNormalMode(boolean stopTimerInNormalMode) {
        this.stopTimerInNormalMode = stopTimerInNormalMode;
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

    public int getBlueCurrentTimerSecondsBlitzMode() {
        return blueCurrentTimerSecondsBlitzMode;
    }

    public void setBlueCurrentTimerSecondsBlitzMode(int blueCurrentTimerSecondsBlitzMode) {
        this.blueCurrentTimerSecondsBlitzMode = blueCurrentTimerSecondsBlitzMode;
    }

    public int getRedCurrentTimerSecondsBlitzMode() {
        return redCurrentTimerSecondsBlitzMode;
    }

    public void setRedCurrentTimerSecondsBlitzMode(int redCurrentTimerSecondsBlitzMode) {
        this.redCurrentTimerSecondsBlitzMode = redCurrentTimerSecondsBlitzMode;
    }
}
