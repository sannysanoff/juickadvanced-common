package com.juickadvanced.imaging;

import com.juickadvanced.data.MessageID;
import com.juickadvanced.lang.Matcher;
import com.juickadvanced.lang.Pattern;
import org.jsoup.helper.StringUtil;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: san
 * Date: 9/14/12
 * Time: 10:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExtractURLFromMessage {
    public static Pattern urlPattern = Pattern.compile("(?:(?:\\s|\\A)+|(?:\\u005B.*?\\u005D\\u0028))?((?:ht|f)tps?://[a-z0-9\\-\\.]+[a-z]{2,}/?[^\\s\\u0029>]*)", Pattern.CASE_INSENSITIVE);

    public static class FoundURL {
        public String url;
        public int start;
        public int end;

        FoundURL(String url, int start, int end) {
            this.url = url;
            this.start = start;
            this.end = end;
        }

        public FoundURL title; // hehehe

        public String getUrl() {
            return url;
        }
    }

    public static ArrayList<FoundURL> extractUrls(String txt, MessageID mid) {
        Matcher m = ExtractURLFromMessage.urlPattern.matcher(txt);
        ArrayList<FoundURL> urls = new ArrayList<FoundURL>();
        int pos = 0;
        while (m.find(pos)) {
            int start = m.start();
            while(Character.isWhitespace(txt.charAt(start))) start++;
            if (txt.charAt(start) == '[') {
                start = txt.indexOf("(", start)+1;
            }
            urls.add(new FoundURL(txt.substring(start, m.end()), start, m.end()));
            pos = m.end();
        }
/*
        int scanOffset = 0;
urlscan:
        while(true) {
            int ix = txt.indexOf("[", scanOffset);
            if (ix == -1) break;
            int ix2 = txt.indexOf("]", ix);
            if (ix2 == -1) break;
            int ix3 = txt.indexOf("[", ix2);
            if (ix3 == -1) break;
            int ix4 = txt.indexOf("]", ix3);
            if (ix4 == -1) break;
            for(int i=ix2+1; i<ix3-1; i++) {
                if (!StringUtil.isWhitespace(txt.charAt(i))) break urlscan;
            }
            scanOffset = ix+1;
            String maybeURL = txt.substring(ix3 + 1, ix4).toLowerCase().trim();
            if (!maybeURL.startsWith("http")) continue;   // not url inside
            FoundURL fu = new FoundURL(maybeURL, ix3+1, ix4);
            urls.add(fu);
            fu.title = new FoundURL(txt.substring(ix+1, ix2), ix+1, ix2);
            scanOffset = ix4+1;
        }
*/
        return urls;
    }

}
