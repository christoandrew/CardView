

package com.iconasystems.christo.fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.iconasystems.christo.Constants;
import com.iconasystems.christo.cardview.R;
import com.iconasystems.christo.utils.JSONParser;
import com.iconasystems.christo.utils.SuggestedCardWithList;
import com.iconasystems.christo.utils.UsersStripAdapter;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lucasr.twowayview.widget.TwoWayView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.gmariotti.cardslib.library.view.CardViewNative;

public class DetailsFragment extends Fragment {

    private static final String TAG_EVENT_ID = "event_id";
   // private String event_id;
   // private String event_name;

    private TextView mEventId;
    private TextView mEventTitle;
    private TextView mEventDescription;
    private ImageView mEventImage;
    private RatingBar mEventRating;
    private CardViewNative cardView;
    private TwoWayView mPeopleAttending;
    private TextView mYesButton;
    private TextView mMaybeButton;
    private TextView mNoButton;
    private TextView mEventAdded;
    private ImageView mOpenOptions;
    private ViewGroup container = null;
    private ProgressWheel progressWheel;
    private JSONParser jsonParser;

    private LayoutTransition mLayoutTransition;


    public DetailsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLayoutTransition = new LayoutTransition();
        mLayoutTransition.setDuration(300);
        mLayoutTransition.setStagger(LayoutTransition.CHANGE_APPEARING, 30);
        mLayoutTransition.setStagger(LayoutTransition.CHANGE_DISAPPEARING, 30);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        mEventId = (TextView) rootView.findViewById(R.id.event_id_fragment);
        mEventImage = (ImageView) rootView.findViewById(R.id.event_image_fragment);
        mEventDescription = (TextView) rootView.findViewById(R.id.event_description_fragment);
        cardView = (CardViewNative) rootView.findViewById(R.id.card_suggested_fragment);
        mEventRating = (RatingBar) rootView.findViewById(R.id.event_rating_fragment);
        mEventTitle = (TextView) rootView.findViewById(R.id.event_details_title);
        mEventAdded = (TextView) rootView.findViewById(R.id.event_added);
        mPeopleAttending = (TwoWayView) rootView.findViewById(R.id.people_attending_photo_strip);
        mYesButton = (TextView) rootView.findViewById(R.id.attending_yes);
        mMaybeButton = (TextView) rootView.findViewById(R.id.attending_maybe);
        mNoButton = (TextView) rootView.findViewById(R.id.attending_no);
        mOpenOptions = (ImageView) rootView.findViewById(R.id.open_options);
        progressWheel = (ProgressWheel) rootView.findViewById(R.id.loading_details);
        container = (ScrollView) rootView.findViewById(R.id.container_details);
        container.setLayoutTransition(mLayoutTransition);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        jsonParser = new JSONParser(getActivity());

        new LoadDetails().execute();

        Typeface titleTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/comfortaa.ttf");
        mEventTitle.setTypeface(titleTypeface);

        Typeface descTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/open-sans.ttf");
        mEventDescription.setTypeface(descTypeface);

        mOpenOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationAppear(new View[]{mYesButton, mMaybeButton, mNoButton}, mOpenOptions);
            }
        });
        mYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpdateStatus().execute("att_yes");
                animateDisappear("Yes");
            }
        });

        mMaybeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpdateStatus().execute("att_maybe");
                animateDisappear("Maybe");
            }
        });

        mNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpdateStatus().execute("att_no");
                animateDisappear("No");
            }
        });

        //mEventId.setText(event_id);
        mEventRating.isIndicator();
        mEventRating.setNumStars(5);
        mEventRating.setClickable(false);

        initSuggestedCard();

    }

    private void animateDisappear(String buttonClicked) {

        switch (buttonClicked) {
            default:
            case "No":
                animatorDisappear(new View[]{mYesButton, mMaybeButton}, mOpenOptions);
                break;
            case "Yes":
                animatorDisappear(new View[]{mNoButton, mMaybeButton}, mOpenOptions);
                break;
            case "Maybe":
                animatorDisappear(new View[]{mNoButton, mYesButton}, mOpenOptions);
                break;
        }
    }

    public void animatorDisappear(final View[] visibleBtns, final View invisible) {
        ObjectAnimator appearInvisible = ObjectAnimator.ofFloat(invisible, "alpha", 0, 1);
        appearInvisible.setDuration(1000);
        appearInvisible.setInterpolator(new AccelerateInterpolator());
        appearInvisible.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                invisible.setVisibility(View.VISIBLE);
            }
        });

        for (int i = 0; i < visibleBtns.length; i++) {
            final View visibleBtn = visibleBtns[i];

            ObjectAnimator disAppButton = ObjectAnimator.ofFloat(visibleBtn, "alpha", 1, 0);
            disAppButton.setDuration(2000);
            disAppButton.setInterpolator(new AnticipateOvershootInterpolator());
            disAppButton.start();
            disAppButton.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    visibleBtn.setVisibility(View.GONE);
                }
            });
        }

        appearInvisible.start();
    }

    public void animationAppear(final View[] invisibleBtns, final View optionsButton) {
        final ObjectAnimator disappearOpenOptions = ObjectAnimator.ofFloat(optionsButton, "alpha", 1, 0);
        disappearOpenOptions.setDuration(2000);
        disappearOpenOptions.setInterpolator(new AnticipateOvershootInterpolator());
        disappearOpenOptions.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                optionsButton.setVisibility(View.INVISIBLE);
            }
        });

        for (int i = 0; i < invisibleBtns.length; i++) {
            final View invisibleBtn = invisibleBtns[i];
            if (invisibleBtn.getVisibility() == View.GONE) {
                ObjectAnimator disAppButton = ObjectAnimator.ofFloat(invisibleBtn, "alpha", 0, 1);
                disAppButton.setDuration(2000);
                disAppButton.setInterpolator(new AnticipateOvershootInterpolator());
                disAppButton.start();
                disAppButton.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        invisibleBtn.setVisibility(View.VISIBLE);

                    }
                });
            }
        }
        disappearOpenOptions.start();
    }

    class UpdateStatus extends AsyncTask<String, String, String> {

        private String message;

        @Override
        protected String doInBackground(String... params) {
            String event_id = getArguments().getString(Constants.NameConstants.TAG_EVENT_ID);
            List<NameValuePair> data = new ArrayList<NameValuePair>();
            data.add(new BasicNameValuePair(Constants.NameConstants.TAG_EVENT_ID, event_id));
            data.add(new BasicNameValuePair(Constants.NameConstants.TAG_USER_ID, "1"));
            data.add(new BasicNameValuePair(params[0], params[0]));

            JSONObject jsonObject = jsonParser.makeHttpRequest(Constants.UrlConstants.url_update_status, "POST", data);

            try {
                int code = jsonObject.getInt(Constants.NameConstants.TAG_SUCCESS);
                message = jsonObject.getString(Constants.NameConstants.TAG_MESSAGE);

                Log.d("Update status code", "code = " + code + " "+message);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);

        }
    }

    class LoadDetails extends AsyncTask<String, String, HashMap<String, String>> {

        private ArrayList<HashMap<String, String>> usersList = new ArrayList<HashMap<String, String>>();
        private HashMap<String, String> eventDetails = new HashMap<String, String>();

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            progressWheel.setVisibility(View.VISIBLE);
        }

        @Override
        protected HashMap doInBackground(String... params) {
            String event_name_data = getArguments().getString(Constants.NameConstants.TAG_EVENT_NAME);
            List<NameValuePair> data = new ArrayList<NameValuePair>();
            data.add(new BasicNameValuePair(Constants.NameConstants.TAG_EVENT_NAME, event_name_data));

            JSONObject resultJsonObject = jsonParser.makeHttpRequest(Constants.UrlConstants.url_get_details, "GET", data);

            Log.d("Event Details", resultJsonObject.toString());

            try {
                int responseCode = resultJsonObject.getInt(Constants.NameConstants.TAG_SUCCESS);
                if (responseCode == 1) {
                    JSONArray resultArray = resultJsonObject.getJSONArray(Constants.NameConstants.TAG_EVENT_DETAILS);
                    JSONObject event_details = resultArray.getJSONObject(0);

                    final String event_image = event_details.getString(Constants.NameConstants.TAG_EVENT_IMAGE);
                    final String event_description = event_details.getString(Constants.NameConstants.TAG_EVENT_DESCRIPTION);
                    final String event_author = event_details.getString(Constants.NameConstants.TAG_EVENT_AUTHOR);
                    final String event_name = event_details.getString(Constants.NameConstants.TAG_EVENT_NAME);
                    final String status = event_details.getString(Constants.NameConstants.TAG_STATUS);
                    final String days_ago = event_details.getString(Constants.NameConstants.TAG_EVENT_DAYS_AGO);

                    eventDetails.put(Constants.NameConstants.TAG_EVENT_NAME, event_name);
                    eventDetails.put(Constants.NameConstants.TAG_EVENT_IMAGE, event_image);
                    eventDetails.put(Constants.NameConstants.TAG_EVENT_DESCRIPTION, event_description);
                    eventDetails.put(Constants.NameConstants.TAG_STATUS, status);
                    eventDetails.put(Constants.NameConstants.TAG_EVENT_DAYS_AGO, days_ago);
                    eventDetails.put(Constants.NameConstants.TAG_EVENT_AUTHOR, event_author);

                    JSONArray usersArray = resultJsonObject.getJSONArray(Constants.NameConstants.TAG_USERS_ATTENDING);
                    for (int i = 0; i < usersArray.length(); i++) {
                        JSONObject userObject = usersArray.getJSONObject(i);
                        String profile_pic = userObject.getString(Constants.NameConstants.TAG_PROFILE_PIC);

                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(Constants.NameConstants.TAG_PROFILE_PIC, profile_pic);

                        usersList.add(map);
                    }
                    //setData(getActivity(), event_description, days_ago, event_author, status, event_name, event_image);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return eventDetails;
        }

        @Override
        public void onPostExecute(HashMap result) {
            super.onPostExecute(result);
            mPeopleAttending.setAdapter(new UsersStripAdapter(usersList, getActivity()));
            setDetails(getActivity(), result);
            progressWheel.setVisibility(View.GONE);
        }
    }


    public void setDetails(Context context, HashMap<String, String> details) {
        String event_image = details.get(Constants.NameConstants.TAG_EVENT_IMAGE);
        String status = details.get(Constants.NameConstants.TAG_STATUS);
        String author = details.get(Constants.NameConstants.TAG_EVENT_AUTHOR);
        String days_ago = details.get(Constants.NameConstants.TAG_EVENT_DAYS_AGO);
        mEventTitle.setText(details.get(Constants.NameConstants.TAG_EVENT_NAME));
        mEventDescription.setText(details.get(Constants.NameConstants.TAG_EVENT_DESCRIPTION));
        mEventAdded.setText(new StringBuilder().append(days_ago).append(" days ago by ").append(author));
        setStatus(status);
        Picasso.with(context).load(event_image).into(mEventImage);
    }

    public void setStatus(String status) {
        switch (status) {
            default:
            case Constants.NameConstants.TAG_YES:
                mYesButton.setVisibility(View.VISIBLE);
                mNoButton.setVisibility(View.GONE);
                mMaybeButton.setVisibility(View.GONE);
                mOpenOptions.setVisibility(View.VISIBLE);
                break;
            case Constants.NameConstants.TAG_MAYBE:
                mYesButton.setVisibility(View.GONE);
                mMaybeButton.setVisibility(View.VISIBLE);
                mNoButton.setVisibility(View.GONE);
                mOpenOptions.setVisibility(View.VISIBLE);
                break;
            case Constants.NameConstants.TAG_NO:
                mMaybeButton.setVisibility(View.GONE);
                mNoButton.setVisibility(View.VISIBLE);
                mOpenOptions.setVisibility(View.VISIBLE);
                mYesButton.setVisibility(View.GONE);
                break;
        }
    }

    public void initSuggestedCard() {
        SuggestedCardWithList suggestedCardWithList = new SuggestedCardWithList(getActivity());
        suggestedCardWithList.init();
        cardView.setCard(suggestedCardWithList);
    }

}
