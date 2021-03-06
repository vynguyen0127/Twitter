package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Parcel
public class Tweet {

    public String body;
    public String createdAt;
    public User user;
    public String mediaURL;
    public String id;
    private boolean liked;
    private boolean retweeted;


    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static final String TAG = "Tweet";

    public Tweet(){
        retweeted = false;
        liked = false;
    };

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        try{
            tweet.body = jsonObject.getString("full_text");
        }catch(JSONException e){
            tweet.body = jsonObject.getString("text");
        }
//        tweet.body = jsonObject.getString("full_text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.id = jsonObject.getString("id_str");
        JSONObject obj = jsonObject.getJSONObject("entities");

        if(obj.has("media")){
            JSONArray media = obj.getJSONArray("media");
            JSONObject obj2 = media.getJSONObject(0);
            Log.i(TAG, media.toString());
            tweet.mediaURL = obj2.getString("media_url_https");
            Log.i(TAG, tweet.mediaURL);
        }
        else{
            tweet.mediaURL = "";
            Log.i(TAG, "does not contain media");
        }

        return tweet;
    }

    public static List<Tweet> fromJSONArray(JSONArray jsonArray) throws JSONException{
        List<Tweet> tweets = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }

    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        try {
            long time = sf.parse(rawJsonDate).getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " m";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " h";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + " d";
            }
        } catch (ParseException e) {
            Log.i(TAG, "getRelativeTimeAgo failed");
            e.printStackTrace();
        }

        return "";
    }
    public void setLiked(boolean status){
        liked = status;
    }
    public boolean getLiked(){
        return liked;
    }

    public void setRetweeted(boolean status){
        retweeted = status;
    }
    public boolean getRetweeted(){
        return retweeted;
    }


}
