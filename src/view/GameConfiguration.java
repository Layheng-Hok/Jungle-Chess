package view;

import model.player.Player;
import model.player.PlayerColor;
import model.player.PlayerType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class GameConfiguration extends JDialog {
    private PlayerType bluePlayerType;
    private PlayerType redPlayerType;
    private JSpinner searchDepthSpinner;

    private static final String HUMAN_TEXT = "Human";
    private static final String COMPUTER_TEXT = "Computer";

    GameConfiguration(final JFrame frame, final boolean modal) {
        super(frame, modal);
        setTitle("Game Configuration");
        final JPanel gameConfigPanel = new JPanel(new GridLayout(0, 1));
        final JRadioButton blueHumanButton = new JRadioButton(HUMAN_TEXT);
        final JRadioButton blueComputerButton = new JRadioButton(COMPUTER_TEXT);
        final JRadioButton redHumanButton = new JRadioButton(HUMAN_TEXT);
        final JRadioButton redComputerButton = new JRadioButton(COMPUTER_TEXT);
        blueHumanButton.setActionCommand(HUMAN_TEXT);
        final ButtonGroup blueGroup = new ButtonGroup();
        blueGroup.add(blueHumanButton);
        blueGroup.add(blueComputerButton);
        blueHumanButton.setSelected(true);

        final ButtonGroup redGroup = new ButtonGroup();
        redGroup.add(redHumanButton);
        redGroup.add(redComputerButton);
        redComputerButton.setSelected(true);

        getContentPane().add(gameConfigPanel);
        gameConfigPanel.add(new JLabel("Blue"));
        gameConfigPanel.add(blueHumanButton);
        gameConfigPanel.add(blueComputerButton);
        gameConfigPanel.add(new JLabel("Red"));
        gameConfigPanel.add(redHumanButton);
        gameConfigPanel.add(redComputerButton);

        final JButton okButton = new JButton("Get Started");

        ActionListener blueButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (blueComputerButton.isSelected()) {
                    redHumanButton.setSelected(true);
                } else if (blueHumanButton.isSelected()){
                    redComputerButton.setSelected(true);
                }
            }
        };
        blueHumanButton.addActionListener(blueButtonListener);
        blueComputerButton.addActionListener(blueButtonListener);

        ActionListener redButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (redComputerButton.isSelected()) {
                    blueHumanButton.setSelected(true);
                } else if (redHumanButton.isSelected()){
                    blueComputerButton.setSelected(true);
                }
            }
        };
        redHumanButton.addActionListener(redButtonListener);
        redComputerButton.addActionListener(redButtonListener);

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bluePlayerType = blueComputerButton.isSelected() ? PlayerType.COMPUTER : PlayerType.HUMAN;
                redPlayerType = redComputerButton.isSelected() ? PlayerType.COMPUTER : PlayerType.HUMAN;
                System.out.println("Set Up Game");
                GameConfiguration.this.setVisible(false);
            }
        });

        gameConfigPanel.add(okButton);
        setSize(190, 200);
        setLocationRelativeTo(null);
        setVisible(false);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                okButton.doClick();
            }
        });
    }

    void promptUser() {
        setVisible(true);
        repaint();
    }

    boolean isAIPlayer(final Player player) {
        if (player.getAllyColor() == PlayerColor.BLUE) {
            return getBluePlayerType() == PlayerType.COMPUTER;
        }
        return getRedPlayerType() == PlayerType.COMPUTER;
    }

    PlayerType getBluePlayerType() {
        return this.bluePlayerType;
    }

    PlayerType getRedPlayerType() {
        return this.redPlayerType;
    }

    int getSearchDepth() {
        return (Integer) this.searchDepthSpinner.getValue();
    }

    void setBluePlayerType(PlayerType bluePlayerType) {
        this.bluePlayerType = bluePlayerType;
    }

    void setRedPlayerType(PlayerType redPlayerType) {
        this.redPlayerType = redPlayerType;
    }
}
