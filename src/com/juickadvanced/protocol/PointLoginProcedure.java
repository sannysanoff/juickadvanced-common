package com.juickadvanced.protocol;

import com.juickadvanced.IHTTPClient;
import com.juickadvanced.IHTTPClientService;
import com.juickadvanced.RESTResponse;
import com.juickadvanced.Utils;
import org.ja.json.JSONObject;

import java.io.InputStream;


/**
 * Created by san on 8/8/14.
 */
public class PointLoginProcedure {
    /**
     * unsafe
     */
    public static RESTResponse obtainCookieByLoginPassword(IHTTPClientService httpService, String login, String password) {
        IHTTPClient client = httpService.createClient();
        try {
            client.setURL("POST", "http://point.im/api/login");
            client.setURLEncodedPostData("login=" + login + "&password=" + password);
            IHTTPClient.Response execute = client.execute();
            if (execute.responseCode == 200) {
                IHTTPClient.Header[] cookies = execute.getHeaders("Set-Cookie");
                String cookie = "";
                for (IHTTPClient.Header header : cookies) {
                    String hv = header.value;
                    if (hv.indexOf("user=") >= 0) {
                        int start = hv.indexOf("user=")+5;
                        int end = hv.indexOf(";", start);
                        cookie = hv.substring(start, end);
                    }
                }
                InputStream content = execute.getStream();
                RESTResponse response = Utils.streamToString(content, null);
                JSONObject responseJO = new JSONObject(response.getResult());
                if (responseJO.has("error")) {
                    return new RESTResponse("Auth error", true, null);
                }
                String token = responseJO.getString("token");
                String csrf_token = responseJO.getString("csrf_token");
                content.close();
                return new RESTResponse(null, false, token+"|"+csrf_token+"|"+cookie);
            } else {
                return new RESTResponse("Auto error", true, null);
            }
        } catch (Exception e) {
            return new RESTResponse("Other error: " + e.toString(), false, null);
            //
        } finally {
            client.terminate();
        }
    }
}
