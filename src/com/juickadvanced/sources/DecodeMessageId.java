package com.juickadvanced.sources;

import com.juickadvanced.data.MessageID;
import com.juickadvanced.data.bnw.BnwMessageID;
import com.juickadvanced.data.facebook.FacebookMessageID;
import com.juickadvanced.data.juick.JuickMessageID;
import com.juickadvanced.data.point.PointMessageID;

/**
 * Created by san on 8/14/14.
 */
public class DecodeMessageId {

    public static MessageID fromString(String str) {
        MessageID messageID = PointMessageID.fromString(str);
        if (messageID == null)
            messageID = BnwMessageID.fromString(str);
        if (messageID == null)
            messageID = FacebookMessageID.fromString(str);
        if (messageID == null)
            messageID = JuickMessageID.fromString(str);
        return messageID;
    }

}
