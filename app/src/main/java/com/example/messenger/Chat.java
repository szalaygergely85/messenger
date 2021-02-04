package com.example.messenger;

public class Chat {
    private String Text;
    private String Sender;
    private String Time;


    public Chat(String text, String sender, String time) {
        Text = text;
        Sender = sender;
        Time = time;
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

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }


}
