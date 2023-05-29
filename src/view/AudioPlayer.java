package view;

import model.piece.Piece;

import javax.sound.sampled.*;
import java.io.File;

public class AudioPlayer {
    private static class LoopPlayer {
        private static Clip clip;
        private static Thread thread;

        public static void playAudio(String path) {
            try {
                File soundPath = new File(path);
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
                        } catch (InterruptedException e) {
                           // e.printStackTrace();
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
        LoopPlayer.stopAudio();
        LoopPlayer.playAudio("resource/audio/menubgm.wav");
    }

    public static void playGameBGM() {
        LoopPlayer.stopAudio();
        LoopPlayer.playAudio("resource/audio/gamebgm.wav");
    }

    public static class SinglePlayer {
        public static void playAnimalSoundEffect(Piece piece) {
            String animalName = piece.getPieceType().toString().toLowerCase();
            String path = "resource/audio/" + animalName + ".wav";
            try {
                File soundPath = new File(path);
                if (soundPath.exists()) {
                    AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioInput);
                    clip.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void playClickEffect() {
            try {
                File soundPath = new File("resource/audio/click.wav");
                if (soundPath.exists()) {
                    AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioInput);
                    clip.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
