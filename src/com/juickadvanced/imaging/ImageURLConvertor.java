package com.juickadvanced.imaging;

import com.juickadvanced.parsers.URLParser;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

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
        if (url.contains("i.point.im")) {
            URLParser parser = new URLParser(url);
            String nurl = parser.getArgsMap().get("u");
            try {
                String retval = URIUtils.decode(nurl);
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
