package com.surkus.android.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.surkus.android.model.CSRDeviceInformation;
import com.surkus.android.networking.CSRWebConstants;

public class CSRDeviceInformationParser {
	
	public ArrayList<CSRDeviceInformation> parseDevices(
			String surkusGoerJSONData) {
		ArrayList<CSRDeviceInformation> deviceList = new ArrayList<CSRDeviceInformation>();
		try {
			
			JSONArray devicesJSONArray = new JSONArray(surkusGoerJSONData);
			for (int i = 0; i < devicesJSONArray.length(); i++) {
				JSONObject deviceJsonObj = devicesJSONArray.getJSONObject(i);				
				CSRDeviceInformation device = parserDeviceObject(deviceJsonObj);				
				deviceList.add(device);
			}

		} catch (JSONException e) {
			
			 try
			 {	
				 JSONObject deviceJsonObj = new JSONObject(surkusGoerJSONData);				 
				 CSRDeviceInformation device = parserDeviceObject(deviceJsonObj);				 
				 deviceList.add(device);	 
			 }
			 catch (JSONException e1) {
				 
			 }		
			//Log.e("Surkus", "Devices exception: " + e.getMessage());
		}
		return deviceList;
	}
	
	
 CSRDeviceInformation parserDeviceObject(JSONObject deviceJsonObj) throws JSONException 
 { 
		CSRDeviceInformation device = new CSRDeviceInformation();

		if (deviceJsonObj.has(CSRWebConstants.TOKEN_KEY)) {

			device.setToken(deviceJsonObj.getString(CSRWebConstants.TOKEN_KEY));
		}

		if (deviceJsonObj.has(CSRWebConstants.OS_NAME_KEY)) {

			device.setAndroidOsName(deviceJsonObj.getString(CSRWebConstants.OS_NAME_KEY));
		}
			
		if (deviceJsonObj.has(CSRWebConstants.OS_VERSION_KEY)) {

			device.setAndroidOsVersion(deviceJsonObj.getString(CSRWebConstants.OS_VERSION_KEY));
		}
		
		if (deviceJsonObj.has(CSRWebConstants.MANUFACTURER_KEY)) {

			device.setManufacturer(deviceJsonObj.getString(CSRWebConstants.MANUFACTURER_KEY));
		}
		
		if (deviceJsonObj.has(CSRWebConstants.MODEL_KEY)) {

			device.setModel(deviceJsonObj.getString(CSRWebConstants.MODEL_KEY));
		}
		
		if (deviceJsonObj.has(CSRWebConstants.ADDED_ON_KEY)) {

			device.setAddedOn(deviceJsonObj.getString(CSRWebConstants.ADDED_ON_KEY));
		}	
		
		if (deviceJsonObj.has(CSRWebConstants._ID_KEY)) {

			device.setId(deviceJsonObj.getString(CSRWebConstants._ID_KEY));
		}
		
		if (deviceJsonObj.has(CSRWebConstants.UID_KEY)) {

			device.setUid(deviceJsonObj.getString(CSRWebConstants.UID_KEY));
		}
		
		if (deviceJsonObj.has(CSRWebConstants.DEVICE_ID_KEY)) {

			device.setDeviceID(deviceJsonObj.getString(CSRWebConstants.DEVICE_ID_KEY));
		}
		
		if (deviceJsonObj.has(CSRWebConstants._V_KEY)) {

			device.setV(deviceJsonObj.getInt(CSRWebConstants._V_KEY));
		}
		
		if (deviceJsonObj.has(CSRWebConstants.UPDATED_ON_KEY)) {

			device.setUpdatedOn(deviceJsonObj.getString(CSRWebConstants.UPDATED_ON_KEY));
		}	
		
		return device;
 }
	
	
}
