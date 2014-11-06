import java.io.Serializable;


public class Point implements Serializable {
    int x;
    int y;


    public Point(int x, int y) {

        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Calculates the distance between two points.
     *
     * @param c
     * @return
     */
    public int getDistance(Point p) {

        return Math.abs((this.getX() - p.getX()) + (this.getY() - p.getY()));

    }


}
