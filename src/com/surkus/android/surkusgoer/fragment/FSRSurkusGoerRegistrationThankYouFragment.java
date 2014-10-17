package com.surkus.android.surkusgoer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.surkus.android.R;
import com.surkus.android.surkusgoer.component.ASRSurkusGoerDashboardActivity;

public class FSRSurkusGoerRegistrationThankYouFragment extends Fragment implements OnClickListener{
	
	Button mGoToProfileButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_registration_thank_you, container, false);
		mGoToProfileButton = (Button) rootView.findViewById(R.id.go_to_profile_btn);
		mGoToProfileButton.setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onClick(View v) {
	   int viewID = v.getId();
	   
	   switch (viewID) {
	     case R.id.go_to_profile_btn:	    	
	     Intent surkusGoerRegistrationIntent = new Intent(getActivity(),ASRSurkusGoerDashboardActivity.class);
		 startActivity(surkusGoerRegistrationIntent); 
		 getActivity().finish();
	    	 
		break;

	default:
		break;
	}
		
	}
}