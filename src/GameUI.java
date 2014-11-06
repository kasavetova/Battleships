import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
    private Player player;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Board bo;

    private JPanel pnlChatWindow;
    private JScrollPane jspAreaChat;
    private JEditorPane txtAreaChat;

    private JPanel pnlChatSender;
    private JTextField txtChat;
    private JButton btnSend;

    private String playerName;
    private String opponentName;

    public GameUI(GameGrid myBoardGrid, ObjectOutputStream outStream,
                  ObjectInputStream inStream, Player player1, Board bo,
                  final String opponentName) {

        super("Battleships");
        this.player = player1;
        this.out = outStream;
        this.in = inStream;
        this.bo = bo;
        this.opponentName = opponentName;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        setSize(900, 600);
        content = new JPanel(new BorderLayout(5, 5));
        content.setBorder(new EmptyBorder(10, 0, 5, 0));
        setContentPane(content);

        playerName = player1.getName();

        pnlBoards = new JPanel(new GridLayout(1, 2, 25, 0));
        pnlMyBoard = new JPanel(new GridLayout(11, 11));
        pnlEnemyBoard = new JPanel(new GridLayout(11, 11));

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

            enemyBoardArray[a][10] = new JLabel(Integer.toString(a),
                    SwingConstants.CENTER);
            enemyBoardArray[0][a - 1] = new JLabel(
                    Character.toString((char) (a + 64)), SwingConstants.CENTER);
        }

        for (int a = 1; a < 11; a++) {
            for (int b = 1; b < 11; b++) {
                myBoardArray[a][b] = (myBoardGrid.getButton(a - 1, b - 1));
                myBoardArray[a][b].setEnabled(false);
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

        pnlBoards.add(pnlMyBoard);
        pnlBoards.add(pnlEnemyBoard);

        pnlPlayerText = new JPanel(new GridLayout(1, 2));

        lblPlayer1 = new JLabel("MY BOARD", SwingConstants.CENTER);
        lblPlayer1.setFont(new Font("DejaVu Sans", Font.BOLD, 24));

        lblPlayer2 = new JLabel("ENEMY BOARD", SwingConstants.CENTER);
        lblPlayer2.setFont(new Font("DejaVu Sans", Font.BOLD, 24));

        pnlPlayerText.add(lblPlayer1);
        pnlPlayerText.add(lblPlayer2);

        pnlChatWindow = new JPanel(new BorderLayout(5, 5));
        txtAreaChat = new JEditorPane();
        txtAreaChat.setEditable(false);
        txtAreaChat.setContentType("text/html");
        txtAreaChat.setSize(Integer.MAX_VALUE, 100);

        jspAreaChat = new JScrollPane(txtAreaChat);

        pnlChatSender = new JPanel(new BorderLayout());
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

        content.add(pnlPlayerText, BorderLayout.NORTH);
        content.add(pnlBoards, BorderLayout.CENTER);
        content.add(pnlChatWindow, BorderLayout.SOUTH);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                try {
                    out.writeObject(new Request("UserLeftGame2", player
                            .getName(), opponentName));
                    // out.writeObject(new Request("UserClosed", name));

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        });
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
        try {
            enemyBoardGrid.getButton(row, col).setEnabled(false);
            enemyBoardGrid.getButton(row, col).removeMouseListener(this);
            out.writeObject(new Request("Move", playerName, opponentName, new GameMove(new Point(row, col), playerName, null)));
            //Disable Board
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

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
            enemyBoardGrid.getButton(p.getX(), p.getY()).setBackground(Color.RED);
        } else if (x.equals("destroyed")) {
            enemyBoardGrid.getButton(p.getX(), p.getY()).setBackground(Color.RED);
            //Tell which ship has been destroyed
            appendMessage("Enemy ship has been destroyed.", "GAME"); //add new line
        } else {
            enemyBoardGrid.getButton(p.getX(), p.getY()).setBackground(Color.CYAN);
        }
    }

    public void updateOwnBoard(String x, Point p) {
        if (x.equals("hit")) {
            myBoardGrid.getButton(p.getX(), p.getY()).setBackground(Color.lightGray);
        } else if (x.equals("destroyed")) {
            myBoardGrid.getButton(p.getX(), p.getY()).setBackground(Color.lightGray);
            //Tell which ship has been destroyed
            appendMessage("Your ship has been destroyed.", "GAME");
        } else {
            myBoardGrid.getButton(p.getX(), p.getY()).setBackground(Color.CYAN);
        }
    }

    public void sendMessage(String message) {
        if (message.length() > 0) {
           /* txtAreaChat.append("\n" + playerName + ": " + message);
            try {
                out.writeObject(new Request("SendMessage", playerName, opponentName, message));
            } catch (IOException e1) {
                e1.printStackTrace();
            }*/
            Document doc = txtAreaChat.getDocument();
            SimpleAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setForeground(attr, Color.darkGray);
            StyleConstants.setBold(attr, true);
            try {
                doc.insertString(doc.getLength(), "\n" + playerName + ": " + message, attr);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
            player.sendServerRequest(new Request("SendMessage", playerName, opponentName, message));
            scrollToBottom();
            txtChat.setText("");
        }
    }

    public void appendMessage(String message, String username) {
        if (message.length() > 0) {
            Document doc = txtAreaChat.getDocument();
            SimpleAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setForeground(attr, Color.blue);
            if (username.equals("GAME")) StyleConstants.setForeground(attr, Color.LIGHT_GRAY);
            StyleConstants.setBold(attr, true);
            try {
                doc.insertString(doc.getLength(), "\n" + username + ": " + message, attr);
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
}
