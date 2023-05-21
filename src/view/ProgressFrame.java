package view;

import javax.swing.*;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static view.GameFrame.defaultImagesPath;

public class ProgressFrame extends JFrame {
    private Thread thread;
    private final int WIDTH;
    private final int HEIGHT;
    private int process;
    private JLabel label;
    private ProgressFrame frame;
    private JProgressBar progressBar;
    private String loadingText;
    private List<ProgressListener> progressListeners = new ArrayList<>();
    private final ImageIcon logo = new ImageIcon(defaultImagesPath + "junglechesslogo.jpg");

    public ProgressFrame() {
        thread = new Thread();
        WIDTH = 300;
        HEIGHT = 150;
        process = 0;
        frame = this;
        loadingText = "Progress";

        setTitle("In Progress");
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setLayout(null);
        setIconImage(logo.getImage());

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

        setVisible(true);

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (process <= 100) {
                    try {
                        Thread.sleep(250);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    int numDots = process / 20;
                    StringBuilder dots = new StringBuilder();
                    dots.append(".".repeat(Math.max(0, numDots)));
                    loadingText = "Progress" + dots;

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            label.setText(loadingText);
                            repaint();
                        }
                    });

                    progressBar.setValue(process);

                    process += 20;
                }

                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        frame.dispose();
                        notifyProgressComplete();
                    }
                });
            }
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
