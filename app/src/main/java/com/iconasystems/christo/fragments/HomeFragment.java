package com.iconasystems.christo.fragments;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.iconasystems.christo.cardview.R;
import com.iconasystems.christo.utils.GridMenuAdapter;
import com.iconasystems.christo.utils.JSONParser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lucasr.twowayview.widget.TwoWayView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    // This the url that contains the script that adds favorites
    private static final String url_add_to_fav = "http://10.0.3.2/trugamerz/add_to_favorites.php";
    private static final String url_get_selected_events = "http://10.0.3.2/trugamerz/get_selected_events.php";
    private static final String TAG_EVENT_IMAGE = "event_image";

    private DisplayImageOptions options;
    private CirclePageIndicator mIndicator;
    private ViewPager mTrendingPager;
    private JSONParser jsonParser;
    private TwoWayView mGridView;
    private SearchView mSearchView;

    public HomeFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
       // mTrendingPager =(ViewPager) rootView.findViewById(R.id.pager_trending);
       // mIndicator = (CirclePageIndicator) rootView.findViewById(R.id.circle_indicator);
       // mGridView = (TwoWayView) rootView.findViewById(R.id.grid_menu);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //new LoadTrendingEvents().execute();
        //mGridView.setAdapter(new GridMenuAdapter(getActivity()));
    }

    public class LoadTrendingEvents extends AsyncTask<String, String, String> {
        private static final String TAG_CODE = "code";
        private static final String TAG_SELECTED_EVENTS = "selected_events";
        private static final String TAG_EVENT_NAME = "event_name";

        private JSONArray mSelectedEvents = null;
        String[] images;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            jsonParser = new JSONParser(getActivity());

        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> data = new ArrayList<NameValuePair>();

            JSONObject mJsonObject = jsonParser.makeHttpRequest(url_get_selected_events, "GET", data);

            Log.d("TruGamerz Selected Events", mJsonObject.toString());
            try {
                int success = mJsonObject.getInt(TAG_CODE);

                if (success == 1) {
                    mSelectedEvents = mJsonObject.getJSONArray(TAG_SELECTED_EVENTS);
                    images = new String[mSelectedEvents.length()];
                    for (int i = 0 ; i < mSelectedEvents.length(); i++){
                        JSONObject details = mSelectedEvents.getJSONObject(i);
                        String event_name = details.getString(TAG_EVENT_NAME);
                        String event_image = details.getString(TAG_EVENT_IMAGE);

                        images[i] = event_image;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ImageAdapter imageAdapter = new ImageAdapter(images);
            mTrendingPager.setAdapter(imageAdapter);
            mIndicator.setViewPager(mTrendingPager);

        }
    }

    private class ImageAdapter extends PagerAdapter {
        private LayoutInflater inflater;
        private String[] image_urls = null;
        private String image_url;
        private int image_number;


        ImageAdapter(String[] urls) {
            inflater = getActivity().getLayoutInflater();
            this.image_urls = urls;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return image_urls.length;
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View imageLayout = inflater.inflate(R.layout.item_pager_image, view, false);
            assert imageLayout != null;
            ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
            final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
           // mPhotoPosition.setText(new StringBuilder().append( position + 1 ).append(" of ").append(image_urls.length()) );

            image_url = image_urls[position];

            Picasso picasso = new Picasso.Builder(getActivity())
                    .loggingEnabled(true)
                    .build();

            picasso.with(getActivity())
                    .load(image_url)
                    .into(imageView);


            /*ImageLoader.getInstance().displayImage(image_url, imageView, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    spinner.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    String message = null;
                    switch (failReason.getType()) {
                        case IO_ERROR:
                            message = "Input/Output error";
                            break;
                        case DECODING_ERROR:
                            message = "Image can't be decoded";
                            break;
                        case NETWORK_DENIED:
                            message = "Downloads are denied";
                            break;
                        case OUT_OF_MEMORY:
                            message = "Out Of Memory error";
                            break;
                        case UNKNOWN:
                            message = "Unknown error";
                            break;
                    }
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                    spinner.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    spinner.setVisibility(View.GONE);
                }
            });*/


            view.addView(imageLayout, 0);
            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }
}
