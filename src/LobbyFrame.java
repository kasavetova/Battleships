import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class LobbyFrame extends JFrame{
	
    private JPanel listPanel;
    private DefaultListModel<String> playersModel;
    private JButton playButton;
    private JScrollPane scrollPane;
    private JPanel rightPanel;
    private JPanel infoPanel;
    private JPanel buttonPanel;
    
    private Player player;
    
	public LobbyFrame(Player player){
		this.player = player;
		initialiseUI();
	}
	
	public void initialiseUI(){
		
		setSize(new Dimension(260, 325));
        setResizable(false);
        setLocationRelativeTo(null); // centers window on screen, must be called after setSize()
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		
		this.setTitle("You are logged in as: " + player.getName());
        GridBagConstraints gc = new GridBagConstraints();

        listPanel = new JPanel(new GridBagLayout());
        add(listPanel, BorderLayout.CENTER);

        playersModel = new DefaultListModel<String>();
        final JList<String> players = new JList<String>(playersModel);
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
        playButton.setEnabled(false);
        buttonPanel.add(playButton);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.weightx = 0;
        gc.weighty = 0;
        gc.anchor = GridBagConstraints.PAGE_END;
        gc.fill = GridBagConstraints.BOTH;
        gc.insets = new Insets(5, 5, 5, 5);
        rightPanel.add(buttonPanel, gc);
        
        this.addWindowListener(new WindowAdapter() {
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
        setMinimumSize(new Dimension(520, 370));
        pack();
	}
	public void addItem(String playername){
        playersModel.addElement(playername);
	}
	public void deleteItem(String playername){
		playersModel.removeElement(playername);
	}
	public void enablePlayButton(boolean b){
		playButton.setEnabled(b);
	}
	public void updateLobby(ArrayList<String> playersList){
		playersModel.clear();
		for (String aPlayersList : playersList) {
            if (!aPlayersList.equals(player.getName())) {
                playersModel.addElement(aPlayersList);
            }
        }
	}
}
