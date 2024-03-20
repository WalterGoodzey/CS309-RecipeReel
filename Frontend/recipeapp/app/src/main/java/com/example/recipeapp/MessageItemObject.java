package com.example.recipeapp;

/**
 * @author Ryan McFadden
 */
public class MessageItemObject {
    private String message;
    private String sender;
    private String recipient;
    private String timestamp;
    private int senderId;


    public MessageItemObject(String message, String sender, String recipient, String timestamp, int senderId) {
        this.message = message;
        this.sender = sender;
        this.recipient = recipient;
        this.timestamp = timestamp;
        this.senderId = senderId;
    }

    public String getMessage() { return message; }

    public String getSender() { return sender; }

    public String getRecipient() {
        return recipient;
    }

    public String getTimestamp() { return timestamp; }

    public int getSenderId(){ return senderId; }
}
