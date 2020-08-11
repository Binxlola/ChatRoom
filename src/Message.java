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
        MessageType type = MessageType.valueOf(typeAndMessage[0]);
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
            case FILE: break;
        }

        return messageToSend;
    }

    public MessageType getType() {return this.type;}
    public String getName() {return this.name;}

    public enum MessageType {
        CONNECT, FILE, MESSAGE
    }
}
