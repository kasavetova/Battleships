import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 * A graphical user interface displaying current list of online players.
 * Players select who they want to play with here.
 * 
 * @author Team 1-O
 */
public class LobbyFrame extends JFrame {

    private JPanel content;
    private DefaultListModel<String> playersModel;
    private JButton playButton;
    private JScrollPane scrollPane;
    private TitledBorder tbListHeader;

    private Player player;

    private Color backgroundColor = new Color(44, 62, 80);

    /**
     * @param player Player that is currently playing
     */
    public LobbyFrame(Player player) {
        this.player = player;
        initialiseUI();
    }

    /**
     * Initialises and creates the frame and widgets.
     */
    public void initialiseUI() {

        setSize(350, 350);
        setResizable(false);
        setLocationRelativeTo(null); // centers window on screen, must be called after setSize()
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        content = new JPanel(new BorderLayout(0, 10));
        content.setBorder(new EmptyBorder(10, 10, 10, 10));
        content.setBackground(backgroundColor);

        setContentPane(content);

        setTitle("You are logged in as: " + player.getName());

        playersModel = new DefaultListModel<>();
        final JList<String> players = new JList<String>(playersModel);
        players.setFont(new Font("EUROSTILE", Font.BOLD, 14));
        players.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        players.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    JList<String> source = (JList<String>) event.getSource();
                    int index = source.getSelectedIndex();
                    if (index > -1) playButton.setEnabled(true);
                }
            }
        });

        scrollPane = new JScrollPane(players);
        tbListHeader = new TitledBorder("CURRENTLY ONLINE:");
        tbListHeader.setTitleFont(new Font("EUROSTILE", Font.BOLD, 18));
        tbListHeader.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setViewportBorder(tbListHeader);

        content.add(scrollPane, BorderLayout.CENTER);

        playButton = new JButton("PLAY");
        playButton.setFont(new Font("EUROSTILE", Font.BOLD, 18));
        playButton.setEnabled(false);

        content.add(playButton, BorderLayout.SOUTH);

        this.addWindowListener(
                new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        player.sendServerRequest((new Request("UserClosed", player.getName())));
                    }
                }
        );

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (players.getSelectedIndex() >= 0) {
                    String playerName = playersModel.getElementAt(players.getSelectedIndex());
                    if (!(player.getName().equals(playerName))) {
                        player.setBusy(true);
                        playButton.setEnabled(false);
                        //UI WITH TIMER DISPLAYED HERE
                        player.sendServerRequest(new Request("GameRequest", player.getName(), playerName));
                    }
                }
            }
        });
    }

    /**
     * Add a String to the current online list widget model.
     * @param playername The name of the player to be added.
     */
    public void addItem(String playername) {
        playersModel.addElement(playername);
    }

    /**
     * Remove a String from the current online list widget model.
     * @param playername The name of the player to be removed.
     */
    public void deleteItem(String playername) {
        playersModel.removeElement(playername);
    }

    /**
     * Enable the play button.
     * @param b Whether or not the button should be enabled
     */
    public void enablePlayButton(boolean b) {
        playButton.setEnabled(b);
    }

    /**
     * Update the lobby online list widget from the model.
     * @param playersList The ArrayList of player names with which to populate the lobby
     */
    public void updateLobby(ArrayList<String> playersList) {
        playersModel.clear();
        for (String aPlayersList : playersList) {
            if (!aPlayersList.equals(player.getName())) {
                playersModel.addElement(aPlayersList);
            }
        }
    }
}
