/**
 * 
 */
package com.smartsport.spedometer.network.hoperun;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.json.JSONObject;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.utils.JSONUtils;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name HopeRunHttpReqRespEntity
 * @descriptor hope run http request response entity
 * @author Ares
 * @version 1.0
 */
public class HopeRunHttpReqRespEntity extends HopeRunHttpEntity {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			HopeRunHttpReqRespEntity.class);

	// intercept time
	private Long interceptTime;

	// status code and message
	private Integer statusCode;
	private String statusMsg;

	// body entity string
	private String bodyString;

	/**
	 * @title HopeRunHttpReqRespEntity
	 * @descriptor hope run http request response entity constructor
	 * @author Ares
	 */
	public HopeRunHttpReqRespEntity() {
		super();

		// initialize hope run http request response intercept time, status code
		// and message
		interceptTime = 0L;
		statusCode = HttpStatus.SC_OK;
		statusMsg = "";
	}

	/**
	 * @title HopeRunHttpReqRespEntity
	 * @descriptor hope run http request response entity constructor with
	 *             request response json object info
	 * @param httpStatusCode
	 *            : hope run http request response status code
	 * @param reqRespInfo
	 *            : hope run http request response info
	 * @author Ares
	 */
	public HopeRunHttpReqRespEntity(int httpStatusCode, JSONObject reqRespInfo) {
		this();

		// check request response json object info
		if (null != reqRespInfo) {
			// set the request response status code
			setStatusCode(statusCode);

			// set the request response header
			setRespHeader(JSONUtils.getJSONObjectFromJSONObject(reqRespInfo,
					HopeRunHttpEntityKeys.HEADER.getValue()));

			// set the request response body
			bodyString = JSONUtils.getStringFromJSONObject(reqRespInfo,
					HopeRunHttpEntityKeys.BODY.getValue());
		} else {
			LOGGER.error("Hope run http request response body is null");
		}
	}

	public Long getInterceptTime() {
		return interceptTime;
	}

	public void setInterceptTime(Long interceptTime) {
		this.interceptTime = interceptTime;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusMsg() {
		return statusMsg;
	}

	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}

	public String getBody() {
		return bodyString;
	}

	@Deprecated
	@Override
	protected void setBody(Object body) {
		// nothing to do
	}

	/**
	 * @title setRespHeader
	 * @descriptor set hope run http request response header with response
	 *             header json object info
	 * @param header
	 *            : hope run http request response header json object info
	 * @return hope run http request response header map
	 * @author Ares
	 */
	public Map<String, String> setRespHeader(JSONObject header) {
		Map<String, String> _respHeaderMap = new HashMap<String, String>();

		// check hope run http request response json object info
		if (null != header) {
			// traversal response json object info and check each key
			for (String _respHeaderKey : JSONUtils.jsonObjectKeys(header)) {
				// get request response header string value with key
				String _headerStringValue = JSONUtils.getStringFromJSONObject(
						header, _respHeaderKey);

				if (HopeRunHttpEntityHeaderKeys.USER_ID.getValue()
						.equalsIgnoreCase(_respHeaderKey)) {
					// user id
					try {
						setUserId(Long.parseLong(_headerStringValue));
					} catch (NumberFormatException e) {
						LOGGER.error("Set request response header user id error, exception message = "
								+ e.getMessage());

						e.printStackTrace();
					}
				} else if (HopeRunHttpEntityHeaderKeys.ACCESS_TOKEN.getValue()
						.equalsIgnoreCase(_respHeaderKey)) {
					// access token
					setAccessToken(_headerStringValue);
				} else if (HopeRunHttpEntityHeaderKeys.USER_TYPE.getValue()
						.equalsIgnoreCase(_respHeaderKey)) {
					// user type
					setUserType(_headerStringValue);
				} else if (HopeRunHttpEntityHeaderKeys.DEVICE_ID.getValue()
						.equalsIgnoreCase(_respHeaderKey)) {
					// device id
					setDeviceId(_headerStringValue);
				} else if (HopeRunHttpEntityHeaderKeys.DEVICE_TYPE.getValue()
						.equalsIgnoreCase(_respHeaderKey)) {
					// device type
					setDeviceType(_headerStringValue);
				} else if (HopeRunHttpEntityHeaderKeys.OS_VERSION.getValue()
						.equalsIgnoreCase(_respHeaderKey)) {
					// os version
					setOsVersion(_headerStringValue);
				} else if (HopeRunHttpEntityHeaderKeys.APP_ID.getValue()
						.equalsIgnoreCase(_respHeaderKey)) {
					// application id
					setAppId(_headerStringValue);
				} else if (HopeRunHttpEntityHeaderKeys.APP_VERSION.getValue()
						.equalsIgnoreCase(_respHeaderKey)) {
					// application version
					setAppVersion(_headerStringValue);
				} else if (HopeRunHttpEntityHeaderKeys.FUNC_ID.getValue()
						.equalsIgnoreCase(_respHeaderKey)) {
					// function id
					setFunId(_headerStringValue);
				} else if (HopeRunHttpReqRespEntityHeaderExtKeys.INTERCEPT_TIME
						.getValue().equalsIgnoreCase(_respHeaderKey)) {
					// intercept time
					// setInterceptTime(JSONUtils.getLongFromJSONObject(header,
					// _respHeaderKey));
				} else if (HopeRunHttpReqRespEntityHeaderExtKeys.STATUS_CODE
						.getValue().equalsIgnoreCase(_respHeaderKey)) {
					// status code
					try {
						// get and check status code
						int _statusCode = Integer.parseInt(_headerStringValue);
						if (Integer
								.parseInt(_sContext
										.getString(R.string.userComReqResp_statusCode_ok)) != _statusCode) {
							setStatusCode(_statusCode);
						}
					} catch (NumberFormatException e) {
						LOGGER.error("Set request response header status code error, exception message = "
								+ e.getMessage());

						e.printStackTrace();
					}
				} else if (HopeRunHttpReqRespEntityHeaderExtKeys.STATUS_MSG
						.getValue().equalsIgnoreCase(_respHeaderKey)) {
					// status message
					setStatusMsg(_headerStringValue);
				}
			}
		} else {
			LOGGER.error("Set hope run http request response header error, response header json object info is null");
		}

		return _respHeaderMap;
	}

	// inner class
	/**
	 * @name HopeRunHttpReqRespEntityHeaderExtKeys
	 * @descriptor hope run http request response entity header extension key
	 *             enumeration
	 * @author Ares
	 * @version 1.0
	 */
	enum HopeRunHttpReqRespEntityHeaderExtKeys {

		// intercept time, status code and message
		INTERCEPT_TIME(_sContext
				.getString(R.string.userComReqResp_interceptTime)), STATUS_CODE(
				_sContext.getString(R.string.userComReqResp_statusCode)), STATUS_MSG(
				_sContext.getString(R.string.userComReqResp_statusMsg));

		// value
		private String value;

		/**
		 * @title HopeRunHttpReqRespEntityHeaderExtKeys
		 * @descriptor hope run http request response entity header extension
		 *             keys enumeration private constructor
		 * @author Ares
		 */
		private HopeRunHttpReqRespEntityHeaderExtKeys(String value) {
			// save hope run http request response entity header extension keys
			// value
			this.value = value;
		}

		public String getValue() {
			return value;
		}

	}

}
