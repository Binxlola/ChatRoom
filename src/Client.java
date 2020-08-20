import javax.swing.*;
import java.io.IOException;
import java.net.Socket;
import java.util.*;

public class Client extends Thread {

    public static Client _client = null;

    public static final String HOST_NAME = "localhost";
    public static final int HOST_PORT = 7777;
    private Listener listener;
    private final HashMap<UUID,Object[]> participants = new HashMap<UUID,Object[]>();

    // User details
    private ImageIcon profileImg = new ImageIcon("src\\user.png");
    private final UUID ID;
    private String userName;

    private Client() {
        // Local client is always a participant
        this.ID = UUID.randomUUID();
        this.addParticipant(this.ID, this.userName, this.profileImg);
    }

    /**
     * If no Client object exists a new one will be create and returned. If one does exist then it will be returned
     * @return The Client object
     */
    public static Client getClient() {
        if(_client == null) {
            _client = new Client();
        }
        return _client;
    }

    /**
     * Starts a connection to the server
     */
    public void connect() {
        try {
            Socket socket = new Socket(HOST_NAME, HOST_PORT); // Client side socket
            this.listener = new Listener(socket, this);
            listener.start();

            listener.set_controller(ClientController.getController());
            // Send setup data for connecting client
            String connectMessage = Message.createFormattedString(Message.MessageType.CONNECT, this, null);
            listener.sendToServer(connectMessage);
        } catch (IOException e) {
            System.err.println("Client could not make connection: " + e);
            System.exit(-1);
        }
    }

    /**
     * Sends a disconnect message to the server
     */
    public void disconnect() {
        participants.clear();
        String disconnectMessage = Message.createFormattedString(Message.MessageType.DISCONNECT, this, null);
        listener.sendToServer(disconnectMessage);
    }

    /**
     * Adds a connected client to the participants mapping
     * @param ID The Unique Client ID
     * @param name The selected username for the Client
     * @param profileImg The Client's current profile image
     */
    public void addParticipant(UUID ID, String name, ImageIcon profileImg) {
        // If the current client does not exist in the mapping, they get added
        if(!participants.containsKey(ID)) {
            Object[] temp = {name,profileImg};
            participants.put(ID,temp);
        }
    }

    /**
     * Taking in a participants String using the format "ID,Username,Icon String" for each client separated by ";"
     * ";" is base 64 encoding safe (it will not be found in the encoded string)
     * will parse the string and add each Client to the participants mapping
     * @param participantsString The correctly formatted participants string for parsing
     */
    public void updateParticipants(String participantsString) {
        this.participants.clear(); // Reset local mapping, so we get 100% synced mapping
        String[] participants = participantsString.split(";");
        for(String participant: participants) {
            String[] parts = participant.split(",");
            this.addParticipant(UUID.fromString(parts[0]),parts[1],Message.convertStringToIcon(parts[2]));
        }

    }

    // Getters and Setters
    public Listener getListener() {return this.listener;}
    public String getUserName() {return this.userName;}
    public ImageIcon getProfileImg() {return this.profileImg;}
    public void setProfileImg(ImageIcon icon) {this.profileImg = icon;}
    public void setUsername(String name) {this.userName = name;}
    public UUID getID() {return  this.ID;}
    public HashMap<UUID,Object[]> getParticipants() {return this.participants;}
}
