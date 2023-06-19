package view;

import model.piece.Piece;

import javax.sound.sampled.*;
import java.io.File;

public class AudioPlayer {
    private static String defaultAudioPath = "resource/audio/";

    private static class LoopPlayer {
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

        public static void stopAudio() {
            if (clip != null && thread != null) {
                clip.stop();
                clip.close();
                thread.interrupt();
                thread = null;
            }
        }
    }

    public static void playMenuBGM() {
        if (!LoopPlayer.isMenuBGMPlaying) {
            LoopPlayer.stopAudio();
            LoopPlayer.playAudio("menubgm.wav");
            LoopPlayer.isMenuBGMPlaying = true;
        }
    }

    public static void playGameBGM() {
        LoopPlayer.stopAudio();
        LoopPlayer.playAudio("gamebgm.wav");
        LoopPlayer.isMenuBGMPlaying = false;
    }

    public static void stopBGM() {
        LoopPlayer.stopAudio();
    }

    public static class SinglePlayer {
        private static Clip clip;

        public static void playAnimalSoundEffect(Piece piece) {
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

        public static void playSoundEffect(String path) {
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
