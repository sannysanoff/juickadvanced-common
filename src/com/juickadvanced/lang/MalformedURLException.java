package com.juickadvanced.lang;

import java.io.IOException;

/**
 * Created by san on 8/9/14.
 */
public class MalformedURLException extends IOException {
    private static final long serialVersionUID = -182787522200415866L;

    /**
     * Constructs a <code>MalformedURLException</code> with no detail message.
     */
    public MalformedURLException() {
    }

    /**
     * Constructs a <code>MalformedURLException</code> with the
     * specified detail message.
     *
     * @param   msg   the detail message.
     */
    public MalformedURLException(String msg) {
        super(msg);
    }
}
