import javax.swing.*;
import java.awt.*;

public class ClientView extends JPanel {

    private Client model;

    // GUI Components below
    private final JTextArea messageArea;
    private final JButton sendBtn, messageOptions, participantsBtn, profile;
    private final JScrollPane messageWindow;
    private final JPanel messages, participants;

    public ClientView(Client model) {

        this.model = model;

        // Components will be laid out manually
        setLayout(null);

        // Setup menu buttons
        profile = new JButton(new ImageIcon("src\\user.png"));
        profile.setSize(24,24);
        profile.setLocation(510, 10);
        profile.setBorder(BorderFactory.createEmptyBorder());
        profile.setOpaque(false);
        profile.setContentAreaFilled(false);
        profile.setBorderPainted(false);
        profile.setName("PROFILE");
        add(profile);

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
        participants.setLayout(new GridLayout(1, 1, 0, 3));
        participants.setSize(230, 940);
        participants.setLocation(560, 10);
        participants.setBackground(Color.RED);















        setSize(560, 1000);
    }

    public void update() {
        this.messageWindow.repaint();
        this.repaint();
    }

    public JPanel getMessages() {return this.messages;}
    public JPanel getParticipants() {return this.participants;}
    public JTextArea getMessageArea() {return this.messageArea;}

    public JButton getProfile() {return this.profile;}
    public JButton getParticipantsBtn() {return this.participantsBtn;}
    public JButton getSendBtn() {return this.sendBtn;}
    public JButton messageOptions() {return this.messageOptions;}
}
