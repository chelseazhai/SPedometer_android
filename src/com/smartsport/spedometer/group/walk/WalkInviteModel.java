/**
 * 
 */
package com.smartsport.spedometer.group.walk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.amap.api.services.core.LatLonPoint;
import com.smartsport.spedometer.R;
import com.smartsport.spedometer.SSApplication;
import com.smartsport.spedometer.group.GroupInviteInfoBean;
import com.smartsport.spedometer.group.info.result.UserInfoGroupResultBean;
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.network.NetworkAdapter;
import com.smartsport.spedometer.network.handler.AsyncHttpRespJSONHandler;
import com.smartsport.spedometer.utils.JSONUtils;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name WalkInviteModel
 * @descriptor walk invite model
 * @author Ares
 * @version 1.0
 */
public class WalkInviteModel {

	// logger
	private static final SSLogger LOGGER = new SSLogger(WalkInviteModel.class);

	// singleton instance
	private static volatile WalkInviteModel _singletonInstance;

	// schedule walk group last walking info timestamp map(key: schedule walk
	// group, value: last walking info timestamp)
	private Map<String, Long> lastWalkingInfoTimestampMap;

	/**
	 * @title WalkInviteModel
	 * @descriptor walk invite model private constructor
	 * @author Ares
	 */
	private WalkInviteModel() {
		super();

		// initialize schedule walk group last walking info timestamp map
		lastWalkingInfoTimestampMap = new HashMap<String, Long>();
	}

	// get walk invite model singleton instance
	public static WalkInviteModel getInstance() {
		if (null == _singletonInstance) {
			synchronized (WalkInviteModel.class) {
				if (null == _singletonInstance) {
					_singletonInstance = new WalkInviteModel();
				}
			}
		}

		return _singletonInstance;
	}

	/**
	 * @title inviteWalk
	 * @descriptor invite one of user friends to walk together
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param inviteeId
	 *            : the friend id who been invite for walking together
	 * @param inviteInfo
	 *            : walk invite info
	 * @param executant
	 *            :
	 * @author Ares
	 */
	public void inviteWalk(int userId, String token, final int inviteeId,
			GroupInviteInfoBean inviteInfo, final ICMConnector executant) {
		// invite one of user friends to walk together with his user id and walk
		// invite info
		((WalkInviteNetworkAdapter) NetworkAdapter.getInstance()
				.getWorkerNetworkAdapter(WalkInviteNetworkAdapter.class))
				.inviteWalk(userId, token, inviteeId, inviteInfo,
						new AsyncHttpRespJSONHandler() {

							@Override
							public void onSuccess(int statusCode,
									JSONArray respJSONArray) {
								// nothing to do
							}

							@Override
							public void onSuccess(int statusCode,
									JSONObject respJSONObject) {
								LOGGER.info("Invite one of user friends, whose id = "
										+ inviteeId
										+ " to walk together successful, status code = "
										+ statusCode
										+ " and response json object = "
										+ respJSONObject);

								// check invite one of user friends to walk
								// together response json object
								if (null != respJSONObject) {
									// invite one of user friends to walk
									// together successful, remote server accept
									// the user's walk invite
									executant.onSuccess(JSONUtils
											.getStringFromJSONObject(
													respJSONObject,
													SSApplication
															.getContext()
															.getString(
																	R.string.walkInviteReqResp_serverAcceptTmpGroupId)));
								} else {
									LOGGER.error("Invite one of user friends, whose id = "
											+ inviteeId
											+ " to walk together response json object is null");
								}
							}

							@Override
							public void onFailure(int statusCode,
									String errorMsg) {
								LOGGER.info("Invite one of user friends, whose id = "
										+ inviteeId
										+ " to walk together failed, status code = "
										+ statusCode
										+ " and error message = "
										+ errorMsg);

								// invite one of user friends to walk together
								// failed
								executant.onFailure(statusCode, errorMsg);
							}

						});
	}

