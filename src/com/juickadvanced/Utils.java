package com.juickadvanced;

import com.juickadvanced.data.juick.JuickMessage;
import com.juickadvanced.lang.*;
import com.juickadvanced.lang.Matcher;
import com.juickadvanced.lang.Pattern;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by san on 8/5/14.
 */
public class Utils {

    public static String toRelaviteDate(long ts, boolean russian) {
        StringBuilder sb = new StringBuilder();
        long ctm = System.currentTimeMillis();
        long delta = ctm - ts;
        if (delta > 10000) {        // probably 1976 year
            Calendar cal = Calendar.getInstance();
            int currentYear = cal.get(Calendar.YEAR);
            cal.setTime(new Date(ts));
            cal.set(Calendar.YEAR, currentYear);
            ts = cal.getTime().getTime();
            delta = ctm - ts;
        }
        ts = (delta / 1000) / 60;
        if (ts < 0) {
            return russian ? "в будущем" : "in future";
        }
        long minutes = ts % 60;
        ts /= 60;
        long hours = ts % 24;
        ts /= 24;
        long days = ts;
        if (days != 0) {
            sb.append(days);
            sb.append(russian?"д":"d ");
        }
        if (hours != 0) {
            sb.append(hours);
            sb.append(russian?"ч":"h ");
        }
        if (minutes != 0) {
            sb.append(minutes);
            sb.append(russian?"м":"m ");
        }
        if (sb.length() == 0) {
            sb.append(russian ? "только что":"now");
        }
        return sb.toString();
    }

    public static boolean isLetterOrDigit(char ch) {
        return ch >= '0' && ch <= '9' || isLetter(ch);
    }

    public static boolean isLetter(char ch) {
        return ((((1 << CharacterData.UPPERCASE_LETTER) |
                (1 << CharacterData.LOWERCASE_LETTER) |
                (1 << CharacterData.TITLECASE_LETTER) |
                (1 << CharacterData.MODIFIER_LETTER) |
                (1 << CharacterData.OTHER_LETTER)) >> CharacterDataLatin1.instance.getType(ch)) & 1)
                != 0;
    }

    public static boolean isIdeographic(char ch) {
        return CharacterDataLatin1.instance.isIdeographic(ch);
    }

    public static boolean isTitleCase(char ch) {
        return CharacterDataLatin1.instance.getType(ch) == CharacterData.TITLECASE_LETTER;
    }

    public static boolean isAlphabetic(char ch) {
        return (((((1 << CharacterData.UPPERCASE_LETTER) |
                (1 << CharacterData.LOWERCASE_LETTER) |
                (1 << CharacterData.TITLECASE_LETTER) |
                (1 << CharacterData.MODIFIER_LETTER) |
                (1 << CharacterData.OTHER_LETTER) |
                (1 << CharacterData.LETTER_NUMBER)) >> CharacterDataLatin1.instance.getType(ch)) & 1) != 0) ||
                CharacterDataLatin1.instance.isOtherAlphabetic(ch);
    }

    public static interface Notification {

    }

    public static interface RetryNotification extends Notification {
        public void notifyRetryIsInProgress(int retry);
    }

    public static interface BackupServerNotification extends Notification {
        public void notifyBackupInUse(boolean backup);
    }

    public static interface HasCachedCopyNotification extends Notification {
        public void onCachedCopyObtained(ArrayList<JuickMessage> messages);
    }

    public static interface DownloadProgressNotification extends Notification {
        public void notifyDownloadProgress(int progressBytes);
    }

    public static interface DownloadErrorNotification extends Notification {
        public void notifyDownloadError(String error);
    }

    public static RESTResponse streamToString(InputStream is, Notification progressNotification) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            long l = System.currentTimeMillis();
            byte[] buf = new byte[1024];
            while (true) {
                int len = is.read(buf);
                if (len <= 0) break;
                baos.write(buf, 0, len);
                if (System.currentTimeMillis() - l > 100) {
                    l = System.currentTimeMillis();
                    if (progressNotification instanceof DownloadProgressNotification)
                        ((DownloadProgressNotification) progressNotification).notifyDownloadProgress(baos.size());
                }
            }
            if (progressNotification instanceof DownloadProgressNotification)
                ((DownloadProgressNotification) progressNotification).notifyDownloadProgress(baos.size());
            return new RESTResponse(null, false, new String(baos.toByteArray(), "UTF-8"));
        } catch (Exception e) {
            if (progressNotification instanceof DownloadErrorNotification)
                ((DownloadErrorNotification) progressNotification).notifyDownloadError(e.toString());
            return new RESTResponse(e.toString(), true, null);
        }
    }


    public static abstract class Function<T, A> {
        public int retryCount = 0;
        public abstract T apply(A a);
    }

    public static char forDigit(int digit, int radix) {
        if ((digit >= radix) || (digit < 0)) {
            return '\0';
        }
        if ((radix < Character.MIN_RADIX) || (radix > Character.MAX_RADIX)) {
            return '\0';
        }
        if (digit < 10) {
            return (char)('0' + digit);
        }
        return (char)('a' - 10 + digit);
    }

    public static String replace(String src, String target, String replacement) {
        return Pattern.compile(target, Pattern.LITERAL).matcher(
                src).replaceAll(Matcher.quoteReplacement(replacement));
    }

    public static String replaceFirst(String src, String regex, String replacement) {
        return Pattern.compile(regex).matcher(src).replaceFirst(replacement);
    }



}
