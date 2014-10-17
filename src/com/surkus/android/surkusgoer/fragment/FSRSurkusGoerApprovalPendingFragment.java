package com.surkus.android.surkusgoer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.surkus.android.R;

public class FSRSurkusGoerApprovalPendingFragment extends Fragment {
	
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
		
		return rootView;
	}


}