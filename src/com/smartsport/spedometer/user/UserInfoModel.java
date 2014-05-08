/**
 * 
 */
package com.smartsport.spedometer.user;

import java.util.ArrayList;
import java.util.List;

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
 * @name UserInfoModel
 * @descriptor user info model
 * @author Ares
 * @version 1.0
 */
public class UserInfoModel {

	// logger
	private static final SSLogger LOGGER = new SSLogger(UserInfoModel.class);

	// user info
	private UserInfoBean userInfo;

	// user friend list
	private List<UserInfoBean> friendsInfo;

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
	public void getUserInfo(final int userId, String token,
			ICMConnector executant) {
		// get user info with user id and token
		((UserInfoNetworkAdapter) NetworkAdapter.getInstance()
				.getWorkerNetworkAdapter(UserInfoNetworkAdapter.class))
				.getUserInfo(userId, token, new AsyncHttpRespJSONHandler() {

					@Override
					public void onSuccess(int statusCode,
							JSONArray respJSONArray) {
						// nothing to do
					}

					@Override
					public void onSuccess(int statusCode,
							JSONObject respJSONObject) {
						LOGGER.info("Get user info successful, status code = "
								+ statusCode + " and response json object = "
								+ respJSONObject);

						// check user info
						if (null == userInfo) {
							// initialize user info object
							userInfo = new UserInfoBean(respJSONObject);
						} else {
							// parse user info json object
							userInfo = userInfo.parseUserInfo(respJSONObject);
						}

						// set user info user id
						userInfo.setUserId(userId);

						// get user info successful
						//
					}

					@Override
					public void onFailure(int statusCode, String errorMsg) {
						LOGGER.info("Get user info failed, status code = "
								+ statusCode + " and error message = "
								+ errorMsg);

						// get user info failed
						//
					}

				});
	}

	/**
	 * @title updateUserInfo
	 * @descriptor update user info
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
	 * @param executant
	 *            :
	 * @author Ares
	 */
	public void updateUserInfo(int userId, String token, Integer age,
			UserGender gender, Float height, Float weight,
			ICMConnector executant) {
		// update user info with user id, token and need to update user
		// info(perhaps including user age, gender, height and weight)
		((UserInfoNetworkAdapter) NetworkAdapter.getInstance()
				.getWorkerNetworkAdapter(UserInfoNetworkAdapter.class))
				.updateUserInfo(userId, token, age, gender, height, weight,
						new AsyncHttpRespJSONHandler() {

							@Override
							public void onSuccess(int statusCode,
									JSONArray respJSONArray) {
								// nothing to do
							}

							@Override
							public void onSuccess(int statusCode,
									JSONObject respJSONObject) {
								LOGGER.info("Update user info successful, status code = "
										+ statusCode
										+ " and response json object = "
										+ respJSONObject);

								// check update user info response json object
								if (null != respJSONObject) {
									// get context
									Context _context = SSApplication
											.getContext();

									try {
										// get and check response result code
										int _respResultCode = Integer.parseInt(JSONUtils
												.getStringFromJSONObject(
														respJSONObject,
														_context.getString(R.string.updateUserInfoReqResp_resultCode)));
										if (Integer.parseInt(_context
												.getString(R.string.updateUserInfoReqResp_updateSuccessful)) == _respResultCode) {
											// update user info successful
											//
										} else if (Integer.parseInt(_context
												.getString(R.string.updateUserInfoReqResp_updateFailed)) == _respResultCode) {
											// update user info failed
											//
										} else {
											LOGGER.error("Update user info failed, response result code = "
													+ _respResultCode
													+ " unrecognized");

											// update user info failed
											//
										}
									} catch (NumberFormatException e) {
										LOGGER.error("Get update user info response result code failed, exception message = "
												+ e.getMessage());

										// update user info failed
										//

										e.printStackTrace();
									}
								} else {
									LOGGER.error("Update user info response json object is null");
								}
							}

							@Override
							public void onFailure(int statusCode,
									String errorMsg) {
								LOGGER.info("Update user info failed, status code = "
										+ statusCode
										+ " and error message = "
										+ errorMsg);

								// update user info failed
								//
							}

						});
	}

	/**
	 * @title getFriends
	 * @descriptor get user all friends
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param executant
	 *            :
	 * @author Ares
	 */
	public void getFriends(int userId, String token, ICMConnector executant) {
		// get user all friends with user id and token
		((UserInfoNetworkAdapter) NetworkAdapter.getInstance()
				.getWorkerNetworkAdapter(UserInfoNetworkAdapter.class))
				.getFriends(userId, token, new AsyncHttpRespJSONHandler() {

					@Override
					public void onSuccess(int statusCode,
							JSONArray respJSONArray) {
						LOGGER.info("Get user friends successful, status code = "
								+ statusCode
								+ " and response json array = "
								+ respJSONArray);

						// check get user friends info response json array
						if (null != respJSONArray) {
							// check user friend info list
							if (null == friendsInfo) {
								friendsInfo = new ArrayList<UserInfoBean>();
							} else {
								friendsInfo.clear();
							}

							// traversal response json array
							for (int i = 0; i < respJSONArray.length(); i++) {
								// get and check user friend info json object
								// from response json array
								JSONObject _friendInfo = JSONUtils
										.getJSONObjectFromJSONArray(
												respJSONArray, i);
								if (null != _friendInfo) {
									// add user friend info object to list
									friendsInfo.add(new UserInfoBean(
											_friendInfo));
								}
							}

							// get user all friends successful
							//
						} else {
							LOGGER.error("Get user friends info response json array is null");
						}
					}

					@Override
					public void onSuccess(int statusCode,
							JSONObject respJSONObject) {
						// nothing to do
					}

					@Override
					public void onFailure(int statusCode, String errorMsg) {
						LOGGER.info("Get user friends failed, status code = "
								+ statusCode + " and error message = "
								+ errorMsg);

						// get user all friends failed
						//
					}

				});
	}

}
