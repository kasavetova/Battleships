import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class WelcomeFrame extends JFrame implements ActionListener{
	
	private JPanel content;
	
	private JLabel backround;
	
	private JLabel title;
	private JLabel prompt;
	private JTextField enterName;
    private JButton connectButton;
    
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket socket;
    private Player player;
    
    private Color backgroundColor = new Color(44, 62, 80);
    private Color textColor = new Color(236, 240, 241);
    
	public WelcomeFrame(Player player){
		this.player = player;
		backround = new JLabel(new ImageIcon("res/homeScreenImage.png"));
		
		//initialise UI
		setSize(new Dimension(350, 500));
        setResizable(false);
        setLocationRelativeTo(null); // centers window on screen, must be called after setSize()
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        content = new JPanel(new GridBagLayout());
		content.setBackground(backgroundColor);
		content.setOpaque(true);
		setContentPane(content);
		
		GridBagConstraints gc = new GridBagConstraints();
		
        title = new JLabel("BATTLESHIP",SwingConstants.CENTER);
        title.setBackground(backgroundColor);
        title.setForeground(textColor);
        title.setOpaque(true);
        title.setFont(new Font("EUROSTILE", Font.BOLD,50));
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 0;
        gc.weighty = 2;
        gc.anchor = GridBagConstraints.PAGE_START;
        gc.fill = GridBagConstraints.BOTH;
        gc.insets = new Insets(5, 5, 5, 5);
        content.add(title, gc);

        prompt = new JLabel();
        prompt.setBackground(backgroundColor);
        prompt.setForeground(textColor);
        prompt.setOpaque(true);
        prompt.setText("<html>" + "<div style=\"text-align: center;\">"
                + "<h2>" + "Welcome to Battleship" + "</h2>" + "<p>"
                + "Enter a nickname for players to identify you with, "
                + "then hit connect!" + "</p></div></html>");
        prompt.setFont(new Font("EUROSTILE", Font.BOLD,18));
        
        gc.gridx = 0;
        gc.gridy = 1;
        gc.weightx = 0;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.PAGE_START;
        gc.fill = GridBagConstraints.BOTH;
        gc.insets = new Insets(5, 5, 5, 5);
        content.add(prompt, gc);

        enterName = new JTextField(30);
        enterName.setFont(new Font("EUROSTILE", Font.BOLD,18));
        gc.gridx = 0;
        gc.gridy = 2;
        gc.weightx = 0.5;
        gc.weighty = 0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.BOTH;
        gc.insets = new Insets(10, 10, 10, 10);
        content.add(enterName, gc);

        connectButton = new JButton();
        connectButton.setText("Connect");
        connectButton.setBackground(backgroundColor);
        connectButton.setOpaque(true);
        connectButton.setFont(new Font("EUROSTILE", Font.BOLD,18));
        gc.gridx = 0;
        gc.gridy = 3;
        gc.weightx = 0;
        gc.weighty = 0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        gc.insets = new Insets(5, 5, 10, 5);
        content.add(connectButton, gc);

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
