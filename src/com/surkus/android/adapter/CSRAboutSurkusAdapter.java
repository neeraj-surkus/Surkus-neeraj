package com.surkus.android.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.surkus.android.R;
import com.surkus.android.fragment.FSRHomeAboutSurkusFragment;

public class CSRAboutSurkusAdapter extends FragmentPagerAdapter {

	int[] milAboutSurkusDrawables ={R.drawable.login_1,R.drawable.login_2,R.drawable.login_3,R.drawable.login_4,R.drawable.login_5};

    public CSRAboutSurkusAdapter(FragmentManager fragmentManager, int inNoOfAboutSurkusPages) {
        super(fragmentManager);       
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return milAboutSurkusDrawables.length;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
    	 return FSRHomeAboutSurkusFragment.newInstance(milAboutSurkusDrawables[position]);
    }
}