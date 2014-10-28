package View;

import Model.Player;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Muntasir Syed
 */

public class MatchFrame extends JFrame implements Observer {

    private JList<String> playerList; // string used as placeholder
    private Player currentPlayer;

    private JScrollPane scrollPane;
    private JPanel listPanel;

    private JButton playButton;
    private JButton playRandButton;
    private JPanel rightPanel;
    private JPanel infoPanel;
    private JPanel buttonPanel;

    /**
     * Constructor of the Match Making dialog.
     */
    public MatchFrame(Player currentPlayer) throws HeadlessException {

        super("Match Making");

        this.currentPlayer = currentPlayer;

        create();
        setMinimumSize(new Dimension(520, 370));
        setLocationRelativeTo(null); // centers window on screen, must be called after setSize()
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * Initialises and adds all widgets to the interface.
     */
    public void create() {

        GridBagConstraints gc = new GridBagConstraints();

        listPanel = new JPanel(new GridBagLayout());
        add(listPanel, BorderLayout.CENTER);

        playerList = new JList<>();
        playerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        playerList.setModel(currentPlayer.getPlayersModel());

        scrollPane = new JScrollPane(playerList);
        TitledBorder b = new TitledBorder("Currently online:");
        b.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setViewportBorder(b);

        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.BOTH;
        gc.insets = new Insets(5, 5, 5, 5);
        listPanel.add(scrollPane, gc);

        rightPanel = new JPanel(new GridBagLayout());
        add(rightPanel, BorderLayout.EAST);

        infoPanel = new JPanel();

        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 0;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.fill = GridBagConstraints.HORIZONTAL;
        rightPanel.add(infoPanel, gc);

        buttonPanel = new JPanel(new GridLayout());
        playButton = new JButton("Play");
        playRandButton = new JButton("Play Random");
        buttonPanel.add(playButton);
        buttonPanel.add(playRandButton);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.weightx = 0;
        gc.weighty = 0;
        gc.anchor = GridBagConstraints.PAGE_END;
        gc.fill = GridBagConstraints.BOTH;
        gc.insets = new Insets(5, 5, 5, 5);
        rightPanel.add(buttonPanel, gc);
    }

    public int getSelectedIndex() {
        return playerList.getSelectedIndex();
    }

    public void addPlayButtonListener(ActionListener l) {
        playButton.addActionListener(l);
    }

    public void addPlayRandButtonListener(ActionListener l) {
        playRandButton.addActionListener(l);
    }

    public void addListMouseAdapter(MouseListener m) {
        playerList.addMouseListener(m);
    }

    @Override
    public void update(Observable o, Object arg) {

        playerList.setModel(currentPlayer.getPlayersModel());
    }
}