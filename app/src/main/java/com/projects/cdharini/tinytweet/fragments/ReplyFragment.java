package com.projects.cdharini.tinytweet.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.projects.cdharini.tinytweet.R;
import com.projects.cdharini.tinytweet.TinyTweetApplication;
import com.projects.cdharini.tinytweet.models.Tweet;
import com.projects.cdharini.tinytweet.networking.TwitterClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ReplyFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = ReplyFragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mReplyUserName;

    Button btnReply;
    ImageButton btnCancel;

    TwitterClient mTwitterClient;
    private TextView tvReplyTitle;
    private EditText etReply;
    private long mReplyToId;

    public ReplyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ReplyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReplyFragment newInstance(String replyToUserName, long replyToId) {
        ReplyFragment fragment = new ReplyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, replyToUserName);
        args.putLong(ARG_PARAM2, replyToId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mReplyUserName = getArguments().getString(ARG_PARAM1);
            mReplyToId = getArguments().getLong(ARG_PARAM2);
        }
        mTwitterClient = TinyTweetApplication.getRestClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reply, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnCancel = (ImageButton) view.findViewById(R.id.ibCancel);
        btnReply = (Button) view.findViewById(R.id.btnSend);
        tvReplyTitle = (TextView) view.findViewById(R.id.tvReplyTitle);
        etReply = (EditText) view.findViewById(R.id.etReply);

        //set reply title text
        tvReplyTitle.setText("Replying to @" + mReplyUserName);
        //set btncancel
        btnCancel.setOnClickListener((v) -> {
            Log.d(TAG, "Canceling reply");
            dismiss();
        });
        //set btnreply
        btnReply.setOnClickListener((v )-> {
            postReply();
        });
    }

    void postReply() {
        mTwitterClient.postTweet(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d(TAG, "success! " + response.toString());
                try {
                    Tweet tweet = Tweet.fromJson(response);
                    //mListener.onTweetPosted(tweet);
                } catch (JSONException e) {
                    Log.e(TAG, "couldn't parse tweet");
                }

                dismiss();
                //super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d(TAG, "success 2");
                dismiss();
                //super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(TAG, "failue 1");
                dismiss();
                //super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d(TAG, "failure 2");
                dismiss();
                //super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(TAG, "failue 3");
                //super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d(TAG, "succeess 3");
                //super.onSuccess(statusCode, headers, responseString);
            }

        }, "@" + mReplyUserName + " " + etReply.getText().toString(), mReplyToId);

    }

}
