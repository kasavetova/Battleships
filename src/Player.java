import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

public class Player extends JFrame implements ActionListener {
	private static String name;
	private String opponentName;
	private boolean isTheirTurn;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Socket socket;
	private DefaultListModel<String> playersModel;
	private JLabel chatArea = null;

	private JLabel prompt;
	private JTextField enterName;
	private JButton connectButton;
	private JButton playButton;
	private JButton playRandButton;
	private JPanel rightPanel;
	private JPanel infoPanel;
	private JPanel buttonPanel;
	private JScrollPane scrollPane;
	private JPanel listPanel;
	private ShipPlacementUI sui;
	private GameUI gui;

	public static void main(String[] args) {
		new Player().setVisible(true);
	}

	public Player() {
		super("Welcome");
		initialiseGUI();
		setSize(new Dimension(260, 325));
		setResizable(false);
		setLocationRelativeTo(null); // centers window on screen, must be called
										// after setSize()
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public void newConnection() {
		try {

			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			opponentName = null;
			isTheirTurn = false;
			out.writeObject(new Request("UserJoinedLobby", name));
			out.writeObject(new Request("RetrieveLobby", name));

			Thread receivingThread = new Thread() {
				Request input;

				@Override
				public void run() {
					try {

						while ((input = (Request) in.readObject()) != null) {
							if (input.getActionType().equals("UserJoinedLobby")) {
								if (!input.getObject().equals(name)) {
									playersModel.addElement((String) input
											.getObject());
								}
							} else if (input.getActionType().equals(
									"UserLeftLobby")) {
								playersModel.removeElement(input.getObject());
							} else if (input.getDestination().equals(name)) {
								if (input.getActionType().equals("GameRequest")) {
									// check if there is already a dialog box
									// opened.
									// if user accepts one of the game requests
									// - the other dialogs are closed
									// and an answer 'no' is sent to the server
									int requestAnswer = JOptionPane
											.showConfirmDialog(null,
													"Do you want to play a game with "
															+ input.getOrigin()
															+ "?",
													"Game Request",
													JOptionPane.YES_NO_OPTION);
									// no = 1, yes = 0
									if (requestAnswer == 1) {
										out.writeObject(new Request(
												"GameRequestAnswer", name,
												input.getOrigin(), "No"));
									}
									if (requestAnswer == 0) {
										out.writeObject(new Request(
												"GameRequestAnswer", name,
												input.getOrigin(), "Yes"));
										gameFrame(input.getOrigin());
										opponentName = input.getOrigin();
									}
								} else if (input.getActionType().equals(
										"GameRequestAnswer")) {
									if (input.getObject().equals("Yes")) {
										gameFrame(input.getOrigin());
										opponentName = input.getOrigin();
										isTheirTurn = true;
									} else if (input.getObject().equals("No")) {
										JOptionPane.showMessageDialog(null,
												"Game request denied from "
														+ input.getOrigin()
														+ ".",
												"Game request denied",
												JOptionPane.ERROR_MESSAGE);
									}
								} else if (input.getActionType().equals(
										"RetrieveLobby")) {
									ArrayList<String> playersList = (ArrayList<String>) input
											.getObject();
									for (int i = 0; i < playersList.size(); i++) {
										if (!playersList.get(i).equals(name)) {
											// System.out.println(playersList.get(i));
											playersModel.addElement(playersList
													.get(i).toString());
										}
									}
								} else if (input.getActionType().equals(
										"ReceiveMessage")) {
									String originalText = chatArea.getText()
											.replaceAll("<html>", "")
											.replaceAll("</html>", "");
									String newText = "<html>" + originalText
											+ "<br><b>" + input.getOrigin()
											+ ":</b> " + input.getObject()
											+ "</html>";
									chatArea.setText(newText);
								} else if (input.getActionType().equals(
										"UserLeftGame")) {
									// Quitting game on selection screen
									mainGUI();
									setVisible(true);
									opponentName = null;
									sui.dispose();
									out.writeObject(new Request(
											"UserJoinedLobby", name));
									out.writeObject(new Request(
											"RetrieveLobby", name));
									JOptionPane
											.showMessageDialog(
													null,
													"Your opponent quit! You win (by default)",
													"Opponent Quit",
													JOptionPane.INFORMATION_MESSAGE);

								} else if (input.getActionType().equals(
										"UserLeftGame2")) {
									// Quitting game on game screen
									mainGUI();
									setVisible(true);
									opponentName = null;
									out.writeObject(new Request(
											"UserJoinedLobby", name));
									out.writeObject(new Request(
											"RetrieveLobby", name));
									gui.dispose();
									JOptionPane
											.showMessageDialog(
													null,
													"Your opponent quit! You win (by default)",
													"Opponent Quit",
													JOptionPane.INFORMATION_MESSAGE);

								} else if (input.getActionType().equals(
										"RandomGameRequestFail")) {
									JOptionPane
											.showMessageDialog(
													null,
													"We couldn't find any active players at the moment. Please try again later.",
													"Error",
													JOptionPane.ERROR_MESSAGE);

								} else if (input.getActionType().equals("MoveResult")) {
									GameMove gm = (GameMove) input.getObject();
									Point coordinates = gm.getMoveCoordinates();
									String playerName = gm.getPlayerName();
				                	String outcome = gm.getMoveResult();
				                	
				                	if(name.equals(playerName)) {
				                		//update own board
				                	} else if(name.equals(opponentName)) {				                		
				                		//update opponent board
				                	}				                	
									isTheirTurn = true;
								} else {
									System.out.println(input);
								}
							}
						}
					} catch (EOFException e) {
						// EOFException - if this input stream reaches the end
						// before reading eight bytes
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			};
			receivingThread.start();
		} catch (EOFException e) {
			// EOFException - if this input stream reaches the end before
			// reading eight bytes
		} catch (IOException e) {

			JOptionPane.showMessageDialog(null,
					"Failed to connect to the server", "Server Error",
					JOptionPane.WARNING_MESSAGE);

		}
	}

	public void initialiseGUI() {

		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		prompt = new JLabel();
		prompt.setText("<html>" + "<div style=\"text-align: center;\">"
				+ "<h2>" + "Welcome to Battleship" + "</h2>" + "<p>"
				+ "Enter a nickname for players to identify you with, "
				+ "then hit connect!" + "</p></html>");
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

		// Handles any players closing their game
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {

				if (socket != null) {
					try {

						out.writeObject(new Request("UserClosed", name));

					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		// Enter button actionlistener
		connectButton.addActionListener(this);
		// Text field listener
		enterName.addActionListener(this);
	}

	public void mainGUI() {

		getContentPane().removeAll();
		setLayout(new BorderLayout());
		GridBagConstraints gc = new GridBagConstraints();

		listPanel = new JPanel(new GridBagLayout());
		add(listPanel, BorderLayout.CENTER);

		playersModel = new DefaultListModel<String>();
		final JList<String> players = new JList<String>(playersModel);
		players.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		scrollPane = new JScrollPane(players);
		TitledBorder b = new TitledBorder("Currently online:");
		b.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setViewportBorder(b);

		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.anchor = GridBagConstraints.CENTER;
		gc.fill = GridBagConstraints.BOTH;
		gc.insets = new Insets(5, 5, 5, 5);
		listPanel.add(scrollPane, gc);

		rightPanel = new JPanel(new GridBagLayout());
		add(rightPanel, BorderLayout.EAST);

		infoPanel = new JPanel();

		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 0;
		gc.weighty = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.fill = GridBagConstraints.HORIZONTAL;
		rightPanel.add(infoPanel, gc);

		buttonPanel = new JPanel(new GridLayout());
		playButton = new JButton("Play");
		playRandButton = new JButton("Play Random");
		buttonPanel.add(playButton);
		buttonPanel.add(playRandButton);

		gc.gridx = 0;
		gc.gridy = 1;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.anchor = GridBagConstraints.PAGE_END;
		gc.fill = GridBagConstraints.BOTH;
		gc.insets = new Insets(5, 5, 5, 5);
		rightPanel.add(buttonPanel, gc);

		playRandButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					System.out.println("here");
					out.writeObject(new Request("RandomGameRequest", name));
				} catch (IOException e1) {
					System.out.println("CATCH FROM RANDOM BTN"
							+ e1.getMessage());
				}
			}
		});

		players.addMouseListener(new MouseAdapter() {
			// Repeated Code
			@Override
			public void mouseClicked(MouseEvent evt) {
				// need to disable clicked item until a response has been
				// received.
				if (evt.getClickCount() == 2) {
					String playerName = playersModel.getElementAt(players
							.getSelectedIndex());
					if (!(name.equals(playerName))) {
						try {
							out.writeObject(new Request("GameRequest", name,
									playerName));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		playButton.addActionListener(new ActionListener() {
			// Repeated Code
			@Override
			public void actionPerformed(ActionEvent e) {
				// need to disable clicked item until a response has been
				// received.
				String playerName = playersModel.getElementAt(players
						.getSelectedIndex());
				if (!(name.equals(playerName))) {
					try {
						out.writeObject(new Request("GameRequest", name,
								playerName));
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		setMinimumSize(new Dimension(520, 370));
		pack();

	}

	public void gameFrame(String opponentName) {

		setVisible(false);
		sui = new ShipPlacementUI(this, out, in, name, opponentName);
		sui.setVisible(true);
		/*
		 * setPreferredSize(new Dimension(500, 500)); JPanel east = new
		 * JPanel(new BorderLayout()); JPanel eastSouth = new JPanel(new
		 * BorderLayout()); chatArea = new JLabel(); final JTextField
		 * chatMessage = new JTextField(); JButton sendButton = new
		 * JButton("Send"); east.add(chatArea, BorderLayout.CENTER);
		 * eastSouth.add(chatMessage, BorderLayout.CENTER);
		 * eastSouth.add(sendButton, BorderLayout.EAST); east.add(eastSouth,
		 * BorderLayout.SOUTH); east.setPreferredSize(new Dimension(200, 200));
		 * add(east, BorderLayout.EAST); pack();
		 * 
		 * 
		 * chatMessage.addActionListener(new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent e) {
		 * if(chatMessage.getText().length()>0) { String originalText =
		 * chatArea.getText().replaceAll("<html>", "").replaceAll("</html>",
		 * ""); String newText = null; if(originalText.indexOf("<br>")==0)
		 * {originalText.replace("<br>", "");} if(originalText.length()==0) {
		 * newText = "<html>" + originalText + name + ": " +
		 * chatMessage.getText() + "</html>"; } //this is to remove else {
		 * newText = "<html>" + originalText + "<br><b>" + name + ":</b> " +
		 * chatMessage.getText() + "</html>"; } //br tags at start of message
		 * chatArea.setText(newText); try { out.writeObject(new
		 * Request("SendMessage", name, opponentName, chatMessage.getText())); }
		 * catch (IOException e1) { e1.printStackTrace(); }
		 * chatMessage.setText(""); } } });
		 * 
		 * sendButton.addActionListener(new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent e) {
		 * if(chatMessage.getText().length()>0) { String originalText =
		 * chatArea.getText().replaceAll("<html>", "").replaceAll("</html>",
		 * ""); String newText = null; if(originalText.indexOf("<br>")==0)
		 * {originalText.replace("<br>", "");} if(originalText.length()==0) {
		 * newText = "<html>" + originalText + name + ": " +
		 * chatMessage.getText() + "</html>"; } //this is to remove else {
		 * newText = "<html>" + originalText + "<br><b>" + name + ":</b> " +
		 * chatMessage.getText() + "</html>"; } //br tags at start of message
		 * chatArea.setText(newText); try { out.writeObject(new
		 * Request("SendMessage", name, opponentName, chatMessage.getText())); }
		 * catch (IOException e1) { e1.printStackTrace(); }
		 * chatMessage.setText(""); } } });
		 */
	}

	public void actionPerformed(ActionEvent e) {

		name = enterName.getText().toString();
		try {
			// TODO not have the portnumber and ip hardcoded
			socket = new Socket("localhost", 4446);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,
					"Failed to connect to the server", "Server Error",
					JOptionPane.WARNING_MESSAGE);
		}
		if (socket != null) {
			mainGUI();
			newConnection();
			// TODO ensure no one has the same username
		}
	}

	public void placementFinished(GameGrid grid, ObjectOutputStream out,
			ObjectInputStream in, Board b) {
		gui = new GameUI(grid, out, in, this, b, opponentName);
		gui.setVisible(true);
		try {
			out.writeObject(new Request("GameBoard", name, "SERVER", b));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void makeMove(Point coordinates) throws IOException {
		if (isTheirTurn) {
			out.writeObject(new Request("Move", name, opponentName, new GameMove(coordinates, name, null)));
			isTheirTurn = false;
		}
	}
	
	public String getName(){
		return name;
	}

}
