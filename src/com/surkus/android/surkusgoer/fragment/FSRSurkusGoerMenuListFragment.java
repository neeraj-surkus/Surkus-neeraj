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
import com.surkus.android.utils.CSRConstants;

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
		
		if(userName != null && !TextUtils.isEmpty(userName))
		    ((TextView) rootView.findViewById(R.id.surkus_goer_name)).setText(userName);
		else
			((TextView) rootView.findViewById(R.id.surkus_goer_name)).setText("User");

		((LinearLayout) rootView.findViewById(R.id.hide_menu_layout)).setOnClickListener(this);
		((ImageView) rootView.findViewById(R.id.dismiss_surkus_goer_menu)).setOnClickListener(this);		
		((Button) rootView.findViewById(R.id.surkusgoer_logout_button)).setOnClickListener(this);
		
		fragmentManager = getActivity().getSupportFragmentManager();

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
		if (position != selectedFragmentPosition)
			switch (position) {

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
			launchLoginActivity();

			break;
			
		default:
			break;
		}

	}
	
	
	void launchLoginActivity()
	{
	   Intent loginIntent = new Intent(getActivity(), ASRLoginActivity.class);
	   loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	   startActivity(loginIntent);
	}

}
