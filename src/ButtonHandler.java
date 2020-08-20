import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class ButtonHandler implements ActionListener {

    private final ClientController _controller;
    private final ClientView clientView;
    private final LoginView loginView;

    public ButtonHandler(ClientController controller, ClientView ClientView, LoginView loginView) {
        this._controller = controller;
        this.clientView = ClientView;
        this.loginView = loginView;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        String name = button.getName();

        // Checks which method to call to handle the event
        switch(name) {
            case "SEND": this.send();break;
            case "PROFILE": this.profile();break;
            case "LOGIN": this.login();break;
            case "PARTICIPANTS": this.participants();break;
            case "FILE_UPLOAD": this.fileUpload();break;
            case "FILE_DOWNLOAD": this.fileDownload(e.getActionCommand(), button.getText());break;
            case "DISCONNECT": this._controller.disconnectClient();break;
        }

    }

    /**
     * Gets any text that is in the user text input area, will then send the text to all other clients via the server
     */
    private void send() {
        JTextArea messageArea = this.clientView.getMessageArea();
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

    private void login() {
        String userName = this.loginView.getUsername().getText();
//        if (userName.length() > 12) {}
//        if(userName.contains(",")) {}
        this._controller.setNewUsername(userName);
    }

    /**
     * Asks the user for a location where a given file will be written and saved to
     * @param fileData The base64 encoded string of the file byte data
     * @param name Name of the file being downloaded
     */
    private void fileDownload(String fileData, String name) {
        File selectedFile = this.selectFile(false, name);
        byte[] fileBytes = Message.decodeBase64toByteArray(fileData);

        System.out.println(selectedFile.getAbsoluteFile());

        try {
            File file = new File(String.valueOf(selectedFile.getAbsoluteFile()));
            OutputStream out = new FileOutputStream(file);

            out.write(fileBytes);
            out.close();
        } catch(Exception e) {
            System.err.println("There was an error saving the file");
            e.printStackTrace();
        }
    }

    /**
     * Opens a file selector for the user to select a file. If the user selects a compatible file an image icon
     * will be created and set as the new client profile image and profile button icon
     */
    private void profile() {
        File selectedFile = this.selectFile(true, "");

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
     * Requests the user select a file they wish to share with other participants.
     * If the file is valid it will be sent to the server and distributed
     */
    private void fileUpload() {
        File selectedFile = this.selectFile(true, "");
        try {
            byte[] data = Files.readAllBytes(selectedFile.toPath());

            // Reading file was successful, it can be sent to the server
            _controller.getClientListener().sendToServer(Message.createFormattedString(
                    Message.MessageType.FILE,
                    Client.getClient(),
                    data,
                    selectedFile.getName())
            );
        } catch (IOException e) {
            System.err.println("File could not be opened");
            e.printStackTrace();
        }



    }

    /**
     * Opens the dialog to select a file, once a file has been select it will be returned.
     * @return The user selected file
     */
    private File selectFile(boolean open, String fileName) {
        JFileChooser fileChooser = this.clientView.getFileChooser();
        fileChooser.setSelectedFile(new File(fileName));
        File selectedFile = null;
        int selected = open ? fileChooser.showOpenDialog(this.clientView) : fileChooser.showSaveDialog(this.clientView);

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
        JScrollPane participantsDisplay = this.clientView.getParticipantsScroll();

        // Get required dimensions and compute new dimensions
        int mainWidth = this.clientView.getWidth();
        int mainHeight = this.clientView.getHeight();
        int secondaryWidth = participantsDisplay.getWidth() + 50;

        // expand the main view
        this.clientView.setSize((mainWidth + secondaryWidth), mainHeight);
        this._controller.setSize(this.clientView.getWidth() + 1, this._controller.getHeight());
        this.clientView.add(participantsDisplay);

        // Tell controller this panel is open
        this._controller.setParticipantsOpen(true);

        // Repaint components to display new changes
        this.repaint();
    }

    /**
     * Will close the participants view of the application
     */
    private void closeParticipants() {
        JScrollPane participantsDisplay = this.clientView.getParticipantsScroll();

        // Get required dimensions
        int originalWidth = this._controller.getOriginalWidth();
        int originalHeight = this._controller.getOriginalHeight();

        // Set back to original size
        this.clientView.remove(participantsDisplay);
        this.clientView.setSize(originalWidth, originalHeight);
        this._controller.setSize(originalWidth + 1, originalHeight + 1);

        // Tell controller this panel is closed
        this._controller.setParticipantsOpen(false);

        // Repaint components to display new changes
        this._controller.repaint();
        this.clientView.repaint();
    }

    /**
     * Repaints the view and controller to display any visual changes
     */
    private void repaint() {
        this._controller.repaint();
        this.clientView.repaint();
    }
}
