package view;

import javax.swing.*;
import java.awt.*;
import java.io.File;

class DifficultyFrame extends JFrame {
    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(530, 850);
    private JButton easy;
    private JButton medium;
    private JButton hard;
    private JButton back;
    private JLabel background;

    private String iconsFolder = "resource/images" + File.separator;
    private String backgroundIcon = iconsFolder + "background.png";
    private String easyIcon = iconsFolder + "easy.png";
    private String mediumIcon = iconsFolder + "medium.png";
    private String hardIcon = iconsFolder + "hard.png";
    private String backIcon = iconsFolder + "back.png";
    private static Difficulty difficulty = null;

    DifficultyFrame() {
        setTitle("Difficulty Selection");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setBounds(630, 180, 530, 850);

        ImageIcon easyI = new ImageIcon(new ImageIcon(easyIcon).getImage().getScaledInstance
                (310, 90, Image.SCALE_DEFAULT));
        easy = new JButton(easyI);
        easy.setBounds(120, 200, 300, 90);
        this.add(easy);
        easy.addActionListener((e) -> {
            this.setVisible(false);
            setDifficulty(Difficulty.EASY);
            GameFrame.get().show();
            GameFrame.get().getGameConfiguration().promptUser();
            GameFrame.get().setupUpdate(GameFrame.get().getGameConfiguration());
        });
        ImageIcon mediumI = new ImageIcon(new ImageIcon(mediumIcon).getImage().getScaledInstance
                (310, 90, Image.SCALE_DEFAULT));
        medium = new JButton(mediumI);
        medium.setBounds(120, 350, 300, 90);
        this.add(medium);
        medium.addActionListener((e) -> {
            this.setVisible(false);
            setDifficulty(Difficulty.MEDIUM);
            GameFrame.get().show();
            GameFrame.get().getGameConfiguration().promptUser();
            GameFrame.get().setupUpdate(GameFrame.get().getGameConfiguration());
        });
        ImageIcon hardI = new ImageIcon(new ImageIcon(hardIcon).getImage().getScaledInstance
                (310, 90, Image.SCALE_DEFAULT));
        hard = new JButton(hardI);
        hard.setBounds(120, 500, 310, 90);
        this.add(hard);
        hard.addActionListener((e) -> {
            this.setVisible(false);
            setDifficulty(Difficulty.HARD);
            GameFrame.get().show();
            GameFrame.get().getGameConfiguration().promptUser();
            GameFrame.get().setupUpdate(GameFrame.get().getGameConfiguration());
        });

        ImageIcon backI = new ImageIcon(new ImageIcon(backIcon).getImage().getScaledInstance
                (180, 75, Image.SCALE_DEFAULT));
        back = new JButton(backI);
        back.setBounds(50, 650, 170, 70);
        this.add(back);
        back.addActionListener((e) -> {
            this.setVisible(false);
            new MainMenu().setVisible(true);
            System.out.println("Back To Main Menu");
        });

        ImageIcon backgroundI = new ImageIcon(new ImageIcon(backgroundIcon).getImage().getScaledInstance
                (530, 850, Image.SCALE_DEFAULT));
        background = new JLabel(backgroundI);
        background.setBounds(0, 0, 530, 850);
        this.add(background);
        setResizable(false);
    }

    enum Difficulty {
        EASY,
        MEDIUM,
        HARD
    }

    static void setDifficulty(Difficulty difficulty) {
        DifficultyFrame.difficulty = difficulty;
    }

    static Difficulty getDifficulty() {
        return difficulty;
    }
}
