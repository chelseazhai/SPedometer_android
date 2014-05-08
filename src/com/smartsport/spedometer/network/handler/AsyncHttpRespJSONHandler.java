/**
 * 
 */
package com.smartsport.spedometer.network.handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.SSApplication;
import com.smartsport.spedometer.utils.JSONUtils;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name AsyncHttpRespJSONHandler
 * @descriptor asynchronous http response json handler
 * @author Ares
 * @version 1.0
 */
public abstract class AsyncHttpRespJSONHandler extends
		AsyncHttpRespStringHandler {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			AsyncHttpRespJSONHandler.class);

	@Override
	public void onSuccess(int statusCode, String respString) {
		// parse response body
		Object _responseBodyObject = parseResponseBody(respString);

		// check response body
		if (null != _responseBodyObject) {
			if (_responseBodyObject instanceof JSONObject) {
				// get context
				Context _context = SSApplication.getContext();

				// get response body json object
				JSONObject _respBodyJSONObject = (JSONObject) _responseBodyObject;

				// response body contains user define error
				if (JSONUtils.jsonObjectKeys(_respBodyJSONObject).contains(
						_context.getString(R.string.comReqResp_errorCode))) {
					// asynchronous http response json handle failed
					onFailure(
							JSONUtils.getIntFromJSONObject(
									_respBodyJSONObject,
									_context.getString(R.string.comReqResp_errorCode)),
							JSONUtils.getStringFromJSONObject(
									_respBodyJSONObject,
									_context.getString(R.string.comReqResp_errorMsg)));
				} else {
					// asynchronous http response json handle successful
					onSuccess(statusCode, (JSONObject) _responseBodyObject);
				}
			} else if (_responseBodyObject instanceof JSONArray) {
				// asynchronous http response json handle successful
				onSuccess(statusCode, (JSONArray) _responseBodyObject);
			} else {
				// response body unrecognized(not json object or json array)
				// asynchronous http response json handle failed
				// test by ares
				onFailure(0, "unrecognized response body");
			}
		} else {
			// response body is null
			// asynchronous http response json handle failed
			// test by ares
			onFailure(0, "null response body");
		}
	}

	/**
	 * @title onSuccess
	 * @descriptor asynchronous http request successful
	 * @param statusCode
	 *            : asynchronous http request response status code
	 * @param respJSONObject
	 *            : asynchronous http request response json object data
	 * @author Ares
	 */
	public abstract void onSuccess(int statusCode, JSONObject respJSONObject);

	/**
	 * @title onSuccess
	 * @descriptor asynchronous http request successful
	 * @param statusCode
	 *            : asynchronous http request response status code
	 * @param respJSONArray
	 *            : asynchronous http request response json array data
	 * @author Ares
	 */
	public abstract void onSuccess(int statusCode, JSONArray respJSONArray);

	/**
	 * @title parseResponseBody
	 * @descriptor parse http response body
	 * @param responseBody
	 *            : http response body string
	 * @return http response body object
	 * @author Ares
	 */
	private Object parseResponseBody(String responseBody) {
		// define response body object
		Object _responseBodyObject = null;

		// check http response body string
		if (null != responseBody) {
			try {
				// convert response body to json object
				_responseBodyObject = new JSONObject(responseBody);
			} catch (JSONException jsonObjectParseException) {
				try {
					// convert response body to json array
					_responseBodyObject = new JSONArray(responseBody);
				} catch (JSONException jsonArrayParseException) {
					LOGGER.error("Http response body not recognized, it is not a json object or array");

					jsonObjectParseException.printStackTrace();
					jsonArrayParseException.printStackTrace();
				}
			}
		} else {
			LOGGER.warning("Http response body is null");
		}

		return _responseBodyObject;
	}

}
