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
import com.surkus.android.listener.ISRPostRatingForCategoryInterface;
import com.surkus.android.listener.ISRShareOnFacebookInterface;
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

public class FSRSurkusGoerRatingQuestionsFragment extends Fragment implements ISRPostRatingForCategoryInterface , OnClickListener {

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

		mRatingQuestionsAdapter = new CSRSurkusGoerRatingQuestionsAdapter(getActivity(),this);

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
	//Clear rating from share pref
	
	void refreshRatedQuestionsAnswesrRating(ArrayList<CSRRatingQuestion> ratingQuestions)
	{
		ArrayList<CSRRatingOption> userRatedOptions = mSurkusGoerUser.getRatedQuestionOptions();
		
		for(int i=0;i<ratingQuestions.size();i++)
			{
				CSRRatingQuestion ratingQuestion = ratingQuestions.get(i);
				String ratingQuestionStr  = ratingQuestion.getRatingQuestion();
			    ArrayList<CSRRatingOption> ratingOptions =  ratingQuestion.getRatingOptionList();
			    
			    for(int j=0;j<ratingOptions.size();j++)
			    {
			    	CSRRatingOption ratingOption = ratingOptions.get(j);
			    	
			    	for(int k=0;k<userRatedOptions.size();k++)
			    	{
			    		CSRRatingOption ratedOption = userRatedOptions.get(k);
			    		if(ratedOption.getRatingQuestion().equalsIgnoreCase(ratingQuestionStr) && ratedOption.geCategory().equalsIgnoreCase(ratingOption.geCategory()))
			    		{
			    			try
			    			{
			    			    ratingOption.setRating(Integer.parseInt(ratedOption.getRatingString()));
			    			}
			    			catch(NumberFormatException e)
			    			{
			    				
			    			}
			    			break;
			    		}
			    		
			    	}
			    }
			
			}				
	
	//	return ratingQuestions;
	}
	
	void saveNumberOfCategoriesInPref(ArrayList<CSRRatingQuestion> ratingQuestions)
	{
		int totalCategories = 0;
		for(int i=0;i<ratingQuestions.size();i++)
		{
			totalCategories += ratingQuestions.get(i).getRatingOptionList().size();
		}	
		
		CSRUtils.createIntSharedPref(getActivity(), CSRConstants.NUMBER_OF_CATEGORIES, totalCategories);
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
			
		//	saveNumberOfCategoriesInPref(ratingQuestions);			
			
			ArrayList<CSRRatingOption> userRatedOptions = mSurkusGoerUser.getRatedQuestionOptions();
			
			if(userRatedOptions != null && userRatedOptions.size() >0)
			{
			     refreshRatedQuestionsAnswesrRating(ratingQuestions);
			}

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
		CSRRatingOption mRatingOption;
		String mRatingQuestion;
			
		UpdateSurkusTokenTask(String surkusToken,CSRRatingOption ratingOption,String ratingQuestion) {
			mSurkusToken = surkusToken;
			mRatingOption = ratingOption;
		    mRatingQuestion = ratingQuestion;

		}
		
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
				new SubmitRatingQuestionTask(mSurkusToken, mRatingOption,mRatingQuestion).execute();
			}
		}
	}

	private class SubmitRatingQuestionTask extends
			AsyncTask<String, Integer, Boolean> {

		String mSurkusToken;	
		CSRRatingOption mRatingOption;
		String mRatingQuestion;
		
		SubmitRatingQuestionTask(String surkusToken,
				CSRRatingOption ratingOption,String ratingQuestion) {
			mSurkusToken = surkusToken;
			mRatingOption = ratingOption;
		    mRatingQuestion = ratingQuestion;

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			displayFetchSurkusGoerInfoOrSubmitDialog(getActivity().getResources().getString(R.string.submitting_rating_msg));

		}

		@Override
		protected Boolean doInBackground(String... args) {
		    boolean isAllRatnigsPosted = true;
		
				try {
						
						JSONObject ratingOptionsJsonData = new JSONObject();
						ratingOptionsJsonData.put(CSRWebConstants.NAME_KEY,
								mRatingQuestion);
						ratingOptionsJsonData.put(CSRWebConstants.VALUE_KEY,
								mRatingOption.geCategory());
						ratingOptionsJsonData.put(CSRWebConstants.RATING_KEY,
								mRatingOption.getRating());

						CSRSurkusApiResponse surkusAPIResponse = mWebServiceSingletonObject
								.postQuestionOptionRating(mSurkusToken,
										ratingOptionsJsonData.toString());
						
						int responseCode = surkusAPIResponse.getStatusCode();
						
						if (responseCode == CSRWebConstants.STATUS_CODE_401)
						{
							isAllRatnigsPosted = false;
							new UpdateSurkusTokenTask(mSurkusToken,mRatingOption,mRatingQuestion).execute(Session.getActiveSession().getAccessToken());					
						}
						else if (responseCode == CSRWebConstants.STATUS_CODE_400)
						{
							isAllRatnigsPosted = false;
							dismissFetchSurkusGoerInfoOrSubmitDialog();
							CSRUtils.showAlertDialog(getActivity(),CSRWebConstants.SERVER_ERROR,getString(R.string.category_not_found));					
						}
						else if (responseCode == CSRWebConstants.STATUS_CODE_500)
						{
							isAllRatnigsPosted = false;
							dismissFetchSurkusGoerInfoOrSubmitDialog();
							CSRUtils.showAlertDialog(getActivity(),CSRWebConstants.SERVER_ERROR,getString(R.string.server_error_msg));
						}
						else if (responseCode == CSRWebConstants.STATUS_CODE_200)
						{
							/// Continue posting 
						}

					} catch (JSONException e) {

					}

			return isAllRatnigsPosted;

		}

		@Override
		protected void onPostExecute(Boolean isAllRatnigsPosted) {	
			if(isAllRatnigsPosted)
			dismissFetchSurkusGoerInfoOrSubmitDialog();
		}
	}
	
	
	void submitRatingQuestionAnswers() {
		if(mRatingQuestionsAdapter.getCount() > 0)
		{
		  if (mRatingQuestionsAdapter.isAllQuestionsAnswered()) {
			  
                ((ASRSurkusGoerDashboardActivity)getActivity()).setHasCategories(true);
				FSRSurkusGoerApprovalPendingFragment surkusGoerDashboardFragment = new FSRSurkusGoerApprovalPendingFragment();	
				Bundle currentMenuPositionBundle = new Bundle();
				currentMenuPositionBundle.putInt(CSRConstants.SURKUS_USER_MENU_INDEX, 0);
				surkusGoerDashboardFragment.setArguments(currentMenuPositionBundle);
				fragmentManager.beginTransaction().replace(R.id.container, surkusGoerDashboardFragment).commit();

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
			((ISRShareOnFacebookInterface) getActivity()).shareOnFacebook();
			break;

		case R.id.twitter_share_imageview:
		 	 CSRUtils.shareOnTwitterEmail(getActivity(),"Get Paid to Party with SURKUS! \n Here's an exclusive invite for you to join SURKUS so you can get paid to party too! Sign-up at https://app.surkus.com", CSRWebServices.SHARE_URL);    	
			 
			break;

		default:
			break;
		}

	}


	@Override
	public void postOrUpdateRatingQuestionWithAnswer(
			CSRRatingOption ratingOption, String ratingQuestion) {	
		new SubmitRatingQuestionTask(mSurkusToken,ratingOption,ratingQuestion).execute();		
	}

}
