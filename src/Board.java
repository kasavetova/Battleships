import java.io.Serializable;
import java.util.ArrayList;
//-4 is DESTROYED -3 is HIT -2 is MISSED -1 is EMPTY 0 is SHIP IS THERE

public class Board implements Serializable {

    int[][] board;
    ArrayList<Ship> ships;
    int indexOfShips;

    public Board() {
        board = new int[10][10];
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                board[i][j] = -1;
        ships = new ArrayList<Ship>();
        indexOfShips = 0;
    }

    public boolean addShip(Ship ship) {

        if (ship.getOrientation() == 'H') {
            System.out.println("H" + ship.getStart().getX() + " " + ship.getStart().getY());
            System.out.println(ship.getEnd().getX() + " " + ship.getEnd().getY());
            for (int i = ship.getStart().getY(); i <= ship.getEnd().getY(); i++) {
                board[ship.getStart().getX()][i] = indexOfShips;

            }

			/*
			for(int i = ship.getStart().getY();i<ship.getEnd().getY();i++)
			{
				if(board[ship.getStart().getX()][i] != -1)
					return false;
				else
					board[ship.getStart().getX()][i] = indexOfShips;
			}
			*/
        } else {

            System.out.println("V" + ship.getStart().getX() + " " + ship.getStart().getY());
            System.out.println(ship.getEnd().getX() + " " + ship.getEnd().getY());
            for (int i = ship.getStart().getX(); i <= ship.getEnd().getX(); i++) {
                board[i][ship.getStart().getY()] = indexOfShips;

            }
			/*
			for(int i = ship.getStart().getX();i<ship.getEnd().getX();i++)
			{	
				if(board[i][ship.getStart().getY()] != 0)
					return false;
				else
					board[i][ship.getStart().getY()] = indexOfShips;
			}
			*/
        }
        ships.add(ship);
        indexOfShips++;
        return true;

    }

    public int[][] getBoard() {
        return board;
    }

    public void printBoard() {
        for (int i = 0; i < 10; i++) {
            System.out.println();
            for (int j = 0; j < 10; j++)
                System.out.print(board[i][j] + " ");

        }
        System.out.println();
    }

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
