import javax.swing.*;

import java.awt.*;
/**
 * 
 * @author Team I-O
 *
 */
public class GameButton extends JButton {

    private int row;
    private int col;
    private boolean cellState;
    private int occupyingShipSize;

    private ImageIcon iiSea = new ImageIcon("res/sea.png");
    private Color borderColor = new Color(0, 66, 90);
    private Color shipBorder = new Color(46, 46, 46);

    /**
     * Creates a {@link GameButton}
     */
    public GameButton() {
        super();
        cellState = false;
        setOpaque(true);
        this.setBorder(BorderFactory.createLineBorder(shipBorder, 1));
        // this.setBackground(shipColor);
    }

    /**
     * Creates a {@link GameButton} to be stored in row, row and column, col
     * 
     * @param row The row where the {@link GameButton} is stored
     * @param col The column where the {@link GameButton} is stored
     */
    public GameButton(int row, int col) {
        super();
        this.row = row;
        this.col = col;
        cellState = false;
        occupyingShipSize = 0;
        setOpaque(true);
        this.setBorder(BorderFactory.createLineBorder(borderColor, 1));
        this.setIcon(iiSea);
    }

    /**
     * Returns the row of a give {@link GameButton}
     * 
     * @return the row of a give {@link GameButton}
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column of a give {@link GameButton}
     * 
     * @return the column of a give {@link GameButton}
     */
    public int getColumn() {
        return col;
    }

    /**
     * Sets the size of the ship that the {@link GameButton} is occupied by and sets the state of the cell
     * 
     * @param state Whether the block has been hit 
     * @param s The size of the occupying ship
     */
    public void setOccupied(boolean state, int s) {
        cellState = state;
        occupyingShipSize = s;
    }

    /**
     * Return if the {@link GameButton} is occupied
     * 
     * @return true if occupied, false if not
     */
    public boolean isOccupied() {
        return cellState;
    }

    /**
     * Gets the size of the occupying ship
     * 
     * @return The size of the occupying ship
     */
    public int getOccupyingShipSize() {
        return occupyingShipSize;
    }

    /**
     * Sets the default icon of the {@link GameButton}
     */
    public void setDefaultIcon() {
        this.setIcon(iiSea);

    }
   
    /**
     * Sets the border of the {@link GameButton} to a Dark colour
     */
    public void setBorderToDark() {
        this.setBorder(BorderFactory.createLineBorder(shipBorder, 1));
    }
}
