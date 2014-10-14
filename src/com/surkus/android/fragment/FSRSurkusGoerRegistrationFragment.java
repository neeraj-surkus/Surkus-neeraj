package com.surkus.android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.surkus.android.R;
import com.surkus.android.model.CSRSurkusUser;
import com.surkus.android.networking.CSRWebConstants;
import com.surkus.android.utils.CSRConstants;

public class FSRSurkusGoerRegistrationFragment extends Fragment {

	View mSurkusGoerRegistationParentView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mSurkusGoerRegistationParentView = inflater.inflate(
				R.layout.fragment_user_registration, container, false);
		
		initializeUserDetails();

		return mSurkusGoerRegistationParentView;
	}

	void initializeUserDetails() {
		Bundle userInformation = getArguments();
		if (userInformation != null) {
			CSRSurkusUser surkusUser= (CSRSurkusUser) userInformation.getSerializable(CSRWebConstants.SURKUS_USER);
			String userName = surkusUser.getName();
			String userEmailID = surkusUser.getEmailID();
			String userGender = surkusUser.getGender();
			String userDOB = surkusUser.getDateOfBirth();

			if(userName != null)
				((EditText)mSurkusGoerRegistationParentView.findViewById(R.id.name_edittext)).setText(userName);
			

			if(userEmailID != null)
				((EditText)mSurkusGoerRegistationParentView.findViewById(R.id.email_edittext)).setText(userEmailID);
			

			if(userGender != null)
				((EditText)mSurkusGoerRegistationParentView.findViewById(R.id.gender_edittext)).setText(userGender);
			

			if(userDOB != null)
				((EditText)mSurkusGoerRegistationParentView.findViewById(R.id.dob_edittext)).setText(userDOB);
		
		}
	}
}
