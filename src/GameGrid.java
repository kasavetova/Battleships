/**
 * Represents the array of {@link GameButton}
 * @author Team I-O
 *
 */
public class GameGrid {

    private GameButton[][] gridHolder;

	private int rows;
    private int cols;

    /**
     * Creates a {@link GameGrid} with cols, number of columns and rows, number of rows
     * 
     * @param rows The number of rows in the grid
     * @param cols The number of columns in the grid
     */
    public GameGrid(int rows, int cols) {
        gridHolder = new GameButton[rows][cols];
        this.rows = rows;
        this.cols = cols;

        for (int a = 0; a < rows; a++) {
            for (int b = 0; b < cols; b++) {
                gridHolder[a][b] = new GameButton(a, b);
            }
        }
    }

    /**
     * Gets the {@link GameButton} at the row and column specified
     * 
     * @param row The row of the button
     * @param col The column of the button	
     * @return The button at the specified row and column
     */
    public GameButton getButton(int row, int col) {
        return gridHolder[row][col];
    }

    /**
     * Return the array holding all of the {@link GameButton}s 
     * 
     * @return the array holding all of the {@link GameButton}s 
     */
    public GameButton[][] getGrid() {
        return gridHolder;
    }
    
    /**
     * Sets the {@link GameButton} at the row and column specified
     * 
     * @param row The row where the button is to be placed
     * @param col The column where the button is to be placed
     * @param gameButton The {@link GameButton} to be set at the specified position
     */
    public void setButton(int row, int col, GameButton gameButton){
    	gridHolder[row][col] = gameButton;
    }
}
