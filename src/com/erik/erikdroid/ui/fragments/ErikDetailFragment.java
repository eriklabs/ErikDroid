package com.erik.erikdroid.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.erik.erikdroid.R;
import com.erik.erikdroid.data.ErikDataModel;
import com.erik.erikdroid.ui.adapters.ErikDetailPagerAdapter;
import com.erik.erikdroid.utils.AppUtils;
import com.erik.erikdroid.utils.DepthPageTransformer;
import com.viewpagerindicator.UnderlinePageIndicator;

public class ErikDetailFragment extends Fragment {

    protected View fragmentContent;
    private ViewPager mPager;
    private UnderlinePageIndicator mPagerIndicator;
    private ErikDataModel mErik;

    public static ErikDetailFragment newInstance(ErikDataModel selectedErik) {
        ErikDetailFragment f = new ErikDetailFragment();
        f.mErik = selectedErik;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentContent = inflater.inflate(R.layout.erik_detail_fragment, container, false);

        mPager = (ViewPager) fragmentContent.findViewById(R.id.erik_detail_pager);
        mPagerIndicator = (UnderlinePageIndicator) fragmentContent.findViewById(R.id.pager_indicator);

        setupActionBar();
        setupPager();
        mPagerIndicator.setViewPager(mPager);

        return fragmentContent;
    }

    private void setupPager() {
        ErikDetailPagerAdapter pagerAdapter = new ErikDetailPagerAdapter(getActivity(), mErik.getAvailableImages());
        mPager.setAdapter(pagerAdapter);
        mPager.setPageTransformer(true, new DepthPageTransformer());
    }

    private void setupActionBar() {
        ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((ActionBarActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        String title = AppUtils.convertToHumanReadable(mErik.Title);
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(title.isEmpty() ? getString(R.string.app_name) : title);
    }

}
