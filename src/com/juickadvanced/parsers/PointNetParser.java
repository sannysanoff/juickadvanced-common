package com.juickadvanced.parsers;

import com.juickadvanced.data.juick.JuickMessage;
import com.juickadvanced.data.juick.JuickUser;
import com.juickadvanced.data.point.PointMessage;
import com.juickadvanced.data.point.PointMessageID;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PointNetParser {

    public static String unwebMessageTextPoint(String text) {
        text = text.replace("<br/>","\n");
        text = text.replace("<br />","\n");
        text = text.replace("<b>","*");
        text = text.replace("</b>","*");
        text = text.replace("<p>","");
        text = text.replace("</p>","\n");
        text = text.replace("<x>","");
        text = text.replace("</x>","");
        text = text.replace("&gt;",">");
        text = text.replace("&lt;","<");
        text = text.replace("&laquo;","«");
        text = text.replace("&quot;","\"");
        text = text.replace("&copy;","©");
        text = text.replace("&raquo;","»");
        text = text.replace("&mdash;","-");
        text = text.replace("&nbsp;"," ");
        text = text.replace("<div class=\"text-content\">","");
        text = text.replace("<div class=\"text\">","");
        text = text.replace("</div>","");
        text = text.replace(" \n","\n");
        while(text.startsWith("\n") || text.startsWith(" ")) {
            text = text.substring(1);
        }
        String[] texts = text.split("\n");
        StringBuilder sb = new StringBuilder();
        for (String s : texts) {
            sb.append(s.trim());    // trim all
            sb.append("\n");
        }
        while (sb.length() > 0 && sb.charAt(sb.length()-1) == '\n')
            sb.setLength(sb.length()-1);    // remove last \n
        return sb.toString();
    }


    public ArrayList<JuickMessage> parseWebMessageListPure(String htmlStr) {
        ArrayList<JuickMessage> retval = new ArrayList<JuickMessage>();

        Document parsed = Jsoup.parse(htmlStr);
        Elements posts = parsed.select("div");
        for (Element post : posts) {
            String postClass = post.attr("class");
            if (postClass.equals("post") || postClass.startsWith("post ")) {
                PointMessage message = new PointMessage();
                message.User = new JuickUser();
                message.User.UName = post.select("div[class=info] > a > img").attr("alt");
                if (message.User.UName.length() == 0) {
                    message.User.UName = post.select("div[class=author] > a").text();
                }
                String dataId = post.attr("data-id");
                String dataCommentId = post.attr("data-comment-id");
                String dataToCommentId = post.attr("data-to-comment-id");
                message.setMID(new PointMessageID(message.User.UName, dataId));
                if (dataCommentId.length() > 0) {
                    message.setRID(Integer.parseInt(dataCommentId));
                }
                if (dataToCommentId.length() > 0) {
                    message.setReplyTo(Integer.parseInt(dataToCommentId));
                }
                message.tags = new Vector<String>();
                for(Element el : post.select("a[class=tag]")) {
                    message.tags.add(el.text());
                }
                message.microBlogCode = PointMessageID.CODE;
                StringBuilder dt = new StringBuilder();
                for(Element el : post.select("div[class=created]")) {
                    dt.append(" ");
                    dt.append(el.text());
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy dd MMM HH:mm");
                try {
                    message.Timestamp = sdf.parse("1976 "+dt.toString().trim());
                } catch (ParseException e) {
                    continue;
                }
                Elements postEls = post.select("div[class=text-content]");
                if (postEls.size() < 1) {
                    postEls = post.select("div[class=text]");
                }
                message.csrf_token = post.select("input[name=csrf_token]").attr("value");

                // last part
                if (postEls.size() < 1) {
                    message.Text = "Error parsing text ;-(";
                } else {
                    Element elem = cleanupElement(postEls.get(0));
                    postEls.get(0).appendChild(elem);   // add to document
                    Document.OutputSettings os = elem.ownerDocument().outputSettings();
                    os.prettyPrint(false);
                    String text = elem.toString().replace("\n"," ");
                    text = text.replace("&amp;","&");       // this was improperly done in cleanupElement
                    while (true) {
                        long olds = text.length();
                        text = text.replace("  "," ");
                        long news = text.length();
                        if (news == olds) break;
                    }
                    try {
                        message.replies = Integer.parseInt(post.select("span[class=cn]").text());
                    } catch(Exception ex){}
                    message.Text = unwebMessageTextPoint(text);
                }
                retval.add(message);
            }
        }


        return retval;
    }

    private Element cleanupElement(Element el) {
        Tag newTag = null;
        String newText = null;
        if (el.nodeName().equals("img")) {
            newTag = Tag.valueOf("x");
            newText = el.attr("src");
        }
        if (el.nodeName().equals("em")) {
            newTag = Tag.valueOf("b");
        }
        if (el.nodeName().equals("a")) {
            String clazz = el.attr("class");
            if (clazz.equals("user")) {
                newTag = Tag.valueOf("x");
                newText = "@" + el.text().trim();
            } else
            if (clazz.startsWith("postimg video")) {
                newTag = Tag.valueOf("x");
                newText = "VIDEO: "+el.attr("href")+" THUMBNAIL: "+el.select("img").attr("src");
            } else
            if (clazz.startsWith("postimg")) {
                newTag = Tag.valueOf("x");
            } else
            if (clazz.equals("post")) {
                newTag = Tag.valueOf("x");
            } else {
                newTag = Tag.valueOf("x");
                newText = el.attr("href");
            }
        }
        if (el.nodeName().equals("div")) {
            newTag = Tag.valueOf("x");
        }
        Element nel;
        if (newTag == null) {
            // el = el;
            nel = el.clone();
            for(List<Node> children = nel.childNodes(); children.size() > 0; children = nel.childNodes()) {
                children.get(0).remove();
            }
        } else {
            nel = new Element(newTag, "");
        }
        if (newText != null) {
            nel.appendChild(new TextNode(newText, ""));
        } else {
            List<Node> children = el.childNodes();
            for (Node child : children) {
                if (child instanceof Element) {
                    nel.appendChild(cleanupElement((Element)child));
                } else {
                    nel.appendChild(new TextNode(child.toString(), ""));
                }
            }
        }
        return nel;
    }

}
