package Mechanics;

import java.io.Serializable;

/**
 * Class that defines a ship.
 * @author Team 1-O
 * @see Point
 */
public class Ship implements Serializable {

   private Point start; //this is the point where the ship begins
   private Point end; //this is the point where the ship ends
   private boolean destroyed;//this is to status of the ship, whether it was destroyed or not.
   private int size; //this is the size of the ship, that will be calculated automatically
   private int life; //this is the life of the ship. Every time the ship gets hit but not destroyed, it will reduce from its life,when life==0 ship is destroyed
   private char orientation;//H-horizontal , V-vertical
   private String name;
   private static boolean thirdShip = false;


    /**
     * Initialises a new ship that starts at Mechanics.Point start and ends at Mechanics.Point end.
     *
     * @param start The point where the new ship starts
     * @param end   The point where the new ship ends
     * @see Point
     */
    public Ship(Point start, Point end, char orientation) {

        this.start = start;
        this.end = end;
        this.orientation = orientation;
		
        size = start.getDistance(end) + 1; //calculates the size of the ship
        destroyed = false; //ship is initially not destroyed
        life = size; // the life of the ship is initially equal to its size
        switch (size) {
        case 2:
            name = "Patrol Boat";
            break;
        case 3:
            if (!thirdShip) {
                thirdShip = true;
                name = "Destroyer";
                break;
            } else {
                thirdShip = false;
                name = "Submarine";
                break;
            }
        case 4:
            name = "Battleship";
            break;
        case 5:
            name = "Aircraft Carrier";
            break;
    }


    }


    /**
     * Getter for the start point.
     *
     * @return Mechanics.Point start(the point where ship starts).
     * @see Point
     */
    public Point getStart() {
        return start;
    }


    /**
     * Setter for the start point.
     *
     * @param start The new point in which the ship will start.
     * @see Point
     */
    public void setStart(Point start) {
        this.start = start;
    }


    /**
     * Getter for the end point.
     *
     * @return Mechanics.Point end(the point where ship ends).
     * @see Point
     */
    public Point getEnd() {
        return end;
    }


    /**
     * Setter for the end point.
     *
     * @param end The new point in which the ship will end.
     * @see Point
     */
    public void setEnd(Point end) {
        this.end = end;
    }


    /**
     * Getter for size.
     *
     * @return Size of ship.
     */
    public int getSize() {
        return size;
    }


    /**
     * Getter for orientation.
     *
     * @return Orientation of ship.
     */
    public char getOrientation() {
        return orientation;
    }


    /**
     * Getter for destroyed status.
     *
     * @return True if ship is destroyed, false otherwise.
     */
    public boolean isDestroyed() {
        return destroyed;
    }


    /**
     * Sets the status of the ship to destroyed or not.
     *
     * @param destroyed whether ship should be set to destroyed or not
     */
    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }


    /**
     * Getter for life.
     *
     * @return The life of the ship.
     */
    public int getLife() {
        return life;
    }


    /**
     * Shoots the ship and decreases the life by one. If life is 0 after it was shot, it will set destroyed.
     * @return The life of the ship after it was shot.
     */
    public int shoot() {
        if (life > 0)
            life--;
        if (life <= 0)
            this.setDestroyed(true);
        return life;

    }


    /**
     * Getter for the name of the ship.
     * @return The name of the ship.
     */
    public String getName() {
        return name;
    }

}
