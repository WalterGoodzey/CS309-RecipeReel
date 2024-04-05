package com.example.recipeapp.objects;

/**
 * Class to model a single chat message and its data
 *
 * @author Ryan McFadden
 */
public class MessageItemObject {
    /** Body of the message */
    private String message;
    /** Date message was sent */
    private String date;
    /** Time message was sent */
    private String timestamp;
    /** username of the sender of the message */
    private String senderUsername;
    /**
     * Boolean to determine whether message was sent or received by local user
     * true = local user sent message
     * false = local user received message
     */
    private Boolean sendingMessage;

    /**
     * Constructor for a MessageItemObject
     * @param message body of message
     * @param date date the message is sent
     * @param timestamp time the message is sent
     * @param senderUsername username of the user sending the message
     * @param sendingMessage Boolean stating whether local user is sent or received this message
     */
    public MessageItemObject(String message, String date, String timestamp, String senderUsername, Boolean sendingMessage) {
        this.message = message;
        this.date = date;
        this.timestamp = timestamp;
        this.senderUsername = senderUsername;
        this.sendingMessage = sendingMessage;
    }

    /**
     * Getter for the body of MessageItemObject's message
     * @return message
     */
    public String getMessage() { return message; }
    /**
     * Getter for the date of MessageItemObject's message
     * @return date
     */
    public String getDate() { return date; }
    /**
     * Getter for the timestamp of MessageItemObject's message
     * @return timestamp
     */
    public String getTimestamp() { return timestamp; }
    /**
     * Getter for the senderUsername of the MessageItemObject
     * @return senderUsername
     */
    public String getSenderUsername(){ return senderUsername; }
    /**
     * Getter for the Boolean state of whether the object was
     * sent or received by the local user
     * true = local user sent message
     * false = local user received message
     * @return sendingMessage
     */
    public Boolean getSendingMessage(){ return sendingMessage; }
}
