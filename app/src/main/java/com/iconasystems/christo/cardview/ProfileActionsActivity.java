package com.iconasystems.christo.cardview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.iconasystems.christo.Constants;
import com.iconasystems.christo.fragments.EventInvitationsFragment;
import com.iconasystems.christo.fragments.EventsAttendedFragment;
import com.iconasystems.christo.fragments.EventsAttendingFragment;
import com.iconasystems.christo.fragments.FindFriendsFragment;
import com.iconasystems.christo.fragments.FollowersFragment;
import com.iconasystems.christo.fragments.FollowingFragment;
import com.iconasystems.christo.fragments.NotificationsFragment;
import com.iconasystems.christo.fragments.RecentEventsFragment;
import com.iconasystems.christo.fragments.SettingsFragment;
import com.iconasystems.christo.fragments.ShareMyProfileFragment;


public class ProfileActionsActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_actions);

        int fragmentIndex = getIntent().getIntExtra(Constants.Extra.FRAGMENT_INDEX, 0);
        Fragment fragment;
        String tag;
        int titleRes;

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(new PlaceHolderFragment(), "Placeholder").commit();
        }

        switch (fragmentIndex) {
            default:
            case NotificationsFragment.INDEX:
                tag = NotificationsFragment.class.getSimpleName();
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new NotificationsFragment();
                }
                titleRes = R.string.notifications;
                break;
            case EventsAttendingFragment.INDEX:
                tag = EventsAttendingFragment.class.getSimpleName();
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new EventsAttendingFragment();
                }
                titleRes = R.string.events_am_attending;
                break;
            case EventsAttendedFragment.INDEX:
                tag = EventsAttendedFragment.class.getSimpleName();
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new EventsAttendedFragment();
                }
                titleRes = R.string.events_attended;
                break;
            case EventInvitationsFragment.INDEX:
                tag = EventInvitationsFragment.class.getSimpleName();
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new EventInvitationsFragment();
                }
                titleRes = R.string.event_invitation;
                break;
            case RecentEventsFragment.INDEX:
                tag = RecentEventsFragment.class.getSimpleName();
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new RecentEventsFragment();
                }
                titleRes = R.string.recent_events;
                break;
            case FindFriendsFragment.INDEX:
                tag = FindFriendsFragment.class.getSimpleName();
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new FindFriendsFragment();
                }
                titleRes = R.string.find_friends;
                break;
            case ShareMyProfileFragment.INDEX:
                tag = ShareMyProfileFragment.class.getSimpleName();
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new ShareMyProfileFragment();
                }
                titleRes = R.string.share_profile;
                break;
            case SettingsFragment.INDEX:
                tag = SettingsFragment.class.getSimpleName();
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new SettingsFragment();
                }
                titleRes = R.string.settings;
                break;
            case FollowersFragment.INDEX:
                tag = FollowersFragment.class.getSimpleName();
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new FollowersFragment();
                }
                titleRes = R.string.followers;
                break;
            case FollowingFragment.INDEX:
                tag = FollowingFragment.class.getSimpleName();
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new FollowingFragment();
                }
                titleRes = R.string.following;
                break;
        }

        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, fragment, tag).commit();



}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class PlaceHolderFragment extends Fragment{

    }
}
