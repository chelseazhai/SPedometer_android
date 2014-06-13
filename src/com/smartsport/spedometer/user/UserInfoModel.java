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

	// singleton instance
	private static volatile UserInfoModel _singletonInstance;

	// user info
	private UserInfoBean userInfo;

	// user friend list
	private List<UserInfoBean> friendsInfo;

	/**
	 * @title UserInfoModel
	 * @descriptor user info model private constructor
	 * @author Ares
	 */
	private UserInfoModel() {
		super();

		// initialize user info and its friend list info
		userInfo = new UserInfoBean();
		friendsInfo = new ArrayList<UserInfoBean>();
	}

	// get user info model singleton instance
	public static UserInfoModel getInstance() {
		if (null == _singletonInstance) {
			synchronized (UserInfoModel.class) {
				if (null == _singletonInstance) {
					_singletonInstance = new UserInfoModel();
				}
			}
		}

		return _singletonInstance;
	}

	public UserInfoBean getUserInfo() {
		return userInfo;
	}

	public List<UserInfoBean> getFriendsInfo() {
		return friendsInfo;
	}

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
	public void getUserInfo(final long userId, String token,
			final ICMConnector executant) {
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

						// parse user info json object
						userInfo.parseUserInfo(respJSONObject);

						// set user info user id
						userInfo.setUserId(userId);

						// get user info successful
						executant.onSuccess(userInfo);
					}

					@Override
					public void onFailure(int statusCode, String errorMsg) {
						LOGGER.info("Get user info failed, status code = "
								+ statusCode + " and error message = "
								+ errorMsg);

						// get user info failed
						executant.onFailure(statusCode, errorMsg);
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
	public void updateUserInfo(final long userId, String token, Integer age,
			UserGender gender, Float height, Float weight,
			final ICMConnector executant) {
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

									// update its user info
									// user id
									userInfo.setUserId(userId);
									// gender
									userInfo.setGender(UserGender.getGender(JSONUtils.getStringFromJSONObject(
											respJSONObject,
											_context.getString(R.string.userInfoReqResp_userGender))));
									// age
									try {
										userInfo.setAge(Integer.parseInt(JSONUtils
												.getStringFromJSONObject(
														respJSONObject,
														_context.getString(R.string.userInfoReqResp_userAge))));
									} catch (NumberFormatException e) {
										LOGGER.error("Get update user age integer from user json info object = "
												+ respJSONObject
												+ " error, exception message = "
												+ e.getMessage());

										e.printStackTrace();
									}
									// height
									try {
										userInfo.setHeight(Float.parseFloat(JSONUtils
												.getStringFromJSONObject(
														respJSONObject,
														_context.getString(R.string.userInfoReqResp_userHeight))));
									} catch (NumberFormatException e) {
										LOGGER.error("Get update user height float from user json info object = "
												+ respJSONObject
												+ " error, exception message = "
												+ e.getMessage());

										e.printStackTrace();
									}
									// weight
									try {
										userInfo.setWeight(Float.parseFloat(JSONUtils
												.getStringFromJSONObject(
														respJSONObject,
														_context.getString(R.string.userInfoReqResp_userWeight))));
									} catch (NumberFormatException e) {
										LOGGER.error("Get update user weight float from user json info object = "
												+ respJSONObject
												+ " error, exception message = "
												+ e.getMessage());

										e.printStackTrace();
									}

									// update user info successful
									executant.onSuccess(userInfo);
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
								executant.onFailure(statusCode, errorMsg);
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
	public void getFriends(long userId, String token,
			final ICMConnector executant) {
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
							// clear user friend info list
							friendsInfo.clear();

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
							executant.onSuccess(friendsInfo);
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
						executant.onFailure(statusCode, errorMsg);
					}

				});
	}

}
