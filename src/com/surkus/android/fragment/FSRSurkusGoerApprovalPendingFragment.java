package com.surkus.android.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.surkus.android.R;
import com.surkus.android.model.CSRSurkusGoerUser;
import com.surkus.android.networking.CSRWebServices;
import com.surkus.android.utils.CSRConstants;
import com.surkus.android.utils.CSRImageLoader;
import com.surkus.android.utils.CSRUtils;

public class FSRSurkusGoerApprovalPendingFragment extends Fragment {
	
	private CSRWebServices webServiceSingletonObject;
	String mSurkusToken;
	GetSurkusUserInfoTask mGetUserInfoTask;	
	private ProgressDialog mFetchSurkusGoerInfoDialog;
	ImageLoader mImageLoader;
	
	ImageView mSurkusGoerUserProfileImageview;
	TextView mSurkusGoerUserNameTextview;
	TextView mSurkusGoerUserEmailIDTextview;
	TextView mSurkusGoerUserPhoneNoTextview;
	TextView mSurkusGoerUserAddressTextview;
	
	LinearLayout mSurkusGoerInfoLayout;
	LinearLayout mSocialShareLayout;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_sukurs_goer_approval_pending, container,false);
		
		mSurkusGoerUserProfileImageview = (ImageView)rootView.findViewById(R.id.user_profile_imageview);		
		mSurkusGoerUserNameTextview = (TextView)rootView.findViewById(R.id.user_name_textview);
		mSurkusGoerUserEmailIDTextview = (TextView)rootView.findViewById(R.id.user_emailid_textview);
		mSurkusGoerUserPhoneNoTextview = (TextView)rootView.findViewById(R.id.user_phone_number_textview);
		mSurkusGoerUserAddressTextview = (TextView)rootView.findViewById(R.id.user_address_number_textview);				
		mSurkusGoerInfoLayout = (LinearLayout)rootView.findViewById(R.id.user_info_layout);
		mSocialShareLayout = (LinearLayout)rootView.findViewById(R.id.social_share_layout);
			
		mImageLoader = CSRImageLoader.getSingletonRef(getActivity()).getImageLoader();
		webServiceSingletonObject = CSRWebServices.getSingletonRef();
		mSurkusToken = CSRUtils.getStringSharedPref(getActivity(), CSRConstants.SURKUS_TOKEN_SHARED_PREFERENCE_KEY);
		
		mGetUserInfoTask = new GetSurkusUserInfoTask(mSurkusToken);
		mGetUserInfoTask.execute();
		
		return rootView;
	}
	

void initiailzeUIFields(CSRSurkusGoerUser surkusGoerUser)
{
	mImageLoader.displayImage(surkusGoerUser.getProfilePicture(), mSurkusGoerUserProfileImageview);
	mSurkusGoerUserNameTextview.setText(surkusGoerUser.getFacebookUserInfo().getName())	;
	mSurkusGoerUserEmailIDTextview.setText(surkusGoerUser.getFacebookUserInfo().getEmail())	;
	mSurkusGoerUserPhoneNoTextview.setText(surkusGoerUser.getCellPhone());
	
	StringBuilder address = new StringBuilder();
	address.append(surkusGoerUser.getAddress().getCity());
	address.append(", "+surkusGoerUser.getAddress().getState());
	address.append(", "+surkusGoerUser.getAddress().getCountry());
	mSurkusGoerUserAddressTextview.setText(address.toString());
	
	dismissFetchSurkusGoerInfoDialog();
	mSurkusGoerInfoLayout.setVisibility(View.VISIBLE);
	mSocialShareLayout.setVisibility(View.VISIBLE);
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
        	   if(mGetUserInfoTask != null && !mGetUserInfoTask.isCancelled())
        	   {
        		   dismissFetchSurkusGoerInfoDialog();
        		   mGetUserInfoTask.cancel(true);
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




	private class GetSurkusUserInfoTask extends
			AsyncTask<String, Integer, CSRSurkusGoerUser> {

		String mSurkusToken;
		GetSurkusUserInfoTask(String surkusToken) {
			mSurkusToken = surkusToken;
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			displayFetchSurkusGoerInfoDialog();
		
		}

		@Override
		protected CSRSurkusGoerUser doInBackground(String... args) {

			CSRSurkusGoerUser surkusUser = webServiceSingletonObject.getSurkusUserInfo(mSurkusToken);
			Log.e("Surkus","Surkus user info : " + surkusUser.getProfilePicture());
			Log.e("Surkus", "Surkus user info : " + surkusUser.toString());
			return surkusUser;

		}

		@Override
		protected void onPostExecute(CSRSurkusGoerUser surkusGoerUser) {		
			initiailzeUIFields(surkusGoerUser);
		}
	}

}