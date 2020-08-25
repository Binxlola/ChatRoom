import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * A simple server design to handle connections from clients wanting to message each other and share data
 */
public class ChatServer {

    public static final int PORT = 7777;
    private boolean stopRequested = false;
    private final ArrayList<Listener> connections = new ArrayList<>();
    private final HashMap<UUID,Object[]> participants = new HashMap<>();

    public void startServer() {
        ServerSocket serverSocket = null;

        // Start up the main server
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started at " + InetAddress.getLocalHost() + " on port " + PORT);
        } catch (IOException e) {
            System.err.println("Server can't listen on port: " +e);
            System.exit(-1);
        }

        // Continuously keep looking for new connection requests
        try {
            while(!stopRequested) {
                Socket socket = serverSocket.accept(); // Accepts a new connection
                Listener newConnection = new Listener(socket, this);
                newConnection.start();
                connections.add(newConnection);
            }
        } catch(IOException e) {
            System.err.println("Can't accept client connection: " + e);
        }
    }

    /**
     * Adds a participant to the currently maintained array of connected clients
     * @param ID The Unique ID of the client being added
     * @param name The username of the client being added
     * @param profileImg The the portfolio image of the client being added
     */
    public void addParticipant(UUID ID, String name, ImageIcon profileImg) {
        Object[] temp = {name,profileImg};
        this.participants.put(ID,temp);
    }

    public ArrayList<Listener> getServerConnections() {return this.connections;}
    public HashMap<UUID,Object[]> getParticipants() {return this.participants;}

    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        server.startServer();
    }
}