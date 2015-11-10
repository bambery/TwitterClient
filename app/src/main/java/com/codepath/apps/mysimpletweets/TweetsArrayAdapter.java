package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

// taking the created tweet objects and turning them into views to be displayed in a list
public class TweetsArrayAdapter extends ArrayAdapter<Tweet>{
    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    // Override and setup custom template
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1. get tweet
        Tweet tweet = getItem(position);
        // 2. find or inflate the template
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }
        // 3. find the subviews to fill with data in the template
        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvComposeUsername);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        TextView tvScreenName = (TextView) convertView.findViewById(R.id.tvScreenName);
        TextView tvTimeSince = (TextView) convertView.findViewById(R.id.tvTimeSince);
        // 4. populate data into the subviews
        tvUsername.setText(tweet.getUser().getName());
        tvBody.setText(tweet.getBody());
        // I feel like the User class should handle its display logic but I don't know how to access
        // strings.xml without passing in a context, which seems hacky.
        tvScreenName.setText(getContext().getString(R.string.at_sign) + tweet.getUser().getScreenName());
        tvTimeSince.setText(tweet.getCreatedAt());
        ivProfileImage.setImageResource(android.R.color.transparent); // good place to set a placeholder while clearing out the old content
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);
        // 5. return the view to be inserted into the list
        return convertView;
    }
}
