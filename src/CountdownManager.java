import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * @author Muntasir Syed
 */

public class CountdownManager {

    private JLabel timeLabel;
    private GameUI gui;
    private String opponentName;
    private Timer countdownTimer;
    private int timeRemaining = 11;

    public CountdownManager(JLabel timeLabel, GameUI gui, String opponentName) {
        this.timeLabel = timeLabel;
        this.gui = gui;
        this.opponentName = opponentName;
    }

    public void start() {
        timeRemaining = 11;
        timeLabel.setText("TIMER: (10)");
        countdownTimer = new Timer(1000, new TimerListener());
        countdownTimer.start();
    }

    class TimerListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String currentText = timeLabel.getText();
            if (--timeRemaining > 0) {
                timeLabel.setText(currentText.substring(0, currentText.lastIndexOf('(') + 1) + timeRemaining + ")");
            } else {
                end();
                try {
                    gui.endTurn();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void end() {
        countdownTimer.stop();
        timeLabel.setText(opponentName + "'s turn.");
    }
}
