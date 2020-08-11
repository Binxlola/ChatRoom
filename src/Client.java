import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

public class Client {

    public static final String HOST_NAME = "localhost";
    public static final int HOST_PORT = 7777;
    private Listener listener;

    // User details
    private ImageIcon profileImg;
    private int ID;
    private String userName;

    /**
     * Creates the client side socket and starts a client thread which handles the communication between the server and
     * client. After this a loop is started specifically for the clients input.
     */
    public Client() {
        try {
            Socket socket = new Socket(HOST_NAME, HOST_PORT); // Client side socket
            this.listener = new Listener(socket, this);
            listener.start();

            // Send setup data for connecting client
            String connectMessage = Message.createStringForServer(Message.MessageType.CONNECT, this, null);
            listener.sendToServer(connectMessage);
        } catch (IOException e) {
            System.err.println("Client could not make connection: " + e);
            System.exit(-1);
        }
    }

    public Listener getListener() {return this.listener;}
    public String getUserName() {return this.userName;}
    public int getID() {return  this.ID;}
}
