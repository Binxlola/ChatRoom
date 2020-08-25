import javax.swing.*;

/**
 * Used for testing purposes
 */
public class ClientServer2 {

    public static void main(String[] args) {
        JFrame frame = ClientController.getController();
        Client.getClient().connect();
        frame.setVisible(true);
    }
}
