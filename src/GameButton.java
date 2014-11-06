import javax.swing.*;
import java.awt.*;

public class GameButton extends JButton {

    private int row;
    private int col;
    private boolean cellState;
    private int occupyingShipSize;

    public GameButton() {
        super();
        cellState = false;
        setOpaque(true);
        this.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
    }

    public GameButton(int row, int col) {
        super();
        this.row = row;
        this.col = col;
        cellState = false;
        occupyingShipSize = 0;
        setOpaque(true);
        this.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return col;
    }

    public void setOccupied(boolean state, int s) {
        cellState = state;
        occupyingShipSize = s;
    }

    public boolean isOccupied() {
        return cellState;
    }

    public int getOccupyingShipSize() {
        return occupyingShipSize;
    }
}
