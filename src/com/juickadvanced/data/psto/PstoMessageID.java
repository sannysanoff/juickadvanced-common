package com.juickadvanced.data.psto;

import com.juickadvanced.data.MessageID;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: san
 * Date: 11/9/12
 * Time: 4:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class PstoMessageID extends MessageID implements Serializable {
    public static final String CODE = "psto";
    String id;
    public String user;

    public PstoMessageID(String user, String id) {
        this.id = id;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "pst-"+id;
    }

    @Override
    public String toDisplayString() {
        return "#"+id;
    }

    @Override
    public String getMicroBlogCode() {
        return CODE;
    }

    public static PstoMessageID fromString(String str) {
        if (str.startsWith("pst-")) {
            return new PstoMessageID("", str.substring(4));
        }
        return null;
    }

    static String remap = "abcdefghijklmnopqrstuvwxyz";

    public int toInt() {
        int integer = 0;
        for(int i=0; i<6; i++) {
            int ix = remap.indexOf(id.charAt(i));
            integer = integer * remap.length() + ix;
        }
        return integer;
    }

    public static String pstoToInt(int midd) {
        String smid = "";
        for(int i=0; i<6; i++) {
            smid = remap.charAt(midd % remap.length()) + smid;
            midd /= remap.length();
        }
        return smid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PstoMessageID that = (PstoMessageID) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
