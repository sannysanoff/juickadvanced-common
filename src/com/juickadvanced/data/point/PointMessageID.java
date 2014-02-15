package com.juickadvanced.data.point;

import com.juickadvanced.data.MessageID;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: san
 * Date: 11/9/12
 * Time: 4:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class PointMessageID extends MessageID implements Serializable {
    public static final String CODE = "point";
    String id;
    public String user;

    public PointMessageID(String user, String id) {
        this.id = id;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "pnt-"+id;
    }

    @Override
    public String toDisplayString() {
        return "#"+id;
    }

    @Override
    public String getMicroBlogCode() {
        return CODE;
    }

    public static PointMessageID fromString(String str) {
        if (str.startsWith("pnt-")) {
            return new PointMessageID("", str.substring(4));
        }
        return null;
    }

    static String remap = "abcdefghijklmnopqrstuvwxyz";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        PointMessageID that = (PointMessageID) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
