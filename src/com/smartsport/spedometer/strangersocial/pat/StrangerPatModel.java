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

	// patted user stranger list
	private List<UserInfoPatLocationExtBean> pattedStrangersInfo;

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
			UserGender strangerGender, LatLonPoint location,
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
										+ " and response json Array = "
										+ respJSONArray);

								// check get user nearby strangers info response
								// json array
								if (null != respJSONArray) {
									// check user nearby stranger info list
									if (null == nearbyStrangersInfo) {
										nearbyStrangersInfo = new ArrayList<UserInfoPatLocationExtBean>();
									}

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
	 * @descriptor pat nearby stranger with location info
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
		// pat nearby stranger with user id, token, patted stranger id and pat
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
										// get the count of user patted the
										// stranger and require pat count
										int _respPattedCount = Integer.parseInt(JSONUtils
												.getStringFromJSONObject(
														respJSONObject,
														_context.getString(R.string.patStrangerReqResp_patedCount)));
										int _respRequirePatCount = Integer.parseInt(JSONUtils
												.getStringFromJSONObject(
														respJSONObject,
														_context.getString(R.string.patStrangerReqResp_requirePatCount)));

										LOGGER.debug("You have patted the stranger = "
												+ strangerId
												+ _respPattedCount
												+ " times"
												+ ((_respPattedCount < _respRequirePatCount) ? " and you need to pat him/her "
														+ (_respRequirePatCount - _respPattedCount)
														+ " times again, then he/she can see your personal information"
														: ""));

										// pat nearby stranger successful
										//
									} catch (NumberFormatException e) {
										LOGGER.error("Get pat nearby stranger response patted count or require pat count failed, exception message = "
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
										+ " and response json Array = "
										+ respJSONArray);

								// check patted user strangers info response
								// json array
								if (null != respJSONArray) {
									// check patted user stranger info list
									if (null == pattedStrangersInfo) {
										pattedStrangersInfo = new ArrayList<UserInfoPatLocationExtBean>();
									}

									for (int i = 0; i < respJSONArray.length(); i++) {
										// get and check patted user stranger
										// info json object from response json
										// array
										JSONObject _pattedStrangerInfo = JSONUtils
												.getJSONObjectFromJSONArray(
														respJSONArray, i);
										if (null != _pattedStrangerInfo) {
											// add patted user stranger info
											// object to list
											pattedStrangersInfo
													.add(new UserInfoPatLocationExtBean(
															_pattedStrangerInfo));
										}
									}

									// get patted user all strangers successful
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

								// get patted user all strangers failed
								//
							}

						});
	}

}
