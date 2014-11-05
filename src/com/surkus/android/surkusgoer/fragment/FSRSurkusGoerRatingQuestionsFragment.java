package com.surkus.android.surkusgoer.fragment;

import java.util.ArrayList;

import android.app.ProgressDialog;
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

import com.surkus.android.R;
import com.surkus.android.adapter.CSRSurkusGoerRatingQuestionsAdapter;
import com.surkus.android.model.CSRRatingQuestion;
import com.surkus.android.model.CSRSurkusGoer;
import com.surkus.android.networking.CSRWebServices;
import com.surkus.android.utils.CSRConstants;
import com.surkus.android.utils.CSRUtils;
import com.surkus.android.utils.CSRUtils.ShareOnFacebookInterface;

public class FSRSurkusGoerRatingQuestionsFragment extends Fragment implements
		OnClickListener {
	
	private CSRWebServices mWebServiceSingletonObject;
	private GetRatingQuestionsTask mGetSurkusGoerInfoTask;	
	private ProgressDialog mFetchSurkusGoerInfoDialog;
	
	private ArrayList<CSRRatingQuestion> mRatingQuestionList;
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

		mRatingQuestionsAdapter = new CSRSurkusGoerRatingQuestionsAdapter(getActivity());
		
		((ListView) rootView.findViewById(R.id.rating_questions_list)).setAdapter(mRatingQuestionsAdapter);
		
		mMenuButton = (Button)rootView.findViewById(R.id.menu_btn);
		mMenuButton.setOnClickListener(this);
		
		mFacebookShareImageview = (ImageView)rootView.findViewById(R.id.facebook_share_imageview);
		mFacebookShareImageview.setOnClickListener(this);
		
		mTwitterShareImageview = (ImageView)rootView.findViewById(R.id.twitter_share_imageview);
		mTwitterShareImageview.setOnClickListener(this);
		
		mSubmitRatingQuestionsBtn = ((Button) rootView.findViewById(R.id.submit_rating_btn));
		mSubmitRatingQuestionsBtn.setOnClickListener(this);
		
		mSocialShareLayout = (RelativeLayout)rootView.findViewById(R.id.social_share_layout);
		
		mRatingQuestionsLayout = (LinearLayout)rootView.findViewById(R.id.rating_questions_layout);
		
		mSurkusToken = CSRUtils.getStringSharedPref(getActivity(), CSRConstants.SURKUS_TOKEN_SHARED_PREFERENCE_KEY);
		mWebServiceSingletonObject = CSRWebServices.getSingletonRef();
		
		mGetSurkusGoerInfoTask = new GetRatingQuestionsTask(mSurkusToken);
		mGetSurkusGoerInfoTask.execute();
		
        fragmentManager = getActivity().getSupportFragmentManager();
		
		if(getArguments() != null)
			selectedFragmentPosition = getArguments().getInt(CSRConstants.SURKUS_USER_MENU_INDEX, 0);

		return rootView;
	}
	
	void displayFetchSurkusGoerInfoDialog()
	{
	  try
	 {
		mFetchSurkusGoerInfoDialog = new ProgressDialog(getActivity());
		mFetchSurkusGoerInfoDialog.setMessage(getActivity().getResources().getString(R.string.loading));
		mFetchSurkusGoerInfoDialog.setCancelable(false);
		mFetchSurkusGoerInfoDialog.setCanceledOnTouchOutside(false);
		mFetchSurkusGoerInfoDialog.setIndeterminate(true);
		mFetchSurkusGoerInfoDialog.show();
	   }
		catch(Exception e)
		{
			
		}
	}
	
	void dismissFetchSurkusGoerInfoDialog()
	{
	    if(mFetchSurkusGoerInfoDialog != null)
	    {
	       mFetchSurkusGoerInfoDialog.dismiss();
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
			displayFetchSurkusGoerInfoDialog();

		}

		@Override
		protected ArrayList<CSRRatingQuestion> doInBackground(String... args) {			
			mSurkusGoerUser = mWebServiceSingletonObject.getSurkusUserInfo(mSurkusToken);	
			CSRUtils.createStringSharedPref(getActivity(),CSRConstants.SURKUS_USER_NAME,mSurkusGoerUser.getFacebookUserInfo().getName());	
			ArrayList<CSRRatingQuestion> ratingQuestions = mWebServiceSingletonObject.getRatingQuestions(mSurkusToken);	
			
			return ratingQuestions;

		}

		@Override
		protected void onPostExecute(ArrayList<CSRRatingQuestion> ratingQuestions) {
			initiailzeUIFields(ratingQuestions);				
		}
	}

	void initiailzeUIFields(ArrayList<CSRRatingQuestion> ratingQuestions)
	{
		dismissFetchSurkusGoerInfoDialog();
		mRatingQuestionsAdapter.setRatingQuestions(ratingQuestions);
		mSocialShareLayout.setVisibility(View.VISIBLE);
		mRatingQuestionsLayout.setVisibility(View.VISIBLE);
		mSubmitRatingQuestionsBtn.setVisibility(View.VISIBLE);
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

			break;

		case R.id.facebook_share_imageview:
			((ShareOnFacebookInterface) getActivity()).shareOnFacebook();
			break;

		case R.id.twitter_share_imageview:
			CSRUtils.shareOnTwitterEmail(getActivity(),
					"Get Paid to Party with SURKUS! — ",
					CSRWebServices.SHARE_URL);
			break;

		default:
			break;
		}

	}

}
