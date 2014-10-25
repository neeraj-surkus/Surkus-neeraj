package com.surkus.android.surkusgoer.component;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.facebook.FacebookException;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.surkus.android.R;
import com.surkus.android.networking.CSRWebServices;
import com.surkus.android.surkusgoer.fragment.FSRSurkusGoerApprovalPendingFragment;
import com.surkus.android.utils.CSRUtils.ShareOnFacebookInterface;





public class ASRSurkusGoerDashboardActivity extends FragmentActivity implements ShareOnFacebookInterface{
	private UiLifecycleHelper uiHelper;
	boolean mbIsOpenedShareDialog;
	boolean bIsFacebookShare;
	Session mSession;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sukurs_goer_dashboard);
	    getSupportFragmentManager().beginTransaction().add(R.id.container,new FSRSurkusGoerApprovalPendingFragment()).commit();	  
	    
		 uiHelper = new UiLifecycleHelper(this, staus);
		 uiHelper.onCreate(savedInstanceState);		
		
	}
	
	StatusCallback staus = new StatusCallback() {


		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			if (session != null && !mbIsOpenedShareDialog && session.isOpened())
			{
				mbIsOpenedShareDialog = true;
				if(bIsFacebookShare)
				{					
					shareURLOnFacebook();
				   bIsFacebookShare = false;
				}
			}
			
		}
	};
	
	void shareURLOnFacebook() {

		Bundle params = new Bundle();
    	params.putString("link", CSRWebServices.SHARE_URL);
    	WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(this, Session.getActiveSession(), params)).setOnCompleteListener(new OnCompleteListener() {
    	                      
    	@Override
    	public void onComplete(Bundle values,FacebookException error) {
    							
    	}

    	}).build();
        feedDialog.show();
	
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
	public void shareOnFacebook() {

		 mSession = Session.openActiveSession(this, true, staus);
		 bIsFacebookShare = true;
		
		 if (mSession != null && mSession.isOpened())
		 {
			 shareURLOnFacebook();
			 bIsFacebookShare = false;			 
		 }
	
		
	}

}
