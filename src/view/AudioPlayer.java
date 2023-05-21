package view;

import javax.sound.sampled.*;
import java.io.File;

public class AudioPlayer {
    private static class LoopPlayer {
        private static Clip clip;
        private static Thread thread;

        public static void playAudio(String path) {
            try {
                File musicPath = new File(path);
                if (musicPath.exists()) {
                    AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                    clip = AudioSystem.getClip();
                    clip.open(audioInput);
                    clip.loop(Clip.LOOP_CONTINUOUSLY);

                    thread = new Thread(() -> {
                        try {
                            while (true) {
                                Thread.sleep(100);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
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
}
