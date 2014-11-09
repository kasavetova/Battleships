import java.io.Serializable;


public class Ship implements Serializable {

    Point start; //this is the point where the ship begins
    Point end; //this is the point where the ship ends
    boolean destroyed;//this is to status of the ship, whether it was destroyed or not.
    int size; //this is the size of the ship, that will be calculated automatically
    int life; //this is the life of the ship. Every time the ship gets hit but not destroyed, it will reduce from its life,when life==0 shis is destroyed
    char orientation;//H-horizontal , V-vertical
    String name;
    static boolean thirdShip = false;


    /**
     * This initializes a new ship that starts at Point start and ends at Point end.
     *
     * @param start The point where the new ship starts
     * @param end   The point where the new ship ends
     */
    public Ship(Point start, Point end, char orientation) {

        this.start = start;
        this.end = end;
        this.orientation = orientation;
        //the following if will tell if the ship is vertical or horizontal
        /*
        if(start.getX()-end.getX() == 0)
			{
			orientation = 'H';
			}
		else		
			{
			orientation = 'V';			
			}
			*/
        size = start.getDistance(end) + 1; //calculates the size of the ship
        destroyed = false; //ship is initially not destroyed
        life = size; // the life of the ship is initially equal to its size
        name = getShipName();


    }


    /**
     * Getter for the start point.
     *
     * @return Point start(the point where ship starts).
     */
    public Point getStart() {
        return start;
    }


    /**
     * Setter for the start point.
     *
     * @param start The new point in which the ship will start.
     */
    public void setStart(Point start) {
        this.start = start;
    }


    /**
     * Getter for the end point.
     *
     * @return Point end(the point where ship ends).
     */
    public Point getEnd() {
        return end;
    }


    /**
     * Setter for the end point.
     *
     * @param end The new point in which the ship will end.
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
     * @param destroyed
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
     * Shoots the ship and reduces the life by one. If life is 0 after it was shot, it will set destroyed.
     *
     * @return
     */
    public int shoot() {
        if (life > 0)
            life--;
        if (life <= 0)
            this.setDestroyed(true);
        return life;

    }

    public String getShipName() {

        switch (size) {
            case 2:
                return "Patrol Boat";
            case 3:
                if (!thirdShip) {
                    thirdShip = true;
                    return "Destroyer";
                } else {
                    thirdShip = false;
                    return "Submarine";
                }
            case 4:
                return "Battleship";
            case 5:
                return "Aircraft Carrier";
        }
        return "Ship";
    }

    public String getName() {
        return name;
    }

}
