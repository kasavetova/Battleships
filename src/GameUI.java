

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class GameUI extends JFrame implements MouseListener{

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
	Board bo;
	
	public GameUI(GameGrid myBoardGrid, ObjectOutputStream out, ObjectInputStream in, Player player, Board bo) {
		
		super("Battleships");
		this.player = player;
		this.out = out;
		this.in = in;
		this.bo = bo;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setSize(900, 500);
		content = new JPanel(new BorderLayout());
		content.setBorder(new EmptyBorder(10, 0, 10, 0));
		setContentPane(content);
		
		pnlBoards = new JPanel(new GridLayout(1, 2, 25, 0));
		pnlMyBoard = new JPanel(new GridLayout(11,11));
		pnlEnemyBoard = new JPanel(new GridLayout(11,11));
		
		this.myBoardGrid = myBoardGrid;
		enemyBoardGrid = new GameGrid(10,10);

		myBoardArray = new JComponent[11][11];
		enemyBoardArray = new JComponent[11][11];
		
		myBoardArray[0][0] = new JLabel("");
		enemyBoardArray[0][10] = new JLabel("");
		
		for (int a = 1; a < 11; a++) {
			myBoardArray[a][0] = new JLabel(Integer.toString(a),SwingConstants.CENTER);
			myBoardArray[0][a] = new JLabel(Character.toString((char) (a + 64)),SwingConstants.CENTER);
			
			enemyBoardArray[a][10] = new JLabel(Integer.toString(a),SwingConstants.CENTER);
			enemyBoardArray[0][a-1] = new JLabel(Character.toString((char) (a + 64)),SwingConstants.CENTER);
		}
		
		for (int a = 1; a < 11; a++) {
			for (int b = 1; b < 11; b++) {
				myBoardArray[a][b] = (myBoardGrid.getButton(a-1, b-1));
				myBoardArray[a][b].setEnabled(false);
				enemyBoardArray[a][b-1] = (enemyBoardGrid.getButton(a-1, b-1));
				enemyBoardArray[a][b-1].addMouseListener(this);
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
		
		pnlPlayerText = new JPanel(new GridLayout(1,2));
		
		lblPlayer1 = new JLabel("MY BOARD",SwingConstants.CENTER);
		lblPlayer1.setFont(new Font("DejaVu Sans", Font.BOLD, 24));
		
		lblPlayer2 = new JLabel("ENEMY BOARD",SwingConstants.CENTER);
		lblPlayer2.setFont(new Font("DejaVu Sans", Font.BOLD, 24));
		
		pnlPlayerText.add(lblPlayer1);
		pnlPlayerText.add(lblPlayer2);		
		
		content.add(pnlPlayerText, BorderLayout.NORTH);
		content.add(pnlBoards, BorderLayout.CENTER);
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
		
		enemyBoardGrid.getButton(row, col).setEnabled(false);
		enemyBoardGrid.getButton(row, col).removeMouseListener(this);
		String x = bo.shoot(new Point(row, col));
		if (x.equals("hit") || x.equals("destroyed")){
			enemyBoardGrid.getButton(row, col).setBackground(Color.RED);
		}
		else{
			enemyBoardGrid.getButton(row, col).setBackground(Color.CYAN);
		}
	
		/*
		if(enemyBoardGrid.getButton(row, col).isOccupied() == true){
			enemyBoardGrid.getButton(row, col).setBackground(Color.RED);
		}else{
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


}
