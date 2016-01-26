package com.juickadvanced.sources;

import com.juickadvanced.IHTTPClientService;
import com.juickadvanced.Utils;
import com.juickadvanced.data.MessageID;
import com.juickadvanced.data.bnw.BNWMessage;
import com.juickadvanced.data.bnw.BnwMessageID;
import com.juickadvanced.data.juick.JuickMessage;
import com.juickadvanced.data.juick.JuickUser;
import com.juickadvanced.parsers.URLParser;
import org.ja.json.JSONArray;
import org.ja.json.JSONException;
import org.ja.json.JSONObject;

import java.util.*;

/**
 * Created by san on 8/8/14.
 */
public class PureBnwMessagesSource extends PureMessageSource {
    URLParser urlParser;
    int page;
    HashSet<String> loadedMessages = new HashSet<String>();

    public PureBnwMessagesSource(IHTTPClientService httpClientService) {
        super(httpClientService);
    }

    @Override
    public void getFirst(Utils.Notification notification, Utils.Function<Void, ArrayList<JuickMessage>> cont) {
        page = 0;
        loadedMessages.clear();
        fetchURLAndProcess(notification, cont);
    }

    public ArrayList<JuickMessage> parseJSONpure(String jsonStr) {
        return parseJSONpure(jsonStr, false);
    }

    public ArrayList<JuickMessage> parseJSONpure(String jsonStr, boolean storeSource) {
        ArrayList<JuickMessage> messages = new ArrayList<JuickMessage>();
        if (jsonStr != null) {
            try {
                JSONObject objMessages = new JSONObject(jsonStr);
                JSONArray json = objMessages.getJSONArray("messages");
                int cnt = json.length();
                for (int i = 0; i < cnt; i++) {
                    JSONObject jsonObject = json.getJSONObject(i);
                    JuickMessage msg = initFromJSON(jsonObject);
                    messages.add(msg);
                    if (!storeSource)
                        msg.source = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                //Log.e("initOpinionsAdapter", e.toString());
            }
        }
        return messages;
    }



    protected void fetchURLAndProcess(Utils.Notification notification, Utils.Function<Void, ArrayList<JuickMessage>> cont) {
        if (page == 0) {
            urlParser.getArgsMap().remove("page");
        } else {
            urlParser.getArgsMap().put("page", "" + page);
        }
        final String jsonStr = httpClientService.getJSON(urlParser.getFullURL(), notification).getResult();
        ArrayList<JuickMessage> messages = parseJSONpure(jsonStr);
        if (messages.size() > 0) {
            ArrayList<JuickMessage> reverse = new ArrayList<JuickMessage>();
            for (Iterator<JuickMessage> iterator = messages.iterator(); iterator.hasNext(); ) {
                JuickMessage message = iterator.next();
                if (!loadedMessages.add(message.getMID().toString())) {
                    iterator.remove();
                } else {
                    reverse.add(0, message);
                }
            }
            if (loadedMessages.size() == 0) {
                page++;
                fetchURLAndProcess(notification, cont);
                return;
            } else {
                messages = reverse;
            }
        }
        cont.apply(messages);
    }

    @Override
    public void getNext(Utils.Notification notification, Utils.Function<Void, ArrayList<JuickMessage>> cont) {
        page++;
        fetchURLAndProcess(notification, cont);
    }

    @Override
    public void getChildren(MessageID mid, Utils.Notification notification, Utils.Function<Void, ArrayList<JuickMessage>> cont) {
        String midString = ((BnwMessageID)mid).getId();
        final String jsonStr = httpClientService.getJSON("http://ipv4.bnw.im/api/show?message=" + midString + "&replies=1", notification).getResult();
        if (jsonStr != null) {
            try {
                JSONObject fullThread = new JSONObject(jsonStr);
                JSONObject root = fullThread.getJSONObject("message");
                ArrayList<BNWMessage> msgs = new ArrayList<BNWMessage>();
                msgs.add(initFromJSON(root));
                JSONArray replies = fullThread.getJSONArray("replies");
                HashMap<String,Integer> numbersRemap = new HashMap<String, Integer>();
                int replyNo = 1;
                for(int i = 0; i < replies.length(); i++) {
                    BNWMessage reply = initFromJSON(replies.getJSONObject(i));
                    msgs.add(reply);
                    reply.setRID(replyNo);
                    numbersRemap.put(reply.getRIDString(), replyNo);
                    replyNo++;
                }
                for (int i = 1; i < msgs.size(); i++) {
                    BNWMessage msg = msgs.get(i);
                    String replyToString = msg.getReplyToString();
                    if (replyToString == null) {
                        msg.setReplyTo(0);
                    } else {
                        Integer prevComment = numbersRemap.get(replyToString);
                        if (prevComment == null) prevComment = 0;
                        msg.setReplyTo(prevComment);
                    }
                }
                cont.apply(new ArrayList<JuickMessage>(msgs));
            } catch (JSONException e) {
                cont.apply(new ArrayList<JuickMessage>());
            }
        } else {
            cont.apply(new ArrayList<JuickMessage>());
        }
    }

    public static BNWMessage initFromJSON(JSONObject json) throws JSONException {
        BNWMessage jmsg = new BNWMessage();
        if (json.has("message")) {
            jmsg.setMID(new BnwMessageID(json.getString("message")));
            jmsg.setRIDString(json.getString("id"));
        } else {
            jmsg.setMID(new BnwMessageID(json.getString("id")));
        }
        jmsg.Text = json.getString("text");
        Calendar cal = new GregorianCalendar();
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        cal.setTimeInMillis((long) (json.getDouble("date") * 1000));
        jmsg.Timestamp = cal.getTime();
        jmsg.User = new JuickUser();
        jmsg.User.UName = json.getString("user");
        if (json.has("replyto") && json.get("replyto") != JSONObject.NULL)
            jmsg.setReplyToString(json.getString("replyto"));

        if (json.has("tags")) {
            JSONArray tags = json.getJSONArray("tags");
            for (int n = 0; n < tags.length(); n++) {
                jmsg.tags.add(tags.getString(n).replace("&quot;", "\""));
            }
        }
        if (json.has("clubs")) {
            JSONArray clubs = json.getJSONArray("clubs");
            for (int n = 0; n < clubs.length(); n++) {
                jmsg.clubs.add(clubs.getString(n).replace("&quot;", "\""));
            }
        }
        if (json.has("replycount")) {
            jmsg.replies = json.getInt("replycount");
        }
        jmsg.microBlogCode = BnwMessageID.CODE;
        return jmsg;
    }


    public void setPath(String path) {
        urlParser = new URLParser("http://ipv4.bnw.im/api"+path);
    }

}
