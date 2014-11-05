package com.surkus.android.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.surkus.android.model.CSRRatingOption;
import com.surkus.android.model.CSRRatingQuestion;
import com.surkus.android.networking.CSRWebConstants;

public class CSRRatingQuestionsParser {

	public ArrayList<CSRRatingQuestion> parseRatingQuestions(
			String surkusGoerJSONData) {
		ArrayList<CSRRatingQuestion> ratingQuestions = new ArrayList<CSRRatingQuestion>();
		try {
			JSONArray ratingQuestionsJSONArray = new JSONArray(
					surkusGoerJSONData);

			for (int i = 0; i < ratingQuestionsJSONArray.length(); i++) {
				
				CSRRatingQuestion ratingQuestion = new CSRRatingQuestion();

				JSONObject ratingQuestionJsonObj = ratingQuestionsJSONArray
						.getJSONObject(i);

				if (ratingQuestionJsonObj.has(CSRWebConstants._ID_KEY)) {

					ratingQuestion.setmID(ratingQuestionJsonObj
							.getString(CSRWebConstants._ID_KEY));
				}

				if (ratingQuestionJsonObj.has(CSRWebConstants.NAME_KEY)) {

					ratingQuestion.setmQuestion(ratingQuestionJsonObj
							.getString(CSRWebConstants.NAME_KEY));
				}
				
				
				if (ratingQuestionJsonObj.has(CSRWebConstants.DISPLAY_TYPE_KEY)) {

					ratingQuestion.setmDisplayType(ratingQuestionJsonObj
							.getString(CSRWebConstants.DISPLAY_TYPE_KEY));
				}
				
				if (ratingQuestionJsonObj.has(CSRWebConstants.GENDER_KEY)) {

					ratingQuestion.setmGender(ratingQuestionJsonObj
							.getString(CSRWebConstants.GENDER_KEY));
				}
				
				if (ratingQuestionJsonObj.has(CSRWebConstants.VALUES_KEY)) {

					 ArrayList<CSRRatingOption> ratingOptions = new ArrayList<CSRRatingOption>();
					JSONArray optionsJSONArray = ratingQuestionJsonObj.getJSONArray(CSRWebConstants.VALUES_KEY);
					
					for(int j=0;j<optionsJSONArray.length();j++)
					{
						CSRRatingOption ratingOption = new CSRRatingOption();
						ratingOption.setTitle(optionsJSONArray.getString(j));
						ratingOptions.add(ratingOption);
					}
					
					ratingQuestion.setRatingOptionList(ratingOptions);
				}
				
				ratingQuestions.add(ratingQuestion);
			}

		} catch (JSONException e) {
			Log.e("Surkus", "Rating questions exception: " + e.getMessage());

		}
		return ratingQuestions;
	}

}
