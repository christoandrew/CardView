package com.iconasystems.christo.cardview;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.iconasystems.christo.Constants;
import com.iconasystems.christo.utils.JSONParser;
import com.iconasystems.christo.utils.SuggestedCardWithList;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.view.CardViewNative;

/**
 * A fragment representing a single Event detail screen.
 * This fragment is either contained in a {@link EventListActivity}
 * in two-pane mode (on tablets) or a {@link EventDetailActivity}
 * on handsets.
 */
public class EventDetailFragment extends Fragment {
    private TextView mEventDescription;
    private TextView mEventAuthor;
    private ImageView mEventImage;
    private RatingBar mEventRating;
    private CardViewNative cardView;

    /**
     * The id of the event to be displayed.
     */
    private String mEventId;
    private JSONParser jsonParser;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(Constants.NameConstants.TAG_EVENT_ID)) {
            /**
             * Get the event id from the arguments
             */
           mEventId = getArguments().getString(Constants.NameConstants.TAG_EVENT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_detail, container, false);

        mEventImage = (ImageView) rootView.findViewById(R.id.event_image_fragment);
        mEventDescription = (TextView) rootView.findViewById(R.id.event_description_fragment);
        mEventAuthor = (TextView) rootView.findViewById(R.id.event_author_username_fragment);
        cardView = (CardViewNative) rootView.findViewById(R.id.card_suggested_fragment);
        mEventRating = (RatingBar) rootView.findViewById(R.id.event_rating_fragment);

        return rootView;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        jsonParser = new JSONParser(getActivity());
        new LoadDetails().execute();
        initSuggestedCard();
    }

    class LoadDetails extends AsyncTask<String, String, String> {

        private static final String TAG_RESPONSE_CODE = "code";
        private static final String TAG_EVENT_DETAILS = "event_details";
        private static final String TAG_EVENT_IMAGE = "event_image";
        private static final String TAG_EVENT_DESCRIPTION = "event_description";
        private static final String TAG_EVENT_AUTHOR = "event_added_by";
        private static final String TAG_EVENT_NAME = "event_name";
        private static final String TAG_RATING = "rating";
        private String url_get_details = "http://10.0.3.2/trugamerz/get_event_details.php";

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> data = new ArrayList<NameValuePair>();
            data.add(new BasicNameValuePair(Constants.NameConstants.TAG_EVENT_ID, mEventId));

            JSONObject resultJsonObject = jsonParser.makeHttpRequest(url_get_details, "GET", data);

            try {
                int responseCode = resultJsonObject.getInt(TAG_RESPONSE_CODE);
                if (responseCode == 1){
                    JSONArray resultArray = resultJsonObject.getJSONArray(TAG_EVENT_DETAILS);
                    JSONObject event_details = resultArray.getJSONObject(0);

                    String event_image = event_details.getString(TAG_EVENT_IMAGE);
                    String event_description = event_details.getString(TAG_EVENT_DESCRIPTION);
                    String event_author = event_details.getString(TAG_EVENT_AUTHOR);
                    String event_name = event_details.getString(TAG_EVENT_NAME);
                    // int event_rating = event_details.getInt(TAG_RATING);

                    final String finalEventImage = event_image;
                    final String finalEventDescription = event_description;
                    final String finalEventAuthor = event_author;
                    final String finalEventName = event_name;

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Picasso.with(getActivity()).load(finalEventImage).into(mEventImage);
                             mEventDescription.setText(finalEventDescription);
                             mEventAuthor.setText(new StringBuilder().append(" added by ").append(finalEventAuthor));
                        }
                    });
                    // mEventRating.setRating(event_rating);

                    Log.d("Details event image", event_image);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public void initSuggestedCard() {
        SuggestedCardWithList suggestedCardWithList = new SuggestedCardWithList(getActivity());
        suggestedCardWithList.init();
        cardView.setCard(suggestedCardWithList);
    }
}
