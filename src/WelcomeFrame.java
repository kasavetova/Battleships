import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class WelcomeFrame extends JFrame implements ActionListener{
	
	private JLabel prompt;
	private JTextField enterName;
    private JButton connectButton;
    
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket socket;
    private Player player;
    
	public WelcomeFrame(Player player){
		this.player = player;
		
		//initialise UI
		setSize(new Dimension(260, 325));
        setResizable(false);
        setLocationRelativeTo(null); // centers window on screen, must be called after setSize()
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        prompt = new JLabel();
        prompt.setText("<html>" + "<div style=\"text-align: center;\">"
                + "<h2>" + "Welcome to Battleship" + "</h2>" + "<p>"
                + "Enter a nickname for players to identify you with, "
                + "then hit connect!" + "</p></div></html>");
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

        // Enter button actionlistener
        connectButton.addActionListener(this);
        // Text field listener
        enterName.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		String text = enterName.getText();
		String nameToCheck = text.replaceAll("\\s+", "");
		if (nameToCheck.length() < 1) {
			prompt.setText("<html>" + "<div style=\"text-align: center;\">"
					+ "<h2>" + "Welcome to Battleship" + "</h2>" + "<p>"
					+ "Enter a nickname for players to identify you with, "
					+ "then hit connect!" + "</p><br><p style=\"color:red\">" +
					"Please enter a username.</p></div></html>");
			enterName.setText("");

		} else if (nameToCheck.length() > 16) {
			prompt.setText("<html>" + "<div style=\"text-align: center;\">"
					+ "<h2>" + "Welcome to Battleship" + "</h2>" + "<p>"
					+ "Enter a nickname for players to identify you with, "
					+ "then hit connect!" + "</p><br><p style=\"color:red\">" +
					"Your username can't be longer than 16 characters.</p></div></html>");
			enterName.setText("");

		} else {
			boolean isUnique = player.checkName(nameToCheck);
			if (isUnique) {
				player.closeWelcomeFrame(nameToCheck);
			} else {
				prompt.setText("<html>" + "<div style=\"text-align: center;\">"
						+ "<h2>" + "Welcome to Battleship" + "</h2>" + "<p>"
						+ "Enter a nickname for players to identify you with, "
						+ "then hit connect!" + "</p><br><p style=\"color:red\">This " +
						"username has been taken. Please pick another.</p></div></html>");
				enterName.setText("");
				//need to catch java.net.SocketException: Socket closed if user closes
				//at this stage without proceeding to lobby.
			}
		}

	}
}
