package com.example.messenger;

public class MessageHead {

    private String Text;

    public MessageHead(String text, String sender) {
        Text = text;
        Sender = sender;
    }

    private String Sender;

    public MessageHead(MessageHead M) {
        Text = M.Text;
        Sender = M.Sender;
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
