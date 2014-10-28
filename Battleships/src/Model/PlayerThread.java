package Model;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * @author Muntasir Syed
 */

public class PlayerThread extends Thread {

    String input;

    private PrintWriter out;
    private BufferedReader in;

    private Player context;

    public PlayerThread(PrintWriter out, BufferedReader in, Player context) {
        this.out = out;
        this.in = in;
        this.context = context;
    }

    @Override
    public void run() {
        try {
            while ((input = in.readLine()) != null) switch (input) {
                case "Update":
                    update();
                    break;
                case "Request":
                    request();
                    break;
                case "YES":
                    accepted();
                    break;
                case "NO":
                    break;
                default:
                    System.out.println(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        try {
            input = input.substring(7);
            Scanner s = new Scanner(input);
            context.clearModel();

            while (s.hasNext()) {
                context.addToModel(s.next());
            }

            context.updateViews();

            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void request() {
        String s = input.substring(8);

        int i = JOptionPane.showConfirmDialog(null, s, "Game Request", JOptionPane.YES_NO_OPTION);
        System.out.println(i);

        // TODO: Link this to the game
        // no = 1, yes = 0

        switch (i) {
            case 1:
                out.println("NO");
                break;
            case 2:
                out.println("YES");
                // gameFrame();
            /*
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Request req = (Request)ois.readObject(); 
            if(req != null){
                System.out.println("NEW GAME");
            }
            */
        }
    }

    public void accepted() {
        System.out.println("Request Accepted");
        context.updateViews();
        // TODO: Launch game
    }
}