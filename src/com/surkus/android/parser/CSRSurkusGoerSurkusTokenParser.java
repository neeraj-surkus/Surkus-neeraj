package com.surkus.android.parser;

import org.json.JSONObject;

import android.util.Log;

import com.surkus.android.model.CSRSurkusApiResponse;
import com.surkus.android.model.CSRSurkusGoerSurkusToken;
import com.surkus.android.networking.CSRWebConstants;

public class CSRSurkusGoerSurkusTokenParser {
		
   public CSRSurkusGoerSurkusToken parseSurkusGoerSurkusToken(CSRSurkusApiResponse surkusAPIResponse)
	{
		CSRSurkusGoerSurkusToken surkusToken = new CSRSurkusGoerSurkusToken();
		
		try
		{	
		   surkusToken.setStatusCode(surkusAPIResponse.getStatusCode());
		   JSONObject surkusTokenJsonObject = new JSONObject(surkusAPIResponse.getSurkusAPIResponse());
		
		   if(surkusTokenJsonObject.has(CSRWebConstants.SURKUS_TOKEN__OUATH0_KEY))
			   surkusToken.setSurkusToken(surkusTokenJsonObject.getString(CSRWebConstants.SURKUS_TOKEN__OUATH0_KEY));
		  else
			 if(surkusTokenJsonObject.has(CSRWebConstants.SURKUS_TOKEN_OAUTH0_KEY))
				 surkusToken.setSurkusToken(surkusTokenJsonObject.getString(CSRWebConstants.SURKUS_TOKEN_OAUTH0_KEY));
		  else
			if(surkusTokenJsonObject.has(CSRWebConstants.SURKUS_TOKEN_AUTH0_KEY))
				surkusToken.setSurkusToken(surkusTokenJsonObject.getString(CSRWebConstants.SURKUS_TOKEN_AUTH0_KEY));
		}
		catch(Exception e)
		{
			surkusToken.setSurkusToken(surkusAPIResponse.getSurkusAPIResponse());
			Log.e("Surkus", "Surkus exception: "+e.getMessage());
		}
		return surkusToken;
	}

}
