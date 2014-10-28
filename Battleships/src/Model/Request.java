package Model;

public class Request {

    private static ServerThread requester;
    private static ServerThread requestee;

    private static boolean reply = false;
    private static int answer = 3;

    public Request(ServerThread requester, ServerThread requestee) {
        Request.requester = requester;
        Request.requestee = requestee;

        requestee.message("Request Do you want to play a game with " + requester.getPlayerName());
    }

    public void answer(int x) {
        answer = x;
        reply = true;

        if (answer == 1) {
            System.out.println("Game Accepted");
            requester.message("YES");
            requester.setInGame(true);
            requestee.setInGame(true);
            reply = false;
            answer = 3;

        } else if (answer == 0) {
            System.out.println("Game Rejeceted");
            answer = 3;
            reply = false;
        }
    }
}