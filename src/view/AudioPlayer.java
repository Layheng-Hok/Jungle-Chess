package view;

import model.piece.Piece;

import javax.sound.sampled.*;
import java.io.File;

public class AudioPlayer {
    private static final String defaultAudioPath = "resources/audio/";

    public static class LoopPlayer {
        private static Clip clip;
        private static Thread thread;
        private static boolean isMenuBGMPlaying = false;

        public static void playAudio(String path) {
            try {
                File soundPath = new File(defaultAudioPath + path);
                if (soundPath.exists()) {
                    AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath);
                    clip = AudioSystem.getClip();
                    clip.open(audioInput);
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                    thread = new Thread(() -> {
                        try {
                            while (true) {
                                Thread.sleep(100);
                            }
                        } catch (InterruptedException ignored) {
                        }
                    });
                    thread.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void stopLoopAudio() {
            if (clip != null && thread != null) {
                clip.stop();
                clip.close();
                thread.interrupt();
                thread = null;
            }
        }

        public static void setMenuBGMPlaying(boolean isMenuBGMPlaying) {
            LoopPlayer.isMenuBGMPlaying = isMenuBGMPlaying;
        }

        public static void playMenuBGM() {
            if (!isMenuBGMPlaying) {
                stopLoopAudio();
                playAudio("menubgm.wav");
                isMenuBGMPlaying = true;
            }
        }

        public static void playGameBGM() {
            stopLoopAudio();
            playAudio("gamebgm.wav");
            isMenuBGMPlaying = false;
        }
    }

    public static class SinglePlayer {
        private static Clip clip;

        public static void playAnimalSoundEffect(Piece piece) {
            if (!MainMenuFrame.get().isGrayScaleSoundEffectButton()) {
                String animalName = piece.getPieceType().toString().toLowerCase();
                String path = defaultAudioPath + animalName + ".wav";
                try {
                    File soundPath = new File(path);
                    if (soundPath.exists()) {
                        AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath);
                        clip = AudioSystem.getClip();
                        clip.open(audioInput);
                        clip.start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public static void playSoundEffect(String path) {
            if (!MainMenuFrame.get().isGrayScaleSoundEffectButton()) {
                try {
                    File soundPath = new File(defaultAudioPath + path);
                    if (soundPath.exists()) {
                        AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath);
                        clip = AudioSystem.getClip();
                        clip.open(audioInput);
                        clip.start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
