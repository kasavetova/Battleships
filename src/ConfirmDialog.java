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
    private int timeRemaining = 20;

    public ConfirmDialog(final Player player, final Request input) {

        super("Game Request");
        this.player = player;
        this.input = input;
        textLabel = new JLabel("<html><div style=\"text-align:center\"><p>Would you like to play a game with <b>"
                + input.getOrigin() + "</b>?   (20)</p></div></html>");

        textLabel.setFont(new Font("EUROSTILE", Font.BOLD, 14));
        textLabel.setForeground(new Color(135,206,235));
        yesButton = new JButton("Yes");
        yesButton.addActionListener(this);
        noButton = new JButton("No");
        noButton.addActionListener(this);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel centerPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout());
        mainPanel.setBackground(new Color(44, 62, 80));
        centerPanel.setBackground(new Color(44, 62, 80));
        buttonPanel.setBackground(new Color(44, 62, 80));
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
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(new Dimension(450, 140));
        setResizable(false);

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
