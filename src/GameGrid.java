public class GameGrid {

    private GameButton[][] gridHolder;

	private int rows;
    private int cols;

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

    public GameButton getButton(int row, int col) {
        return gridHolder[row][col];
    }

    public GameButton[][] getGrid() {
        return gridHolder;
    }
    
    public void setButton(int row, int col, GameButton gameButton){
    	
    	gridHolder[row][col] = gameButton;
    }
}
