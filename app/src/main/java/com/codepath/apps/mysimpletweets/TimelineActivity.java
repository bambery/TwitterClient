package com.codepath.apps.mysimpletweets;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends AppCompatActivity implements ComposeTweetDialog.OnSubmitNewTweetListener {

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
        client = TwitterApplication.getRestClient(); // singleton client
        setContentView(R.layout.activity_timeline);
        // set custom toolbar
        setupMyToolbar();

        //create the arraylist from data source
        tweets = new ArrayList<>();
        //construct the adapter from data source
        aTweets = new TweetsArrayAdapter(this, tweets);

        //get client

        lvTweets = (ListView) findViewById(R.id.lvTweets);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                getMoreTweets(getLastTweetId());
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });
        // connect adapter to listview
        lvTweets.setAdapter(aTweets);
        setCurrentUser();
        populateTimeline();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_bar_timeline, menu);
        return true;
    }

    @Override
    public void onNewTweetSubmitted(String tweetBody){
        client.postTweet(tweetBody, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Long newTweetId = Tweet.getPostedTweetId(response);
                refreshAfterNewTweet(newTweetId);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(TimelineActivity.this, "Failed posting tweet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setCurrentUser(){
       client.getCurrentUser(new JsonHttpResponseHandler() {
           @Override
           public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
               User.setCurrentUser(User.fromJSON(response));
           }

           //failure
           @Override
           public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
               Log.d("ERROR", errorResponse.toString());
           }
       });
    }

    public void setupMyToolbar(){
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(R.string.title_activity_timeline);
        myToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        myToolbar.inflateMenu(R.menu.action_bar_timeline);
        // launch compose tweet activity if button is clicked
        myToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_new_tweet) {
                    showNewTweetDialog();
                    return true;
                }
                return false;
            }
        });
    }

    private void showNewTweetDialog(){
        FragmentManager fm = getSupportFragmentManager();
        ComposeTweetDialog composeTweetDialog = ComposeTweetDialog.newInstance(User.getCurrentUser());
        composeTweetDialog.show(fm, "fragment_compose_tweet");
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

    public void getMoreTweets(long lastTweetId){
        Log.d("DEBUG", "inside getmoretweets");
        client.getLatestTweetsSince(lastTweetId, new JsonHttpResponseHandler() {
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

    private void refreshAfterNewTweet(long myNewTweetId){
        aTweets.clear();
        client.getTweetsAfterMyTweet(myNewTweetId, new JsonHttpResponseHandler() {
            // success

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //deserialize json
                //create models and add to adapter
                //load data model into listview
                //               ArrayList<Tweet> tweets = Tweet.fromJSONArray(response);
                // should only be one tweet here
                aTweets.addAll(Tweet.fromJSONArray(response));
            }

            //failure
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("FAILURE DEBUG", errorResponse.toString());
            }
        });
        aTweets.notifyDataSetChanged();
    }

    private long getLastTweetId(){
        return (tweets.get(tweets.size() - 1).getUid());
    }
}
