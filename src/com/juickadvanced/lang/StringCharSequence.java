package com.juickadvanced.lang;

/**
 * Created by san on 8/9/14.
 */
public class StringCharSequence implements CharSequence {
    String str;

    public StringCharSequence(String str) {
        this.str = str;
    }

    @Override
    public int length() {
        return str.length();
    }

    @Override
    public char charAt(int index) {
        return str.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return new StringCharSequence(str.substring(start, end));
    }
}
