import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientController extends JFrame {

    private final Client model = new Client();
    private ClientView view = new ClientView(this.model);
    public static ClientController _controller = null;

    private ClientController(String title) {
        super(title); //#TODO allow user to change title of their client window

        getContentPane().add(this.view);

        setSize(this.view.getSize());
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

    public void displayMessage(String message) {
        JPanel messagePanel = this.view.getMessages();
        JLabel test = new JLabel(message);
        test.setSize(25,25);
        test.setLocation(0,0);
        messagePanel.add(test);
        messagePanel.repaint();
    }
    public Listener getClientListener() {return this.model.getListener();}

    public static void main(String[] args) {
        JFrame frame = ClientController.getController();
        frame.setVisible(true);

    }
}
