package com.juickadvanced.protocol;

import com.juickadvanced.IHTTPClient;
import com.juickadvanced.IHTTPClientService;
import com.juickadvanced.RESTResponse;
import com.juickadvanced.lang.URL;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by san on 8/8/14.
 */
public class JuickLoginProcedure {
    public static RESTResponse validateLoginPassword(String loginS, String passwordS, IHTTPClientService httpClientService) {
        int status = 0;
        RESTResponse result = null;
        if (loginS == null) {
            return new RESTResponse("Empty own Juick username (?)", false, null);
        }
        RESTResponse json = httpClientService.getJSON(JuickHttpAPI.getAPIURL() + "users?uname=" + loginS.trim(), null);
        if (json.getErrorText() != null) {
            result = new RESTResponse("Unknown username!", false, null);
        } else {
            IHTTPClient client = httpClientService.createClient();
            try {
                JSONArray idname = new JSONArray(json.getResult());
                if (idname.length() != 1)
                    throw new RuntimeException("Unknown username (zero length API response)!");
                String canonicalName = (String)((JSONObject) idname.get(0)).get("uname");
                String authStr = canonicalName + ":" + passwordS;
                final String basicAuth = "Basic " + Base64.encodeToString(authStr.getBytes(), Base64.NO_WRAP);
                URL apiUrl = new URL(JuickHttpAPI.getAPIURL() + "post?login_check");
                client.setURL("POST", apiUrl.toExternalForm());
                client.addHeader("Authorization", basicAuth);
                client.setURLEncodedPostData("body=S%20*juick_advanced");
                IHTTPClient.Response execute = client.execute();
                status = execute.responseCode;
                if (status == 200) {
                    result = new RESTResponse(null, false, canonicalName+":"+idname.getJSONObject(0).getInt("uid"));
                } else {
                    result = new RESTResponse("Login failed, status="+status, false, null);
                }
            } catch (Exception e) {
                result = new RESTResponse(e.toString(), false, null);
            } finally {
                client.terminate();
            }
        } return result;
    }
}
