package model;

import model.player.PlayerType;
import view.DifficultyFrame;
import view.GameFrame;
import view.MainMenu;
import view.ProgressFrame;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Controller {
    public static void writeGame(String fileName) {
        String location = "database\\" + fileName + ".txt";
        if (!fileName.matches("[^\\\\/:*?\"<>|]+")) {
            JOptionPane.showMessageDialog(null, "A file name cannot contain any illegal characters.", "Invalid File Name", JOptionPane.ERROR_MESSAGE);
            return;
        }
        File file = new File(location);
        try {
            if (file.exists()) {
                int response = JOptionPane.showConfirmDialog(null, "The file already exists, do you want to overwrite it?",
                        "Overlapped File Name", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.NO_OPTION) {
                    return;
                }
            }
            FileWriter fileWriter = new FileWriter(file);
            String difficulty = "nu";
            if (GameFrame.get().getGameConfiguration().getBluePlayerType() == PlayerType.HUMAN &&
                    GameFrame.get().getGameConfiguration().getRedPlayerType() == PlayerType.AI
                    || GameFrame.get().getGameConfiguration().getBluePlayerType() == PlayerType.AI &&
                    GameFrame.get().getGameConfiguration().getRedPlayerType() == PlayerType.HUMAN) {
                difficulty = DifficultyFrame.getDifficulty().toString().toLowerCase().substring(0, 2);
            }
            fileWriter.write(GameFrame.get().getGameConfiguration().getBluePlayerType().toString().toLowerCase().substring(0, 2) + " "
                    + GameFrame.get().getGameConfiguration().getRedPlayerType().toString().toLowerCase().substring(0, 2) + " "
                    + difficulty + "\n");
            fileWriter.write(String.valueOf(GameFrame.get().getMoveLog().size()) + "\n");
            for (int i = 0; i < GameFrame.get().getMoveLog().size(); i++) {
                fileWriter.write(GameFrame.get().getMoveLog().getMove(i).toString() + "\n");
            }
            fileWriter.write(GameFrame.get().getChessBoard().getCurrentPlayer().toString().toLowerCase().substring(0, 2) + "\n");
            fileWriter.write(GameFrame.get().getChessBoard().toString());
            fileWriter.close();
            ProgressFrame progressFrame = new ProgressFrame();
            progressFrame.addProgressListener(new ProgressFrame.ProgressListener() {
                @Override
                public void onProgressComplete() {
                    int continueResponse = JOptionPane.showOptionDialog(null, "Game saved successfully.\nWould you want to continue playing or go back to the main menu?", "Game Saved", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Continue", "Back"}, "Continue");
                    if (continueResponse == JOptionPane.NO_OPTION) {
                        GameFrame.get().getGameConfiguration().setBluePlayerType(PlayerType.HUMAN);
                        GameFrame.get().getGameConfiguration().setRedPlayerType(PlayerType.HUMAN);
                        GameFrame.get().restartGame();
                        GameFrame.get().dispose();
                        new MainMenu().setVisible(true);
                        System.out.println("Back To Main Menu");
                    }
                }
            });
            System.out.println("Game Saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
