import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class ChatServer {

    public static final int PORT = 7777;
    private boolean stopRequested = false;
    private final ArrayList<Listener> connections = new ArrayList<Listener>();

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

    public ArrayList<Listener> getServerConnections() {return this.connections;}

    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        server.startServer();
    }
}