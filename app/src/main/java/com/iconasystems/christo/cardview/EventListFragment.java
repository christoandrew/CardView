package com.iconasystems.christo.cardview;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iconasystems.christo.Constants;
import com.iconasystems.christo.utils.EventListAdapter;
import com.iconasystems.christo.utils.JSONParser;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A list fragment representing a list of Events. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link EventDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class EventListFragment extends ListFragment {
    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = callBack;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(String id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks callBack = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View convertView = inflater.inflate(R.layout.fragment_event_list, container, false);

        return convertView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        new LoadNewestBars().execute();

        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        getListView().setItemChecked(getSelectedItemPosition(), true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Reset the active callbacks interface
        mCallbacks = callBack;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        TextView mEventId = (TextView) view.findViewById(R.id.event_id_list);

        if (mEventId != null){
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            final String event_id = mEventId.getText().toString();
            mCallbacks.onItemSelected(event_id);
        } else throw new NullPointerException("The Event Id Field Is not seen");


    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    class LoadNewestBars extends AsyncTask<String, String, String> {
        private JSONParser jsonParser = new JSONParser(getActivity());

        private ArrayList<HashMap<String, String>> eventsList = new ArrayList<HashMap<String, String>>();

        @Override
        public void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> data = new ArrayList<NameValuePair>();

            JSONObject jsonObject = jsonParser.makeHttpRequest(Constants.UrlConstants.url_get_all_events, "GET", data);

            Log.d("All Bars", jsonObject.toString());

            try {
                int success = jsonObject.getInt(Constants.NameConstants.TAG_SUCCESS);

                if (success == 1) {
                    JSONArray eventsArray = jsonObject.getJSONArray(Constants.NameConstants.TAG_EVENTS);

                    for (int i = 0; i < eventsArray.length(); i++) {
                        JSONObject json = eventsArray.getJSONObject(i);

                        String event_name = json.getString(Constants.NameConstants.TAG_EVENT_NAME);
                        String event_id = json.getString(Constants.NameConstants.TAG_EVENT_ID);
                        String event_image = json.getString(Constants.NameConstants.TAG_EVENT_IMAGE);

                        // Log.d("Image Urls" , "Total Images = "+imageUrls.length);

                        HashMap<String, String> hashMap = new HashMap<String, String>();

                        hashMap.put(Constants.NameConstants.TAG_EVENT_NAME, event_name);
                        hashMap.put(Constants.NameConstants.TAG_EVENT_IMAGE, event_image);
                        hashMap.put(Constants.NameConstants.TAG_EVENT_ID, event_id);


                        eventsList.add(hashMap);
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
            setListAdapter(new EventListAdapter(getActivity(), eventsList));
        }
    }


}
