package com.surkus.android.surkusgoer.fragment;

import com.surkus.android.R;
import com.surkus.android.surkusgoer.component.ASRSurkusGoerDashboardActivity;
import com.surkus.android.utils.CSRConstants;
import com.surkus.android.utils.CSRUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FSRSurkusGoerPaymentFragment extends Fragment implements OnClickListener{
	private Button mMenuButton;
	private FragmentManager fragmentManager;
	private String userName = "";
	private int selectedFragmentPosition = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_sukurs_goer_payment, container, false);
		mMenuButton = (Button)rootView.findViewById(R.id.menu_btn);
		mMenuButton.setOnClickListener(this);
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

	default:
		break;
	}
		
	}

}
