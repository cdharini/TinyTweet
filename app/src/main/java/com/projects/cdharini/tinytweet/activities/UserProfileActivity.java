package com.projects.cdharini.tinytweet.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.projects.cdharini.tinytweet.R;
import com.projects.cdharini.tinytweet.fragments.TimelineFragment;
import com.projects.cdharini.tinytweet.models.User;
import com.projects.cdharini.tinytweet.utils.TinyTweetConstants;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


public class UserProfileActivity extends AppCompatActivity {

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mUser = Parcels.unwrap(getIntent().getParcelableExtra(TinyTweetConstants.EXTRA_USER));

        if (savedInstanceState == null) {
            TimelineFragment frag = TimelineFragment.newInstance(2, mUser.getId());

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, frag);
            ft.commit();
        }

        TextView tvUserName = (TextView) findViewById(R.id.tvUserName);
        TextView tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowers = (TextView) findViewById(R.id.tvNumFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvNumFollowing);
        ImageView ivProfile = (ImageView) findViewById(R.id.ivProfilePic);

        tvUserName.setText(mUser.getName());
        tvScreenName.setText("@" + mUser.getScreenName());
        tvFollowers.setText(String.valueOf(mUser.getFollowersCount()));
        tvFollowing.setText(String.valueOf(mUser.getFollowingCount()));
        tvTagline.setText(mUser.getDescription());


        Glide.with(this).load(mUser.getProfilePicUrl())
                .bitmapTransform(new CropCircleTransformation(this))
                .error(R.drawable.ic_error_outline_black_24dp).into(ivProfile);
    }

}
