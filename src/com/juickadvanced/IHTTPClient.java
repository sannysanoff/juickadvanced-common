package com.juickadvanced;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by san on 8/8/14.
 */
public interface IHTTPClient {

    public static class Header {
        public String name;
        public String value;

        public Header(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }

    public abstract static class Response {
        public int responseCode;

        protected Response(int responseCode) {
            this.responseCode = responseCode;
        }

        public abstract InputStream getStream() throws IOException;

        public abstract Header[] getHeaders(String s);

    }
    public void setURL(String method, String url);
    public void addHeader(String name, String value);
    public void setURLEncodedPostData(String data);
    public Response execute() throws IOException;
    public void terminate();
}
