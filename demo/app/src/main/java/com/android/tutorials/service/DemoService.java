package com.android.tutorials.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class DemoService extends Service {
    private static final String TAG = "DemoService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate : " + getClass().getSimpleName());
    }

    /**
     * @deprecated Implement {@link #onStartCommand(Intent, int, int)} instead.
     */
    @Override
    public void onStart(final Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "onStart : " + getClass().getSimpleName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand : " + getClass().getSimpleName());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind : " + getClass().getSimpleName());
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind : " + getClass().getSimpleName());
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy : " + getClass().getSimpleName());
    }
}
