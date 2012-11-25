package com.juickadvanced.xmpp;

import com.juickadvanced.xmpp.commands.*;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: san
 * Date: 11/16/12
 * Time: 3:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientToServer implements Serializable {

    String sessionId;

    Login login;
    SendMessage sendMessage;
    Poll poll;
    ConfirmPoll confirmPoll;
    Disconnect disconnect;

    public ClientToServer(String sessionId) {
        this.sessionId = sessionId;
    }

    public Login getLogin() {
        return login;
    }

    public SendMessage getSendMessage() {
        return sendMessage;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public void setSendMessage(SendMessage sendMessage) {
        this.sendMessage = sendMessage;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public ConfirmPoll getConfirmPoll() {
        return confirmPoll;
    }

    public void setConfirmPoll(ConfirmPoll confirmPoll) {
        this.confirmPoll = confirmPoll;
    }

    public Disconnect getDisconnect() {
        return disconnect;
    }

    public void setDisconnect(Disconnect disconnect) {
        this.disconnect = disconnect;
    }
}
