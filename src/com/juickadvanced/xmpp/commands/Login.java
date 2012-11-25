package com.juickadvanced.xmpp.commands;

import com.juickadvanced.xmpp.XMPPConnectionSetup;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: san
 * Date: 11/16/12
 * Time: 3:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class Login implements Serializable {

    XMPPConnectionSetup connectionSetup;
    String gcmId;
    HashSet<String> watchedJids;

    public Login(XMPPConnectionSetup connectionSetup, HashSet<String> watchedJids, String gcmId) {
        this.connectionSetup = connectionSetup;
        this.gcmId = gcmId;
        this.watchedJids = watchedJids;
    }

    public XMPPConnectionSetup getConnectionSetup() {
        return connectionSetup;
    }

    public String getGcmId() {
        return gcmId;
    }

    public void setGcmId(String gcmId) {
        this.gcmId = gcmId;
    }

    public HashSet<String> getWatchedJids() {
        return watchedJids;
    }
}
