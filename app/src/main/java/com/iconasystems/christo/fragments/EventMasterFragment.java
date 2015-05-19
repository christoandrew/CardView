package com.iconasystems.christo.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iconasystems.christo.Constants;
import com.iconasystems.christo.cardview.DetailsActivity;
import com.iconasystems.christo.cardview.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventMasterFragment extends Fragment implements ListEventFragment.OnItemSelectedListener {

    private boolean mTwoPane = true;
    private ListEventFragment eventListFragment;
    private DetailsFragment detailsFragment;

    public EventMasterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_event_master, container, false);
        /*if (rootView.findViewById(R.id.detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            *//*((EventListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.event_list))
                    .setActivateOnItemClick(true);*//*
        }*/
        /*eventListFragment = (ListEventFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.event_master_list);
        detailsFragment = (DetailsFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.detail_container);*/

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        //eventListFragment.setOnItemClickListener(this);
    }


    @Override
    public void onItemSelected(String id) {
        /*if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(Constants.NameConstants.TAG_EVENT_ID, id);

            //detailsFragment.setArguments(arguments);
            *//*DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(arguments);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, fragment)
                    .commit();*//*

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(getActivity(), DetailsActivity.class);
            detailIntent.putExtra(Constants.NameConstants.TAG_EVENT_ID, id);
            startActivity(detailIntent);
        }*/
    }

}
