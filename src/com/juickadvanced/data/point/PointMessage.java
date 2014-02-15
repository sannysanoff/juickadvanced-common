package com.juickadvanced.data.point;

import com.juickadvanced.data.bnw.BNWMessage;
import com.juickadvanced.data.juick.JuickMessage;

/**
 * Created with IntelliJ IDEA.
 * User: san
 * Date: 11/10/12
 * Time: 1:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class PointMessage extends JuickMessage {

    public String csrf_token;
    public boolean subscribed;

    @Override
    protected String webLinkToMessage(String msg) {
        PointMessageID mid = (PointMessageID)getMID();
        msg += "http://"+mid.user+".point.im/"+mid.getId();
        return msg;
    }

}
