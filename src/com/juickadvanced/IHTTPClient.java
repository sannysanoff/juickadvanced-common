package com.juickadvanced;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by san on 8/8/14.
 */
public interface IHTTPClient {
    public abstract static class Response {
        public int responseCode;

        protected Response(int responseCode) {
            this.responseCode = responseCode;
        }

        public abstract InputStream getStream() throws IOException;
    }
    public void setURL(String method, String url);
    public void addHeader(String name, String value);
    public void setPostData(String data);
    public Response execute() throws IOException;
    public void terminate();
}
