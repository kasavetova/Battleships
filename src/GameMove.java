import java.io.Serializable;


public class GameMove implements Serializable {
    private Point coordinates;
    private String playerName;
    private String moveResult;

    public GameMove(Point coordinates, String playerName, String moveResult) {
        this.coordinates = coordinates;
        this.playerName = playerName;
        this.moveResult = moveResult;

    }

    public Point getMoveCoordinates() {
        return coordinates;
    }

    public String getMoveResult() {
        return moveResult;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setMoveResult(String result) {
        moveResult = result;
    }
}
