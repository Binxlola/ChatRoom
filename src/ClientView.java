import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class ClientView extends JPanel {

    private Client model;

    // GUI Components below
    private final JTextArea messageArea;
    private final JButton sendBtn, messageOptions, participantsBtn, profileBtn;
    private final JScrollPane messageWindow;
    private final JPanel messages, participants;
    private final JFileChooser fileChooser;

    public ClientView(Client model) {

        this.model = model;

        // Components will be laid out manually
        setLayout(null);

        // Setup menu buttons
        profileBtn = new JButton(new ImageIcon("src\\user.png"));
        profileBtn.setSize(24,24);
        profileBtn.setLocation(510, 10);
        profileBtn.setBorder(BorderFactory.createEmptyBorder());
        profileBtn.setOpaque(false);
        profileBtn.setContentAreaFilled(false);
        profileBtn.setBorderPainted(false);
        profileBtn.setName("PROFILE");
        add(profileBtn);

        participantsBtn = new JButton(new ImageIcon("src\\team.png"));
        participantsBtn.setSize(24,24);
        participantsBtn.setLocation(470, 10);
        participantsBtn.setBorder(BorderFactory.createEmptyBorder());
        participantsBtn.setOpaque(false);
        participantsBtn.setContentAreaFilled(false);
        participantsBtn.setBorderPainted(false);
        participantsBtn.setName("PARTICIPANTS");
        add(participantsBtn);

        JSeparator divider = new JSeparator(JSeparator.HORIZONTAL);
        divider.setSize(535, 2);
        divider.setLocation(5, 50);
        add(divider);

        // Setup where the message will be displayed
        messages = new JPanel();
        messages.setLayout(null);
        messages.setBackground(Color.WHITE);
        messages.setBounds(0, 0 ,535,850);
        messageWindow = new JScrollPane(messages,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        messageWindow.setSize(535, 850);
        messageWindow.setLocation(5, 60);
        messageWindow.setBorder(BorderFactory.createEmptyBorder());
        add(messageWindow);

        // Set up text area for client message input
        messageOptions = new JButton(new ImageIcon("src\\plus.png"));
        messageOptions.setSize(32,32);
        messageOptions.setLocation(10,920);
        messageOptions.setBorder(BorderFactory.createEmptyBorder());
        messageOptions.setOpaque(false);
        messageOptions.setContentAreaFilled(false);
        messageOptions.setBorderPainted(false);
        messageOptions.setName("MESSAGE_OPTIONS");
        add(messageOptions);

        messageArea = new JTextArea();
        messageArea.setSize(450,32);
        messageArea.setLocation(50,920);
        add(messageArea);

        sendBtn = new JButton(new ImageIcon("src\\send.png"));
        sendBtn.setSize(100,24);
        sendBtn.setLocation(470, 923);
        sendBtn.setBorder(BorderFactory.createEmptyBorder());
        sendBtn.setOpaque(false);
        sendBtn.setContentAreaFilled(false);
        sendBtn.setBorderPainted(false);
        sendBtn.setName("SEND");
        add(sendBtn);

        // Setup open/close elements
        participants = new JPanel();
        participants.setLayout(null);
        participants.setSize(230, 940);
        participants.setLocation(560, 10);

        // Setup file chooser for file upload
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home"))); // set to home dir













        setSize(560, 1000);
    }

    public void update() {
        this.setupParticipants();
    }

    private void setupParticipants() {
        Component[] components = this.participants.getComponents();
        HashMap<UUID,Object[]> participants = this.model.getParticipants();
        Iterator<Map.Entry<UUID,Object[]>> iterator = participants.entrySet().iterator();

        this.generateParticipantsList(iterator, this.getParticipants());
    }

    /**
     * Initial setup of participants view for the local client
     * @param participants The mapping of currently connected clients/participants
     */
    private void generateParticipantsList(Iterator<Map.Entry<UUID,Object[]>> participants, JPanel mainPanel) {
        int y = 5;

        while(participants.hasNext()) {
            Map.Entry<UUID,Object[]> pair = participants.next();
            Object[] details = pair.getValue();

            // Main container
            JPanel container = new JPanel();
            container.setSize(220, 50);
            container.setLocation(5,y);
            container.setName(pair.getKey().toString());

            // UserName
            JLabel userName = new JLabel((String) details[0]);
            userName.setLocation(0,0);
            userName.setSize(150, 50);

            // Profile Image
            JLabel img = new JLabel();
            img.setIcon((ImageIcon) details[1]);
            img.setSize(50,50);
            img.setLocation(150, 0);

            // Add all the components
            container.add(userName);
            container.add(img);
            mainPanel.add(container);

            // Increase the y-axis placement value
            y+= 50;

        }
    }

    private void updateParticipantList(Iterator<Map.Entry<UUID,Object[]>> participants) {

    }

    // Getters and Setters
    public JPanel getMessages() {return this.messages;}
    public JPanel getParticipants() {return this.participants;}
    public JTextArea getMessageArea() {return this.messageArea;}
    public JFileChooser getFileChooser() {return this.fileChooser;}
    public JButton getProfile() {return this.profileBtn;}
    public JButton getParticipantsBtn() {return this.participantsBtn;}
    public JButton getSendBtn() {return this.sendBtn;}
    public JButton getMessageOptions() {return this.messageOptions;}
}
