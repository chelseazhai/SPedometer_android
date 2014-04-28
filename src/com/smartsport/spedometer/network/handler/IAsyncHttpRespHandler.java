/**
 * 
 */
package com.smartsport.spedometer.network.handler;

/**
 * @name IAsyncHttpRespHandler
 * @descriptor asynchronous http response handler
 * @author Ares
 * @version 1.0
 */
public interface IAsyncHttpRespHandler {

	/**
	 * @title onSuccess
	 * @descriptor asynchronous http request successful
	 * @param statusCode
	 *            : asynchronous http request response status code
	 * @param responseBody
	 *            : asynchronous http request response body data
	 * @author Ares
	 */
	public void onSuccess(int statusCode, byte[] responseBody);

	/**
	 * @title onFailure
	 * @descriptor asynchronous http request failed
	 * @param statusCode
	 *            : asynchronous http request response status code
	 * @param errorMsg
	 *            : asynchronous http request response error message
	 * @author Ares
	 */
	public void onFailure(int statusCode, String errorMsg);

}
