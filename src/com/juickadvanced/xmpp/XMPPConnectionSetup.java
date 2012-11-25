package com.juickadvanced.xmpp;

import java.io.Serializable;

/**
* Created with IntelliJ IDEA.
* User: san
* Date: 11/16/12
* Time: 3:15 PM
* To change this template use File | Settings | File Templates.
*/

public class XMPPConnectionSetup implements Serializable {
    public String login;
    public String password;
    public String service;
    public String server;
    public String jid;
    public int port;
    public String resource;
    public int priority;
    public boolean secure;

    public XMPPConnectionSetup() {
    }

    public XMPPConnectionSetup(String jid, String password, int port, int priority, String resource, String server, boolean secure) {
        this.password = password;
        this.port = port;
        this.jid = jid;
        this.priority = priority;
        this.resource = resource;
        this.server = server;
        this.secure = secure;
    }

    public String getJid() {
        if (jid == null) {
            return nvl(login)+"@"+nvl(getService());
        }
        return jid;
    }

    private String nvl(String str) {
        if (str == null) return "";
        return str;
    }


    public String getLogin() {
        if (jid == null || jid.length() == 0) {
            return login;
        }
        int index = jid.indexOf("@");
        if (index == -1) {
            return jid;
        }
        return jid.substring(0, index);
    }

    public String getService() {
        if (jid == null || jid.length() == 0) {
            if (service == null || service.length() == 0) {
                return server;
            }
            return service;
        }
        int index = jid.indexOf("@");
        if (index == -1) {
            return server;
        }
        return jid.substring(index+1);
    }

    public String getServer() {
        if (server == null || server.length() == 0) {
            return getService();
        }
        return server;
    }
}
