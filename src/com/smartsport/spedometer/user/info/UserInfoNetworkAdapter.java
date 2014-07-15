/**
 * 
 */
package com.smartsport.spedometer.user.info;

import java.util.Map;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.network.INetworkAdapter;
import com.smartsport.spedometer.network.NetworkUtils;
import com.smartsport.spedometer.network.handler.AsyncHttpRespJSONHandler;

/**
 * @name UserInfoNetworkAdapter
 * @descriptor user info network adapter
 * @author Ares
 * @version 1.0
 */
public class UserInfoNetworkAdapter implements INetworkAdapter {

	/**
	 * @title getUserInfo
	 * @descriptor get user info from remote server
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param asyncHttpRespJSONHandler
	 *            : asynchronous http response json handler
	 * @author Ares
	 */
	public void getUserInfo(long userId, String token,
			AsyncHttpRespJSONHandler asyncHttpRespJSONHandler) {
		// send get user info asynchronous post http request
		NETWORK_ENGINE
				.postWithAPI(
						NETWORK_ENGINE.getContext().getString(
								R.string.getUserInfo_url),
						NetworkUtils.genUserComReqParam(userId, token),
						asyncHttpRespJSONHandler);
	}

	/**
	 * @title updateUserInfo
	 * @descriptor update user info to remote server
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param age
	 *            : user need to update age
	 * @param gender
	 *            : user need to update gender
	 * @param height
	 *            : user need to update height
	 * @param weight
	 *            : user need to update weight
	 * @param asyncHttpRespJSONHandler
	 *            : asynchronous http response json handler
	 * @author Ares
	 */
	public void updateUserInfo(long userId, String token, Integer age,
			UserGender gender, Float height, Float weight,
			AsyncHttpRespJSONHandler asyncHttpRespJSONHandler) {
		// get user common request param
		Map<String, String> _updateUserInfoReqParam = NetworkUtils
				.genUserComReqParam(userId, token);

		// check user need to update info and set it to param
		if (null != age) {
			_updateUserInfoReqParam.put(
					NETWORK_ENGINE.getContext().getString(
							R.string.updateUserInfoReqParam_userAge),
					String.valueOf(age));
		}
		if (null != gender) {
			_updateUserInfoReqParam.put(
					NETWORK_ENGINE.getContext().getString(
							R.string.updateUserInfoReqParam_userGender),
					String.valueOf(gender.getValue()));
		}
		if (null != height) {
			_updateUserInfoReqParam.put(
					NETWORK_ENGINE.getContext().getString(
							R.string.updateUserInfoReqParam_userHeight),
					String.valueOf(height));
		}
		if (null != weight) {
			_updateUserInfoReqParam.put(
					NETWORK_ENGINE.getContext().getString(
							R.string.updateUserInfoReqParam_userWeight),
					String.valueOf(weight));
		}

		// send update user info asynchronous post http request
		NETWORK_ENGINE.postWithAPI(
				NETWORK_ENGINE.getContext().getString(
						R.string.updateUserInfo_url), _updateUserInfoReqParam,
				asyncHttpRespJSONHandler);
	}

	/**
	 * @title getFriends
	 * @descriptor get user all friends from remote server
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param asyncHttpRespJSONHandler
	 *            : asynchronous http response json handler
	 * @author Ares
	 */
	public void getFriends(long userId, String token,
			AsyncHttpRespJSONHandler asyncHttpRespJSONHandler) {
		// send get user friend list asynchronous post http request
		NETWORK_ENGINE.postWithAPI(
				NETWORK_ENGINE.getContext().getString(
						R.string.getUserFriends_url),
				NetworkUtils.genUserComReqParam(userId, token),
				asyncHttpRespJSONHandler);
	}

}
