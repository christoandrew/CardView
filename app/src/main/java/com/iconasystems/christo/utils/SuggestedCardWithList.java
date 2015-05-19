package com.iconasystems.christo.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iconasystems.christo.Constants;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.prototypes.CardWithList;
import it.gmariotti.cardslib.library.prototypes.LinearListView;
import it.gmariotti.cardslib.library.view.component.CardThumbnailView;
import it.gmariotti.cardslib.library.R;

/**
 * Created by Christo on 1/5/2015.
 */
public class SuggestedCardWithList extends CardWithList {
    private Activity context;
    private LinearListAdapter mLinearListAdapter;

    public SuggestedCardWithList(Activity context) {
        super(context);
        this.context = context;
        this.mLinearListAdapter = getLinearListAdapter();
    }


    @Override
    protected CardHeader initCardHeader() {
        return new SuggestedCardHeader(context);
    }

    @Override
    protected void initCard() {

    }

    @Override
    protected List<ListObject> initChildren() {
        new LoadObjects().execute();

        return null;
    }

    @Override
    public View setupChildView(int childPosition, ListObject object, View convertView, ViewGroup parent) {

        if (convertView != null) {
            SuggestionObject suggestionObject = (SuggestionObject) object;

            MyThumbnail cardThumbnail = new MyThumbnail(context, suggestionObject.mImageUrl);
            cardThumbnail.setExternalUsage(true);

            TextView title = (TextView) convertView.findViewById(R.id.suggested_title);
            TextView member = (TextView) convertView.findViewById(R.id.suggested_member);
            TextView subtitle = (TextView) convertView.findViewById(R.id.suggested_subtitle);
            TextView community = (TextView) convertView.findViewById(R.id.suggested_add);

            CardThumbnailView thumbnailView = (CardThumbnailView)
                    convertView.findViewById(R.id.suggested_card_thumbnail_layout);
            thumbnailView.addCardThumbnail(cardThumbnail);

            if (title != null)
                title.setText(suggestionObject.mEventName);

            if (member != null)
                member.setText("" + suggestionObject.mAttendanceNum);

            if (subtitle != null)
                subtitle.setText(suggestionObject.mEventDescription);

            if (community != null) {
                community.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }

        }

        return convertView;
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.custom_suggested_layout;
    }


    class SuggestedCardHeader extends CardHeader {

        public SuggestedCardHeader(Context context) {
            this(context, it.gmariotti.cardslib.library.R.layout.custom_suggested_header_inner);
        }

        public SuggestedCardHeader(Context context, int innerLayout) {
            super(context, innerLayout);
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {

            if (view != null) {
                TextView textView = (TextView) view.findViewById(R.id.text_suggested_title);

                if (textView != null) {
                    textView.setText(mContext.getString(com.iconasystems.christo.cardview.R.string.suggested_label));
                }
            }
        }
    }

    public class SuggestionObject extends DefaultListObject {
        private String mImageUrl;
        private String mEventId;
        private String mAttendanceNum;
        private String mFriendsAttending;
        private String mEventName;
        private String mEventLocation;
        private String mEventRegistration;
        private String mEventDescription;


        public SuggestionObject(Card parentCard) {
            super(parentCard);
            init();
        }

        private void init() {
            //OnClick Listener
            setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(LinearListView parent, View view, int position, ListObject object) {
                    Toast.makeText(getContext(), "Click on " + getObjectId(), Toast.LENGTH_SHORT).show();
                }
            });

        }

