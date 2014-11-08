import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Kristin on 07-11-14.
 */
public class ConfirmDialog extends JFrame implements ActionListener {
    private JLabel textLabel;
    private JButton yesButton;
    private JButton noButton;
    private Player player;
    private Request input;
    private Timer time;
    private int timeRemaining = 10;

    public ConfirmDialog(final Player player, final Request input) {

        super("Game Request");
        this.player = player;
        this.input = input;
        textLabel = new JLabel("<html><div style=\"text-align:center\"><p>Would you like to play a game with <b>"
                + input.getOrigin() + "</b>?   (10)</p></div></html>");

        textLabel.setFont(new Font("", Font.PLAIN, 13));
        yesButton = new JButton("Yes");
        yesButton.addActionListener(this);
        noButton = new JButton("No");
        noButton.addActionListener(this);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel centerPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        centerPanel.add(buttonPanel, BorderLayout.CENTER);
        centerPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        mainPanel.add(textLabel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        pack();
        getContentPane().add(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(new Dimension(450, 140));
        setResizable(false);

        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                player.refuseRequest(input);
                dispose();
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
                    dispose();
                }
            }
        });
        time.start();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == yesButton) {
            player.acceptRequest(input);
            dispose();

        } else if (e.getSource() == noButton) {
            dispose();
        }
    }

}
