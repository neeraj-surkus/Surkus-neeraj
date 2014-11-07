package com.surkus.android.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.surkus.android.model.CSRFacebookUserInfo;
import com.surkus.android.model.CSRRatingOption;
import com.surkus.android.model.CSRRatingQuestion;
import com.surkus.android.model.CSRSurkusGoer;
import com.surkus.android.model.CSRSurkusUserAddress;
import com.surkus.android.networking.CSRWebConstants;

public class CSRSurkusGoerParser {
	
	public CSRSurkusGoer parseSurkusGoerInfo(String surkusGoerJSONData)
	{
		CSRSurkusGoer surkusUser = new CSRSurkusGoer();
		try {

			JSONObject surkusGoerJSONObject = new JSONObject(surkusGoerJSONData);

			if (surkusGoerJSONObject.has(CSRWebConstants._ID_KEY))
				surkusUser.setId(surkusGoerJSONObject.getString(CSRWebConstants._ID_KEY));
			
			if (surkusGoerJSONObject.has(CSRWebConstants.FB_ID_KEY))
				surkusUser.setFbID(surkusGoerJSONObject.getString(CSRWebConstants.FB_ID_KEY));

			if (surkusGoerJSONObject.has(CSRWebConstants.FB_KEY)) {
				
				CSRFacebookUserInfo facebookUserInfo = new CSRFacebookUserInfo();
				
				JSONObject userFacebookInfoJsonObject = new JSONObject(surkusGoerJSONObject.getString(CSRWebConstants.FB_KEY));
				// Will process based on the server response
				
				if (userFacebookInfoJsonObject.has(CSRWebConstants.ID_KEY)) {
					facebookUserInfo.setId(userFacebookInfoJsonObject.getString(CSRWebConstants.ID_KEY));
				}
				if (userFacebookInfoJsonObject.has(CSRWebConstants.EMAIL_KEY)) {
					facebookUserInfo.setEmail(userFacebookInfoJsonObject.getString(CSRWebConstants.EMAIL_KEY));
				}
				if (userFacebookInfoJsonObject.has(CSRWebConstants.FIRST_NAME_KEY)) {
					facebookUserInfo.setFirstName(userFacebookInfoJsonObject.getString(CSRWebConstants.FIRST_NAME_KEY));
				}
				if (userFacebookInfoJsonObject.has(CSRWebConstants.GENDER_KEY)) {
					facebookUserInfo.setGender(userFacebookInfoJsonObject.getString(CSRWebConstants.GENDER_KEY));
				}
				if (userFacebookInfoJsonObject.has(CSRWebConstants.LAST_NAME_KEY)) {
					facebookUserInfo.setLastName(userFacebookInfoJsonObject.getString(CSRWebConstants.LAST_NAME_KEY));
				}
				if (userFacebookInfoJsonObject.has(CSRWebConstants.LINK_KEY)) {
					facebookUserInfo.setLink(userFacebookInfoJsonObject.getString(CSRWebConstants.LINK_KEY));	
				}
				if (userFacebookInfoJsonObject.has(CSRWebConstants.LOCALE_KEY)) {
					facebookUserInfo.setLocal(userFacebookInfoJsonObject.getString(CSRWebConstants.LOCALE_KEY));
				}
				if (userFacebookInfoJsonObject.has(CSRWebConstants.NAME_KEY)) {
					facebookUserInfo.setName(userFacebookInfoJsonObject.getString(CSRWebConstants.NAME_KEY));
				}
				if (userFacebookInfoJsonObject.has(CSRWebConstants.TIMEZONE_KEY)) {
					facebookUserInfo.setTimeZone(userFacebookInfoJsonObject.getDouble(CSRWebConstants.TIMEZONE_KEY));
				}
				if (userFacebookInfoJsonObject.has(CSRWebConstants.UPDATED_TIME_KEY)) {
					facebookUserInfo.setUpdatedTime(userFacebookInfoJsonObject.getString(CSRWebConstants.UPDATED_TIME_KEY));
				}
				if (userFacebookInfoJsonObject.has(CSRWebConstants.VERIFIED_KEY)) {
					facebookUserInfo.setVerified(userFacebookInfoJsonObject.getBoolean(CSRWebConstants.VERIFIED_KEY));
				}
				surkusUser.setFacebookUserInfo(facebookUserInfo);
			}

			if (surkusGoerJSONObject.has(CSRWebConstants.PROFILE_PICTURE_KEY))
				surkusUser.setProfilePicture(surkusGoerJSONObject.getString(CSRWebConstants.PROFILE_PICTURE_KEY));

			if (surkusGoerJSONObject.has(CSRWebConstants._V_KEY))
				surkusUser.setV(surkusGoerJSONObject.getInt(CSRWebConstants._V_KEY));

			if (surkusGoerJSONObject.has(CSRWebConstants.CELL_PHONE_KEY))
				surkusUser.setCellPhone(surkusGoerJSONObject.getString(CSRWebConstants.CELL_PHONE_KEY));

			if (surkusGoerJSONObject.has(CSRWebConstants.UPDATED_ON_KEY))
				surkusUser.setUpdatedOn(surkusGoerJSONObject
						.getString(CSRWebConstants.UPDATED_ON_KEY));

			if (surkusGoerJSONObject.has(CSRWebConstants.STATUS_KEY))
				surkusUser.setStatus(surkusGoerJSONObject.getString(CSRWebConstants.STATUS_KEY));
			

			if (surkusGoerJSONObject.has(CSRWebConstants.ADDRESS_KEY))
			{
				CSRSurkusUserAddress surkusUserAddress= new CSRSurkusUserAddress();
				
				JSONObject addressJsonObject = new JSONObject(surkusGoerJSONObject.getString(CSRWebConstants.ADDRESS_KEY));
				
				if (addressJsonObject.has(CSRWebConstants.CITY_KEY)) {
					surkusUserAddress.setCity(addressJsonObject.getString(CSRWebConstants.CITY_KEY));
				}
				
				if (addressJsonObject.has(CSRWebConstants.COUNTRY_KEY)) {
					surkusUserAddress.setCountry(addressJsonObject.getString(CSRWebConstants.COUNTRY_KEY));
				}
				
				if (addressJsonObject.has(CSRWebConstants.STATE_KEY)) {
					surkusUserAddress.setState(addressJsonObject.getString(CSRWebConstants.STATE_KEY));
				}
				
				if (addressJsonObject.has(CSRWebConstants.ZIPCODE_KEY)) {
					surkusUserAddress.setZipCode(addressJsonObject.getString(CSRWebConstants.ZIPCODE_KEY));
				}
				
				if (addressJsonObject.has(CSRWebConstants.STATUS_KEY)) {
					surkusUserAddress.setStatus(addressJsonObject.getString(CSRWebConstants.STATUS_KEY));
				}
			     surkusUser.setAddress(surkusUserAddress);
			}
			
			if (surkusGoerJSONObject.has(CSRWebConstants.CATEGORY_KEY))
			{
				if(surkusGoerJSONObject.getJSONArray(CSRWebConstants.CATEGORY_KEY) != null && surkusGoerJSONObject.getJSONArray(CSRWebConstants.CATEGORY_KEY).length() > 0)
				{
					ArrayList<CSRRatingOption> ratedCategories = new ArrayList<CSRRatingOption>();
					
					 JSONArray categoriesArray = surkusGoerJSONObject.getJSONArray(CSRWebConstants.CATEGORY_KEY);
					
				   for(int i=0;i<categoriesArray.length();i++)
					{
					      CSRRatingOption category =  new CSRRatingOption();
						  JSONObject categoryJsonObj = categoriesArray.getJSONObject(i);
						  
						  if (categoryJsonObj.has(CSRWebConstants.NAME_KEY)) {
							  category.setRatingQuestion(categoryJsonObj.getString(CSRWebConstants.NAME_KEY));
						  }
						  
						  if (categoryJsonObj.has(CSRWebConstants.VALUE_KEY)) {
							  category.setCategory(categoryJsonObj.getString(CSRWebConstants.VALUE_KEY));
						  }
						  
						  if (categoryJsonObj.has(CSRWebConstants.RATING_KEY)) {
							  category.setRatingString(categoryJsonObj.getString(CSRWebConstants.RATING_KEY));
						  }
						  
						  if (categoryJsonObj.has(CSRWebConstants._ID_KEY)) {
							  category.setId(categoryJsonObj.getString(CSRWebConstants._ID_KEY));
						  }
						  
						  ratedCategories.add(category);
					}
					
				    surkusUser.setRatedQuestionOptions(ratedCategories);
					surkusUser.setHasCategories(true);
				}	
			}
				//return surkusUser;
		} catch (JSONException e) {
	     	Log.e("Surkus", "Surkus exception: "+e.getMessage());

		}
	   return surkusUser;
	}	
}
