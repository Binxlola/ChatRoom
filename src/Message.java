import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * A class that is used to wrap a message that is sent between the server and it's connected clients
 * handles string formatting to make communication simple.
 * Also includes functionality for encoding images to string such that they can be sent as a string to the server
 *
 * @author Jason Smit
 */
public class Message implements Serializable {

    private UUID ID;
    private String clientName;
    private String message;
    private ImageIcon ImageIcon;
    private byte[] data;
    private String fileName;
    private final MessageType type;

    /**
     * Will parse a message and init the message object with required fields
     * @param message The message meeting correct format
     */
    public Message(String message) {
        String[] typeAndMessage = message.split("=", 2); // Make sure we split at first "=" ONLY
        String[] partSplit;
        String data;

        this.type = MessageType.valueOf(typeAndMessage[0]);

        if(!(type.equals(MessageType.CLIENT_UPDATE))) {
            // Loop through each part of the data section of the string
            for(String part : typeAndMessage[1].split(",")) {

                // Split the part of data for it's type and data
                partSplit = part.split(":");
                data = partSplit[1];

                // Assign parts of data string to corresponding variable
                switch (partSplit[0]) {
                    case "USERNAME": this.clientName = data;break;
                    case "ID": this.ID = UUID.fromString(data);break;
                    case "MESSAGE": this.message = data;break;
                    case "ICON": this.ImageIcon = convertStringToIcon(data);break;
                    case "DATA": this.data = decodeBase64toByteArray(data);
                    case "FILENAME": this.fileName = data;

                }
            }
        } else {
            partSplit = typeAndMessage[1].split(":");
            data = partSplit[1];
            this.message = data;
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
    public static String createFormattedString(MessageType type, Client client, String message) {
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
            case CLIENT_UPDATE:
                messageToSend = String.format("%s=MESSAGE:%s",type,message);
                break;
            case DISCONNECT:
                messageToSend = String.format("%s=USERNAME:%s,ID:%s", type, client.getUserName(), client.getID());
                break;
        }

        return messageToSend;
    }

    /**
     * Used when sending message or data to the server where the data is passed in form of a byte[] and not String
     * @param type Type of data being sent to the server
     * @param client The client that is sending the message to the server
     * @param data The array of byte data representing what is to be sent
     * @param fileName The file name if need be provided
     * @return The correctly formatted string
     */
    public static String createFormattedString(MessageType type, Client client, byte[] data, String fileName) {
        return String.format("%s=USERNAME:%s,ID:%s,DATA:%s,FILENAME:%s",
                type,
                client.getUserName()
                ,client.getID()
                ,encodeByteArray(data),
                fileName);
    }

    /**
     * Taking in a mapping of client ID's to client details, generates a String representation of the mapping
     * @param participants The mapping of clients/participants
     * @return The String representation of clients
     */
    public static String generateParticipantsString(HashMap<UUID,Object[]> participants) {
        StringBuilder participantsString = new StringBuilder();
        Iterator<Map.Entry<UUID,Object[]>> iterator = participants.entrySet().iterator();

        while(iterator.hasNext()) {
            Map.Entry<UUID,Object[]> pair = iterator.next();
            Object[] details = pair.getValue();

            // ";" is base 64 encoding safe (it will not be found in the encoded string)
            String temp = String.format("%s,%s,%s%s",
                    pair.getKey(),
                    details[0],
                    convertIconToString((ImageIcon) details[1]),
                    iterator.hasNext() ? ";" : "");
            participantsString.append(temp);


        }
        return createFormattedString(MessageType.CLIENT_UPDATE, null, participantsString.toString());
    }

    /**
     * Taking in an ImageIcon object, converts this to a Base64 encoded String
     * @param image The ImageIcon to be converted to a string
     * @return The ImageIcon converted to Base64 encoded String
     */
    private static String convertIconToString(ImageIcon image) {
        boolean hasAlpha;
        PixelGrabber grabber = new PixelGrabber(image.getImage(), 0, 0, 1, 1, false);
        ByteArrayOutputStream b = new ByteArrayOutputStream();

        try {
            // Check to see if Icon has a transparent background
            grabber.grabPixels();
            hasAlpha = grabber.getColorModel().hasAlpha();

            // If the icon has transparent background use ARGB and format png for buffered Image
            int type = hasAlpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
            String format = hasAlpha ? "png" : "jpg";

            BufferedImage img = new BufferedImage(image.getIconWidth(),image.getIconHeight(), type);
            Graphics g = img.createGraphics();
            image.paintIcon(null, g,0,0);
            g.dispose();
            ImageIO.write(img, format, b);
        } catch (Exception e) {
            e.printStackTrace();
        }

        byte[] imageInByte = b.toByteArray();

        return encodeByteArray(imageInByte);
    }

    /**
     * Taking in a Bas64 Encoded String of an ImageIcon object, will decode this string and covert back to ImageIcon
     * @param encodedString The Base64 encoded String
     * @return The ImageIcon decoded from the string
     */
    public static ImageIcon convertStringToIcon(String encodedString) {
        byte[] bytes = decodeBase64toByteArray(encodedString);
        try {
            return new ImageIcon(ImageIO.read(new ByteArrayInputStream(bytes)));
        } catch (IOException e) {
            System.err.println("Error occurred converting string to Icon");
            e.printStackTrace();
        }
        return null;
    }

    public static String encodeByteArray(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] decodeBase64toByteArray(String encodedString) {
        return Base64.getDecoder().decode(encodedString);
    }

    public MessageType getType() {return this.type;}
    public String getClientName() {return this.clientName;}
    public String getMessage() {return this.message;}
    public String getFileName() {return this.fileName;}
    public byte[] getData() {return this.data;}
    public UUID getID() {return this.ID;}
    public ImageIcon getImageIcon() {return this.ImageIcon;}


    /**
     * An enum for message type classifications
     */
    public enum MessageType {
        CONNECT, FILE, MESSAGE, CLIENT_UPDATE, DISCONNECT
    }
}
