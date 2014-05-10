/**
 * 
 */
package com.smartsport.spedometer.mvc;

import android.content.Intent;

/**
 * @name ISSBaseActivityResult
 * @descriptor smartsport base activity result interface
 * @author Ares
 * @version 1.0
 */
public interface ISSBaseActivityResult {

	/**
	 * @title onActivityResult
	 * @descriptor application launching
	 * @param resultCode
	 *            : the integer result code returned by the child activity
	 *            through its setResult()
	 * @param data
	 *            : an Intent, which can return result data to the caller
	 *            (various data can be attached to Intent "extras")
	 * @author Ares
	 */
	public void onActivityResult(int resultCode, Intent data);

}
