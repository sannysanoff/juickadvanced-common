package com.juickadvanced.protocol;

/**
 * Created with IntelliJ IDEA.
 * User: coderoo
 * Date: 6/12/13
 * Time: 9:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class JuickHttpAPI {

    private static boolean httpsEnabled = false;

    public static final String API_URL_HOST = "api.juick.com";
    public static final String HTTP_URL = "http://" + API_URL_HOST + "/";
    public static final String HTTPS_URL = "https://" + API_URL_HOST + "/";

    public static String getAPIURL() {
        return httpsEnabled ? HTTPS_URL : HTTP_URL;
    }

    public static boolean isURLforAPIHost(final String url) {
        return url.startsWith(HTTP_URL) || url.startsWith(HTTPS_URL);
    }

    public synchronized static void setHttpsEnabled(final boolean value) {
        httpsEnabled = value;
    }

    public static boolean isHttpsEnabled() {
        return httpsEnabled;
    }
}