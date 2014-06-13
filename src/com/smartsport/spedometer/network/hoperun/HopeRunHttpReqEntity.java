/**
 * 
 */
package com.smartsport.spedometer.network.hoperun;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Set;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.utils.JSONUtils;
import com.smartsport.spedometer.utils.MapUtils;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name HopeRunHttpReqEntity
 * @descriptor hope run http request entity
 * @author Ares
 * @version 1.0
 */
public class HopeRunHttpReqEntity extends HopeRunHttpEntity {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			HopeRunHttpReqEntity.class);

	/**
	 * @title HopeRunHttpReqEntity
	 * @descriptor hope run http request entity constructor with request
	 *             parameter map
	 * @param reqParam
	 *            : hope run http request parameter map
	 * @author Ares
	 */
	public HopeRunHttpReqEntity(Map<String, String> reqParam) {
		super();

		// set body
		setBody(reqParam);
	}

	@Override
	protected void setBody(Object body) {
		// check body
		if (null != body && body instanceof Map) {
			// convert body to request parameter map
			@SuppressWarnings("unchecked")
			Map<String, String> _reqParamMap = (Map<String, String>) body;

			// get request parameter user id and access token string
			String _userIdParam = _sContext
					.getString(R.string.userComReqParam_userId);
			String _userAccessToken = _sContext
					.getString(R.string.userComReqParam_userAccessToken);

			// get and check the request parameter map keys set
			Set<String> _reqParamMapKeys = _reqParamMap.keySet();
			if (_reqParamMapKeys.contains(_userIdParam)) {
				// set user id
				setUserId(Long.parseLong((String) MapUtils.pop(_reqParamMap,
						_userIdParam)));
			}
			if (_reqParamMapKeys.contains(_userAccessToken)) {
				// set user access token
				setAccessToken((String) MapUtils.pop(_reqParamMap,
						_userAccessToken));
			}

			// set application version to header
			// test by ares
			setAppVersion("1.1.1");

			// set real request parameter
			this.body.putAll(_reqParamMap);
		} else {
			LOGGER.error("Set hope run http request entity body with parameter map object error, body parameter = "
					+ body + " is null or not a map");
		}
	}

	/**
	 * @title genReqStringEntity
	 * @descriptor generate hope run http request json string entity
	 * @return hope run http request json string entity
	 * @author Ares
	 */
	public StringEntity genReqStringEntity() {
		// define json string parameter(charset:utf-8)
		StringEntity _jsonStringEntity = null;

		// generate hope run http request parameter json object
		JSONObject _reqParamJSONObject = new JSONObject();
		JSONUtils
				.putObject2JSONObject(_reqParamJSONObject,
						HopeRunHttpEntityKeys.HEADER.getValue(),
						new JSONObject(header));
		JSONUtils.putObject2JSONObject(_reqParamJSONObject,
				HopeRunHttpEntityKeys.BODY.getValue(), body.isEmpty() ? null
						: new JSONObject(body));

		try {
			_jsonStringEntity = new StringEntity(
					_reqParamJSONObject.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Generate hope run http request json string param error, exception message = "
					+ e.getMessage());

			e.printStackTrace();
		}

		return _jsonStringEntity;
	}

}
