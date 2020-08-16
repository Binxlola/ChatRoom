import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

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

        // Checks which method to call to handle the event
        switch(name) {
            case "SEND": this.send();break;
            case "PROFILE": this.profile();break;
            case "PARTICIPANTS": this.participants();break;
            case "MESSAGE_OPTIONS": this.messageOptions();break;
            case "DISCONNECT": this._controller.disconnectClient();break;
        }

    }

    /**
     * Gets any text that is in the user text input area, will then send the text to all other clients via the server
     */
    private void send() {
        JTextArea messageArea = this.VIEW.getMessageArea();
        String message = messageArea.getText();
        if(!message.equals("")) {
            _controller.getClientListener().sendToServer(Message.createFormattedString(
                    Message.MessageType.MESSAGE,
                    Client.getClient(),
                    message)
            );
            messageArea.setText(null); // Clears our the text area
        }
    }

    /**
     * Opens a file selector for the user to select a file. If the user selects a compatible file an image icon
     * will be created and set as the new client profile image and profile button icon
     */
    private void profile() {
        File selectedFile = this.selectFile();

        try {
            if(!(ImageIO.read(selectedFile) == null)) {
                this._controller.setNewProfileImg(new ImageIcon(selectedFile.getPath()));
                this.repaint();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Handles additional features such as file upload, emojis and others alike
     */
    private void messageOptions() {
        //#TODO change to a multi-function handler (Currently used for file upload)
        File selectedFile = this.selectFile();
    }

    /**
     * Opens the dialog to select a file, once a file has been select it will be returned.
     * @return The user selected file
     */
    private File selectFile() {
        JFileChooser fileChooser = this.VIEW.getFileChooser();
        File selectedFile = null;
        int selected = fileChooser.showOpenDialog(this.VIEW);

        if(selected == JFileChooser.APPROVE_OPTION) { selectedFile = fileChooser.getSelectedFile();}
        return selectedFile;
    }

    /**
     * Controls the flow for opening and closing the participants view
     */
    private void participants() {
         boolean isOpen = this._controller.isParticipantsOpen();
        if(!isOpen) {
            this.openParticipants();
        } else {
            this.closeParticipants();
        }
    }

    /**
     * Will open the participants view of the application
     */
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
        this.repaint();
    }

    /**
     * Will close the participants view of the application
     */
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

    /**
     * Repaints the view and controller to display any visual changes
     */
    private void repaint() {
        this._controller.repaint();
        this.VIEW.repaint();
    }
}
