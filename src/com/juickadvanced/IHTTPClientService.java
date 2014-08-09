package com.juickadvanced;

/**
 * Created by san on 8/8/14.
 */
public interface IHTTPClientService {

    public RESTResponse getJSON(String url, Utils.Notification progressNotification);

    IHTTPClient createClient();

}
