import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class Message implements Serializable {

    private String status;
    private UUID ID;
    private String name;
    private String message;
    private ImageIcon ImageIcon;
    private byte[] data;
    private MessageType type;

    /**
     * Will parse a message and init the message object with required fields
     * @param message The message meeting correct format
     */
    public Message(String message) {
        String[] typeAndMessage = message.split("=");
        this.type = MessageType.valueOf(typeAndMessage[0]);

        // Loop through each part of the data section of the string
        for(String part : typeAndMessage[1].split(",")) {

            // Split the part of data for it's type and data
            String[] partSplit = part.split(":");
            String data = partSplit[1];

            // Assign parts of data string to corresponding variable
            switch (partSplit[0]) {
                case "USERNAME": this.name = data;break;
                case "ID": this.ID = UUID.fromString(data);break;
                case "MESSAGE": this.message = data;break;
                case "ICON": this.ImageIcon = this.convertStringToIcon(data);break;

            }
        }
    }

    /**
     * Used when sending a message or data to the server. Will format this string such that it can be parsed by the server
     * and clients receiving the message/data
     * @param type Type of data being sent to the server
     * @param client The client that is sending the message to the server
     * @param message The message that is being sent to the server
     * @return The correctly formatted string
     */
    public static String createStringForServer(MessageType type, Client client, String message) {
        String messageToSend = "";

        switch (type) {
            case CONNECT:
                messageToSend = String.format("%s=USERNAME:%s,ID:%s,ICON:%s",
                        type,client.getUserName(),client.getID(),convertIconToString(client.getProfileImg()));
                break;
            case MESSAGE:
                messageToSend = String.format("%s=USERNAME:%s,ID:%s,MESSAGE:%s", type,
                        client.getUserName(),client.getID(),message);
                break;
            case FILE: //#TODO setup the file case
                break;
        }

        return messageToSend;
    }

    public static String generateParticipantsString(HashMap<UUID,Object[]> participants) {
        String participantsString = "";
        Iterator<Map.Entry<UUID,Object[]>> iterator = participants.entrySet().iterator();

        while(iterator.hasNext()) {
            Map.Entry<UUID,Object[]> pair = iterator.next();
            Object[] details = pair.getValue();

            String temp = String.format("%s,%s,%s#",pair.getKey(),details[0],convertIconToString((ImageIcon) details[1]));
            participantsString += temp;


        }
        return participantsString;
    }

    private static String convertIconToString(javax.swing.ImageIcon image) {
        BufferedImage img = new BufferedImage(image.getIconWidth(),image.getIconHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = img.createGraphics();

        image.paintIcon(null, g,0,0);
        g.dispose();

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, "jpg", b);
        } catch (Exception e) {
            e.printStackTrace();
        }

        byte[] imageInByte = b.toByteArray();

        return Base64.getEncoder().encodeToString(imageInByte);
    }

    private ImageIcon convertStringToIcon(String encodedString) {
        byte[] bytes = Base64.getDecoder().decode(encodedString);
        try {
            return new ImageIcon(ImageIO.read(new ByteArrayInputStream(bytes)));
        } catch (IOException e) {
            System.err.println("Error occurred converting string to Icon");
            e.printStackTrace();
        }
        return null;
    }

    public MessageType getType() {return this.type;}
    public String getName() {return this.name;}
    public String getMessage() {return this.message;}
    public UUID getID() {return this.ID;}
    public ImageIcon getImageIcon() {return this.ImageIcon;}

    public enum MessageType {
        CONNECT, FILE, MESSAGE
    }
}
