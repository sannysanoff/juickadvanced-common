package com.juickadvanced.sources;

import com.juickadvanced.IHTTPClientService;
import com.juickadvanced.RESTResponse;
import com.juickadvanced.Utils;
import com.juickadvanced.data.MessageID;
import com.juickadvanced.data.juick.JuickMessage;
import com.juickadvanced.data.juick.JuickMessageID;
import com.juickadvanced.parsers.DevJuickComMessages;
import com.juickadvanced.parsers.JuickParser;
import com.juickadvanced.parsers.URLParser;
import com.juickadvanced.protocol.JuickHttpAPI;
import org.ja.json.JSONArray;
import org.ja.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by san on 8/8/14.
 */
public class PureJuickCompatibleURLMessagesSource extends PureMessageSource {
    public URLParser urlParser;
    int lastRetrievedMID;
    int page = 0;

    public PureJuickCompatibleURLMessagesSource(IHTTPClientService httpClientService) {
        super(httpClientService);
    }

    public static ArrayList<JuickMessage> parseJSONpure(String jsonStr, boolean storeSource) {
        ArrayList<JuickMessage> messages = new ArrayList<JuickMessage>();
        if (jsonStr != null) {
            try {
                JSONArray json = new JSONArray(jsonStr);
                int cnt = json.length();
                for (int i = 0; i < cnt; i++) {
                    JSONObject jsonObject = json.getJSONObject(i);
                    JuickMessage msg = JuickParser.initFromJSON(jsonObject);
                    if (msg.User != null && msg.User.UName != null) {
                        msg.User.UName = msg.User.UName.trim();
                    }
                    msg.Text = DevJuickComMessages.unjuick(msg.Text);
                    messages.add(msg);
                    if (!storeSource)
                        msg.source = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        HashMap<Integer, JuickMessage> replies = new HashMap<Integer, JuickMessage>();
        for (JuickMessage message : messages) {
            if (message.getRID() > 0) {
                replies.put(message.getRID(), message);
            }
        }
        for (JuickMessage message : messages) {
            if (message.getRID() > 0 && message.getReplyTo() > 0) {
                JuickMessage repliedTo = replies.get(message.getReplyTo());
                if (repliedTo != null && !message.Text.startsWith("@")) {
                    message.Text = "@"+repliedTo.User.UName+" "+message.Text;
                }
            }
        }
        return messages;
    }

    public void init(String url) {
        urlParser = new URLParser(url);
    }

    @Override
    public void getFirst(Utils.Notification notification, Utils.Function<Void, ArrayList<JuickMessage>> cont) {
        urlParser.getArgsMap().remove("before_mid");
        urlParser.getArgsMap().remove("page");
        page = 0;
        lastRetrievedMID = -1;
        fetchURLAndProcess(notification, cont);
    }

    public void fetchURLAndProcess(Utils.Notification notification, Utils.Function<Void, ArrayList<JuickMessage>> cont) {
        String fullURL = urlParser.getFullURL();
        RESTResponse json = httpClientService.getJSON(fullURL, notification);
        if (json.getResult() != null) {
            final String jsonStr = json.getResult();
            ArrayList<JuickMessage> messages = parseAndProcess(jsonStr);
            if (messages.size() > 0) {
                JuickMessage juickMessage = messages.get(messages.size() - 1);
                lastRetrievedMID = ((JuickMessageID)juickMessage.getMID()).getMid();
            }
            cont.apply(messages);
        } else {
            cont.apply(new ArrayList<JuickMessage>());
        }
    }

    @Override
    public void getNext(Utils.Notification notification, Utils.Function<Void, ArrayList<JuickMessage>> cont) {
        page++;
        if (page > 0) {
            putArg("page",""+page);
        }
        if (lastRetrievedMID > 0) {
            putArg("before_mid",""+lastRetrievedMID);
        }
        fetchURLAndProcess(notification, cont);
    }

    public void putArg(String name, String value) {
        urlParser.getArgsMap().put(name, value);
    }

    private ArrayList<JuickMessage> parseAndProcess(String jsonStr) {
        return parseJSONpure(jsonStr, false);
    }


    public void resetSavedPosition() {
        lastRetrievedMID = -1;
        page = 0;
        urlParser.getArgsMap().remove("before_mid");
        urlParser.getArgsMap().remove("page");
    }

    @Override
    public void getChildren(MessageID parent, Utils.Notification notification, Utils.Function<Void, ArrayList<JuickMessage>> cont) {
        ArrayList<JuickMessage> download = new PureJuickChildrenDownloader().download(parent, httpClientService, notification, false);
        cont.apply(download);
    }

    public static class PureJuickChildrenDownloader {
        public ArrayList<JuickMessage> download(final MessageID mid, IHTTPClientService client, final Utils.Notification notifications, boolean storeSource) {
            RESTResponse result = client.getJSON(JuickHttpAPI.getAPIURL() + "thread?mid=" + ((JuickMessageID) mid).getMid(), notifications);
            final String jsonStr = result.getResult();
            final ArrayList<JuickMessage> stuff = PureJuickCompatibleURLMessagesSource.parseJSONpure(jsonStr, storeSource);
            return stuff;
        }

        public ArrayList<JuickMessage> parseJSONpure(String str) {
            return PureJuickCompatibleURLMessagesSource.parseJSONpure(str, false);
        }

    }
}
