package com.juickadvanced.parsers;

import com.juickadvanced.data.juick.JuickMessage;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: san
 * Date: 11/29/12
 * Time: 2:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class DevJuickComThread {

    static Pattern userNamePattern = Pattern.compile("<div class=\"msg-avatar\"><a href=\"/(.*?)/\"><img");

    String userName;

    public void parseWebPage(String htmlStr) {
        Matcher userNameMatcher = userNamePattern.matcher(htmlStr);
        if (userNameMatcher.find()) {
            userName = userNameMatcher.group(1);
        }
        return;
    }

}
