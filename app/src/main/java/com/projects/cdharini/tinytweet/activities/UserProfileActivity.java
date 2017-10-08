package com.projects.cdharini.tinytweet.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.projects.cdharini.tinytweet.R;
import com.projects.cdharini.tinytweet.fragments.TimelineFragment;
import com.projects.cdharini.tinytweet.models.User;
import com.projects.cdharini.tinytweet.utils.TinyTweetConstants;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


public class UserProfileActivity extends AppCompatActivity implements TimelineFragment.LoadCompleteListener{

    private User mUser;
    MenuItem miActionProgressItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mUser = Parcels.unwrap(getIntent().getParcelableExtra(TinyTweetConstants.EXTRA_USER));

        if (savedInstanceState == null) {
            TimelineFragment frag = TimelineFragment.newInstance(
                    TinyTweetConstants.USER_TIMELINE, mUser.getId());

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

        setTitle(mUser.getName() + "'s Timeline");
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        // Extract the action-view from the menu item
        ProgressBar v =  (ProgressBar) MenuItemCompat.getActionView(miActionProgressItem);
        // Return to finish
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    public void showProgressBar() {
        // Show progress item
        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        miActionProgressItem.setVisible(false);
    }

    @Override
    public void onLoadComplete() {
        hideProgressBar();
    }

    @Override
    public void onLoadStart() {
        showProgressBar();
    }
}
