package com.erik.erikdroid.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.erik.erikdroid.R;
import com.erik.erikdroid.data.ErikDataModel;
import com.erik.erikdroid.ui.fragments.ErikListFragment;

public class ErikListActivity extends ActionBarActivity {

    static FragmentTransaction ft;
    static FragmentManager fm;

    ErikListFragment erikListFragment;

    public static ArrayList<ErikDataModel> erikListData;
    public static int totalPageCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().show();
        
        setupFragment();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.aboutDialogMessage).setTitle(R.string.app_name);
            builder.setPositiveButton(R.string.dialogOk, null);
            builder.setIcon(R.drawable.ic_launcher);

            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("Recycle")
    public void setupFragment() {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        if (erikListFragment == null) {
            erikListFragment = ErikListFragment.newInstance();
        }
        ft.replace(R.id.frame_main, erikListFragment);
        ft.commit();
    }

}
