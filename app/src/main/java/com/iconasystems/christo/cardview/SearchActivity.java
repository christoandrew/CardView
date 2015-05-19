package com.iconasystems.christo.cardview;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.iconasystems.christo.Constants;
import com.iconasystems.christo.utils.EventListAdapter;
import com.iconasystems.christo.utils.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SearchActivity extends ListActivity {
    private JSONParser jsonParser = new JSONParser(SearchActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        handleIntent(getIntent());
    }

    public void onNewIntent(Intent intent){
        setIntent(intent);
        handleIntent(intent);
    }

    public void handleIntent(Intent intent){
        if (Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);

            doSearch(query);
        }
    }

    private void doSearch(String query) {
        new LoadResults().execute(query);
    }

    class LoadResults extends AsyncTask<String, String, String> {
        private ArrayList<HashMap<String, String>> eventsList = new ArrayList<HashMap<String, String>>();

        @Override
        public void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> data  = new ArrayList<NameValuePair>();
            data.add(new BasicNameValuePair(Constants.NameConstants.TAG_SEARCH_CRITERIA, params[0]));

            JSONObject jsonObject = jsonParser.makeHttpRequest(Constants.UrlConstants.url_search_events, "GET", data);

            Log.d("Search Results", jsonObject.toString());

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
                    SearchActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SearchActivity.this, "Error Occurred", Toast.LENGTH_LONG).show();
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
            setListAdapter(new EventListAdapter(SearchActivity.this, eventsList));
        }
    }
}
