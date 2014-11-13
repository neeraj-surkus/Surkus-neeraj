package com.surkus.android.component;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;

import com.surkus.android.R;
import com.surkus.android.listener.ISRNotifySplashInterface;
import com.surkus.android.model.CSRRatingQuestion;
import com.surkus.android.model.CSRSurkusGoer;
import com.surkus.android.networking.CSRWebServices;
import com.surkus.android.pushnotification.CSRMessageReceivingService;
import com.surkus.android.pushnotification.CSRMessageReceivingService.MessageReceivingServiceBinder;
import com.surkus.android.surkusgoer.component.ASRSurkusGoerDashboardActivity;
import com.surkus.android.surkusgoer.component.ASRSurkusGoerRegistrationActivity;
import com.surkus.android.utils.CSRConstants;
import com.surkus.android.utils.CSRUtils;

public class ASRSplashActivity extends Activity implements ISRNotifySplashInterface{

	Thread watingThread;
	boolean bHasSplashIsRunning = true;
	private CSRWebServices webServiceSingletonObject;
	String mSurkusToken;
	private boolean bound = false;
	
	CSRMessageReceivingService registerService;
	
	//public static Boolean inBackground = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
	    mSurkusToken = CSRUtils.getStringSharedPref(this,
				CSRConstants.SURKUS_TOKEN_SHARED_PREFERENCE_KEY);
		webServiceSingletonObject = CSRWebServices.getSingletonRef();

		if (mSurkusToken != null && !TextUtils.isEmpty(mSurkusToken)) {
		    //new GetSurkusGoerInfoTask(mSurkusToken).execute();	
			registerDeviceID();

		} else {

			watingThread = new Thread(new Runnable() {

				@Override
				public void run() {

					while (bHasSplashIsRunning) {
						try {
							Thread.sleep(2000);
							runOnUiThread(new Runnable() {
								@Override
								public void run() {

									if (bHasSplashIsRunning)
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
	}
	  
	@Override
    public void onResume(){
        super.onResume();
      //  inBackground = false;
    }
	
	@Override
    public void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        setIntent(intent);
    }
	
	@Override
    public void onStop(){
        super.onStop();
       // inBackground = true;
        
        // Unbind from service
        if (bound) {
        	registerService.setCallbacks(null); // unregister
            unbindService(serviceConnection);
            bound = false;
        }
    }

	void registerDeviceID() {
		
		  bindService(new Intent(this, CSRMessageReceivingService.class),serviceConnection, Context.BIND_AUTO_CREATE);
	}
	
	  /** Callbacks for service binding, passed to bindService() */
    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // cast the IBinder and get MyService instance
        	MessageReceivingServiceBinder binder = (MessageReceivingServiceBinder) service;
        	registerService = binder.getService();
            bound = true;
            registerService.setCallbacks(ASRSplashActivity.this); // register
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };


	private class GetSurkusGoerInfoTask extends
			AsyncTask<String, Integer, CSRSurkusGoer> {

		String mSurkusToken;

		GetSurkusGoerInfoTask(String surkusToken) {
			mSurkusToken = surkusToken;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected CSRSurkusGoer doInBackground(String... args) {
			return webServiceSingletonObject.getSurkusUserInfo(mSurkusToken);

		}

		@Override
		protected void onPostExecute(CSRSurkusGoer surkusGoerUser) {

			if (surkusGoerUser == null
					|| (surkusGoerUser != null && (surkusGoerUser
							.getCellPhone() == null
							|| TextUtils.isEmpty(surkusGoerUser.getCellPhone())
							|| surkusGoerUser.getAddress().getZipCode() == null || TextUtils
								.isEmpty(surkusGoerUser.getAddress()
										.getZipCode())))) {

				Intent surkusGoerRegistrationIntent = new Intent(
						ASRSplashActivity.this,
						ASRSurkusGoerRegistrationActivity.class);
				startActivity(surkusGoerRegistrationIntent);
				finish();

			} else {

				new GetRatingQuestionsTask(mSurkusToken, surkusGoerUser)
						.execute();
			}

		}
	}

	private class GetRatingQuestionsTask extends
			AsyncTask<String, Integer, Integer> {

		String mSurkusToken;
		CSRSurkusGoer mSurkusGoerUser;

		GetRatingQuestionsTask(String surkusToken, CSRSurkusGoer surkusGoerUser) {
			mSurkusToken = surkusToken;
			mSurkusGoerUser = surkusGoerUser;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(String... params) {

			ArrayList<CSRRatingQuestion> ratingQuestions = webServiceSingletonObject
					.getRatingQuestions(mSurkusToken);

			if (ratingQuestions != null && ratingQuestions.size() > 0) {
				int totalCategories = 0;
				for (int i = 0; i < ratingQuestions.size(); i++) {
					totalCategories += ratingQuestions.get(i)
							.getRatingOptionList().size();
				}

				return totalCategories;
			} else
				return 0;

		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);

			Intent surkusGoerDashboardIntent = new Intent(
					ASRSplashActivity.this,
					ASRSurkusGoerDashboardActivity.class);

			if (mSurkusGoerUser.getRatedQuestionOptions() != null
					&& mSurkusGoerUser.getRatedQuestionOptions().size() == result) {
				surkusGoerDashboardIntent.putExtra(
						CSRConstants.IS_CATEGORY_AVAILABLE, true);
			} else
				surkusGoerDashboardIntent.putExtra(
						CSRConstants.IS_CATEGORY_AVAILABLE, false);

			startActivity(surkusGoerDashboardIntent);
			finish();

		}
	}

	void launchHome() {
		Intent homeIntent = new Intent(this, ASRLoginActivity.class);
		startActivity(homeIntent);
		finish();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		bHasSplashIsRunning = false;
	}

	@Override
	public void notifySplash() {
		new GetSurkusGoerInfoTask(mSurkusToken).execute();		
	}

}
