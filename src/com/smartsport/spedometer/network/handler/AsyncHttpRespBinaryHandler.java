/**
 * 
 */
package com.smartsport.spedometer.network.handler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name AsyncHttpRespBinaryHandler
 * @descriptor asynchronous http response binary handler
 * @author Ares
 * @version 1.0
 */
public abstract class AsyncHttpRespBinaryHandler implements
		IAsyncHttpRespHandler {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			AsyncHttpRespBinaryHandler.class);

	// content type
	private String contentType;

	@Override
	public void onSuccess(int statusCode, byte[] responseBody) {
		LOGGER.info("AsyncHttpRespBinaryHandler - onSuccess, statusCode = "
				+ statusCode + ", responseBody = " + responseBody
				+ " and content type = " + contentType);

		// check content type and response binary data
		if (null != contentType && null != responseBody) {
			if (contentType.matches("^image/[^gif][a-zA-Z]+$")) {
				// image fie
				// get and check image bitmap
				Bitmap _imgBitmap = BitmapFactory.decodeByteArray(responseBody,
						0, responseBody.length);
				if (null != _imgBitmap) {
					// asynchronous http response binary image handle successful
					onSuccess(statusCode, _imgBitmap);
				} else {
					LOGGER.error("Bitmap factory decode response binary data to image bitmap error");

					// unrecognized binary data response body
					onFailure(
							NetworkPedometerReqRespStatusConstant.UNRECOGNIZED_RESPBODY,
							"Response binary data = " + responseBody
									+ " unrecognized");
				}
			} else {
				// others file
				//

				// asynchronous http response binary file handle
				// successful
				// onSuccess(statusCode, "");
				// test by ares
				// get and check image bitmap
				Bitmap _imgBitmap = BitmapFactory.decodeByteArray(responseBody,
						0, responseBody.length);
				if (null != _imgBitmap) {
					// asynchronous http response binary image handle successful
					onSuccess(statusCode, _imgBitmap);
				} else {
					LOGGER.error("Bitmap factory decode response binary data to image bitmap error");

					// unrecognized binary data response body
					onFailure(
							NetworkPedometerReqRespStatusConstant.UNRECOGNIZED_RESPBODY,
							"Response binary data = " + responseBody
									+ " unrecognized");
				}
			}
		} else {
			LOGGER.error("Response header content type = " + contentType
					+ ", can't recognized response body binary data = "
					+ responseBody);

			// unrecognized binary data response body
			onFailure(
					NetworkPedometerReqRespStatusConstant.UNRECOGNIZED_RESPBODY,
					"Response binary data = " + responseBody + " unrecognized");
		}
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @title onSuccess
	 * @descriptor asynchronous http request successful
	 * @param statusCode
	 *            : asynchronous http request response status code
	 * @param imgBitmap
	 *            : image bitmap
	 * @author Ares
	 */
	public abstract void onSuccess(int statusCode, Bitmap imgBitmap);

	/**
	 * @title onSuccess
	 * @descriptor asynchronous http request successful
	 * @param statusCode
	 *            : asynchronous http request response status code
	 * @param filePath
	 *            : file path
	 * @author Ares
	 */
	public abstract void onSuccess(int statusCode, String filePath);

}
