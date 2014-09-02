package com.juickadvanced.data.facebook;

import com.juickadvanced.data.MessageID;

/**
 * Created with IntelliJ IDEA.
 * User: san
 * Date: 11/10/12
 * Time: 1:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class FacebookMessageID extends MessageID {

    public static final String CODE = "facebook";
    public String fbid;
    public String feedbackId;

    public FacebookMessageID(String fbid) {
        this.fbid = fbid;
    }

    @Override
    public String toString() {
        return "fb-"+ fbid;
    }

    @Override
    public String toDisplayString() {
        if (fbid == null) {
            return "";
        }
        return "#"+ fbid;
    }

    @Override
    public String getMicroBlogCode() {
        return CODE;
    }

    public static FacebookMessageID fromString(String str) {
        if (str.startsWith("fb-")) {
            return new FacebookMessageID(str.substring(3));
        }
        return null;
    }


}
