/**
 * 
 */
package com.smartsport.spedometer.network.handler;

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
		LOGGER.debug("Asynchronous http response string handler handle successful, status code = "
				+ statusCode + " and response body = " + responseBody);
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
