package com.projects.cdharini.tinytweet.models;

import com.projects.cdharini.tinytweet.MyDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by dharinic on 9/27/17.
 */
@Table(database = MyDatabase.class)
@Parcel(analyze={User.class})
public class User extends BaseModel{

    @Column
    String mName;

    @Column
    @PrimaryKey
    long mId;

    @Column
    String mScreenName;

    @Column
    String mProfilePicUrl;

    @Column
    int mFollowersCount;

    @Column
    int mFollowingCount;

    @Column
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
