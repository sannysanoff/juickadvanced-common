package com.juickadvanced.serializers;

import com.juickadvanced.data.MessageID;
import com.juickadvanced.data.juick.JuickMessageID;
import com.juickadvanced.data.point.PointMessageID;

/**
 * Created with IntelliJ IDEA.
 * User: san
 * Date: 11/29/12
 * Time: 8:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class JuickPlainTextMessages {

    public static String createReplyMessagePlainText(MessageID mid, int rid, String author, String body) {
        StringBuilder sb = new StringBuilder();
        sb.append("Reply by @"+author+":\n");
        sb.append("> ...\n\n");
        sb.append(body);
        sb.append("\n");
        sb.append(mid.toDisplayString()+"/"+rid);
        sb.append(" ");
        sb.append(getLinkToMessage(mid, rid));
        return sb.toString();
    }

    private static String getLinkToMessage(MessageID mid, int rid) {
        if (mid == null) {
            throw new IllegalArgumentException("getLinkToMessage: invalid messageid: "+mid);
        }
        if (mid instanceof JuickMessageID) {
            return "http://juick.com/"+((JuickMessageID) mid).getMid()+(rid != 0 ? "#"+rid : "") +"\n";
        }
        if (mid instanceof PointMessageID) {
            return "http://point.im/"+((PointMessageID) mid).getId()+(rid != 0 ? "#"+rid : "")+"\n";
        }
        throw new IllegalArgumentException("getLinkToMessage: invalid messageid: "+mid.getClass().getName());
    }

    public static String createMessagePlainText(MessageID mid, String userName, String body, String[] tagsS) {
        StringBuilder sb = new StringBuilder();
        sb.append("@"+userName+":");
        for(int i=0; i<tagsS.length; i++) {
            sb.append(" *"+tagsS[i]);
        }
        sb.append("\n");
        sb.append(body);
        sb.append("\n");
        sb.append(mid.toDisplayString());
        sb.append(" ");
        sb.append(getLinkToMessage(mid, 0));
        return sb.toString();
    }
}
