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

    private static class ViewHolder {
        ImageView ivProfileImage;
        TextView tvUsername;
        TextView tvBody;
        TextView tvScreenName;
        TextView tvTimeSince;
    }

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    // Override and setup custom template
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1. get tweet
        Tweet tweet = getItem(position);
        // 2. find or inflate the template
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_tweet, parent, false);

            viewHolder.ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
            viewHolder.tvUsername = (TextView) convertView.findViewById(R.id.tvComposeUsername);
            viewHolder.tvBody = (TextView) convertView.findViewById(R.id.tvBody);
            viewHolder.tvScreenName = (TextView) convertView.findViewById(R.id.tvScreenName);
            viewHolder.tvTimeSince = (TextView) convertView.findViewById(R.id.tvTimeSince);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 4. populate data into the subviews
        viewHolder.tvUsername.setText(tweet.getUser().getName());
        viewHolder.tvBody.setText(tweet.getBody());
        // I feel like the User class should handle its display logic but I don't know how to access
        // strings.xml without passing in a context, which seems hacky.
        viewHolder.tvScreenName.setText(getContext().getString(R.string.at_sign) + tweet.getUser().getScreenName());
        viewHolder.tvTimeSince.setText(tweet.getCreatedAt());
        viewHolder.ivProfileImage.setImageResource(android.R.color.transparent); // good place to set a placeholder while clearing out the old content
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(viewHolder.ivProfileImage);
        // 5. return the view to be inserted into the list
        return convertView;
    }
}
