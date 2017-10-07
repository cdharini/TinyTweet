package com.projects.cdharini.tinytweet.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.projects.cdharini.tinytweet.R;
import com.projects.cdharini.tinytweet.fragments.TimelineFragment;
import com.projects.cdharini.tinytweet.models.User;
import com.projects.cdharini.tinytweet.utils.TinyTweetConstants;

import org.parceler.Parcels;


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

        //get views
        //set the values there
    }

}
