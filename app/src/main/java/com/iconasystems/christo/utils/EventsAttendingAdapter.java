package com.iconasystems.christo.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iconasystems.christo.Constants;
import com.iconasystems.christo.cardview.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.lucasr.twowayview.widget.TwoWayView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Christo on 1/15/2015.
 */
public class EventsAttendingAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<HashMap<String,String>> eventsList;
    private ArrayList<HashMap<String,String>> usersList;
    private Activity activity;
    private TwoWayView mPhotoStrip;
    private int resId;

    public EventsAttendingAdapter(Activity activity, ArrayList<HashMap<String,String>> eventsList, ArrayList<HashMap<String,String>> usersList) {
        this.activity = activity;
        this.eventsList = eventsList;
        this.usersList = usersList;
        this.resId = resId;
    }

    @Override
    public int getCount() {
        return eventsList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_error)
                .showImageForEmptyUri(R.drawable.ic_error)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(5))
                .build();


        if (inflater == null) {
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (view == null) {
            view = inflater.inflate(R.layout.event_attending_item,null);
        }

        ImageView mEventImage = (ImageView) view.findViewById(R.id.event_image_attending);
        TextView mEventTitle = (TextView) view.findViewById(R.id.event_name_attending);
        TextView mEventDescription =(TextView) view.findViewById(R.id.event_description_attending);
        TextView mEventId = (TextView) view.findViewById(R.id.event_id_attending);
        mPhotoStrip = (TwoWayView) view.findViewById(R.id.friends_event_attending_strip);

        mPhotoStrip.setAdapter(new UsersStripAdapter(usersList, activity));

        HashMap<String, String> map = eventsList.get(position);
        String image_url = map.get(Constants.NameConstants.TAG_EVENT_IMAGE);
        mEventTitle.setText(map.get(Constants.NameConstants.TAG_EVENT_NAME));
        mEventDescription.setText(map.get(Constants.NameConstants.TAG_EVENT_DESCRIPTION));
        mEventId.setText(map.get(Constants.NameConstants.TAG_EVENT_ID));

        Log.d("Event Attending" , image_url);

        // Log.d("Event List Adapter Event Id ", map.get(Constants.NameConstants.TAG_EVENT_ID));

        ImageLoader.getInstance().displayImage(image_url, mEventImage, options);

        return view;
    }
}
