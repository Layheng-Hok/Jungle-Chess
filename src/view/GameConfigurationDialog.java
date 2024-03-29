package view;

import model.player.Player;
import model.player.PlayerColor;
import model.player.PlayerType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static view.GameFrame.defaultImagesPath;

public class GameConfigurationDialog extends JDialog {
    private PlayerType bluePlayerType;
    private PlayerType redPlayerType;
    private static final String HUMAN = "Human";
    private static final String COMPUTER = "Computer";
    private boolean isReady = false;

    GameConfigurationDialog(final JFrame frame, final boolean modal) {
        super(frame, modal);
        setTitle("PICK A SIDE");
        final JPanel gameConfigPanel = new JPanel(new GridLayout(0, 1));
        final JRadioButton blueHumanButton = new JRadioButton(HUMAN);
        final JRadioButton blueComputerButton = new JRadioButton(COMPUTER);
        final JRadioButton redHumanButton = new JRadioButton(HUMAN);
        final JRadioButton redComputerButton = new JRadioButton(COMPUTER);
        blueHumanButton.setActionCommand(HUMAN);

        final ButtonGroup blueGroup = new ButtonGroup();
        blueGroup.add(blueHumanButton);
        blueGroup.add(blueComputerButton);
        blueHumanButton.setSelected(true);

        final ButtonGroup redGroup = new ButtonGroup();
        redGroup.add(redHumanButton);
        redGroup.add(redComputerButton);
        redComputerButton.setSelected(true);

        getContentPane().add(gameConfigPanel);
        gameConfigPanel.add(new JLabel("      Blue Side"));
        gameConfigPanel.add(blueHumanButton);
        gameConfigPanel.add(blueComputerButton);
        gameConfigPanel.add(new JLabel("      Red Side"));
        gameConfigPanel.add(redHumanButton);
        gameConfigPanel.add(redComputerButton);

        final JButton okButton = new JButton("Get Started");

        ActionListener blueButtonListener = e -> {
            if (blueComputerButton.isSelected()) {
                redHumanButton.setSelected(true);
            } else if (blueHumanButton.isSelected()){
                redComputerButton.setSelected(true);
            }
        };
        blueHumanButton.addActionListener(blueButtonListener);
        blueComputerButton.addActionListener(blueButtonListener);

        ActionListener redButtonListener = e -> {
            if (redComputerButton.isSelected()) {
                blueHumanButton.setSelected(true);
            } else if (redHumanButton.isSelected()){
                blueComputerButton.setSelected(true);
            }
        };
        redHumanButton.addActionListener(redButtonListener);
        redComputerButton.addActionListener(redButtonListener);

        okButton.addActionListener(e -> {
            AudioPlayer.SinglePlayer.playSoundEffect("buttonclick.wav");
            bluePlayerType = blueComputerButton.isSelected() ? PlayerType.AI : PlayerType.HUMAN;
            redPlayerType = redComputerButton.isSelected() ? PlayerType.AI : PlayerType.HUMAN;
            GameConfigurationDialog.this.setVisible(false);
            isReady = true;
            System.out.println("Set Up Game");
        });

        gameConfigPanel.add(okButton);
        ImageIcon logo = new ImageIcon(defaultImagesPath + "junglechesslogo.jpg");
        setIconImage(logo.getImage());
        setSize(190, 200);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(false);
    }

    void promptUser() {
        setVisible(true);
        repaint();
    }

    public boolean isAIPlayer(final Player player) {
        if (player.getAllyColor() == PlayerColor.BLUE) {
            return getBluePlayerType() == PlayerType.AI;
        }
        return getRedPlayerType() == PlayerType.AI;
    }

    public PlayerType getBluePlayerType() {
        return this.bluePlayerType;
    }

    public void setBluePlayerType(PlayerType bluePlayerType) {
        this.bluePlayerType = bluePlayerType;
    }

    public PlayerType getRedPlayerType() {
        return this.redPlayerType;
    }

    public void setRedPlayerType(PlayerType redPlayerType) {
        this.redPlayerType = redPlayerType;
    }

    boolean isReady() {
        return isReady;
    }

    void setReady(boolean isReady) {
        this.isReady = isReady;
    }
}
