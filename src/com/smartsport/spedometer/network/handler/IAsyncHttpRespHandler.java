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

	// inner class
	/**
	 * @name NetworkPedometerReqRespStatusConstant
	 * @descriptor network pedometer request response status constant
	 * @author Ares
	 * @version 1.0
	 */
	public static class NetworkPedometerReqRespStatusConstant {

		// request timeout and host exception
		public static final int REQUEST_TIMEOUT = 900;
		public static final int REQUEST_HOSTEXCEPTION = 901;

		// response body unrecognized and null
		public static final int UNRECOGNIZED_RESPBODY = 910;
		public static final int NULL_RESPBODY = 911;

	}

}
