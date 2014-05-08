/**
 * 
 */
package com.smartsport.spedometer.strangersocial.pat;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.amap.api.services.core.LatLonPoint;
import com.smartsport.spedometer.R;
import com.smartsport.spedometer.SSApplication;
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.network.NetworkAdapter;
import com.smartsport.spedometer.network.handler.AsyncHttpRespJSONHandler;
import com.smartsport.spedometer.strangersocial.LocationBean;
import com.smartsport.spedometer.user.UserGender;
import com.smartsport.spedometer.utils.JSONUtils;
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
	private List<UserInfoPatLocationExtBean> nearbyStrangersInfo;

	/**
	 * @title getNearbyStrangers
	 * @descriptor get user nearby all strangers with location info
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param strangerGender
	 *            : need to get stranger gender
	 * @param location
	 *            : user location info
	 * @param executant
	 *            :
	 * @author Ares
	 */
	public void getNearbyStrangers(int userId, String token,
			UserGender strangerGender, LocationBean location,
			ICMConnector executant) {
		// get user nearby strangers info with user id, token, location info and
		// need to get stranger gender
		((StrangerPatNetworkAdapter) NetworkAdapter.getInstance()
				.getWorkerNetworkAdapter(StrangerPatNetworkAdapter.class))
				.getNearbyStrangers(userId, token, strangerGender, location,
						new AsyncHttpRespJSONHandler() {

							@Override
							public void onSuccess(int statusCode,
									JSONArray respJSONArray) {
								LOGGER.info("Get user nearby strangers successful, status code = "
										+ statusCode
										+ " and response json array = "
										+ respJSONArray);

								// check get user nearby strangers info response
								// json array
								if (null != respJSONArray) {
									// check user nearby stranger info list
									if (null == nearbyStrangersInfo) {
										nearbyStrangersInfo = new ArrayList<UserInfoPatLocationExtBean>();
									} else {
										nearbyStrangersInfo.clear();
									}

									// traversal response json array
									for (int i = 0; i < respJSONArray.length(); i++) {
										// get and check user nearby stranger
										// info json object from response json
										// array
										JSONObject _nearbyStrangerInfo = JSONUtils
												.getJSONObjectFromJSONArray(
														respJSONArray, i);
										if (null != _nearbyStrangerInfo) {
											// add user nearby stranger info
											// object to list
											nearbyStrangersInfo
													.add(new UserInfoPatLocationExtBean(
															_nearbyStrangerInfo));
										}
									}

									// get user nearby all strangers successful
									//
								} else {
									LOGGER.error("Get user nearby strangers info response json array is null");
								}
							}

							@Override
							public void onSuccess(int statusCode,
									JSONObject respJSONObject) {
								// nothing to do
							}

							@Override
							public void onFailure(int statusCode,
									String errorMsg) {
								LOGGER.info("Get user nearby strangers failed, status code = "
										+ statusCode
										+ " and error message = "
										+ errorMsg);

								// get user nearby all strangers failed
								//
							}

						});
	}

	/**
	 * @title patStranger
	 * @descriptor pat nearby stranger with pat location info
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param strangerId
	 *            : nearby stranger id
	 * @param patLocation
	 *            : pat location info
	 * @param executant
	 *            :
	 * @author Ares
	 */
	public void patStranger(int userId, String token, final int strangerId,
			LatLonPoint patLocation, ICMConnector executant) {
		// pat nearby stranger with user id, token, pat stranger id and pat
		// location info
		((StrangerPatNetworkAdapter) NetworkAdapter.getInstance()
				.getWorkerNetworkAdapter(StrangerPatNetworkAdapter.class))
				.patStranger(userId, token, strangerId, patLocation,
						new AsyncHttpRespJSONHandler() {

							@Override
							public void onSuccess(int statusCode,
									JSONArray respJSONArray) {
								// nothing to do
							}

							@Override
							public void onSuccess(int statusCode,
									JSONObject respJSONObject) {
								LOGGER.info("Pat nearby stranger successful, status code = "
										+ statusCode
										+ " and response json object = "
										+ respJSONObject);

								// check pat nearby stranger response json
								// object
								if (null != respJSONObject) {
									// get context
									Context _context = SSApplication
											.getContext();

									try {
										// get the count of user pat the
										// stranger and require pat count
										int _respPatCount = Integer.parseInt(JSONUtils
												.getStringFromJSONObject(
														respJSONObject,
														_context.getString(R.string.patStrangerReqResp_patCount)));
										int _respRequirePatCount = Integer.parseInt(JSONUtils
												.getStringFromJSONObject(
														respJSONObject,
														_context.getString(R.string.patStrangerReqResp_requirePatCount)));

										LOGGER.debug("You have pat the stranger, whose user id = "
												+ strangerId
												+ " "
												+ _respPatCount
												+ " times"
												+ ((_respPatCount < _respRequirePatCount) ? " and you need to pat him/her "
														+ (_respRequirePatCount - _respPatCount)
														+ " times again, then"
														: ", now")
												+ "  he/she can see your personal information");

										// pat nearby stranger successful
										//
									} catch (NumberFormatException e) {
										LOGGER.error("Get pat nearby stranger response pat count or require pat count failed, exception message = "
												+ e.getMessage());

										// pat nearby stranger failed
										//

										e.printStackTrace();
									}
								} else {
									LOGGER.error("Pat nearby stranger response json object is null");
								}
							}

							@Override
							public void onFailure(int statusCode,
									String errorMsg) {
								LOGGER.info("Pat nearby stranger failed, status code = "
										+ statusCode
										+ " and error message = "
										+ errorMsg);

								// pat nearby stranger failed
								//
							}

						});
	}

	/**
	 * @title getPattedStrangers
	 * @descriptor get all strangers who patted the user
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param executant
	 *            :
	 * @author Ares
	 */
	public void getPattedStrangers(int userId, String token,
			ICMConnector executant) {
		// get patted strangers with user id and token
		((StrangerPatNetworkAdapter) NetworkAdapter.getInstance()
				.getWorkerNetworkAdapter(StrangerPatNetworkAdapter.class))
				.getPattedStrangers(userId, token,
						new AsyncHttpRespJSONHandler() {

							@Override
							public void onSuccess(int statusCode,
									JSONArray respJSONArray) {
								LOGGER.info("Get patted user strangers successful, status code = "
										+ statusCode
										+ " and response json array = "
										+ respJSONArray);

								// check the strangers who pat the user info
								// response json array
								if (null != respJSONArray) {
									// define the stranger who pat the user info
									// list
									List<UserInfoPatLocationExtBean> _strangersPatUserInfo = new ArrayList<UserInfoPatLocationExtBean>();

									// traversal the json array
									for (int i = 0; i < respJSONArray.length(); i++) {
										// get and check the stranger who pat
										// the user info json object from
										// response json array
										JSONObject _strangerPatUserInfo = JSONUtils
												.getJSONObjectFromJSONArray(
														respJSONArray, i);
										if (null != _strangerPatUserInfo) {
											// add the stranger who pat the user
											// info object to list
											_strangersPatUserInfo
													.add(new UserInfoPatLocationExtBean(
															_strangerPatUserInfo));
										}
									}

									LOGGER.debug("The strangers info list = "
											+ _strangersPatUserInfo
											+ " who pat the user");

									// get all strangers who pat the user
									// successful
									//
								} else {
									LOGGER.error("Get patted user strangers info response json array is null");
								}
							}

							@Override
							public void onSuccess(int statusCode,
									JSONObject respJSONObject) {
								// nothing to do
							}

							@Override
							public void onFailure(int statusCode,
									String errorMsg) {
								LOGGER.info("Get patted user strangers failed, status code = "
										+ statusCode
										+ " and error message = "
										+ errorMsg);

								// get all strangers who pat the user failed
								//
							}

						});
	}

}
