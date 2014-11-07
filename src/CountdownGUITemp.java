import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Muntasir Syed
 */

public class CountdownGUITemp extends JFrame {

    private JLabel timeLabel = new JLabel("Timer");
    
    private Countdown countdown = new Countdown();
    
    public CountdownGUITemp() throws HeadlessException {
        
        super("tester");

        add(timeLabel, BorderLayout.CENTER);
        
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(300, 300));
        setVisible(true);
    }

    private void setTimer(String sTime) {
        timeLabel.setText(sTime);
    }
    
    private void startTimer() {
        countdown.startCount();
    }
    
    private void turnOver() {
        // called by the timer when it finishes
    }

    private class Countdown implements ActionListener {
        
        private int timerDelay = 1000;  // todo
        boolean active;
        private Timer timer = new Timer(timerDelay, this);
        
        int totalTime;

        public Countdown() {
            totalTime = 120;
            setTimer(timeFormatter(totalTime));
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {

            if (totalTime > 0) {
                totalTime--;
                setTimer(timeFormatter(totalTime));
            } else {
                active = false;
                turnOver();
            }


        }
        
        public void startCount() {
            totalTime = 120;
            active = true;
            timer.start();
        }
        
    }

    private String timeFormatter(int count) {

        String s = " ";
        
        int hours = count / 3600;
        int minutes = (count - hours * 3600) / 60;
        int seconds = count - minutes * 60;

        s = String.format("%02d", minutes) + " : " + String.format("%02d", seconds);
        
        return s;
    }

    public static void main(String[] args) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CountdownGUITemp();
            }
        });
    }
}
