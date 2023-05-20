package view;


import model.board.Board;
import model.board.Move;
import model.board.MoveTransition;
import model.player.PlayerType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static view.GameFrame.defaultImagesPath;

public class MainMenu extends JFrame {
    private JLabel background;
    private JLabel logo;
    private JButton onePlayer;
    private JButton twoPlayer;
    private JButton loadGame;
    private JButton exit;

    private String iconsFolder = defaultImagesPath + File.separator;
    private final ImageIcon logoImage = new ImageIcon(defaultImagesPath + "junglechesslogo.jpg");
    private String jChessIcon = iconsFolder + "logo.png";
    private String backgroundIcon = iconsFolder + "background.png";
    private String onePlayerIcon = iconsFolder + "oneplayer.png";
    private String twoPlayerIcon = iconsFolder + "twoplayer.png";
    private String loadGameIcon = iconsFolder + "loadgame.png";
    private String exitIcon = iconsFolder + "exit.png";

    public MainMenu() {
        setTitle("Jungle Chess (斗兽棋) - Main Menu");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setBounds(630, 180, 530, 850);
        this.setIconImage(logoImage.getImage());
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
                GameFrame.get().getGameConfiguration().setBluePlayerType(PlayerType.HUMAN);
                GameFrame.get().getGameConfiguration().setRedPlayerType(PlayerType.HUMAN);
                System.out.println("Load a Two-Player Game");
            }
        });
        ImageIcon loadGameI = new ImageIcon(new ImageIcon(loadGameIcon).getImage().getScaledInstance
                (178, 74, Image.SCALE_DEFAULT));
        loadGame = new JButton(loadGameI);
        loadGame.setBounds(170, 550, 180, 80);
        this.add(loadGame);
        loadGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File("database"));
                fileChooser.showOpenDialog(null);
                File file = fileChooser.getSelectedFile();

                if (!file.getName().endsWith(".txt")) {
                    JOptionPane.showMessageDialog(null, "The file extension is either missing or not supported.",
                            "File Load Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                ArrayList<String> readList = new ArrayList<>();
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        readList.add(line);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error reading file.",
                            "File Read Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Board loadedBoard = Board.constructStandardBoard();
                GameFrame.MoveLog moveLog = new GameFrame.MoveLog();

                ArrayList<String> playerTypeList;
                int numMoves;
                ArrayList<String> playerList = new ArrayList<>();
                ArrayList<Integer> currentCoordinateList = new ArrayList<>();
                ArrayList<Integer> destinationCoordinateList = new ArrayList<>();
                String playerTypeLine = readList.remove(0);
                try {
                playerTypeList = new ArrayList<>(Arrays.asList(playerTypeLine.split(" "))); }
                catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "The file is corrupted.",
                            "File Load Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                numMoves = Integer.parseInt(readList.remove(0));

                for (int i = 0; i < numMoves; i++) {
                    String moveLine = readList.remove(0);
                    String[] moveTokens = moveLine.split(" ");
                    try {
                        if (moveTokens.length != 3) {
                            JOptionPane.showMessageDialog(null, "The file is corrupted.",
                                    "File Load Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        playerList.add(moveTokens[0]);
                        currentCoordinateList.add(Integer.parseInt(moveTokens[1]));
                        destinationCoordinateList.add(Integer.parseInt(moveTokens[2]));
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "The file is corrupted.",
                                "File Load Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                if (numMoves == currentCoordinateList.size() && numMoves == destinationCoordinateList.size() && numMoves == playerList.size()) {
                    for (int i = 0; i < currentCoordinateList.size(); i++) {
                        Move move = Move.MoveCreator.createMove(loadedBoard, currentCoordinateList.get(i), destinationCoordinateList.get(i));
                        MoveTransition transition = loadedBoard.getCurrentPlayer().makeMove(move);
                        if (transition.getMoveStatus().isDone()) {
                            loadedBoard = transition.getTransitionBoard();
                            moveLog.addMove(move);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "The file is corrupted.",
                            "File Load Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String lastTurn = readList.get(0);
                readList.remove(0);
                playerList.add(lastTurn);
                for (int i = 0; i < playerList.size(); i++) {
                    if (i % 2 == 0) {
                        if (!playerList.get(i).equals("bl")) {
                            JOptionPane.showMessageDialog(null, "The file is corrupted.",
                                    "File Load Error", JOptionPane.ERROR_MESSAGE);
                            System.out.println("Wrong player turns.");
                            return;
                        }
                    } else {
                        if (!playerList.get(i).equals("re")) {
                            JOptionPane.showMessageDialog(null, "The file is corrupted.",
                                    "File Load Error", JOptionPane.ERROR_MESSAGE);
                            System.out.println("Wrong player turns.");
                            return;
                        }
                    }
                }
                int roundNumber = playerList.size() % 2 == 0 ? playerList.size() / 2 : playerList.size() / 2 + 1;
                GameFrame.get().setLoadBoard(loadedBoard, moveLog, roundNumber);
                GameFrame.get().setMoveLog(moveLog);

                if (playerTypeList.get(0).equals("ai") && playerTypeList.get(1).equals("hu")) {
                    GameFrame.get().getGameConfiguration().setBluePlayerType(PlayerType.AI);
                    GameFrame.get().getGameConfiguration().setRedPlayerType(PlayerType.HUMAN);
                    switch (playerTypeList.get(2)) {
                        case "ea" -> DifficultyFrame.setDifficulty(DifficultyFrame.Difficulty.EASY);
                        case "me" -> DifficultyFrame.setDifficulty(DifficultyFrame.Difficulty.MEDIUM);
                        case "ha" -> DifficultyFrame.setDifficulty(DifficultyFrame.Difficulty.HARD);
                    }
                } else if (playerTypeList.get(0).equals("hu") && playerTypeList.get(1).equals("ai")) {
                    GameFrame.get().getGameConfiguration().setBluePlayerType(PlayerType.HUMAN);
                    GameFrame.get().getGameConfiguration().setRedPlayerType(PlayerType.AI);
                    switch (playerTypeList.get(2)) {
                        case "ea" -> DifficultyFrame.setDifficulty(DifficultyFrame.Difficulty.EASY);
                        case "me" -> DifficultyFrame.setDifficulty(DifficultyFrame.Difficulty.MEDIUM);
                        case "ha" -> DifficultyFrame.setDifficulty(DifficultyFrame.Difficulty.HARD);
                    }
                } else if (playerTypeList.get(0).equals("hu") && playerTypeList.get(1).equals("hu")) {
                    GameFrame.get().getGameConfiguration().setBluePlayerType(PlayerType.HUMAN);
                    GameFrame.get().getGameConfiguration().setRedPlayerType(PlayerType.HUMAN);
                } else {
                    JOptionPane.showMessageDialog(null, "The file is corrupted.",
                            "File Load Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                ArrayList<String> animalList = new ArrayList<>();
                ArrayList<Integer> coordinateList = new ArrayList<>();

                int position = 0;
                for (String line : readList) {
                    String[] tokens = line.split("\\s+");
                    for (String token : tokens) {
                        if (token.length() == 2 && Character.isLetter(token.charAt(0)) && Character.isLetter(token.charAt(1))) {
                            animalList.add(token);
                            coordinateList.add(position);
                        } else if (token.length() == 2 && Character.isDigit(token.charAt(0)) && Character.isDigit(token.charAt(1))) {
                            position = Integer.parseInt(token);
                        }
                        position++;
                    }
                }

                Board expectedBoard = Board.constructSpecificBoard(animalList, coordinateList, lastTurn);
                System.out.println(loadedBoard);
                System.out.println(expectedBoard);
                if (!expectedBoard.equals(loadedBoard)) {
                    System.out.println("Board is incorrect");
                    JOptionPane.showMessageDialog(null, "The file is corrupted.",
                            "File Load Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    System.out.println("Board is correct");
                    setVisible(false);
                    GameFrame.get().setVisible(true);
                    System.out.println("Load a Saved Game");
                }
            }
        });

        ImageIcon exitI = new ImageIcon(new ImageIcon(exitIcon).getImage().getScaledInstance
                (178, 74, Image.SCALE_DEFAULT));
        exit = new JButton(exitI);
        exit.setBounds(170, 650, 180, 80);
        this.add(exit);
        exit.addActionListener(e -> {
            System.out.println("Exit");
            System.exit(0);
        });

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
}
