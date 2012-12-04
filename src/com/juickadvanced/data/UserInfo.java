package com.juickadvanced.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: san
 * Date: 12/4/12
 * Time: 8:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserInfo implements Serializable {
    public String fullName;
    ArrayList<String> extraInfo;

    public static final String INFO_FIRST_MESSAGE = "First activity";
    public static final String INFO_LAST_MESSAGE = "Last activity";
    public static final String INFO_MESSAGES_TOTAL = "Total message";
    public static final String INFO_COMMENTS_TOTAL = "Total comments";
    public static final String INFO_WEEK_ACTIVITY = "Last week msg/comm";
    public static final String INFO_AVG_COMMENTS_POST = "Avg comments/post";
    public static final String INFO_AVG_POSTS_COMMENTED = "Avg posts commented";
    public static final String INFO_TOP_COMMENTERS = "They comm. user:";
    public static final String INFO_TOP_COMMENTING = "User comm. them: ";
    public static final String INFO_AVG_POST_LENGTH = "Avg post len, words";
    public static final String INFO_AVG_COMMENT_LENGTH = "Avg comment len, words";

    public static HashMap<String,String> translations = new HashMap<String, String>();
    static {
        translations.put(INFO_FIRST_MESSAGE, "Первое появление");
        translations.put(INFO_LAST_MESSAGE, "Последнее появление");
        translations.put(INFO_MESSAGES_TOTAL, "Всего мессаг");
        translations.put(INFO_COMMENTS_TOTAL, "Всего комментов");
        translations.put(INFO_WEEK_ACTIVITY, "Активность за неделю, мессаг/комментов");
        translations.put(INFO_AVG_COMMENTS_POST, "Среднее: комментов/пост");
        translations.put(INFO_AVG_POSTS_COMMENTED, "Среднее: постов с комментами");
        translations.put(INFO_TOP_COMMENTERS, "Его комментаторы");
        translations.put(INFO_TOP_COMMENTING, "Комментирует их");
        translations.put(INFO_AVG_POST_LENGTH, "Среднее: длина поста, слов");
        translations.put(INFO_AVG_COMMENT_LENGTH, "Среднее: длина коммента, слов");
    }

    public UserInfo(String fullName, ArrayList<String> extraInfo) {
        this.fullName = fullName;
        this.extraInfo = extraInfo;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public ArrayList<String> getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(ArrayList<String> extraInfo) {
        this.extraInfo = extraInfo;
    }
}
