package com.codepath.apps.mysimpletweets;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.codepath.apps.mysimpletweets.models.User;

public class ComposeTweetDialog extends DialogFragment {

    private EditText editText;

    public ComposeTweetDialog() {
        // empty constructor is needed?
    }

    public static ComposeTweetDialog newInstance(User user) {
        ComposeTweetDialog frag = new ComposeTweetDialog();
        Bundle args = new Bundle();
        args.putString("username", user.getName());
        args.putString("screenname", user.getScreenName());
        args.putString("profile_picture", user.getProfileImageUrl());
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose_tweet, container);
    }

}
