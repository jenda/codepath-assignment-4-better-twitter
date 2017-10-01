package com.codepath.apps.bluebirdone.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.bluebirdone.R;
import com.codepath.apps.bluebirdone.models.Tweet;
import com.codepath.apps.bluebirdone.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jan_spidlen on 9/28/17.
 */
public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private final List<Tweet> tweets;
    private final Context context;

    public TweetAdapter(List<Tweet> tweets, Context context) {
        this.tweets = tweets;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View bookView = inflater.inflate(R.layout.tweet_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(bookView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);

        holder.tweetTextTextView.setText(tweet.text);
        holder.timeAgoTextView.setText(Utils.getRelativeTimeAgo(tweet.createdAt));

        holder.userNameTextView.setText(tweet.user.name);
        holder.handleTextView.setText(tweet.user.getHandle());

        if (tweet.getPhoto() != null) {
            Glide.with(context)
                    .load(tweet.getPhoto().mediaUrlHttps)
                    .into(holder.mediaImageView);
            holder.mediaImageView.setVisibility(View.VISIBLE);
        } else {
            holder.mediaImageView.setVisibility(View.GONE);
        }

        Glide.with(context)
                .load(tweet.user.profileImageUrl)
                .into(holder.profileImageView);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }
    /**
     * Viewholder class for the tweets.
     */
    protected class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tweet_text_text_view)
        TextView tweetTextTextView;

        @BindView(R.id.user_name_text_view)
        TextView userNameTextView;

        @BindView(R.id.handle_text_view)
        TextView handleTextView;

        @BindView(R.id.time_ago_text_view)
        TextView timeAgoTextView;

        @BindView(R.id.profile_image_view)
        ImageView profileImageView;

        @BindView(R.id.mediaImageView)
        ImageView mediaImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
