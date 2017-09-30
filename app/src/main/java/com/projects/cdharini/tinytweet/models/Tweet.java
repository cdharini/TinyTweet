package com.projects.cdharini.tinytweet.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by dharinic on 9/27/17.
 */
@Parcel
public class Tweet {

    User mUser;
    String mText;
    String mCreatedAt;
    long mUid;

    int mNumRetweets;
    int mNumLikes;


    public Tweet() {

    }

    public static Tweet fromJson(JSONObject obj) throws JSONException {
        Tweet t = new Tweet();
        t.mUser = User.fromJson(obj.getJSONObject("user"));
        t.mText = obj.getString("text");
        t.mCreatedAt = obj.getString("created_at");
        t.mUid = obj.getLong("id");
        t.mNumRetweets = obj.getInt("retweet_count");
        if (obj.has("favorite_count")) {
            t.mNumLikes = obj.getInt("favorite_count");
        } else {
            t.mNumLikes = 0;
        }
        return t;
    }

    public static List<Tweet> fromJsonArray(JSONArray arr) {
        List<Tweet> tweetList = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            try {
                tweetList.add(Tweet.fromJson(arr.getJSONObject(i)));
            } catch (JSONException e) {
                Log.d(TAG, "Couldn't parse tweet!");
            }
        }
        return tweetList;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public User getUser() {
        return mUser;
    }

    public String getText() {
        return mText;
    }

    public long getUid() {
        return mUid;
    }

    public int getNumRetweets() {
        return mNumRetweets;
    }

    public int getNumLikes() {
        return mNumLikes;
    }
}
