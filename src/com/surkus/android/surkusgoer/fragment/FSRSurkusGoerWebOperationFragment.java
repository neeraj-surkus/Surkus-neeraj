package com.surkus.android.surkusgoer.fragment;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.surkus.android.R;
import com.surkus.android.networking.CSRWebConstants;
import com.surkus.android.utils.CSRConstants;
import com.surkus.android.utils.CSRUtils;

public class FSRSurkusGoerWebOperationFragment extends Fragment implements OnClickListener{

	private ProgressDialog mloadSurkusGoerInfoDialog;
	private WebView userInfoWebView;
	private Button mMenuButton;
	String mUserInfoURL = "";
	private int selectedFragmentPosition = 0;
	private String userName = "";
	private FragmentManager fragmentManager;


	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_web_operation,
				container, false);

		mMenuButton = (Button)rootView.findViewById(R.id.menu_btn);
		mMenuButton.setOnClickListener(this);
		
		enableThradPolicyAboveFroyo();
		initUserInfo();
		
		userInfoWebView = (WebView) rootView.findViewById(R.id.user_info_webview);
		showSignup();
		
		return rootView;

	}

	void initUserInfo() {
		Bundle bundle = getArguments();
		if (bundle != null) {
			mUserInfoURL = bundle.getString(CSRWebConstants.USER_INFO_URL_KEY);
			selectedFragmentPosition = bundle.getInt(CSRConstants.SURKUS_USER_MENU_INDEX, 0);
		}
		
		userName = CSRUtils.getStringSharedPref(getActivity(), CSRConstants.SURKUS_USER_NAME);
		fragmentManager = getActivity().getSupportFragmentManager();
	}
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	void enableThradPolicyAboveFroyo()
	{
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion > android.os.Build.VERSION_CODES.FROYO){
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
	}

	void displaySurkusGoerInfoDialog() {
		try {
			mloadSurkusGoerInfoDialog = new ProgressDialog(getActivity());
			mloadSurkusGoerInfoDialog.setMessage(getActivity().getResources()
					.getString(R.string.loading));
			mloadSurkusGoerInfoDialog.setCancelable(false);
			mloadSurkusGoerInfoDialog.setCanceledOnTouchOutside(false);
			mloadSurkusGoerInfoDialog.setIndeterminate(true);
			mloadSurkusGoerInfoDialog.show();
		} catch (Exception e) {

		}
	}

	void dismissSurkusGoerInfoDialog() {
		if (mloadSurkusGoerInfoDialog != null) {
			mloadSurkusGoerInfoDialog.dismiss();
		}
	}

	private void showSignup() {
		displaySurkusGoerInfoDialog();
		userInfoWebView.getSettings().setJavaScriptEnabled(true);
		userInfoWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		userInfoWebView.loadUrl(mUserInfoURL);
		userInfoWebView.setHorizontalScrollBarEnabled(true);
		userInfoWebView.getSettings().setBuiltInZoomControls(true);
		userInfoWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;

			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);

				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						dismissSurkusGoerInfoDialog();
					}
				});

			}
			
			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				super.onReceivedSslError(view, handler, error);			
				handler.proceed();
			}
		});
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
		
	   }
	}
	
}
