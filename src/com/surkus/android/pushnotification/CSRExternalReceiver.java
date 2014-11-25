package com.surkus.android.pushnotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.surkus.android.component.ASRSplashActivity;
import com.surkus.android.utils.CSRUtils;

public class CSRExternalReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        if(intent!=null){
            Bundle extras = intent.getExtras();
            Toast.makeText(context, "notification received", 2000).show();
           
            if(CSRUtils.isAppForground(context)){
            	 CSRMessageReceivingService.saveToLog(extras, context);
           }
            else{
            	CSRMessageReceivingService.sendToApp(extras, context);
            }
        }
    }
}

