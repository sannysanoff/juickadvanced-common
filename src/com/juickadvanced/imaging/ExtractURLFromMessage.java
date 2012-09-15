package com.juickadvanced.imaging;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: san
 * Date: 9/14/12
 * Time: 10:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExtractURLFromMessage {
    public static Pattern urlPattern = Pattern.compile("((?<=\\A)|(?<=\\s))(ht|f)tps?://[a-z0-9\\-\\.]+[a-z]{2,}/?[^\\s\\n]*", Pattern.CASE_INSENSITIVE);

    public static class FoundURL {
        String url;
        int start;
        int end;

        FoundURL(String url, int start, int end) {
            this.url = url;
            this.start = start;
            this.end = end;
        }

        public String getUrl() {
            return url;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }
    }

    public static ArrayList<FoundURL> extractUrls(String txt) {
        Matcher m = ExtractURLFromMessage.urlPattern.matcher(txt);
        ArrayList<FoundURL> urls = new ArrayList<FoundURL>();
        int pos = 0;
        while (m.find(pos)) {
            urls.add(new FoundURL(txt.substring(m.start(), m.end()), m.start(), m.end()));
            pos = m.end();
        }
        return urls;

    }

}
