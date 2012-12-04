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
    String proofAccountId;
    String proofAccountToken;
    String proofAccountType;

    public Login(XMPPConnectionSetup connectionSetup, HashSet<String> watchedJids, String gcmId) {
        this.connectionSetup = connectionSetup;
        this.gcmId = gcmId;
        this.watchedJids = watchedJids;
    }

    public String getProofAccountId() {
        return proofAccountId;
    }

    public void setProofAccountId(String proofAccountId) {
        this.proofAccountId = proofAccountId;
    }

    public String getProofAccountToken() {
        return proofAccountToken;
    }

    public void setProofAccountToken(String proofAccountToken) {
        this.proofAccountToken = proofAccountToken;
    }

    public String getProofAccountType() {
        return proofAccountType;
    }

    public void setProofAccountType(String proofAccountType) {
        this.proofAccountType = proofAccountType;
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
