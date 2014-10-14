package com.surkus.android.component;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.surkus.android.R;
import com.surkus.android.entity.CirclePageIndicator;
import com.surkus.android.model.CSRAboutSurkusAdapter;
import com.surkus.android.utils.CSRConstants;

public class ASRLoginActivity extends FragmentActivity{
	
	CSRAboutSurkusAdapter mAdapter;
	ViewPager mPager;
	CirclePageIndicator mIndicator;
	Button mFacebookLoginBtn;
	
	  @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_login);

	        mAdapter = new CSRAboutSurkusAdapter(getSupportFragmentManager(),CSRConstants.NO_OF_ABOUT_SURKUS_PAGES);
	        mPager = (ViewPager)findViewById(R.id.about_surkus_viewpager);
	        mPager.setAdapter(mAdapter);

	        mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
	        mIndicator.setViewPager(mPager);
	        
	        mFacebookLoginBtn = (Button)findViewById(R.id.facebook_login_btn);
	        mFacebookLoginBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					
					startActivity(new Intent(ASRLoginActivity.this,ASRWhatAreYouActivity.class));
					
				}
			});
	    }

}
