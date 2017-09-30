package com.projects.cdharini.tinytweet.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.projects.cdharini.tinytweet.TinyTweetApplication;
import com.projects.cdharini.tinytweet.databinding.FragmentComposeTweetBinding;
import com.projects.cdharini.tinytweet.models.Tweet;
import com.projects.cdharini.tinytweet.networking.TwitterClient;
import com.projects.cdharini.tinytweet.utils.TinyTweetConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ComposeTweetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComposeTweetFragment extends DialogFragment {
    public static final String TAG = ComposeTweetFragment.class.getSimpleName();

    private ImageButton btnCancel;
    private Button btnTweet;
    private EditText etNewTweet;
    private TextView tvCharCount;

    private TwitterClient mTwitterClient;
    private FragmentComposeTweetBinding binding;
    private ComposeTweetDialogListener mListener;

    public ComposeTweetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ComposeTweetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ComposeTweetFragment newInstance() {
        ComposeTweetFragment fragment = new ComposeTweetFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTwitterClient = TinyTweetApplication.getRestClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentComposeTweetBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        btnTweet = binding.btnTweet;
        etNewTweet = binding.etComposeTweet;
        btnCancel = binding.ibCancel;
        tvCharCount = binding.tvCharCount;
        tvCharCount.setText(String.valueOf(TinyTweetConstants.MAX_TWEET_LENGTH));
        etNewTweet.addTextChangedListener(mTextEditorWatcher);
        btnCancel.setOnClickListener((v)-> {
            Log.d(TAG, "Cancelling tweet");
            dismiss();});

        btnTweet.setOnClickListener((v) -> {
            postTweet();
        });
        mListener = (ComposeTweetDialogListener) getActivity();
        return view;
    }

    public void postTweet() {
        mTwitterClient.postTweet(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d(TAG, "success! " + response.toString());
                try {
                    Tweet tweet = Tweet.fromJson(response);

                    mListener.onTweetPosted(tweet);
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

        }, etNewTweet.getText().toString());

    }

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            int charLeft = TinyTweetConstants.MAX_TWEET_LENGTH - s.length();
            if (charLeft > 20) {
                tvCharCount.setTextColor(Color.GRAY);
                btnTweet.setEnabled(true);
            } else  if (charLeft > 0) {
                tvCharCount.setTextColor(Color.RED);
                btnTweet.setEnabled(true);
            } else {
                tvCharCount.setTextColor(Color.RED);
                    btnTweet.setEnabled(false);
                }

            tvCharCount.setText(String.valueOf(charLeft));
        }

        public void afterTextChanged(Editable s) {
        }
    };

    public interface ComposeTweetDialogListener {
        void onTweetPosted(Tweet t);
    }
}
