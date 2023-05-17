package view;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainMenu extends JFrame {
    private JLabel background;
    private JLabel logo;
    private JButton onePlayer;
    private JButton twoPlayer;
    private JButton loadGame;

    private String iconsFolder = GameFrame.defaultImagesPath + File.separator;
    private String jChessIcon = iconsFolder + "logo.png";
    private String backgroundIcon = iconsFolder + "background.png";
    private String onePlayerIcon = iconsFolder + "oneplayer.png";
    private String twoPlayerIcon = iconsFolder + "twoplayer.png";
    private String loadGameIcon = iconsFolder + "loadgame.png";

    public MainMenu() {
        setTitle("Main Menu");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setBounds(630, 180, 530, 850);
        this.getContentPane().setBackground(Color.GRAY);
        ImageIcon onePlayerI = new ImageIcon(new ImageIcon(onePlayerIcon).getImage().getScaledInstance
                (178, 74, Image.SCALE_DEFAULT));
        onePlayer = new JButton(onePlayerI);
        onePlayer.setBounds(170, 350, 180, 80);
        this.add(onePlayer);
        onePlayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new DifficultyFrame().setVisible(true);
                System.out.println("Load Difficulty Frame");
            }
        });

        ImageIcon twoPlayerI = new ImageIcon(new ImageIcon(twoPlayerIcon).getImage().getScaledInstance
                (178, 74, Image.SCALE_DEFAULT));
        twoPlayer = new JButton(twoPlayerI);
        twoPlayer.setBounds(170, 450, 180, 80);
        this.add(twoPlayer);
        twoPlayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                GameFrame.get().show();
                GameFrame.get().setVisible(true);
                System.out.println("Load a Two-Player Game");
            }
        });
        ImageIcon loadGameI = new ImageIcon(new ImageIcon(loadGameIcon).getImage().getScaledInstance
                (178, 74, Image.SCALE_DEFAULT));
        loadGame = new JButton(loadGameI);
        loadGame.setBounds(170, 550, 180, 80);
        this.add(loadGame);
        //rule.addActionListener(actions);

        ImageIcon jChessI = new ImageIcon(new ImageIcon(jChessIcon).getImage().getScaledInstance
                (390, 390, Image.SCALE_DEFAULT));
        logo = new JLabel(jChessI);
        logo.setBounds(70, 0, 400, 300);
        this.add(logo);

        ImageIcon backgroundI = new ImageIcon(new ImageIcon(backgroundIcon).getImage().getScaledInstance
                (530, 850, Image.SCALE_DEFAULT));
        background = new JLabel(backgroundI);
        background.setBounds(0, 0, 530, 850);
        this.add(background);

        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }

    /* private ActionListener actions = new ActionListener()
     {
         @Override
         public void actionPerformed(ActionEvent e)
         {
             if(e.getSource() == onePlayer)
             {
                 setVisible(false);
                 jChessMain.showDifficultiesForm(); //show chess board with AI
             }

             else if(e.getSource() == twoPlayer){
                 setVisible(false);
                 jChessMain.showLeaderboard();// show chess board for two players
             }

             else if(e.getSource() == rule){
                 setVisible(false);
                 jChessMain.showLeaderboard();// show rule frame
             }
         }
     };*/
}

