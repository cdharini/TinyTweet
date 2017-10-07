package com.projects.cdharini.tinytweet.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by dharinic on 9/27/17.
 */
@Parcel
public class User {

    String mName;
    long mId;
    String mScreenName;
    String mProfilePicUrl;
    int mFollowersCount;
    int mFollowingCount;
    String mDescription;

    public User() {

    }

    public static User fromJson(JSONObject obj) throws JSONException {
        User u = new User();
        u.mName = obj.getString("name");
        u.mScreenName = obj.getString("screen_name");
        u.mId = obj.getLong("id");
        u.mProfilePicUrl = obj.getString("profile_image_url");
        u.mFollowersCount = obj.getInt("followers_count");
        u.mFollowingCount = obj.getInt("friends_count");
        u.mDescription = obj.getString("description");
        return u;
    }
    public String getName() {
        return mName;
    }

    public long getId() {
        return mId;
    }

    public String getScreenName() {
        return mScreenName;
    }

    public String getProfilePicUrl() {
        return mProfilePicUrl;
    }

    public int getFollowersCount() {
        return mFollowersCount;
    }

    public int getFollowingCount() {
        return mFollowingCount;
    }

    public String getDescription() {
        return mDescription;
    }
}
