package com.android.tutorials;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.tutorials.service.DemoIntentService;
import com.android.tutorials.service.DemoService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LifeCycleActivity.startActivity(this);

        startIntentService();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService();
    }

    private void startService() {
        Intent intent = new Intent();
        intent.setClass(this, DemoService.class);
        startService(intent);
    }

    private void stopService() {
        Intent intent = new Intent();
        intent.setClass(this, DemoService.class);
        stopService(intent);
    }

    private void startIntentService() {
        Intent intent = new Intent();
        intent.setClass(this, DemoIntentService.class);
        intent.putExtra(DemoIntentService.EXTRA_INFO, "from extra");
        startService(intent);
    }
}
