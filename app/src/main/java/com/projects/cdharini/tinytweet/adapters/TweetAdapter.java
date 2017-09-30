package com.projects.cdharini.tinytweet.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.projects.cdharini.tinytweet.R;
import com.projects.cdharini.tinytweet.activities.TweetDetailActivity;
import com.projects.cdharini.tinytweet.models.Tweet;
import com.projects.cdharini.tinytweet.utils.TinyTweetConstants;
import com.projects.cdharini.tinytweet.utils.TinyTweetUtils;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by dharinic on 9/27/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Tweet> mTweets;
    private Context mContext;

    public TweetAdapter(Context context, List<Tweet> tweets) {
        mContext = context;
        mTweets = tweets;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        RecyclerView.ViewHolder viewHolder;
        View v = inflater.inflate(R.layout.item_tweet, parent, false);
        viewHolder = new BasicTweetView(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Tweet t = mTweets.get(position);
        BasicTweetView view = (BasicTweetView) holder;
        view.bind(t);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    public void AddAll(List<Tweet> tweets) {
        mTweets.addAll(tweets);
        notifyDataSetChanged();
    }

    private class BasicTweetView extends RecyclerView.ViewHolder{
        TextView tvUserName;
        TextView tvScreenName;
        TextView tvTweet;
        ImageView ivProfilePic;
        TextView tvTweetAge;

        public BasicTweetView(View itemView) {
            super(itemView);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvScreenName = (TextView) itemView.findViewById(R.id.tvScreenName);
            tvTweet = (TextView) itemView.findViewById(R.id.tvTweet);
            tvTweetAge = (TextView) itemView.findViewById(R.id.tvTweetAge);
            ivProfilePic = (ImageView) itemView.findViewById(R.id.ivProfilePic);

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, TweetDetailActivity.class);
                intent.putExtra(TinyTweetConstants.EXTRA_TWEET, Parcels.wrap(mTweets.get(getAdapterPosition())));
                mContext.startActivity(intent);
            });
        }

        public void bind(Tweet t) {
            tvTweet.setText(t.getText());
            tvUserName.setText(t.getUser().getName());
            tvScreenName.setText("@" + t.getUser().getScreenName());
            tvTweetAge.setText(TinyTweetUtils.getRelativeTimeAgo(t.getCreatedAt()));

            Glide.with(mContext).load(t.getUser().getProfilePicUrl())
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .error(R.drawable.ic_error_outline_black_24dp).into(ivProfilePic);

        }
    }
}
