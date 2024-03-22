package com.example.recipeapp;

/**
 * @author Ryan McFadden
 */
public class MessageItemObject {
    private String message;
    private String otherUser;
    private String date;
    private String timestamp;
    private int senderId;


    public MessageItemObject(String message, String otherUser, String date, String timestamp, int senderId) {
        this.message = message;
        this.otherUser = otherUser;
        this.date = date;
        this.timestamp = timestamp;
        this.senderId = senderId;
    }

    public String getMessage() { return message; }

    public String getOtherUser() { return otherUser; }

    public String getDate() { return date; }

    public String getTimestamp() { return timestamp; }

    public int getSenderId(){ return senderId; }
}
