package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ComposeActivity extends AppCompatActivity {
public static final int MAX_TWEET_LENGTH = 10;
    EditText etCompose;
    Button btnTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btnTweet);

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
            }
        });
    }
}