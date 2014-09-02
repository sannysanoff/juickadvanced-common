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
public class AccountProof implements Serializable {

    String proofAccountId;
    String proofAccountToken;
    String proofAccountType;

    public AccountProof(String proofAccountId, String proofAccountToken, String proofAccountType) {
        this.proofAccountId = proofAccountId;
        this.proofAccountToken = proofAccountToken;
        this.proofAccountType = proofAccountType;
    }

    public AccountProof() {
    }

    public String getProofAccountId() {
        return proofAccountId;
    }

    public String getProofAccountToken() {
        return proofAccountToken;
    }

    public String getProofAccountType() {
        return proofAccountType;
    }

    public String getFullString() {
        return proofAccountId+"@"+getProofAccountType();
    }
}
