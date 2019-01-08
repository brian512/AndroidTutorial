package com.android.tutorials.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class DemoIntentService extends IntentService {
    private static final String TAG = "DemoIntentService";

    public static final String EXTRA_INFO = "extra_info";

    public DemoIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate : " + getClass().getSimpleName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand : " + getClass().getSimpleName());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind : " + getClass().getSimpleName());
        return super.onBind(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, intent.getStringExtra(DemoIntentService.EXTRA_INFO));
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy : " + getClass().getSimpleName());
        super.onDestroy();
    }
}
