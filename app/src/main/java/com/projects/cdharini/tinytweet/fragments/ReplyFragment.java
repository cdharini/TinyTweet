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
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.projects.cdharini.tinytweet.R;
import com.projects.cdharini.tinytweet.TinyTweetApplication;
import com.projects.cdharini.tinytweet.models.Tweet;
import com.projects.cdharini.tinytweet.networking.TwitterClient;
import com.projects.cdharini.tinytweet.utils.TinyTweetUtils;

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
        if (!TinyTweetUtils.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), "No internet, try again later!", Toast.LENGTH_SHORT).show();
        }

        mTwitterClient.postTweet(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d(TAG, "success! " + response.toString());
                try {
                    Tweet tweet = Tweet.fromJson(response);
                } catch (JSONException e) {
                    Log.e(TAG, "couldn't parse tweet");
                }

                dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(TAG, "Failure to reply to tweet");
                dismiss();
            }

        }, "@" + mReplyUserName + " " + etReply.getText().toString(), mReplyToId);

    }

}
