import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonHandler implements ActionListener {

    private final ClientController _controller;
    private final ClientView VIEW;

    public ButtonHandler(ClientController controller, ClientView view) {
        this._controller = controller;
        this.VIEW = view;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        String name = button.getName();

        System.out.println(e);

        switch(name) {
            case "SEND": this.send();break;
            case "PROFILE": this.profile();break;
            case "PARTICIPANTS": this.participants();break;
        }

    }

    private void send() {
        JTextArea messageArea = this.VIEW.getMessageArea();
        String message = messageArea.getText();
        if(!message.equals("")) {
            _controller.getClientListener().sendToServer(message);
            messageArea.setText(null); // Clears our the text area
        }
    }

    private void profile() {

    }

    private void participants() {
        JPanel participantsDisplay = this.VIEW.getParticipants();
        int mainWidth = this.VIEW.getWidth();
        int secondaryWidth = participantsDisplay.getWidth() + 20;

        // expand the main view
        this.VIEW.setSize((mainWidth + secondaryWidth), this.VIEW.getHeight());
        this.VIEW.add(participantsDisplay);
        this._controller.repaint();
        this.VIEW.repaint();
    }
}
