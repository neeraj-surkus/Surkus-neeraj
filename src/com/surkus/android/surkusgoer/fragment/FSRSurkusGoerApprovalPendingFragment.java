package com.surkus.android.surkusgoer.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.surkus.android.R;
import com.surkus.android.component.ASRLoginActivity;
import com.surkus.android.model.CSRSurkusGoer;
import com.surkus.android.networking.CSRWebServices;
import com.surkus.android.utils.CSRConstants;
import com.surkus.android.utils.CSRImageLoader;
import com.surkus.android.utils.CSRUtils;
import com.surkus.android.utils.CSRUtils.ShareOnFacebookInterface;

public class FSRSurkusGoerApprovalPendingFragment extends Fragment implements OnClickListener{
	
	private CSRWebServices mWebServiceSingletonObject;
	private String mSurkusToken;
	private GetSurkusGoerInfoTask mGetSurkusGoerInfoTask;	
	private ProgressDialog mFetchSurkusGoerInfoDialog;
	private ImageLoader mImageLoader;
	private ImageView mSurkusGoerProfileImageview;
	private TextView mSurkusGoerUserAccountStatusTextview;
	private TextView mSurkusGoerUserNameTextview;
	private TextView mSurkusGoerUserEmailIDTextview;
	private TextView mSurkusGoerUserPhoneNoTextview;
	private TextView mSurkusGoerUserAddressTextview;	
	private LinearLayout mSurkusGoerInfoLayout;
	private RelativeLayout mSocialShareLayout;	
	private Button mMenuButton;
	private FragmentManager fragmentManager;
	
	ImageView mFacebookShareImageview;
	ImageView mTwitterShareImageview;
	
	CSRSurkusGoer mSurkusGoerUser;
	
	private int selectedFragmentPosition = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_sukurs_goer_approval_pending, container,false);
		
		mSurkusGoerProfileImageview = (ImageView)rootView.findViewById(R.id.user_profile_imageview);	
		
		mSurkusGoerUserAccountStatusTextview = (TextView)rootView.findViewById(R.id.account_status_textview);
		
		mSurkusGoerUserNameTextview = (TextView)rootView.findViewById(R.id.user_name_textview);
		mSurkusGoerUserEmailIDTextview = (TextView)rootView.findViewById(R.id.user_emailid_textview);
		mSurkusGoerUserPhoneNoTextview = (TextView)rootView.findViewById(R.id.user_phone_number_textview);
		mSurkusGoerUserAddressTextview = (TextView)rootView.findViewById(R.id.user_address_number_textview);				
		mSurkusGoerInfoLayout = (LinearLayout)rootView.findViewById(R.id.user_info_layout);
		mSocialShareLayout = (RelativeLayout)rootView.findViewById(R.id.social_share_layout);
		
		mMenuButton = (Button)rootView.findViewById(R.id.menu_btn);
		mMenuButton.setOnClickListener(this);
		
		mFacebookShareImageview = (ImageView)rootView.findViewById(R.id.facebook_share_imageview);
		mFacebookShareImageview.setOnClickListener(this);
		
		mTwitterShareImageview = (ImageView)rootView.findViewById(R.id.twitter_share_imageview);
		mTwitterShareImageview.setOnClickListener(this);
			
		mImageLoader = CSRImageLoader.getSingletonRef(getActivity()).getImageLoader();
		mWebServiceSingletonObject = CSRWebServices.getSingletonRef();
		mSurkusToken = CSRUtils.getStringSharedPref(getActivity(), CSRConstants.SURKUS_TOKEN_SHARED_PREFERENCE_KEY);
		
		mGetSurkusGoerInfoTask = new GetSurkusGoerInfoTask(mSurkusToken);
		mGetSurkusGoerInfoTask.execute();
		
		fragmentManager = getActivity().getSupportFragmentManager();
		
		if(getArguments() != null)
			selectedFragmentPosition = getArguments().getInt(CSRConstants.SURKUS_USER_MENU_INDEX, 0);
		
		return rootView;
	}
	

