Battleships
===========

## Prerequisites 

Both client and server must have Java 1.7 or higher installed.

## Deployment

### Server
For server deployment, please ensure the portNumber variable in ServerMain.java in the 
Server package has been assigned to the correct port number.	
To run	the	server, run	the	file ServerMain.java

*Please note: there is no graphical interface for the server*

### Client

For clients to connect to the server correctly, be sure the portNumber variable in Player.java in the Client package has
been assigned to the correct port number. To connect to a specific server ensure serverIP in Player.java contains IP address of the 
server. To connect to a local server keep serverIP = "localhost";
**To run the client, run the file Player.java**

## Game Instructions
*Please note: A server must have already been started before running the client. Otherwise you will get a 'Connect
to Server' error when trying to enter a username.*

1. Run Player.java
2. You will be met with a screen to enter a username; this will be used to identify yourself throughout the course of 
the game.
3. Once a valid username is entered, you will now be able to see all active users (those not in a game already) and can request
a game with them by selected their name on the list and pressing 'Play'.
4. Players can then accept or decline invitations to play. *Note there is a time limit on request*
5. Once a user has accepted you will be invited to place your ships, to change the orientation or the active ship to be placed,
please click the corresponding buttons.
6. After you have confirmed, you will have to wait for your opponent to confirm their ships.
7. The game will start; you will have a time limit on how long you can spend deliberating on a move. You can also chat to
your opponent using the chat in the middle of the screen. To place a move click on the square you would like to fire a 
missile at, if you hit a part of their ship, the square will turn red and you will have another go, else it will show as blue
and you will have lost your turn.
8. Once the game is finished you will return to the lobby. During the ship placement or during at any point during the game
you can also return to the lobby pressing the 'home' button. *Note that your opponent will win by default if you quit*