	/**
	 * @title respondWalkInvite
	 * @descriptor respond the friend walk together invite
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param tmpGroupId
	 *            : the temp walk group id generate by server
	 * @param isAgreed
	 *            : your decision of the walk invite
	 * @param executant
	 *            :
	 * @author Ares
	 */
	public void respondWalkInvite(int userId, String token,
			final String tmpGroupId, final boolean isAgreed,
			final ICMConnector executant) {
		// respond the friend walk together invite with temp group id and user
		// decision
		((WalkInviteNetworkAdapter) NetworkAdapter.getInstance()
				.getWorkerNetworkAdapter(WalkInviteNetworkAdapter.class))
				.respondWalkInvite(userId, token, tmpGroupId, isAgreed,
						new AsyncHttpRespJSONHandler() {

							@Override
							public void onSuccess(int statusCode,
									JSONArray respJSONArray) {
								// nothing to do
							}

							@Override
							public void onSuccess(int statusCode,
									JSONObject respJSONObject) {
								LOGGER.info("Respond the friend walk together invite successful, temp group id = "
										+ tmpGroupId
										+ ", my decision agreed = "
										+ isAgreed
										+ ", status code = "
										+ statusCode
										+ " and response json object = "
										+ respJSONObject);

								// check respond the friend walk together invite
								// response json object
								if (null != respJSONObject) {
									// get context
									Context _context = SSApplication
											.getContext();

									// respond the friend walk together invite
									// successful with response group id and
									// group invite info
									executant.onSuccess(
											JSONUtils
													.getStringFromJSONObject(
															respJSONObject,
															_context.getString(R.string.respondWalkInviteReqResp_groupId)),
											new GroupInviteInfoBean(
													JSONUtils
															.getJSONObjectFromJSONObject(
																	respJSONObject,
																	_context.getString(R.string.respondWalkInviteReqResp_groupInfo))));
								} else {
									LOGGER.error("Respond the friend walk together invite failed, temp group id = "
											+ tmpGroupId
											+ " and my decision agreed = "
											+ isAgreed
											+ " response json object is null");
								}
							}

							@Override
							public void onFailure(int statusCode,
									String errorMsg) {
								LOGGER.info("Respond the friend walk together invite failed, temp group id = "
										+ tmpGroupId
										+ ", my decision agreed = "
										+ isAgreed
										+ ", status code = "
										+ statusCode
										+ " and error message = " + errorMsg);

								// respond the friend walk together invite
								// failed
								executant.onFailure(statusCode, errorMsg);
							}

						});
	}

	/**
	 * @title startWalking
	 * @descriptor start walking, start step counting
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param groupId
	 *            : the start walking group id
	 * @param executant
	 *            :
	 * @author Ares
	 */
	public void startWalking(int userId, String token, final String groupId,
			final ICMConnector executant) {
		// start one of your schedule walk group with the group id
		((WalkInviteNetworkAdapter) NetworkAdapter.getInstance()
				.getWorkerNetworkAdapter(WalkInviteNetworkAdapter.class))
				.startWalking(userId, token, groupId,
						new AsyncHttpRespJSONHandler() {

							@Override
							public void onSuccess(int statusCode,
									JSONArray respJSONArray) {
								// nothing to do

								// start the schedule walk group successful
								executant.onSuccess((Object) null);
							}

							@Override
							public void onSuccess(int statusCode,
									JSONObject respJSONObject) {
								// nothing to do

								// start the schedule walk group successful
								executant.onSuccess((Object) null);
							}

							@Override
							public void onFailure(int statusCode,
									String errorMsg) {
								LOGGER.info("Start the schedule walk group failed, group id = "
										+ groupId
										+ ", status code = "
										+ statusCode
										+ " and error message = "
										+ errorMsg);

								// start the schedule walk group failed
								executant.onFailure(statusCode, errorMsg);
							}

						});
	}

