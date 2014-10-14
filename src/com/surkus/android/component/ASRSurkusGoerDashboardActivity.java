package com.surkus.android.component;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.surkus.android.R;
import com.surkus.android.fragment.FSRSurkusGoerDashboardApprovalPendingFragment;

public class ASRSurkusGoerDashboardActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sukurs_goer_dashboard);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new FSRSurkusGoerDashboardApprovalPendingFragment()).commit();
		}
	}
}
