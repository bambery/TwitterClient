package com.codepath.apps.mysimpletweets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    private ListView lvTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        // set appbar title
        setTitle(R.string.title_activity_timeline);
        //find the listview
        lvTweets = (ListView) findViewById(R.id.lvTweets);
        //create the arraylist from data source
        tweets = new ArrayList<>();
        //construct the adapter from data source
        aTweets = new TweetsArrayAdapter(this, tweets);
        // connect adapter to listview
        lvTweets.setAdapter(aTweets);
        //get client
        client = TwitterApplication.getRestClient(); // singleton client
        populateTimeline();
    }

    //send api request to get the timeline json
    // fill listview by creating the tweet objects from the json
    private void populateTimeline(){
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            // success

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //deserialize json
                //create models and add to adapter
                //load data model into listview
 //               ArrayList<Tweet> tweets = Tweet.fromJSONArray(response);
                aTweets.addAll(Tweet.fromJSONArray(response));
            }

            //failure
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("FAILURE DEBUG", errorResponse.toString());
            }
        });
    }
}
