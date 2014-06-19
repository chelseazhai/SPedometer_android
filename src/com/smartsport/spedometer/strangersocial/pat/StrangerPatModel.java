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

	// singleton instance
	private static volatile StrangerPatModel _singletonInstance;

	// user nearby stranger list
	private List<UserInfoPatLocationExtBean> nearbyStrangersInfo;

	/**
	 * @title StrangerPatModel
	 * @descriptor stranger pat model private constructor
	 * @author Ares
	 */
	private StrangerPatModel() {
		super();

		// initialize user nearby strangers info
		nearbyStrangersInfo = new ArrayList<UserInfoPatLocationExtBean>();
	}

	// get stranger pat model singleton instance
	public static StrangerPatModel getInstance() {
		if (null == _singletonInstance) {
			synchronized (StrangerPatModel.class) {
				if (null == _singletonInstance) {
					_singletonInstance = new StrangerPatModel();
				}
			}
		}

		return _singletonInstance;
	}

	public List<UserInfoPatLocationExtBean> getNearbyStrangersInfo() {
		return nearbyStrangersInfo;
	}

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
	public void getNearbyStrangers(long userId, String token,
			UserGender strangerGender, LocationBean location,
			final ICMConnector executant) {
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
									// clear user nearby stranger info list
									nearbyStrangersInfo.clear();

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
									executant.onSuccess(nearbyStrangersInfo);
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
								executant.onFailure(statusCode, errorMsg);
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
	public void patStranger(long userId, String token, final long strangerId,
			LatLonPoint patLocation, final ICMConnector executant) {
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
										executant.onSuccess(_respPatCount,
												_respRequirePatCount);
									} catch (NumberFormatException e) {
										// get exception message
										String _exceptionMsg = e.getMessage();

										LOGGER.error("Get pat nearby stranger response pat count or require pat count failed, exception message = "
												+ _exceptionMsg);

										// pat nearby stranger failed
										// test by ares
										executant.onFailure(11, _exceptionMsg);

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
								executant.onFailure(statusCode, errorMsg);
							}

						});
	}

	/**
	 * @title getPatStrangers
	 * @descriptor get all strangers user patted
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param executant
	 *            :
	 * @author Ares
	 */
	public void getPatStrangers(long userId, String token,
			ICMConnector executant) {
		// get pat strangers with user id and token
		((StrangerPatNetworkAdapter) NetworkAdapter.getInstance()
				.getWorkerNetworkAdapter(StrangerPatNetworkAdapter.class))
				.getPatStrangers(userId, token, new AsyncHttpRespJSONHandler() {

					@Override
					public void onSuccess(int statusCode,
							JSONArray respJSONArray) {
						LOGGER.info("Get user pat strangers successful, status code = "
								+ statusCode
								+ " and response json array = "
								+ respJSONArray);

						// check the user pat strangers info response json array
						if (null != respJSONArray) {
							// define the user pat stranger info list
							List<UserInfoPatLocationExtBean> _userPatStrangersInfo = new ArrayList<UserInfoPatLocationExtBean>();

							// traversal the json array
							for (int i = 0; i < respJSONArray.length(); i++) {
								// get and check the user pat stranger info json
								// object from response json array
								JSONObject _userPatStrangerInfo = JSONUtils
										.getJSONObjectFromJSONArray(
												respJSONArray, i);
								if (null != _userPatStrangerInfo) {
									// add the user pat stranger info object to
									// list
									_userPatStrangersInfo
											.add(new UserInfoPatLocationExtBean(
													_userPatStrangerInfo));
								}
							}

							LOGGER.debug("The user pat strangers info list = "
									+ _userPatStrangersInfo);

							// get the user pat all strangers successful
							//
						} else {
							LOGGER.error("Get the user pat strangers info response json array is null");
						}
					}

					@Override
					public void onSuccess(int statusCode,
							JSONObject respJSONObject) {
						// nothing to do
					}

					@Override
					public void onFailure(int statusCode, String errorMsg) {
						LOGGER.info("Get the user pat strangers failed, status code = "
								+ statusCode
								+ " and error message = "
								+ errorMsg);

						// get the user pat all strangers failed
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
	@Deprecated
	public void getPattedStrangers(long userId, String token,
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
