package com.juickadvanced.xmpp.commands;

import com.juickadvanced.xmpp.XMPPConnectionSetup;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: san
 * Date: 11/16/12
 * Time: 3:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class Poll implements Serializable {

    long since;

    public Poll() {
    }

    public Poll(long since) {
        this.since = since;
    }

    public long getSince() {
        return since;
    }
}
