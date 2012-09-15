package com.juickadvanced.imaging;

/**
 * Created with IntelliJ IDEA.
 * User: san
 * Date: 9/14/12
 * Time: 10:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExtractImageURLFromHTML {
    public static String extractImageURLFromHTML(StringBuffer sb) {
        String html = sb.toString();
        String imageURL = null;
        if (html.indexOf("gelbooru") != -1) {
            // <img src="http://cdn1.gelbooru.com//samples/1397/sample_8f31057149f27c1c211f587d8251cedb.jpg?1608984" id="image" ...
            int idimage = html.indexOf("id=\"image\"");
            if (idimage != -1) {
                html = html.substring(0, idimage);
                int src = html.lastIndexOf("src=");
                if (src != -1) {
                    html = html.substring(src+5);
                    int quote = html.lastIndexOf("\"");
                    if (quote != -1) {
                        html = html.substring(0, quote-1);
                        imageURL = html;
                    }
                }
            }
        }
        return imageURL;
    }
}
