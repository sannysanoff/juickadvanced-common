package com.juickadvanced.xmpp.messages;

/**
 * Created with IntelliJ IDEA.
 * User: san
 * Date: 12/12/12
 * Time: 2:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewInfoNotification {

    String requestId;

    public NewInfoNotification(String requestId) {
        this.requestId = requestId;
    }

    public NewInfoNotification() {
    }

    public String getRequestId() {
        return requestId;
    }
}
