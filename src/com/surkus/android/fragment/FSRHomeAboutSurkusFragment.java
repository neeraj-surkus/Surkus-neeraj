package com.surkus.android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.surkus.android.R;
import com.surkus.android.utils.CSRConstants;

public class FSRHomeAboutSurkusFragment extends Fragment {
    private int mLoginImageDrawbleID;

    // newInstance constructor for creating fragment with arguments
    public static FSRHomeAboutSurkusFragment newInstance(int page) {
    	FSRHomeAboutSurkusFragment aboutSurkusfragment = new FSRHomeAboutSurkusFragment();
        Bundle args = new Bundle();
        args.putInt(CSRConstants.LOGIN_IMAGE_DRAWABLE, page);
        aboutSurkusfragment.setArguments(args);
        return aboutSurkusfragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginImageDrawbleID = getArguments().getInt(CSRConstants.LOGIN_IMAGE_DRAWABLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_surkus, container,false);
        ((ImageView)view.findViewById(R.id.about_surkus_imageview)).setBackgroundResource(mLoginImageDrawbleID);
        return view;
    }
}