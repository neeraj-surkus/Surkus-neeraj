package com.surkus.android.pushnotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.surkus.android.component.ASRSplashActivity;

public class CSRExternalReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        if(intent!=null){
            Bundle extras = intent.getExtras();
       /*     if(!ASRSplashActivity.inBackground){
                CSRMessageReceivingService.sendToApp(extras, context);
            }
            else{
                CSRMessageReceivingService.saveToLog(extras, context);
            }*/
        }
    }
}

