import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * A custom dialog box class represented in a <code>JFrame</code>. Used to send game requests between players.
 *
 * @author Team 1-O
 *
 * @see javax.swing.Timer
 * @see javax.swing.JFrame
 * @see java.awt.event.ActionListener
 * @see Player
 */
public class ConfirmDialog extends JFrame implements ActionListener {
    
    private JLabel textLabel;
    private JButton yesButton;
    private JButton noButton;
    
    private Player player;
    private Request input;
    
    private Timer time;
    private int timeRemaining = 20;

    private Color backgroundColor = new Color(44, 62, 80);
    private Color textColor = new Color(255, 255, 255);


    /**
     * Initialises the dialog box for the {@link Player} when a {@link Request} has been received.
     *
     * @param player the player to receive the dialog box
     * @param input  the request received from the server
     */
    public ConfirmDialog(final Player player, final Request input) {

        super("Game Request");
        this.player = player;
        this.input = input;
        textLabel = new JLabel("<html><div style=\"text-align:center\"><p>Would you like to play a game with <b>"
                + input.getOrigin() + "</b>?   (20)</p></div></html>");

        textLabel.setFont(new Font("EUROSTILE", Font.BOLD, 14));
        textLabel.setForeground(textColor);
        yesButton = new JButton("Yes");
        yesButton.addActionListener(this);
        noButton = new JButton("No");
        noButton.addActionListener(this);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel centerPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout());
        mainPanel.setBackground(backgroundColor);
        centerPanel.setBackground(backgroundColor);
        buttonPanel.setBackground(backgroundColor);
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        centerPanel.add(buttonPanel, BorderLayout.CENTER);
        centerPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        mainPanel.add(textLabel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        
        getContentPane().add(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        pack();

        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                player.refuseRequest(input);
            }
        });

        time = new Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textLabel.getText();
                if (--timeRemaining > 0) {
                    textLabel.setText(text.substring(0, text.lastIndexOf('(') + 1) + timeRemaining + ")</p></div></html>");
                } else {
                    player.refuseRequest(input);
                    time.stop();
                }
            }
        });
        time.start();
    }

    /**
     * Handles the the user's answer for the game request.
     * @param e the event that occurred
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == yesButton) {
            player.acceptRequest(input);
            time.stop();

        } else if (e.getSource() == noButton) {
            player.refuseRequest(input);
            time.stop();
        }
    }

}
