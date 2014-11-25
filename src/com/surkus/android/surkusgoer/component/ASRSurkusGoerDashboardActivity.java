package com.surkus.android.surkusgoer.component;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.surkus.android.R;
import com.surkus.android.component.ASRSplashActivity;
import com.surkus.android.listener.ISRNotifySplashInterface;
import com.surkus.android.listener.ISRShareOnFacebookInterface;
import com.surkus.android.networking.CSRWebServices;
import com.surkus.android.pushnotification.CSRMessageReceivingService;
import com.surkus.android.pushnotification.CSRMessageReceivingService.MessageReceivingServiceBinder;
import com.surkus.android.surkusgoer.fragment.FSRSurkusGoerApprovalPendingFragment;
import com.surkus.android.surkusgoer.fragment.FSRSurkusGoerRatingQuestionsFragment;
import com.surkus.android.utils.CSRConstants;
import com.surkus.android.utils.CSRUtils;

public class ASRSurkusGoerDashboardActivity extends FragmentActivity implements ISRShareOnFacebookInterface,ISRNotifySplashInterface {
	private UiLifecycleHelper uiHelper;
	boolean mbIsOpenedShareDialog;
	boolean bIsFacebookShare;
	Session mSession;
	boolean mbHasCategoriesAvailable;
	CSRMessageReceivingService registerService;
	private boolean bound = false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sukurs_goer_dashboard);
		
		getSupportFragmentManager().beginTransaction().add(R.id.container, new FSRSurkusGoerRatingQuestionsFragment()).commit(); // new FSRSurkusGoerApprovalPendingFragment(		

		uiHelper = new UiLifecycleHelper(this, staus);
		uiHelper.onCreate(savedInstanceState);
		
		mbHasCategoriesAvailable = getIntent().getBooleanExtra(CSRConstants.IS_CATEGORY_AVAILABLE,false);
		
		if(mbHasCategoriesAvailable)
		{
			getSupportFragmentManager().beginTransaction().add(R.id.container, new FSRSurkusGoerApprovalPendingFragment()).commit(); // new FSRSurkusGoerApprovalPendingFragment(		
		}
		else
		{
			getSupportFragmentManager().beginTransaction().add(R.id.container, new FSRSurkusGoerRatingQuestionsFragment()).commit(); // new FSRSurkusGoerApprovalPendingFragment(
			
		}
     
		//CSRUtils.createBooleanSharedPref(this, CSRConstants.IS_USER_LOGGED_IN, true);
		CSRUtils.setUserLoggedIn(true);
		if(isGmcTokenExists()){
			
		}else{
			registerDeviceID();
		}
	}
	
	public void setHasCategories(boolean isCategoriesAvailable)
	{
		mbHasCategoriesAvailable = isCategoriesAvailable;
	}
	
	public boolean getHasCategories()
	{
		return mbHasCategoriesAvailable;
	}

	StatusCallback staus = new StatusCallback() {

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			if (session != null && !mbIsOpenedShareDialog && session.isOpened()) {
				mbIsOpenedShareDialog = true;
				if (bIsFacebookShare) {
					shareURLOnFacebook();
					bIsFacebookShare = false;
				}
			}

		}
	};

	void shareURLOnFacebook() {

		if (FacebookDialog.canPresentShareDialog(getApplicationContext(),FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) 
		{
			// Publish the post using the Share Dialog
			FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
			.setName("Get Paid to Party with SURKUS!")
			//.setPicture(CSRWebServices.SOCIAL_SHARING_URL)
		    .setLink(CSRWebServices.SOCIAL_SHARING_URL)
		    .setDescription("Here's an exclusive invite for you to join SURKUS so you can get paid to party too! Sign-up at https://app.surkus.com")
					.build();
			uiHelper.trackPendingDialogCall(shareDialog.present());

		} else {

			Bundle params = new Bundle();
			params.putString("name", "Get Paid to Party with SURKUS!");
			params.putString("picture", CSRWebServices.SOCIAL_SHARING_URL); // CSRWebServices.SOCIAL_SHARING_URL
			// params.putString("link", CSRWebServices.SHARE_URL);
			params.putString(
					"description",
					"Here's an exclusive invite for you to join SURKUS so you can get paid to party too! Sign-up at https://app.surkus.com");
			WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(this,
					Session.getActiveSession(), params)).setOnCompleteListener(
					new OnCompleteListener() {

						@Override
						public void onComplete(Bundle values,
								FacebookException error) {

						}

					}).build();
			feedDialog.show();
		}

		mbIsOpenedShareDialog = false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		uiHelper.onResume();

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		uiHelper.onActivityResult(requestCode, resultCode, data,
				new FacebookDialog.Callback() {
					@Override
					public void onError(FacebookDialog.PendingCall pendingCall,
							Exception error, Bundle data) {

					}

					@Override
					public void onComplete(
							FacebookDialog.PendingCall pendingCall, Bundle data) {

					}
				});
	}

	@Override
	public void onBackPressed() {	
		super.onBackPressed();
		/*try
		{
			FragmentManager fm = getSupportFragmentManager();
			int count = fm.getBackStackEntryCount();	
			
			if(count > 0)
			{
				super.onBackPressed();
			}
			else
			{
				
			}
			
		}
		catch (Exception e) {
		}*/
	
	
	}
	
	@Override
	public void shareOnFacebook() {

		mSession = Session.openActiveSession(this, true, staus);
		bIsFacebookShare = true;

		if (mSession != null && mSession.isOpened()) {
			shareURLOnFacebook();
			bIsFacebookShare = false;
		}

	}
	
	private boolean isGmcTokenExists(){
		
		String token=CSRUtils.getGcmToken(this);
		if(token.length()>0)return true;
		
		return false;
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
          registerService.setCallbacks(ASRSurkusGoerDashboardActivity.this); // register
      }

      @Override
      public void onServiceDisconnected(ComponentName arg0) {
          bound = false;
      }
  };
	@Override
	public void notifySplash() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "gcm token registered", 2000).show();
		
	}

}
