package com.surkus.android.surkusgoer.component;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.surkus.android.R;
import com.surkus.android.surkusgoer.fragment.FSRSurkusGoerRegistrationFragment;

public class ASRSurkusGoerRegistrationActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_registration); 				
		getSupportFragmentManager().beginTransaction().replace(R.id.container, new FSRSurkusGoerRegistrationFragment()).commit();
		
	}
}