        @Override
        public String getObjectId() {
            return mEventId;
        }
    }


    class MyThumbnail extends CardThumbnail {
        private String mImageUrl;

        public MyThumbnail(Context context, String imageUrl) {
            super(context);
            this.mImageUrl = imageUrl;
        }

        @Override
        public void setupInnerViewElements(final ViewGroup parent, final View imageView) {

            ImageLoader.getInstance()
                    .displayImage(mImageUrl, (ImageView) imageView, new SimpleImageLoadingListener() {
                        final ProgressBar spinner = (ProgressBar) parent.findViewById(R.id.load_image);

                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            spinner.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            String message = null;
                            switch (failReason.getType()) {
                                case IO_ERROR:
                                    message = context.getString(com.iconasystems.christo.cardview.R.string.io_error);
                                    break;
                                case DECODING_ERROR:
                                    message = context.getString(com.iconasystems.christo.cardview.R.string.decode_error);
                                    break;
                                case NETWORK_DENIED:
                                    message = context.getString(com.iconasystems.christo.cardview.R.string.network_denied_error);
                                    break;
                                case OUT_OF_MEMORY:
                                    message = context.getString(com.iconasystems.christo.cardview.R.string.memory_error);
                                    break;
                                case UNKNOWN:
                                    message = context.getString(com.iconasystems.christo.cardview.R.string.unknown_error);
                                    break;
                            }
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                            spinner.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            spinner.setVisibility(View.GONE);
                        }

                    });
            return;
        }
    }


    class LoadObjects extends AsyncTask<String, String, ArrayList<SuggestionObject>> {
        private static final String TAG_EVENT_ATTENDANCE = "attendance";
        private static final String TAG_EVENT_NAME = "event_name";
        private static final String TAG_EVENT_ID = "event_id";
        private static final String TAG_EVENT_LOCATION = "event_venue";
        private static final String TAG_EVENT_IMAGE = "event_image";
        private static final String TAG_EVENTS = "events";
        private static final String url_get_all_events = "http://10.0.3.2/trugamerz/get_all_events.php";
        private static final String TAG_SUCCESS = "code";
        private static final String TAG_EVENT_REGISTRATION_FEE = "event_registration";
        private JSONParser jsonParser = new JSONParser(context);

        private ArrayList<SuggestionObject> myObjects = new ArrayList<SuggestionObject>();

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected ArrayList<SuggestionObject> doInBackground(String... params) {
            List<NameValuePair> data = new ArrayList<NameValuePair>();
            String image_url;
            String event_name = null;
            String event_location = null;
            String event_id = null;
            String event_registration;
            String attendance;
            String event_description;

            JSONObject jsonObject = jsonParser.makeHttpRequest(url_get_all_events, "GET", data);

            try {
                int success = jsonObject.getInt(TAG_SUCCESS);

                if (success == 1) {
                    JSONArray events = jsonObject.getJSONArray(TAG_EVENTS);
                    for (int i = 0; i < events.length(); i++) {
                        JSONObject event = events.getJSONObject(i);

                        image_url = event.getString(TAG_EVENT_IMAGE);
                        event_name = event.getString(TAG_EVENT_NAME);
                        event_location = event.getString(TAG_EVENT_LOCATION);
                        event_id = event.getString(TAG_EVENT_ID);
                        attendance = event.getString(TAG_EVENT_ATTENDANCE);
                        event_registration = event.getString(TAG_EVENT_REGISTRATION_FEE);
                        event_description = event.getString(Constants.NameConstants.TAG_EVENT_DESCRIPTION);

                        final String finalEvent_name = event_name;
                        final String finalImage_url = image_url;
                        final String finalEvent_id = event_id;
                        final String finalAttendance = attendance;
                        final String finalEvent_location = event_location;
                        final String finalEvent_registration = event_registration;
                        final String finalEventDescription = event_description;

                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateItems(finalEvent_name, finalImage_url, finalEvent_id,
                                        finalAttendance, finalEvent_location, finalEvent_registration, finalEventDescription);
                            }
                        });

                        // Log.d("Card List", image_url+""+event_name+""+event_location+""+event_id+""+attendance+""+event_registration);


                    }
                } else if (success == 5) {
                    Toast.makeText(context, context.getString(com.iconasystems.christo.cardview.R.string.no_events_label), Toast.LENGTH_SHORT).show();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return myObjects;
        }

        @Override
        protected void onPostExecute(ArrayList<SuggestionObject> result) {
            super.onPostExecute(result);

        }

        public void updateItems(String event_name, String image_url, String event_id, String attendance, String location, String registration, String event_description) {
            SuggestionObject suggestionObject = new SuggestionObject(SuggestedCardWithList.this);
            suggestionObject.mImageUrl = image_url;
            suggestionObject.mAttendanceNum = attendance;
            suggestionObject.mEventRegistration = registration;
            suggestionObject.mEventName = event_name;
            suggestionObject.mEventId = event_id;
            suggestionObject.mEventLocation = location;
            suggestionObject.mEventDescription = event_description;

            getLinearListAdapter().add(suggestionObject);
        }
    }
}
