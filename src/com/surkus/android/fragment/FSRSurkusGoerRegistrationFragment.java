package com.surkus.android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.surkus.android.R;

public class FSRSurkusGoerRegistrationFragment extends Fragment {

	View mSurkusGoerRegistationParentView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mSurkusGoerRegistationParentView = inflater.inflate(
				R.layout.fragment_user_registration, container, false);
		
		//initializeUserDetails();

		return mSurkusGoerRegistationParentView;
	}

	void initializeUserDetails() {
		
	}
}
