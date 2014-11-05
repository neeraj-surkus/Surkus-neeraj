package com.surkus.android.model;

import java.util.ArrayList;

public class CSRRatingQuestion {
	private String mID;
	private String mQuestion;
	private String mV;
	private String mDisplayType;
	private String mGender;
	private ArrayList<CSRRatingOption> ratingOptionList;

	public String getRatingQuestion() {
		return mQuestion;
	}

	public void setRatingQuestion(String ratingTitle) {
		this.mQuestion = ratingTitle;
	}

	public ArrayList<CSRRatingOption> getRatingOptionList() {
		return ratingOptionList;
	}

	public void setRatingOptionList(ArrayList<CSRRatingOption> ratingItemList) {
		this.ratingOptionList = ratingItemList;
	}

	public String getmID() {
		return mID;
	}

	public void setmID(String mID) {
		this.mID = mID;
	}

	public String getmQuestion() {
		return mQuestion;
	}

	public void setmQuestion(String mQuestion) {
		this.mQuestion = mQuestion;
	}

	public String getmV() {
		return mV;
	}

	public void setmV(String mV) {
		this.mV = mV;
	}

	public String getmDisplayType() {
		return mDisplayType;
	}

	public void setmDisplayType(String mDisplayType) {
		this.mDisplayType = mDisplayType;
	}

	public String getmGender() {
		return mGender;
	}

	public void setmGender(String mGender) {
		this.mGender = mGender;
	}
	

}
