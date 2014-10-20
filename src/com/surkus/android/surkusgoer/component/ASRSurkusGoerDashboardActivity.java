package com.surkus.android.surkusgoer.component;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.surkus.android.R;
import com.surkus.android.surkusgoer.fragment.FSRSurkusGoerApprovalPendingFragment;

public class ASRSurkusGoerDashboardActivity extends FragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sukurs_goer_dashboard);
	    getSupportFragmentManager().beginTransaction().add(R.id.container,new FSRSurkusGoerApprovalPendingFragment()).commit();	    
		
	}
}
