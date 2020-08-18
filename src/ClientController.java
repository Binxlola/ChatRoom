import javax.swing.*;

public class ClientController extends JFrame {

    public static ClientController _controller = null;

    private final Client _model = Client.getClient();
    private final ClientView view = new ClientView(this._model);
    private boolean participantsOpen = false;
    private final int originalWidth, originalHeight;

    private ClientController(String title) {
        super(title); //#TODO allow user to change title of their client window

        // Setup the frame
        getContentPane().add(this.view);
        setSize(this.view.getWidth() + 1, this.view.getHeight() + 1);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Store view sizes for later use
        this.originalHeight = this.view.getHeight();
        this.originalWidth = this.view.getWidth();

        // Add all event handlers below
        ButtonHandler buttonHandler = new ButtonHandler(this, this.view);
        view.getSendBtn().addActionListener(buttonHandler);
        view.getParticipantsBtn().addActionListener(buttonHandler);
        view.getProfileBtn().addActionListener(buttonHandler);
        view.getMessageOptions().addActionListener(buttonHandler);
        view.getDisconnectBtn().addActionListener(buttonHandler);

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


    // Methods interacting with the ClientView or Client
    /**
     * Set the client profile image to the given image icon, and then sets the profile button icon to the same
     * @param icon The image icon to be set
     */
    public void setNewProfileImg(ImageIcon icon) {
        this._model.setProfileImg(icon);
        this.view.getProfileBtn().setIcon(this._model.getProfileImg());
    }

    public void disconnectClient() {
        this._model.disconnect();
        this.updateView();
    }

    private void test() {
        //TODO this is where the message layout can be setup
    }

    /**
     * Updates the view as required and repaints the client window
     */
    public void updateView() {
        this.view.update();
        this.repaintClient();
    }

    /**
     * Repaint the view and controller of the client window
     */
    private void repaintClient() {
        this.repaint();
        this.view.repaint();
    }

    /**
     * Passes a received message to the view so as to be added to the message display, will then repaint
     * @param message The received message contained in a Message object
     */
    public void displayMessage(Message message) {
        this.view.addMessageBlock(message, message.getType());
        this.repaintClient();

        //#TODO This needs to be taken out and have it's own method created to deal with this functionality
    }

    // Getters and Setters
    public Listener getClientListener() {return this._model.getListener();}
    public boolean isParticipantsOpen() {return this.participantsOpen;}
    public void setParticipantsOpen(boolean open) {this.participantsOpen = open;}
    public int getOriginalWidth() {return this.originalWidth;}
    public int getOriginalHeight() {return  this.originalHeight;}

    public static void main(String[] args) {
        JFrame frame = ClientController.getController();
        Client.getClient().connect();
        frame.setVisible(true);
    }
}
