package com.surkus.android.model;

import java.io.Serializable;

public class CSRSurkusUser implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String fbID;
	private CSRFacebookUserInfo facebookUserInfo;
	private String profilePicture;
	private CSRSurkusUserAddress address;
	private String homePhone;
	private int v;
	private String cellPhone;
	private String updatedOn;
	private String status;
	private String apiResponse;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFbID() {
		return fbID;
	}

	public void setFbID(String fbID) {
		this.fbID = fbID;
	}

	public CSRFacebookUserInfo getFacebookUserInfo() {
		return facebookUserInfo;
	}

	public void setFacebookUserInfo(CSRFacebookUserInfo facebookUserInfo) {
		this.facebookUserInfo = facebookUserInfo;
	}

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	public CSRSurkusUserAddress getAddress() {
		return address;
	}

	public void setAddress(CSRSurkusUserAddress address) {
		this.address = address;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public int getV() {
		return v;
	}

	public void setV(int v) {
		this.v = v;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getApiResponse() {
		return apiResponse;
	}

	public void setApiResponse(String apiResponse) {
		this.apiResponse = apiResponse;
	}

}
