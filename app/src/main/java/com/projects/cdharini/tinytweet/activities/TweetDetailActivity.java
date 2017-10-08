package com.projects.cdharini.tinytweet.activities;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bumptech.glide.Glide;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.projects.cdharini.tinytweet.R;
import com.projects.cdharini.tinytweet.TinyTweetApplication;
import com.projects.cdharini.tinytweet.databinding.ActivityTweetDetailBinding;
import com.projects.cdharini.tinytweet.fragments.ReplyFragment;
import com.projects.cdharini.tinytweet.models.Tweet;
import com.projects.cdharini.tinytweet.networking.TwitterClient;
import com.projects.cdharini.tinytweet.utils.TinyTweetConstants;
import com.projects.cdharini.tinytweet.utils.TinyTweetUtils;

import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class TweetDetailActivity extends AppCompatActivity {
    ActivityTweetDetailBinding binding;
    Tweet mTweet;
    TwitterClient mTwitterClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tweet_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setLogo(R.drawable.ic_if_twitter_291710);
        mTweet = Parcels.unwrap(getIntent().getParcelableExtra(TinyTweetConstants.EXTRA_TWEET));
        mTwitterClient = TinyTweetApplication.getRestClient();
        populateTweetIntoView(mTweet);
    }

    private void populateTweetIntoView(Tweet tweet) {
        binding.tvScreenNameDetail.setText("@" + tweet.getUser().getScreenName());
        binding.tvUserNameDetail.setText(tweet.getUser().getName());
        binding.tvTweetDetail.setText(tweet.getText());
        binding.tvTweetDateDetail.setText(
                TinyTweetUtils.getDateStringForDetailTweet(tweet.getCreatedAt()));
        Glide.with(this).load(tweet.getUser().getProfilePicUrl())
                .bitmapTransform(new CropCircleTransformation(this))
                .error(R.drawable.ic_error_outline_black_24dp).into(binding.ivProfileDetailTweet);
        binding.tvNumLikes.setText(String.valueOf(tweet.getNumLikes()));
        binding.tvNumRetweets.setText(String.valueOf(tweet.getNumRetweets()));
        binding.ibReply.setOnClickListener(v -> {
            FragmentManager fm = getSupportFragmentManager();
            ReplyFragment fragment = ReplyFragment.newInstance(
                    tweet.getUser().getScreenName(), tweet.getUid());
            fragment.show(fm, "fragment_reply_tweet");
        });

        if (tweet.isFavorited()) {
            setImageSelectedColor(binding.ivFavorite.getDrawable(),
                    ContextCompat.getColor(this, R.color.colorAppRed));
        } else {
            setImageUnSelectedColor(binding.ivFavorite.getDrawable());
        }
        if (tweet.isRetweeted()) {
            setImageSelectedColor(binding.ivRetweet.getDrawable());
        } else {
            setImageUnSelectedColor(binding.ivRetweet.getDrawable());
        }

    }

    public void setImageSelectedColor(Drawable d, int c) {
        DrawableCompat.setTint(d, c);

    }

    public void setImageSelectedColor(Drawable d) {
        setImageSelectedColor(d,
                ContextCompat.getColor(TweetDetailActivity.this, R.color.colorPrimary));

    }

    public void setImageUnSelectedColor(Drawable d) {
        DrawableCompat.setTint(d,
                ContextCompat.getColor(TweetDetailActivity.this, R.color.colorSecondaryText));

    }

    public void onRetweet(View view) {

        mTwitterClient.retweet(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Set icon color to blue
                setImageSelectedColor(binding.ivRetweet.getDrawable());
                //Increment retweet num
                Integer retweetnum = Integer.parseInt(binding.tvNumRetweets.getText().toString());
                binding.tvNumRetweets.setText(String.valueOf(retweetnum + 1));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

        }, mTweet.getUid());
    }

    public void onFavorite(View v) {
        mTwitterClient.favorite(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Set icon color to red
                setImageSelectedColor(binding.ivFavorite.getDrawable(), ContextCompat.getColor(TweetDetailActivity.this, R.color.colorAppRed));
                //Increment like num
                Integer likenum = Integer.parseInt(binding.tvNumLikes.getText().toString());
                binding.tvNumLikes.setText(String.valueOf(likenum + 1));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        }, mTweet.getUid());
    }
}
