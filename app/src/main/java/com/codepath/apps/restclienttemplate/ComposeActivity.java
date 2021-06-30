package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {
public static final int MAX_TWEET_LENGTH = 140;
    EditText etCompose;
    Button btnTweet;
    private final int REQUEST_CODE = 30;

    public static final String TAG = "ComposeActivity";
    TwitterClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApp.getRestClient(this);
        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btnTweet);
        final boolean reply = getIntent().getBooleanExtra("Reply",false);
        String screen_name = getIntent().getStringExtra("screen_name");
        final String id;
        if(reply){
            etCompose.setText("@" + screen_name +" ");
            id = getIntent().getStringExtra("id");
        }
        else{
            id = "";
        }
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // error handling
                String content = etCompose.getText().toString();
                if(content.isEmpty()){
                    Toast.makeText(ComposeActivity.this, "Sorry, your tweet cannot be empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(content.length() > MAX_TWEET_LENGTH){
                    Toast.makeText(ComposeActivity.this, String.format("Sorry, your tweet cannot exceed %s characters", MAX_TWEET_LENGTH), Toast.LENGTH_SHORT).show();
                    return;
                }

                // make API call to Twitter to publish the tweet
                Toast.makeText(ComposeActivity.this, content, Toast.LENGTH_SHORT).show();
                client.publishTweet(content, reply, id, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "onSuccess to publish tweet");
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Log.i(TAG, "Published tweet says: " + tweet);
                            Intent i = new Intent();
                            i.putExtra("Tweet", Parcels.wrap(tweet));
                            setResult(RESULT_OK,i );
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure to publish tweet");
                    }
                });
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_compose,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("TAG","onOptionsItemSelected");
        if(item.getItemId() == R.id.cancel){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}