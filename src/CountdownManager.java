import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Muntasir Syed
 */

public class CountdownManager {

    private JLabel timeLabel;
    private GameUI gameUI;
    
    private Countdown countdown = new Countdown();
    
    public CountdownManager(JLabel timeLabel, GameUI gameUI) throws HeadlessException {
        this.timeLabel = timeLabel;
        this.gameUI = gameUI;
        
    }

    private void setTimer(String sTime) {
        timeLabel.setText(sTime);
    }

    /**
     * Call this method to start the timer.
     */
    private void startTimer() {
        countdown.startCount();
    }

    /**
     * This method is called in line 63 when the timer finishes
     */
    private void turnOver() {
        // called by the timer when it finishes
        // call something in GameUI when turn finishes
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
                // this is they ran this class
            }
        });
    }
}
