package com.surkus.android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.surkus.android.R;
import com.surkus.android.utils.CSRConstants;

public class FSRHomeAboutSurkusFragment extends Fragment {
    private int mLoginImageDrawbleID;
    String mWelcomeInfo;
    int mCurrentPage;

    // newInstance constructor for creating fragment with arguments
    public static FSRHomeAboutSurkusFragment newInstance(int welcomeSurkusGoerImageID,String welcomeSurkusGoerImageInfo,int currentPage) {
    	FSRHomeAboutSurkusFragment aboutSurkusfragment = new FSRHomeAboutSurkusFragment();
        Bundle args = new Bundle();
        args.putInt(CSRConstants.LOGIN_IMAGE_DRAWABLE, welcomeSurkusGoerImageID);
        args.putString(CSRConstants.LOGIN_WELCOME_INFO, welcomeSurkusGoerImageInfo);        
        args.putInt(CSRConstants.LOGIN_SURKUS_GOER_CURRENT_PAGE, currentPage);   
        aboutSurkusfragment.setArguments(args);
        return aboutSurkusfragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginImageDrawbleID = getArguments().getInt(CSRConstants.LOGIN_IMAGE_DRAWABLE);
        mWelcomeInfo = getArguments().getString(CSRConstants.LOGIN_WELCOME_INFO);        
        mCurrentPage= getArguments().getInt(CSRConstants.LOGIN_SURKUS_GOER_CURRENT_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_surkus, container,false);
        ((ImageView)view.findViewById(R.id.about_surkus_imageview)).setBackgroundResource(mLoginImageDrawbleID);
     
        int textSize = (int) getResources().getDimension(R.dimen.spannable_textsize_15);
      
        SpannableStringBuilder welcomeSurkusgoerInfoSB = new SpannableStringBuilder(mWelcomeInfo);   
        StyleSpan boldStyleSpan = new StyleSpan(android.graphics.Typeface.BOLD); 
             
        if(mCurrentPage == 0)
        	welcomeSurkusgoerInfoSB.setSpan(boldStyleSpan, 104, 111, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        
        if(mCurrentPage == 1)
        	welcomeSurkusgoerInfoSB.setSpan(new AbsoluteSizeSpan(textSize), 48,88, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        
        if(mCurrentPage == 2)
        	welcomeSurkusgoerInfoSB.setSpan(new AbsoluteSizeSpan(textSize), 0,17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        
        if(mCurrentPage == 3)
        {
        	 welcomeSurkusgoerInfoSB.setSpan(new AbsoluteSizeSpan(textSize),13,35, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        	 welcomeSurkusgoerInfoSB.setSpan(boldStyleSpan, 61,70, Spannable.SPAN_INCLUSIVE_INCLUSIVE);        	
        }
        if(mCurrentPage == 4)     
           welcomeSurkusgoerInfoSB.setSpan(new AbsoluteSizeSpan(textSize),25,mWelcomeInfo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);               
        
        ((TextView)view.findViewById(R.id.surkues_goer_info_textview)).setText(welcomeSurkusgoerInfoSB);
         return view;
    }
}