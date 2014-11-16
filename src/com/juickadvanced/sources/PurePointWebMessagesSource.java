package com.juickadvanced.sources;

import com.juickadvanced.IHTTPClientService;
import com.juickadvanced.Utils;
import com.juickadvanced.data.MessageID;
import com.juickadvanced.data.juick.JuickMessage;
import com.juickadvanced.parsers.PointNetParser;
import com.juickadvanced.parsers.URLParser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by san on 8/8/14.
 */
public class PurePointWebMessagesSource extends PureMessageSource {

    public URLParser urlParser;
    int page;
    HashSet<String> loadedMessages = new HashSet<String>();

    public PurePointWebMessagesSource(IHTTPClientService httpClientService) {
        super(httpClientService);
    }

    @Override
    public void getFirst(Utils.Notification notification, Utils.Function<Void, ArrayList<JuickMessage>> cont) {
        page = 1;
        loadedMessages.clear();
        fetchURLAndProcess(notification, cont);
    }

    protected void fetchURLAndProcess(Utils.Notification notification, Utils.Function<Void, ArrayList<JuickMessage>> cont) {
        // put in page
        String pathPart = urlParser.getPathPart();
        int ix = pathPart.lastIndexOf("/");
        if (ix != -1) {
            try {
                Integer.parseInt(pathPart.substring(ix+1));
                urlParser.setPath(pathPart.substring(0, ix));  // replace page
            } catch (NumberFormatException e) {
                // not a number
            }
        } else {
            try {
                Integer.parseInt(pathPart);
                urlParser.setPath("");  // pathPart is already number
            } catch (Exception ex) {}
            // have no number
        }
        if (page != 0) {
            if (urlParser.getPathPart().length() > 0) {
                urlParser.setPath(urlParser.getPathPart()+"/"+page);
            } else {
                urlParser.setPath(""+page);
            }
        }
        final String jsonStr = httpClientService.getJSON(urlParser.getFullURL(), notification).getResult();
        if (jsonStr != null) {
            ArrayList<JuickMessage> messages = new PointNetParser().parseWebMessageListPure(jsonStr);
            if (messages.size() > 0) {
                for (Iterator<JuickMessage> iterator = messages.iterator(); iterator.hasNext(); ) {
                    JuickMessage message = iterator.next();
                    if (!loadedMessages.add(""+message.getMID())) {
                        iterator.remove();
                    }
                }
                if (loadedMessages.size() == 0) {
                    page++;
                    fetchURLAndProcess(notification, cont);
                    return;
                }
            }
            cont.apply(messages);
        } else {
            // error (notified via Notification)
            cont.apply(new ArrayList<JuickMessage>());
        }
    }


    @Override
    public void getNext(Utils.Notification notification, Utils.Function<Void, ArrayList<JuickMessage>> cont) {
        page++;
        fetchURLAndProcess(notification, cont);

    }

    @Override
    public void getChildren(MessageID parent, Utils.Notification notification, Utils.Function<Void, ArrayList<JuickMessage>> cont) {
        ArrayList<JuickMessage> download = new PurePointAPIMessagesSource.PureChildrenDownloader().download(parent, httpClientService, notification, false);
        cont.apply(download);
    }

    @Override
    public void cleanFromCache() {
        super.cleanFromCache();
    }
}
