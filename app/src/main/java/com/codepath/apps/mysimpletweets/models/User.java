package com.codepath.apps.mysimpletweets.models;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;
    private static User currentUser;

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public static User fromJSON(JSONObject jsonObject){
        User u = new User();
        try {
            u.name = jsonObject.getString("name");
            u.uid = jsonObject.getLong("id");
            u.screenName = jsonObject.getString("screen_name");
            u.profileImageUrl = jsonObject.getString("profile_image_url");
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return u;
    }

    public static User getCurrentUser(){
        return User.currentUser;
    }

    public static void setCurrentUser(User current){
        User.currentUser = current;
    }
}
