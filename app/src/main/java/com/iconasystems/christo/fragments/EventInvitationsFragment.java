package com.iconasystems.christo.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iconasystems.christo.cardview.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventInvitationsFragment extends Fragment {
    public static final int INDEX  = 4;

    public EventInvitationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_invitations, container, false);
    }


}
