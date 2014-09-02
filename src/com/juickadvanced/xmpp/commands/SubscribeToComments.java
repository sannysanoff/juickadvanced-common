package com.juickadvanced.xmpp.commands;

/**
 * Created with IntelliJ IDEA.
 * User: san
 * Date: 11/29/12
 * Time: 4:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class SubscribeToComments extends MicroblogGommand {
    String commenter;
    String subscribeStatus; // [S]ubscribed/ / null

    public SubscribeToComments(String commenter, String subscribeStatus, String microblogCode) {
        super(microblogCode);
        this.commenter = commenter;
        this.subscribeStatus = subscribeStatus;
    }

    public String getCommenter() {
        return commenter;
    }

    public String getSubscribeStatus() {
        return subscribeStatus;
    }
}
