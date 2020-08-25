import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a java swing panel that is used to display the main view of the chat room application to the client
 * Such as messages, participants and profile details
 *
 * @author Jason Smit
 */
public class ClientView extends JPanel {

    private final Client _model;

    // GUI Components below
    private final JTextArea messageArea;
    private final JButton sendBtn, fileUpload, participantsBtn, profileBtn, disconnectBtn;
    private final JScrollPane messageWindow, participantsScroll;
    private final JPanel messages, participants;
    private final JFileChooser fileChooser;

    public ClientView(Client model) {

        this._model = model;

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
        messages.setPreferredSize(new Dimension(535,2000));
        messageWindow = new JScrollPane(messages,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        messageWindow.setSize(535, 850);
        messageWindow.setLocation(5, 60);
        messageWindow.setBorder(BorderFactory.createEmptyBorder());
        add(messageWindow);

        // Set up text area for client message input
        fileUpload = new JButton(new ImageIcon("src\\plus.png"));
        fileUpload.setSize(32,32);
        fileUpload.setLocation(10,920);
        fileUpload.setBorder(BorderFactory.createEmptyBorder());
        fileUpload.setOpaque(false);
        fileUpload.setContentAreaFilled(false);
        fileUpload.setBorderPainted(false);
        fileUpload.setName("FILE_UPLOAD");
        add(fileUpload);

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

    /**
     * Update an information contained in the model that needs to be displayed in the view by "default"
     */
    public void update() {
        this.setupParticipants();
    }

    /**
     * Create the Component for displaying a message in the message view. Then add the component to the view
     * @param msgObj The original message object to be used
     * @param type The type of message
     */
    public void addMessageBlock(Message msgObj, Message.MessageType type) {
        int yPos;
        int xPos;

        Component[] componentList = this.messages.getComponents();
        if(componentList.length > 0) {
            JPanel lastMsg = (JPanel) componentList[componentList.length - 1];
            yPos = lastMsg.getY() + lastMsg.getHeight() + 5; // The extra 5 is for padding
        } else {
            yPos = 5;
        }


        JPanel container = new JPanel();
        container.setLayout(null);
        JLabel message = null;
        JButton file = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        switch (type) {
            case MESSAGE:
                message = new JLabel();
                LineBorder roundedBorder = new LineBorder(Color.CYAN, 1, true);
                message.setText("<html>" + msgObj.getMessage() + "</html>");
                message.setSize(190, this.getContainerHeight("<html>" + msgObj.getMessage() + "</html>", null));
                message.setLocation(2,5);
                container.setBorder(BorderFactory.createTitledBorder(roundedBorder,
                        msgObj.getClientName(),
                        msgObj.getID().equals(this._model.getID()) ? 1 :3, // 0 is for left, 3 is for right
                        TitledBorder.DEFAULT_POSITION));
                container.setSize(320, message.getHeight() + 10);
                xPos = msgObj.getID().equals(this._model.getID()) ? 5 : this.messages.getWidth() - 325;
                break;
            case CONNECT:
            case DISCONNECT:
                message = new JLabel();
                message.setText(String.format("%s has %s the room at %s",
                        msgObj.getClientName(),
                        type.equals(Message.MessageType.CONNECT) ? "connected to" : "disconnected from",
                        formatter.format(LocalDateTime.now()))
                );
                message.setHorizontalAlignment(SwingConstants.CENTER);
                message.setVerticalAlignment(SwingConstants.CENTER);
                message.setSize(535, 30);
                container.setSize(535, 30);
                xPos = 10;
                break;
            case FILE:
                file = new JButton(msgObj.getFileName());
                file.setActionCommand(Message.encodeByteArray(msgObj.getData()));
                file.setSize(320, this.getContainerHeight(msgObj.getFileName(), null));
                file.setBorder(new LineBorder(Color.BLACK, 1, true));
                file.addActionListener(ClientController.getController().getButtonHandler());
                file.setName("FILE_DOWNLOAD");
                container.setSize(320, file.getHeight());
                xPos = msgObj.getID().equals(this._model.getID()) ? 5 : this.messages.getWidth() - 325;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }

        // Container setup
        container.setOpaque(false);
        container.setLocation(xPos,yPos);
        container.add(type.equals(Message.MessageType.FILE) ? file : message);
        this.messages.add(container);


    }

    /**
     * Used to find the minimum height of a container based in the height of the text held in the container
     * @param text The string of text that is to be in the container
     * @param font The font that is being set to the text inside the container
     * @return The calculated height of the text
     */
    private int getContainerHeight(String text, Font font) {
        FontMetrics metrics;
        metrics = getGraphics().getFontMetrics(font != null ? font : UIManager.getDefaults().getFont("Label.font"));
        int width = metrics.stringWidth(text);
        int linesNeeded = width / 300;

        return 30 + (linesNeeded * 30);
    }

    /**
     * Preps the participants view for component generation
     */
    private void setupParticipants() {
        Component[] components = this.participants.getComponents();
        HashMap<UUID,Object[]> participants = this._model.getParticipants();
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
    public JScrollPane getMessageWindow() {return this.messageWindow;}
    public JTextArea getMessageArea() {return this.messageArea;}
    public JFileChooser getFileChooser() {return this.fileChooser;}
    public JButton getProfileBtn() {return this.profileBtn;}
    public JButton getParticipantsBtn() {return this.participantsBtn;}
    public JButton getSendBtn() {return this.sendBtn;}
    public JButton getFileUpload() {return this.fileUpload;}
    public JButton getDisconnectBtn() {return this.disconnectBtn;}
}
