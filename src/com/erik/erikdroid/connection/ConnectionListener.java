package com.erik.erikdroid.connection;

public interface ConnectionListener {
    public void onResponseReceived();

    public void onResponseFailed(int errorResource, String errorMessage);
}
