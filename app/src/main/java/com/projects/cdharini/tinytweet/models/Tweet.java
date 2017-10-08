package com.projects.cdharini.tinytweet.models;

import android.util.Log;

import com.projects.cdharini.tinytweet.MyDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

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
@Table(database = MyDatabase.class)
@Parcel(analyze={Tweet.class})
public class Tweet extends BaseModel{

    @Column
    @ForeignKey(saveForeignKeyModel = true)
    User mUser;

    @Column
    String mText;

    @Column
    String mCreatedAt;

    @Column
    @PrimaryKey
    long mUid;

    @Column
    int mNumRetweets;

    @Column
    int mNumLikes;

    @Column
    boolean mFavorited;

    @Column
    boolean mRetweeted;


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
        t.mFavorited = obj.getBoolean("favorited");
        t.mRetweeted = obj.getBoolean("retweeted");
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


    public boolean isFavorited() {
        return mFavorited;
    }

    public boolean isRetweeted() {
        return mRetweeted;
    }
}
