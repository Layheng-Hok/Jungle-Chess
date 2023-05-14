package view;

import model.player.Player;
import model.player.PlayerColor;
import model.player.PlayerType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class GameConfiguration extends JDialog {

    private PlayerType bluePlayerType;
    private PlayerType redPlayerType;
    private JSpinner searchDepthSpinner;

    private static final String HUMAN_TEXT = "Human";
    private static final String COMPUTER_TEXT = "Computer";

    GameConfiguration(final JFrame frame,
                      final boolean modal) {
        super(frame, modal);
        final JPanel myPanel = new JPanel(new GridLayout(0, 1));
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
        redHumanButton.setSelected(true);

        getContentPane().add(myPanel);
        myPanel.add(new JLabel("Blue"));
        myPanel.add(blueHumanButton);
        myPanel.add(blueComputerButton);
        myPanel.add(new JLabel("Red"));
        myPanel.add(redHumanButton);
        myPanel.add(redComputerButton);

        myPanel.add(new JLabel("Search"));
        this.searchDepthSpinner = addLabeledSpinner(myPanel, "Search Depth", new SpinnerNumberModel(6, 0, Integer.MAX_VALUE, 1));

        final JButton cancelButton = new JButton("Cancel");
        final JButton okButton = new JButton("Ok");

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bluePlayerType = blueComputerButton.isSelected() ? PlayerType.COMPUTER : PlayerType.HUMAN;
                redPlayerType = redComputerButton.isSelected() ? PlayerType.COMPUTER : PlayerType.HUMAN;
                GameConfiguration.this.setVisible(false);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Cancel");
                GameConfiguration.this.setVisible(false);
            }
        });

        myPanel.add(cancelButton);
        myPanel.add(okButton);

        setLocationRelativeTo(frame);
        pack();
        setVisible(false);
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

    private static JSpinner addLabeledSpinner(final Container c,
                                              final String label,
                                              final SpinnerModel model) {
        final JLabel l = new JLabel(label);
        c.add(l);
        final JSpinner spinner = new JSpinner(model);
        l.setLabelFor(spinner);
        c.add(spinner);
        return spinner;
    }

    int getSearchDepth() {
        return (Integer) this.searchDepthSpinner.getValue();
    }
}
