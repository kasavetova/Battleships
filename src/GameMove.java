import java.io.Serializable;

/**
 * Represents a GameMove made by the user.
 *
 * @author Tea 1-O
 * @see Player
 * @see GameUI
 */
public class GameMove implements Serializable {
    private Point coordinates;
    private String playerName;
    private String moveResult;

    /**
     * Initialises the object properties of the game move.
     *
     * @param coordinates the move coordinates
     * @param playerName  the name of the player who made the move
     * @param moveResult  the result of the move
     */
    public GameMove(Point coordinates, String playerName, String moveResult) {
        this.coordinates = coordinates;
        this.playerName = playerName;
        this.moveResult = moveResult;

    }

    /**
     * Returns the move's coordinates
     *
     * @return the move's coordinates
     */
    public Point getMoveCoordinates() {
        return coordinates;
    }

    /**
     * Returns the coordinates of the game move.
     * @return the coordinates of the move.
     */
    public String getMoveResult() {
        return moveResult;
    }

    /**
     * Returns the player's name who made the move.
     * @return the player name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Returns the result of the game move
     * @param result the result of the game move
     */
    public void setMoveResult(String result) {
        moveResult = result;
    }
}
