/**
 * 
 */
package com.smartsport.spedometer.user;

import java.util.HashMap;
import java.util.Map;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.network.INetworkAdapter;
import com.smartsport.spedometer.network.handler.AsyncHttpRespJSONHandler;

/**
 * @name UserNetworkAdapter
 * @descriptor user network adapter
 * @author Ares
 * @version 1.0
 */
public class UserNetworkAdapter implements INetworkAdapter {

	/**
	 * @title userLogin
	 * @descriptor user login to remote server
	 * @param loginName
	 *            : user login name
	 * @param loginPwd
	 *            : user login password
	 * @param asyncHttpRespJSONHandler
	 *            : asynchronous http response json handler
	 * @author Ares
	 */
	public void userLogin(String loginName, String loginPwd,
			AsyncHttpRespJSONHandler asyncHttpRespJSONHandler) {
		// generate user login request param
		Map<String, String> _userLoginReqParam = new HashMap<String, String>();

		// set user login name and password to param
		_userLoginReqParam.put(
				NETWORK_ENGINE.getContext().getString(
						R.string.userLoginReqParam_loginName), loginName);
		_userLoginReqParam.put(
				NETWORK_ENGINE.getContext().getString(
						R.string.userLoginReqParam_loginPwd), loginPwd);

		// send user login asynchronous post http request
		NETWORK_ENGINE.postWithAPI(
				NETWORK_ENGINE.getContext().getString(
						R.string.platformUserModule), NETWORK_ENGINE
						.getContext().getString(R.string.userLogin_url),
				_userLoginReqParam, asyncHttpRespJSONHandler);
	}

}
