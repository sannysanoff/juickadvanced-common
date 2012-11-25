package com.juickadvanced.xmpp.messages;

/**
* Created with IntelliJ IDEA.
* User: san
* Date: 11/16/12
* Time: 3:24 PM
* To change this template use File | Settings | File Templates.
*/
public class ContactOnline {
    String jid;

    public ContactOnline(String jid) {
        this.jid = jid;
    }

    public String getJid() {
        return jid;
    }
}
