package com.surkus.android.pushnotification;

import java.io.IOException;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.surkus.android.R;
import com.surkus.android.component.ASRSplashActivity;
import com.surkus.android.listener.ISRNotifySplashInterface;
import com.surkus.android.networking.CSRWebServices;
import com.surkus.android.utils.CSRConstants;
import com.surkus.android.utils.CSRUtils;

/*
 * This service is designed to run in the background and receive messages from gcm. If the app is in the foreground
 * when a message is received, it will immediately be posted. If the app is not in the foreground, the message will be saved
 * and a notification is posted to the NotificationManager.
 */
public class CSRMessageReceivingService extends Service{
    private GoogleCloudMessaging gcm;
    public static SharedPreferences savedValues;
    String mSurkusToken;
    ISRNotifySplashInterface notifySplash;
    
    private final IBinder binder = new MessageReceivingServiceBinder();

/*    public static void sendToApp(Bundle extras, Context context){
        Intent newIntent = new Intent();
        newIntent.setClass(context, ASRSplashActivity.class);
        newIntent.putExtras(extras);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newIntent);
    }*/
    
    
    
    // Class used for the client Binder.
    public class MessageReceivingServiceBinder extends Binder {
    	public CSRMessageReceivingService getService() {
            // Return this instance of MyService so clients can call public methods
            return CSRMessageReceivingService.this;
        }
    }

    public void onCreate(){
        super.onCreate();
        final String preferences = getString(R.string.preferences);
        
        mSurkusToken = CSRUtils.getStringSharedPref(this,CSRConstants.SURKUS_TOKEN_SHARED_PREFERENCE_KEY);
        
        savedValues = getSharedPreferences(preferences, Context.MODE_PRIVATE);
        // In later versions multi_process is no longer the default
        if(VERSION.SDK_INT >  9){
            savedValues = getSharedPreferences(preferences, Context.MODE_MULTI_PROCESS);
        }
        gcm = GoogleCloudMessaging.getInstance(getBaseContext());
       // SharedPreferences savedValues = PreferenceManager.getDefaultSharedPreferences(this);
        //if(savedValues.getBoolean(getString(R.string.first_launch), true)){
            register();
           /* SharedPreferences.Editor editor = savedValues.edit();
            editor.putBoolean(getString(R.string.first_launch), false);
            editor.commit();*/
       // }
        // Let ASRSplashActivity know we have just initialized and there may be stored messages
      //  sendToApp(new Bundle(), this);
    }

    protected static void saveToLog(Bundle extras, Context context){
        SharedPreferences.Editor editor=savedValues.edit();
        String numOfMissedMessages = context.getString(R.string.num_of_missed_messages);
        int linesOfMessageCount = 0;
        for(String key : extras.keySet()){
            String line = String.format("%s=%s", key, extras.getString(key));
            editor.putString("MessageLine" + linesOfMessageCount, line);
            linesOfMessageCount++;
        }
        editor.putInt(context.getString(R.string.lines_of_message_count), linesOfMessageCount);
        editor.putInt(context.getString(R.string.lines_of_message_count), linesOfMessageCount);
        editor.putInt(numOfMissedMessages, savedValues.getInt(numOfMissedMessages, 0) + 1);
        editor.commit();
        postNotification(new Intent(context, ASRSplashActivity.class), context);
    }

    protected static void postNotification(Intent intentAction, Context context){
        final NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentAction, Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL);
        final Notification notification = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Message Received!")
                .setContentText("")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .getNotification();

        mNotificationManager.notify(R.string.notification_number, notification);
    }

	private void register() {
        new AsyncTask(){
            protected Object doInBackground(final Object... params) {
                String token;
                try {
                    
                	token = gcm.register(getString(R.string.project_number));                    
                	CSRWebServices webServiceSingletonObject = CSRWebServices.getSingletonRef();
                 	boolean bisDeviceRegsitered = webServiceSingletonObject.isDeviceRegisterd(getApplicationContext(), mSurkusToken, token);
                	if(!bisDeviceRegsitered)
                	{
                		webServiceSingletonObject.registerDevice(getApplicationContext(), mSurkusToken, token);
                	}
                     
                    Log.i("registrationId", token);
                } 
                catch (IOException e) {
                    Log.i("Registration Error", e.getMessage());
                }
                return true;
            }
            
            protected void onPostExecute(Object result) 
            {
            	if(notifySplash != null)
            	   notifySplash.notifySplash();
            };
            
        }.execute(null, null, null);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    
    
    public void setCallbacks(ISRNotifySplashInterface callbacks) {
    	notifySplash = callbacks;
    }

}