void initiailzeUIFields(CSRSurkusGoer surkusGoerUser)
{
	String accountStatus = "ACCOUNT ";
	if(surkusGoerUser.getStatus() != null)
	accountStatus = "ACCOUNT "+surkusGoerUser.getStatus();
	
	mSurkusGoerUserAccountStatusTextview.setText(accountStatus.toUpperCase());
	mImageLoader.displayImage(surkusGoerUser.getProfilePicture(), mSurkusGoerProfileImageview);
	mSurkusGoerUserNameTextview.setText(surkusGoerUser.getFacebookUserInfo().getName())	;
	mSurkusGoerUserEmailIDTextview.setText(surkusGoerUser.getFacebookUserInfo().getEmail())	;
	mSurkusGoerUserPhoneNoTextview.setText(surkusGoerUser.getCellPhone());
	
	StringBuilder address = new StringBuilder();
	address.append(surkusGoerUser.getAddress().getCity());
	address.append(", "+surkusGoerUser.getAddress().getState());
	address.append(", "+surkusGoerUser.getAddress().getZipCode());
	mSurkusGoerUserAddressTextview.setText(address.toString());
	
	dismissFetchSurkusGoerInfoDialog();
	mSurkusGoerInfoLayout.setVisibility(View.VISIBLE);
	
	mSurkusGoerUser = surkusGoerUser;	
	CSRUtils.createStringSharedPref(getActivity(),CSRConstants.SURKUS_USER_NAME,surkusGoerUser.getFacebookUserInfo().getName());
}
	
    
@Override
public void onStart() {
   super.onStart();

   getView().setFocusableInTouchMode(true);
   getView().requestFocus();
   getView().setOnKeyListener( new OnKeyListener()
   {
       @Override
       public boolean onKey( View v, int keyCode, KeyEvent event )
       {
           if( keyCode == KeyEvent.KEYCODE_BACK )
           {
        	   if(mGetSurkusGoerInfoTask != null && !mGetSurkusGoerInfoTask.isCancelled())
        	   {
        		   dismissFetchSurkusGoerInfoDialog();
        		   mGetSurkusGoerInfoTask.cancel(true);
        	   }
           }
           return false;
       }
   } );
}

void displayFetchSurkusGoerInfoDialog()
{
  try
 {
	mFetchSurkusGoerInfoDialog = new ProgressDialog(getActivity());
	mFetchSurkusGoerInfoDialog.setMessage(getActivity().getResources().getString(R.string.loading));
	mFetchSurkusGoerInfoDialog.setCancelable(true);
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
			displayFetchSurkusGoerInfoDialog();
		
		}

		@Override
		protected CSRSurkusGoer doInBackground(String... args) {

			CSRSurkusGoer surkusUser = mWebServiceSingletonObject.getSurkusUserInfo(mSurkusToken);

			return surkusUser;

		}

		@Override
		protected void onPostExecute(CSRSurkusGoer surkusGoerUser) {		
			initiailzeUIFields(surkusGoerUser);
		}
	}




	@Override
	public void onClick(View v) {			
	   int viewID = v.getId();		   
	   switch (viewID) {
	   
	 
	    case R.id.menu_btn:
		 Bundle userInfoBundle = new Bundle();
		 userInfoBundle.putInt(CSRConstants.SURKUS_USER_MENU_INDEX, selectedFragmentPosition);
		 userInfoBundle.putString(CSRConstants.USER_NAME, mSurkusGoerUser.getFacebookUserInfo().getName());		
		 FragmentTransaction transaction = fragmentManager.beginTransaction();
		 transaction.setCustomAnimations(R.anim.right_to_left_slide_in,R.anim.right_to_left_slide_out, R.anim.left_to_right_slide_in, R.anim.left_to_right_slide_out);
		 Fragment surkusGoerMenuFragment = new FSRSurkusGoerMenuListFragment();	
		 surkusGoerMenuFragment.setArguments(userInfoBundle);
		 transaction.add(R.id.container, surkusGoerMenuFragment );
		 transaction.addToBackStack("");
		 transaction.commit();
		break;
		
	
		}
	   
	}

}