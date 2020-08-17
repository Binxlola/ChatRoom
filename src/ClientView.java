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
    private final JButton sendBtn, messageOptions, participantsBtn, profileBtn, disconnectBtn;
    private final JScrollPane messageWindow, participantsScroll;
    private final JPanel messages, participants;
    private final JFileChooser fileChooser;

    public ClientView(Client model) {

        this.model = model;

        // Components will be laid out manually
        setLayout(null);

        // Setup menu buttons
        disconnectBtn = new JButton(new ImageIcon("src\\disconnected.png"));
        disconnectBtn.setSize(24,24);
        disconnectBtn.setLocation(5, 10);
        disconnectBtn.setBorder(BorderFactory.createEmptyBorder());
        disconnectBtn.setOpaque(false);
        disconnectBtn.setContentAreaFilled(false);
        disconnectBtn.setBorderPainted(false);
        disconnectBtn.setName("DISCONNECT");
        add(disconnectBtn);

        participantsBtn = new JButton(new ImageIcon("src\\team.png"));
        participantsBtn.setSize(24,24);
        participantsBtn.setLocation(470, 10);
        participantsBtn.setBorder(BorderFactory.createEmptyBorder());
        participantsBtn.setOpaque(false);
        participantsBtn.setContentAreaFilled(false);
        participantsBtn.setBorderPainted(false);
        participantsBtn.setName("PARTICIPANTS");
        add(participantsBtn);

        profileBtn = new JButton(new ImageIcon("src\\user.png"));
        profileBtn.setSize(24,24);
        profileBtn.setLocation(510, 10);
        profileBtn.setBorder(BorderFactory.createEmptyBorder());
        profileBtn.setOpaque(false);
        profileBtn.setContentAreaFilled(false);
        profileBtn.setBorderPainted(false);
        profileBtn.setName("PROFILE");
        add(profileBtn);

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
        participants.setPreferredSize(new Dimension(230, 2000));
        participantsScroll = new JScrollPane(participants,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        participantsScroll.setSize(230,940);
        participantsScroll.setLocation(560, 10);

        // Setup file chooser for file upload
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home"))); // set to home dir













        setSize(560, 1000);
    }

    public void update() {
        this.setupParticipants();
    }

    public void addMessageBlock(Message msgObj) {
//        JPanel lastMsg = (JPanel) this.messages.getComponent(-1);

        JPanel container = new JPanel();
        container.setLayout(null);
        JLabel user = new JLabel(), message = new JLabel();
        user.setFont(new Font("Arial", Font.PLAIN, 12));
        user.setText(msgObj.getName() + ":");
        user.setSize(65, 25);
        user.setLocation(0,0);



        message.setText("<html>" + msgObj.getMessage() + "</html>");
        message.setSize(190, this.getContainerHeight("<html>" + msgObj.getMessage() + "</html>", null));
        message.setLocation(66, 0);
        container.setSize(300, message.getHeight());
        container.setLocation(5,5);
        container.add(user);
        container.add(message);
        this.messages.add(container);


    }

    private int getContainerHeight(String text, Font font) {
        FontMetrics metrics;
        metrics = getGraphics().getFontMetrics(font != null ? font : UIManager.getDefaults().getFont("Label.font"));
        int width = metrics.stringWidth(text);
        int linesNeeded = width / 190;
        int height = 25 + (linesNeeded * 25);

        return height;
    }

    /**
     * Preps the participants view for component generation
     */
    private void setupParticipants() {
        Component[] components = this.participants.getComponents();
        HashMap<UUID,Object[]> participants = this.model.getParticipants();
        Iterator<Map.Entry<UUID,Object[]>> iterator = participants.entrySet().iterator();

        if(components.length > 0) {
            this.getParticipants().removeAll();
        }
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
            img.setBorder(BorderFactory.createEmptyBorder());
            img.setOpaque(false);

            // Add all the components
            container.add(userName);
            container.add(img);
            mainPanel.add(container);

            // Increase the y-axis placement value
            y+= 50;

        }
    }

    // Getters and Setters
    public JPanel getMessages() {return this.messages;}
    public JPanel getParticipants() {return this.participants;}
    public JScrollPane getParticipantsScroll() {return this.participantsScroll;}
    public JTextArea getMessageArea() {return this.messageArea;}
    public JFileChooser getFileChooser() {return this.fileChooser;}
    public JButton getProfileBtn() {return this.profileBtn;}
    public JButton getParticipantsBtn() {return this.participantsBtn;}
    public JButton getSendBtn() {return this.sendBtn;}
    public JButton getMessageOptions() {return this.messageOptions;}
    public JButton getDisconnectBtn() {return this.disconnectBtn;}
}
