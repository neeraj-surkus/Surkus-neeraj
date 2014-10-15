package com.surkus.android.component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
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
import com.surkus.android.model.CSRSurkusUser;
import com.surkus.android.networking.CSRWebConstants;
import com.surkus.android.networking.CSRWebServices;
import com.surkus.android.utils.CSRConstants;
import com.surkus.android.utils.CSRUtils;

public class ASRWhatAreYouActivity extends Activity {
	private boolean bIsUserLoggedInToFacebook;
	private UiLifecycleHelper uiHelper;
	private CSRWebServices webServiceSingletonObject;

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

		getHashKey();

	}

	void getHashKey() {
		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					"com.surkus.android", PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d("KeyHash:",
						Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (NameNotFoundException e) {

		} catch (NoSuchAlgorithmException e) {

		}
	}

	StatusCallback staus = new StatusCallback() {

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			if (session != null && session.isOpened()) {

				if (bIsUserLoggedInToFacebook) {

					// asynchronusFacebookUserInformationRequest();
					new CreateSurkusUserAndGenerateToken(true, 0)
							.execute(session.getAccessToken());
					bIsUserLoggedInToFacebook = false;
				}
			}

		}
	};

	void launchSurkusGoerRegistrationActivity(Session session) {

		try {

		} catch (Exception e) {

		}

	}

	private class CreateSurkusUserAndGenerateToken extends
			AsyncTask<String, Integer, CSRSurkusApiResponse> {

		boolean mbIsNewSurkusUser;
		int miStatusCode;

		CreateSurkusUserAndGenerateToken(boolean bIsNewSurkusUser,
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
				new CreateSurkusUserAndGenerateToken(false,
						CSRWebConstants.STATUS_CODE_400).execute(Session
						.getActiveSession().getAccessToken());
			} else if (surkusTokenResponse.getStatusCode() == CSRWebConstants.STATUS_CODE_401) {
				facebookLogout();
				facebookLogin();
			}

			else if (surkusTokenResponse.getResponseData().equalsIgnoreCase(
					CSRWebConstants.NO_RESPONSE_FROM_SERVER)) {

				CSRUtils.showAlertDialog(ASRWhatAreYouActivity.this,
						CSRWebConstants.SERVER_ERROR,
						CSRWebConstants.NO_RESPONSE_FROM_SERVER);

			} else {
				CSRUtils.createStringSharedPref(ASRWhatAreYouActivity.this,
						CSRConstants.SURKUS_TOKEN_SHARED_PREFERENCE_KEY,
						surkusTokenResponse.getResponseData());

				new RegistetSurkusUserContactInfo("07980", "123456789",surkusTokenResponse.getResponseData()).execute();				
			    new GetSurkusUserInfo(surkusTokenResponse.getResponseData()).execute();

				  if (miStatusCode == CSRWebConstants.STATUS_CODE_400)
				  {
                         Intent surkusGoerRegistrationIntent = new Intent(ASRWhatAreYouActivity.this,ASRSurkusGoerDashboardActivity.class);
				          startActivity(surkusGoerRegistrationIntent); 
				   }
				  else 
				  {
				  
				    Intent surkusGoerRegistrationIntent = new Intent(ASRWhatAreYouActivity.this, ASRSurkusGoerRegistrationActivity.class);
				    startActivity(surkusGoerRegistrationIntent); 
				   }				 
			      }
		}
	}

	private class RegistetSurkusUserContactInfo extends
			AsyncTask<String, Integer, CSRSurkusApiResponse> {

		String mCellPhoneNumber;
		String mZipCode;
		String mSurkusToken;

		RegistetSurkusUserContactInfo(String zipCode, String cellPhoneNumber,
				String surkusToken) {// zipCode
			mCellPhoneNumber = cellPhoneNumber;
			mZipCode = zipCode;
			mSurkusToken = surkusToken;
		}

		@Override
		protected CSRSurkusApiResponse doInBackground(String... args) {
			try {
				JSONObject jObject = new JSONObject();
				jObject.put(CSRWebConstants.ZIPCODE_KEY, mZipCode);
				jObject.put(CSRWebConstants.CELL_PHONE_KEY, mCellPhoneNumber);
				webServiceSingletonObject.registerSurkusUser(mSurkusToken,
						jObject.toString());

			} catch (JSONException e) {

			}

			return null;

		}

		@Override
		protected void onPostExecute(CSRSurkusApiResponse surkusTokenResponse) {

		}
	}

	private class GetSurkusUserInfo extends
			AsyncTask<String, Integer, CSRSurkusApiResponse> {

		String mSurkusToken;

		GetSurkusUserInfo(String surkusToken) {
			mSurkusToken = surkusToken;
		}

		@Override
		protected CSRSurkusApiResponse doInBackground(String... args) {
			try {
		
				CSRSurkusUser surkusUser = webServiceSingletonObject.getSurkusUserInfo(mSurkusToken);

			} catch (Exception e) {

			}

			return null;

		}

		@Override
		protected void onPostExecute(CSRSurkusApiResponse surkusTokenResponse) {

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
			new CreateSurkusUserAndGenerateToken(true, 0)
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
