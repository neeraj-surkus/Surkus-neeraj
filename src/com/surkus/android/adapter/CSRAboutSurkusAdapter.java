package com.surkus.android.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.surkus.android.R;
import com.surkus.android.fragment.FSRHomeAboutSurkusFragment;

public class CSRAboutSurkusAdapter extends FragmentPagerAdapter {
	
	int[] milAboutSurkusDrawables = {R.drawable.surkus_info_1,R.drawable.surkus_info_2,R.drawable.surkus_info_3,R.drawable.surkus_info_4,R.drawable.surkus_info_6};
	String[] mWelcomeInfo; 

    public CSRAboutSurkusAdapter(Context  inContext,FragmentManager fragmentManager) {
        super(fragmentManager);       
        
        mWelcomeInfo = inContext.getResources().getStringArray(R.array.welcome_info);
        
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return milAboutSurkusDrawables.length;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
    	 return FSRHomeAboutSurkusFragment.newInstance(milAboutSurkusDrawables[position],mWelcomeInfo[position],position);
    }
}