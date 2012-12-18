package com.juickadvanced.parsers;

import com.juickadvanced.data.juick.JuickMessage;
import com.juickadvanced.data.juick.JuickUser;
import com.juickadvanced.data.psto.PstoMessage;
import com.juickadvanced.data.psto.PstoMessageID;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: san
 * Date: 12/14/12
 * Time: 8:16 AM
 * To change this template use File | Settings | File Templates.
 */

public class PstoNetParser {

    static Pattern messageStart = Pattern.compile("<div class=\"post\">|<div class=\"post private\">");
    static Pattern commentStart = Pattern.compile("div class=\"post\" id=\"comment-(.*?)\" ");
    static Pattern commentNumber = Pattern.compile("data-comment-id=\"(.*?)\"");
    static Pattern commentReplyto = Pattern.compile("data-to-comment-id=\"(.*?)\"");
    static Pattern messageTime = Pattern.compile("<span class=\"info\">(.*?)</span>");
    static Pattern messageUser = Pattern.compile("<a class=\"name\" href=\"(.*?)/\" title=\"(.*?)\">(.*?)</a>");
    static Pattern answer = Pattern.compile("<a class=\"answer\" href=\"#\" data-to=\"(.*)\" data-to-comment=\"(.*?)\"");
    static Pattern messageTag = Pattern.compile("<a href=\"http://psto.net/tag\\?tag=(.*?)\">(.*)</a>");
    static Pattern messageID = Pattern.compile("<div class=\"post-id\"><a href=\"(.*)\">#(.*)</a></div>");
    static Pattern nreplies = Pattern.compile("title=\"Add comment\"><img src=\"/img/reply.png\" alt=\"re\"/>(.*)</a>");
    static Pattern nrepliesRU = Pattern.compile("title=\"Добавить комментарий\"><img src=\"/img/reply.png\" alt=\"re\"/>(.*)</a>");
    static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    static {
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
    }
    public static ArrayList<JuickMessage> badRetval = new ArrayList<JuickMessage>();

    public enum ParseMode {
        PARSE_MESSAGE_LIST,
        PARSE_THREAD_FIRST,
        PARSE_THREAD_COMMENTS
    }

    public ArrayList<JuickMessage> parseWebMessageListPure(String jsonStr, ParseMode parseMode) {
        ArrayList<JuickMessage> retval = new ArrayList<JuickMessage>();
        String[] lines = jsonStr.split("\n");
        JuickMessage message = null;
        //
        // YES! I CAN USE REGEXPS to parse HTML!
        //
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            line = line.trim();
            Matcher matcher = parseMode == ParseMode.PARSE_THREAD_COMMENTS ? commentStart.matcher(line) : messageStart.matcher(line);
            if (matcher.find()) {
                if (message != null) {
                    retval.add(message);
                }
                message = new PstoMessage();
                message.User = new JuickUser();
                message.microBlogCode = PstoMessageID.CODE;
                message.privateMessage = line.contains("private");
                if (parseMode == ParseMode.PARSE_THREAD_COMMENTS) {
                    matcher = commentNumber.matcher(line);
                    if (matcher.find()) {
                        message.setRID(Integer.parseInt(matcher.group(1)));
                    }
                    matcher = commentReplyto.matcher(line);
                    if (matcher.find()) {
                        message.setReplyTo(Integer.parseInt(matcher.group(1)));
                    }
                }
                continue;
            }
            if (message == null) continue;
            if (line.contains("class=\"pager\"")) break;    // end of messages
            if (line.contains("<div class=\"comments\">")) break;    // end of messages
            Matcher timeMatcher = messageTime.matcher(line);
            if (timeMatcher.find()) {
                try {
                    String dateOrTime = timeMatcher.group(1).trim();
                    String[] split = dateOrTime.split(" ");
                    // 10.09.2012 06:53 Gajim_
                    if (split[0].length() == 5 && split[0].indexOf(':') == 2) { // time first
                        dateOrTime = split[0];
                    } else {
                        dateOrTime = split[0] + " " + split[1];    // date + time
                    }
                    if (dateOrTime.length() < 6) {      // time only
                        // 14:07
                        split = dateOrTime.split(":");
                        GregorianCalendar gregorianCalendar = new GregorianCalendar();
                        gregorianCalendar.setTimeZone(TimeZone.getTimeZone("GMT"));
                        gregorianCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(split[0]));
                        gregorianCalendar.set(Calendar.MINUTE, Integer.parseInt(split[1]));
                        message.Timestamp = gregorianCalendar.getTime();
                    } else {
                        // 09.11.2012 12:25
                        message.Timestamp = sdf.parse(dateOrTime);
                    }
                    continue;
                } catch (Exception e) {
                    return badRetval;
                }
            }
            if (line.startsWith("<p>")) {
                if (parseMode == ParseMode.PARSE_THREAD_COMMENTS) {
                    // multiple lines here
                    StringBuilder sb = new StringBuilder(line.trim());
                    while(true) {
                        i++;
                        line = lines[i];
                        sb.append(line.trim());
                        if (line.indexOf("</p>") != -1) {
                            break;
                        }
                    }
                    line = sb.toString();
                }
                if (!line.endsWith("</p>")) {
                    return badRetval;
                }
                line = line.substring(3, line.length() - 4);
                message.Text = DevJuickComMessages.unwebMessageText(line);
                if (message.privateMessage) {
                    message.Text = "[private] " +message.Text;
                }
                continue;
            }
            Matcher tagMatcher = messageTag.matcher(line);
            if (tagMatcher.find()) {
                message.tags.add(tagMatcher.group(2));
                continue;
            }
            Matcher nameMatcher = messageUser.matcher(line);
            if (nameMatcher.find()) {
                if (message.User.UName == null) {
                    message.User.UName = nameMatcher.group(3);
                    if (message.getMID() != null) {
                        ((PstoMessageID) message.getMID()).user = message.User.UName;
                    }
                }
                continue;
            }
            Matcher answerMatcher = answer.matcher(line);
            if (answerMatcher.find() && answerMatcher.group(1).length() > 0) {
                message.User.UID = Integer.parseInt(answerMatcher.group(1));
                continue;
            }
            Matcher postidMatcher = messageID.matcher(line);
            if (postidMatcher.find()) {
                PstoMessageID mid = new PstoMessageID("",postidMatcher.group(2));
                message.setMID(mid);
                if (message.User.UName != null) {
                    mid.user = message.User.UName;
                }
                continue;
            }
            Matcher nrepliesMatcher = nreplies.matcher(line);
            boolean repliesFound = nrepliesMatcher.find();
            if (!repliesFound) {
                Matcher nrepliesMatcherRU = nrepliesRU.matcher(line);
                repliesFound = nrepliesMatcherRU.find();
                if (repliesFound) {
                    nrepliesMatcher = nrepliesMatcherRU;
                }
            }
            if (repliesFound) {
                String group = nrepliesMatcher.group(1);
                String[] split = group.split(" ");
                if (split[0].equals("add") || split[0].startsWith("добавить")) {
                    // zero
                } else {
                    try {
                        message.replies = Integer.parseInt(split[0]);
                    } catch (NumberFormatException e) {
                        // bad luck
                    }
                }
                continue;
            }
        }
        if (message != null) {
            retval.add(message);
        }
        return retval;
    }


}
