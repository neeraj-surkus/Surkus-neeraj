package com.surkus.android.component;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.surkus.android.R;


public class ASRSplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        

        Thread watingThread = new Thread(new Runnable() {
 
            @Override
            public void run() {
 
                while (true) {
                    try {
                        Thread.sleep(2000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                 launchHome();
                            }
                        });
                        break;
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });
        watingThread.start();
 
    }
     
    void launchHome()
    {
        Intent homeIntent = new Intent(this, ASRLoginActivity.class);
        startActivity(homeIntent);
        finish();
    }

}
