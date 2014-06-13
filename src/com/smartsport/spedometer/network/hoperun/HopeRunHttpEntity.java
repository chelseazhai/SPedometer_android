/**
 * 
 */
package com.smartsport.spedometer.network.hoperun;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.SSApplication;

/**
 * @name HopeRunHttpEntity
 * @descriptor hope run http entity
 * @author Ares
 * @version 1.0
 */
public abstract class HopeRunHttpEntity {

	// context
	protected static Context _sContext;

	// hope run http entity header and body map
	protected Map<String, String> header;
	protected Map<String, String> body;

	// header
	// user id, access token, user type, device id, type, os version,
	// application id, version, function id
	private Long userId;
	private String accessToken;
	private String userType;
	private String deviceId;
	private String deviceType;
	private String osVersion;
	private String appId;
	private String appVersion;
	private String funId;

	/**
	 * @title HopeRunHttpEntity
	 * @descriptor hope run http entity constructor
	 * @author Ares
	 */
	public HopeRunHttpEntity() {
		super();

		// initialize context, hope run http entity header and body
		_sContext = SSApplication.getContext();
		header = new HashMap<String, String>();
		body = new HashMap<String, String>();
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;

		// put user id to hope run http entity header
		header.put(HopeRunHttpEntityHeaderKeys.USER_ID.value, userId.toString());
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;

		// put user access token to hope run http entity header
		header.put(HopeRunHttpEntityHeaderKeys.ACCESS_TOKEN.value, accessToken);
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;

		// put user type to hope run http entity header
		header.put(HopeRunHttpEntityHeaderKeys.USER_TYPE.value, userType);
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;

		// put device id to hope run http entity header
		header.put(HopeRunHttpEntityHeaderKeys.DEVICE_ID.value, deviceId);
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;

		// put device type to hope run http entity header
		header.put(HopeRunHttpEntityHeaderKeys.DEVICE_TYPE.value, deviceType);
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;

		// put os version to hope run http entity header
		header.put(HopeRunHttpEntityHeaderKeys.OS_VERSION.value, osVersion);
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;

		// put application id to hope run http entity header
		header.put(HopeRunHttpEntityHeaderKeys.APP_VERSION.value, accessToken);
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;

		// put application version to hope run http entity header
		header.put(HopeRunHttpEntityHeaderKeys.APP_VERSION.value, appVersion);
	}

	public String getFunId() {
		return funId;
	}

	public void setFunId(String funId) {
		this.funId = funId;

		// put function id to hope run http entity header
		header.put(HopeRunHttpEntityHeaderKeys.FUNC_ID.value, funId);
	}

	/**
	 * @title setBody
	 * @descriptor set hope run http entity body
	 * @param body
	 *            : hope run http entity body object
	 * @author Ares
	 */
	protected abstract void setBody(Object body);

	@Override
	public String toString() {
		return "HopeRunHttpEntity [header userId=" + userId + ", accessToken="
				+ accessToken + ", userType=" + userType + ", deviceId="
				+ deviceId + ", deviceType=" + deviceType + ", osVersion="
				+ osVersion + ", appId=" + appId + ", appVersion=" + appVersion
				+ ", funId=" + funId + " and body=" + body + "]";
	}

	// inner class
	/**
	 * @name HopeRunHttpEntityKeys
	 * @descriptor hope run http entity key enumeration
	 * @author Ares
	 * @version 1.0
	 */
	protected enum HopeRunHttpEntityKeys {

		// header and body
		HEADER(_sContext.getString(R.string.userComReqParam_header)), BODY(
				_sContext.getString(R.string.userComReqParam_body));

		// value
		private String value;

		/**
		 * @title HopeRunHttpEntityKeys
		 * @descriptor hope run http entity keys enumeration private constructor
		 * @author Ares
		 */
		private HopeRunHttpEntityKeys(String value) {
			// save hope run http entity keys value
			this.value = value;
		}

		public String getValue() {
			return value;
		}

	}

	/**
	 * @name HopeRunHttpEntityHeaderKeys
	 * @descriptor hope run http entity header key enumeration
	 * @author Ares
	 * @version 1.0
	 */
	protected enum HopeRunHttpEntityHeaderKeys {

		// user id, access token, user type, device id, device type, os version,
		// application id, application version and function id
		USER_ID(_sContext.getString(R.string.userComReqParam_userId)), ACCESS_TOKEN(
				_sContext.getString(R.string.userComReqParam_userAccessToken)), USER_TYPE(
				_sContext.getString(R.string.userComReqParam_userType)), DEVICE_ID(
				_sContext.getString(R.string.userComReqParam_deviceId)), DEVICE_TYPE(
				_sContext.getString(R.string.userComReqParam_deviceType)), OS_VERSION(
				_sContext.getString(R.string.userComReqParam_osVersion)), APP_ID(
				_sContext.getString(R.string.userComReqParam_appId)), APP_VERSION(
				_sContext.getString(R.string.userComReqParam_appVersion)), FUNC_ID(
				_sContext.getString(R.string.userComReqParam_funcId));

		// value
		private String value;

		/**
		 * @title HopeRunHttpEntityHeaderKeys
		 * @descriptor hope run http entity header keys enumeration private
		 *             constructor
		 * @author Ares
		 */
		private HopeRunHttpEntityHeaderKeys(String value) {
			// save hope run http entity header keys value
			this.value = value;
		}

		public String getValue() {
			return value;
		}

	}

}
