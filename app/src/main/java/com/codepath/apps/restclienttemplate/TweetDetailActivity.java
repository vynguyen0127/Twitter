package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

    private boolean favorited = false;
    private boolean retweeted = false;
    int iconLike;
    int iconRetweet;

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
//            Log.i("TweetsAdapter", tweet.body);
//            Log.i("TweetsAdapter",tweet.user.profileImageUrl);
        Glide.with(TweetDetailActivity.this)
                .load(tweet.user.profileImageUrl)
                .into(ivProfileImage);
        tvTime.setText(tweet.getRelativeTimeAgo(tweet.createdAt));

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
                if(!favorited){
                    client.favoriteTweet(tweet.id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i(TAG, "tweet favorited");
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.i(TAG,"favorite error");
                        }
                    });
                    iconLike = R.drawable.ic_vector_heart;
                    favorited = true;
                }
                else{
                    client.unfavoriteTweet(tweet.id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i(TAG, "tweet unfavorited");
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.i(TAG, "unfavorite error");
                        }
                    });
                    iconLike = R.drawable.ic_vector_heart_stroke;
                    favorited = false;
                }
                ibFavorite.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), iconLike));
            }
        });

        ibRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!retweeted){
                    client.retweetTweet(tweet.id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i(TAG, "tweet retweeted");
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.i(TAG, "retweet error");
                        }
                    });
                    iconRetweet = R.drawable.ic_vector_retweet_stroke;
                    retweeted = true;
                }
                else{
                    client.unretweetTweet(tweet.id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i(TAG, "tweet unretweeted");
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.i(TAG, "unretweet error");
                        }
                    });
                    iconRetweet = R.drawable.ic_vector_retweet;
                    retweeted = false;
                }

                ibRetweet.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), iconRetweet));
            }
        });
    }
}