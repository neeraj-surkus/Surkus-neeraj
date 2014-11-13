package com.surkus.android.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.surkus.android.R;
import com.surkus.android.model.CSRDeviceInformation;
import com.surkus.android.model.CSRRatingQuestion;
import com.surkus.android.model.CSRSurkusApiResponse;
import com.surkus.android.model.CSRSurkusGoer;
import com.surkus.android.model.CSRSurkusGoerSurkusToken;
import com.surkus.android.parser.CSRDeviceInformationParser;
import com.surkus.android.parser.CSRRatingQuestionsParser;
import com.surkus.android.parser.CSRSurkusGoerParser;
import com.surkus.android.parser.CSRSurkusGoerSurkusTokenParser;
import com.surkus.android.utils.CSRUtils;

public class CSRWebServices {

	// Development URLS
	private String SURKUS_USER_URL = "https://devapi.surkus.com/v1/user/";
	
	private String REGISTER_SURKUS_USER_ZIP_CELL_URL = SURKUS_USER_URL+ "contact-info/zip-cell/"; 
	public static  String PAYMENT_URL = "https://devapp.surkus.com/coming-soon/";
	public static String TERMS_AND_CONDITION_URL = "https://www.surkus.com/app-terms-and-conditions";
	public static  String PRIVACY_URL = "https://www.surkus.com/app-privacy-policy";
	public static  String ABOUT_SURKUS_URL = "https://www.surkus.com/";
	public static String SHARE_URL = "https://app.surkus.com";
	public static String SOCIAL_SHARING_URL = "http://www.surkus.com/images/SurkusFBDialog.jpg";
	private String RATING_QUESTIONS_URL = SURKUS_USER_URL+ "category/"; 

	
	private String DEVICES_URL = SURKUS_USER_URL+ "device/"; 
	
	private static CSRWebServices mSingletonRef;

	public static CSRWebServices getSingletonRef() {
		if (mSingletonRef == null)
			mSingletonRef = new CSRWebServices();

		return mSingletonRef;
	}
	
	private CSRSurkusApiResponse getDataFromSurkus(String dataURL) {
		String jsonString = "";
		int statusCode = 0;
		CSRSurkusApiResponse surkusAPIResponse = new CSRSurkusApiResponse();
		
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(dataURL);
			StringBuffer dataBuffer = new StringBuffer();
			String currentDataLine = "";
			HttpResponse serverDataResponse = null;
			serverDataResponse = httpClient.execute(httpGet);		
			statusCode = serverDataResponse.getStatusLine().getStatusCode();			
			HttpEntity entity2 = serverDataResponse.getEntity();
			BufferedReader bufferReader = null;
			bufferReader = new BufferedReader(new InputStreamReader(
					entity2.getContent()));
			while ((currentDataLine = bufferReader.readLine()) != null) {
				dataBuffer.append(currentDataLine);
			}
			jsonString = dataBuffer.toString();
			
			surkusAPIResponse.setSurkusAPIResponse(jsonString);
			surkusAPIResponse.setStatusCode(statusCode);
			
			bufferReader.close();
			
			return surkusAPIResponse;
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		surkusAPIResponse.setSurkusAPIResponse(jsonString);
		surkusAPIResponse.setStatusCode(statusCode);
		
		return surkusAPIResponse;
	}

	private CSRSurkusApiResponse postDataToSurkus(String postData, String Url) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(Url);
		StringBuffer res_builder = new StringBuffer();
		int statusCode = 0;
		String jsonString = "";
		String newdatacheck = "";
		CSRSurkusApiResponse surkusAPIResponse = new CSRSurkusApiResponse();

		try {

			httpPost.addHeader("content-type", "application/json");
			httpPost.setEntity(new StringEntity(postData));
			HttpResponse serverDataResponse = httpClient.execute(httpPost);
			statusCode = serverDataResponse.getStatusLine().getStatusCode();			
			Log.d("Status code", "postDataToSurkus : "+serverDataResponse.getStatusLine().getStatusCode());
			HttpEntity entity = serverDataResponse.getEntity();
			BufferedReader newbrp = null;
			newbrp = new BufferedReader(new InputStreamReader(
					entity.getContent()));

			while ((newdatacheck = newbrp.readLine()) != null) {
				res_builder.append(newdatacheck);
			}

			jsonString = res_builder.toString();

			surkusAPIResponse.setSurkusAPIResponse(jsonString);
			surkusAPIResponse.setStatusCode(statusCode);

			newbrp.close();

			return surkusAPIResponse;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

		surkusAPIResponse.setSurkusAPIResponse(jsonString);
		surkusAPIResponse.setStatusCode(statusCode);

		return surkusAPIResponse;
	}

