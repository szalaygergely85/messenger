package com.example.messenger;

import java.io.Serializable;

public class MessageHead implements Serializable {
    private String Text;
    private String Sender;
    private String UserID;


    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }


    public MessageHead(String text, String sender) {
        Text = text;
        Sender = sender;
    }


    public MessageHead(MessageHead M) {
        Text = M.Text;
        Sender = M.Sender;
    }

    public MessageHead() {
    }

    public MessageHead(String text, String sender, String userID) {
        Text = text;
        Sender = sender;
        UserID = userID;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }


}
