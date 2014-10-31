package com.surkus.android.surkusgoer.fragment;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.surkus.android.R;
import com.surkus.android.adapter.CSRSurkusGoerMenuAdapter;
import com.surkus.android.component.ASRLoginActivity;
import com.surkus.android.networking.CSRWebServices;
import com.surkus.android.utils.CSRConstants;
import com.surkus.android.utils.CSRUtils;
import com.surkus.android.utils.DeleteSurkusUserTask;

public class FSRSurkusGoerMenuListFragment extends ListFragment implements
		OnClickListener {
	public CSRSurkusGoerMenuAdapter adapter;
	private int selectedFragmentPosition = 0;
	JSONObject mObject;
	private FragmentManager fragmentManager;
	

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.slider_list, null);
		String userName = getArguments().getString(CSRConstants.USER_NAME);

		if (userName != null && !TextUtils.isEmpty(userName))
			((TextView) rootView.findViewById(R.id.surkus_goer_name))
					.setText(userName);
		else
			((TextView) rootView.findViewById(R.id.surkus_goer_name))
					.setText("User");

		((LinearLayout) rootView.findViewById(R.id.hide_menu_layout))
				.setOnClickListener(this);
		((ImageView) rootView.findViewById(R.id.dismiss_surkus_goer_menu))
				.setOnClickListener(this);
		((Button) rootView.findViewById(R.id.surkusgoer_logout_button))
				.setOnClickListener(this);

		fragmentManager = getActivity().getSupportFragmentManager();
		
		if(getArguments() != null)
			selectedFragmentPosition = getArguments().getInt(CSRConstants.SURKUS_USER_MENU_INDEX, 0);

		return rootView;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		adapter = new CSRSurkusGoerMenuAdapter(getActivity());
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		fragmentManager.popBackStack();		
		Bundle currentMenuPositionBundle = new Bundle();
		if (position != selectedFragmentPosition)
			switch (position) {
			
			case 0:
				//selectedFragmentPosition = 0;

				currentMenuPositionBundle.putInt(CSRConstants.SURKUS_USER_MENU_INDEX, 0);
				FSRSurkusGoerApprovalPendingFragment surkusGoerDashboardFragment = new FSRSurkusGoerApprovalPendingFragment();	
				surkusGoerDashboardFragment.setArguments(currentMenuPositionBundle);
				fragmentManager.beginTransaction().replace(R.id.container, surkusGoerDashboardFragment).addToBackStack("1").commit();
				break;

			case 1:
				//selectedFragmentPosition = 1;
	
				currentMenuPositionBundle.putInt(CSRConstants.SURKUS_USER_MENU_INDEX, 1);
				FSRSurkusGoerPaymentFragment surkusGoerPaymentFragment = new FSRSurkusGoerPaymentFragment();	
				surkusGoerPaymentFragment.setArguments(currentMenuPositionBundle);
				fragmentManager.beginTransaction().replace(R.id.container, surkusGoerPaymentFragment).addToBackStack("1").commit();
				break;
				
			case 2:		
				//selectedFragmentPosition = 2;
				currentMenuPositionBundle.putInt(CSRConstants.SURKUS_USER_MENU_INDEX, 2);
				FSRSurkusGoerShareFragment surkusGoerShareFragment = new FSRSurkusGoerShareFragment();		
				surkusGoerShareFragment.setArguments(currentMenuPositionBundle);
				fragmentManager.beginTransaction().replace(R.id.container, surkusGoerShareFragment).addToBackStack("1").commit();
					
				break;

			case 3:
				//selectedFragmentPosition = -1;
				CSRUtils.openURLInBrowser(getActivity(),
						CSRWebServices.ABOUT_SURKUS_URL);
				break;

			case 4:
				//selectedFragmentPosition = -1;
				CSRUtils.openURLInBrowser(getActivity(),
						CSRWebServices.TERMS_AND_CONDITION_URL);
				break;
				
			case 5:
				//selectedFragmentPosition = -1;
				CSRUtils.openURLInBrowser(getActivity(),
						CSRWebServices.PRIVACY_URL);
				break;

			}
	}

	@Override
	public void onClick(View v) {
		int viewID = v.getId();

		switch (viewID) {
		case R.id.hide_menu_layout:
			fragmentManager.popBackStack();
			break;

		case R.id.dismiss_surkus_goer_menu:
			fragmentManager.popBackStack();

			break;
		case R.id.surkusgoer_logout_button:
			clearUserDataAndLaunchLoginActivity();

			break;

		default:
			break;
		}

	}

	void clearUserDataAndLaunchLoginActivity() {
		CSRUtils.facebookLogout(getActivity());
		CSRUtils.logoutSurkusUser(getActivity());
	//    new  DeleteSurkusUserTask(CSRUtils.getStringSharedPref(getActivity(), CSRConstants.SURKUS_TOKEN_SHARED_PREFERENCE_KEY)).execute();
		Intent loginIntent = new Intent(getActivity(), ASRLoginActivity.class);
		loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(loginIntent);
	}

}