	public CSRSurkusApiResponse putDataToSurkus(String putData, String url) {
		HttpClient client = new DefaultHttpClient();
		HttpPut put = new HttpPut(url);
		StringBuffer res_builder = new StringBuffer();
		int statusCode = 0;
		String jsonString = "";
		String newdatacheck = "";
		CSRSurkusApiResponse surkusAPIResponse = new CSRSurkusApiResponse();
		try {

			put.addHeader("Content-Type", "application/json");
			put.setEntity(new StringEntity(putData));
			HttpResponse serverDataResponse = client.execute(put);
			statusCode = serverDataResponse.getStatusLine().getStatusCode();
			Log.d("Status code", "putDataToSurkus : "+serverDataResponse.getStatusLine().getStatusCode());
			HttpEntity entity = serverDataResponse.getEntity();
			BufferedReader newbrp = null;
			newbrp = new BufferedReader(new InputStreamReader(
					entity.getContent()));

			while ((newdatacheck = newbrp.readLine()) != null) {
				res_builder.append(newdatacheck);
			}

			jsonString = res_builder.toString();
			surkusAPIResponse.setSurkusAPIResponse(jsonString);
			surkusAPIResponse.setStatusCode(statusCode);

			newbrp.close();

			return surkusAPIResponse;

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		surkusAPIResponse.setSurkusAPIResponse(jsonString);
		surkusAPIResponse.setStatusCode(statusCode);

		return surkusAPIResponse;
	}


	public void deleteSurkusUser(String surkusToken)
	{
		    	DefaultHttpClient httpclient = new DefaultHttpClient();
		        HttpDelete httpdelete = new HttpDelete(SURKUS_USER_URL+ surkusToken);
		 
		        try {  
//		        	httpdelete.setHeader("Accept", "application/json");
//		        	httpdelete.setHeader("Content-type", "application/json");				 
					HttpResponse response = (HttpResponse) httpclient.execute(httpdelete);
					int statusCode = response.getStatusLine().getStatusCode();
					Log.d("Status code", "Delete Surkus user response : "+statusCode);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				}
		        catch (IOException e) {
		        	e.printStackTrace();
				}

	}
	
   public CSRSurkusGoerSurkusToken getSurkusToken(String fbToken,boolean bIsNewSurkusUser) {
		
		CSRSurkusApiResponse surkusAPIResponse;
		CSRSurkusGoerSurkusToken surkusGoerSurkusToken = null;
		
		if(bIsNewSurkusUser)
			surkusAPIResponse = postDataToSurkus("",SURKUS_USER_URL + fbToken);
		else
			surkusAPIResponse = putDataToSurkus("",SURKUS_USER_URL + fbToken);
		
		if (surkusAPIResponse.getSurkusAPIResponse() == null|| surkusAPIResponse.getSurkusAPIResponse().equalsIgnoreCase("")) {
			
			surkusGoerSurkusToken = new CSRSurkusGoerSurkusToken();			
			surkusGoerSurkusToken.setSurkusToken(CSRWebConstants.NO_RESPONSE_FROM_SERVER);
			
			return surkusGoerSurkusToken;
			
		} else {
				
				CSRSurkusGoerSurkusTokenParser surkusTokenParser = new CSRSurkusGoerSurkusTokenParser();
				surkusGoerSurkusToken = surkusTokenParser.parseSurkusGoerSurkusToken(surkusAPIResponse);			  
				return surkusGoerSurkusToken;
		}

	}

	public CSRSurkusApiResponse registerSurkusUser(String surkusToken, String contactInfoJSONData) {
		CSRSurkusApiResponse surkusAPIResponse = postDataToSurkus(contactInfoJSONData, REGISTER_SURKUS_USER_ZIP_CELL_URL+ surkusToken);
		return surkusAPIResponse;

	}
	
    public CSRSurkusGoer getSurkusUserInfo(String surkusToken) {
		
    	CSRSurkusApiResponse surkusAPIResponse = getDataFromSurkus(SURKUS_USER_URL+ surkusToken);;
    	
    	if (surkusAPIResponse.getSurkusAPIResponse() == null|| surkusAPIResponse.getSurkusAPIResponse().equalsIgnoreCase("")) {    		
    		return null;
    	}
    	else
    	{
    		   CSRSurkusGoerParser surkusGoerParser = new CSRSurkusGoerParser();
    		   return surkusGoerParser.parseSurkusGoerInfo(surkusAPIResponse.getSurkusAPIResponse());
    	}

	}
    
	public ArrayList<CSRRatingQuestion> getRatingQuestions(String surkusToken) {
		
		CSRSurkusApiResponse surkusAPIResponse = getDataFromSurkus(RATING_QUESTIONS_URL+ surkusToken+"?displaytype=app-self-register");
		
		if (surkusAPIResponse.getSurkusAPIResponse() == null|| surkusAPIResponse.getSurkusAPIResponse().equalsIgnoreCase("")) {    		
    		return null;
    	} else {			
			CSRRatingQuestionsParser surkusGoerParser = new CSRRatingQuestionsParser();
		    return surkusGoerParser.parseRatingQuestions(surkusAPIResponse.getSurkusAPIResponse());
		}

	}

	/*public CSRSurkusGoer getSurkusUserInfo(String surkusToken) {
		
		String userInfoJsonReponse = getDataFromSurkus(SURKUS_USER_URL+ surkusToken);
		if (userInfoJsonReponse.equalsIgnoreCase("")) {
			return null;
		} else {			
		   CSRSurkusGoerParser surkusGoerParser = new CSRSurkusGoerParser();
		   return surkusGoerParser.parseSurkusGoerInfo(userInfoJsonReponse);
		}
	}*/
	
/*	public ArrayList<CSRRatingQuestion> getRatingQuestions(String surkusToken) {
		
		String userInfoJsonReponse = getDataFromSurkus(RATING_QUESTIONS_URL+ surkusToken+"?displaytype=app-self-register");
		
		if (userInfoJsonReponse.equalsIgnoreCase("")) {
			return null;
		} else {			
			CSRRatingQuestionsParser surkusGoerParser = new CSRRatingQuestionsParser();
		    return surkusGoerParser.parseRatingQuestions(userInfoJsonReponse);
		}

	}*/
	
	public CSRSurkusApiResponse postQuestionOptionRating(String surkusToken, String questionOptionRatingJSONData) {
		CSRSurkusApiResponse surkusAPIResponse = postDataToSurkus(questionOptionRatingJSONData, RATING_QUESTIONS_URL+ surkusToken);
		return surkusAPIResponse;
	}
	
	public CSRSurkusApiResponse registerDevice(Context context, String surkusToken,String token)
	{
		   CSRSurkusApiResponse surkusAPIResponse = null;
		   String deviceID = CSRUtils.getDeviceID(context);
		   String androidOSName = "android";
		   String androidOSVersion = android.os.Build.VERSION.RELEASE;
		   String deviceModel =  android.os.Build.MODEL;;
		   String devicemanufacturer = android.os.Build.MANUFACTURER;	   	   
		   Date currentDeviceTime = new Date();
		   SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		   String formattedTime = dateFormatter.format(currentDeviceTime);
		   
		   try
		   {
		      JSONObject jObject = new JSONObject();
		      jObject.put(CSRWebConstants.TOKEN_KEY, token);
		      jObject.put(CSRWebConstants.DEVICE_ID_KEY, deviceID);
		      jObject.put(CSRWebConstants.OS_NAME_KEY, androidOSName);
		      jObject.put(CSRWebConstants.OS_VERSION_KEY, androidOSVersion);
		      jObject.put(CSRWebConstants.MANUFACTURER_KEY, devicemanufacturer);
		      jObject.put(CSRWebConstants.MODEL_KEY, deviceModel);
		      jObject.put(CSRWebConstants.ADDED_ON_KEY, formattedTime);		   
		      surkusAPIResponse = postDataToSurkus(jObject.toString(), DEVICES_URL+ surkusToken);
		      
		   }catch(JSONException e)
		   {
			   
		   }
		   catch(Exception e)
		   {
			   
		   }
		   
		   return surkusAPIResponse;	   
	}	
	
	public boolean isDeviceRegisterd(Context context, String surkusToken,String token)
	{
		CSRSurkusApiResponse surkusAPIResponse  = getDataFromSurkus(DEVICES_URL+ surkusToken);
		String jsonResponse = surkusAPIResponse.getSurkusAPIResponse();
		
		if (jsonResponse == null|| jsonResponse.equalsIgnoreCase("") || surkusAPIResponse.getStatusCode() == CSRWebConstants.STATUS_CODE_400 || jsonResponse.equalsIgnoreCase(context.getString(R.string.no_device_found))) {    		
    		return false;
    	} 
		else
		{
			
			CSRDeviceInformationParser deviceInformationParser = new CSRDeviceInformationParser();
			ArrayList<CSRDeviceInformation> devices =  deviceInformationParser.parseDevices(jsonResponse);
			
			if(devices == null || devices.size() == 0)
				return false;
			else
			{
				for(int i=0;i<devices.size();i++)
				{
					CSRDeviceInformation device  = devices.get(i);
					
					if(device.getToken().equalsIgnoreCase(token))
						return true;
				}
				
				return false;
			}

		}		
		
	}
}
