package com.juickadvanced.lang;

/**
 * Created by san on 11/8/15.
 */
public interface ISimpleDateFormat {

    public long parse(String str) throws IllegalArgumentException;
    public String format(long date);

}
