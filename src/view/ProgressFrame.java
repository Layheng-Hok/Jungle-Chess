package view;

import javax.swing.*;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static view.GameFrame.defaultImagesPath;

public class ProgressFrame extends JFrame {
    private int process = 0;
    private JLabel label;
    private final ProgressFrame frame;
    private JProgressBar progressBar;
    private String loadingText;
    private final List<ProgressListener> progressListeners = new ArrayList<>();
    private final ImageIcon logo = new ImageIcon(defaultImagesPath + "junglechesslogo.jpg");

    public ProgressFrame(String loadingText) {
        frame = this;
        setBasicProgressFrameAttributes();
        setLoadingTextAndProgressBar();
        setProgressBarUI();
        setVisible(true);
        this.loadingText = loadingText;
        startProgress();
    }

    private void setBasicProgressFrameAttributes() {
        int WIDTH = 300;
        int HEIGHT = 150;
        setTitle("Progress Frame");
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(MainMenuFrame.get());
        setLayout(null);
        setIconImage(logo.getImage());
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    private void setLoadingTextAndProgressBar() {
        label = new JLabel(loadingText);
        label.setLocation(100, 20);
        label.setSize(100, 40);
        label.setFont(new Font("Consolas", Font.BOLD, 16));
        label.setForeground(Color.BLACK);
        add(label);

        progressBar = new JProgressBar();
        progressBar.setBounds(42, 65, 200, 20);
        progressBar.setStringPainted(true);
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setForeground(new Color(8, 237, 237));
        progressBar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        progressBar.setFont(new Font("Consolas", Font.BOLD, 12));
    }

    private void setProgressBarUI() {
        BasicProgressBarUI progressBarUI = new BasicProgressBarUI() {
            @Override
            protected Color getSelectionForeground() {
                return Color.BLACK;
            }

            @Override
            protected Color getSelectionBackground() {
                return Color.BLACK;
            }
        };
        progressBar.setUI(progressBarUI);
        progressBar.setStringPainted(true);
        add(progressBar);
    }

    private void startProgress() {
        Thread thread = new Thread(() -> {
            while (process <= 100) {
                try {
                    Thread.sleep(250);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                int numDots = process / 20;
                StringBuilder dots = new StringBuilder();
                dots.append(".".repeat(Math.max(0, numDots)));
                loadingText = loadingText + dots;

                SwingUtilities.invokeLater(() -> {
                    label.setText(loadingText);
                    repaint();
                });

                progressBar.setValue(process);

                process += 20;
            }

            try {
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }

            SwingUtilities.invokeLater(() -> {
                frame.dispose();
                notifyProgressComplete();
            });
        });
        thread.start();
    }

    public interface ProgressListener {
        void onProgressComplete();
    }

    public void addProgressListener(ProgressListener listener) {
        progressListeners.add(listener);
    }

    private void notifyProgressComplete() {
        for (ProgressListener listener : progressListeners) {
            listener.onProgressComplete();
        }
    }
}
