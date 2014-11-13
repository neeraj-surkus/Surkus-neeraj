package com.surkus.android.utils;

import android.os.AsyncTask;

import com.surkus.android.model.CSRSurkusGoer;
import com.surkus.android.networking.CSRWebServices;

public class CSRDeleteSurkusUserTask extends AsyncTask<String, Integer, CSRSurkusGoer> {

	private String mSurkusToken;
	private CSRWebServices webServiceSingletonObject;

	public CSRDeleteSurkusUserTask(String surkusToken) {
		mSurkusToken = surkusToken;
		webServiceSingletonObject = CSRWebServices.getSingletonRef();
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();

	}

	@Override
	protected CSRSurkusGoer doInBackground(String... args) {
		webServiceSingletonObject.deleteSurkusUser(mSurkusToken);
		return null;
	}

	@Override
	protected void onPostExecute(CSRSurkusGoer surkusGoerUser) {

	}
}