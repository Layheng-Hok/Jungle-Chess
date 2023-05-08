package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainMenu extends JFrame   {
    private JLabel background;
    private JLabel logo;
    private JButton onePlayer;
    private JButton twoPlayer;
    private JButton rule;

    private String iconsFolder="icons"+ File.separator;
    private String jChessIcon=iconsFolder+"logo.png";
    private String backgroundIcon=iconsFolder+"background.png";
    private String onePlayerIcon=iconsFolder+"oneplayer.png";
    private String twoPlayerIcon=iconsFolder+"twoplayer.png";
    private String ruleIcon=iconsFolder+"rule.png";
    public MainMenu(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setBounds(630, 180, 525, 675);
        this.getContentPane().setBackground(Color.GRAY);
        ImageIcon onePlayerI = new ImageIcon(new ImageIcon(onePlayerIcon).getImage().getScaledInstance
                (240, 90, Image.SCALE_DEFAULT));
        onePlayer = new JButton(onePlayerI);
        onePlayer.setBounds(173, 326, 240, 80);
        this.add(onePlayer);
        //onePlayer.addActionListener(actions);

        ImageIcon twoPlayerI = new ImageIcon(new ImageIcon(twoPlayerIcon).getImage().getScaledInstance
                (250, 65, Image.SCALE_DEFAULT));
        twoPlayer = new JButton(twoPlayerI);
        twoPlayer.setBounds(173, 424, 240, 60);
        this.add(twoPlayer);
        //twoPlayer.addActionListener(actions);
        ImageIcon ruleI = new ImageIcon(new ImageIcon(ruleIcon).getImage().getScaledInstance
                (129, 47, Image.SCALE_DEFAULT));
        rule = new JButton(ruleI);
        rule.setBounds(240, 512, 100, 44);
        this.add(rule);
        //rule.addActionListener(actions);

        ImageIcon jChessI = new ImageIcon(new ImageIcon(jChessIcon).getImage().getScaledInstance
                (340, 230, Image.SCALE_DEFAULT));
        logo = new JLabel(jChessI);
        logo.setBounds(100, 0, 400, 300);
        this.add(logo);

        ImageIcon backgroundI = new ImageIcon(new ImageIcon(backgroundIcon).getImage().getScaledInstance
                (600, 661, Image.SCALE_DEFAULT));
        background = new JLabel(backgroundI);
        background.setBounds(0, 0, 600, 661);
        this.add(background);

        setResizable(false);
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

