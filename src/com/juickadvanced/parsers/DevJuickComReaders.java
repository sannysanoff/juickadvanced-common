package com.juickadvanced.parsers;

import com.juickadvanced.data.juick.JuickUser;
import com.juickadvanced.lang.Matcher;
import com.juickadvanced.lang.Pattern;
import com.juickadvanced.lang.StringSplitter;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: san
 * Date: 11/29/12
 * Time: 2:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class DevJuickComReaders {

    static Pattern userPattern = Pattern.compile("<a href=\"/(.+?)/\"><img src=\"//i.juick.com/as/(\\d+).png\"/>");

    public ArrayList<JuickUser> parseReaders(String html) {
        int ixStart = html.indexOf("table class=\"users\"");
        if (ixStart == -1) return null;
        html = html.substring(ixStart);
        int ixEnd = html.indexOf("</table>");
        if (ixEnd == -1) return null;
        html = html.substring(0, ixEnd);
        String[] lines = StringSplitter.split(html, "</td>");
        ArrayList<JuickUser> retval = new ArrayList<JuickUser>();
        for (String line : lines) {
            Matcher matcher = userPattern.matcher(line);
            if (matcher.find()) {
                JuickUser juickUser = new JuickUser();
                juickUser.UName = matcher.group(1);
                juickUser.UID = Integer.parseInt(matcher.group(2));
                retval.add(juickUser);
            }
        }
        return retval;
    }
}
