package com.codepath.apps.bluebirdone.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.bluebirdone.R;
import com.codepath.apps.bluebirdone.models.Tweet;
import com.codepath.apps.bluebirdone.models.User;
import com.codepath.apps.bluebirdone.presenters.TweetPresenter;
import com.codepath.apps.bluebirdone.utils.PatternEditableBuilder;
import com.codepath.apps.bluebirdone.utils.Utils;

import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by jan_spidlen on 9/28/17.
 */
public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private final List<Tweet> tweets;
    private final Context context;

    private final TweetPresenter presenter;
    public TweetAdapter(List<Tweet> tweets, Context context, TweetPresenter presenter) {
        this.tweets = tweets;
        this.context = context;
        this.presenter = presenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View tweetView = inflater.inflate(R.layout.tweet_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);

        holder.tweetTextTextView.setText(tweet.text);
        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\@(\\w+)"), Color.BLUE,
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                Toast.makeText(context, "Clicked username: " + text,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).into(holder.tweetTextTextView);

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

        holder.retweetCountTextView.setText(tweet.retweetCount + " retweets");
        holder.favsCountTextView.setText(tweet.favouritesCount + " favs");

        Glide.with(context)
                .load(tweet.user.profileImageUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.profileImageView);

        holder.tweet = tweet;
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

        @BindView(R.id.retweet_count)
        TextView retweetCountTextView;

        @BindView(R.id.favsCount)
        TextView favsCountTextView;

        public Tweet tweet;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Optional
        @OnClick({R.id.profile_image_view, R.id.user_name_text_view, R.id.handle_text_view})
        protected void onUserClicked() {
            Log.d("jenda", "onUserClicked");

            presenter.onUserClicked(tweet.user);
        }
    }
}
