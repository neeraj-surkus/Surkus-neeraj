package com.surkus.android.surkusgoer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.surkus.android.R;
import com.surkus.android.utils.CSRConstants;

public class FSRSurkusGoerApprovalPendingFragment extends Fragment  implements OnClickListener{
	
	ImageView mSurkusGoerUserProfileImageview;
	TextView mSurkusGoerUserNameTextview;
	TextView mSurkusGoerUserEmailIDTextview;
	TextView mSurkusGoerUserPhoneNoTextview;
	TextView mSurkusGoerUserAddressTextview;
	
	LinearLayout mSurkusGoerInfoLayout;
	LinearLayout mSocialShareLayout;
	Button mMenuButton;
	private FragmentManager fragmentManager;

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
		
		mMenuButton = (Button)rootView.findViewById(R.id.menu_btn);
		mMenuButton.setOnClickListener(this);
		
		fragmentManager = getFragmentManager();
		
		return rootView;
	}


	@Override
	public void onClick(View v) {		
		Bundle userInfoBundle = new Bundle();
		userInfoBundle.putString(CSRConstants.USER_NAME, "");		
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.setCustomAnimations(R.anim.right_to_left_slide_in,R.anim.right_to_left_slide_out, R.anim.left_to_right_slide_in, R.anim.left_to_right_slide_out);
		Fragment surkusGoerMenuFragment = new FSRSurkusGoerMenuListFragment();	
		surkusGoerMenuFragment.setArguments(userInfoBundle);
		transaction.add(R.id.container, surkusGoerMenuFragment );
		transaction.addToBackStack("");
		transaction.commit();
	}


}