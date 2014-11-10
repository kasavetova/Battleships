import java.io.Serializable;
import java.util.ArrayList;
//-4 is DESTROYED -3 is HIT -2 is MISSED -1 is EMPTY 0 is SHIP IS THERE

/**
 * Class that defines a board with ships of type Ship as a 2d array of integers.
 * -4 = DESTROYED, -3 = HIT, -2 = MISSED, -1 is EMPTY and 0 = SHIP
 * @author Team 1-O
 * @see Ship
 */

public class Board implements Serializable {

	private int[][] board;
	private  ArrayList<Ship> ships;
	private int indexOfShips;


    /**
     * Initialises a new board.
     */
    public Board() {
        board = new int[10][10];
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                board[i][j] = -1;
        ships = new ArrayList<>();
        indexOfShips = 0;
    }

    /**
     * Adds a new Ship to the board.
     * @param ship The ship of type Ship to be added to the board.
     * @see Ship
     */
    public void addShip(Ship ship) {

        if (ship.getOrientation() == 'H') {
            System.out.println("H" + ship.getStart().getX() + " " + ship.getStart().getY());
            System.out.println(ship.getEnd().getX() + " " + ship.getEnd().getY());
            for (int i = ship.getStart().getY(); i <= ship.getEnd().getY(); i++) {
                board[ship.getStart().getX()][i] = indexOfShips;

            }
        } else {

            System.out.println("V" + ship.getStart().getX() + " " + ship.getStart().getY());
            System.out.println(ship.getEnd().getX() + " " + ship.getEnd().getY());
            for (int i = ship.getStart().getX(); i <= ship.getEnd().getX(); i++) {
                board[i][ship.getStart().getY()] = indexOfShips;

            }
        }
        ships.add(ship);
        indexOfShips++;


    }

    /**
     * Getter for the board.
     * @return Returns the board in a 2D array of integers format.
     */
    public int[][] getBoard() {
        return board;
    }

    /** 
     * For debugging purposes. Prints current board to console.
     */
    public void printBoard() {
        for (int i = 0; i < 10; i++) {
            System.out.println();
            for (int j = 0; j < 10; j++)
                System.out.print(board[i][j] + " ");

        }
        System.out.println();
    }

    /**
     * Method to shoot at a given point.
     * @param point The point towards which the shot is directed.
     * @return Returns a String with the outcome of the shot. "destroyed" if the entire ship was destroyed, "hit" if a part of the ship was hit but the ship is still alive, "missed" if the point shot is empty and "invalid" in case the move is invalid.
     * @see Point Ship
     */
    public String shoot(Point point) {

        int shot = board[point.getX()][point.getY()];
        System.out.println(shot);
        if (shot >= 0) {
            ships.get(shot).shoot();
            if (ships.get(shot).isDestroyed()) {
                board[point.getX()][point.getY()] = -4;
                return "destroyed" + ships.get(shot).getName();

            }

            board[point.getX()][point.getY()] = -3;
            return "hit";

        }

        if (shot == -1) {
            board[point.getX()][point.getY()] = -2;
            return "missed";
        }

        return "invalid";

    }

}
