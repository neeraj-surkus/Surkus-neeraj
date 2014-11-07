package com.surkus.android.listener;

import com.surkus.android.model.CSRRatingOption;

public interface ISRPostRatingForCategoryInterface {

	public void postOrUpdateRatingQuestionWithAnswer(CSRRatingOption ratingOption,String ratingQuestion);
}
