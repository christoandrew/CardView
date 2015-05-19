package com.iconasystems.christo.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.iconasystems.christo.Constants;
import com.iconasystems.christo.cardview.R;
import com.iconasystems.christo.utils.EventsAttendingAdapter;
import com.iconasystems.christo.utils.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsAttendedFragment extends ListFragment {
    public static final int INDEX  = 3;

    public EventsAttendedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events_attended, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        new LoadEventsAttended().execute();
    }

    class LoadEventsAttended extends AsyncTask<String, String, String> {
        private JSONParser jsonParser = new JSONParser(getActivity());

        private ArrayList<HashMap<String, String>> usersList = new ArrayList<HashMap<String, String>>();

        private ArrayList<HashMap<String, String>> eventsList = new ArrayList<HashMap<String, String>>();

        @Override
        public void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> data = new ArrayList<NameValuePair>();
            data.add(new BasicNameValuePair(Constants.NameConstants.TAG_USER_ID, "1"));

            JSONObject jsonObject = jsonParser.makeHttpRequest(Constants.UrlConstants.url_get_events_attended, "GET", data);

            Log.d("Events Attending", jsonObject.toString());

            try {
                int success = jsonObject.getInt(Constants.NameConstants.TAG_SUCCESS);

                if (success == 1) {
                    JSONArray eventsArray = jsonObject.getJSONArray(Constants.NameConstants.TAG_EVENTS_ATTENDED);
                    JSONArray usersArray = jsonObject.getJSONArray(Constants.NameConstants.TAG_USERS_ATTENDED);

                    for (int i = 0; i < eventsArray.length(); i++) {
                        JSONObject json = eventsArray.getJSONObject(i);

                        String event_name = json.getString(Constants.NameConstants.TAG_EVENT_NAME);
                        String event_id = json.getString(Constants.NameConstants.TAG_EVENT_ID);
                        String event_image = json.getString(Constants.NameConstants.TAG_EVENT_IMAGE);
                        String event_description = json.getString(Constants.NameConstants.TAG_EVENT_DESCRIPTION);

                        // Log.d("Image Urls" , "Total Images = "+imageUrls.length);

                        HashMap<String, String> hashMap = new HashMap<String, String>();

                        hashMap.put(Constants.NameConstants.TAG_EVENT_NAME, event_name);
                        hashMap.put(Constants.NameConstants.TAG_EVENT_IMAGE, event_image);
                        hashMap.put(Constants.NameConstants.TAG_EVENT_ID, event_id);
                        hashMap.put(Constants.NameConstants.TAG_EVENT_DESCRIPTION, event_description);

                        eventsList.add(hashMap);
                    }

                    for (int j = 0; j < usersArray.length(); j++){
                        JSONObject userObject = usersArray.getJSONObject(j);
                        String username = userObject.getString(Constants.NameConstants.TAG_USERNAME);
                        String profile_pic = userObject.getString(Constants.NameConstants.TAG_PROFILE_PIC);

                        HashMap<String , String> map = new HashMap<String, String>();
                        map.put(Constants.NameConstants.TAG_USERNAME, username);
                        map.put(Constants.NameConstants.TAG_PROFILE_PIC, profile_pic);

                        usersList.add(map);
                    }
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Error Occurred", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);
            setListAdapter(new EventsAttendingAdapter(getActivity(), eventsList, usersList));
        }
    }


}
