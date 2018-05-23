package com.juickadvanced.parsers;

import com.juickadvanced.Utils;
import com.juickadvanced.data.juick.JuickMessage;
import com.juickadvanced.data.juick.JuickMessageID;
import com.juickadvanced.data.juick.JuickUser;
import com.juickadvanced.lang.ISimpleDateFormat;
import org.ja.json.JSONArray;
import org.ja.json.JSONException;
import org.ja.json.JSONObject;

import java.util.Date;

/**
 * Created by san on 4/14/14.
 */
public class JuickParser {

    public static JuickMessage initFromJSON(JSONObject json) throws JSONException {
        JuickMessage jmsg = new JuickMessage();
        jmsg.source = json.toString();
        final int mid = json.has("mid") ? json.getInt("mid") : json.getInt("parent_mid");   // for replies unprocessed
        jmsg.setMID(new JuickMessageID(mid));
        if (json.has("rid")) {
            jmsg.setRID(json.getInt("rid"));
        }
        if (json.has("replyto")) {
            jmsg.setReplyTo(json.getInt("replyto"));
        }
        if (json.has("body") && !json.isNull("body")) {
            jmsg.Text = Utils.replace(json.getString("body"),"&quot;", "\"");
        } else {
            jmsg.Text = "";
        }
        jmsg.User = parseUserJSON(json.getJSONObject("user"));

        try {
            ISimpleDateFormat df = DevJuickComMessages.sdftz.createSDF("yyyy-MM-dd HH:mm:ss","en","US","UTC");
            if (json.has("timestamp") && !json.isNull("timestamp")) {
                jmsg.Timestamp = new Date(df.parse(json.getString("timestamp")));
            } else {
                jmsg.Timestamp = new Date();
            }
        } catch (IllegalArgumentException e) {
        }

        if (json.has("tags")) {
            JSONArray tags = json.getJSONArray("tags");
            for (int n = 0; n < tags.length(); n++) {
                jmsg.tags.add(Utils.replace(tags.getString(n), "&quot;", "\""));
            }
        }

        if (json.has("replies")) {
            jmsg.replies = json.getInt("replies");
        }

        if (json.has("photo") && !json.isNull("photo")) {
            jmsg.Photo = json.getJSONObject("photo").getString("small");
            if (jmsg.Photo.startsWith("//")) {
                jmsg.Photo = "http:" + jmsg.Photo;
            }
        }
        if (json.has("video") && !json.isNull("video")) {
            jmsg.Video = json.getJSONObject("video").getString("mp4");
            if (jmsg.Video.startsWith("//")) {
                jmsg.Video = "http:" + jmsg.Video;
            }
        }
        if (json.has("context_post") && !json.isNull("context_post")) {
            jmsg.contextPost = initFromJSON(json.getJSONObject("context_post"));
        }
        if (json.has("context_reply") && !json.isNull("context_reply")) {
            jmsg.contextReply = initFromJSON(json.getJSONObject("context_reply"));
        }
        if (json.has("my_found_count")) {
            jmsg.myFoundCount = (int)json.getDouble("my_found_count");
        }
        if (json.has("his_found_count")) {
            jmsg.hisFoundCount = (int)json.getDouble("his_found_count");
        }
        jmsg.microBlogCode = JuickMessageID.CODE;

        return jmsg;
    }

    public static JuickUser parseUserJSON(org.ja.json.JSONObject json) throws JSONException {
        JuickUser juser = new JuickUser();
        juser.UID = json.getInt("uid");
        juser.UName = json.has("uname") && !json.isNull("uname") ? json.getString("uname") : "";
        if (json.has("fullname") && !json.isNull("fullname")) {
            juser.FullName = json.getString("fullname");
        }
        return juser;
    }
}
