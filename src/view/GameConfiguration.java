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
    private static final String HUMAN = "Human";
    private static final String COMPUTER = "Computer";
    private static final String defaultImagesPath = "resource/images/";
    private boolean isReady = false;

    GameConfiguration(final JFrame frame, final boolean modal) {
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
                bluePlayerType = blueComputerButton.isSelected() ? PlayerType.AI : PlayerType.HUMAN;
                redPlayerType = redComputerButton.isSelected() ? PlayerType.AI : PlayerType.HUMAN;
                GameConfiguration.this.setVisible(false);
                isReady = true;
                System.out.println("Set Up Game");
            }
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

    boolean isAIPlayer(final Player player) {
        if (player.getAllyColor() == PlayerColor.BLUE) {
            return getBluePlayerType() == PlayerType.AI;
        }
        return getRedPlayerType() == PlayerType.AI;
    }

    PlayerType getBluePlayerType() {
        return this.bluePlayerType;
    }

    void setBluePlayerType(PlayerType bluePlayerType) {
        this.bluePlayerType = bluePlayerType;
    }

    PlayerType getRedPlayerType() {
        return this.redPlayerType;
    }

    void setRedPlayerType(PlayerType redPlayerType) {
        this.redPlayerType = redPlayerType;
    }

    int getSearchDepth() {
        return 4;
    }

    boolean isReady() {
        return isReady;
    }

    void setReady(boolean isReady) {
        this.isReady = isReady;
    }
}
