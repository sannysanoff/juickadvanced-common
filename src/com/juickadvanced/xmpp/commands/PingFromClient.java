package com.juickadvanced.xmpp.commands;

/**
 * Created with IntelliJ IDEA.
 * User: san
 * Date: 12/12/12
 * Time: 2:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class PingFromClient {
    private long ts;

    public PingFromClient() {
    }

    public PingFromClient(long ts) {
        this.ts = ts;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }
}
