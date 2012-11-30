package com.juickadvanced.xmpp.commands;

/**
 * Created with IntelliJ IDEA.
 * User: san
 * Date: 11/16/12
 * Time: 10:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class SendGCMRegistration {
    String gcm_regid;

    public SendGCMRegistration(String gcm_regid) {
        this.gcm_regid = gcm_regid;
    }

    public String getGcm_regid() {
        return gcm_regid;
    }
}
