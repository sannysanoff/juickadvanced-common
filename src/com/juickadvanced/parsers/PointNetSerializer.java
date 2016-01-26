package com.juickadvanced.parsers;

import com.juickadvanced.data.point.PointMessage;
import com.juickadvanced.data.point.PointMessageID;
import com.juickadvanced.lang.ISimpleDateFormat;
import org.ja.json.JSONArray;
import org.ja.json.JSONException;
import org.ja.json.JSONObject;

/**
 * Created by san on 8/14/14.
 */
public class PointNetSerializer {
    public static JSONObject messageToJSON(PointMessage pm) throws JSONException {
        PointMessageID mid = pm.getMID();
        JSONObject jo = new JSONObject();
        jo.put("body", pm.Text);
        if (pm.getRID() != 0) {
            jo.put("parent_mid", "pnt-"+pm.getMID().getId());
        } else {
            jo.put("mid", "pnt-"+pm.getMID().getId());
        }
        ISimpleDateFormat sdf = DevJuickComMessages.sdftz.createSDF("yyyy-MM-dd HH:mm:ss", "en","US", "UTC");

        jo.put("timestamp", sdf.format(pm.Timestamp.getTime()));
        if (pm.getRID() != 0) {
            jo.put("rid", pm.getRID());
        }
        if (pm.getReplyTo() != 0) {
            jo.put("replyto", pm.getReplyTo());
        }
        JSONObject user = new JSONObject();
        jo.put("user", user);
        JSONArray arr = new JSONArray();
        for (String tag : pm.tags) {
            arr.put(tag);
        }
        if (arr.length() > 0) {
            jo.put("tags", arr);
        }
        user.put("uname", pm.User.UName);
        jo.put("mb","point");
        return jo;
    }
}
