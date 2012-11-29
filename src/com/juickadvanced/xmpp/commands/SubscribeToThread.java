package com.juickadvanced.xmpp.commands;

/**
 * Created with IntelliJ IDEA.
 * User: san
 * Date: 11/29/12
 * Time: 4:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class SubscribeToThread {
    int mid;
    String subscribeStatus; // [S]ubscribed/ [R]epliesonly / null

    public SubscribeToThread(int mid, String subscribeStatus) {
        this.mid = mid;
        this.subscribeStatus = subscribeStatus;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getSubscribeStatus() {
        return subscribeStatus;
    }

    public void setSubscribeStatus(String subscribeStatus) {
        this.subscribeStatus = subscribeStatus;
    }
}
