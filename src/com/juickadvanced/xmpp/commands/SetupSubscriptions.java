package com.juickadvanced.xmpp.commands;

import java.util.ArrayList;

public class SetupSubscriptions {
    // tags and messages from juick 'S'
    ArrayList<String> subscriptions;

    public SetupSubscriptions(ArrayList<String> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public ArrayList<String> getSubscriptions() {
        return subscriptions;
    }
}
