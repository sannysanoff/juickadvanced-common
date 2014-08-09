package com.juickadvanced.parsers;

import com.juickadvanced.Utils;
import com.juickadvanced.data.juick.JuickMessage;
import com.juickadvanced.data.juick.JuickMessageID;
import com.juickadvanced.data.juick.JuickUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.Vector;

import com.juickadvanced.lang.Matcher;
import com.juickadvanced.lang.Pattern;
import com.juickadvanced.lang.StringSplitter;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created with IntelliJ IDEA.
 * User: san
 * Date: 11/29/12
 * Time: 1:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class DevJuickComMessages {

    static Pattern hyperlink = Pattern.compile("<a (.*?)href=\"(.*?)\"(.*?)>(.*?)</a>");
    static Pattern juick = Pattern.compile("http://juick.com/(\\d+)");
    static Pattern juick2 = Pattern.compile("http://juick.com/(\\w+)/(\\d+)");
    static Pattern hashNo = Pattern.compile("#(\\d*)");
    static Pattern blockQuote = Pattern.compile("<blockquote>(.*?)</blockquote>");
    static Pattern italic = Pattern.compile("<i>(.*?)</i>");
    static Pattern bold = Pattern.compile("<b>(.*?)</b>");
    static Pattern underline = Pattern.compile("<u>(.*?)</u>");

    public interface SDFTZ {
        void initSDFTZ(SimpleDateFormat sdf, String tz);
        SimpleDateFormat createSDF(String format, String l1, String l2);
    }

    public static SDFTZ sdftz;

    enum State {
        WAIT_MESSAGE_START,
        WAIT_MSG_TEXT,
        IN_MESSAGE_TEXT
    }

    public static ArrayList<JuickMessage> parseWebPage(String htmlStr) {
        ArrayList<JuickMessage> retval = new ArrayList<JuickMessage>();
        Document parsed = Jsoup.parse(htmlStr);
        Elements elems = parsed.select("article");
        if (elems != null) {
            for (Element article : elems) {
                String mid = article.attr("data-mid");
                if (mid.length() == 0) continue;
                JuickMessage msg = new JuickMessage();
                msg.setMID(new JuickMessageID(Integer.parseInt(mid)));
                Elements t = article.select("header.t");
                Elements userpic = article.select("aside > a > img");
                String[] userpicArr = StringSplitter.split(userpic.attr("src"), "[/\\.]");
                Elements link = t.select("a");
                Elements time = t.select("time");
                msg.User = new JuickUser();
                msg.User.UName = StringSplitter.split(link.attr("href"),"/")[1];
                msg.User.UID = Integer.parseInt(userpicArr[6]);
                msg.microBlogCode = JuickMessageID.CODE;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    if (sdftz != null) {
                        sdftz.initSDFTZ(sdf, "GMT");
                    }
                    msg.Timestamp = sdf.parse(time.attr("datetime"));
                } catch (ParseException e) {
                    continue;
                }
                msg.tags = new Vector<String>();
                Elements tags = article.select("header.u > a");
                for(int i=1; i<tags.size(); i++) {
                    msg.tags.add(tags.get(i).text());
                }
                Elements text = article.select("p");
                StringBuilder sb = new StringBuilder();
                for (Element element : text) {
                    sb.append(element.toString());
                }
                msg.Text = unwebMessageTextJuickWeb(sb.toString());
                System.out.println("ok");
                retval.add(msg);
            }
        }
        return retval;
    }

    public static String unwebMessageTextJuickWeb(String text) {
        text = Utils.replace(text, "<br/>","\n");
        text = Utils.replace(text, "<br />","\n");
        text = Utils.replace(text, "</div>","");
        text = Utils.replace(text, "</p>","");
        text = Utils.replace(text, "<p>","");
        while(true) {
            Matcher matcher = hyperlink.matcher(text);
            if (matcher.find()) {
                try {
                    text = matcher.replaceFirst(matcher.group(2));
                } catch (Exception e) {
                    // bugs in regexp?
                    break;
                }
                continue;
            } else {
                break;
            }
        }
        text = unjuick(text);
        while(true) {
            Matcher matcher = blockQuote.matcher(text);
            if (matcher.find()) {
                try {
                    text = matcher.replaceFirst("> " + matcher.group(1));
                } catch (Exception e) {
                    // bugs in regexp?
                    break;
                }
                continue;
            } else {
                break;
            }
        }
        while(true) {
            Matcher matcher = bold.matcher(text);
            if (matcher.find()) {
                try {
                    text = matcher.replaceFirst("*" + matcher.group(1)+"*");
                } catch (Exception e) {
                    // bugs in regexp?
                    break;
                }
                continue;
            } else {
                break;
            }
        }
        while(true) {
            Matcher matcher = italic.matcher(text);
            if (matcher.find()) {
                try {
                    text = matcher.replaceFirst("/" + matcher.group(1)+"/");
                } catch (Exception e) {
                    // bugs in regexp?
                    break;
                }
                continue;
            } else {
                break;
            }
        }
        while(true) {
            Matcher matcher = underline.matcher(text);
            if (matcher.find()) {
                try {
                    text = matcher.replaceFirst("_" + matcher.group(1)+"_");
                } catch (Exception e) {
                    // bugs in regexp?
                    break;
                }
                continue;
            } else {
                break;
            }
        }
        text = Utils.replace(text, "&gt;",">");
        text = Utils.replace(text, "&lt;","<");
        text = Utils.replace(text, "&quot;","\"");
        text = Utils.replace(text, "&laquo;","«");
        text = Utils.replace(text, "&raquo;","»");
        text = Utils.replace(text, "&mdash;","–");
        return text;
    }

    public static String unjuick(String text) {
        while(true) {
            Matcher matcher = juick.matcher(text);
            if (matcher.find()) {
                int end = matcher.end(1);
                Matcher hash = hashNo.matcher(text.substring(end));
                if (hash.find() && hash.start() == 0) {
                    text = text.substring(0, end) + hash.replaceFirst("/"+hash.group(1));
                    continue;
                }
                text = matcher.replaceFirst("#"+matcher.group(1));
                continue;
            } else {
                break;
            }
        }
        while(true) {
            Matcher matcher = juick2.matcher(text);
            if (matcher.find()) {
                int end = matcher.end(2);
                Matcher hash = hashNo.matcher(text.substring(end));
                if (hash.find() && hash.start() == 0) {
                    text = text.substring(0, end) + hash.replaceFirst("/"+hash.group(1));
                    continue;
                }
                text = matcher.replaceFirst("#"+matcher.group(2));
                continue;
            } else {
                break;
            }
        }
        return text;
    }

}
