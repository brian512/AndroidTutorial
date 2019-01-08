package com.android.tutorials.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyIntentService extends Service {
    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                onHandleIntent(intent);
                stopSelf();
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected void onHandleIntent(Intent intent) {
        // 处理耗时任务
    }
}