	/**
	 * @title stopWalking
	 * @descriptor stop walking, step counting stopped
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param groupId
	 *            : the stop walking group id
	 * @param executant
	 *            :
	 * @author Ares
	 */
	public void stopWalking(int userId, String token, final String groupId,
			final ICMConnector executant) {
		// stop one of your walking group with the group id
		((WalkInviteNetworkAdapter) NetworkAdapter.getInstance()
				.getWorkerNetworkAdapter(WalkInviteNetworkAdapter.class))
				.stopWalking(userId, token, groupId,
						new AsyncHttpRespJSONHandler() {

							@Override
							public void onSuccess(int statusCode,
									JSONArray respJSONArray) {
								// nothing to do
							}

							@Override
							public void onSuccess(int statusCode,
									JSONObject respJSONObject) {
								LOGGER.info("Stop the walking group successful, group id = "
										+ groupId
										+ ", status code = "
										+ statusCode
										+ " and response json object = "
										+ respJSONObject);

								// check stop the walking group response json
								// object
								if (null != respJSONObject) {
									// get context
									Context _context = SSApplication
											.getContext();

									// define the walking group start and stop
									// time
									long _walkingGroupStartTime, _walkingGroupStopTime;
									_walkingGroupStartTime = _walkingGroupStopTime = 0L;

									// get the walking group start and stop time
									// start time
									try {
										_walkingGroupStartTime = Long.parseLong(JSONUtils
												.getStringFromJSONObject(
														respJSONObject,
														_context.getString(R.string.walkStopReqResp_walkStartTime)));
									} catch (NumberFormatException e) {
										LOGGER.error("Get the walking group, its id = "
												+ groupId
												+ " start time error, exception message = "
												+ e.getMessage());

										e.printStackTrace();
									}
									// stop time
									try {
										_walkingGroupStopTime = Long.parseLong(JSONUtils
												.getStringFromJSONObject(
														respJSONObject,
														_context.getString(R.string.walkStopReqResp_walkStopTime)));
									} catch (Exception e) {
										LOGGER.error("Get the walking group, its id = "
												+ groupId
												+ " stop time error, exception message = "
												+ e.getMessage());

										e.printStackTrace();
									}

									// define the walking group member result
									// info list
									List<UserInfoGroupResultBean> _walkingGroupMemberResultInfoList = new ArrayList<UserInfoGroupResultBean>();

									// get and traversal walking group members
									// result info
									JSONArray _walkingGroupMemsResultInfo = JSONUtils.getJSONArrayFromJSONObject(
											respJSONObject,
											_context.getString(R.string.walkStopReqResp_walkResult));
									for (int i = 0; i < _walkingGroupMemsResultInfo
											.length(); i++) {
										// get members info with result info in
										// the walking group and add it to list
										_walkingGroupMemberResultInfoList
												.add(new UserInfoGroupResultBean(
														JSONUtils
																.getJSONObjectFromJSONArray(
																		_walkingGroupMemsResultInfo,
																		i)));
									}

									LOGGER.debug("Stop the walking group, group id = "
											+ groupId
											+ ", walk start time = "
											+ _walkingGroupStartTime
											+ ", stop time = "
											+ _walkingGroupStopTime
											+ " and members result info = "
											+ _walkingGroupMemberResultInfoList);

									// stop the walking group successful
									executant.onSuccess(_walkingGroupStartTime,
											_walkingGroupStopTime,
											_walkingGroupMemberResultInfoList);
								} else {
									LOGGER.error("Stop the walking group successful, group id = "
											+ groupId
											+ " response json object is null");
								}
							}

							@Override
							public void onFailure(int statusCode,
									String errorMsg) {
								LOGGER.info("Stop the walking group failed, group id = "
										+ groupId
										+ ", status code = "
										+ statusCode
										+ " and error message = "
										+ errorMsg);

								// stop the walking group failed
								executant.onFailure(statusCode, errorMsg);
							}

						});
	}

	/**
	 * @title publishWalkingInfo
	 * @descriptor publish walking info including walking location, total step
	 *             and distance
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param groupId
	 *            : the walking group id
	 * @param walkingLocation
	 *            : walking location info
	 * @param totalStep
	 *            : walking total step
	 * @param totalDistance
	 *            : walking total distance
	 * @param executant
	 *            :
	 * @author Ares
	 */
	public void publishWalkingInfo(int userId, String token,
			final String groupId, final LatLonPoint walkingLocation,
			final int totalStep, final double totalDistance,
			final ICMConnector executant) {
		// publish user walking info with group id, walking location, total step
		// and distance
		((WalkInviteNetworkAdapter) NetworkAdapter.getInstance()
				.getWorkerNetworkAdapter(WalkInviteNetworkAdapter.class))
				.publishWalkingInfo(userId, token, groupId, walkingLocation,
						totalStep, totalDistance,
						new AsyncHttpRespJSONHandler() {

							@Override
							public void onSuccess(int statusCode,
									JSONArray respJSONArray) {
								// nothing to do

								// publish self walk info successful
								executant.onSuccess((Object) null);
							}

							@Override
							public void onSuccess(int statusCode,
									JSONObject respJSONObject) {
								// nothing to do

								// publish self walk info successful
								executant.onSuccess((Object) null);
							}

							@Override
							public void onFailure(int statusCode,
									String errorMsg) {
								LOGGER.info("Publish user walking info failed, group id = "
										+ groupId
										+ ", walking location = "
										+ walkingLocation
										+ ", total step = "
										+ totalStep
										+ ", total distance = "
										+ totalDistance
										+ ", status code = "
										+ statusCode
										+ " and error message = "
										+ errorMsg);

								// publish user walking info failed
								executant.onFailure(statusCode, errorMsg);
							}

						});
	}

