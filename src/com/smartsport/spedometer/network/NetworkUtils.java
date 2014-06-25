/**
 * 
 */
package com.smartsport.spedometer.network;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.SSApplication;

/**
 * @name NetworkUtils
 * @descriptor network utils
 * @author Ares
 * @version 1.0
 */
public class NetworkUtils {

	/**
	 * @title genUserComReqParam
	 * @descriptor generate user common http request parameter
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @return user common http request parameter
	 * @author Ares
	 */
	public static Map<String, String> genUserComReqParam(long userId,
			String token) {
		// define user common http request parameter
		Map<String, String> _reqParam = new HashMap<String, String>();

		// get context
		Context _context = SSApplication.getContext();

		// put user id and access token in
		_reqParam.put(_context.getString(R.string.userComReqParam_userId),
				String.valueOf(userId));
		_reqParam.put(
				_context.getString(R.string.userComReqParam_userAccessToken),
				token);

		return _reqParam;
	}

}
