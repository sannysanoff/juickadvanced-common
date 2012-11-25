package com.juickadvanced.xmpp.commands;

/**
 * Created with IntelliJ IDEA.
 * User: san
 * Date: 11/16/12
 * Time: 10:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class SendMessage {
    String toJid;
    String message;

    public SendMessage(String message, String toJid) {
        this.message = message;
        this.toJid = toJid;
    }

    public String getMessage() {
        return message;
    }

    public String getToJid() {
        return toJid;
    }
}
