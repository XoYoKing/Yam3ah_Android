package com.ds.yam3ah.yam3ah;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;


public class SplashActivity extends ActionBarActivity {
    private Thread splashThread;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.hide();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        splashThread = new Thread() {
            @Override
            public void run() {
                int count = 2000;
                for (int i = 0; i < count; i++) {
                    progressBar.showContextMenu();
                    progressBar.setProgress(i);
                    try {
                        Thread.sleep(3);
                    } catch (InterruptedException ignore) {
                    }
                }

                Log.e("Check else", "Check else");
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        };

        progressBar.setVisibility(View.GONE);
        splashThread.start();


    }

    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        if (evt.getAction() == MotionEvent.ACTION_DOWN) {
            synchronized (splashThread) {
                splashThread.notifyAll();
            }
        }
        return true;
    }
}
