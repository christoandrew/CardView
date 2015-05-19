package com.iconasystems.christo;

/**
 * Created by Christo on 1/15/2015.
 */
public class Constants {

    public Constants() {
    }

    public static class NameConstants{
        public static final String TAG_SUCCESS = "code";
        public static final String TAG_EVENT_REGISTRATION_FEE = "event_registration";
        public static final String TAG_EVENT_RATING = "rating";
        public static final String TAG_EVENT_ATTENDANCE = "attendance";
        public static final String TAG_EVENT_NAME = "event_name";
        public static final String TAG_EVENT_ID = "event_id";
        public static final String TAG_EVENT_LOCATION = "event_venue";
        public static final String TAG_EVENT_IMAGE = "event_image";
        public static final String TAG_EVENTS = "events";
        public static final String TAG_MY_EVENTS = "my_events";
        public static final String TAG_EVENT_DESCRIPTION = "event_description";
        public static final String TAG_EVENT_DAYS_AGO = "days_ago";
        public static final String TAG_EVENT_DAYS_REMAINING = "days_remaining";
        public static final String TAG_EVENT_DATE_ADDED = "date_added";
        public static final String TAG_USERS_ATTENDING = "users_attending";
        public static final String TAG_USERS_ATTENDED = "users_attended";
        public static final String TAG_USERNAME = "username";
        public static final String TAG_USER_ID = "user_id";
        public static final String TAG_PROFILE_PIC = "profile_pic";
        public static final String TAG_EVENTS_ATTENDED = "events_attended";
        public static final String TAG_ATT_YES = "att_yes";
        public static final String TAG_ATT_MAYBE = "att_maybe";
        public static final String TAG_ATT_NO = "att_no";
        public static final String TAG_YES = "Yes";
        public static final String TAG_NO = "No";
        public static final String TAG_MAYBE = "Maybe";
        public static final String TAG_MESSAGE = "message";
        public static final String TAG_STATUS = "status";
        public static final String TAG_EVENT_DETAILS = "event_details";
        public static final String TAG_EVENT_AUTHOR = "event_added_by";
        public static final String TAG_SEARCH_CRITERIA = "search_criteria";
    }

    public static class UrlConstants{
        public static final String url_get_all_events = "http://10.0.3.2:99/trugamerz/get_all_events.php";
        public static final String url_get_my_events = "http://10.0.3.2:99/trugamerz/get_my_events.php";
        public static final String url_get_selected_events = "http://10.0.3.2:99/trugamerz/get_selected_events.php";
        public static final String url_get_event_user_attendance = "http://10.0.3.2:99/trugamerz/get_attendance.php";
        public static final String url_get_event_details = "http://10.0.3.2:99/trugamerz/get_event_details.php";
        public static final String url_register_event = "http://10.0.3.2:99/trugamerz/register_event.php";
        public static final String url_add_to_fav = "http://10.0.3.2:99/trugamerz/add_to_fav.php";
        public static final String url_get_images = "http://10.0.3.2:99/trugamerz/get_images.php";
        public static final String url_add_friend = "http:///10.0.3.2:99/trugamerz/add_friend.php";
        public static final String url_get_events_attended = "http://10.0.3.2:99/trugamerz/get_events_attended.php";
        public static final String url_get_details = "http://10.0.3.2:99/trugamerz/get_event_details.php";
        public static final String url_update_status = "http://10.0.3.2:99/trugamerz/update_event_att_status.php";
        public static final String url_search_events = "http://10.0.3.2:99/trugamerz/search_events.php";
    }

    public static class Extra{
        public static final String FRAGMENT_INDEX = "com.iconasystems.christo.cardview.FRAGMENT_INDEX";
    }

    public static class TwitterExtras{
        // Constants
        /**
         * Register your here app https://dev.twitter.com/apps/new and get your
         * consumer key and secret
         */
        public static String TWITTER_CONSUMER_KEY = "K0v1DOIqT7oL1hCmICtJDn0uj";
        public static String TWITTER_CONSUMER_SECRET = "zIqfnjGV0VXYnOsYuBDP14anTvg1BtBUOSw8GkjQKv11nQbvKA";
        public static String TWITTER_ACCESS_TOKEN = "230804941-9EYUD72TL151coSuaCz4CV10RNtHSrABYRJxHPXw";
        public static String TWITTER_ACCESS_TOKEN_SECRET = "SFQo3KN5ZyQIgt56RV79GvvzXSdpwQAM5OmQpr7FWAKr5";

        // Preference Constants
        public static String PREFERENCE_NAME = "twitter_oauth";
        public static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
        public static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
        public static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";

        public static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";

        // Twitter oauth urls
        public static final String URL_TWITTER_AUTH = "auth_url";
        public static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
        public static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
    }
}
