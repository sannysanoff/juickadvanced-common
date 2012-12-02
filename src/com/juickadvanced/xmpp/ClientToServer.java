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
    RemoveSession removeSession;
    SendMessage sendMessage;
    Poll poll;
    ConfirmPoll confirmPoll;
    Disconnect disconnect;
    SubscribeToComments subscribeToComments;
    SendGCMRegistration sendGCMRegistration;
    SubscribeToThread subscribeToThread;
    SubscribeToAll subscribeToAll;

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

    public void setSendMessage(SendMessage sendMessage) {
        this.sendMessage = sendMessage;
    }

    public void setDisconnect(Disconnect disconnect) {
        this.disconnect = disconnect;
    }

    public void setLogin(Login login) {
        this.login = login;
    }


    public Poll getPoll() {
        return poll;
    }


    public ConfirmPoll getConfirmPoll() {
        return confirmPoll;
    }


    public Disconnect getDisconnect() {
        return disconnect;
    }

    public SubscribeToComments getSubscribeToComments() {
        return subscribeToComments;
    }

    public SubscribeToThread getSubscribeToThread() {
        return subscribeToThread;
    }

    public RemoveSession getRemoveSession() {
        return removeSession;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public void setConfirmPoll(ConfirmPoll confirmPoll) {
        this.confirmPoll = confirmPoll;
    }

    public SubscribeToAll getSubscribeToAll() {
        return subscribeToAll;
    }

    public void setSubscribeToAll(SubscribeToAll subscribeToAll) {
        this.subscribeToAll = subscribeToAll;
    }

    public SendGCMRegistration getSendGCMRegistration() {
        return sendGCMRegistration;
    }

    public void setSendGCMRegistration(SendGCMRegistration sendGCMRegistration) {
        this.sendGCMRegistration = sendGCMRegistration;
    }

    public void setSubscribeToThread(SubscribeToThread subscribeToThread) {
        this.subscribeToThread = subscribeToThread;
    }
}
