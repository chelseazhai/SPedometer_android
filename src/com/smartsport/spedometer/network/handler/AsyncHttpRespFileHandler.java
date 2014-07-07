/**
 * 
 */
package com.smartsport.spedometer.network.handler;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import com.smartsport.spedometer.network.hoperun.HopeRunHttpReqRespEntity;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name AsyncHttpRespFileHandler
 * @descriptor asynchronous http response file handler
 * @author Ares
 * @version 1.0
 */
public abstract class AsyncHttpRespFileHandler implements IAsyncHttpRespHandler {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			AsyncHttpRespFileHandler.class);

	@Override
	public void onSuccess(int statusCode, byte[] responseBody) {
		LOGGER.info("Asynchronous http response file handler handle successful, status code = "
				+ statusCode + " and response body = " + responseBody);

		// define the request response string body
		String _reqRespStringBody = null;

		try {
			// get request response string body parsed with utf-8 charset
			// encoding
			_reqRespStringBody = new String(responseBody, "UTF-8");

			// get hope run http request response entity
			HopeRunHttpReqRespEntity _hopeRunHttpReqRespEntity = new HopeRunHttpReqRespEntity(
					statusCode, new JSONObject(_reqRespStringBody));

			// get and check status code
			Integer _statusCode = _hopeRunHttpReqRespEntity.getStatusCode();
			if (statusCode == _statusCode) {
				// on success
				onSuccess(statusCode);
			} else {
				// on failed
				onFailure(_statusCode, _hopeRunHttpReqRespEntity.getStatusMsg());
			}
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Generate http request response string body error, exception message = "
					+ e.getMessage());

			e.printStackTrace();
		} catch (JSONException e) {
			LOGGER.error("Get hope run http request response entity json object error, exception message = "
					+ e.getMessage());

			e.printStackTrace();

			// response body unrecognized(not json object)
			onFailure(HttpStatus.SC_INTERNAL_SERVER_ERROR, _reqRespStringBody);
		}
	}

	/**
	 * @title onSuccess
	 * @descriptor asynchronous http request successful
	 * @param statusCode
	 *            : asynchronous http request response status code
	 * @author Ares
	 */
	public abstract void onSuccess(int statusCode);

}
