/**
 * 
 */
package com.smartsport.spedometer.network;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.SSApplication;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name HopeRunHttpReqEntity
 * @descriptor hope run http request entity
 * @author Ares
 * @version 1.0
 */
public class HopeRunHttpReqEntity {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			HopeRunHttpReqEntity.class);

	// context
	private static Context _sContext;

	// header
	// user id, access token, user type, device id, type, os version,
	// application id, version, function id
	private Integer userId;
	private String accessToken;
	private String userType;
	private String deviceId;
	private String deviceType;
	private String osVersion;
	private String appId;
	private String appVersion;
	private String funId;

	// body
	private String body;

	// status
	private Integer statusCode;
	private String statusMsg;

	// hope run http request entity header and parameter map
	private Map<String, String> header;
	private Map<String, String> parameter;

	/**
	 * @title HopeRunHttpReqEntity
	 * @descriptor hope run http request entity constructor
	 * @author Ares
	 */
	public HopeRunHttpReqEntity() {
		super();

		// initialize context, hope run http request entity header and parameter
		_sContext = SSApplication.getContext();
		header = new HashMap<String, String>();
		parameter = new HashMap<String, String>();
	}

	/**
	 * @title HopeRunHttpReqEntity
	 * @descriptor hope run http request entity constructor with user id and
	 *             access token
	 * @param userId
	 *            : user id
	 * @param accessToken
	 *            : user access token
	 * @author Ares
	 */
	public HopeRunHttpReqEntity(Integer userId, String accessToken) {
		this();

		// save user id and access token
		this.userId = userId;
		this.accessToken = accessToken;
	}

	/**
	 * @title HopeRunHttpReqEntity
	 * @descriptor hope run http request entity constructor with response json
	 *             object info
	 * @param respInfo
	 *            : hope run http request response info
	 * @author Ares
	 */
	public HopeRunHttpReqEntity(JSONObject respInfo) {
		this();

		// TODO Auto-generated constructor stub
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;

		// put user id to hope run http request entity header
		header.put(HopeRunHttpReqEntityHeaderKeys.USER_ID.value,
				userId.toString());
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;

		// put user access token to hope run http request entity header
		header.put(HopeRunHttpReqEntityHeaderKeys.ACCESS_TOKEN.value,
				accessToken);
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;

		// put user type to hope run http request entity header
		header.put(HopeRunHttpReqEntityHeaderKeys.USER_TYPE.value, userType);
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;

		// put device id to hope run http request entity header
		header.put(HopeRunHttpReqEntityHeaderKeys.DEVICE_ID.value, deviceId);
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;

		// put device type to hope run http request entity header
		header.put(HopeRunHttpReqEntityHeaderKeys.DEVICE_TYPE.value, deviceType);
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;

		// put os version to hope run http request entity header
		header.put(HopeRunHttpReqEntityHeaderKeys.OS_VERSION.value, osVersion);
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;

		// put application id to hope run http request entity header
		header.put(HopeRunHttpReqEntityHeaderKeys.APP_VERSION.value,
				accessToken);
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;

		// put application version to hope run http request entity header
		header.put(HopeRunHttpReqEntityHeaderKeys.APP_VERSION.value, appVersion);
	}

	public String getFunId() {
		return funId;
	}

	public void setFunId(String funId) {
		this.funId = funId;

		// put function id to hope run http request entity header
		header.put(HopeRunHttpReqEntityHeaderKeys.FUNC_ID.value, funId);
	}

	public String getBody() {
		return body;
	}

	/**
	 * @title setBody
	 * @descriptor set hope run http request entity body
	 * @param body
	 *            : hope run http request entity body json object
	 * @author Ares
	 */
	public void setBody(JSONObject body) {
		// check the body
		if (null != body) {
			this.body = body.toString();
		} else {
			LOGGER.error("Set hope run http request entity body with json object error, body is null");
		}
	}

	/**
	 * @title setBody
	 * @descriptor set hope run http request entity body
	 * @param body
	 *            : hope run http request entity body json array
	 * @author Ares
	 */
	public void setBody(JSONArray body) {
		// check the body
		if (null != body) {
			this.body = body.toString();
		} else {
			LOGGER.error("Set hope run http request entity body with json array error, body is null");
		}
	}

	/**
	 * @title toParamMap
	 * @descriptor get hope run http request parameter
	 * @return hope run http request parameter map
	 * @author Ares
	 */
	public Map<String, String> toParam() {
		Map<String, String> _paramMap = new HashMap<String, String>();

		// initialize hope run http request header and body
		JSONObject _hrHeader = new JSONObject();

		return _paramMap;
	}

	@Override
	public String toString() {
		return "HopeRunHttpReqEntity [userId=" + userId + ", accessToken="
				+ accessToken + ", userType=" + userType + ", deviceId="
				+ deviceId + ", deviceType=" + deviceType + ", osVersion="
				+ osVersion + ", appId=" + appId + ", appVersion=" + appVersion
				+ ", funId=" + funId + " and body=" + body + "]";
	}

	// inner class
	/**
	 * @name HopeRunHttpReqEntityKeys
	 * @descriptor hope run http request entity key enumeration
	 * @author Ares
	 * @version 1.0
	 */
	enum HopeRunHttpReqEntityKeys {

		// header and body
		HEADER(_sContext.getString(R.string.userComReqParam_header)), BODY(
				_sContext.getString(R.string.userComReqParam_body));

		// value
		private String value;

		/**
		 * @title HopeRunHttpReqEntityKeys
		 * @descriptor hope run http request entity keys enumeration private
		 *             constructor
		 * @author Ares
		 */
		private HopeRunHttpReqEntityKeys(String value) {
			// save hope run http request entity keys value
			this.value = value;
		}

		public String getValue() {
			return value;
		}

	}

	/**
	 * @name HopeRunHttpReqEntityHeaderKeys
	 * @descriptor hope run http request entity header key enumeration
	 * @author Ares
	 * @version 1.0
	 */
	enum HopeRunHttpReqEntityHeaderKeys {

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
		 * @title HopeRunHttpReqEntityHeaderKeys
		 * @descriptor hope run http request entity header keys enumeration
		 *             private constructor
		 * @author Ares
		 */
		private HopeRunHttpReqEntityHeaderKeys(String value) {
			// save hope run http request entity header keys value
			this.value = value;
		}

		public String getValue() {
			return value;
		}

	}

}
