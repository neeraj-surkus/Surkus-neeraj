package com.surkus.android.model;

public class CSRRatingOption {

	String id; 
	String ratingQuestion;
	private String mCategory;
	private int miRating;
	private String ratingString;


	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getRatingQuestion() {
		return ratingQuestion;
	}

	public void setRatingQuestion(String ratingQuestion) {
		this.ratingQuestion = ratingQuestion;
	}



	public String geCategory() {
		return mCategory;
	}

	public void setCategory(String title) {
		this.mCategory = title;
	}

	public int getRating() {
		return miRating;
	}


	public void setRating(int rating) {
		this.miRating = rating;
	}
	



	public String getRatingString() {
		return ratingString;
	}

	public void setRatingString(String ratingString) {
		this.ratingString = ratingString;
	}
}
