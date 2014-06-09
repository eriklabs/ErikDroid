package com.erik.erikdroid.ui.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ListAdapter;

import com.erik.erikdroid.R;
import com.erik.erikdroid.connection.ConnectionListener;
import com.erik.erikdroid.ui.ErikListActivity;

public class EndlessErikAdapter extends EndlessAdapter {

    Context context;
    int pendingResource;

    public EndlessErikAdapter(final Context context, ListAdapter wrappedAdapter, int pendingResource, int pageNumber, int siteID, boolean keepAppending) {
        super(context, wrappedAdapter, pendingResource, pageNumber, siteID, null);
        this.context = context;
        this.keepOnAppending.set(keepAppending);
        this.listener = new EndlessConnectionListener();
        connection.mListener = listener;
    }

    private RotateAnimation getRotateAnimation() {
        RotateAnimation rotate = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1000);
        rotate.setRepeatMode(Animation.RESTART);
        rotate.setRepeatCount(Animation.INFINITE);
        return rotate;
    }

    @Override
    protected View getPendingView(ViewGroup parent) {
        View row = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.endless_loading, null);

        View child = row.findViewById(R.id.throbber);
        child.setVisibility(View.VISIBLE);
        child.startAnimation(getRotateAnimation());

        return (row);
    }

    @Override
    protected boolean cacheInBackground() throws Exception {
        return false;
    }

    @Override
    protected void appendCachedData() {
        // nop
    }

    private final class EndlessConnectionListener implements ConnectionListener {
        @Override
        public void onResponseReceived() {

            if (getWrappedAdapter().getCount() / 10 < ErikListActivity.totalPageCount) {
                keepOnAppending.set(true);
            } else {
                keepOnAppending.set(false);
            }

            final ErikAdapter erikAdapter = (ErikAdapter) getWrappedAdapter();

            ((FragmentActivity) EndlessErikAdapter.this.context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    erikAdapter.setData(ErikListActivity.erikListData);
                }
            });

            pageIndex++;
            pendingView = null;
        }

        @Override
        public void onResponseFailed(int errorResource, String errorMessage) {

        }
    }

}
