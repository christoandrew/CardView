package com.iconasystems.christo.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.iconasystems.christo.Constants;
import com.iconasystems.christo.cardview.R;
import com.iconasystems.christo.utils.JSONParser;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardGridView;

/**
 * A simple {@link Fragment} subclass.
 */
public class GridEventsFragment extends Fragment {
    private CardGridView cardGridView;

    public GridEventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_grid_events, container, false);
        cardGridView = (CardGridView) rootView.findViewById(R.id.grid_events);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        new LoadEvents().execute();

    }

    class MyGridCard extends Card{
        private String event_name;
        private String event_rating;
        private String event_description;
        private String event_attendance;

        public MyGridCard(Context context) {
            super(context);
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {

        }
    }
    class LoadEvents extends AsyncTask<String, String, String> {


        private JSONParser jsonParser = new JSONParser(getActivity());
        private ArrayList<HashMap<String, String>> eventsList;
        ArrayList<Card> cards = new ArrayList<Card>();
        private String eventId;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading Events...Please Wait");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> data = new ArrayList<NameValuePair>();
            String image_url;
            String event_name = null;
            String event_location = null;
            String event_id = null;
            String event_rating;
            String event_registration;
            int attendance;
            JSONObject jsonObject = jsonParser.makeHttpRequest(Constants.UrlConstants.url_get_all_events, "GET", data);

            Log.d("All Events", jsonObject.toString());

            try {
                int success = jsonObject.getInt(Constants.NameConstants.TAG_SUCCESS);

                if (success == 1) {
                    JSONArray events = jsonObject.getJSONArray(Constants.NameConstants.TAG_EVENTS);
                    for (int i = 0; i < events.length(); i++) {
                        JSONObject event = events.getJSONObject(i);

                        image_url = event.getString(Constants.NameConstants.TAG_EVENT_IMAGE);
                        event_name = event.getString(Constants.NameConstants.TAG_EVENT_NAME);
                        event_location = event.getString(Constants.NameConstants.TAG_EVENT_LOCATION);
                        event_id = event.getString(Constants.NameConstants.TAG_EVENT_ID);
                        attendance = event.getInt(Constants.NameConstants.TAG_EVENT_ATTENDANCE);
                        event_registration = event.getString(Constants.NameConstants.TAG_EVENT_REGISTRATION_FEE);
                        event_rating = event.getString(Constants.NameConstants.TAG_EVENT_RATING);

                        Log.d("TruGamerz View Image Url", image_url);

                        MyGridCard card = new MyGridCard(getActivity());

                        CardHeader cardHeader = new CardHeader(getActivity());
                        cardHeader.setTitle(event_name);

                        card.addCardHeader(cardHeader);

                        MyThumbnail myThumbnail = new MyThumbnail(getActivity(), image_url);
                        myThumbnail.setExternalUsage(true);
                        card.addCardThumbnail(myThumbnail);

                        card.setInnerLayout(R.layout.custom_grid_card_thumbnail_layout);

                        cards.add(card);
                    }
                } else if (success == 5) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "No Events Found", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            CardGridArrayAdapter cardArrayAdapter = new CardGridArrayAdapter(getActivity(), cards);
            if (cardGridView != null) {
                cardGridView.setAdapter(cardArrayAdapter);
            }

            progressDialog.dismiss();
        }
    }

    class MyThumbnail extends CardThumbnail {
        private String url = null;

        public MyThumbnail(Context context, String Url) {
            super(context);
            this.url = Url;
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View imageView) {
            if (imageView != null ) {
                Picasso.with(getContext()).load(url).into((ImageView) imageView);

            }
        }
    }


}
