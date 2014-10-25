package com.juickadvanced.protocol;

import com.juickadvanced.IHTTPClient;
import com.juickadvanced.RESTResponse;
import com.juickadvanced.Utils;
import com.juickadvanced.data.MessageID;
import com.juickadvanced.data.facebook.FacebookMessage;
import com.juickadvanced.data.facebook.FacebookMessageID;
import com.juickadvanced.data.facebook.FacebookUser;
import com.juickadvanced.data.juick.JuickMessage;
import com.juickadvanced.lang.MD5Digest;
import com.juickadvanced.lang.URLDecoder;
import com.juickadvanced.parsers.URLParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by san on 8/5/14.
 */
@SuppressWarnings("IndexOfReplaceableByContains")
public class FacebookTransport {

    public static final String secureConstant = "62f"+"8ce9f"+"74b12f"+"84c123c"+"c2343"+"7a4"+"a32";
    public static final String apikey = "882"+"a849036"+"1da9870"+"2bf97"+"a021"+"ddc"+"14d";
    public static final String hex = "0123456789abcdef";

    public String oauth;
    IHTTPClient client;

    public FacebookTransport(IHTTPClient client) {
        this.client = client;
    }

    public RESTResponse performLogin(String login, String password) throws IOException, JSONException {
        // HttpClient client = new DefaultHttpClient();
        try {
            client.setURL("POST", "https://b-api.facebook.com/method/auth.login");
            String part1 = "api_key="+apikey+"&client_country_code=UA&credentials_type=password&email="+login+"&error_detail_type=button_with_disabled&fb_api_caller_class=com.facebook.auth.protocol.AuthenticateMethod&fb_api_req_friendly_name=authenticate&format=json&generate_machine_id=1&generate_session_cookies=1&locale=en_US&method=auth.login&";
            String part1sub = Utils.replace(part1, "&","")+"password="+password + secureConstant;
            MD5Digest md5 = new MD5Digest();
            byte[] bytes = part1sub.getBytes();
            md5.update(bytes, 0, bytes.length);
            byte[] digest = new byte[16];
            md5.doFinal(digest, 0);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < digest.length; i++) {
                byte b = digest[i];
                sb.append(hex.charAt((b >> 4) & 0xF));
                sb.append(hex.charAt((b) & 0xF));
            }
            part1 = part1 + "password="+password + "&sig="+sb.toString();
            part1 = Utils.replace(part1, "@", "%40");
            client.setURLEncodedPostData(part1);
            client.addHeader("User-Agent", getUserAgent());
            IHTTPClient.Response content = client.execute();
            RESTResponse restResponse = Utils.streamToString(content.getStream(), null);
            JSONObject response = new JSONObject(restResponse.getResult());
            if (response.has("error_msg")) {
                return new RESTResponse(response.getString("error_msg"), false, null);
            } else {
                oauth = response.getString("access_token");
                return new RESTResponse(null, false, "Login ok");
            }
        } finally {
            client.terminate();
        }
    }

    public static ArrayList<JuickMessage> parseJsonPure(JSONObject jo) throws JSONException {
        ArrayList<JuickMessage> retval = new ArrayList<JuickMessage>();
        JSONArray edges = jo.getJSONObject("viewer").getJSONObject("news_feed").getJSONArray("edges");
        for(int i=0; i<edges.length(); i++) {
            try {
                JSONObject edge = edges.getJSONObject(i);
                JSONObject node = edge.getJSONObject("node");
                if (node.has("pymk_items") && node.get("pymk_items") != JSONObject.NULL) continue;
                FacebookMessage msg = new FacebookMessage();
                StringBuilder sb = new StringBuilder();
                JSONObject actor = node.getJSONArray("actors").getJSONObject(0);
                FacebookUser fbuser = new FacebookUser();
                msg.User = fbuser;
                msg.User.UID = Long.parseLong(actor.getString("id"));
                msg.User.FullName = actor.getString("name");
                msg.User.UName = msg.User.FullName;
                String messageText = null;
                msg.Timestamp = new Date(node.getLong("creation_time")*1000L);
                if (actor.has("profile_picture") && actor.get("profile_picture") != JSONObject.NULL) {
                    fbuser.avatarUrl = actor.getJSONObject("profile_picture").getString("uri");
                }
                if (node.has("attached_story") && node.get("attached_story") != JSONObject.NULL) {
                    if (node.has("title") && node.get("title") != JSONObject.NULL) {
                        JSONObject message = node.getJSONObject("title");
                        String text = message.getString("text");
                        sb.append(text + "\n>>>>>>\n");
                    }
                    node = node.getJSONObject("attached_story");
                    actor = node.getJSONArray("actors").getJSONObject(0);
                    String actorName = actor.getString("name");
                    sb.append("@"+actorName+":\n");
                }

                if (node.has("url") && node.get("url") != JSONObject.NULL) {
                    String id = new URLParser(node.getString("url")).getArgsMap().get("story_fbid");
                    msg.setMID(new FacebookMessageID(id));
                } else {
    //                String id = node.getString("id");
    //                msg.setMID(new FacebookMessageID(id));
                    continue;       // user likes some group
                }

                if (node.has("message") && node.get("message") != JSONObject.NULL) {
                    JSONObject message = node.getJSONObject("message");
                    String text = message.getString("text");
                    messageText = text;
                    sb.append(text + "\n");
                }
                JSONArray attachments = node.getJSONArray("attachments");
                for (int j = 0; j < attachments.length(); j++) {
                    sb.append(">>>>>>\n");
                    JSONObject attachment = attachments.getJSONObject(j);
                    String title = attachment.getString("title");
                    sb.append("*"+title + "*\n");
                    if (attachment.get("description") != JSONObject.NULL) {
                        String text = attachment.getJSONObject("description").getString("text");
                        if (messageText != null &&
                                text.substring(0, Math.min(20, text.length())).equals(messageText.substring(0, Math.min(20, messageText.length())))) {
                            // do nothing
                        } else{
                            sb.append(text + "\n");
                        }
                    }
                    if (attachment.has("url")) {
                        String url = attachment.getString("url");
                        if (url.indexOf("m.facebook.com/l.php") >= 0) {
                            URLParser parser = new URLParser(url);
                            url = parser.getArgsMap().get("u");
                            url = URLDecoder.decode(url);
                        }
                        if (title.equals("Timeline Photos") || url.indexOf("https://m.facebook.com/photo.php") >=0 ) {
                            url = attachment.getJSONObject("media").getJSONObject("image_large_aspect").getString("uri");
                        }
                        if (url.startsWith("http")) {
                            sb.append("URL: " + url + "\n");
                        }
                    }
                }
                if (node.has("feedback") && node.get("feedback") != JSONObject.NULL) {
                    JSONObject feedback = node.getJSONObject("feedback");
                    msg.replies = (int)feedback.getJSONObject("comments").getLong("count");
                    long likers = feedback.getJSONObject("likers").getLong("count");
                    if (likers > 0) {
                        sb.append("❤" + likers + "\n");
                    }
                    String feedbackId = feedback.getString("id");
                    ((FacebookMessageID) msg.getMID()).feedbackId = feedbackId;
                    msg.likers = likers;
                }
                if (sb.length() > 0 && sb.charAt(sb.length()-1) == '\n') {
                    sb.setLength(sb.length()-1);
                }
                msg.Text = sb.toString();

                retval.add(msg);
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
        return retval;
    }

    public static String getUserAgent() {
        return "[FBAN/FB4A;FBAV/11.0.0.11.23;FBBV/3002847;FBDM/{density=1.5,width=480,height=800};FBLC/en_US;FBCR/SU-LENINCELL;FBPN/com.facebook.katana;FBDV/FSB-CELLTEL;FBSV/4.2.2;FBOP/1;FBCA/armeabi-v7a:armeabi;]";
    }

    long queryId;

    public JuickMessage parseCommentPure(MessageID mid, JSONObject comment, int rid) throws JSONException {
        try {
            FacebookMessage msg = new FacebookMessage();
            msg.setMID(mid);
            msg.setRID(rid);
            msg.Text = comment.getJSONObject("body").getString("text");
            JSONObject feedback = comment.getJSONObject("feedback");
            long likers = feedback.getJSONObject("likers").getLong("count");
            if (likers > 0) {
                msg.Text += (" ♥" + likers);
            }
            FacebookUser fbuser = new FacebookUser();
            msg.User = fbuser;
            JSONObject author = comment.getJSONObject("author");
            msg.User.UID = Long.parseLong(author.getString("id"));
            msg.User.FullName = author.getString("name");
            msg.User.UName = msg.User.FullName;
            msg.Timestamp = new Date(comment.getLong("created_time")*1000L);
            msg.likers = likers;
            if (author.has("profile_picture") && author.get("profile_picture") != JSONObject.NULL) {
                fbuser.avatarUrl = author.getJSONObject("profile_picture").getString("uri");
            }
            msg.microBlogCode = FacebookMessageID.CODE;
            return msg;
        } catch(Exception ex) {
            return null;
        }
    }

    public class Feed {
        public JSONObject part;

        Feed(JSONObject part) {
            this.part = part;
        }
    }

    public interface ReauthCallback {
        boolean reauthorizeFacebook(FacebookTransport transport, Utils.Notification notifications);
    }

    public static ReauthCallback reauthCallback;

    public Feed getFirstHomeFeed(Utils.Notification notifications) throws IOException, JSONException {
        try {
            while(true) {
                createGraphqlRequest();
                queryId = 10152793363801729L;
                String request = "query_id="+10152793363801729L+"&method=get&query_params={\"action_location\":\"feed\",\"ad_media_type\":\"image/x-auto\",\"angora_attachment_cover_image_size\":\"720\",\"angora_attachment_profile_image_size\":\"120\",\"celebrations_profile_pic_size_param\":\"120\",\"collections_rating_pic_size_param\":\"120\",\"creative_high_img_size\":\"960\",\"creative_low_img_size\":\"320\",\"creative_med_img_size\":\"480\",\"debug_mode\":\"PRODUCTION\",\"default_image_scale\":\"1.5\",\"discovery_image_size\":\"90\",\"enable_comment_replies\":\"false\",\"first_home_story_param\":\"10\",\"friends_nearby_profile_pic_size_param\":\"80\",\"gysj_facepile_count_param\":\"4\",\"gysj_facepile_size_param\":\"60\",\"gysj_size_param\":\"90\",\"image_high_height\":\"2048\",\"image_high_width\":\"480\",\"image_large_aspect_height\":\"248\",\"image_large_aspect_width\":\"480\",\"image_low_height\":\"2048\",\"image_low_width\":\"120\",\"image_medium_height\":\"2048\",\"image_medium_width\":\"240\",\"likers_profile_image_size\":\"80\",\"media_type\":\"image/jpeg\",\"mobile_zero_upsell_size_param\":\"120\",\"num_faceboxes_and_tags\":\"50\",\"orderby_home_story_param\":\"most_recent\",\"page_review_cover_photo_size_param\":\"720\",\"place_star_survey_item_size_param\":\"120\",\"prefetch_chaining_enabled_param\":\"false\",\"presence_profile_pic_size_param\":\"60\",\"profile_image_size\":\"60\",\"profile_pic_media_type\":\"image/x-auto\",\"profile_pic_swipe_size_param\":\"320\",\"pymk_size_param\":\"320\",\"pyml_size_param\":\"80\",\"refresh_mode_param\":\"auto\",\"saved_item_pic_height\":\"320\",\"saved_item_pic_width\":\"480\",\"size_style\":\"contain-fit\"}&locale=en_US&client_country_code=SU&fb_api_req_friendly_name=NewsFeedQueryDepth2&fb_api_caller_class=com.facebook.feed.protocol.FetchNewsFeedMethod";
                client.setURLEncodedPostData(request);
                IHTTPClient.Response content = client.execute();
                RESTResponse restResponse = Utils.streamToString(content.getStream(), notifications);
                String result = restResponse.getResult();
                JSONObject response = new JSONObject(result);
                if (response.has("error")) {
                    JSONObject error = response.getJSONObject("error");
                    if (error.has("message") && error.getString("message").indexOf("Invalid OAuth access token") != -1) {
                        if (reauthCallback != null) {
                            if (reauthCallback.reauthorizeFacebook(this, notifications)) {
                                continue;
                            }
                        }
                    }
                }
                return new Feed(response);
            }
        } finally {
            client.terminate();
        }
    }

    public Feed getNextHomeFeed(Utils.Notification notifications, String endCursor) throws IOException, JSONException {
        try {
            createGraphqlRequest();
            String request = "query_id=10152793363801729&method=get&query_params={\"action_location\":\"feed\",\"ad_media_type\":\"image/webp\",\"after_home_story_param\":\""+endCursor+"\",\"angora_attachment_cover_image_size\":\"720\",\"angora_attachment_profile_image_size\":\"120\",\"celebrations_profile_pic_size_param\":\"120\",\"collections_rating_pic_size_param\":\"120\",\"creative_high_img_size\":\"960\",\"creative_low_img_size\":\"320\",\"creative_med_img_size\":\"480\",\"debug_mode\":\"PRODUCTION\",\"default_image_scale\":\"1.5\",\"discovery_image_size\":\"90\",\"enable_comment_replies\":\"false\",\"first_home_story_param\":\"10\",\"friends_nearby_profile_pic_size_param\":\"80\",\"gysj_facepile_count_param\":\"4\",\"gysj_facepile_size_param\":\"60\",\"gysj_size_param\":\"90\",\"image_high_height\":\"2048\",\"image_high_width\":\"480\",\"image_large_aspect_height\":\"248\",\"image_large_aspect_width\":\"480\",\"image_low_height\":\"2048\",\"image_low_width\":\"120\",\"image_medium_height\":\"2048\",\"image_medium_width\":\"240\",\"likers_profile_image_size\":\"80\",\"media_type\":\"image/jpeg\",\"mobile_zero_upsell_size_param\":\"120\",\"num_faceboxes_and_tags\":\"50\",\"orderby_home_story_param\":\"most_recent\",\"page_review_cover_photo_size_param\":\"720\",\"place_star_survey_item_size_param\":\"120\",\"prefetch_chaining_enabled_param\":\"false\",\"presence_profile_pic_size_param\":\"60\",\"profile_image_size\":\"60\",\"profile_pic_media_type\":\"image/jpeg\",\"profile_pic_swipe_size_param\":\"320\",\"pymk_size_param\":\"320\",\"pyml_size_param\":\"80\",\"saved_item_pic_height\":\"320\",\"saved_item_pic_width\":\"480\",\"size_style\":\"contain-fit\"}&locale=en_US&client_country_code=SU&fb_api_req_friendly_name=NewsFeedQueryDepth2&fb_api_caller_class=com.facebook.feed.protocol.FetchNewsFeedMethod";
            client.setURLEncodedPostData(request);
            IHTTPClient.Response content = client.execute();
            RESTResponse restResponse = Utils.streamToString(content.getStream(), notifications);
            JSONObject response = new JSONObject(restResponse.getResult());
            return new Feed(response);
        } finally {
            client.terminate();
        }
    }

    static class MyChainedNotification implements Utils.DownloadProgressNotification, Utils.DownloadErrorNotification {

        int cumulative = 0;
        int lastProgress = 0;
        Utils.Notification chained;

        MyChainedNotification(Utils.Notification chained) {
            this.chained = chained;
        }

        @Override
        public void notifyDownloadProgress(int progressBytes) {
            if (progressBytes > lastProgress) {
                cumulative += progressBytes - lastProgress;
                lastProgress = progressBytes;
            } else {
                cumulative += progressBytes;
                lastProgress = progressBytes;
            }
            if (chained instanceof Utils.DownloadProgressNotification) {
                ((Utils.DownloadProgressNotification)chained).notifyDownloadProgress(cumulative);
            }
        }

        @Override
        public void notifyDownloadError(String error) {
            if (chained instanceof Utils.DownloadErrorNotification) {
                ((Utils.DownloadErrorNotification)chained).notifyDownloadError(error);
            }
        }
    }

    public JSONArray getChildren(Utils.Notification notifications, String feedbackId) throws IOException, JSONException {
        MyChainedNotification chained = new MyChainedNotification(notifications);
        try {
            createGraphqlRequest();
            String request = "query_id=10152777407321729&method=get&query_params={\"angora_attachment_cover_image_size\":\"720\",\"angora_attachment_profile_image_size\":\"120\",\"enable_comment_replies\":\"false\",\"feedback_id\":[\""+feedbackId+"\"],\"image_high_height\":\"2048\",\"image_high_width\":\"480\",\"image_low_height\":\"2048\",\"image_low_width\":\"120\",\"image_medium_height\":\"2048\",\"image_medium_width\":\"240\",\"likers_profile_image_size\":\"80\",\"max_comments\":\"25\",\"max_likers\":\"25\",\"media_type\":\"image/jpeg\",\"profile_image_size\":\"60\",\"profile_pic_media_type\":\"image/jpeg\",\"size_style\":\"contain-fit\"}&locale=en_US&client_country_code=UA&fb_api_req_friendly_name=StaticFeedbackBatchQuery&fb_api_caller_class=com.facebook.graphql.model.GraphQLFeedback";
            client.setURLEncodedPostData(request);
            IHTTPClient.Response content = client.execute();
            InputStream strm = content.getStream();
            RESTResponse restResponse = Utils.streamToString(strm, chained);
            strm.close();
            JSONObject response = new JSONObject(restResponse.getResult());
            JSONObject root = response.getJSONObject(feedbackId);
            JSONObject comments = root.getJSONObject("comments");
            JSONArray rootNodes = comments.getJSONArray("nodes");
            String endCursor = comments.getJSONObject("page_info").getString("end_cursor");
            while(comments.getJSONObject("page_info").getBoolean("has_next_page")) {
                createGraphqlRequest();
                request = "query_id=10152777407296729&method=get&query_params={\"after_comments\":\""+endCursor+"\",\"angora_attachment_cover_image_size\":\"720\",\"angora_attachment_profile_image_size\":\"120\",\"enable_comment_replies\":\"false\",\"feedback_id\":\""+feedbackId+"\",\"image_high_height\":\"2048\",\"image_high_width\":\"480\",\"image_low_height\":\"2048\",\"image_low_width\":\"120\",\"image_medium_height\":\"2048\",\"image_medium_width\":\"240\",\"likers_profile_image_size\":\"80\",\"max_comments\":\"25\",\"media_type\":\"image/jpeg\",\"profile_image_size\":\"60\",\"profile_pic_media_type\":\"image/jpeg\",\"size_style\":\"contain-fit\"}&locale=en_US&client_country_code=UA&fb_api_req_friendly_name=UFIFeedbackQuery&fb_api_caller_class=com.facebook.graphql.model.GraphQLFeedback";
                client.setURLEncodedPostData(request);
                IHTTPClient.Response resp = client.execute();
                InputStream stream = resp.getStream();
                restResponse = Utils.streamToString(stream, chained);
                stream.close();
                response = new JSONObject(restResponse.getResult());
                JSONObject smallPart = response.getJSONObject(feedbackId);
                comments = smallPart.getJSONObject("comments");
                JSONArray nodes = comments.getJSONArray("nodes");
                for(int i=0; i<nodes.length(); i++) {
                    rootNodes.put(nodes.getJSONObject(i));
                }
                endCursor = comments.getJSONObject("page_info").getString("end_cursor");
            }
            return rootNodes;
        } finally {
            client.terminate();
        }
    }


    private void createGraphqlRequest() {
        client.setURL("POST", "https://graph.facebook.com/graphql");
        client.addHeader("Authorization", "OAuth " + oauth);
        client.addHeader("X-FB-Connection-Type", "WIFI");
        client.addHeader("User-Agent", getUserAgent());
    }
}
