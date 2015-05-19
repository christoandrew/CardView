package com.iconasystems.christo.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iconasystems.christo.Constants;
import com.iconasystems.christo.cardview.ProfileActionsActivity;
import com.iconasystems.christo.cardview.R;
import com.iconasystems.christo.utils.AlertDialogManager;
import com.iconasystems.christo.utils.CircleImageDisplayer;
import com.iconasystems.christo.utils.ConnectionManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.squareup.picasso.Picasso;

import cn.pedant.SweetAlert.SweetAlertDialog;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment {
    private ImageView mProfilePhoto;
    private ImageView mProfileBackground;
    private TextView mDisplayName;
    private TextView mFollowers;
    private TextView mFollowing;
    private TextView mNotifications;
    private TextView mEventsAttending;
    private TextView mEventsAttended;
    private TextView mEventInvitations;
    private TextView mRecentEvents;
    private TextView mFindFriends;
    private TextView mShareProfile;
    private TextView mSettings;
    private SharedPreferences preferences;
    private DisplayImageOptions options;

    public MeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_me, container, false);
        mProfilePhoto = (ImageView) rootView.findViewById(R.id.profile_pic);
        mDisplayName = (TextView) rootView.findViewById(R.id.profile_display_name);
        mFollowers = (TextView) rootView.findViewById(R.id.profile_followers);
        mFollowing = (TextView) rootView.findViewById(R.id.profile_following);
        mNotifications = (TextView) rootView.findViewById(R.id.profile_notifications);
        mEventsAttending = (TextView) rootView.findViewById(R.id.profile_events_am_attending);
        mEventsAttended = (TextView) rootView.findViewById(R.id.profile_events_attended);
        mEventInvitations = (TextView) rootView.findViewById(R.id.profile_event_invitations);
        mRecentEvents = (TextView) rootView.findViewById(R.id.profile_recent_events);
        mFindFriends = (TextView) rootView.findViewById(R.id.profile_find_friends);
        mShareProfile = (TextView) rootView.findViewById(R.id.profile_share_my_profile);
        mSettings = (TextView) rootView.findViewById(R.id.profile_settings);
        mProfileBackground = (ImageView) rootView.findViewById(R.id.profile_background);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ConnectionManager connectionManager = new ConnectionManager(getActivity());
        if (connectionManager.isConnected()){
            new LoadUserInfo().execute();
        }else {
            AlertDialogManager alertDialogManager = new AlertDialogManager(getActivity(),
                    SweetAlertDialog.ERROR_TYPE,
                    connectionManager.getErrorMessage(),
                    connectionManager.getErrorTitle());
            alertDialogManager.alertDialog().show();
        }

        preferences = getActivity().getSharedPreferences(Constants.TwitterExtras.PREFERENCE_NAME, Context.MODE_PRIVATE);

        options = new DisplayImageOptions.Builder()
                .displayer(new CircleImageDisplayer())
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .imageScaleType(ImageScaleType.EXACTLY)
                .considerExifParams(true)
                .build();




        mFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent followers = new Intent(getActivity(), ProfileActionsActivity.class);
                followers.putExtra(Constants.Extra.FRAGMENT_INDEX, FollowersFragment.INDEX);
                startActivity(followers);
            }
        });

        mFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent following = new Intent(getActivity(), ProfileActionsActivity.class);
                following.putExtra(Constants.Extra.FRAGMENT_INDEX, FollowingFragment.INDEX);
                startActivity(following);
            }
        });

        mNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notifications = new Intent(getActivity(), ProfileActionsActivity.class);
                notifications.putExtra(Constants.Extra.FRAGMENT_INDEX, NotificationsFragment.INDEX);
                startActivity(notifications);
            }
        });

        mEventsAttending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent events_attending = new Intent(getActivity(), ProfileActionsActivity.class);
                events_attending.putExtra(Constants.Extra.FRAGMENT_INDEX, EventsAttendingFragment.INDEX);
                startActivity(events_attending);
            }
        });

        mEventsAttended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent events_attended = new Intent(getActivity(), ProfileActionsActivity.class);
                events_attended.putExtra(Constants.Extra.FRAGMENT_INDEX, EventsAttendedFragment.INDEX);
                startActivity(events_attended);
            }
        });

        mEventInvitations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent event_invitations = new Intent(getActivity(), ProfileActionsActivity.class);
                event_invitations.putExtra(Constants.Extra.FRAGMENT_INDEX, EventInvitationsFragment.INDEX);
                startActivity(event_invitations);
            }
        });

        mRecentEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent recent_events = new Intent(getActivity(), ProfileActionsActivity.class);
                recent_events.putExtra(Constants.Extra.FRAGMENT_INDEX, RecentEventsFragment.INDEX);
                startActivity(recent_events);
            }
        });

        mFindFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent find_friends = new Intent(getActivity(), ProfileActionsActivity.class);
                find_friends.putExtra(Constants.Extra.FRAGMENT_INDEX, FindFriendsFragment.INDEX);
                startActivity(find_friends);
            }
        });

        mShareProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share_Profile = new Intent(getActivity(), ProfileActionsActivity.class);
                share_Profile.putExtra(Constants.Extra.FRAGMENT_INDEX, ShareMyProfileFragment.INDEX);
                startActivity(share_Profile);
            }
        });

        mSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settings = new Intent(getActivity(), ProfileActionsActivity.class);
                settings.putExtra(Constants.Extra.FRAGMENT_INDEX, SettingsFragment.INDEX);
                startActivity(settings);
            }
        });
    }

    class LoadUserInfo extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {

            //String oauth_token = preferences.getString(Constants.TwitterExtras.PREF_KEY_OAUTH_TOKEN, "");
            //String oauth_secret = preferences.getString(Constants.TwitterExtras.PREF_KEY_OAUTH_SECRET, "");

            ConfigurationBuilder configurationBuilder = new ConfigurationBuilder()
                    .setOAuthConsumerKey(getActivity().getResources().getString(R.string.twitter_api_key))
                    .setOAuthConsumerSecret(getActivity().getResources().getString(R.string.twitter_api_secret));
            TwitterFactory twitterFactory = new TwitterFactory(configurationBuilder.build());

            AccessToken accessToken = new AccessToken(Constants.TwitterExtras.TWITTER_ACCESS_TOKEN, Constants.TwitterExtras.TWITTER_ACCESS_TOKEN_SECRET);

            Twitter twitter = twitterFactory.getInstance(accessToken);
            try {
                final User user = twitter.showUser(twitter.getOAuthAccessToken().getUserId());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDisplayName.setText(user.getName());
                        ImageLoader.getInstance().displayImage(user.getOriginalProfileImageURL(),mProfilePhoto);
                        ImageLoader.getInstance().displayImage(user.getProfileBackgroundImageURL(), mProfileBackground);
                       /* Picasso.with(getActivity()).load(user.getProfileBackgroundImageURL()).into(mProfileBackground);
                        Picasso.with(getActivity()).load(user.getOriginalProfileImageURL()).into(mProfilePhoto);*/
                        Log.d("Image url", user.getBiggerProfileImageURL());
                    }
                });
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * Check user already logged in your application using twitter Login flag is
     * fetched from Shared Preferences
     */
    private boolean isTwitterLoggedInAlready() {
        // return twitter login status from Shared Preferences
        return preferences.getBoolean(Constants.TwitterExtras.PREF_KEY_TWITTER_LOGIN, false);
    }
}
