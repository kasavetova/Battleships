package Mechanics;

import java.io.Serializable;
/**
 * Class that defines a point and its coordinates.
 * @author Team 1-O
 *
 */
public class Point implements Serializable {
	private int x;
	private  int y;
	
	/**
	 * Constructs a point with coordinates x and y.
	 * @param x first coordinate
	 * @param y second coordinate
	 */
    public Point(int x, int y) {

        this.x = x;
        this.y = y;
    }
    
    /**
     * Setter for coordinate x.
     * @param x The new coordinate.
     */
    public void setX(int x) {
        this.x = x;
    }
   
    /**
     * Setter for coordinate y.
     * @param y The new coordinate.
     */
    public void setY(int y) {
        this.y = y;
    }
    
    /**
     * Getter for coordinate x.
     * @return Coordinate x.
     */
    public int getX() {
        return x;
    }
   
    /**
     * Getter for coordinate y.
     * @return Coordinate y.
     */
    public int getY() {
        return y;
    }

    /**
     * Calculates the distance between two points.
     *
     * @param p The point to which the distance must be calculated.
     * @return The distance between 2 points.
     */
    public int getDistance(Point p) {

        return Math.abs((this.getX() - p.getX()) + (this.getY() - p.getY()));

    }


}
