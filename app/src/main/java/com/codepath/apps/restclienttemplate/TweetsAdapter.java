package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{
    @NonNull
    @NotNull

    Context context;
    List<Tweet> tweets;
    TwitterClient client;
    public static final String TAG = "TweetsAdapter";

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
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        TextView tvTime;
        TextView tvName;
        ImageView ivMedia;
        Button btnReply;

        int iconLike;
        int iconRetweet;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvTime = itemView.findViewById(R.id.tvTime);
            ivMedia = itemView.findViewById(R.id.ivMedia);
            btnReply = itemView.findViewById(R.id.btnReply);
            tvName = itemView.findViewById(R.id.tvName);

            itemView.setOnClickListener(this);


        }

        public void bind(final Tweet tweet) {
            tvName.setText(tweet.user.name);
            tvBody.setText(tweet.body);
            tvScreenName.setText("@" + tweet.user.screenName);
//            Log.i("TweetsAdapter", tweet.body);
//            Log.i("TweetsAdapter",tweet.user.profileImageUrl);
            Glide.with(context)
                    .load(tweet.user.profileImageUrl)
                    .circleCrop()
                    .into(ivProfileImage);
            tvTime.setText(tweet.getRelativeTimeAgo(tweet.createdAt));

            if(!tweet.mediaURL.isEmpty()){
                Glide.with(context)
                        .load(tweet.mediaURL)
                        .transform(new RoundedCorners(30))
                        .into(ivMedia);
                ivMedia.setVisibility(View.VISIBLE);
            }
            else{
                ivMedia.setVisibility(View.GONE);
            }

            btnReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, ComposeActivity.class);
                    i.putExtra("Reply",true);
                    i.putExtra("screen_name",tweet.user.screenName);
                    i.putExtra("id",tweet.id);
                    context.startActivity(i);

                }
            });

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            if(position != RecyclerView.NO_POSITION){
                Tweet tweet = tweets.get(position);

                Intent i = new Intent(context,TweetDetailActivity.class);
                i.putExtra("Tweet", Parcels.wrap(tweet));
                context.startActivity(i);
                Log.i("TweetsAdapter","item clicked!");
            }
        }
    }


}
