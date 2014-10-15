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

import com.surkus.android.model.CSRFacebookUser;
import com.surkus.android.model.CSRSurkusApiResponse;
import com.surkus.android.model.CSRSurkusUser;

public class CSRWebServices {

	// Development URLS
	private static String SURKUS_USER_URL = "https://devapi.surkus.com/v1/user/";
	private static String REGISTER_SURKUS_USER_ZIP_CELL_URL = SURKUS_USER_URL
			+ "/zip-cell/";

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
					.setResponseData(CSRWebConstants.NO_REPONSE_FROM_SERVER);
		} else {
			try {
				JSONObject surkusTokenJsonObject = new JSONObject(
						surkusAPIResponse.getResponseData());
				surkusAPIResponse.setResponseData(surkusTokenJsonObject
						.getString(CSRWebConstants.SURKUS_TOKEN_KEY));
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

	public CSRSurkusUser getSurkusUserInfo(String surkusToken) {

		String userInfoJsonReponse = getDataFromSurkus(SURKUS_USER_URL
				+ surkusToken);
		if (userInfoJsonReponse.equalsIgnoreCase("")) {
			return null;
		} else {

			try {

				CSRSurkusUser surkusUser = new CSRSurkusUser();
				JSONObject userInfoJsonObject = new JSONObject(
						userInfoJsonReponse);

				if (userInfoJsonObject.has(CSRWebConstants.FB_ID))
					surkusUser.setFbID(userInfoJsonObject
							.getString(CSRWebConstants.FB_ID));

				if (userInfoJsonObject.has(CSRWebConstants.FB)) {
					CSRFacebookUser facebookUserDataObject = new CSRFacebookUser();
					JSONObject userFacebookInfoJsonObject = new JSONObject(
							userInfoJsonObject.getString(CSRWebConstants.FB));
					// Will process based on the server response
					surkusUser
							.setFacebookUserDataObject(facebookUserDataObject);
				}

				if (userInfoJsonObject.has(CSRWebConstants.ADDRESS))
					surkusUser.setAddress(userInfoJsonObject
							.getString(CSRWebConstants.ADDRESS));

				if (userInfoJsonObject.has(CSRWebConstants.HOME_PHONE))
					surkusUser.setHomePhone(userInfoJsonObject
							.getString(CSRWebConstants.HOME_PHONE));

				if (userInfoJsonObject.has(CSRWebConstants.PROFILE_PICTURE))
					surkusUser.setProfilePicture(userInfoJsonObject
							.getString(CSRWebConstants.PROFILE_PICTURE));

				if (userInfoJsonObject.has(CSRWebConstants.CELL_PHONE))
					surkusUser.setCellPhone(userInfoJsonObject
							.getString(CSRWebConstants.CELL_PHONE));

				if (userInfoJsonObject.has(CSRWebConstants.STATUS))
					surkusUser.setStatus(userInfoJsonObject
							.getString(CSRWebConstants.STATUS));

				return surkusUser;
			} catch (JSONException e) {
				CSRSurkusUser surkusUser = new CSRSurkusUser();
				surkusUser.setApiResponse(userInfoJsonReponse);
				return surkusUser;
			}
		}
	}
}
