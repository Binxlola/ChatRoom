import java.io.Serializable;

public class Message implements Serializable {

    private String status;
    private int ID;
    private String name;
    private String message;
    private String ImageIcon;
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
                case "ID": this.ID = Integer.parseInt(data);break;
                case "MESSAGE": this.message = data;break;

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
                messageToSend = String.format("%s=USERNAME:%s, ID:%s",
                        type,client.getUserName(),client.getID());
                break;
            case MESSAGE:
                messageToSend = String.format("%s=USERNAME:%s, ID:%s, MESSAGE:%s", type,
                        client.getUserName(),client.getID(),message);
                break;
            case FILE: //#TODO setup the file case
                break;
        }

        return messageToSend;
    }

    public MessageType getType() {return this.type;}
    public String getName() {return this.name;}
    public String getMessage() {return this.message;}

    public enum MessageType {
        CONNECT, FILE, MESSAGE
    }
}
