import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class GameUI extends JFrame implements MouseListener {

    private JPanel content;
    private JPanel pnlBoards;
    private JPanel pnlMyBoard;
    private JPanel pnlEnemyBoard;

    private GameGrid myBoardGrid;
    private GameGrid enemyBoardGrid;

    private JComponent[][] myBoardArray;
    private JComponent[][] enemyBoardArray;

    private JPanel pnlPlayerText;
    private JLabel lblPlayer1;
    private JLabel lblPlayer2;

    private JPanel pnlTimerHome;
    private JLabel lblTimer;
    private JButton btnHome;

    private Player player;

    private Board bo;

    private JPanel pnlChatWindow;
    private JScrollPane jspAreaChat;
    private JEditorPane txtAreaChat;

    private JPanel pnlChatSender;
    private JTextField txtChat;
    private JButton btnSend;

    private String playerName;
    private String opponentName;

    private Color backroundColor = new Color(44, 62, 80);
    private Color textColor = new Color(236, 240, 241);

    private CountdownManager cm;
    
    private int shipsLeft = 5;
    private boolean gameFinished = false;

    public GameUI(GameGrid myBoardGrid, Player player1, Board bo) {

        super("Battleships");
        this.player = player1;

        this.bo = bo;
        this.playerName = player.getName();
        this.opponentName = player.getOpponentName();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        setLocationRelativeTo(null);
        setSize(1300, 500);
        content = new JPanel(new BorderLayout(5, 5));
        content.setBorder(BorderFactory.createLineBorder(backroundColor, 5));
        content.setBackground(backroundColor);
        setContentPane(content);

        playerName = player1.getName();

        pnlBoards = new JPanel(new GridLayout(1, 2, 25, 0));
        pnlBoards.setBackground(backroundColor);

        pnlMyBoard = new JPanel(new GridLayout(11, 11));
        pnlMyBoard.setBackground(backroundColor);

        pnlEnemyBoard = new JPanel(new GridLayout(11, 11));
        pnlEnemyBoard.setBackground(backroundColor);

        this.myBoardGrid = myBoardGrid;
        enemyBoardGrid = new GameGrid(10, 10);

        myBoardArray = new JComponent[11][11];
        enemyBoardArray = new JComponent[11][11];

        myBoardArray[0][0] = new JLabel("");
        enemyBoardArray[0][10] = new JLabel("");

        for (int a = 1; a < 11; a++) {
            myBoardArray[a][0] = new JLabel(Integer.toString(a),
                    SwingConstants.CENTER);
            myBoardArray[0][a] = new JLabel(
                    Character.toString((char) (a + 64)), SwingConstants.CENTER);
            myBoardArray[a][0].setBackground(backroundColor);
            myBoardArray[a][0].setForeground(textColor);
            myBoardArray[0][a].setBackground(backroundColor);
            myBoardArray[0][a].setForeground(textColor);


            enemyBoardArray[a][10] = new JLabel(Integer.toString(a),
                    SwingConstants.CENTER);
            enemyBoardArray[0][a - 1] = new JLabel(
                    Character.toString((char) (a + 64)), SwingConstants.CENTER);
            enemyBoardArray[a][10].setBackground(backroundColor);
            enemyBoardArray[a][10].setForeground(textColor);
            enemyBoardArray[0][a - 1].setBackground(backroundColor);
            enemyBoardArray[0][a - 1].setForeground(textColor);
        }

        for (int a = 1; a < 11; a++) {
            for (int b = 1; b < 11; b++) {
                myBoardArray[a][b] = (myBoardGrid.getButton(a - 1, b - 1));
                enemyBoardArray[a][b - 1] = (enemyBoardGrid.getButton(a - 1,
                        b - 1));
                enemyBoardArray[a][b - 1].addMouseListener(this);
            }
        }

        for (int a = 0; a < 11; a++) {
            for (int b = 0; b < 11; b++) {
                pnlMyBoard.add(myBoardArray[a][b]);
                pnlEnemyBoard.add(enemyBoardArray[a][b]);
            }
        }

        pnlPlayerText = new JPanel(new GridLayout(1, 3));
        pnlPlayerText.setBackground(backroundColor);

        lblPlayer1 = new JLabel("MY BOARD", SwingConstants.CENTER);
        lblPlayer1.setFont(new Font("EUROSTILE", Font.BOLD, 24));
        lblPlayer1.setBackground(backroundColor);
        lblPlayer1.setForeground(textColor);

        lblPlayer2 = new JLabel(opponentName.toUpperCase() + "'s BOARD", SwingConstants.CENTER);
        lblPlayer2.setFont(new Font("EUROSTILE", Font.BOLD, 24));
        lblPlayer2.setBackground(backroundColor);
        lblPlayer2.setForeground(textColor);

        pnlTimerHome = new JPanel(new GridLayout(1, 2));
        pnlTimerHome.setBackground(backroundColor);

        lblTimer = new JLabel("TIMER", SwingConstants.CENTER);
        lblTimer.setFont(new Font("EUROSTILE", Font.BOLD, 18));
        lblTimer.setBorder(BorderFactory.createLineBorder(textColor, 1));
        lblTimer.setBackground(backroundColor);
        lblTimer.setForeground(textColor);

        btnHome = new JButton("QUIT");
        btnHome.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            	if(gameFinished){
					player.sendServerRequest(new Request("UserWentBackToLobby", player.getName(), opponentName, "Finished"));
				}
				else{
					player.sendServerRequest(new Request("UserWentBackToLobby", player.getName(), opponentName, "NotFinished"));
				}

				player.reshowLobby();
				
            }
        });
        btnHome.setFont(new Font("EUROSTILE", Font.BOLD, 18));

        pnlTimerHome.add(lblTimer);
        pnlTimerHome.add(btnHome);

        pnlPlayerText.add(lblPlayer1);
        pnlPlayerText.add(pnlTimerHome);
        pnlPlayerText.add(lblPlayer2);

        pnlChatWindow = new JPanel(new BorderLayout(5, 5));
        pnlChatWindow.setBackground(backroundColor);

        txtAreaChat = new JEditorPane();
        txtAreaChat.setEditable(false);
        txtAreaChat.setContentType("text/html");
        txtAreaChat.setText("<i>Chat messages will appear here...</i>");
        txtAreaChat.setMinimumSize(new Dimension(Integer.MAX_VALUE, 100));

        jspAreaChat = new JScrollPane(txtAreaChat);

        pnlChatSender = new JPanel(new BorderLayout());
        pnlChatSender.setBackground(backroundColor);

        txtChat = new JTextField();

        txtChat.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage(txtChat.getText());
                }
            }
        });

        btnSend = new JButton("SEND");
        btnSend.setFont(new Font("EUROSTILE", Font.BOLD, 14));

        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage(txtChat.getText());
            }
        });

        pnlChatSender.add(txtChat, BorderLayout.CENTER);
        pnlChatSender.add(btnSend, BorderLayout.EAST);

        pnlChatWindow.add(jspAreaChat, BorderLayout.CENTER);
        pnlChatWindow.add(pnlChatSender, BorderLayout.SOUTH);

        pnlBoards.add(pnlMyBoard);
        pnlBoards.add(pnlChatWindow);
        pnlBoards.add(pnlEnemyBoard);

        content.add(pnlPlayerText, BorderLayout.NORTH);
        content.add(pnlBoards, BorderLayout.CENTER);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                player.sendServerRequest(new Request("UserLeftGame", player.getName(), opponentName));
            }

        });
        cm = new CountdownManager(lblTimer, this, opponentName);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        int row = ((GameButton) e.getSource()).getRow();
        int col = ((GameButton) e.getSource()).getColumn();

        enemyBoardGrid.getButton(row, col).setEnabled(false);
        enemyBoardGrid.getButton(row, col).setBackground(Color.BLUE);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        int row = ((GameButton) e.getSource()).getRow();
        int col = ((GameButton) e.getSource()).getColumn();

        enemyBoardGrid.getButton(row, col).setEnabled(true);
        enemyBoardGrid.getButton(row, col).setBackground(null);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = ((GameButton) e.getSource()).getRow();
        int col = ((GameButton) e.getSource()).getColumn();
        Request request = new Request("Move", playerName, opponentName, new GameMove(new Point(row, col), playerName, null));
        

        if(!gameFinished){
        	if (player.makeMove(request)) {
        		//enemyBoardGrid.getButton(row, col).setEnabled(false);
        		enemyBoardGrid.getButton(row, col).removeMouseListener(this);
        		cm.end();
        	}
        }
        //Disable Board

		/*
        if (x.equals("hit") || x.equals("destroyed")) {
			enemyBoardGrid.getButton(row, col).setBackground(Color.RED);
		} else {
			enemyBoardGrid.getButton(row, col).setBackground(Color.CYAN);
		}
		*/


    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    public void updateEnemyBoard(String x, Point p) {
        if (x.equals("hit")) {
        	//sound effect
        	playSound("hit");
        	
        	enemyBoardGrid.getButton(p.getX(), p.getY()).setBackground(Color.BLACK);   	
        	
        	//TODO fix the change of color when button is disabled!
        	//START OF ANIMATION!
        	enemyBoardGrid.getButton(p.getX(), p.getY()).setEnabled(true);//to be fixed!!!     
        	//explodes
            enemyBoardGrid.getButton(p.getX(), p.getY()).setIcon(new ImageIcon("res/explosion.gif"));
            try {
				Thread.sleep(500);
			} catch (InterruptedException e) {//sleeps for a bit to show the explosion
				e.printStackTrace();
			}
            //sets the flames to show that it was destroyed
            enemyBoardGrid.getButton(p.getX(), p.getY()).setIcon(new ImageIcon("res/destroyed.gif"));
            enemyBoardGrid.getButton(p.getX(), p.getY()).setBackground(null);//don't think is needed?
            //END OF ANIMATION!
        } else if (x.startsWith("destroyed")) {
            //sound effect
        	playSound("destroyed");
        	enemyBoardGrid.getButton(p.getX(), p.getY()).setBackground(Color.BLACK);
        	//START OF ANIMATION!
        	enemyBoardGrid.getButton(p.getX(), p.getY()).setEnabled(true);//to be fixed!!!     
        	//explodes
            enemyBoardGrid.getButton(p.getX(), p.getY()).setIcon(new ImageIcon("res/explosion.gif"));
            try {
				Thread.sleep(500);
			} catch (InterruptedException e) {//sleeps for a bit to show the explosion
				e.printStackTrace();
			}
            //sets the flames to show that it was destroyed
            enemyBoardGrid.getButton(p.getX(), p.getY()).setIcon(new ImageIcon("res/destroyed.gif"));
            enemyBoardGrid.getButton(p.getX(), p.getY()).setBackground(null);//don't think is needed?
            //END OF ANIMATION!
            //Tell which ship has been destroyed
            appendMessage("Enemy's " + x.substring(9) + " has been destroyed.", "GAME"); //add new line
            decrementLife();
        } else {
        	playSound("missed");
            enemyBoardGrid.getButton(p.getX(), p.getY()).setIcon(new ImageIcon("res/splash.png"));
            enemyBoardGrid.getButton(p.getX(), p.getY()).setEnabled(true);
            enemyBoardGrid.getButton(p.getX(), p.getY()).setBackground(Color.CYAN);
        }
    }

    public void updateOwnBoard(String x, Point p) {
        if (x.equals("hit")) {
            playSound("hit");

            myBoardGrid.getButton(p.getX(), p.getY()).setBackground(Color.BLACK);

            //TODO fix the change of color when button is disabled!
            //START OF ANIMATION!
            myBoardGrid.getButton(p.getX(), p.getY()).setEnabled(true);//to be fixed!!!
            //explodes
            myBoardGrid.getButton(p.getX(), p.getY()).setIcon(new ImageIcon("res/explosion.gif"));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {//sleeps for a bit to show the explosion
                e.printStackTrace();
            }
            //sets the flames to show that it was destroyed
            myBoardGrid.getButton(p.getX(), p.getY()).setIcon(new ImageIcon("res/destroyed.gif"));
            myBoardGrid.getButton(p.getX(), p.getY()).setBackground(null);//don't think is needed?
            //END OF ANIMATION!
        } else if (x.startsWith("destroyed")) {
            playSound("destroyed");
            myBoardGrid.getButton(p.getX(), p.getY()).setBackground(Color.BLACK);
            //START OF ANIMATION!
            myBoardGrid.getButton(p.getX(), p.getY()).setEnabled(true);//to be fixed!!!
            //explodes
            myBoardGrid.getButton(p.getX(), p.getY()).setIcon(new ImageIcon("res/explosion.gif"));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {//sleeps for a bit to show the explosion
                e.printStackTrace();
            }
            //sets the flames to show that it was destroyed
            myBoardGrid.getButton(p.getX(), p.getY()).setIcon(new ImageIcon("res/destroyed.gif"));
            myBoardGrid.getButton(p.getX(), p.getY()).setBackground(null);//don't think is needed?
            //END OF ANIMATION!
            //Tell which ship has been destroyed
            appendMessage("Your " + x.substring(9) + " has been destroyed.", "GAME");
            decrementLife();
        } else {
            playSound("missed");
            myBoardGrid.getButton(p.getX(), p.getY()).setIcon(new ImageIcon("res/splash.png"));
            myBoardGrid.getButton(p.getX(), p.getY()).setEnabled(true);
            myBoardGrid.getButton(p.getX(), p.getY()).setBackground(Color.CYAN);
        }
    }

    public void sendMessage(String message) {
        if (message.length() > 0) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String timeStamp = dateFormat.format(date).substring(11);
            Document doc = txtAreaChat.getDocument();
            SimpleAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setForeground(attr, Color.darkGray);
            StyleConstants.setBold(attr, true);
            try {
                doc.insertString(doc.getLength(), "\n" + "[" + timeStamp + "] " + playerName + ": " + message, attr);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
            player.sendServerRequest(new Request("SendMessage", playerName, opponentName, message));
            //scrollToBottom();
            txtChat.setText("");
        }
    }

    public void appendMessage(String message, String username) {
        if (message.length() > 0) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String timeStamp = dateFormat.format(date).substring(11);
            Document doc = txtAreaChat.getDocument();
            SimpleAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setForeground(attr, Color.blue);
            if (username.equals("GAME")) StyleConstants.setForeground(attr, Color.ORANGE);
            StyleConstants.setBold(attr, true);
            try {
                doc.insertString(doc.getLength(), "\n" + "[" + timeStamp + "] " + username + ": " + message, attr);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
            scrollToBottom();
        }
    }

    public void scrollToBottom() {
        jspAreaChat.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                e.getAdjustable().setValue(e.getAdjustable().getMaximum());
            }
        });
    }

    public void startTimer() {
        cm.start();
    }

    public void setLabelText() {
        lblTimer.setText(opponentName + "'s turn.");
    }

    public void endTurn() throws IOException {
        appendMessage("Your time ran out. You missed your turn.", "GAME");
        Request request = new Request("MoveEnded", playerName, opponentName);
        player.finishMove(request);
    }
    public void decrementLife(){
    	if (--shipsLeft == 0){
    		appendMessage("You have won", "GAME");
    		player.makeMove(new Request("GameFinished", player.getName(), player.getOpponentName()));
    		setGameFinished(true);
    	}
    }

	public void setGameFinished(boolean b) {
		gameFinished = b;
		cm.endGame();
		
	}
	
    public void playSound(String effect) {
    	Random rand = new Random();
    	String path = String.format("res/sounds/%s.wav", effect);
    	System.out.println(path);
        File in = new File(path);
        Clip play;
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(in);
			play = AudioSystem.getClip();
			play.open(audioInputStream);
	        play.start();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //FloatControl volume= (FloatControl)play.getControl(FloatControl.Type.MASTER_GAIN);
        //volume.setValue(1.0f); // Reduce volume by 10 decibels.

         SwingUtilities.invokeLater(new Runnable() {
             public void run() {
                 // A GUI element to prevent the Clip's daemon Thread
                 // from terminating at the end of the main()
                 System.out.print("hiit!");
            }
         });
     }

}
