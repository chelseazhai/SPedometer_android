/**
 * 
 */
package com.smartsport.spedometer.mvc;

/**
 * @name ICMConnector
 * @descriptor controller and model connector interface
 * @author Ares
 * @version 1.0
 */
public interface ICMConnector {

	/**
	 * @title onSuccess
	 * @descriptor model get value successful
	 * @param retValue
	 *            : model get value successful return values
	 * @author Ares
	 */
	public void onSuccess(Object... retValue);

	/**
	 * @title onFailure
	 * @descriptor model get value failed
	 * @param errorCode
	 *            : model get value failed, error code
	 * @param errorMsg
	 *            : model get value failed error message
	 * @author Ares
	 */
	public void onFailure(int errorCode, String errorMsg);

}
