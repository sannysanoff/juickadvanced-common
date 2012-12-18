package com.juickadvanced.data.bnw;

import com.juickadvanced.data.juick.JuickMessage;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: san
 * Date: 11/9/12
 * Time: 1:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class BNWMessage extends JuickMessage {

    private String RIDString;
    public ArrayList<String> clubs = new ArrayList<String>();
    private String replyToString;

    public BNWMessage() {
    }


    public void setRIDString(String RIDString) {
        this.RIDString = RIDString;
    }

    public String getRIDString() {
        return RIDString;
    }

    public void setReplyToString(String replyToString) {
        this.replyToString = replyToString;
    }

    public String getReplyToString() {
        return replyToString;
    }

    @Override
    protected String webLinkToMessage(String msg) {
        msg += "http://bnw.im/p/"+((BnwMessageID)getMID()).getId();
        return msg;
    }
}
