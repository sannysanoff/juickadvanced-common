package com.juickadvanced.parsers;

import com.juickadvanced.data.point.PointMessage;
import com.juickadvanced.data.point.PointMessageID;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (DevJuickComMessages.sdftz != null) {
            DevJuickComMessages.sdftz.initSDFTZ(sdf, "GMT");
        }
        jo.put("timestamp", sdf.format(pm.Timestamp));
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
