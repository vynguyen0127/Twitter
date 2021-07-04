package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.parceler.Parcels;

import okhttp3.Headers;

public class TweetDetailActivity extends AppCompatActivity {

    ImageView ivProfileImage;
    TextView tvBody;
    TextView tvScreenName;
    TextView tvTime;
    ImageView ivMedia;
    ImageButton ibFavorite;
    ImageButton ibRetweet;
    TextView tvName;
    Tweet tweet;
    TwitterClient client;

    public static final String TAG = "TweetDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);
        client = TwitterApp.getRestClient(this);
        tweet = Parcels.unwrap(getIntent().getParcelableExtra("Tweet"));
        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvBody = findViewById(R.id.tvBody);
        tvScreenName = findViewById(R.id.tvScreenName);
        tvTime = findViewById(R.id.tvTime);
        ivMedia = findViewById(R.id.ivMedia);
        ibFavorite = findViewById(R.id.ibFavorite);
        ibRetweet = findViewById(R.id.ibRetweet);
        tvName = findViewById(R.id.tvName);
        tvBody.setText(tweet.body);
        tvName.setText(tweet.user.name);
        tvScreenName.setText("@" + tweet.user.screenName);

        Glide.with(TweetDetailActivity.this)
                .load(tweet.user.profileImageUrl)
                .circleCrop()
                .into(ivProfileImage);

        tvTime.setText(tweet.getRelativeTimeAgo(tweet.createdAt));

        ibFavorite.setImageResource((tweet.getLiked()) ?
                R.drawable.ic_vector_heart : R.drawable.ic_vector_heart_stroke);

        ibRetweet.setImageResource((tweet.getRetweeted()) ?
                R.drawable.ic_vector_retweet : R.drawable.ic_vector_retweet_stroke);

        if(!tweet.mediaURL.isEmpty()){
            Glide.with(TweetDetailActivity.this)
                    .load(tweet.mediaURL)
                    .transform(new RoundedCorners(30))
                    .into(ivMedia);
            ivMedia.setVisibility(View.VISIBLE);
        }
        else{
            ivMedia.setVisibility(View.GONE);
        }

        ibFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!tweet.getLiked()){
                    client.likeTweet(tweet.id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i(TAG, "tweet liked");
                            tweet.setLiked(true);
                            ibFavorite.setImageResource(R.drawable.ic_vector_heart);
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.i(TAG,"like error");
                        }
                    });

                }
                else{
                    client.unlikeTweet(tweet.id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i(TAG, "tweet unliked");
                            tweet.setLiked(false);
                            ibFavorite.setImageResource(R.drawable.ic_vector_heart_stroke);
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.i(TAG, "unlike error");
                        }
                    });

                }

            }
        });

        ibRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!tweet.getRetweeted()){
                    client.retweetTweet(tweet.id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i(TAG, "tweet retweeted");
                            tweet.setRetweeted(true);
                            ibRetweet.setImageResource(R.drawable.ic_vector_retweet);
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.i(TAG, "retweet error");
                        }
                    });

                }
                else{
                    client.unretweetTweet(tweet.id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i(TAG, "tweet unretweeted");
                            tweet.setRetweeted(false);
                            ibRetweet.setImageResource(R.drawable.ic_vector_retweet_stroke);
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.i(TAG, "unretweet error");
                        }
                    });

                }

            }
        });
    }
}