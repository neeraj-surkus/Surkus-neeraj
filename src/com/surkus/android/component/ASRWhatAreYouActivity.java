package com.surkus.android.component;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.surkus.android.model.CSRSurkusApiResponse;
import com.surkus.android.model.CSRSurkusGoerUser;
import com.surkus.android.networking.CSRWebConstants;
import com.surkus.android.networking.CSRWebServices;
import com.surkus.android.surkusgoer.component.ASRSurkusGoerDashboardActivity;
import com.surkus.android.surkusgoer.component.ASRSurkusGoerRegistrationActivity;
import com.surkus.android.utils.CSRConstants;
import com.surkus.android.utils.CSRUtils;

public class ASRWhatAreYouActivity extends Activity {
	private boolean bIsUserLoggedInToFacebook;
	private UiLifecycleHelper uiHelper;
	private CSRWebServices webServiceSingletonObject;
	private ProgressDialog mGetSurkusGoerTokenDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_what_are_you);

		((Button) findViewById(R.id.cast_now_btn))
				.setOnClickListener(mClickListener);
		((Button) findViewById(R.id.be_casted_btn))
				.setOnClickListener(mClickListener);

		uiHelper = new UiLifecycleHelper(this, staus);
		uiHelper.onCreate(savedInstanceState);

		webServiceSingletonObject = CSRWebServices.getSingletonRef();

	}

	StatusCallback staus = new StatusCallback() {

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			if (session != null && session.isOpened()) {

				if (bIsUserLoggedInToFacebook) {
					displayGetSurkusGoerTokenDialog();
					new CreateSurkusUserAndGenerateTokenTask(true, 0)
							.execute(session.getAccessToken());
					bIsUserLoggedInToFacebook = false;
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

	@Override
	public void onBackPressed() {
		super.onBackPressed();

	}

	void dismissFetchSurkusGoerInfoDialog() {
		if (mGetSurkusGoerTokenDialog != null) {
			mGetSurkusGoerTokenDialog.dismiss();
		}
	}

	private class CreateSurkusUserAndGenerateTokenTask extends
			AsyncTask<String, Integer, CSRSurkusApiResponse> {

		boolean mbIsNewSurkusUser;
		int miStatusCode;

		CreateSurkusUserAndGenerateTokenTask(boolean bIsNewSurkusUser,
				int statusCode) {
			mbIsNewSurkusUser = bIsNewSurkusUser;
			miStatusCode = statusCode;
		}

		@Override
		protected CSRSurkusApiResponse doInBackground(String... args) {

			String accessToken = args[0];

			return webServiceSingletonObject.getSurkusToken(accessToken,
					mbIsNewSurkusUser);

		}

		@Override
		protected void onPostExecute(CSRSurkusApiResponse surkusTokenResponse) {
			super.onPostExecute(surkusTokenResponse);

			if (surkusTokenResponse.getStatusCode() == CSRWebConstants.STATUS_CODE_400) {
				new CreateSurkusUserAndGenerateTokenTask(false,
						CSRWebConstants.STATUS_CODE_400).execute(Session
						.getActiveSession().getAccessToken());
			} else if (surkusTokenResponse.getStatusCode() == CSRWebConstants.STATUS_CODE_401) {
				dismissFetchSurkusGoerInfoDialog();
				facebookLogout();
				facebookLogin();
			}

			else if (surkusTokenResponse.getResponseData().equalsIgnoreCase(
					CSRWebConstants.NO_RESPONSE_FROM_SERVER)) {
				dismissFetchSurkusGoerInfoDialog();
				CSRUtils.showAlertDialog(ASRWhatAreYouActivity.this,
						CSRWebConstants.SERVER_ERROR,
						CSRWebConstants.NO_RESPONSE_FROM_SERVER);

			} else {
				dismissFetchSurkusGoerInfoDialog();
				//new DeleteSurkusUserTask(surkusTokenResponse.getResponseData()).execute();
				// Storing Surkus token for future API calls.
				CSRUtils.createStringSharedPref(ASRWhatAreYouActivity.this,
						CSRConstants.SURKUS_TOKEN_SHARED_PREFERENCE_KEY,
						surkusTokenResponse.getResponseData()); 
																

				if (miStatusCode == CSRWebConstants.STATUS_CODE_400) {
					Intent surkusGoerRegistrationIntent = new Intent(
							ASRWhatAreYouActivity.this,
							ASRSurkusGoerDashboardActivity.class);
					// Intent surkusGoerRegistrationIntent = new
					// Intent(ASRWhatAreYouActivity.this,
					// ASRSurkusGoerRegistrationActivity.class);
					startActivity(surkusGoerRegistrationIntent);
				} else {
					Intent surkusGoerRegistrationIntent = new Intent(
							ASRWhatAreYouActivity.this,
							ASRSurkusGoerRegistrationActivity.class);
					startActivity(surkusGoerRegistrationIntent);
				}
			}
		}
	}

	private class DeleteSurkusUserTask extends
			AsyncTask<String, Integer, CSRSurkusGoerUser> {

		String mSurkusToken;

		DeleteSurkusUserTask(String surkusToken) {
			mSurkusToken = surkusToken;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected CSRSurkusGoerUser doInBackground(String... args) {
			
		        webServiceSingletonObject.deleteSurkusUser(mSurkusToken);
       
                return null;

		}

		@Override
		protected void onPostExecute(CSRSurkusGoerUser surkusGoerUser) {

		}
	}

	void facebookLogout() {
		Session mSession = Session.getActiveSession();
		if (mSession != null) {
			mSession.closeAndClearTokenInformation();
			Session.setActiveSession(null);
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
						Toast.makeText(ASRWhatAreYouActivity.this,
								"Error: %s" + error.toString(),
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onComplete(
							FacebookDialog.PendingCall pendingCall, Bundle data) {
						Log.i("Activity", "Success!");
						Toast.makeText(ASRWhatAreYouActivity.this, "Success!",
								Toast.LENGTH_SHORT).show();
					}
				});
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
			new CreateSurkusUserAndGenerateTokenTask(true, 0)
					.execute(currentSession.getAccessToken());
			bIsUserLoggedInToFacebook = false;
			// meFbSignIn();
		} else {
			// Ask for username and password
			OpenRequest op = new Session.OpenRequest((Activity) this);
			// don't use SSO.
			op.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
			// no callback needed.
			op.setCallback(null);
			// set permissions.
			List<String> permissions = new ArrayList<String>();
			permissions.add("email,user_birthday");
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

	View.OnClickListener mClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.cast_now_btn:
				// facebookLogin();
				break;

			case R.id.be_casted_btn:
				// /facebookLogout();
				facebookLogin();
				break;
			default:
				break;
			}
		}
	};

}
