package com.juickadvanced.sources;

import com.juickadvanced.IHTTPClient;
import com.juickadvanced.IHTTPClientService;
import com.juickadvanced.Utils;
import com.juickadvanced.data.MessageID;
import com.juickadvanced.data.facebook.FacebookMessageID;
import com.juickadvanced.data.juick.JuickMessage;
import com.juickadvanced.protocol.FacebookTransport;
import org.ja.json.JSONArray;
import org.ja.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by san on 8/8/14.
 */
public class PureFacebookFeedMessagesSource extends PureMessageSource {

    private String nextCursor;

    public PureFacebookFeedMessagesSource(IHTTPClientService httpClientService) {
        super(httpClientService);
    }

    @Override
    public void getFirst(Utils.Notification notification, Utils.Function<Void, ArrayList<JuickMessage>> cont) {
        try {
            IHTTPClient client = httpClientService.createClient();
            FacebookTransport transport = new FacebookTransport(client);
            FacebookTransport.Feed firstHomeFeed = transport.getFirstHomeFeed(notification);
            nextCursor = null;
            if (firstHomeFeed != null) {
                try {
                    JSONObject feed = firstHomeFeed.part.getJSONObject("viewer").getJSONObject("news_feed");
                    nextCursor = feed.getJSONObject("page_info").getString("end_cursor");
                } catch (Exception ex) {}
                ArrayList<JuickMessage> juickMessages = FacebookTransport.parseJsonPure(firstHomeFeed.part);
                cont.apply(juickMessages);
            }
        } catch (Exception e) {
            if (notification instanceof com.juickadvanced.Utils.DownloadErrorNotification) {
                com.juickadvanced.Utils.DownloadErrorNotification n = (com.juickadvanced.Utils.DownloadErrorNotification)notification;
                n.notifyDownloadError(e.toString());
            }
        }
    }

    @Override
    public void getNext(Utils.Notification notification, Utils.Function<Void, ArrayList<JuickMessage>> cont) {
        try {
            if (nextCursor == null) {
                cont.apply(new ArrayList<JuickMessage>());
                return;
            }
            IHTTPClient client = httpClientService.createClient();
            FacebookTransport transport = new FacebookTransport(client);
            FacebookTransport.Feed nextPart = transport.getNextHomeFeed(notification, nextCursor);
            if (nextPart != null) {
                try {
                    JSONObject feed = nextPart.part.getJSONObject("viewer").getJSONObject("news_feed");
                    nextCursor = feed.getJSONObject("page_info").getString("end_cursor");
                } catch (Exception ex) {}
                ArrayList<JuickMessage> juickMessages = FacebookTransport.parseJsonPure(nextPart.part);
                cont.apply(juickMessages);
            }
        } catch (Exception e) {
            if (notification instanceof com.juickadvanced.Utils.DownloadErrorNotification) {
                com.juickadvanced.Utils.DownloadErrorNotification n = (com.juickadvanced.Utils.DownloadErrorNotification)notification;
                n.notifyDownloadError(e.toString());
            }
        }
    }

    @Override
    public void getChildren(MessageID parent, Utils.Notification notification, Utils.Function<Void, ArrayList<JuickMessage>> cont) {
        try {
            ArrayList<JuickMessage> retval = new ArrayList<JuickMessage>();
            IHTTPClient client = httpClientService.createClient();
            FacebookTransport transport = new FacebookTransport(client);
            FacebookMessageID fbmid = (FacebookMessageID)parent;
            JSONArray children = transport.getChildren(notification, fbmid.feedbackId);
            int ix = 0;
            for(int i=children.length()-1; i>=0; i--) {
                JSONObject comment = children.getJSONObject(i);
                JuickMessage cmt = transport.parseCommentPure(parent, comment, ++ix);
                if (cmt != null) {
                    retval.add(cmt);
                }
            }
            cont.apply(retval);
        } catch (Exception e) {
            if (notification instanceof com.juickadvanced.Utils.DownloadErrorNotification) {
                com.juickadvanced.Utils.DownloadErrorNotification n = (com.juickadvanced.Utils.DownloadErrorNotification)notification;
                n.notifyDownloadError(e.toString());
            }
        }
    }
}
