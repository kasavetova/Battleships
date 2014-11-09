import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Player extends JFrame implements ActionListener {
    private static String name;
    private String opponentName;
    private boolean isTheirTurn;
    private boolean isBusy;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket socket;
    private DefaultListModel<String> playersModel;
    private JLabel chatArea = null;

    private JLabel prompt;
    private JTextField enterName;
    private JButton connectButton;
    private JButton playButton;
    private JPanel rightPanel;
    private JPanel infoPanel;
    private JPanel buttonPanel;
    private JScrollPane scrollPane;
    private JPanel listPanel;
    private ShipPlacementUI sui;
    private GameUI gui;
    private ConfirmDialog confirmDialog;

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
            isBusy = false;
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
                            } 
                            
                            else if (input.getActionType().equals(
                                    "UserLeftLobby")) {
                                playersModel.removeElement(input.getObject());
                            } 
                           
                            else if (input.getDestination().equals(name)) {

                                String actionType = input.getActionType();
                                switch (actionType) {
                                    case "GameRequest":
                                        if (!isBusy) {
                                            isBusy = true;

                                            confirmDialog = new ConfirmDialog(Player.this, input);
                                            confirmDialog.setVisible(true);

                                        } else {
                                            out.writeObject(new Request("PlayerBusy", name, input.getOrigin()));
                                        }
                                        break;
                                    case "GameRequestAnswer":
                                        isBusy = false;
                                        playButton.setEnabled(true);
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
                                        break;
                                    case "RetrieveLobby":
                                        playersModel.clear();
                                        ArrayList<String> playersList = (ArrayList<String>) input.getObject();
                                        for (String aPlayersList : playersList) {
                                            if (!aPlayersList.equals(name)) {
                                                playersModel.addElement(aPlayersList);
                                            }
                                        }
                                        break;
                                    case "ReceiveMessage":
                                        gui.appendMessage((String) input.getObject(), input.getOrigin());
                                        break;
                                    case "UserLeftGame":
                                        // Quitting game
                                        reshowLobby();
                                        JOptionPane.showMessageDialog(null, "Your opponent quit! You win (by default)", 
                                                "Opponent Quit", JOptionPane.INFORMATION_MESSAGE);
                                        break;
                                    case "UserWentBackToLobby":
                                        //Returning to lobby
                                        reshowLobby();
                                        JOptionPane.showMessageDialog(null, "Your opponent went back to lobby",
                                                "Opponent Quit", JOptionPane.INFORMATION_MESSAGE);
                                        break;
                                    case "MoveResult":
                                        GameMove gm = (GameMove) input.getObject();
                                        Point coordinates = gm.getMoveCoordinates();
                                        String playerName = gm.getPlayerName();
                                        String outcome = gm.getMoveResult();

                                        if (name.equals(playerName)) {
                                            //update enemy board
                                            if (outcome.equals("hit") || outcome.startsWith("destroyed")) {
                                                isTheirTurn = true;
                                                gui.startTimer();
                                            }
                                            gui.updateEnemyBoard(outcome, coordinates);
                                        } else {
                                            //update own board
                                            if (!outcome.equals("hit") && !outcome.startsWith("destroyed")) {
                                                isTheirTurn = true;
                                                gui.appendMessage("It's your turn to play.", "GAME");
                                                gui.startTimer();
                                            }
                                            gui.updateOwnBoard(outcome, coordinates);
                                        }
                                        break;
                                    case "MoveEnded":
                                        isTheirTurn = true;
                                        gui.appendMessage("Enemy ran out of time.", "GAME");
                                        gui.startTimer();
                                        break;
                                    case "GameStart":
                                        sui.startGame();
                                        break;
                                    case "PlayerBusy":
                                        isBusy = false;
                                        playButton.setEnabled(true);
                                        JOptionPane.showMessageDialog(null, "Player is busy. Please try again later",
                                                "Error", JOptionPane.ERROR_MESSAGE);
                                        break;
                                    default:
                                        System.out.println(input);
                                        break;
                                }
                            }
                        }
                    } catch (EOFException e) {
                        // EOFException - if this input stream reaches the end
                        // before reading eight bytes
                    } catch (ClassCastException e) {
                        e.printStackTrace();
                        System.out.println("An error occurred.");
                        System.exit(1);
                    } catch (ClassNotFoundException | IOException e) {
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

        // Handles any players closing their game on lobby screen
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
        players.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    JList source = (JList) event.getSource();
                    int index = source.getSelectedIndex();
                    if (index > -1) playButton.setEnabled(true);
                }
            }
        });

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
        playButton.setEnabled(false);
        buttonPanel.add(playButton);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.weightx = 0;
        gc.weighty = 0;
        gc.anchor = GridBagConstraints.PAGE_END;
        gc.fill = GridBagConstraints.BOTH;
        gc.insets = new Insets(5, 5, 5, 5);
        rightPanel.add(buttonPanel, gc);

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (players.getSelectedIndex() >= 0) {
                    String playerName = playersModel.getElementAt(players
                            .getSelectedIndex());
                    if (!(name.equals(playerName))) {
                        try {
                            isBusy = true;
                            playButton.setEnabled(false);

                            //UI WITH TIMER DISPLAYED HERE
                            out.writeObject(new Request("GameRequest", name,
                                    playerName));
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
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
    }

    public void actionPerformed(ActionEvent e) {
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
            try {
                // TODO not have the portnumber and ip hardcoded
                socket = new Socket("localhost", 4446);
                boolean isUnique = checkName(nameToCheck);
                System.out.println(isUnique);
                if (isUnique) {
                    out.writeObject(new Request("Accepted"));
                    name = nameToCheck;
                    this.setTitle("You are logged in as: " + name);
                    mainGUI();
                    newConnection();
                } else {
                    out.writeObject(new Request("Rejected"));
                    out.close();
                    in.close();
                    socket.close();
                    socket = null;
                    prompt.setText("<html>" + "<div style=\"text-align: center;\">"
                            + "<h2>" + "Welcome to Battleship" + "</h2>" + "<p>"
                            + "Enter a nickname for players to identify you with, "
                            + "then hit connect!" + "</p><br><p style=\"color:red\">This " +
                            "username has been taken. Please pick another.</p></div></html>");
                    enterName.setText("");
                    //need to catch java.net.SocketException: Socket closed if user closes
                    //at this stage without proceeding to lobby.
                }

            } catch (UnknownHostException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                JOptionPane.showMessageDialog(null, "Failed to connect to the server", "Server Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public void sendServerRequest(Request request) {
        try {
            out.writeObject(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refuseRequest(Request input) {
        isBusy = false;
        playButton.setEnabled(true);
        try {
            out.writeObject(new Request(
                    "GameRequestAnswer", name,
                    input.getOrigin(), "No"));
            confirmDialog.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void acceptRequest(Request input) {
        isBusy = false;
        playButton.setEnabled(true);
        try {
            out.writeObject(new Request(
                    "GameRequestAnswer", name,
                    input.getOrigin(), "Yes"));
            gameFrame(input.getOrigin());
            opponentName = input.getOrigin();
            confirmDialog.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void placementFinished(GameGrid grid, Board b) {
        gui = new GameUI(grid, out, in, this, b, opponentName);
        gui.setVisible(true);
        try {
            out.writeObject(new Request("GameBoard", name, "SERVER", b));
            if (isTheirTurn) {
                gui.startTimer();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public boolean makeMove (Request request) throws IOException {
        if (isTheirTurn) {
            out.writeObject(request);
            isTheirTurn = false;
            return true;
        }  else {
            gui.appendMessage("It's not your turn yet! Please wait for your opponent.", "GAME");
            return false;
        }
    }

    public void finishMove(Request request) throws IOException {
        out.writeObject(request);
        isTheirTurn = false;
    }

    public String getName() {
        return name;
    }
    
    public boolean checkName(String nameToCheck){
    	try {
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			Request input;
			while ((input = (Request) in.readObject()) != null) {
				if (input.getActionType().equals("RetrieveLobby")) {
					ArrayList<String> playersList = (ArrayList<String>) input.getObject();
					if(playersList != null){
						for (int i = 0; i < playersList.size(); i++) {
							System.out.println(playersList);
							if (playersList.get(i).equals(nameToCheck)) {
								return false;
							}
						}	
					}
					break;
				}
			}
        } catch (ClassNotFoundException | IOException e) {
            // TODO Auto-generated catch block
			e.printStackTrace();
        }
        return true;
    }
    public void reshowLobby(){
    	 mainGUI();
         setVisible(true);
         opponentName = null;
         
         if(sui !=null){
         	sui.dispose();
         }
         if(gui !=null){
         	gui.dispose();
         }
         playersModel.clear();
         try {
        	
			out.writeObject(new Request(
			         "RetrieveLobby", name));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
