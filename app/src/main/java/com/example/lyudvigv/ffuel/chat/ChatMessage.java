package com.example.lyudvigv.ffuel.chat;

/**
 * Created by LyudvigV on 9/1/2017.
 */

public class ChatMessage {
    public String body;

    public String msgid;
    public boolean isMine;// Did I send the message.

    public ChatMessage(String messageString,
                        boolean isMine) {
        this.body = messageString;
        this.isMine = isMine;
    }

/*    public void setMsgID() {
        msgid += "-" + String.format("%02d", new Random().nextInt(100));
    }*/
}
