import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Establishes a connection to the server and controls the user interface.
 *
 * @author Team 1-O
 */
public class Player {
    
    private int portNumber = 4446;
    private String serverIP = "localhost";

    private static String name;
    private String opponentName;
    private boolean isTheirTurn;
    private boolean isBusy;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket socket;

    private WelcomeFrame welcomeFrame;
    private LobbyFrame lobbyFrame;
    private ShipPlacementUI shipPlacement;
    private GameUI gameUI;
    private ConfirmDialog confirmDialog;

    /**
     * Runs the client side of the program.
     *
     * @param args
     */
    public static void main(String[] args) {
        new Player();
    }

    /**
     * Initialises the {@link WelcomeFrame}.
     */
    public Player() {
        welcomeFrame = new WelcomeFrame(this);
        welcomeFrame.setVisible(true);
    }

    /**
     * Informs the server a new player has connected.
     */
    public void newConnection() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            opponentName = null;
            isTheirTurn = false;
            isBusy = false;
            out.writeObject(new Request("UserJoinedLobby", name));
            out.writeObject(new Request("RetrieveLobby", name));

            ReceivingThread receivingThread = new ReceivingThread();
            receivingThread.start();

        } catch (IOException e) {

            JOptionPane.showMessageDialog(null,
                    "Failed to connect to the server", "Server Error",
                    JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "<html><<p style=\"color:rgb(255, 255, 255)\";font-weight:bold>The following error has occurred</p><p>" + e.getMessage() + "</p></html>",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Handles requests from {@link ServerThread}.
     *
     * @author Team 1-O
     */
    class ReceivingThread extends Thread {
        Request input;

        /**
         * Reads objects from {@link ServerThread} and does the corresponding action.
         */
        @Override
        public void run() {
            try {
                while ((input = (Request) in.readObject()) != null) {

                    if (input.getActionType().equals("UserJoinedLobby")) {
                        if (!input.getObject().equals(name)) {
                            lobbyFrame.addItem((String) input.getObject());
                        }
                    } else if (input.getActionType().equals("UserLeftLobby")) {
                        lobbyFrame.deleteItem((String) input.getObject());
                    } else if (input.getDestination().equals(name)) {

                        String actionType = input.getActionType();
                        switch (actionType) {
                            case "GameRequest":
                                if (!isBusy) {
                                    isBusy = true;
                                    confirmDialog = new ConfirmDialog(Player.this, input);
                                    confirmDialog.setVisible(true);
                                    confirmDialog.setAlwaysOnTop(true);
                                } else {
                                    out.writeObject(new Request("PlayerBusy", name, input.getOrigin()));
                                }
                                break;
                            case "GameRequestAnswer":
                                isBusy = false;
                                lobbyFrame.enablePlayButton(true);
                                if (input.getObject().equals("Yes")) {
                                    opponentName = input.getOrigin();
                                    gameFrame();
                                    isTheirTurn = true;
                                } else if (input.getObject().equals("No")) {
                                    String text = "Game request denied from " + input.getOrigin() + ".";
                                    UIManager UI = new UIManager();
                                    UIManager.put("OptionPane.background", new Color(44, 62, 80));
                                    UIManager.put("Panel.background", new Color(44, 62, 80));
                                    JOptionPane.showMessageDialog(null, String.format("<html><<p style=\"color:rgb(255, 255, 255)\";font-weight:bold>%s</p></html>", text),
                                            "Game request denied", JOptionPane.ERROR_MESSAGE);

                                }
                                break;
                            case "RetrieveLobby":
                                ArrayList<String> playersList = (ArrayList<String>) input.getObject();
                                lobbyFrame.updateLobby(playersList);
                                break;
                            case "ReceiveMessage":
                                gameUI.appendMessage((String) input.getObject(), input.getOrigin());
                                break;
                            case "UserLeftGame":
                                // Quitting game
                                reshowLobby();

                                UIManager.put("OptionPane.background", new Color(44, 62, 80));
                                UIManager.put("Panel.background", new Color(44, 62, 80));


                                JOptionPane.showMessageDialog(null, "<html><<p style=\"color:rgb(255, 255, 255)\";font-weight:bold>Your opponent quit! You win (by default)</p></html>",
                                        "Opponent Quit", JOptionPane.INFORMATION_MESSAGE);

                                break;
                            case "UserWentBackToLobby":
                                //Returning to lobby
                                reshowLobby();
                                if (!input.getObject().equals("Finished")) {

                                    UIManager.put("OptionPane.background", new Color(44, 62, 80));
                                    UIManager.put("Panel.background", new Color(44, 62, 80));

                                    JOptionPane.showMessageDialog(null, "<html><<p style=\"color:rgb(255, 255, 255)\";font-weight:bold>Your opponent quit! You win (by default)</p></html>",
                                            "Opponent Quit", JOptionPane.INFORMATION_MESSAGE);
                                }
                                break;
                            case "MoveResult":
                                GameMove gm = (GameMove) input.getObject();
                                Point coordinates = gm.getMoveCoordinates();
                                String playerName = gm.getPlayerName();
                                String outcome = gm.getMoveResult();

                                if (name.equals(playerName)) {
                                    //update enemy board
                                    if (outcome.equals("hit") || outcome.startsWith("destroyed")) {
                                        isTheirTurn = true;
                                        gameUI.startTimer();
                                    }
                                    gameUI.updateEnemyBoard(outcome, coordinates);
                                } else {
                                    //update own board
                                    if (!outcome.equals("hit") && !outcome.startsWith("destroyed")) {
                                        isTheirTurn = true;
                                        gameUI.appendMessage("It's your turn to play.", "GAME");
                                        gameUI.startTimer();
                                    }
                                    gameUI.updateOwnBoard(outcome, coordinates);
                                }
                                break;
                            case "MoveEnded":
                                isTheirTurn = true;
                                gameUI.appendMessage("Enemy ran out of time.", "GAME");
                                gameUI.startTimer();
                                break;
                            case "GameStart":
                                shipPlacement.startGame();
                                break;
                            case "PlayerBusy":
                                isBusy = false;
                                lobbyFrame.enablePlayButton(true);

                                UIManager.put("OptionPane.background", new Color(44, 62, 80));
                                UIManager.put("Panel.background", new Color(44, 62, 80));


                                JOptionPane.showMessageDialog(null, "<html><<p style=\"color:rgb(255, 255, 255)\";font-weight:bold>Player is busy. Please try again later</p></html>",
                                        "Error", JOptionPane.ERROR_MESSAGE);

                                break;
                            case "GameFinished":
                                gameUI.appendMessage("Enemy has won", "GAME");
                                gameUI.setGameFinished();
                                break;

                        }
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "<html><<p style=\"color:rgb(255, 255, 255)\";font-weight:bold>The following error has occurred</p><p>" + e.getMessage() + "</p></html>",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Closes {@link LobbyFrame} and opens {@link ShipPlacementUI}.
     */
    public void gameFrame() {
        lobbyFrame.dispose();
        shipPlacement = new ShipPlacementUI(this);
        shipPlacement.setVisible(true);
    }

    /**
     * Sends a request to the server.
     *
     * @param request The request to be sent to the server
     */
    public void sendServerRequest(Request request) {
        try {
            out.writeObject(request);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "<html><<p style=\"color:rgb(255, 255, 255)\";font-weight:bold>The following error has occurred</p><p>" + e.getMessage() + "</p></html>",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Denies the game request sent to the user, closes {@link ConfirmDialog}.
     *
     * @param input The request the user received from the server
     */
    public void refuseRequest(Request input) {
        isBusy = false;
        lobbyFrame.enablePlayButton(true);
        sendServerRequest(new Request(
                "GameRequestAnswer", name,
                input.getOrigin(), "No"));
        confirmDialog.dispose();
    }

    /**
     * Accepts the game request sent to the user, closes {@link ConfirmDialog}.
     *
     * @param input the request the user received from the server
     */
    public void acceptRequest(Request input) {
        isBusy = false;
        lobbyFrame.enablePlayButton(true);
        sendServerRequest(new Request(
                "GameRequestAnswer", name,
                input.getOrigin(), "Yes"));
        opponentName = input.getOrigin();
        gameFrame();
        confirmDialog.dispose();
    }

    /**
     * Starts a new game for the user by creating a new instance of {@link GameGrid} and {@link Board}.
     *
     * @param grid the game grid to be created
     * @param b    the board to be created
     */
    public void placementFinished(GameGrid grid, Board b) {
        gameUI = new GameUI(grid, this);
        gameUI.setVisible(true);
        sendServerRequest(new Request("GameBoard", name, "SERVER", b));
        if (isTheirTurn) {
            gameUI.startTimer();
        } else {
            gameUI.setLabelText();
        }
    }

    /**
     * Checks if it's the user's turn to play and sends a new request to the server with the details
     * of the game move the user has made.
     *
     * @param request the request to be sent to the server
     * @return true if it's the user's turn to play and they are successful in making a move; false otherwise.
     */
    public boolean makeMove(Request request) {
        if (isTheirTurn) {
            sendServerRequest(request);
            isTheirTurn = false;
            return true;
        } else {
            gameUI.appendMessage("It's not your turn yet! Please wait for your opponent.", "GAME");
            return false;
        }
    }

    /**
     * Sends a request to the server informing that the user has finished their move.
     *
     * @param request the request sent to the server
     */
    public void finishMove(Request request) {
        sendServerRequest(request);
        isTheirTurn = false;
    }

    /**
     * Returns the user's name
     *
     * @return user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if the name is unique to play with.
     *
     * @param nameToCheck the name to be checked
     * @return true if the name is unique; false otherwise.
     */
    public boolean checkName(String nameToCheck) {
        try {
            socket = new Socket(serverIP, portNumber);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            Request input;
            while ((input = (Request) in.readObject()) != null) {
                if (input.getActionType().equals("RetrieveLobby")) {
                    ArrayList<String> playersList = (ArrayList<String>) input.getObject();
                    if (playersList != null) {
                        for (int i = 0; i < playersList.size(); i++) {
                            if (playersList.get(i).toUpperCase().equals(nameToCheck.toUpperCase())) {
                                sendServerRequest(new Request("Rejected"));
                                out.close();
                                in.close();
                                socket.close();
                                socket = null;
                                return false;
                            }
                        }
                    }
                    break;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "<html><<p style=\"color:rgb(255, 255, 255)\";font-weight:bold>The following error has occurred</p><p>" + e.getMessage() + "</p></html>",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        sendServerRequest(new Request("Accepted"));
        return true;
    }

    /**
     * Closes other frames and opens up the {@link LobbyFrame}.
     */
    public void reshowLobby() {

        lobbyFrame = new LobbyFrame(this);
        lobbyFrame.setVisible(true);
        opponentName = null;

        if (shipPlacement != null) {
            shipPlacement.dispose();
        }
        if (gameUI != null) {
            gameUI.dispose();
        }
        sendServerRequest(new Request("RetrieveLobby", name));
    }

    /**
     * Closes the {@link WelcomeFrame} and opens up {@link LobbyFrame} with the user's name added to it.
     *
     * @param name the user name to be added to the lobby
     */
    public void closeWelcomeFrame(String name) {
        welcomeFrame.dispose();
        Player.name = name;
        lobbyFrame = new LobbyFrame(this);
        lobbyFrame.setVisible(true);
        newConnection();
    }

    /**
     * Sets the active status of a user.
     *
     * @param b the user's status
     */
    public void setBusy(boolean b) {
        isBusy = b;
    }

    /**
     * Returns the name for the user's opponent.
     *
     * @return the opponent name
     */
    public String getOpponentName() {
        return opponentName;
    }
}
