package com.juickadvanced.xmpp.messages;

/**
* Created with IntelliJ IDEA.
* User: san
* Date: 11/17/12
* Time: 8:41 PM
* To change this template use File | Settings | File Templates.
*/
public class TimestampedMessage {
    long timestamp;
    String message;
    String from;

    public TimestampedMessage(String message, String from, long timestamp) {
        this.message = message;
        this.from = from;
        this.timestamp = timestamp;
    }

    public String getFrom() {
        return from;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
