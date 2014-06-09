package com.erik.erikdroid.ui.components;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import com.erik.erikdroid.R;

public class ErikSwipeRefreshLayout extends SwipeRefreshLayout {

    public ErikSwipeRefreshLayout(Context context) {
        super(context);

    }

    public ErikSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setup() {
        setColorScheme(R.color.red, R.color.blue, R.color.yellow, R.color.green);
    }
}
