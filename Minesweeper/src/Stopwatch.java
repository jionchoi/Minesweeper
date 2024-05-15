import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Stopwatch extends JFrame implements ActionListener {
    private JLabel timeLabel;
    private JButton startButton, stopButton, resetButton;
    private Timer timer;
    private int elapsedTime;

    public Stopwatch() {
        // Set up the user interface
        timeLabel = new JLabel("00:00:00", JLabel.CENTER);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 50));
        timeLabel.setForeground(Color.red);

        startButton = new JButton("Start");
        startButton.addActionListener(this);
        stopButton = new JButton("Stop");
        stopButton.addActionListener(this);
        resetButton = new JButton("Reset");
        resetButton.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3));
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(resetButton);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(timeLabel, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        // Set up the timer
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                elapsedTime += 1000;
                updateTime();
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            timer.start();
        } else if (e.getSource() == stopButton) {
            timer.stop();
        } else if (e.getSource() == resetButton) {
            timer.stop();
            elapsedTime = 0;
            updateTime();
        }
    }

    private void updateTime() {
        
        String seconds = String.valueOf((elapsedTime % 60000) / 1000);
    
        timeLabel.setText(seconds);
    }

    public static void main(String[] args) {
        Stopwatch frame = new Stopwatch();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 250);
        frame.setVisible(true);
    }
} 

