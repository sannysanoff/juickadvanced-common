package com.juickadvanced.imaging;

import com.juickadvanced.lang.URLDecoder;
import com.juickadvanced.parsers.URLParser;

import java.io.UnsupportedEncodingException;

/**
 * Created with IntelliJ IDEA.
 * User: san
 * Date: 9/14/12
 * Time: 10:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class ValidImageURLDetector {
    public static boolean isValidImageURL0(String urlLower) {
        if (urlLower.indexOf("http://gyazo.com") != -1) return true;
        if (urlLower.startsWith("http://i.point.im/") || urlLower.startsWith("https://i.point.im/")) {
            if (urlLower.contains("u=http")) {
                URLParser p = new URLParser(urlLower);
                urlLower = p.getArgsMap().get("u");
                try {
                    urlLower = URLDecoder.decode(urlLower, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                p = new URLParser(urlLower);
                urlLower = p.getPathPart();
            }
            if (urlLower.endsWith(".thumb")) {
                urlLower = urlLower.substring(0, urlLower.length()-6);
            }
            if (urlLower.endsWith(":large")) {
                urlLower = urlLower.substring(0, urlLower.length()-6);
            }
            // dont check args
        } else {
            int args = urlLower.indexOf("?");
            if (args != -1) {
                urlLower = urlLower.substring(0, args);
            }
            args = urlLower.indexOf("&");       // dropbox? without "?"
            if (args != -1) {
                urlLower = urlLower.substring(0, args);
            }
        }
        if (urlLower.indexOf("img-fotki.yandex.ru/get") >= 0 && urlLower.endsWith("orig"))
            return true;
        if (urlLower.indexOf("fbcdn-sphotos") >= 0)
            return true;
        if (urlLower.indexOf("http://commons.wikimedia.org/wiki/file:") >= 0)
            return false;
        if (urlLower.endsWith(":large")) {
            urlLower = urlLower.substring(0, urlLower.length() - 6);
        }
        return urlLower.endsWith(".png") || urlLower.endsWith(".gif") || urlLower.endsWith(".jpg") || urlLower.endsWith(".jpeg");
    }
}
