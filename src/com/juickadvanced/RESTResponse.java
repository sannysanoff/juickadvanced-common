package com.juickadvanced;

/**
* Created by san on 8/5/14.
*/
public class RESTResponse {
    public String result;
    public String errorText;
    public boolean mayRetry;

    public RESTResponse(String errorText, boolean mayRetry, String result) {
        this.errorText = errorText;
        this.mayRetry = mayRetry;
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public boolean isMayRetry() {
        return mayRetry;
    }

    public String getErrorText() {
        return errorText;
    }
}
