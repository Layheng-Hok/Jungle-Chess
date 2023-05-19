package view;

import javax.swing.*;
import java.awt.*;
import java.io.File;

class DifficultyFrame extends JFrame {
    /*private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(530, 850);
    private JButton easyButton;
    private JButton mediumButton;
    private JButton hardButton;
    private static Difficulty difficulty = null;

    DifficultyFrame() {
        initializeFrame();
        createButtons();
        addButtonsToFrame();
    }

    private void initializeFrame() {
        setTitle("Difficulty Selection");
        setSize(new Dimension(OUTER_FRAME_DIMENSION));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());
        setContentPane(new JLabel(new ImageIcon("background.png")));
    }

    private void createButtons() {
        easyButton = new JButton();
        easyButton.setText("Easy");
        easyButton.setSize(300, 55);
        easyButton.setLocation(120, 300);
        easyButton.addActionListener((e) -> {
            this.setVisible(false);
            setDifficulty(Difficulty.EASY);
            GameFrame.get().setVisible(true);
            GameFrame.get().getGameConfiguration().promptUser();
            GameFrame.get().setupUpdate(GameFrame.get().getGameConfiguration());
        });

        // easyButton.setIcon(new ImageIcon("easy_icon.png")); // Replace "easy_icon.png" with the icon image for the Easy button
        mediumButton = new JButton();
        mediumButton.setText("Medium");
        mediumButton.setSize(300, 60);
        mediumButton.setLocation(120, 410);
        mediumButton.addActionListener((e) -> {
            this.setVisible(false);
            setDifficulty(Difficulty.MEDIUM);
            GameFrame.get().setVisible(true);
            GameFrame.get().getGameConfiguration().promptUser();
            GameFrame.get().setupUpdate(GameFrame.get().getGameConfiguration());
        });
        // mediumButton.setIcon(new ImageIcon("medium_icon.png")); // Replace "medium_icon.png" with the icon image for the Medium button
        hardButton = new JButton("Hard");
        hardButton.setSize(300, 60);
        hardButton.setLocation(120, 520);
        hardButton.addActionListener((e) -> {
            this.setVisible(false);
            setDifficulty(Difficulty.HARD);
            GameFrame.get().setVisible(true);
            GameFrame.get().getGameConfiguration().promptUser();
            GameFrame.get().setupUpdate(GameFrame.get().getGameConfiguration());
        });
        //  hardButton.setIcon(new ImageIcon("hard_icon.png")); // Replace "hard_icon.png" with the icon image for the Hard button
    }

    private void addButtonsToFrame() {
        this.add(easyButton);
        this.add(mediumButton);
        this.add(hardButton);
    }*/
    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(530, 850);
    private JButton easy;
    private JButton medium;
    private JButton hard;
    private JButton back;
    private JLabel background;

    private String iconsFolder = "resource/images" + File.separator;
    private String backgroundIcon = iconsFolder +"background.png";
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
            GameFrame.get().setVisible(true);
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
            GameFrame.get().setVisible(true);
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
            GameFrame.get().setVisible(true);
            GameFrame.get().getGameConfiguration().promptUser();
            GameFrame.get().setupUpdate(GameFrame.get().getGameConfiguration());
        });

        ImageIcon backI = new ImageIcon(new ImageIcon(backIcon).getImage().getScaledInstance
                (180, 75, Image.SCALE_DEFAULT));
        back = new JButton(backI);
        back.setBounds(50, 650, 170, 70);
        this.add(back);
        back.addActionListener((e) -> {

            //this.setVisible(false);
            // GameFrame.get().getGameConfiguration().promptUser();
            // GameFrame.get().setupUpdate(GameFrame.get().getGameConfiguration());
        });
        //background label
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

    private static void setDifficulty(Difficulty difficulty) {
        DifficultyFrame.difficulty = difficulty;
    }

    static Difficulty getDifficulty() {
        return difficulty;
    }
}
