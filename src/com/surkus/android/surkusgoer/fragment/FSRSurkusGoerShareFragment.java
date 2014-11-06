package com.surkus.android.surkusgoer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.surkus.android.R;
import com.surkus.android.networking.CSRWebServices;
import com.surkus.android.utils.CSRConstants;
import com.surkus.android.utils.CSRUtils;
import com.surkus.android.utils.CSRUtils.ShareOnFacebookInterface;

public class FSRSurkusGoerShareFragment extends Fragment implements OnClickListener{
	private Button mMenuButton;
	private LinearLayout  mFacebookShareLayout;
	private LinearLayout  mTwitterShareLayout;
	private LinearLayout  mEmailShareLayout;
	private LinearLayout  mTextShareLayout;
	private FragmentManager fragmentManager;
	private String userName = "";
	private int selectedFragmentPosition = 0;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_sukurs_goer_share, container, false);
		
		mMenuButton = (Button)rootView.findViewById(R.id.menu_btn);
		mMenuButton.setOnClickListener(this);
		
		mFacebookShareLayout = (LinearLayout)rootView.findViewById(R.id.share_facbook_layout);
		mFacebookShareLayout.setOnClickListener(this);
		
		mTwitterShareLayout = (LinearLayout)rootView.findViewById(R.id.share_twitter_layout);
		mTwitterShareLayout.setOnClickListener(this);
		
		mEmailShareLayout = (LinearLayout)rootView.findViewById(R.id.share_email_layout);
		mEmailShareLayout.setOnClickListener(this);
		
		mTextShareLayout = (LinearLayout)rootView.findViewById(R.id.share_text_layout);
		mTextShareLayout.setOnClickListener(this);
		
		fragmentManager = getActivity().getSupportFragmentManager();
		userName = CSRUtils.getStringSharedPref(getActivity(), CSRConstants.SURKUS_USER_NAME);
		
		if(getArguments() != null)
			selectedFragmentPosition = getArguments().getInt(CSRConstants.SURKUS_USER_MENU_INDEX, 0);
		
		return rootView;
	}
	


	@Override
	public void onClick(View v) {
	   int viewID = v.getId();
	   
	   switch (viewID) {
	     case R.id.menu_btn:	    	
	    	Bundle userInfoBundle = new Bundle();
	    	userInfoBundle.putInt(CSRConstants.SURKUS_USER_MENU_INDEX, selectedFragmentPosition);
	 		userInfoBundle.putString(CSRConstants.USER_NAME, userName);		
	 		FragmentTransaction transaction = fragmentManager.beginTransaction();
	 		transaction.setCustomAnimations(R.anim.right_to_left_slide_in,R.anim.right_to_left_slide_out, R.anim.left_to_right_slide_in, R.anim.left_to_right_slide_out);
	 		Fragment surkusGoerMenuFragment = new FSRSurkusGoerMenuListFragment();	
	 		surkusGoerMenuFragment.setArguments(userInfoBundle);
	 		transaction.add(R.id.container, surkusGoerMenuFragment );
	 		transaction.addToBackStack("");
	 		transaction.commit();
	    	 
		break;
		
	     case R.id.share_facbook_layout:
	   
	    	 ((ShareOnFacebookInterface)getActivity()).shareOnFacebook();	
	   
	    	break;
	    	 
	     case R.id.share_twitter_layout:
		 	 CSRUtils.shareOnTwitterEmail(getActivity(),"Get Paid to Party with SURKUS! \n Here's an exclusive invite for you to join SURKUS so you can get paid to party too! Sign-up at https://app.surkus.com", CSRWebServices.SHARE_URL);    	
				 	 break;
	    	 
	     case R.id.share_email_layout:
	    	 CSRUtils.sendEmail(getActivity(), CSRWebServices.SHARE_URL,"file:///android_asset/surkus_share_image.jpg");
	    	 break;
	    	 
	     case R.id.share_text_layout:
	    	// CSRUtils.sendMessage(getActivity(), "Get Paid to Party with SURKUS! — "+CSRWebServices.SHARE_URL);
	    	 CSRUtils.sendMessage(getActivity(), "Get Paid to Party with SURKUS! \n Here's an exclusive invite for you to join SURKUS so you can get paid to party too! Sign-up at https://app.surkus.com");
	    	 break; 	 
	    	 
    	 
	default:
		break;
	}
		
	}
	
}
