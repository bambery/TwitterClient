package com.codepath.apps.mysimpletweets;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
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
    private Toolbar myToolbar;

    // change title color: http://developer.android.com/reference/android/widget/Toolbar.html#setTitleTextColor(int)
    // getting color int: http://stackoverflow.com/questions/31842983/getresources-getcolor-is-deprecated
    // getting rid of overflow menu icon: http://stackoverflow.com/questions/28291001/how-does-one-remove-default-toolbar-menu-items-and-replace-with-different-icons

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        // set custom toolbar
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(R.string.title_activity_timeline);

        myToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        myToolbar.inflateMenu(R.menu.action_bar_timeline);

        //setSupportActionBar(myToolbar);

        //create the arraylist from data source
        tweets = new ArrayList<>();
        //construct the adapter from data source
        aTweets = new TweetsArrayAdapter(this, tweets);

        //get client
        client = TwitterApplication.getRestClient(); // singleton client
        lvTweets = (ListView) findViewById(R.id.lvTweets);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                getMoreTweets();
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });
        // connect adapter to listview
        lvTweets.setAdapter(aTweets);
        populateTimeline();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_bar_timeline, menu);
        return true;
    }

    //send api request to get the timeline json
    // fill listview by creating the tweet objects from the json
    private void populateTimeline() {
        client.getInitialHomeTimeline(new JsonHttpResponseHandler() {
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

    /*
    // Append more data into the adapter
    public void customLoadMoreDataFromApi(int offset) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter

        client.getMoreTweets();
    }
    */

    public void getMoreTweets(){
        client.getLatestTweetsSince(getLastTweetId(), new JsonHttpResponseHandler(){
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

    private long getLastTweetId(){
        return (tweets.get(tweets.size() - 1).getUid());
    }
}
