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
public class LoginMultiple implements Serializable {

    XMPPConnectionSetup connectionSetup;
    String gcmId;
    HashSet<String> watchedJids;
    HashSet<AccountProof> proofs;
    String version;

    public LoginMultiple(XMPPConnectionSetup connectionSetup, HashSet<String> watchedJids, String gcmId, String version) {
        this.connectionSetup = connectionSetup;
        this.gcmId = gcmId;
        this.watchedJids = watchedJids;
        this.version = version;
    }

    public HashSet<AccountProof> getProofs() {
        return proofs;
    }

    public void setProofs(HashSet<AccountProof> proofs) {
        this.proofs = proofs;
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

    public String getVersion() {
        return version;
    }
}
