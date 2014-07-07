/**
 * 
 */
package com.smartsport.spedometer.network.handler;

import java.io.UnsupportedEncodingException;

import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name AsyncHttpRespStringHandler
 * @descriptor asynchronous http response string handler
 * @author Ares
 * @version 1.0
 */
public abstract class AsyncHttpRespStringHandler implements
		IAsyncHttpRespHandler {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			AsyncHttpRespStringHandler.class);

	@Override
	public void onSuccess(int statusCode, byte[] responseBody) {
		LOGGER.info("Asynchronous http response string handler handle successful, status code = "
				+ statusCode + " and response body = " + responseBody);

		// on success with response string parse with utf-8 charset encoding
		try {
			onSuccess(statusCode, new String(responseBody, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Generate http request response string body error, exception message = "
					+ e.getMessage());

			e.printStackTrace();
		}
	}

	/**
	 * @title onSuccess
	 * @descriptor asynchronous http request successful
	 * @param statusCode
	 *            : asynchronous http request response status code
	 * @param respString
	 *            : asynchronous http request response string data
	 * @author Ares
	 */
	public abstract void onSuccess(int statusCode, String respString);

}
