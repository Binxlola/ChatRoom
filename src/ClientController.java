import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientController extends JFrame {

    private final Client model = new Client();
    private ClientView view = new ClientView(this.model);
    public static ClientController _controller = null;

    private boolean participantsOpen = false;
    private int originalWidth, originalHeight;

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
        view.getProfile().addActionListener(buttonHandler);

        // Pass this controller to client listener to allow for messages to be passed to controller
        this.model.getListener().set_controller(this);
    }

    public static ClientController getController() {
        if(_controller == null) {
            _controller = new ClientController("Test Client");
        }
        return _controller;
    }

    /**
     * Creates a participant box with required details and adds this to the participants view
     * @param name The name of the participant
     */
    private void addParticipant(String name) {

    }

    private void test() {}

    public void displayMessage(String message) {
        Message messageObj = new Message(message);

        switch (messageObj.getType()) {
            // If the user is connecting, then they need to be added to the participants list
            case CONNECT: this.addParticipant(messageObj.getName());this.test();break;
            case MESSAGE: this.test();break;
        }

        //#TODO This needs to be taken out and have it's own method created to deal with this functionality
        JPanel messagePanel = this.view.getMessages();
        JLabel test = new JLabel(message);
        test.setSize(25,25);
        test.setLocation(0,0);
        messagePanel.add(test);
        messagePanel.repaint();
    }
    public Listener getClientListener() {return this.model.getListener();}

    public boolean isParticipantsOpen() {return this.participantsOpen;}
    public void setParticipantsOpen(boolean open) {this.participantsOpen = open;}

    public int getOriginalWidth() {return this.originalWidth;}
    public int getOriginalHeight() {return  this.originalHeight;}

    public static void main(String[] args) {
        JFrame frame = ClientController.getController();
        frame.setVisible(true);
    }
}
