package com.juickadvanced.data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: san
 * Date: 11/9/12
 * Time: 4:03 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class MessageID implements Serializable {
    public abstract String toString();

    public abstract String toDisplayString();

    public abstract String getMicroBlogCode();


}
