package com.juickadvanced.parsers;

import com.juickadvanced.Utils;
import com.juickadvanced.data.MessageID;
import com.juickadvanced.data.juick.JuickMessage;
import com.juickadvanced.data.juick.JuickUser;
import com.juickadvanced.data.point.PointMessage;
import com.juickadvanced.data.point.PointMessageID;
import com.juickadvanced.data.point.PointUser;
import com.juickadvanced.lang.StringSplitter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
        text = Utils.replace(text,"<br/>","\n");
        text = Utils.replace(text,"<br />","\n");
        text = Utils.replace(text,"<b>","*");
        text = Utils.replace(text,"</b>","*");
        text = Utils.replace(text,"<p>","");
        text = Utils.replace(text,"</p>","\n");
        text = Utils.replace(text,"<x>","");
        text = Utils.replace(text,"</x>","");
        text = Utils.replace(text,"&gt;",">");
        text = Utils.replace(text,"&lt;","<");
        text = Utils.replace(text,"&laquo;","«");
        text = Utils.replace(text,"&quot;","\"");
        text = Utils.replace(text,"&copy;","©");
        text = Utils.replace(text,"&raquo;","»");
        text = Utils.replace(text,"&mdash;","-");
        text = Utils.replace(text,"&nbsp;"," ");
        text = Utils.replace(text,"<div class=\"text-content\">","");
        text = Utils.replace(text,"<div class=\"text\">","");
        text = Utils.replace(text,"</div>","");
        text = Utils.replace(text," \n","\n");
        while(text.startsWith("\n") || text.startsWith(" ")) {
            text = text.substring(1);
        }
        String[] texts = StringSplitter.split(text, "\n");
        StringBuilder sb = new StringBuilder();
        for (String s : texts) {
            sb.append(s.trim());    // trim all
            sb.append("\n");
        }
        while (sb.length() > 0 && sb.charAt(sb.length()-1) == '\n')
            sb.setLength(sb.length()-1);    // remove last \n
        return sb.toString();
    }


    public ArrayList<JuickMessage> parseAPIPostAndReplies(String jsonStr) {
        ArrayList<JuickMessage> retval = new ArrayList<JuickMessage>();
        try {
            JSONObject jo = new JSONObject(jsonStr);
            JSONObject post = jo.getJSONObject("post");
            PointMessage msg = new PointMessage();
            parsePointAPIMessagePost(msg, post);
            retval.add(msg);
            JSONArray comments = jo.getJSONArray("comments");
            for(int i=0; i<comments.length(); i++) {
                JSONObject comment = comments.getJSONObject(i);
                PointMessage comm = new PointMessage();
                try {
                    parsePointAPIComment(comment, comm, msg);
                    retval.add(comm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return retval;
    }

    public ArrayList<JuickMessage> parseAPIMessageListPure(String jsonStr) {
        ArrayList<JuickMessage> retval = new ArrayList<JuickMessage>();
        try {
            JSONObject jo = new JSONObject(jsonStr);
            JSONArray posts = jo.getJSONArray("posts");
            for(int i=0; i<posts.length(); i++) {
                JSONObject post = posts.getJSONObject(i);
                PointMessage msg = new PointMessage();
                try {
                    parsePointAPIMessage(post, msg);
                    retval.add(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return retval;
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    SimpleDateFormat sdfTz = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZZZZZ");

    private void parsePointAPIMessage(JSONObject post, PointMessage msg) throws JSONException, ParseException {
        JSONObject p = post.getJSONObject("post");
        parsePointAPIMessagePost(msg, p);
        PointMessageID mid = (PointMessageID)msg.getMID();
        mid.uid = post.getInt("uid");
        msg.source = post.toString();
        msg.subscribed = post.getBoolean("subscribed");
    }

    public void parsePointAPIMessagePost(PointMessage msg, JSONObject p) throws JSONException, ParseException {
        PointUser pu = new PointUser();
        msg.User = pu;
        JSONObject author = p.getJSONObject("author");
        pu.UName = author.getString("login");
        pu.FullName = author.getString("name");
        pu.UID = author.getInt("id");
        if (author.has("avatar") && author.get("avatar") != JSONObject.NULL)
            pu.avatarUrl = author.getString("avatar");
        msg.Text = p.getString("text");
        String createdStr = p.getString("created");
        msg.Timestamp = parsePointAPIDate(createdStr);
        JSONArray tagso = p.getJSONArray("tags");
        msg.tags = new Vector<String>();
        for(int i=0; i<tagso.length(); i++) {
            msg.tags.add(tagso.getString(i));
        }
        msg.setMID(new PointMessageID(pu.UName, p.getString("id"), 0));
        msg.replies = p.getInt("comments_count");
        msg.microBlogCode = PointMessageID.CODE;
    }

    private Date parsePointAPIDate(String createdStr) throws ParseException {
        if (createdStr.indexOf("+") >= 0) {
            return sdfTz.parse(createdStr);
        } else {
            return sdf.parse(createdStr);
        }
    }

    private void parsePointAPIComment(JSONObject comm, PointMessage msg, PointMessage parent) throws JSONException, ParseException {
        PointUser pu = new PointUser();
        msg.User = pu;
        JSONObject author = comm.getJSONObject("author");
        pu.UName = author.getString("login");
        pu.FullName = author.getString("name");
        pu.UID = author.getInt("id");
        if (author.has("avatar") && author.get("avatar") != JSONObject.NULL) {
            pu.avatarUrl = author.getString("avatar");
        }
        msg.Text = comm.getString("text");
        msg.is_rec = comm.getBoolean("is_rec");
        msg.Timestamp = parsePointAPIDate(comm.getString("created"));
        msg.setMID(parent.getMID());
        msg.setRID(comm.getInt("id"));
        Object toCommentId = comm.get("to_comment_id");
        if (toCommentId != JSONObject.NULL) {
            msg.setReplyTo(comm.getInt("to_comment_id"));
        }
        msg.microBlogCode = PointMessageID.CODE;
    }

    public ArrayList<JuickMessage> parseWebMessageListPure(String htmlStr) {
        ArrayList<JuickMessage> retval = new ArrayList<JuickMessage>();

        Document parsed = Jsoup.parse(htmlStr);
        Elements posts = parsed.select("div");
        SimpleDateFormat sdf;
        SimpleDateFormat sdf2;
        if (DevJuickComMessages.sdftz != null) {
            sdf = DevJuickComMessages.sdftz.createSDF("yyyy dd MMM HH:mm", "en","US");
            sdf2 = DevJuickComMessages.sdftz.createSDF("yyyy dd MMM HH:mm", "ru","RU");
            DevJuickComMessages.sdftz.initSDFTZ(sdf2, "GMT+4");
            DevJuickComMessages.sdftz.initSDFTZ(sdf, "GMT+4");
        } else {
            sdf = new SimpleDateFormat("yyyy dd MMM HH:mm");
            sdf2 = new SimpleDateFormat("yyyy dd MMM HH:mm");
        }
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
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
                message.setMID(new PointMessageID(message.User.UName, dataId, 0));
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
                try {
                    message.Timestamp = sdf.parse(currentYear+" "+dt.toString().trim());
                } catch (ParseException e) {
                    try {
                        message.Timestamp = sdf2.parse(currentYear + " "+dt.toString().trim());
                    } catch (ParseException e1) {
                        continue;
                    }
                }
                Date mt = message.Timestamp;
                if (mt.getTime() > System.currentTimeMillis() + 50 * 24 * 60 * 60 * 1000L) {
                    Calendar cal2 = Calendar.getInstance();
                    cal2.setTime(mt);
                    cal2.set(Calendar.YEAR, cal2.get(Calendar.YEAR)-1);
                    message.Timestamp = cal2.getTime();
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
                    String text = Utils.replace(elem.toString(),"\n"," ");
                    text = Utils.replace(text,"&amp;","&");       // this was improperly done in cleanupElement
                    while (true) {
                        long olds = text.length();
                        text = Utils.replace(text,"  "," ");
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

        for (JuickMessage juickMessage : retval) {
            if (juickMessage.getRID() != 0 && juickMessage.getReplyTo() != 0 && !juickMessage.Text.startsWith("@")) {
                String uzur = null;
                for (JuickMessage scan : retval) {
                    if (scan.getRID() == juickMessage.getReplyTo()) {
                        uzur = scan.User.UName;
                        break;
                    }
                }
                if (uzur != null) {
                    juickMessage.Text = "@"+uzur+" "+juickMessage.Text;
                }
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
            nel = new Element(el.tag(), "");
//            for(List<Node> children = nel.childNodes(); children.size() > 0; children = nel.childNodes()) {
//                children.get(0).remove();
//            }
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
