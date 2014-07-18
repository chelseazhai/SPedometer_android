/**
 * 
 */
package com.smartsport.spedometer.user;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.SSApplication;
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.network.NetworkAdapter;
import com.smartsport.spedometer.network.handler.AsyncHttpRespJSONHandler;
import com.smartsport.spedometer.utils.JSONUtils;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name UserModel
 * @descriptor user model
 * @author Ares
 * @version 1.0
 */
public class UserModel {

	// logger
	private static final SSLogger LOGGER = new SSLogger(UserModel.class);

	// singleton instance
	private static volatile UserModel _singletonInstance;

	/**
	 * @title UserModel
	 * @descriptor user model private constructor
	 * @author Ares
	 */
	private UserModel() {
		super();

		//
	}

	// get user model singleton instance
	public static UserModel getInstance() {
		if (null == _singletonInstance) {
			synchronized (UserModel.class) {
				if (null == _singletonInstance) {
					_singletonInstance = new UserModel();
				}
			}
		}

		return _singletonInstance;
	}

	/**
	 * @title userLogin
	 * @descriptor user login
	 * @param loginName
	 *            : user login name
	 * @param loginPwd
	 *            : user login password
	 * @param executant
	 *            :
	 * @author Ares
	 */
	public void userLogin(final String loginName, final String loginPwd,
			final ICMConnector executant) {
		// user login with user name and password
		((UserNetworkAdapter) NetworkAdapter.getInstance()
				.getWorkerNetworkAdapter(UserNetworkAdapter.class)).userLogin(
				loginName, loginPwd, new AsyncHttpRespJSONHandler() {

					@Override
					public void onSuccess(int statusCode,
							JSONArray respJSONArray) {
						// nothing to do
					}

					@Override
					public void onSuccess(int statusCode,
							JSONObject respJSONObject) {
						LOGGER.info("User login successful, status code = "
								+ statusCode + " and response json object = "
								+ respJSONObject);

						// get login user and set its login name and password
						UserPedometerExtBean _loginUser = new UserPedometerExtBean();
						_loginUser.setLoginName(loginName);
						_loginUser.setLoginPwd(loginPwd);
						_loginUser.setUserKey("token, test@ares");

						// get context
						Context _context = SSApplication.getContext();

						// get and check logined user info json object
						JSONObject _loginedUserInfo = JSONUtils
								.getJSONObjectFromJSONObject(
										respJSONObject,
										_context.getString(R.string.userLoginReqResp_loginedUserInfo));
						if (null != _loginedUserInfo) {
							// get logined user id, nickname and avatar url then
							// update logined user info
							_loginUser.setUserId(JSONUtils
									.getLongFromJSONObject(
											_loginedUserInfo,
											_context.getString(R.string.userLoginReqResp_loginedUser_userId)));
							_loginUser.setAvatarUrl(JSONUtils
									.getStringFromJSONObject(
											_loginedUserInfo,
											_context.getString(R.string.userLoginReqResp_loginedUser_avatarUrl)));

							// update login user
							UserManager.getInstance().setLoginUser(_loginUser);

							// user login successful
							executant.onSuccess(_loginUser);
						} else {
							LOGGER.error("Parse user login response user info error, logined user info is null");

							// user login failed
							executant
									.onFailure(
											NetworkPedometerReqRespStatusConstant.NULL_RESPBODY,
											"Parse user login response user info error, logined user info is null");
						}
					}

					@Override
					public void onFailure(int statusCode, String errorMsg) {
						LOGGER.info("User login failed, status code = "
								+ statusCode + " and error message = "
								+ errorMsg);

						// user login failed
						executant.onFailure(statusCode, errorMsg);
					}

				});
	}

}
