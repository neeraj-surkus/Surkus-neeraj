package com.surkus.android.model;

public class CSRDeviceInformation {
	
	String id;
	String uid;
	String deviceID;
	String mToken;
	String mAndroidOsName;
	String mAndroidOsVersion;
	String mManufacturer;
	String mModel;
	int v;
	String updatedOn;
	String mAddedOn;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public int getV() {
		return v;
	}
	public void setV(int v) {
		this.v = v;
	}
	public String getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}
	public String getToken() {
		return mToken;
	}
	public void setToken(String token) {
		this.mToken = token;
	}
	public String getAndroidOsName() {
		return mAndroidOsName;
	}
	public void setAndroidOsName(String androidOsName) {
		this.mAndroidOsName = androidOsName;
	}
	public String getAndroidOsVersion() {
		return mAndroidOsVersion;
	}
	public void setAndroidOsVersion(String androidOsVersion) {
		this.mAndroidOsVersion = androidOsVersion;
	}
	public String getManufacturer() {
		return mManufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.mManufacturer = manufacturer;
	}
	public String getModel() {
		return mModel;
	}
	public void setModel(String model) {
		this.mModel = model;
	}
	public String getAddedOn() {
		return mAddedOn;
	}
	public void setAddedOn(String addedOn) {
		this.mAddedOn = addedOn;
	}
}
