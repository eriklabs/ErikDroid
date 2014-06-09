package com.erik.erikdroid.ui.fragments;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.erik.erikdroid.R;
import com.erik.erikdroid.connection.Connection;
import com.erik.erikdroid.connection.ConnectionListener;
import com.erik.erikdroid.data.ErikDataModel;
import com.erik.erikdroid.ui.ErikDetailActivity;
import com.erik.erikdroid.ui.ErikListActivity;
import com.erik.erikdroid.ui.adapters.EndlessAdapter;
import com.erik.erikdroid.ui.adapters.EndlessErikAdapter;
import com.erik.erikdroid.ui.adapters.ErikAdapter;
import com.erik.erikdroid.ui.components.ErikSwipeRefreshLayout;
import com.erik.erikdroid.utils.AppUtils;

public class ErikListFragment extends Fragment implements ConnectionListener, OnItemClickListener {

    public static int lastScrollPosition = 0;

    protected View fragmentContent;

    ListView erikList;
    ErikSwipeRefreshLayout swipeRefreshLayout;

    boolean refreshing = false;
    Connection connection;

    public static ErikListFragment newInstance() {
        return new ErikListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        connection = new Connection(this);

        fragmentContent = inflater.inflate(R.layout.erik_list_fragment, container, false);
        swipeRefreshLayout = (ErikSwipeRefreshLayout) fragmentContent.findViewById(R.id.swipe_refresh_layout);
        erikList = (ListView) fragmentContent.findViewById(R.id.erik_list);

        setupSwipeRefresh();
        setupActionBar();
        setupList();

        requestDataIfNeeded();

        return fragmentContent;
    }

    private void requestDataIfNeeded() {
        if (ErikListActivity.erikListData != null && ErikListActivity.erikListData.size() > 0) {
            erikList.setAdapter(createAdapter());
            swipeRefreshLayout.setRefreshing(false);
        } else {
            ErikListActivity.erikListData = new ArrayList<ErikDataModel>();
            connection.getEriks(1, 0);
        }
    }

    private void setupList() {
        erikList.setOnItemClickListener(this);
        erikList.scrollTo(lastScrollPosition, 0);
    }

    private EndlessErikAdapter createAdapter() {
        return new EndlessErikAdapter(getActivity(), new ErikAdapter(getActivity(), ErikListActivity.erikListData), R.layout.endless_loading, EndlessAdapter.pageIndex, 0, true);
    }

    private void setupActionBar() {
        ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((ActionBarActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(false);
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
    }

    private void setupSwipeRefresh() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setup();
            swipeRefreshLayout.setOnRefreshListener(new RefreshListener());
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void onResponseReceived() {

        final ErikAdapter adapter = new ErikAdapter(getActivity(), ErikListActivity.erikListData);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                erikList.setAdapter(new EndlessErikAdapter(getActivity(), adapter, R.layout.endless_loading, 2, 0, true));
                if (refreshing) {
                    refreshing = false;
                    swipeRefreshLayout.setRefreshing(false);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        lastScrollPosition = erikList.getFirstVisiblePosition();

        Intent it = new Intent(getActivity(), ErikDetailActivity.class);
        it.putExtra("ERIK", ErikListActivity.erikListData.get(position));
        startActivity(it);
    }

    @Override
    public void onResponseFailed(final int errorResource, final String errorMessage) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);

                if (errorResource == -1 && errorMessage == null) {
                    AppUtils.showAlert(getActivity(), "An Unexpected Error Has Occured!", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().finish();
                        }
                    });
                    return;
                }

                if (errorResource > 0) {
                    AppUtils.showAlert(getActivity(), errorResource, new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().finish();
                        }
                    });
                    return;
                }

                if (errorMessage != null) {
                    AppUtils.showAlert(getActivity(), errorMessage, new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().finish();
                        }
                    });
                    return;
                }
            }
        });

    }

    private final class RefreshListener implements OnRefreshListener {
        @Override
        public void onRefresh() {
            refreshing = true;
            ErikListActivity.erikListData = new ArrayList<ErikDataModel>();
            connection.getEriks(1, 0);
        }
    }
}
