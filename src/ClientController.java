import javax.swing.*;

/**
 * Extends JFrame and represents the overall client side of the chat room application, handles the interactions between
 * different views and the client model
 *
 * @author Jason Smit
 */
public class ClientController extends JFrame {

    public static ClientController _controller = null;

    private final Client _model = Client.getClient();
    private final ClientView clientView = new ClientView(this._model);
    private final LoginView loginView = new LoginView(this._model);

    private boolean participantsOpen = false;
    private boolean loggedIn = false;
    private final int originalWidth, originalHeight;
    private final ButtonHandler buttonHandler = new ButtonHandler(this, this.clientView, this.loginView);

    private ClientController(String title) {
        super(title); //#TODO allow user to change title of their client window

        // Setup the frame
        getContentPane().add(this.loginView);
        setSize(this.loginView.getWidth() + 1, this.loginView.getHeight() + 1);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Store view sizes for later use
        this.originalHeight = this.clientView.getHeight();
        this.originalWidth = this.clientView.getWidth();

        // Add all event handlers below
        loginView.getLogin().addActionListener(buttonHandler);

        clientView.getSendBtn().addActionListener(buttonHandler);
        clientView.getParticipantsBtn().addActionListener(buttonHandler);
        clientView.getProfileBtn().addActionListener(buttonHandler);
        clientView.getFileUpload().addActionListener(buttonHandler);
        clientView.getDisconnectBtn().addActionListener(buttonHandler);

    }

    /**
     * If not ClientController object exists a new one will be create and returned.
     * If one does exist then it will be returned
     * @return The ClientController object
     */
    public static ClientController getController() {
        if(_controller == null) {
            _controller = new ClientController("Test Client");
        }
        return _controller;
    }


    // Methods interacting with the ClientView or ClientModel
    /**
     * Set the client profile image to the given image icon, and then sets the profile button icon to the same
     * @param icon The image icon to be set
     */
    public void setNewProfileImg(ImageIcon icon) {
        this._model.setProfileImg(icon);
        this.clientView.getProfileBtn().setIcon(this._model.getProfileImg());
    }

    /**
     * Set the user name of the client to the given String, if the user is not logged in already
     * The user will be set to logged in and the view will change to the Client View and window repainted.
     * @param username The name to be set as the clients username
     */
    public void setNewUsername(String username) {
        this._model.setUsername(username);
        if (!loggedIn) {
            this.setContentPane(this.clientView);
            setSize(this.clientView.getWidth() + 1, this.clientView.getHeight() + 1);
            this.repaintClient();
            this._model.connect();
            this.loggedIn = true;
        }
    }

    /**
     * Disconnect the client from the server, update the main client view and switch to the login view. Finally switch login
     * flag to false as client has not logged out
     */
    public void disconnectClient() {
        this._model.disconnect();
        this.updateView();

        this.setContentPane(this.loginView);
        this.repaintClient();
        this.loggedIn = false;
    }

    /**
     * Updates the view as required and repaints the client window
     */
    public void updateView() {
        this.clientView.update();
        this.repaintClient();
    }

    /**
     * Repaint the view and controller of the client window
     */
    private void repaintClient() {
        this.repaint();
        this.clientView.repaint();
        this.loginView.repaint();
    }

    /**
     * Passes a received message to the view so as to be added to the message display, will then repaint
     * @param message The received message contained in a Message object
     */
    public void displayMessage(Message message) {
        this.clientView.addMessageBlock(message, message.getType());
        this.repaintClient();
    }

    // Getters and Setters
    public Listener getClientListener() {return this._model.getListener();}
    public boolean isParticipantsOpen() {return this.participantsOpen;}
    public void setParticipantsOpen(boolean open) {this.participantsOpen = open;}
    public int getOriginalWidth() {return this.originalWidth;}
    public int getOriginalHeight() {return  this.originalHeight;}
    public ButtonHandler getButtonHandler() {return  this.buttonHandler;}

    public static void main(String[] args) {
        JFrame frame = ClientController.getController();
        frame.setVisible(true);
    }
}
