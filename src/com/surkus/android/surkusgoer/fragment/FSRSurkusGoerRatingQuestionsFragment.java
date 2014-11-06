package com.surkus.android.surkusgoer.fragment;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.facebook.Session;
import com.surkus.android.R;
import com.surkus.android.adapter.CSRSurkusGoerRatingQuestionsAdapter;
import com.surkus.android.component.ASRLoginActivity;
import com.surkus.android.model.CSRRatingOption;
import com.surkus.android.model.CSRRatingQuestion;
import com.surkus.android.model.CSRSurkusApiResponse;
import com.surkus.android.model.CSRSurkusGoer;
import com.surkus.android.model.CSRSurkusGoerSurkusToken;
import com.surkus.android.networking.CSRWebConstants;
import com.surkus.android.networking.CSRWebServices;
import com.surkus.android.surkusgoer.component.ASRSurkusGoerDashboardActivity;
import com.surkus.android.utils.CSRConstants;
import com.surkus.android.utils.CSRUtils;
import com.surkus.android.utils.CSRUtils.ShareOnFacebookInterface;

public class FSRSurkusGoerRatingQuestionsFragment extends Fragment implements
		OnClickListener {

	private CSRWebServices mWebServiceSingletonObject;
	private GetRatingQuestionsTask mGetSurkusGoerInfoTask;
	private ProgressDialog mFetchSurkusGoerInfoOrSubmitDialog;

	//private ArrayList<CSRRatingQuestion> mRatingQuestions;
	CSRSurkusGoerRatingQuestionsAdapter mRatingQuestionsAdapter;
	private int selectedFragmentPosition = 0;
	CSRSurkusGoer mSurkusGoerUser;
	private FragmentManager fragmentManager;

	private RelativeLayout mSocialShareLayout;

	private LinearLayout mRatingQuestionsLayout;

	private String mSurkusToken;

	private Button mMenuButton;
	Button mSubmitRatingQuestionsBtn;

	ImageView mFacebookShareImageview;
	ImageView mTwitterShareImageview;
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				R.layout.fragment_sukurs_goer_rating_questionst, container,
				false);

		mRatingQuestionsAdapter = new CSRSurkusGoerRatingQuestionsAdapter(
				getActivity());

		((ListView) rootView.findViewById(R.id.rating_questions_list))
				.setAdapter(mRatingQuestionsAdapter);

		mMenuButton = (Button) rootView.findViewById(R.id.menu_btn);
		mMenuButton.setOnClickListener(this);

		mFacebookShareImageview = (ImageView) rootView
				.findViewById(R.id.facebook_share_imageview);
		mFacebookShareImageview.setOnClickListener(this);

		mTwitterShareImageview = (ImageView) rootView
				.findViewById(R.id.twitter_share_imageview);
		mTwitterShareImageview.setOnClickListener(this);

		mSubmitRatingQuestionsBtn = ((Button) rootView
				.findViewById(R.id.submit_rating_btn));
		mSubmitRatingQuestionsBtn.setOnClickListener(this);

		mSocialShareLayout = (RelativeLayout) rootView
				.findViewById(R.id.social_share_layout);

		mRatingQuestionsLayout = (LinearLayout) rootView
				.findViewById(R.id.rating_questions_layout);

		mSurkusToken = CSRUtils.getStringSharedPref(getActivity(),
				CSRConstants.SURKUS_TOKEN_SHARED_PREFERENCE_KEY);
		mWebServiceSingletonObject = CSRWebServices.getSingletonRef();

		mGetSurkusGoerInfoTask = new GetRatingQuestionsTask(mSurkusToken);
		mGetSurkusGoerInfoTask.execute();

		fragmentManager = getActivity().getSupportFragmentManager();

		if (getArguments() != null)
			selectedFragmentPosition = getArguments().getInt(
					CSRConstants.SURKUS_USER_MENU_INDEX, 0);

		return rootView;
	}

	void displayFetchSurkusGoerInfoOrSubmitDialog(String msg) {
		
		if(mFetchSurkusGoerInfoOrSubmitDialog != null && mFetchSurkusGoerInfoOrSubmitDialog.isShowing())
		{
			// Doing registration process after updating surkus token. Diloag is already displaying to user.
		}
		else{
		try {
			mFetchSurkusGoerInfoOrSubmitDialog = new ProgressDialog(getActivity());
			mFetchSurkusGoerInfoOrSubmitDialog.setMessage(msg);
			mFetchSurkusGoerInfoOrSubmitDialog.setCancelable(false);
			mFetchSurkusGoerInfoOrSubmitDialog.setCanceledOnTouchOutside(false);
			mFetchSurkusGoerInfoOrSubmitDialog.setIndeterminate(true);
			mFetchSurkusGoerInfoOrSubmitDialog.show();
		} catch (Exception e) {

		}
		}
	}

	void dismissFetchSurkusGoerInfoOrSubmitDialog() {
		if (mFetchSurkusGoerInfoOrSubmitDialog != null) {
			mFetchSurkusGoerInfoOrSubmitDialog.dismiss();
		}
	}

	private class GetRatingQuestionsTask extends
			AsyncTask<String, Integer, ArrayList<CSRRatingQuestion>> {

		String mSurkusToken;

		GetRatingQuestionsTask(String surkusToken) {
			mSurkusToken = surkusToken;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			displayFetchSurkusGoerInfoOrSubmitDialog(getActivity().getResources().getString(R.string.loading));

		}

		@Override
		protected ArrayList<CSRRatingQuestion> doInBackground(String... args) {
			mSurkusGoerUser = mWebServiceSingletonObject
					.getSurkusUserInfo(mSurkusToken);
			CSRUtils.createStringSharedPref(getActivity(),
					CSRConstants.SURKUS_USER_NAME, mSurkusGoerUser
							.getFacebookUserInfo().getName());
			ArrayList<CSRRatingQuestion> ratingQuestions = mWebServiceSingletonObject
					.getRatingQuestions(mSurkusToken);

			return ratingQuestions;

		}

		@Override
		protected void onPostExecute(
				ArrayList<CSRRatingQuestion> ratingQuestions) {
			initiailzeUIFields(ratingQuestions);
		}
	}

	void initiailzeUIFields(ArrayList<CSRRatingQuestion> ratingQuestions) {
		dismissFetchSurkusGoerInfoOrSubmitDialog();
		mRatingQuestionsAdapter.setRatingQuestions(ratingQuestions);
		mSocialShareLayout.setVisibility(View.VISIBLE);
		mRatingQuestionsLayout.setVisibility(View.VISIBLE);
		mSubmitRatingQuestionsBtn.setVisibility(View.VISIBLE);
	}

	private class UpdateSurkusTokenTask extends
			AsyncTask<String, Integer, CSRSurkusGoerSurkusToken> {

		String mSurkusToken;
		
		@Override
		protected CSRSurkusGoerSurkusToken doInBackground(String... args) {
			String accessToken = args[0];
			return mWebServiceSingletonObject.getSurkusToken(accessToken, false);
		}

		@Override
		protected void onPostExecute(
				CSRSurkusGoerSurkusToken surkusTokenResponse) {
			super.onPostExecute(surkusTokenResponse);

			if (surkusTokenResponse.getStatusCode() == CSRWebConstants.STATUS_CODE_401
					|| surkusTokenResponse.getStatusCode() == CSRWebConstants.STATUS_CODE_500) {
				dismissFetchSurkusGoerInfoOrSubmitDialog();
				launchLoginActivity();
			}

			else if (surkusTokenResponse.getSurkusToken().equalsIgnoreCase(
					CSRWebConstants.NO_RESPONSE_FROM_SERVER)) {
				dismissFetchSurkusGoerInfoOrSubmitDialog();
				CSRUtils.showAlertDialog(getActivity(),
						CSRWebConstants.SERVER_ERROR,
						CSRWebConstants.NO_RESPONSE_FROM_SERVER);

			} else if (surkusTokenResponse.getStatusCode() == CSRWebConstants.STATUS_CODE_200) {
				// dismissRegisterSurkusGoerDiloag();
				
				new SubmitRatingQuestionTask(mSurkusToken, mRatingQuestionsAdapter.getRatingQuestions()).execute();;
			}
		}
	}

	private class SubmitRatingQuestionTask extends
			AsyncTask<String, Integer, Boolean> {

		String mSurkusToken;
		ArrayList<CSRRatingQuestion> mRatingQuestions;


		SubmitRatingQuestionTask(String surkusToken,
				ArrayList<CSRRatingQuestion> ratingQuestions) {
			mSurkusToken = surkusToken;
			mRatingQuestions = ratingQuestions;

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			displayFetchSurkusGoerInfoOrSubmitDialog(getActivity().getResources().getString(R.string.submitting_rating_msg));

		}

		@Override
		protected Boolean doInBackground(String... args) {
		    boolean isAllRatnigsPosted = true;
			boolean isSubmitTaskCancelled = false;

			for (int i = 0; i < mRatingQuestions.size(); i++) {
				
				if(isSubmitTaskCancelled)
					break;
				
				CSRRatingQuestion ratingQuestion = mRatingQuestions.get(i);
				String ratingQuestionStr = ratingQuestion.getRatingQuestion();
				ArrayList<CSRRatingOption> ratingOptionList = ratingQuestion
						.getRatingOptionList();

				for (int j = 0; j < ratingOptionList.size(); j++) {
			
					try {
						CSRRatingOption ratingOption = ratingOptionList.get(j);
						JSONObject ratingOptionsJsonData = new JSONObject();
						ratingOptionsJsonData.put(CSRWebConstants.NAME_KEY,
								ratingQuestionStr);
						ratingOptionsJsonData.put(CSRWebConstants.VALUE_KEY,
								ratingOption.getTitle());
						ratingOptionsJsonData.put(CSRWebConstants.RATING_KEY,
								ratingOption.getRating());

						CSRSurkusApiResponse surkusAPIResponse = mWebServiceSingletonObject
								.postQuestionOptionRating(mSurkusToken,
										ratingOptionsJsonData.toString());
						
						int responseCode = surkusAPIResponse.getStatusCode();
						
						if (responseCode == CSRWebConstants.STATUS_CODE_401)
						{
							isAllRatnigsPosted = false;
							new UpdateSurkusTokenTask().execute(Session.getActiveSession().getAccessToken());
							isSubmitTaskCancelled = true;
							break;
						}
						else if (responseCode == CSRWebConstants.STATUS_CODE_400)
						{
							isAllRatnigsPosted = false;
							dismissFetchSurkusGoerInfoOrSubmitDialog();
							CSRUtils.showAlertDialog(getActivity(),CSRWebConstants.SERVER_ERROR,getString(R.string.category_not_found));
							isSubmitTaskCancelled = true;
							break;
						}
						else if (responseCode == CSRWebConstants.STATUS_CODE_500)
						{
							isAllRatnigsPosted = false;
							dismissFetchSurkusGoerInfoOrSubmitDialog();
							CSRUtils.showAlertDialog(getActivity(),CSRWebConstants.SERVER_ERROR,getString(R.string.server_error_msg));
							isSubmitTaskCancelled = true;
							break;
						}
						else if (responseCode == CSRWebConstants.STATUS_CODE_200)
						{
							/// Continue posting 
						}

					} catch (JSONException e) {

					}

				}
			}
	
			dismissFetchSurkusGoerInfoOrSubmitDialog();
			return isAllRatnigsPosted;

		}

		@Override
		protected void onPostExecute(Boolean isAllRatnigsPosted) {
			
			if(isAllRatnigsPosted)
			{
                ((ASRSurkusGoerDashboardActivity)getActivity()).setHasCategories(true);
				FSRSurkusGoerApprovalPendingFragment surkusGoerDashboardFragment = new FSRSurkusGoerApprovalPendingFragment();	
				Bundle currentMenuPositionBundle = new Bundle();
				currentMenuPositionBundle.putInt(CSRConstants.SURKUS_USER_MENU_INDEX, 0);
				surkusGoerDashboardFragment.setArguments(currentMenuPositionBundle);
				fragmentManager.beginTransaction().replace(R.id.container, surkusGoerDashboardFragment).commit();
			}
		}
	}

	void submitRatingQuestionAnswers() {
		if(mRatingQuestionsAdapter.getCount() > 0)
		{
		  if (mRatingQuestionsAdapter.isAllQuestionsAnswered()) {
			
			new SubmitRatingQuestionTask(mSurkusToken, mRatingQuestionsAdapter.getRatingQuestions()).execute();

		  } else {
			CSRUtils.showAlertDialog(getActivity(),
					CSRWebConstants.SERVER_ERROR,
					getString(R.string.rating_alert));
		   }
		}
		else
		{
			
		}
			
	}

	void launchLoginActivity() {
		Intent loginActivityIntent = new Intent(getActivity(),
				ASRLoginActivity.class);
		startActivity(loginActivityIntent);
		getActivity().finish();
	}

	@Override
	public void onClick(View v) {
		int viewID = v.getId();
		switch (viewID) {

		case R.id.menu_btn:
			Bundle userInfoBundle = new Bundle();
			userInfoBundle.putInt(CSRConstants.SURKUS_USER_MENU_INDEX,
					selectedFragmentPosition);
			userInfoBundle.putString(CSRConstants.USER_NAME, mSurkusGoerUser
					.getFacebookUserInfo().getName());
			FragmentTransaction transaction = fragmentManager
					.beginTransaction();
			transaction.setCustomAnimations(R.anim.right_to_left_slide_in,
					R.anim.right_to_left_slide_out,
					R.anim.left_to_right_slide_in,
					R.anim.left_to_right_slide_out);
			Fragment surkusGoerMenuFragment = new FSRSurkusGoerMenuListFragment();
			surkusGoerMenuFragment.setArguments(userInfoBundle);
			transaction.add(R.id.container, surkusGoerMenuFragment);
			transaction.addToBackStack("");
			transaction.commit();
			break;

		case R.id.submit_rating_btn:
			submitRatingQuestionAnswers();
			break;

		case R.id.facebook_share_imageview:
			((ShareOnFacebookInterface) getActivity()).shareOnFacebook();
			break;

		case R.id.twitter_share_imageview:
		 	 CSRUtils.shareOnTwitterEmail(getActivity(),"Get Paid to Party with SURKUS! \n Here's an exclusive invite for you to join SURKUS so you can get paid to party too! Sign-up at https://app.surkus.com", CSRWebServices.SHARE_URL);    	
			 
			break;

		default:
			break;
		}

	}

}
