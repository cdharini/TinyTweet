package com.projects.cdharini.tinytweet.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.projects.cdharini.tinytweet.R;
import com.projects.cdharini.tinytweet.databinding.ActivityTweetDetailBinding;
import com.projects.cdharini.tinytweet.models.Tweet;
import com.projects.cdharini.tinytweet.utils.TinyTweetConstants;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class TweetDetailActivity extends AppCompatActivity {
    ActivityTweetDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tweet_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Tweet tweet = getIntent().getParcelableExtra(TinyTweetConstants.EXTRA_TWEET);
        populateTweetIntoView(tweet);
    }

    private void populateTweetIntoView(Tweet tweet) {
        binding.tvScreenNameDetail.setText(tweet.getUser().getScreenName());
        binding.tvUserNameDetail.setText(tweet.getUser().getName());
        binding.tvTweetDetail.setText(tweet.getText());
        binding.tvTweetDateDetail.setText(tweet.getCreatedAt());
        Glide.with(this).load(tweet.getUser().getProfilePicUrl())
                .bitmapTransform(new CropCircleTransformation(this))
                .error(R.drawable.ic_error_outline_black_24dp).into(binding.ivProfileDetailTweet);

    }
}
