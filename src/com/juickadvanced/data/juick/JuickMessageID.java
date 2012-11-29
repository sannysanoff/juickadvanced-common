package com.juickadvanced.data.juick;

import com.juickadvanced.data.MessageID;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: san
 * Date: 11/9/12
 * Time: 4:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class JuickMessageID extends MessageID implements Serializable {
    public static final String CODE = "juick";
    private int mid;

    public JuickMessageID(int mid) {
        this.mid = mid;
    }

    public int getMid() {
        return mid;
    }

    @Override
    public String toString() {
        return "jui-"+mid;
    }

    @Override
    public String toDisplayString() {
        return "#"+mid;
    }

    @Override
    public String getMicroBlogCode() {
        return CODE;
    }

    public static JuickMessageID fromString(String str) {
        if (str.startsWith("jui-")) {
            return new JuickMessageID(Integer.parseInt(str.substring(4)));
        }
        if (str.indexOf("-") < 0) {
            try {
                return new JuickMessageID(Integer.parseInt(str));
            } catch (NumberFormatException e) {
                //
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JuickMessageID that = (JuickMessageID) o;

        if (mid != that.mid) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return mid;
    }

    public JuickMessageID getNextMid() {
        return new JuickMessageID(mid+1);
    }
}
