package com.example.messenger;

import java.io.Serializable;

public class MessageHead implements Serializable {

    private String Text;
    private String Sender;

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
