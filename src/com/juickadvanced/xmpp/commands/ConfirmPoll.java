package com.juickadvanced.xmpp.commands;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: san
 * Date: 11/16/12
 * Time: 3:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConfirmPoll implements Serializable {

    long polledAt;

    public ConfirmPoll(long polledAt) {
        this.polledAt = polledAt;
    }

    public long getPolledAt() {
        return polledAt;
    }
}
