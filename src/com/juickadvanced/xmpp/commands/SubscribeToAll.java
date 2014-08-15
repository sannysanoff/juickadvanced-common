package com.juickadvanced.xmpp.commands;

/**
 * Created with IntelliJ IDEA.
 * User: san
 * Date: 11/29/12
 * Time: 10:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class SubscribeToAll extends MicroblogGommand {
    public String status;   // [S],[U] from listening all stream

    public SubscribeToAll(String status, String microblogId) {
        super(microblogId);
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