	/**
	 * @title getPartnerWalkingInfo
	 * @descriptor get walk partner walking info including walking location and
	 *             total step
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param groupId
	 *            : the walking group id
	 * @param lastFetchTimestamp
	 *            : last fetched walk partner walking info timestamp
	 * @param executant
	 *            :
	 * @author Ares
	 */
	public void getPartnerWalkingInfo(int userId, String token,
			final String groupId, final long lastFetchTimestamp,
			ICMConnector executant) {
		// get the walking group partner walking info including walking location
		// and total step with group id and last fetch timestamp
		((WalkInviteNetworkAdapter) NetworkAdapter.getInstance()
				.getWorkerNetworkAdapter(WalkInviteNetworkAdapter.class))
				.getPartnerWalkingInfo(userId, token, groupId,
						lastFetchTimestamp, new AsyncHttpRespJSONHandler() {

							@Override
							public void onSuccess(int statusCode,
									JSONArray respJSONArray) {
								// nothing to do
							}

							@Override
							public void onSuccess(int statusCode,
									JSONObject respJSONObject) {
								LOGGER.info("Get walking group partner walking info successful, status code = "
										+ statusCode
										+ " and response json object = "
										+ respJSONObject);

								// check get walking group partner walking info
								// response json object
								if (null != respJSONObject) {
									// get context
									Context _context = SSApplication
											.getContext();

									// define walking group partner walking info
									// latest timestamp, total step and location
									// list
									long _walkingGroupPartnerWalkingInfoLatestTimestamp = 0;
									int _walkingGroupPartnerWalkingToalStep = 0;
									List<LatLonPoint> _walkingGroupPartnerWalkingLocationList = new ArrayList<LatLonPoint>();

									try {
										// get and check response walking group
										// partner walking info latest timestamp
										// and total step
										// walking group partner walking info
										// latest timestamp
										_walkingGroupPartnerWalkingInfoLatestTimestamp = Long.parseLong(JSONUtils
												.getStringFromJSONObject(
														respJSONObject,
														_context.getString(R.string.getPartnerWalkInfoReqResp_latestTimestamp)));

										// add walking group partner walking
										// info latest timestamp
										lastWalkingInfoTimestampMap
												.put(groupId,
														_walkingGroupPartnerWalkingInfoLatestTimestamp);

										// walking group partner walking total
										// step
										_walkingGroupPartnerWalkingToalStep = Integer.parseInt(JSONUtils
												.getStringFromJSONObject(
														respJSONObject,
														_context.getString(R.string.getPartnerWalkInfoReqResp_walkTotalStep)));
									} catch (NumberFormatException e) {
										LOGGER.error("Get walking group partner walking info, group id = "
												+ groupId
												+ ", last fetch timestamp = "
												+ lastFetchTimestamp
												+ " failed, exception message = "
												+ e.getMessage());

										// get the walking group partner walking
										// info failed
										//

										e.printStackTrace();
									}

									// get and check response walking group
									// partner walking location json array
									JSONArray _walkingGroupPartnerWalkingLocationJSONArray = JSONUtils.getJSONArrayFromJSONObject(
											respJSONObject,
											_context.getString(R.string.getPartnerWalkInfoReqResp_walkPath));
									if (null != _walkingGroupPartnerWalkingLocationList) {
										for (int i = 0; i < _walkingGroupPartnerWalkingLocationJSONArray
												.length(); i++) {
											// get and check walking group
											// partner walking location json
											// object
											JSONObject _partnerWalkingLocation = JSONUtils
													.getJSONObjectFromJSONArray(
															_walkingGroupPartnerWalkingLocationJSONArray,
															i);
											if (null != _partnerWalkingLocation) {
												// generate walking location
												// latitude and longitude point
												// and add to list
												_walkingGroupPartnerWalkingLocationList
														.add(new LatLonPoint(
																0.0, 0.0));
											}
										}
									}

									LOGGER.debug("Get walking group partner walking info, group id = "
											+ groupId
											+ ", last fetch timestamp = "
											+ lastFetchTimestamp
											+ ", the partner walking info latest timestamp = "
											+ _walkingGroupPartnerWalkingInfoLatestTimestamp
											+ ", total step = "
											+ _walkingGroupPartnerWalkingToalStep
											+ " and location list = "
											+ _walkingGroupPartnerWalkingLocationList);

									// get walking group partner walking info
									// successful
									//
								} else {
									LOGGER.error("Get walking group partner walking info response json object is null");
								}
							}

							@Override
							public void onFailure(int statusCode,
									String errorMsg) {
								LOGGER.info("Get the walking group partner walking info failed, group id = "
										+ groupId
										+ ", last fetch timestamp = "
										+ lastFetchTimestamp
										+ ", status code = "
										+ statusCode
										+ " and error message = " + errorMsg);

								// get the walking group partner walking info
								// failed
								//
							}

						});
	}

}
