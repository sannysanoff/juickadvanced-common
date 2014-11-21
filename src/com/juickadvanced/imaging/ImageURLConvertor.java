package com.juickadvanced.imaging;

import com.juickadvanced.parsers.URLParser;


/**
 * Created with IntelliJ IDEA.
 * User: san
 * Date: 9/14/12
 * Time: 10:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageURLConvertor {
    public static String convertURLToDownloadable(String url) {
        if (url.indexOf("www.dropbox.com/") != -1 && url.indexOf("dl=1") == -1) {
            if (url.indexOf("?") != -1) {
                url += "&dl=1";
            } else {
                url += "?dl=1";
            }
        }
        if (url.indexOf("gyazo.com/") != -1) {
            if (url.lastIndexOf(".") < url.length() - 10) {
                url += ".png";
            }
        }
        if (url.indexOf("i.point.im") != -1) {
            URLParser parser = new URLParser(url);
            String nurl = parser.getArgsMap().get("u");
            if (nurl == null) nurl = url;
            try {
                String retval = URIUtils.decode(nurl);
                if (retval.endsWith(".thumb")) {
                    retval = retval.substring(0, retval.length() - 6);
                }
                if (retval.endsWith(":large")) {
                    retval = retval.substring(0, retval.length() - 6);
                }
                return retval;
            } catch (Exception e) {
                //
            }
        }
        if (url.indexOf("%") != -1) {
            return URIUtils.decode(url);
        }
        return url;
    }
}
