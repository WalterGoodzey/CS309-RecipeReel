package com.example.recipeapp;

/**
 * @author Ryan McFadden
 */
public class MessageItemObject {
    private String message;
    private String date;
    private String timestamp;
    private int senderId;
    private Boolean sendingMessage;

    public MessageItemObject(String message, String date, String timestamp, int senderId, Boolean sendingMessage) {
        this.message = message;
        this.date = date;
        this.timestamp = timestamp;
        this.senderId = senderId;
        this.sendingMessage = sendingMessage;
    }

    public String getMessage() { return message; }

    public String getDate() { return date; }

    public String getTimestamp() { return timestamp; }

    public int getSenderId(){ return senderId; }
    public Boolean getSendingMessage(){ return sendingMessage; }
}
