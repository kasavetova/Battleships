import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public GameUI(GameGrid myBoardGrid, Player player1, Board bo) {

        super("Battleships");
        this.player = player1;

        this.bo = bo;
        this.playerName = player.getName();
        this.opponentName = player.getOpponentName();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        setSize(1200, 500);
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
        //myBoardArray[0][0].setBackground(backroundColor);
        //myBoardArray[0][0].setForeground(textColor);
        
        enemyBoardArray[0][10] = new JLabel("");
        //enemyBoardArray[0][0].setBackground(backroundColor);
        //enemyBoardArray[0][0].setForeground(textColor);
        
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
            enemyBoardArray[0][a-1].setBackground(backroundColor);
            enemyBoardArray[0][a-1].setForeground(textColor);
        }

        for (int a = 1; a < 11; a++) {
            for (int b = 1; b < 11; b++) {
                myBoardArray[a][b] = (myBoardGrid.getButton(a - 1, b - 1));
                //myBoardArray[a][b].setEnabled(false);
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
        lblPlayer1.setFont(new Font("DejaVu Sans", Font.BOLD, 24));
        lblPlayer1.setBackground(backroundColor);
        lblPlayer1.setForeground(textColor);

        lblPlayer2 = new JLabel(opponentName.toUpperCase() + "'s BOARD", SwingConstants.CENTER);
        lblPlayer2.setFont(new Font("DejaVu Sans", Font.BOLD, 24));
        lblPlayer2.setBackground(backroundColor);
        lblPlayer2.setForeground(textColor);
        
        pnlTimerHome = new JPanel(new GridLayout(1, 2));
        pnlTimerHome.setBackground(backroundColor);
        
        lblTimer = new JLabel("TIMER",SwingConstants.CENTER);
        lblTimer.setFont(new Font("DejaVu Sans", Font.PLAIN, 18));
        lblTimer.setBorder(BorderFactory.createLineBorder(textColor, 1));
        lblTimer.setBackground(backroundColor);
        lblTimer.setForeground(textColor);

        btnHome = new JButton("QUIT");
        btnHome.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				player.sendServerRequest(new Request("UserWentBackToLobby", player.getName(), opponentName));
				//player.sendServerRequest(new Request("UserJoinedLobby", player.getName()));
				player.reshowLobby();
				
			}
		});
        btnHome.setFont(new Font("DejaVu Sans", Font.PLAIN, 18));
        
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
		if(player.makeMove(request)) {
		    //enemyBoardGrid.getButton(row, col).setEnabled(false);
		    enemyBoardGrid.getButton(row, col).removeMouseListener(this);
		    cm.end();
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
        	enemyBoardGrid.getButton(p.getX(), p.getY()).setIcon(null);
        	enemyBoardGrid.getButton(p.getX(), p.getY()).setBackground(Color.RED);
        } else if (x.startsWith("destroyed")) {
        	enemyBoardGrid.getButton(p.getX(), p.getY()).setIcon(null);
        	enemyBoardGrid.getButton(p.getX(), p.getY()).setBackground(Color.RED);
            //Tell which ship has been destroyed
            appendMessage("Enemy's " + x.substring(9) + " has been destroyed.", "GAME"); //add new line
        } else {
        	enemyBoardGrid.getButton(p.getX(), p.getY()).setIcon(null);
        	enemyBoardGrid.getButton(p.getX(), p.getY()).setBackground(Color.CYAN);
        }
    }

    public void updateOwnBoard(String x, Point p) {
        if (x.equals("hit")) {
            myBoardGrid.getButton(p.getX(), p.getY()).setBackground(Color.lightGray);
        } else if (x.startsWith("destroyed")) {
            myBoardGrid.getButton(p.getX(), p.getY()).setBackground(Color.lightGray);
            //Tell which ship has been destroyed
            appendMessage("Your "+x.substring(9)+" has been destroyed.", "GAME");
        } else {
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
    public void setLabelText(){
    	lblTimer.setText(opponentName + "'s turn.");
    }

    public void endTurn() throws IOException {
        appendMessage("Your time ran out. You missed your turn.", "GAME");
        Request request = new Request("MoveEnded", playerName, opponentName);
        player.finishMove(request);
    }
    
}
