package com.juickadvanced.lang;

import java.util.ArrayList;

/**
 * Created by san on 8/9/14.
 */
public class StringSplitter {

    public static String[] split(String what, String regex) {
        return split(what, regex, 0);
    }

    public static String[] split(String what, String regex, int limit) {
        /* fastpath if the regex is a
         (1)one-char String and this character is not one of the
            RegEx's meta characters ".$|()[{^?*+\\", or
         (2)two-char String and the first char is the backslash and
            the second is not the ascii digit or ascii letter.
         */
        char ch = 0;
        if (((regex.length() == 1 &&
                ".$|()[{^?*+\\".indexOf(ch = regex.charAt(0)) == -1) ||
                (regex.length() == 2 &&
                        regex.charAt(0) == '\\' &&
                        (((ch = regex.charAt(1))-'0')|('9'-ch)) < 0 &&
                        ((ch-'a')|('z'-ch)) < 0 &&
                        ((ch-'A')|('Z'-ch)) < 0)) &&
                (ch < Character.MIN_HIGH_SURROGATE ||
                        ch > Character.MAX_LOW_SURROGATE))
        {
            int off = 0;
            int next = 0;
            boolean limited = limit > 0;
            ArrayList<String> list = new ArrayList<String>();
            while ((next = what.indexOf(ch, off)) != -1) {
                if (!limited || list.size() < limit - 1) {
                    list.add(what.substring(off, next));
                    off = next + 1;
                } else {    // last one
                    //assert (list.size() == limit - 1);
                    list.add(what.substring(off, what.length()));
                    off = what.length();
                    break;
                }
            }
            // If no match was found, return this
            if (off == 0)
                return new String[]{what};

            // Add remaining segment
            if (!limited || list.size() < limit)
                list.add(what.substring(off, what.length()));

            // Construct result
            int resultSize = list.size();
            if (limit == 0)
                while (resultSize > 0 && list.get(resultSize - 1).length() == 0)
                    resultSize--;
            String[] result = new String[resultSize];
            return list.subList(0, resultSize).toArray(result);
        }
        return Pattern.compile(regex).split(new StringCharSequence(what), limit);
    }
}
