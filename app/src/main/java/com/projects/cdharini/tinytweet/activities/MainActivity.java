package com.projects.cdharini.tinytweet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.projects.cdharini.tinytweet.R;
import com.projects.cdharini.tinytweet.TinyTweetApplication;
import com.projects.cdharini.tinytweet.adapters.MainFragmentPagerAdapter;
import com.projects.cdharini.tinytweet.fragments.ComposeTweetFragment;
import com.projects.cdharini.tinytweet.fragments.TimelineFragment;
import com.projects.cdharini.tinytweet.models.Tweet;
import com.projects.cdharini.tinytweet.models.User;
import com.projects.cdharini.tinytweet.utils.TinyTweetConstants;

import org.parceler.Parcels;


public class MainActivity extends AppCompatActivity
        implements ComposeTweetFragment.ComposeTweetDialogListener, TimelineFragment.LoadCompleteListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private MainFragmentPagerAdapter mViewPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    MenuItem miActionProgressItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mViewPagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mViewPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                ComposeTweetFragment fragment = ComposeTweetFragment.newInstance();
                fragment.show(fm, "fragment_compose_tweet");
            }
        });

       setTitle("TinyTweet");

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        showProgressBar();
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


    @Override
    public void onTweetPosted(Tweet t) {
        // tell home timeline fragment to add new tweet to timeline
        TimelineFragment frag = (TimelineFragment)
                mViewPagerAdapter.getRegisteredFragment(TinyTweetConstants.HOME_TIMELINE);
        frag.addTweetToTimeline(t);
    }

    /**
     * Launch profile of user
     * @param view
     */
    public void onProfileClick(View view) {
        Intent intent = new Intent(this, UserProfileActivity.class);
        User user = TinyTweetApplication.getLoggedInUser();
        intent.putExtra(TinyTweetConstants.EXTRA_USER, Parcels.wrap(user));
        this.startActivity(intent);

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
