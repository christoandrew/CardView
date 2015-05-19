package com.iconasystems.christo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.iconasystems.christo.Constants;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by Christo on 1/29/2015.
 */
public class TwitterHandler {
    private ConfigurationBuilder configurationBuilder;
    private TwitterFactory twitterFactory;
    private AccessToken accessToken;
    private RequestToken requestToken;
    private Twitter mTwitter;
    private Activity activity;
    private Context context;
    private SharedPreferences sharedPreferences;

    private ConnectionManager connectionManager;

    private static String PREFERENCES_TWITTER = "pref_twitter";

    public TwitterHandler(Activity context) {
        this.context = context;
        sharedPreferences = activity.getSharedPreferences(PREFERENCES_TWITTER, Context.MODE_PRIVATE);
        connectionManager = new ConnectionManager(context);
        configurationBuilder = new ConfigurationBuilder()
                .setOAuthConsumerKey(Constants.TwitterExtras.TWITTER_CONSUMER_KEY)
                .setOAuthConsumerSecret(Constants.TwitterExtras.TWITTER_CONSUMER_SECRET);
        twitterFactory = new TwitterFactory(configurationBuilder.build());
        mTwitter = twitterFactory.getInstance();
    }

    public void login(){
        if (!isLoggedIn()){
            Uri uri = activity.getIntent().getData();

            if (uri != null && uri.toString().startsWith(Constants.TwitterExtras.TWITTER_CALLBACK_URL)){
                String verifier = uri.getQueryParameter(Constants.TwitterExtras.URL_TWITTER_OAUTH_VERIFIER);

                try {
                    accessToken = mTwitter.getOAuthAccessToken(requestToken, verifier);
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void logOut(){

    }

    /**
     * Check user already logged in your application using twitter Login flag is
     * fetched from Shared Preferences
     */
    private boolean isLoggedIn() {
        // return twitter login status from Shared Preferences
        return sharedPreferences.getBoolean(Constants.TwitterExtras.PREF_KEY_TWITTER_LOGIN, false);
    }

}
