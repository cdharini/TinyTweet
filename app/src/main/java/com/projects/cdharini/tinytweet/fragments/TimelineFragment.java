package com.projects.cdharini.tinytweet.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.projects.cdharini.tinytweet.MyDatabase;
import com.projects.cdharini.tinytweet.R;
import com.projects.cdharini.tinytweet.TinyTweetApplication;
import com.projects.cdharini.tinytweet.adapters.TweetAdapter;
import com.projects.cdharini.tinytweet.models.Tweet;
import com.projects.cdharini.tinytweet.models.Tweet_Table;
import com.projects.cdharini.tinytweet.networking.TwitterClient;
import com.projects.cdharini.tinytweet.utils.EndlessRecyclerViewScrollListener;
import com.projects.cdharini.tinytweet.utils.TinyTweetConstants;
import com.projects.cdharini.tinytweet.utils.TinyTweetUtils;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by dharinic on 10/3/17.
 */

public class TimelineFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String ARG_USERID = "ARG_USERID";

    private int mPage;
    private long mUserId;

    private static final String TAG = TimelineFragment.class.getSimpleName();
    private TwitterClient mTwitterClient;
    private List<Tweet> mTweets;
    private TweetAdapter mTweetAdapter;
    private RecyclerView rvTimeline;
    private SwipeRefreshLayout srSwipeContainer;
    private EndlessRecyclerViewScrollListener mScrollListener;
    private long mMaxIdSoFar;

    private LoadCompleteListener mListener;

    public static TimelineFragment newInstance(int page, long userId) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putLong(ARG_USERID, userId);
        TimelineFragment fragment = new TimelineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        mUserId = getArguments().getLong(ARG_USERID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        mListener = (LoadCompleteListener) getActivity();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTwitterClient = TinyTweetApplication.getRestClient();
        rvTimeline = (RecyclerView)view.findViewById(R.id.rvTimeline);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        rvTimeline.setLayoutManager(manager);
        mTweets = new ArrayList<>();
        mTweetAdapter = new TweetAdapter(getActivity(), mTweets);
        rvTimeline.setAdapter(mTweetAdapter);
        mScrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                populateTimeline(mPage, mMaxIdSoFar);
            }
        };

        srSwipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshContainer);
        srSwipeContainer.setOnRefreshListener(()-> {
            mTweets.clear();
            mTweetAdapter.clear();
            mScrollListener.resetState();
            Toast.makeText(getActivity(), "Refreshing", Toast.LENGTH_SHORT).show();
            mListener.onLoadStart();
            populateTimeline(mPage, -1);
        });
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvTimeline.getContext(),
                manager.getOrientation());
        rvTimeline.addItemDecoration(dividerItemDecoration);


        rvTimeline.addOnScrollListener(mScrollListener);

        populateTimeline(mPage, -1);

    }

    private void populateTimeline(int page, long maxId) {

        // check for internet
        // if no internet, then display toast and if home timeline, populate from db
        if(!TinyTweetUtils.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(),
                    "Unfortunately, there is no internet connection!", Toast.LENGTH_SHORT).show();
            //load data from database
            if (page == TinyTweetConstants.HOME_TIMELINE) {

                mTweets = SQLite.select().
                        from(Tweet.class).orderBy(Tweet_Table.mUid, false).queryList();
                srSwipeContainer.setRefreshing(false);
                mTweetAdapter.addAll(mTweets);

            }
            return;
        }

        if (page == TinyTweetConstants.HOME_TIMELINE) {
            mTwitterClient.getTweetTimeline(new TimelineResponseHandler(), maxId);
        } else if (page == TinyTweetConstants.MENTIONS_TIMELINE) {
            mTwitterClient.getMentionsTimeline(new TimelineResponseHandler(), maxId);
        } else {
            mTwitterClient.getUserTimeline(new TimelineResponseHandler(), maxId, mUserId);
        }
    }

    private class TimelineResponseHandler extends JsonHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.d(TAG, "success, object :" + response.toString());
            super.onSuccess(statusCode, headers, response);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            List<Tweet> newTweets = Tweet.fromJsonArray(response);
            mTweets.addAll(newTweets);
            mTweetAdapter.notifyItemRangeInserted(mTweetAdapter.getItemCount(), mTweets.size() - 1);
            Log.d(TAG, "success, array :" + response.toString());
            srSwipeContainer.setRefreshing(false);
            mMaxIdSoFar = mTweets.get(mTweets.size() - 1).getUid();
            if (TinyTweetConstants.HOME_TIMELINE == mPage) {
                saveTweetsToDatabase(newTweets);
            }
            mListener.onLoadComplete();
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
    }

    public void addTweetToTimeline(Tweet t) {
        mTweets.add(0, t);
        mTweetAdapter.notifyItemRangeInserted(0, 1);
        rvTimeline.scrollToPosition(0);
    }

    public void saveTweetsToDatabase(List<Tweet> tweets) {
        FlowManager.getDatabase(MyDatabase.class)
                .beginTransactionAsync(new ProcessModelTransaction.Builder<>(
                        new ProcessModelTransaction.ProcessModel<Tweet>() {
                            @Override
                            public void processModel(Tweet tweet, DatabaseWrapper wrapper) {
                             tweet.save(wrapper);
                            }
                        }).addAll(tweets).build())  // add elements (can also handle multiple)
                .error((transaction, error) -> {
                        Log.d(TAG, "Error in making database transaction");
                })
                .success(transaction -> Log.d(TAG, "successfully saved tweets")).build().execute();
    }

    public interface LoadCompleteListener{
        void onLoadComplete();
        void onLoadStart();
    }
}

