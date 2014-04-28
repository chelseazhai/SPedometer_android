/**
 * 
 */
package com.smartsport.spedometer.strangersocial.pat;

import java.util.List;

import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.user.UserInfoBean;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name StrangerPatModel
 * @descriptor stranger pat model
 * @author Ares
 * @version 1.0
 */
public class StrangerPatModel {

	// logger
	private static final SSLogger LOGGER = new SSLogger(StrangerPatModel.class);

	// user nearby stranger list
	private List<UserInfoBean> nearbyStrangersInfo;

	/**
	 * @title getUserInfo
	 * @descriptor get user info
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param executant
	 *            :
	 * @author Ares
	 */
	public void getUserInfo(int userId, String token, ICMConnector executant) {
		// get user info with user id and token

		//
	}

}
