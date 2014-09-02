package com.juickadvanced.sources;

import com.juickadvanced.IHTTPClientService;
import com.juickadvanced.Utils;
import com.juickadvanced.data.MessageID;
import com.juickadvanced.data.juick.JuickMessage;
import com.juickadvanced.data.juick.JuickMessageID;
import com.juickadvanced.parsers.URLParser;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by san on 8/8/14.
 */
public abstract class PureMessageSource implements Serializable {

    transient IHTTPClientService httpClientService;

    public PureMessageSource(IHTTPClientService httpClientService) {
        this.httpClientService = httpClientService;
    }

    // after deserialie
    public void setHttpClientService(IHTTPClientService httpClientService) {
        this.httpClientService = httpClientService;
    }

    public abstract void getFirst(Utils.Notification notification, Utils.Function<Void, ArrayList<JuickMessage>> cont);
    public abstract void getNext(Utils.Notification notification, Utils.Function<Void, ArrayList<JuickMessage>> cont);

    public abstract void getChildren(MessageID parent, Utils.Notification notification, Utils.Function<Void, ArrayList<JuickMessage>> cont);

    public void cleanFromCache() {

    }
}
