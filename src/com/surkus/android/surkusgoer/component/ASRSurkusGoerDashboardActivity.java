package com.surkus.android.surkusgoer.component;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

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
import com.surkus.android.surkusgoer.fragment.FSRSurkusGoerRatingQuestionsFragment;
import com.surkus.android.utils.CSRUtils.ShareOnFacebookInterface;

public class ASRSurkusGoerDashboardActivity extends FragmentActivity implements
		ShareOnFacebookInterface {
	private UiLifecycleHelper uiHelper;
	boolean mbIsOpenedShareDialog;
	boolean bIsFacebookShare;
	Session mSession;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sukurs_goer_dashboard);
		getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.container, new FSRSurkusGoerRatingQuestionsFragment()) // new FSRSurkusGoerApprovalPendingFragment(
				.commit();

		uiHelper = new UiLifecycleHelper(this, staus);
		uiHelper.onCreate(savedInstanceState);

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
		try
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
		}
	
	
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

}
