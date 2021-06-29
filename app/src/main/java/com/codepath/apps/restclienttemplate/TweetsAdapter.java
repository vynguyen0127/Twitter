package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{
    @NonNull
    @NotNull

    Context context;
    List<Tweet> tweets;

    // Pass in the context and list of tweets
    TweetsAdapter(Context context, List<Tweet> tweets){
        this.context = context;
        this.tweets = tweets;
    }


    // For each row, inflate the layout
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        // Get the data at position
        Tweet tweet = tweets.get(position);

        holder.bind(tweet);

        // Bind the tweet with view holder
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }

    // Bind values based on the position of the element

    // Define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        TextView tvTime;
        ImageView ivMedia;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvTime = itemView.findViewById(R.id.tvTime);
            ivMedia = itemView.findViewById(R.id.ivMedia);
        }

        public void bind(Tweet tweet) {
            tvBody.setText(tweet.body);
            tvScreenName.setText(tweet.user.screenName);
//            Log.i("TweetsAdapter", tweet.body);
//            Log.i("TweetsAdapter",tweet.user.profileImageUrl);
            Glide.with(context)
                    .load(tweet.user.profileImageUrl)
                    .into(ivProfileImage);
            tvTime.setText(tweet.getRelativeTimeAgo(tweet.createdAt));

            if(!tweet.mediaURL.isEmpty()){
                Glide.with(context)
                        .load(tweet.mediaURL)
                        .into(ivMedia);
                ivMedia.setVisibility(View.VISIBLE);
            }
            else{
                ivMedia.setVisibility(View.GONE);
            }
        }


    }
}
