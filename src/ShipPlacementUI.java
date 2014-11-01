

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class ShipPlacementUI extends JFrame implements ActionListener,MouseListener {

	private JPanel content;
	private JPanel pnlNorth;
	private JPanel pnlGrid;

	private GameGrid gameGrid;
	private JComponent[][] arrayGrid;
	
	private int rows = 10;
	private int cols = 10;

	private int shipSize = 5;
	private int shipHorVert = 1;
	private int shipsLeftToPlace = 5;
	
	private ButtonGroup btgShips;
	private JRadioButton btnShip5;
	private JRadioButton btnShip4;
	private JRadioButton btnShip3a;
	private JRadioButton btnShip3b;
	private JRadioButton btnShip2;

	private JPanel pnlShip5;
	private JPanel pnlShip4;
	private JPanel pnlShip3a;
	private JPanel pnlShip3b;
	private JPanel pnlShip2;
	private JPanel pnlShipHolder;

	private ButtonGroup btgHorizontalVertical;
	private JRadioButton btnHorizontal;
	private JRadioButton btnVertical;
	
	private JPanel pnlHorizontalVertical;
	
	private Color backroundColor;
	private Color buttonColor;
	private Color hoverColor;
	private Color selectedColor;
	
	private JButton btnConfirm;
	private Board b;

	private Player player;
	//public static void main(String args[]) {
		//new ShipPlacementUI().setVisible(true);
	//}

	public ShipPlacementUI(final Player player, final ObjectOutputStream out, final ObjectInputStream in, final String name, final String opponentName) {
		
		super("Place Your Ships!");
		this.player = player;
		b = new Board();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setSize(400, 550);
		content = new JPanel(new BorderLayout(0,5));
		content.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(content);

		btnShip5 = new JRadioButton();
		btnShip5.setActionCommand("5");
		btnShip5.addActionListener(this);
		btnShip5.setSelected(true);

		btnShip4 = new JRadioButton();
		btnShip4.addActionListener(this);
		btnShip4.setActionCommand("4");

		btnShip3a = new JRadioButton();
		btnShip3a.addActionListener(this);
		btnShip3a.setActionCommand("3");

		btnShip3b = new JRadioButton();
		btnShip3b.addActionListener(this);
		btnShip3b.setActionCommand("3");

		btnShip2 = new JRadioButton();
		btnShip2.addActionListener(this);
		btnShip2.setActionCommand("2");

		btgShips = new ButtonGroup();
		btgShips.add(btnShip5);
		btgShips.add(btnShip4);
		btgShips.add(btnShip3a);
		btgShips.add(btnShip3b);
		btgShips.add(btnShip2);

		pnlShip5 = new JPanel(new GridLayout(1, 6));
		GameButton[] ship5Array = new GameButton[5];
		for (int a = 0; a < 5; a++) {
			ship5Array[a] = new GameButton();
			ship5Array[a].setEnabled(false);
		}

		for (int a = 0; a < 5; a++) {
			pnlShip5.add(ship5Array[a]);
		}

		pnlShip5.add(btnShip5);

		pnlShip4 = new JPanel(new GridLayout(1, 6));
		GameButton[] ship4Array = new GameButton[5];
		for (int a = 0; a < 5; a++) {
			ship4Array[a] = new GameButton();
			ship4Array[a].setEnabled(false);
		}

		for (int a = 0; a < 5; a++) {
			pnlShip4.add(ship4Array[a]);
		}

		ship4Array[4].setVisible(false);
		pnlShip4.add(btnShip4);

		pnlShip3a = new JPanel(new GridLayout(1, 6));
		GameButton[] ship3aArray = new GameButton[5];
		for (int a = 0; a < 5; a++) {
			ship3aArray[a] = new GameButton();
			ship3aArray[a].setEnabled(false);
		}

		for (int a = 0; a < 5; a++) {
			pnlShip3a.add(ship3aArray[a]);
		}

		ship3aArray[3].setVisible(false);
		ship3aArray[4].setVisible(false);
		pnlShip3a.add(btnShip3a);

		pnlShip3b = new JPanel(new GridLayout(1, 6));
		GameButton[] ship3bArray = new GameButton[5];
		for (int a = 0; a < 5; a++) {
			ship3bArray[a] = new GameButton();
			ship3bArray[a].setEnabled(false);
		}

		for (int a = 0; a < 5; a++) {
			pnlShip3b.add(ship3bArray[a]);
		}

		ship3bArray[3].setVisible(false);
		ship3bArray[4].setVisible(false);
		pnlShip3b.add(btnShip3b);

		pnlShip2 = new JPanel(new GridLayout(1, 6));
		GameButton[] ship2Array = new GameButton[5];
		for (int a = 0; a < 5; a++) {
			ship2Array[a] = new GameButton();
			ship2Array[a].setEnabled(false);
		}

		for (int a = 0; a < 5; a++) {
			pnlShip2.add(ship2Array[a]);
		}

		ship2Array[2].setVisible(false);
		ship2Array[3].setVisible(false);
		ship2Array[4].setVisible(false);
		pnlShip2.add(btnShip2);

		pnlShipHolder = new JPanel(new GridLayout(3, 2, 0 ,5));

		btnHorizontal = new JRadioButton();
		btnHorizontal.addActionListener(this);
		btnHorizontal.setActionCommand("1");
		btnHorizontal.setSelected(true);

		btnVertical = new JRadioButton();
		btnVertical.addActionListener(this);
		btnVertical.setActionCommand("0");

		btgHorizontalVertical = new ButtonGroup();
		btgHorizontalVertical.add(btnHorizontal);
		btgHorizontalVertical.add(btnVertical);

		pnlHorizontalVertical = new JPanel(new FlowLayout(FlowLayout.LEADING));
		pnlHorizontalVertical.add(new JLabel("Horizontal"));
		pnlHorizontalVertical.add(btnHorizontal);
		pnlHorizontalVertical.add(new JLabel("Vertical"));
		pnlHorizontalVertical.add(btnVertical);

		pnlShipHolder.add(pnlShip5);
		pnlShipHolder.add(pnlShip4);
		pnlShipHolder.add(pnlShip3a);
		pnlShipHolder.add(pnlShip3b);
		pnlShipHolder.add(pnlShip2);
		pnlShipHolder.add(pnlHorizontalVertical);

		pnlNorth = new JPanel(new BorderLayout(0,5));
		pnlNorth.add(new JLabel("<html><b>SELECT THE SHIP YOU WISH TO PLACE</b></html>",SwingConstants.CENTER), BorderLayout.NORTH);
		pnlNorth.add(pnlShipHolder, BorderLayout.CENTER);

		gameGrid = new GameGrid(rows, cols);
		pnlGrid = new JPanel(new GridLayout(11, 11));

		arrayGrid = new JComponent[11][11];
		arrayGrid[0][0] = new JLabel("");
		
		for (int a = 1; a < 11; a++) {
			arrayGrid[a][0] = new JLabel(Integer.toString(a),SwingConstants.CENTER);
			arrayGrid[0][a] = new JLabel(Character.toString((char) (a + 64)),SwingConstants.CENTER);
		}
		
		for (int a = 1; a < 11; a++) {
			for (int b = 1; b < 11; b++) {
				gameGrid.getButton(a-1, b-1).addMouseListener(this);
				arrayGrid[a][b] = (gameGrid.getButton(a-1, b-1));
			}
		}

		for (int a = 0; a < 11; a++) {
			for (int b = 0; b < 11; b++) {
				pnlGrid.add(arrayGrid[a][b]);
			}
		}
		
		btnConfirm = new JButton("CONFIRM");
		btnConfirm.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(shipsLeftToPlace == 0){
					setVisible(false);
					player.placementFinished(gameGrid, out, in, b);
					dispose();
				}				
			}
		});
		
		content.add(pnlNorth, BorderLayout.NORTH);
		content.add(pnlGrid, BorderLayout.CENTER);
		content.add(btnConfirm, BorderLayout.SOUTH);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {

				try {
					System.out.println(name + " " + opponentName);
					out.writeObject(new Request("UserLeftGame", name, opponentName));
					//out.writeObject(new Request("UserClosed", name));

				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		});
	}

	public boolean validPathCheck(int col, int row) {
		boolean validPath = true;

		if (shipSize == 0) {
			validPath = false;
		}else{
			if (shipHorVert == 1) {
				if (!(col < ((cols - shipSize) + 1))) {
					validPath = false;
				} else {
					for (int i = 0; i < shipSize; i++) {
						if (gameGrid.getButton(row, col + i).isOccupied() == true) {
							validPath = false;
						}
					}
				}
			} else if (shipHorVert == 0) {
				if (!(row < ((rows - shipSize) + 1))) {
					validPath = false;
				} else {
					for (int i = 0; i < shipSize; i++) {
						if (gameGrid.getButton(row + i, col).isOccupied() == true) {
							validPath = false;
						}
					}
				}
			}
		}

		return validPath;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Integer actionNumber = Integer.parseInt(e.getActionCommand());

		if (actionNumber.equals(null)) {
			shipSize = 0;
		} else if (actionNumber < 2) {
			shipHorVert = actionNumber;
		} else {
			shipSize = actionNumber;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		int row = ((GameButton) e.getSource()).getRow();
		int col = ((GameButton) e.getSource()).getColumn();

		if (validPathCheck(col, row) == true) {
			if (shipHorVert == 1) {
				for (int i = 0; i < shipSize; i++) {
					gameGrid.getButton(row, col + i).setEnabled(false);
					gameGrid.getButton(row, col + i).setBackground(Color.RED);
				}
			} else if (shipHorVert == 0) {
				for (int i = 0; i < shipSize; i++) {
					gameGrid.getButton(row + i, col).setEnabled(false);
					gameGrid.getButton(row + i, col).setBackground(Color.RED);
				}
			}
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		int row = ((GameButton) e.getSource()).getRow();
		int col = ((GameButton) e.getSource()).getColumn();

		if (validPathCheck(col, row) == true) {
			if (shipHorVert == 1) {
				for (int i = 0; i < shipSize; i++) {
					gameGrid.getButton(row, col + i).setEnabled(true);
					gameGrid.getButton(row, col + i).setBackground(null);
				}
			} else if (shipHorVert == 0) {
				for (int i = 0; i < shipSize; i++) {
					gameGrid.getButton(row + i, col).setEnabled(true);
					gameGrid.getButton(row + i, col).setBackground(null);
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int row = ((GameButton) e.getSource()).getRow();
		int col = ((GameButton) e.getSource()).getColumn();


		if (validPathCheck(col, row) == true) {
			if (shipHorVert == 1) {
				b.addShip(new Ship(new Point(row, col), new Point(row, col+shipSize-1), 'H'));
				for (int i = 0; i < shipSize; i++) {
					gameGrid.getButton(row, col + i).removeMouseListener(this);
					gameGrid.getButton(row, col + i).setEnabled(false);
					gameGrid.getButton(row, col + i).setOccupied(true, shipSize);
					gameGrid.getButton(row, col + i).setBackground(Color.BLUE);
				}
			} else if (shipHorVert == 0) {
				b.addShip(new Ship(new Point(row, col), new Point(row+shipSize-1, col), 'V'));
				for (int i = 0; i < shipSize; i++) {
					gameGrid.getButton(row + i, col).removeMouseListener(this);
					gameGrid.getButton(row + i, col).setEnabled(false);
					gameGrid.getButton(row + i, col).setOccupied(true,shipSize);
					gameGrid.getButton(row + i, col).setBackground(Color.BLUE);
				}
			}
			shipsLeftToPlace--;
			
			if(shipsLeftToPlace == 0){
				btgHorizontalVertical.clearSelection();
				btnHorizontal.setEnabled(false);
				btnVertical.setEnabled(false);
			}
			btgShips.getSelection().setEnabled(false);
			btgShips.clearSelection();
			shipSize = 0;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}
