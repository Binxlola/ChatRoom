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
         boolean isOpen = this._controller.isParticipantsOpen();
        if(!isOpen) {
            this.openParticipants();
        } else {
            this.closeParticipants();
        }
    }

    private void openParticipants() {
        JPanel participantsDisplay = this.VIEW.getParticipants();

        // Get required dimensions and compute new dimensions
        int mainWidth = this.VIEW.getWidth();
        int mainHeight = this.VIEW.getHeight();
        int secondaryWidth = participantsDisplay.getWidth() + 50;

        // expand the main view
        this.VIEW.setSize((mainWidth + secondaryWidth), mainHeight);
        this._controller.setSize(this.VIEW.getWidth() + 1, this._controller.getHeight());
        this.VIEW.add(participantsDisplay);

        // Tell controller this panel is open
        this._controller.setParticipantsOpen(true);

        // Repaint components to display new changes
        this._controller.repaint();
        this.VIEW.repaint();
    }

    private void closeParticipants() {
        JPanel participantsDisplay = this.VIEW.getParticipants();

        // Get required dimensions
        int originalWidth = this._controller.getOriginalWidth();
        int originalHeight = this._controller.getOriginalHeight();

        // Set back to original size
        this.VIEW.remove(participantsDisplay);
        this.VIEW.setSize(originalWidth, originalHeight);
        this._controller.setSize(originalWidth + 1, originalHeight + 1);

        // Tell controller this panel is closed
        this._controller.setParticipantsOpen(false);

        // Repaint components to display new changes
        this._controller.repaint();
        this.VIEW.repaint();
    }
}
