import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Listener extends Thread {

    private final Socket SOCKET;
    private boolean stop = false;
    private ChatServer SERVER = null;
    private Client CLIENT = null;
    private ClientController _controller = null;
    BufferedReader reader;
    PrintWriter writer;

    /**
     * Creates a listener that is to be used client side
     * @param socket The client side socket connection
     * @param client One of the clients currently connected to the server
     */
    public Listener(Socket socket, Client client) {
        super("Connection");
        this.SOCKET = socket;
        this.CLIENT = client;
    }

    /**
     * Creates a listener that is to be used server side
     * @param socket One of the server side socket connections
     * @param server The main server for connecting clients
     */
    public Listener(Socket socket, ChatServer server) {
        super("Connection");
        this.SOCKET = socket;
        this.SERVER = server;
    }

    public void set_controller(ClientController controller) {this._controller = controller;}

    /**
     * Send a client message to the server main chat server
     * @param message The message being sent to the server
     */
    public void sendToServer(String message) {
        this.writer.println(message);
    }

    /**
     * Receives a message from the server
     * @param message The message coming in from the server
     */
    public void receiveFromServer(String message) {
        this.writer.println(message);
    }

    /**
     * Loops through the list of connections to the server, for each one the given messages will be sent to.
     * @param message The string message that is to be sent to all clients
     */
    public void serveToClients(String message) {
        for(Listener connection : this.SERVER.getServerConnections()) {
            connection.receiveFromServer(message);
        }
    }

    @Override
    public void run() {
        try {
            this.reader = new BufferedReader(new InputStreamReader(this.SOCKET.getInputStream()));
            this.writer = new PrintWriter(this.SOCKET.getOutputStream(), true);
            String message;

            while(!stop) {
                while(!this.reader.ready()) {Thread.sleep(1);} // There has been no data, wait before checking again

                message = this.reader.readLine(); // Data has come through

                if(this.SERVER != null) {
                    this.serveToClients(message);
                } else if(this.CLIENT != null) {
                    this._controller.displayMessage(message);
                }
            }

            // Connection has been terminated, close down socket and IO streams
            this.reader.close();
            this.writer.close();
            this.SOCKET.close();

        } catch (IOException | InterruptedException e) {
            System.out.println("There was an error when listening for messages");
        }
    }
}
