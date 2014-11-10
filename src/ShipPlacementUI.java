import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
/**
 * A graphical user interface displaying ship the placement screen.
 * @author Team 1-O
 *
 */
public class ShipPlacementUI extends JFrame implements ActionListener,
        MouseListener {

    private JPanel content;
    private JPanel pnlNorth;
    private JLabel lblTitle;

    private JPanel pnlGrid;

    private GameGrid gameGrid;
    private JComponent[][] arrayGrid;

    private int rows = 10;
    private int cols = 10;

    private int shipSize = 5;
    private int shipHorVert = 1;
    private int shipsLeftToPlace = 5;

    private ButtonGroup btgShips;
    private ArrayList<DefaultButtonModel> arrayShipButtons;
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
    private JLabel lblHorizontal;
    private JLabel lblVertical;

    private String playerName;
    private String opponentName;

    private JPanel pnlSouth;
    private JPanel pnlConfirmHome;
    private JLabel lblPlacementStatus;
    private JButton btnConfirm;
    private JButton btnHome;
    private Border bdrRaisedButton;
    private Border bdrLoweredButton;

    private Board b;
    private Player player;

    private Color backgroundColor = new Color(44, 62, 80);
    private Color textColor = new Color(236, 240, 241);
    /**
     * Creates an instance of {@link ShipPlacementUI} to allow the player to place there ships
     * 
     * @param player An instance of the {@link Player} class for corresponding with the server and controlling the UI
     */
    public ShipPlacementUI(final Player player) {

        super();
        this.player = player;

        playerName = player.getName();
        opponentName = player.getOpponentName();
        
        setTitle(playerName + ", place your ships!");
        
        b = new Board();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        setSize(470, 650);
        setLocationRelativeTo(null);
        content = new JPanel(new BorderLayout(0, 5));
        content.setBorder(BorderFactory.createLineBorder(backgroundColor, 5));
        content.setBackground(backgroundColor);
        setContentPane(content);

        btnShip5 = new JRadioButton();
        btnShip5.setActionCommand("5");
        btnShip5.addActionListener(this);
        btnShip5.setSelected(true);
        btnShip5.setBackground(backgroundColor);

        btnShip4 = new JRadioButton();
        btnShip4.addActionListener(this);
        btnShip4.setActionCommand("4");
        btnShip4.setBackground(backgroundColor);

        btnShip3a = new JRadioButton();
        btnShip3a.addActionListener(this);
        btnShip3a.setActionCommand("3");
        btnShip3a.setBackground(backgroundColor);

        btnShip3b = new JRadioButton();
        btnShip3b.addActionListener(this);
        btnShip3b.setActionCommand("3");
        btnShip3b.setBackground(backgroundColor);

        btnShip2 = new JRadioButton();
        btnShip2.addActionListener(this);
        btnShip2.setActionCommand("2");
        btnShip2.setBackground(backgroundColor);

        arrayShipButtons = new ArrayList<DefaultButtonModel>();
        arrayShipButtons.add((DefaultButtonModel) btnShip5.getModel());
        arrayShipButtons.add((DefaultButtonModel) btnShip4.getModel());
        arrayShipButtons.add((DefaultButtonModel) btnShip3a.getModel());
        arrayShipButtons.add((DefaultButtonModel) btnShip3b.getModel());
        arrayShipButtons.add((DefaultButtonModel) btnShip2.getModel());

        btgShips = new ButtonGroup();
        btgShips.add(btnShip5);
        btgShips.add(btnShip4);
        btgShips.add(btnShip3a);
        btgShips.add(btnShip3b);
        btgShips.add(btnShip2);

        pnlShip5 = new JPanel(new GridLayout(1, 6));
        pnlShip5.setBackground(backgroundColor);
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
        pnlShip4.setBackground(backgroundColor);
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
        pnlShip3a.setBackground(backgroundColor);
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
        pnlShip3b.setBackground(backgroundColor);
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
        pnlShip2.setBackground(backgroundColor);
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

        pnlShipHolder = new JPanel(new GridLayout(3, 2, 0, 5));
        pnlShipHolder.setBackground(backgroundColor);

        btnHorizontal = new JRadioButton();
        btnHorizontal.addActionListener(this);
        btnHorizontal.setActionCommand("1");
        btnHorizontal.setSelected(true);
        btnHorizontal.setBackground(backgroundColor);

        btnVertical = new JRadioButton();
        btnVertical.addActionListener(this);
        btnVertical.setActionCommand("0");
        btnVertical.setBackground(backgroundColor);

        btgHorizontalVertical = new ButtonGroup();
        btgHorizontalVertical.add(btnHorizontal);
        btgHorizontalVertical.add(btnVertical);

        lblHorizontal = new JLabel("HORIZONTAL");
        lblHorizontal.setBackground(backgroundColor);
        lblHorizontal.setForeground(textColor);
        lblHorizontal.setFont(new Font("EUROSTILE", Font.BOLD, 12));

        lblVertical = new JLabel("VERTICAL");
        lblVertical.setBackground(backgroundColor);
        lblVertical.setForeground(textColor);
        lblVertical.setFont(new Font("EUROSTILE", Font.BOLD, 12));

        pnlHorizontalVertical = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlHorizontalVertical.setBackground(backgroundColor);
        pnlHorizontalVertical.add(lblHorizontal);
        pnlHorizontalVertical.add(btnHorizontal);
        pnlHorizontalVertical.add(lblVertical);
        pnlHorizontalVertical.add(btnVertical);

        pnlShipHolder.add(pnlShip5);
        pnlShipHolder.add(pnlShip4);
        pnlShipHolder.add(pnlShip3a);
        pnlShipHolder.add(pnlShip3b);
        pnlShipHolder.add(pnlShip2);


        pnlNorth = new JPanel(new BorderLayout(0, 5));
        pnlNorth.setBackground(backgroundColor);

        lblTitle = new JLabel("SELECT THE SHIP YOU WISH TO PLACE", SwingConstants.CENTER);
        lblTitle.setForeground(textColor);
        lblTitle.setFont(new Font("EUROSTILE", Font.BOLD, 14));

        pnlNorth.add(lblTitle, BorderLayout.NORTH);
        pnlNorth.add(pnlShipHolder, BorderLayout.CENTER);
        pnlNorth.add(pnlHorizontalVertical, BorderLayout.SOUTH);

        gameGrid = new GameGrid(rows, cols);
        pnlGrid = new JPanel(new GridLayout(11, 11));
        pnlGrid.setBackground(backgroundColor);

        arrayGrid = new JComponent[11][11];
        arrayGrid[0][0] = new JLabel("");
        arrayGrid[0][0].setBackground(backgroundColor);
        arrayGrid[0][0].setOpaque(true);

        for (int a = 1; a < 11; a++) {
            arrayGrid[a][0] = new JLabel(Integer.toString(a),
                    SwingConstants.CENTER);
            arrayGrid[0][a] = new JLabel(Character.toString((char) (a + 64)),
                    SwingConstants.CENTER);
            arrayGrid[a][0].setBackground(backgroundColor);
            arrayGrid[0][a].setBackground(backgroundColor);
            arrayGrid[a][0].setFont(new Font("EUROSTILE", Font.BOLD, 12));
            arrayGrid[0][a].setFont(new Font("EUROSTILE", Font.BOLD, 12));
            arrayGrid[a][0].setForeground(textColor);
            arrayGrid[0][a].setForeground(textColor);
            arrayGrid[a][0].setOpaque(true);
            arrayGrid[0][a].setOpaque(true);
        }

        for (int a = 1; a < 11; a++) {
            for (int b = 1; b < 11; b++) {
                gameGrid.getButton(a - 1, b - 1).addMouseListener(this);
                arrayGrid[a][b] = (gameGrid.getButton(a - 1, b - 1));
            }
        }

        for (int a = 0; a < 11; a++) {
            for (int b = 0; b < 11; b++) {
                pnlGrid.add(arrayGrid[a][b]);
            }
        }

        pnlConfirmHome = new JPanel(new GridLayout(1, 2));
        pnlConfirmHome.setBorder(BorderFactory.createLineBorder(backgroundColor, 5));
        pnlConfirmHome.setBackground(backgroundColor);
        
        pnlSouth = new JPanel(new GridLayout(2, 1));
        pnlSouth.setBackground(backgroundColor);
        lblPlacementStatus = new JLabel("PLACE YOUR SHIPS!", SwingConstants.CENTER);
        lblPlacementStatus.setFont(new Font("EUROSTILE", Font.BOLD, 14));
        lblPlacementStatus.setForeground(textColor);
        lblPlacementStatus.setBorder(BorderFactory.createLineBorder(backgroundColor, 2));

        btnConfirm = new JButton("CONFIRM");
        btnConfirm.setEnabled(false);
        btnConfirm.setBackground(backgroundColor);
        btnConfirm.setFont(new Font("EUROSTILE", Font.BOLD, 14));
        btnConfirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (shipsLeftToPlace == 0) {
                    player.sendServerRequest(new Request("PlayerReady", playerName, opponentName));
                    lblPlacementStatus.setText("WAITING FOR OPPONENT...");
                    btnConfirm.setEnabled(false);   
                }
            }
        });

        btnHome = new JButton("<html><b>QUIT</b></html>");
        btnHome.setFont(new Font("EUROSTILE", Font.BOLD, 14));
        btnHome.setBackground(backgroundColor);
        btnHome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.sendServerRequest(new Request("UserWentBackToLobby", player.getName(), opponentName, "NotFinished"));
                player.reshowLobby();

            }
        });

        pnlConfirmHome.add(btnConfirm);
        pnlConfirmHome.add(btnHome);
        pnlSouth.add(lblPlacementStatus);
        pnlSouth.add(pnlConfirmHome);

        content.add(pnlNorth, BorderLayout.NORTH);
        content.add(pnlGrid, BorderLayout.CENTER);
        content.add(pnlSouth, BorderLayout.SOUTH);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                player.sendServerRequest(new Request("UserLeftGame", player.getName(),
                        opponentName));
            }

        });
    }

    
    /**
     * From a give grid location give by col and row it determines if a ship of a give size can
     * be placed, starting in this grid square without exceeding the limits of the grid
     * 
     * @param col The column number of the mouse over grid square
     * @param row The row number of the mouse over grid square
     * @return true if the path is valid and false if not
     */
    public boolean validPathCheck(int col, int row) {
        boolean validPath = true;

        if (shipSize == 0) {
            validPath = false;
        } else {
            if (shipHorVert == 1) {
                if (!(col < ((cols - shipSize) + 1))) {
                    validPath = false;
                } else {
                    for (int i = 0; i < shipSize; i++) {
                        if (gameGrid.getButton(row, col + i).isOccupied()) {
                            validPath = false;
                        }
                    }
                }
            } else if (shipHorVert == 0) {
                if (!(row < ((rows - shipSize) + 1))) {
                    validPath = false;
                } else {
                    for (int i = 0; i < shipSize; i++) {
                        if (gameGrid.getButton(row + i, col).isOccupied()) {
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

        if (validPathCheck(col, row)) {
            if (shipHorVert == 1) {
                for (int i = 0; i < shipSize; i++) {
                    gameGrid.getButton(row, col + i).setEnabled(false);
                    gameGrid.getButton(row, col + i).setIcon(null);
                    gameGrid.getButton(row, col + i).setBackground(Color.RED);
                }
            } else if (shipHorVert == 0) {
                for (int i = 0; i < shipSize; i++) {
                    gameGrid.getButton(row + i, col).setEnabled(false);
                    gameGrid.getButton(row + i, col).setIcon(null);
                    gameGrid.getButton(row + i, col).setBackground(Color.RED);
                }
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        int row = ((GameButton) e.getSource()).getRow();
        int col = ((GameButton) e.getSource()).getColumn();

        if (validPathCheck(col, row)) {
            if (shipHorVert == 1) {
                for (int i = 0; i < shipSize; i++) {
                    gameGrid.getButton(row, col + i).setEnabled(true);
                    gameGrid.getButton(row, col + i).setDefaultIcon();
                    gameGrid.getButton(row, col + i).setBackground(null);
                }
            } else if (shipHorVert == 0) {
                for (int i = 0; i < shipSize; i++) {
                    gameGrid.getButton(row + i, col).setEnabled(true);
                    gameGrid.getButton(row + i, col).setDefaultIcon();
                    gameGrid.getButton(row + i, col).setBackground(null);
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = ((GameButton) e.getSource()).getRow();
        int col = ((GameButton) e.getSource()).getColumn();

        if (validPathCheck(col, row)) {
            if (shipHorVert == 1) {
                b.addShip(new Ship(new Point(row, col), new Point(row, col
                        + shipSize - 1), 'H'));
                for (int i = 0; i < shipSize; i++) {
                    gameGrid.getButton(row, col + i).removeMouseListener(this);
                    gameGrid.getButton(row, col + i).setEnabled(false);
                    gameGrid.getButton(row, col + i)
                            .setOccupied(true, shipSize);
                    gameGrid.getButton(row, col + i).setIcon(null);
                    gameGrid.getButton(row, col + i).setBackground(Color.GRAY);
                    gameGrid.getButton(row, col + i).setBorderToDark();
                }
            } else if (shipHorVert == 0) {
                b.addShip(new Ship(new Point(row, col), new Point(row
                        + shipSize - 1, col), 'V'));
                for (int i = 0; i < shipSize; i++) {
                    gameGrid.getButton(row + i, col).removeMouseListener(this);
                    gameGrid.getButton(row + i, col).setEnabled(false);
                    gameGrid.getButton(row + i, col)
                            .setOccupied(true, shipSize);
                    gameGrid.getButton(row + i, col).setIcon(null);
                    gameGrid.getButton(row + i, col).setBackground(Color.GRAY);
                    gameGrid.getButton(row + i, col).setBorderToDark();
                }
            }
            shipsLeftToPlace--;

            int selectedButton = arrayShipButtons.indexOf(btgShips
                    .getSelection());

            btgShips.getSelection().setEnabled(false);
            btgShips.clearSelection();

            arrayShipButtons.remove(selectedButton);

            if (shipsLeftToPlace != 0) {
                btgShips.setSelected(arrayShipButtons.get(0), true);
                shipSize = Integer.parseInt(arrayShipButtons.get(0)
                        .getActionCommand());
                lblPlacementStatus.setText("(" + shipsLeftToPlace + ")" + " SHIPS LEFT TO PLACE");
                lblPlacementStatus.setText(lblPlacementStatus.getText().toUpperCase());
            } else {
                shipSize = 0;
                btgHorizontalVertical.clearSelection();
                btnHorizontal.setEnabled(false);
                btnVertical.setEnabled(false);
                lblPlacementStatus.setText("FINISHED! PRESS CONFIRM TO CONTINUE");
                lblPlacementStatus.setText(lblPlacementStatus.getText().toUpperCase());
                btnConfirm.setEnabled(true);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * Closes the {@link ShipPlacementUI} and opens a new {@link GameUI}, starting the game
     */
    public void startGame() {
        setVisible(false);
        player.placementFinished(gameGrid, b);
        dispose();
    }
}
