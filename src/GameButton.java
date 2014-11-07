import javax.swing.*;

import java.awt.*;

public class GameButton extends JButton {

    private int row;
    private int col;
    private boolean cellState;
    private int occupyingShipSize;
    
    private ImageIcon iiSea = new ImageIcon("res/sea.png");
    private Color borderColor = new Color(0, 66, 90);
    private Color shipColor = Color.GRAY;
    private Color shipBorder = new Color(46,46,46);
    
    public GameButton() {
        super();
        cellState = false;
        setOpaque(true);
        this.setBorder(BorderFactory.createLineBorder(shipBorder, 1));
        this.setBackground(shipColor);
    }

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

	public void setDefaultIcon() {
		this.setIcon(iiSea);
		
	}
	
	public void setBorderToDark(){
		this.setBorder(BorderFactory.createLineBorder(shipBorder, 1));
	}
}
