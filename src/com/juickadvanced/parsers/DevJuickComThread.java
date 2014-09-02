package com.juickadvanced.parsers;

import com.juickadvanced.data.juick.JuickMessage;
import com.juickadvanced.lang.Matcher;
import com.juickadvanced.lang.Pattern;

import java.util.ArrayList;

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
