package com.surkus.android.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.Session.OpenRequest;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.surkus.android.R;
import com.surkus.android.adapter.CSRAboutSurkusAdapter;
import com.surkus.android.entity.CirclePageIndicator;
import com.surkus.android.model.CSRRatingOption;
import com.surkus.android.model.CSRRatingQuestion;
import com.surkus.android.model.CSRSurkusGoer;
import com.surkus.android.model.CSRSurkusGoerSurkusToken;
import com.surkus.android.networking.CSRWebConstants;
import com.surkus.android.networking.CSRWebServices;
import com.surkus.android.surkusgoer.component.ASRSurkusGoerDashboardActivity;
import com.surkus.android.surkusgoer.component.ASRSurkusGoerRegistrationActivity;
import com.surkus.android.utils.CSRConstants;
import com.surkus.android.utils.CSRUtils;

public class ASRLoginActivity extends FragmentActivity {

	private boolean bIsUserLoggedInToFacebook;
	private UiLifecycleHelper uiHelper;
	private CSRWebServices webServiceSingletonObject;
	private ProgressDialog mGetSurkusGoerTokenDialog;

	private CSRAboutSurkusAdapter mAdapter;
	private ViewPager mPager;
	private CirclePageIndicator mIndicator;
	Button mFacebookLoginBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		mAdapter = new CSRAboutSurkusAdapter(getSupportFragmentManager(),
				CSRConstants.NO_OF_ABOUT_SURKUS_PAGES);
		mPager = (ViewPager) findViewById(R.id.about_surkus_viewpager);
		mPager.setAdapter(mAdapter);

		mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);

		mFacebookLoginBtn = (Button) findViewById(R.id.facebook_signup_btn);
		mFacebookLoginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				facebookLogin();

			}
		});

		uiHelper = new UiLifecycleHelper(this, staus);
		uiHelper.onCreate(savedInstanceState);
		// CSRUtils.getHashKey(this);
		webServiceSingletonObject = CSRWebServices.getSingletonRef();
	}

	StatusCallback staus = new StatusCallback() {

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			if (session != null && session.isOpened()) {

				if (bIsUserLoggedInToFacebook) {
					displayGetSurkusGoerTokenDialog();
					/*
					 * new CreateSurkusUserAndGenerateTokenTask(true, 0)
					 * .execute(session.getAccessToken());
					 */
					registerSurkusUser();
					// bIsUserLoggedInToFacebook = false;
				}
			}

		}
	};

	void displayGetSurkusGoerTokenDialog() {
		try {
			mGetSurkusGoerTokenDialog = new ProgressDialog(this);
			mGetSurkusGoerTokenDialog.setMessage(getResources().getString(
					R.string.loading));
			mGetSurkusGoerTokenDialog.setCancelable(false);
			mGetSurkusGoerTokenDialog.setCanceledOnTouchOutside(false);
			mGetSurkusGoerTokenDialog.setIndeterminate(true);
			mGetSurkusGoerTokenDialog.show();
		} catch (Exception e) {

		}
	}

	void dismissFetchSurkusGoerInfoDialog() {
		if (mGetSurkusGoerTokenDialog != null) {
			mGetSurkusGoerTokenDialog.dismiss();
		}
	}

	private class CreateSurkusUserAndGenerateTokenTask extends
			AsyncTask<String, Integer, CSRSurkusGoerSurkusToken> {

		boolean mbIsNewSurkusUser;
		int miStatusCode;

		CreateSurkusUserAndGenerateTokenTask(boolean bIsNewSurkusUser,
				int statusCode) {
			mbIsNewSurkusUser = bIsNewSurkusUser;
			miStatusCode = statusCode;
		}

		@Override
		protected CSRSurkusGoerSurkusToken doInBackground(String... args) {

			String accessToken = args[0];

			return webServiceSingletonObject.getSurkusToken(accessToken,
					mbIsNewSurkusUser);

		}

		@Override
		protected void onPostExecute(
				CSRSurkusGoerSurkusToken surkusTokenResponse) {
			super.onPostExecute(surkusTokenResponse);

			if (surkusTokenResponse.getStatusCode() == CSRWebConstants.STATUS_CODE_400) {
				new CreateSurkusUserAndGenerateTokenTask(false,
						CSRWebConstants.STATUS_CODE_400).execute(Session
						.getActiveSession().getAccessToken());
			} else if (surkusTokenResponse.getStatusCode() == CSRWebConstants.STATUS_CODE_401
					|| surkusTokenResponse.getStatusCode() == CSRWebConstants.STATUS_CODE_500) {
				dismissFetchSurkusGoerInfoDialog();
				CSRUtils.facebookLogout(ASRLoginActivity.this);
				facebookLogin();
			}

			else if (surkusTokenResponse.getSurkusToken().equalsIgnoreCase(
					CSRWebConstants.NO_RESPONSE_FROM_SERVER)) {
				dismissFetchSurkusGoerInfoDialog();
				CSRUtils.showAlertDialog(ASRLoginActivity.this,
						CSRWebConstants.SERVER_ERROR,
						CSRWebConstants.NO_RESPONSE_FROM_SERVER);

			} else if (surkusTokenResponse.getStatusCode() == CSRWebConstants.STATUS_CODE_200) {
			/*	 dismissFetchSurkusGoerInfoDialog();
				 new DeleteSurkusUserTask(surkusTokenResponse.getSurkusToken()).execute();*/
				
				// Storing Surkus token for future API calls.
				CSRUtils.createStringSharedPref(ASRLoginActivity.this,CSRConstants.SURKUS_TOKEN_SHARED_PREFERENCE_KEY,surkusTokenResponse.getSurkusToken());

				if (miStatusCode == CSRWebConstants.STATUS_CODE_400) {
					new GetSurkusGoerInfoTask(
							surkusTokenResponse.getSurkusToken()).execute();

				} else {
					dismissFetchSurkusGoerInfoDialog();
					Intent surkusGoerRegistrationIntent = new Intent(
							ASRLoginActivity.this,
							ASRSurkusGoerRegistrationActivity.class);
					startActivity(surkusGoerRegistrationIntent);
				}
			}
		}
	}

	private class GetRatingQuestionsTask extends
			AsyncTask<String, Integer,Integer> {

		String mSurkusToken;
		CSRSurkusGoer mSurkusGoerUser;

		GetRatingQuestionsTask(String surkusToken,CSRSurkusGoer surkusGoerUser) {
			mSurkusToken = surkusToken;
			mSurkusGoerUser = surkusGoerUser;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

	    @Override
	    protected Integer doInBackground(String... params) {

	     ArrayList<CSRRatingQuestion> ratingQuestions = webServiceSingletonObject.getRatingQuestions(mSurkusToken);
			
			if(ratingQuestions != null && ratingQuestions.size() >0)
			{
			  int totalCategories = 0;
		      for(int i=0;i<ratingQuestions.size();i++)
			  {
				totalCategories += ratingQuestions.get(i).getRatingOptionList().size();
			  }
		      
		      return totalCategories;
			}
			else
				return 0;
	 
	    }
	    
	    
	    @Override
	    protected void onPostExecute(Integer result) {
	    	super.onPostExecute(result);
	    	
	    	dismissFetchSurkusGoerInfoDialog();
	    	
	    	Intent surkusGoerDashboardIntent = new Intent(ASRLoginActivity.this,ASRSurkusGoerDashboardActivity.class);

			if (mSurkusGoerUser.getRatedQuestionOptions() != null && mSurkusGoerUser.getRatedQuestionOptions().size() == result) 
			{
				surkusGoerDashboardIntent.putExtra(CSRConstants.IS_CATEGORY_AVAILABLE, true);
			} 
			else
				surkusGoerDashboardIntent.putExtra(CSRConstants.IS_CATEGORY_AVAILABLE, false);
			
			   startActivity(surkusGoerDashboardIntent);
	    	
	    }
	}
	
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
				dismissFetchSurkusGoerInfoDialog();
				Intent surkusGoerRegistrationIntent = new Intent(
						ASRLoginActivity.this,
						ASRSurkusGoerRegistrationActivity.class);
				startActivity(surkusGoerRegistrationIntent);
			} else {
				
				
				new GetRatingQuestionsTask(mSurkusToken,surkusGoerUser).execute();
			}

		}
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
						Log.e("Activity",
								String.format("Error: %s", error.toString()));
						Toast.makeText(ASRLoginActivity.this,
								"Error: %s" + error.toString(),
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onComplete(
							FacebookDialog.PendingCall pendingCall, Bundle data) {
						Log.i("Activity", "Success!");
						Toast.makeText(ASRLoginActivity.this, "Success!",
								Toast.LENGTH_SHORT).show();
					}
				});
	}

	void registerSurkusUser() {
		Session session = Session.getActiveSession();
		String[] PERMISSION_ARRAY_READ = { "email" };
		List<String> permissionList = Arrays.asList(PERMISSION_ARRAY_READ);
		// If all required permissions are available...
		bIsUserLoggedInToFacebook = false;
		if (session.getPermissions().containsAll(permissionList)) {

			new CreateSurkusUserAndGenerateTokenTask(true, 0).execute(Session
					.getActiveSession().getAccessToken());
		} else {
			dismissFetchSurkusGoerInfoDialog();
			bIsUserLoggedInToFacebook = true;
			session.requestNewReadPermissions(new Session.NewPermissionsRequest(
					this, permissionList));
		}
	}

	void facebookLogin() {
		Session currentSession = Session.getActiveSession();
		if (currentSession == null || currentSession.getState().isClosed()) {
			// create a new session.
			Session session = new Session.Builder(getApplicationContext())
					.build();
			// set it a the active session.
			Session.setActiveSession(session);
			// keep a variable link to session.
			currentSession = session;
		}
		// if a session is already open then issue a request using the available
		// session. Otherwise ask for user credentials.
		if (currentSession.isOpened()) {
			displayGetSurkusGoerTokenDialog();
			/*
			 * new CreateSurkusUserAndGenerateTokenTask(true, 0)
			 * .execute(currentSession.getAccessToken());
			 */
			registerSurkusUser();
			// bIsUserLoggedInToFacebook = false;
			// meFbSignIn();
		} else {
			// Ask for username and password
			OpenRequest op = new Session.OpenRequest((Activity) this);
			// don't use SSO.
			op.setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK);
			// no callback needed.
			op.setCallback(null);
			// set permissions.
			List<String> permissions = new ArrayList<String>();
			permissions.add("email"); // user_birthday
			/*
			 * permissions.add("user_birthday");
			 * permissions.add("user_location"); permissions.add("user_likes");
			 */
			op.setPermissions(permissions);
			// open session for read.
			currentSession.openForRead(op);
			bIsUserLoggedInToFacebook = true;

		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		bIsUserLoggedInToFacebook = false;
	}

}
