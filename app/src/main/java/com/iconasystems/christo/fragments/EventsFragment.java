package com.iconasystems.christo.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iconasystems.christo.Constants;
import com.iconasystems.christo.cardview.DetailsActivity;
import com.iconasystems.christo.cardview.R;
import com.iconasystems.christo.utils.AlertDialogManager;
import com.iconasystems.christo.utils.ConnectionManager;
import com.iconasystems.christo.utils.JSONParser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lucasr.twowayview.widget.TwoWayView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.view.CardListView;


/**
 * Events Fragment has the list of all events.
 */
public class EventsFragment extends Fragment {

    private CardListView cardListView;
    private DisplayImageOptions options;
    private ProgressDialog progressDialog;
    private boolean mTwoPane = true;

    public EventsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_events, container, false);
        cardListView = (CardListView) rootView.findViewById(R.id.myList);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(10))
                .considerExifParams(true)
                .showImageForEmptyUri(R.drawable.ic_error)
                .build();

        ConnectionManager connectionManager = new ConnectionManager(getActivity());
        if (connectionManager.isConnected()){
            new LoadEvents().execute();
        }else {
            AlertDialogManager alertDialogManager = new AlertDialogManager(getActivity(),
                    SweetAlertDialog.ERROR_TYPE,
                    connectionManager.getErrorMessage(),
                    "Disconnected");
            alertDialogManager.alertDialog().show();
        }

    }


    class LoadEvents extends AsyncTask<String, String, String> {


        private JSONParser jsonParser = new JSONParser(getActivity());
        private ArrayList<HashMap<String, String>> eventsList;
        ArrayList<Card> cards = new ArrayList<Card>();
        private String eventId;

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

                        MyCard card = new MyCard(getActivity(), event_location, event_id, attendance, event_registration, event_rating);
                        final String finalEvent_name1 = event_name;
                        card.setOnClickListener(new Card.OnCardClickListener() {
                            @Override
                            public void onClick(Card card, View view) {
                                eventId = ((TextView) view.findViewById(R.id.event_id)).getText().toString();

                                Intent viewEvent = new Intent(getActivity(), DetailsActivity.class);
                                viewEvent.putExtra(Constants.NameConstants.TAG_EVENT_ID, eventId);
                                viewEvent.putExtra(Constants.NameConstants.TAG_EVENT_NAME, card.getCardHeader().getTitle());
                               // Toast.makeText(getActivity(), , Toast.LENGTH_SHORT).show();

                                startActivity(viewEvent);
                            }
                        });

                        CardHeader cardHeader = new CardHeader(getActivity());
                        cardHeader.setTitle(event_name);
                        final String finalEvent_name = event_name;
                        cardHeader.setPopupMenu(R.menu.menu_popup_event, new CardHeader.OnClickCardHeaderPopupMenuListener() {
                            @Override
                            public void onMenuItemClick(BaseCard card, MenuItem item) {
                                int item_id;

                                item_id = item.getItemId();

                                switch (item_id) {
                                    case R.id.action_add_to_wish_list:
                                        new AddToWishList().execute(finalEvent_name);
                                        break;
                                    case R.id.action_join:
                                        new RegisterEvent().execute(finalEvent_name);
                                        break;
                                    case R.id.action_add_to_fav:
                                        new AddToFavorites().execute(finalEvent_name);
                                        break;
                                    default:
                                        return;
                                }
                            }
                        });
                        card.addCardHeader(cardHeader);

                        MyThumbnail myThumbnail = new MyThumbnail(getActivity(), image_url);
                        myThumbnail.setExternalUsage(true);
                        card.addCardThumbnail(myThumbnail);

                        card.setInnerLayout(R.layout.custom_native_card_thumbnail_layout);

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

            CardArrayAdapter cardArrayAdapter = new CardArrayAdapter(getActivity(), cards);
            if (cardListView != null) {
                cardListView.setAdapter(cardArrayAdapter);
            }

            progressDialog.dismiss();
        }
    }

    class MyCard extends Card {
        private String mEventLocation;
        private String mEventID;
        private int mAttendanceNum;
        private String mRegistrationFee;
        private String event_rating;

        public MyCard(Context context, String location, String eventId, int attendance, String registration, String rating) {
            super(context);
            this.mEventLocation = location;
            this.mEventID = eventId;
            this.mAttendanceNum = attendance;
            this.mRegistrationFee = registration;
            this.event_rating = rating;
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {

            if (view != null) {
                TextView mLocation = (TextView) parent.findViewById(R.id.event_location);
                TextView mEventId = (TextView) parent.findViewById(R.id.event_id);
                TextView mAttendance = (TextView) parent.findViewById(R.id.event_attendance);
                TextView mRegistration = (TextView) parent.findViewById(R.id.event_registration_fee);
                TextView mRating = (TextView) parent.findViewById(R.id.event_rating);
                TwoWayView mFriendsStrip = (TwoWayView) parent.findViewById(R.id.friends_attending_strip);
                if (mAttendanceNum > 1) {
                    mAttendance.setText(new StringBuilder().append("" + mAttendanceNum + " ").append("people are attending."));
                } else {
                    mAttendance.setText(new StringBuilder().append(" " + mAttendanceNum + " ").append("person is attending."));
                }
                mEventId.setText(mEventID);
                mLocation.setText(mEventLocation);
                mRegistration.setText(mRegistrationFee);
                mRating.setText(event_rating);
            }
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
            ImageLoader.getInstance().displayImage(url, (ImageView) imageView, options);

            imageView.getLayoutParams().height = 250;
            imageView.getLayoutParams().width = 250;
        }
    }

    class AddToFavorites extends AsyncTask<String, String, String> {
        private static final String url_add_to_fav = "http://10.0.3.2/trugamerz/add_to_fav.php";
        private static final String TAG_RESPONSE_CODE = "code";
        private static final String TAG_MESSAGE = "message";
        private JSONParser jsonParser = new JSONParser(getActivity());

        public void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Adding To Favorites");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(true);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> data = new ArrayList<NameValuePair>();
            String user_id = "1";
            data.add(new BasicNameValuePair("user_id", user_id));
            data.add(new BasicNameValuePair("event_name", params[0]));

            JSONObject jsonObject = jsonParser.makeHttpRequest(url_add_to_fav, "GET", data);

            Log.d("TruGamerz Add Favorites", jsonObject.toString());

            try {
                int success = jsonObject.getInt(TAG_RESPONSE_CODE);

                if (success == 1) {
                    final String message = jsonObject.getString(TAG_MESSAGE);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success")
                                    .setContentText("Added To Favorites")
                                    .show();
                        }
                    });

                } else if (success == 0) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           // Toast.makeText(getActivity(), "Error Occurred", Toast.LENGTH_SHORT).show();
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Something went wrong!")
                                    .show();
                        }
                    });
                } else if (success == 3) {
                    final String message = jsonObject.getString(TAG_MESSAGE);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText(message)
                                    .show();
                        }
                    });
                } else if (success == 5) {
                    final String message = jsonObject.getString(TAG_MESSAGE);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText(message)
                                    .show();
                        }
                    });
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           // Toast.makeText(getActivity(), "Unknown Error Occurred", Toast.LENGTH_SHORT).show();
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Unknown Error Occurred")
                                    .show();
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
            progressDialog.dismiss();
        }

    }

    class AddToWishList extends AsyncTask<String, String, String> {
        private static final String url_add_to_wishlist = "http://10.0.3.2/trugamerz/add_to_wishlist.php";
        private static final String TAG_RESPONSE_CODE = "code";
        private static final String TAG_MESSAGE = "message";
        private JSONParser jsonParser = new JSONParser(getActivity());

        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> data = new ArrayList<NameValuePair>();
            String user_id = "1";
            data.add(new BasicNameValuePair("user_id", user_id));
            data.add(new BasicNameValuePair("event_name", params[0]));

            JSONObject jsonObject = jsonParser.makeHttpRequest(url_add_to_wishlist, "GET", data);

            Log.d("TruGamerz Add To Wish List", jsonObject.toString());

            try {
                int success = jsonObject.getInt(TAG_RESPONSE_CODE);

                if (success == 1) {
                    final String message = jsonObject.getString(TAG_MESSAGE);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success")
                                    .setContentText("Added To Wishlist")
                                    .show();
                        }
                    });

                } else if (success == 0) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Toast.makeText(getActivity(), "Error Occurred", Toast.LENGTH_SHORT).show();
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Something went wrong!")
                                    .show();
                        }
                    });
                } else if (success == 3) {
                    final String message = jsonObject.getString(TAG_MESSAGE);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText(message)
                                    .show();
                        }
                    });
                } else if (success == 5) {
                    final String message = jsonObject.getString(TAG_MESSAGE);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText(message)
                                    .show();
                        }
                    });
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Toast.makeText(getActivity(), "Unknown Error Occurred", Toast.LENGTH_SHORT).show();
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Unknown Error Occurred")
                                    .show();
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
        }
    }

    class RegisterEvent extends AsyncTask<String, String, String> {
        private static final String url_add_to_wishlist = "http://10.0.3.2/trugamerz/register_event.php";
        private static final String TAG_RESPONSE_CODE = "code";
        private static final String TAG_MESSAGE = "message";
        private JSONParser jsonParser = new JSONParser(getActivity());

        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> data = new ArrayList<NameValuePair>();
            String user_id = "1";
            data.add(new BasicNameValuePair("user_id", user_id));
            data.add(new BasicNameValuePair("event_name", params[0]));

            JSONObject jsonObject = jsonParser.makeHttpRequest(url_add_to_wishlist, "GET", data);

            Log.d("TruGamerz Register Event", jsonObject.toString());

            try {
                int success = jsonObject.getInt(TAG_RESPONSE_CODE);

                if (success == 1) {
                    final String message = jsonObject.getString(TAG_MESSAGE);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success")
                                    .setContentText("Registered For Event")
                                    .show();
                        }
                    });

                } else if (success == 0) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Toast.makeText(getActivity(), "Error Occurred", Toast.LENGTH_SHORT).show();
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Something went wrong!")
                                    .show();
                        }
                    });
                } else if (success == 3) {
                    final String message = jsonObject.getString(TAG_MESSAGE);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText(message)
                                    .show();
                        }
                    });
                } else if (success == 5) {
                    final String message = jsonObject.getString(TAG_MESSAGE);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText(message)
                                    .show();
                        }
                    });
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Toast.makeText(getActivity(), "Unknown Error Occurred", Toast.LENGTH_SHORT).show();
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Unknown Error Occurred")
                                    .show();
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
        }
    }

}
