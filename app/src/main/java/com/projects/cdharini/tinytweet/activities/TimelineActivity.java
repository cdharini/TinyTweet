package com.projects.cdharini.tinytweet.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.projects.cdharini.tinytweet.R;
import com.projects.cdharini.tinytweet.TinyTweetApplication;
import com.projects.cdharini.tinytweet.adapters.TweetAdapter;
import com.projects.cdharini.tinytweet.fragments.ComposeTweetFragment;
import com.projects.cdharini.tinytweet.models.Tweet;
import com.projects.cdharini.tinytweet.networking.TwitterClient;
import com.projects.cdharini.tinytweet.utils.EndlessRecyclerViewScrollListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity implements ComposeTweetFragment.ComposeTweetDialogListener{

    private static final String TAG = TimelineActivity.class.getSimpleName();
    private TwitterClient mTwitterClient;
    private List<Tweet> mTweets;
    private TweetAdapter mTweetAdapter;
    private RecyclerView rvTimeline;
    private SwipeRefreshLayout srSwipeContainer;
    private EndlessRecyclerViewScrollListener mScrollListener;
    private long mMaxIdSoFar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                ComposeTweetFragment fragment = ComposeTweetFragment.newInstance();
                fragment.show(fm, "fragment_compose_tweet");
            }
        });

        mTwitterClient = TinyTweetApplication.getRestClient();
        rvTimeline = (RecyclerView)findViewById(R.id.rvTimeline);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvTimeline.setLayoutManager(manager);
        mTweets = new ArrayList<>();
        mTweetAdapter = new TweetAdapter(this, mTweets);
        rvTimeline.setAdapter(mTweetAdapter);
        mScrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                populateHomeTimeline(mMaxIdSoFar);
                //load new tweets, need max_id instead of page
            }
        };

        srSwipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshContainer);
        srSwipeContainer.setOnRefreshListener(()-> {
            mTweets.clear();
            mTweetAdapter.clear();
            mScrollListener.resetState();
            Toast.makeText(TimelineActivity.this, "Refreshing", Toast.LENGTH_SHORT).show();
            populateHomeTimeline(-1);
        });

        rvTimeline.addOnScrollListener(mScrollListener);
        populateHomeTimeline(-1);
    }

    private void populateHomeTimeline(long maxId) {
        mTwitterClient.getTweetTimeline(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, "success, object :" + response.toString());
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                mTweets.addAll(Tweet.fromJsonArray(response));
                mTweetAdapter.notifyItemRangeInserted(mTweetAdapter.getItemCount(), mTweets.size() - 1);
                Log.d(TAG, "success, array :" + response.toString());
                srSwipeContainer.setRefreshing(false);
                mMaxIdSoFar = mTweets.get(mTweets.size() - 1).getUid();
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(TAG, "failure, statuscode : " + statusCode );
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d(TAG, "failure, statuscode : " + statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(TAG, "failure, statuscode : " + statusCode  + " response :" + responseString);
            }
        }, maxId);
    }

    @Override
    public void onTweetPosted(Tweet t) {
        mTweets.add(0, t);
        mTweetAdapter.notifyItemRangeInserted(0, 1);
        rvTimeline.scrollToPosition(0);
    }
}
