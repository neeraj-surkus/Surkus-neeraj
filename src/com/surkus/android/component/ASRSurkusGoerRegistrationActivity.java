package com.surkus.android.component;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.surkus.android.R;
import com.surkus.android.fragment.FSRSurkusGoerRegistrationFragment;
import com.surkus.android.networking.CSRWebConstants;

public class ASRSurkusGoerRegistrationActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_registration); 		
		FSRSurkusGoerRegistrationFragment surkusGoerRegisterFragment =  new FSRSurkusGoerRegistrationFragment();
		getSupportFragmentManager().beginTransaction().add(R.id.container, surkusGoerRegisterFragment).commit();
	}
}
