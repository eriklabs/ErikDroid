package com.erik.erikdroid.ui.adapters;

import java.util.concurrent.atomic.AtomicBoolean;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.commonsware.cwac.adapter.AdapterWrapper;
import com.erik.erikdroid.connection.Connection;
import com.erik.erikdroid.connection.ConnectionListener;

public abstract class EndlessAdapter extends AdapterWrapper {
    abstract protected boolean cacheInBackground() throws Exception;

    abstract protected void appendCachedData();

    protected View pendingView = null;
    protected AtomicBoolean keepOnAppending = new AtomicBoolean(false);
    private Context context;
    private int pendingResource = -1;
    private boolean isSerialized = false;
    public static int pageIndex;
    protected int siteID;
    protected ConnectionListener listener;
    ListAdapter wrappedAdapter;
    public Connection connection;

    /**
     * Constructor wrapping a supplied ListAdapter
     */
    public EndlessAdapter(ListAdapter wrapped) {
        super(wrapped);
    }

    /**
     * Constructor wrapping a supplied ListAdapter and providing a id for a pending view.
     * 
     * @param context
     * @param wrapped
     * @param pendingResource
     */
    public EndlessAdapter(Context context, ListAdapter wrapped,
            int pendingResource) {
        super(wrapped);
        this.context = context;
        this.pendingResource = pendingResource;
    }

    /**
     * Constructor wrapping a supplied ListAdapter and providing a id for a pending view.
     * 
     * @param context
     * @param wrapped
     * @param pendingResource
     */
    @SuppressWarnings("static-access")
    public EndlessAdapter(Context context, ListAdapter wrapped,
            int pendingResource, int pageNumber, int siteID,
            ConnectionListener listener) {
        super(wrapped);
        this.context = context;
        this.pendingResource = pendingResource;
        this.pageIndex = pageNumber;
        this.siteID = siteID;
        this.listener = listener;
        this.connection = new Connection(listener);
        wrappedAdapter = wrapped;
    }

    public boolean isSerialized() {
        return (isSerialized);
    }

    public void setSerialized(boolean isSerialized) {
        this.isSerialized = isSerialized;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     */
    @Override
    public int getCount() {
        if (keepOnAppending.get()) {
            return (super.getCount() + 1); // one more for
                                           // "pending"
        }

        return (super.getCount());
    }

    /**
     * Masks ViewType so the AdapterView replaces the "Pending" row when new data is loaded.
     */
    public int getItemViewType(int position) {
        if (position == getWrappedAdapter().getCount()) {
            return (IGNORE_ITEM_VIEW_TYPE);
        }

        return (super.getItemViewType(position));
    }

    /**
     * Masks ViewType so the AdapterView replaces the "Pending" row when new data is loaded.
     * 
     * @see #getItemViewType(int)
     */
    public int getViewTypeCount() {
        return (super.getViewTypeCount() + 1);
    }

    /**
     * Get a View that displays the data at the specified position in the data set. In this case, if we are at the end
     * of the list and we are still in append mode, we ask for a pending view and return it, plus kick off the
     * background task to append more data to the wrapped adapter.
     * 
     * @param position
     *            Position of the item whose data we want
     * @param convertView
     *            View to recycle, if not null
     * @param parent
     *            ViewGroup containing the returned View
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (position == super.getCount() && keepOnAppending.get()) {
            if (pendingView == null) {
                pendingView = getPendingView(parent);

                if (keepOnAppending.get()) {

                    connection = new Connection(listener);
                    connection.getEriks(pageIndex, siteID);
                }
            }

            return (pendingView);
        }

        return (super.getView(position, convertView, parent));
    }

    /**
     * Called if cacheInBackground() raises a runtime exception, to allow the UI to deal with the exception on the main
     * application thread.
     * 
     * @param pendingView
     *            View representing the pending row
     * @param e
     *            Exception that was raised by cacheInBackground()
     * @return true if should allow retrying appending new data, false otherwise
     */
    protected boolean onException(View pendingView, Exception e) {
        Log.e("EndlessAdapter", "Exception in cacheInBackground()", e);

        return (false);
    }

    /**
     * Inflates pending view using the pendingResource ID passed into the constructor
     * 
     * @param parent
     * @return inflated pending view, or null if the context passed into the pending view constructor was null.
     */
    protected View getPendingView(ViewGroup parent) {
        if (context != null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(pendingResource, parent, false);
        }

        throw new RuntimeException(
                "You must either override getPendingView() or supply a pending View resource via the constructor");
    }

    /**
     * Getter method for the Context being held by the adapter
     * 
     * @return Context
     */
    protected Context getContext() {
        return (context);
    }

}