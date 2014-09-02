package com.juickadvanced.xmpp.commands;

import com.juickadvanced.data.juick.JuickMessageID;

/**
 * Created by san on 8/14/14.
 */
public class MicroblogGommand {
    String microblogCode;

    public MicroblogGommand(String microblogCode) {
        this.microblogCode = microblogCode;
    }

    public String getMicroblogCode() {
        if (microblogCode == null)
            return JuickMessageID.CODE;
        return microblogCode;
    }
}
