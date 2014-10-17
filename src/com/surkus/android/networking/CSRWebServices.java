package com.surkus.android.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.surkus.android.model.CSRFacebookUserInfo;
import com.surkus.android.model.CSRSurkusApiResponse;
import com.surkus.android.model.CSRSurkusGoerUser;
import com.surkus.android.model.CSRSurkusUserAddress;

public class CSRWebServices {

	// Development URLS
	private static String SURKUS_USER_URL = "https://devapi.surkus.com/v1/user/";
	private static String REGISTER_SURKUS_USER_ZIP_CELL_URL = SURKUS_USER_URL+ "contact-info/zip-cell/"; //  /user/contact-info/zip-cell/

	private static CSRWebServices mSingletonRef;

	public static CSRWebServices getSingletonRef() {
		if (mSingletonRef == null)
			mSingletonRef = new CSRWebServices();

		return mSingletonRef;
	}

	private String getDataFromSurkus(String dataURL) {
		String jsonString = "";
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(dataURL);
			StringBuffer dataBuffer = new StringBuffer();
			String currentDataLine = "";
			HttpResponse serverDataResponse = null;
			serverDataResponse = httpClient.execute(httpGet);
			System.out.println(serverDataResponse.getStatusLine());
			HttpEntity entity2 = serverDataResponse.getEntity();
			BufferedReader bufferReader = null;
			bufferReader = new BufferedReader(new InputStreamReader(
					entity2.getContent()));
			while ((currentDataLine = bufferReader.readLine()) != null) {
				dataBuffer.append(currentDataLine);
			}
			jsonString = dataBuffer.toString();
			bufferReader.close();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonString;
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
			Log.d("Status code", statusCode + "");
			HttpEntity entity = serverDataResponse.getEntity();
			BufferedReader newbrp = null;
			newbrp = new BufferedReader(new InputStreamReader(
					entity.getContent()));

			while ((newdatacheck = newbrp.readLine()) != null) {
				res_builder.append(newdatacheck);
			}

			jsonString = res_builder.toString();

			surkusAPIResponse.setResponseData(jsonString);
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

		surkusAPIResponse.setResponseData(jsonString);
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
			Log.d("Status code", statusCode + "");
			HttpEntity entity = serverDataResponse.getEntity();
			BufferedReader newbrp = null;
			newbrp = new BufferedReader(new InputStreamReader(
					entity.getContent()));

			while ((newdatacheck = newbrp.readLine()) != null) {
				res_builder.append(newdatacheck);
			}

			jsonString = res_builder.toString();
			surkusAPIResponse.setResponseData(jsonString);
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
		surkusAPIResponse.setResponseData(jsonString);
		surkusAPIResponse.setStatusCode(statusCode);

		return surkusAPIResponse;
	}

	public CSRSurkusApiResponse getSurkusToken(String fbToken,boolean bIsNewSurkusUser) {
		
		CSRSurkusApiResponse surkusAPIResponse;
		
		if(bIsNewSurkusUser)
			surkusAPIResponse = postDataToSurkus("",SURKUS_USER_URL + fbToken);
		else
			surkusAPIResponse = putDataToSurkus("",SURKUS_USER_URL + fbToken);
		
		if (surkusAPIResponse.getResponseData() == null
				|| surkusAPIResponse.getResponseData().equalsIgnoreCase("")) {
			surkusAPIResponse
					.setResponseData(CSRWebConstants.NO_RESPONSE_FROM_SERVER);
		} else {
			try {
				JSONObject surkusTokenJsonObject = new JSONObject(
						surkusAPIResponse.getResponseData());
			
				if(surkusTokenJsonObject.has(CSRWebConstants.SURKUS_TOKEN__OUATH0_KEY))
					surkusAPIResponse.setResponseData(surkusTokenJsonObject.getString(CSRWebConstants.SURKUS_TOKEN__OUATH0_KEY));
				else
				
				surkusAPIResponse.setResponseData(surkusTokenJsonObject.getString(CSRWebConstants.SURKUS_TOKEN_OAUTH0_KEY));
			} catch (JSONException e) {

			}

		}
		return surkusAPIResponse;
	}

	public int registerSurkusUser(String surkusToken, String contactInfoJSONData) {
		CSRSurkusApiResponse surkusAPIResponse = postDataToSurkus(
				contactInfoJSONData, REGISTER_SURKUS_USER_ZIP_CELL_URL
						+ surkusToken);
		return 0;

	}

	public CSRSurkusGoerUser getSurkusUserInfo(String surkusToken) {

		CSRSurkusGoerUser surkusUser = new CSRSurkusGoerUser();
		
		String userInfoJsonReponse = getDataFromSurkus(SURKUS_USER_URL
				+ surkusToken);
		if (userInfoJsonReponse.equalsIgnoreCase("")) {
			return null;
		} else {

			try {


				JSONObject userInfoJsonObject = new JSONObject(userInfoJsonReponse);


				if (userInfoJsonObject.has(CSRWebConstants._ID_KEY))
					surkusUser.setId(userInfoJsonObject.getString(CSRWebConstants._ID_KEY));
				
				if (userInfoJsonObject.has(CSRWebConstants.FB_ID_KEY))
					surkusUser.setFbID(userInfoJsonObject.getString(CSRWebConstants.FB_ID_KEY));

				if (userInfoJsonObject.has(CSRWebConstants.FB_KEY)) {
					
					CSRFacebookUserInfo facebookUserInfo = new CSRFacebookUserInfo();
					
					JSONObject userFacebookInfoJsonObject = new JSONObject(userInfoJsonObject.getString(CSRWebConstants.FB_KEY));
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

				if (userInfoJsonObject.has(CSRWebConstants.PROFILE_PICTURE_KEY))
					surkusUser.setProfilePicture(userInfoJsonObject.getString(CSRWebConstants.PROFILE_PICTURE_KEY));

				if (userInfoJsonObject.has(CSRWebConstants._V_KEY))
					surkusUser.setV(userInfoJsonObject.getInt(CSRWebConstants._V_KEY));

				if (userInfoJsonObject.has(CSRWebConstants.CELL_PHONE_KEY))
					surkusUser.setCellPhone(userInfoJsonObject.getString(CSRWebConstants.CELL_PHONE_KEY));

				if (userInfoJsonObject.has(CSRWebConstants.UPDATED_ON_KEY))
					surkusUser.setUpdatedOn(userInfoJsonObject
							.getString(CSRWebConstants.UPDATED_ON_KEY));

				if (userInfoJsonObject.has(CSRWebConstants.STATUS_KEY))
					surkusUser.setStatus(userInfoJsonObject.getString(CSRWebConstants.STATUS_KEY));
				

				if (userInfoJsonObject.has(CSRWebConstants.ADDRESS_KEY))
				{
					CSRSurkusUserAddress surkusUserAddress= new CSRSurkusUserAddress();
					
					JSONObject addressJsonObject = new JSONObject(userInfoJsonObject.getString(CSRWebConstants.ADDRESS_KEY));
					
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
					//return surkusUser;
			} catch (JSONException e) {
		     	Log.e("Surkus", "Surkus exception: "+e.getMessage());

			}
		}
		
		return surkusUser;
	}
}
