package com.juickadvanced.serializers;

/**
 * Created with IntelliJ IDEA.
 * User: san
 * Date: 11/29/12
 * Time: 8:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class JuickPlainTextMessages {

    public static String createReplyMessagePlainText(Object mid, int rid, String author, String body) {
        StringBuilder sb = new StringBuilder();
        sb.append("Reply by @"+author+":\n");
        sb.append("> ...\n\n");
        sb.append(body);
        sb.append("\n");
        sb.append("#"+mid+"/"+rid);
        sb.append(" ");
        sb.append("http://juick.com/"+mid+"#"+rid+"\n");
        return sb.toString();
    }

    public static String createMessagePlainText(Object mid, String userName, String body) {
        StringBuilder sb = new StringBuilder();
        sb.append("@"+userName+":\n");
        sb.append(body);
        sb.append("\n");
        sb.append("#"+mid);
        sb.append(" ");
        sb.append("http://juick.com/"+mid);
        return sb.toString();
    }
}
