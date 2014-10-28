package Model;

import View.MatchFrame;
import View.WelcomeFrame;

import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;


public class Player extends Observable {

    private WelcomeFrame welcomeFrame;
    private MatchFrame matchFrame;

    private static String name;

    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;
    private PlayerThread receivingThread;

    private DefaultListModel<String> playersModel;

    public Player() {

        welcomeFrame = new WelcomeFrame();
        welcomeFrame.setVisible(true);

        // handles any players closing game
        welcomeFrame.addWindowListener(new WindowCloser());

        // enter button/text field listeners
        welcomeFrame.addConnectButtonListener(new ConnectButtonListener());
        welcomeFrame.addEnterNameListener(new EnterNameListener());
    }

    public void addToModel(String s) {
        playersModel.addElement(s);
    }

    public void clearModel() {
        playersModel.clear();
    }

    public void newConnection() {
        try {
            socket = new Socket("localhost", 4445);

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println("Name is: " + name);

            receivingThread = new PlayerThread(out, in, this);
            receivingThread.start();

            updateViews();

            // TODO: Not have the port number and ip hardcoded

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void launchMatchFrame() {

        welcomeFrame.setVisible(false);

        playersModel = new DefaultListModel<>();
        updateViews();

        matchFrame = new MatchFrame(this);
        matchFrame.setVisible(true);
        addObserver(matchFrame);

        matchFrame.addListMouseAdapter(new ListMouseAdapter());
        matchFrame.addPlayButtonListener(new PlayButtonListener());
        matchFrame.addPlayRandButtonListener(new PlayRandButtonListener());
    }

    public DefaultListModel<String> getPlayersModel() {
        return playersModel;
    }

    public void updateViews() {
        setChanged();
        notifyObservers();
    }

    class WindowCloser extends WindowAdapter {

        @Override
        public void windowClosing(WindowEvent e) {
            if (socket != null) {
                out.println("Closing");
            }
        }
    }

    class EnterNameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            name = welcomeFrame.getTextfieldText();

            newConnection();
            launchMatchFrame();
        }
    }

    class ConnectButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            name = welcomeFrame.getTextfieldText();

            newConnection();
            launchMatchFrame();
        }
    }

    class PlayButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String player = playersModel.getElementAt(matchFrame.getSelectedIndex());

            if (!(name.equals(player))) {
                out.println("Request " + player);
            }
        }
    }

    class PlayRandButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO: PlayRandom Button Listener
        }
    }

    class ListMouseAdapter extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent evt) {
            if (evt.getClickCount() == 2) {

                String player = playersModel.getElementAt(matchFrame.getSelectedIndex());

                if (!(name.equals(player))) {
                    out.println("Request " + player);
                }
            }
        }
    }

    public static void main(String[] args) {
        new Player();
    }

}
