package com.erik.erikdroid.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import com.erik.erikdroid.R;
import com.erik.erikdroid.data.ErikDataModel;
import com.erik.erikdroid.ui.fragments.ErikDetailFragment;
import com.erik.erikdroid.ui.fragments.ErikListFragment;

public class ErikDetailActivity extends ActionBarActivity {

    static FragmentTransaction ft;
    static FragmentManager fm;

    ErikListFragment erikListFragment;
    static ErikDetailFragment erikDetailFragment;

    public static ArrayList<ErikDataModel> erikListData;
    public static int totalPageCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().show();

        setupFragment((ErikDataModel) getIntent().getSerializableExtra("ERIK"));
    }

    @SuppressLint("Recycle")
    public void setupFragment(ErikDataModel selectedErik) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        erikDetailFragment = ErikDetailFragment.newInstance(selectedErik);
        ft.replace(R.id.frame_main, erikDetailFragment);
        ft.commit();
    }

}
