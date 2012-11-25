package com.juickadvanced.xmpp.messages;

/**
* Created with IntelliJ IDEA.
* User: san
* Date: 11/16/12
* Time: 3:25 PM
* To change this template use File | Settings | File Templates.
*/
public class ContactOffline {
    String jid;

    public ContactOffline(String jid) {
        this.jid = jid;
    }

    public String getJid() {
        return jid;
    }
}
