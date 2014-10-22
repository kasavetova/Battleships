package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Muntasir Syed
 */

public class WelcomeFrame extends JFrame implements Observer {

    private JLabel prompt;
    private JTextField enterName;
    private JButton connectButton;

    /**
     * Constructor of the Welcome dialog.
     */
    public WelcomeFrame() throws HeadlessException {

        super("Welcome");

        create();
        setSize(new Dimension(260, 325));
        setResizable(false);
        setLocationRelativeTo(null); // centers window on screen, must be called after setSize()
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }


    /**
     * Initialises and adds all widgets to the interface.
     */
    public void create() {

        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        prompt = new JLabel();
        prompt.setText("<html>" +
                "<div style=\"text-align: center;\">" +
                "<h2>" +
                "Welcome to Battleship" +
                "</h2>" +
                "<p>" +
                "Enter a nickname for players to identify you with, " +
                "then hit connect!" +
                "</p></html>");
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 0;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.PAGE_START;
        gc.fill = GridBagConstraints.BOTH;
        gc.insets = new Insets(5, 5, 5, 5);
        add(prompt, gc);

        enterName = new JTextField(30);
        gc.gridx = 0;
        gc.gridy = 1;
        gc.weightx = 0.5;
        gc.weighty = 0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.BOTH;
        gc.insets = new Insets(10, 10, 10, 10);
        add(enterName, gc);

        connectButton = new JButton();
        connectButton.setText("Connect");
        gc.gridx = 0;
        gc.gridy = 2;
        gc.weightx = 0;
        gc.weighty = 0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        gc.insets = new Insets(5, 5, 10, 5);
        add(connectButton, gc);

    }

    public void addEnterNameListener(ActionListener l) {
        enterName.addActionListener(l);
    }

    public void addConnectButtonListener(ActionListener l) {
        connectButton.addActionListener(l);
    }

    public static void main(String[] args) {
        new WelcomeFrame();
    }

    @Override
    public void update(Observable o, Object arg) {
        // TODO - Observer update
    }
}
