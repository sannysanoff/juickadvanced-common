package com.juickadvanced.imaging;

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
        if (urlLower.startsWith("http://i.point.im/")) {
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
