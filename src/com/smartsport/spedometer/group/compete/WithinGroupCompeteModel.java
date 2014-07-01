/**
 * 
 */
package com.smartsport.spedometer.group.compete;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.SSApplication;
import com.smartsport.spedometer.group.GroupInviteInfoBean;
import com.smartsport.spedometer.group.info.member.MemberWalkVelocityBean;
import com.smartsport.spedometer.group.info.result.UserInfoGroupResultBean;
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.network.NetworkAdapter;
import com.smartsport.spedometer.network.handler.AsyncHttpRespJSONHandler;
import com.smartsport.spedometer.utils.JSONUtils;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name WithinGroupCompeteModel
 * @descriptor within group compete model
 * @author Ares
 * @version 1.0
 */
public class WithinGroupCompeteModel {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			WithinGroupCompeteModel.class);

	// singleton instance
	private static volatile WithinGroupCompeteModel _singletonInstance;

	/**
	 * @title WithinGroupCompeteModel
	 * @descriptor within group compete model private constructor
	 * @author Ares
	 */
	private WithinGroupCompeteModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	// get within group compete model singleton instance
	public static WithinGroupCompeteModel getInstance() {
		if (null == _singletonInstance) {
			synchronized (WithinGroupCompeteModel.class) {
				if (null == _singletonInstance) {
					_singletonInstance = new WithinGroupCompeteModel();
				}
			}
		}

		return _singletonInstance;
	}

	/**
	 * @title inviteWithinGroupCompete
	 * @descriptor invite more than one of user friends to walk compete
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param inviteesId
	 *            : the friends id who been invite for walking compete
	 * @param inviteInfo
	 *            : walk compete invite info
	 * @param executant
	 *            :
	 * @author Ares
	 */
	public void inviteWithinGroupCompete(long userId, String token,
			final List<Long> inviteesId, GroupInviteInfoBean inviteInfo,
			final ICMConnector executant) {
		// invite more than one of user friends to walk compete with their user
		// id list and within group walk compete
		((WithinGroupCompeteNetworkAdapter) NetworkAdapter
				.getInstance()
				.getWorkerNetworkAdapter(WithinGroupCompeteNetworkAdapter.class))
				.inviteWithinGroupCompete(userId, token, inviteesId,
						inviteInfo, new AsyncHttpRespJSONHandler() {

							@Override
							public void onSuccess(int statusCode,
									JSONArray respJSONArray) {
								// nothing to do
							}

							@Override
							public void onSuccess(int statusCode,
									JSONObject respJSONObject) {
								LOGGER.info("Invite more than one of user friends, their user id = "
										+ inviteesId
										+ " to walk compete successful, status code = "
										+ statusCode
										+ " and response json object = "
										+ respJSONObject);

								// check invite more than one of user friends to
								// walk compete response json object
								if (null != respJSONObject) {
									// get context
									Context _context = SSApplication
											.getContext();

									try {
										// get and check response group id,
										// topic and compete start time
										// within group compete group id
										String _withinGroupCompeteGroupId = JSONUtils
												.getStringFromJSONObject(
														respJSONObject,
														_context.getString(R.string.withinGroupCompeteInviteReqResp_groupId));

										// within group compete group topic
										String _withinGroupCompeteGroupTopic = JSONUtils
												.getStringFromJSONObject(
														respJSONObject,
														_context.getString(R.string.withinGroupCompeteInviteReqResp_groupTopic));

										// within group compete start time
										long _withinGroupCompeteStartTime = Long.parseLong(JSONUtils
												.getStringFromJSONObject(
														respJSONObject,
														_context.getString(R.string.withinGroupCompeteInviteReqResp_competeStartTime)));

										LOGGER.debug("Invite more than one of user friends, their user id = "
												+ inviteesId
												+ " to walk compete, the schedule within group compete group id = "
												+ _withinGroupCompeteGroupId
												+ ", topic = "
												+ _withinGroupCompeteGroupTopic
												+ " and start time = "
												+ _withinGroupCompeteStartTime);

										// invite more than one of user friends
										// to walk compete successful
										executant.onSuccess(
												_withinGroupCompeteGroupId,
												_withinGroupCompeteGroupTopic,
												_withinGroupCompeteStartTime);
									} catch (NumberFormatException e) {
										// get exception message
										String _exceptionMsg = e.getMessage();

										LOGGER.error("Invite more than one of user friends, their user id = "
												+ inviteesId
												+ " to walk compete failed, exception message = "
												+ e.getMessage());

										// invite more than one of user friends
										// to walk compete failed
										// test by ares
										executant.onFailure(11, _exceptionMsg);

										e.printStackTrace();
									}
								} else {
									LOGGER.error("Invite more than one of user friends, their user id = "
											+ inviteesId
											+ " to walk compete response json object is null");
								}
							}

							@Override
							public void onFailure(int statusCode,
									String errorMsg) {
								LOGGER.info("Invite more than one of user friends, their user id list = "
										+ inviteesId
										+ " to walk compete failed, status code = "
										+ statusCode
										+ " and error message = "
										+ errorMsg);

								// invite more than one of user friends to walk
								// compete failed
								executant.onFailure(statusCode, errorMsg);
							}

						});
	}

	/**
	 * @title respondWithinGroupCompeteInvite
	 * @descriptor respond the within group walk compete invite
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param groupId
	 *            : the walk compete group id
	 * @param isAgreed
	 *            : your decision of the within group walk compete invite
	 * @param executant
	 *            :
	 * @author Ares
	 */
	public void respondWithinGroupCompeteInvite(long userId, String token,
			final String groupId, final boolean isAgreed,
			final ICMConnector executant) {
		// respond the within group walk compete invite with compete group id
		// and user decision
		((WithinGroupCompeteNetworkAdapter) NetworkAdapter
				.getInstance()
				.getWorkerNetworkAdapter(WithinGroupCompeteNetworkAdapter.class))
				.respondWithinGroupCompeteInvite(userId, token, groupId,
						isAgreed, new AsyncHttpRespJSONHandler() {

							@Override
							public void onSuccess(int statusCode,
									JSONArray respJSONArray) {
								// nothing to do
							}

							@Override
							public void onSuccess(int statusCode,
									JSONObject respJSONObject) {
								LOGGER.info("Respond the within group walk compete invite successful, group id = "
										+ groupId
										+ ", my decision agreed = "
										+ isAgreed
										+ ", status code = "
										+ statusCode
										+ " and response json object = "
										+ respJSONObject);

								// check respond the within group walk compete
								// invite response json object
								if (null != respJSONObject) {
									// get context
									Context _context = SSApplication
											.getContext();

									// get within group walk compete group
									// invite info
									// GroupInviteInfoBean
									// _withinGroupWalkCompeteInviteInfo = new
									// GroupInviteInfoBean(
									// JSONUtils
									// .getJSONObjectFromJSONObject(
									// respJSONObject,
									// _context.getString(R.string.respondWithinGroupCompeteInviteReqResp_groupInfo)));

									// test by ares
									// define within group compete invite
									// response group info key
									String _competeTopicKey = _context
											.getString(R.string.groupInviteInfo_topic);
									String _competeStartTimeKey = _context
											.getString(R.string.respondWithinGroupCompeteInviteReqResp_competeStartTime);
									String _competeDurationTimeKey = _context
											.getString(R.string.groupInviteInfo_duration);

									// generate the within group compete group
									// info
									JSONObject _withinGroupCompeteGroupInfo = new JSONObject();
									JSONUtils.putObject2JSONObject(
											_withinGroupCompeteGroupInfo,
											_competeTopicKey, JSONUtils
													.getStringFromJSONObject(
															respJSONObject,
															_competeTopicKey));
									// test by ares
									JSONUtils
											.putObject2JSONObject(
													_withinGroupCompeteGroupInfo,
													_competeStartTimeKey,
													(null == JSONUtils
															.getLongFromJSONObject(
																	respJSONObject,
																	_competeStartTimeKey) ? System
															.currentTimeMillis() / 1000L + 3 * 60
															: JSONUtils
																	.getLongFromJSONObject(
																			respJSONObject,
																			_competeStartTimeKey)));
									JSONUtils.putObject2JSONObject(
											_withinGroupCompeteGroupInfo,
											_competeDurationTimeKey,
											JSONUtils.getIntFromJSONObject(
													respJSONObject,
													_competeDurationTimeKey));
									GroupInviteInfoBean _withinGroupWalkCompeteInviteInfo = new GroupInviteInfoBean(
											_withinGroupCompeteGroupInfo);

									LOGGER.debug("Respond the within group walk compete invite, its group id = "
											+ groupId
											+ ", my decision agreed = "
											+ isAgreed
											+ " and group invite info = "
											+ _withinGroupWalkCompeteInviteInfo);

									// respond the within group walk compete
									// invite successful
									executant.onSuccess(groupId,
											_withinGroupWalkCompeteInviteInfo);
								} else {
									LOGGER.error("Respond the within group walk compete invite, its group id = "
											+ groupId
											+ " and my decision agreed = "
											+ isAgreed
											+ " response json object is null");
								}
							}

							@Override
							public void onFailure(int statusCode,
									String errorMsg) {
								LOGGER.info("Respond the within group walk compete invite failed, group id = "
										+ groupId
										+ ", my decision agreed = "
										+ isAgreed
										+ ", status code = "
										+ statusCode
										+ " and error message = "
										+ errorMsg);

								// respond the within group walk compete invite
								// failed
								executant.onFailure(statusCode, errorMsg);
							}

						});
	}

	/**
	 * @title publishWithinGroupCompeteWalkingInfo
	 * @descriptor publish within group compete walking info including walking
	 *             velocity and total distance
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param groupId
	 *            : the walk compete group id
	 * @param walkingVelocity
	 *            : walking velocity
	 * @param totalStep
	 *            : walking total step
	 * @param totalDistance
	 *            : walking total distance
	 * @param executant
	 *            :
	 * @author Ares
	 */
	public void publishWithinGroupCompeteWalkingInfo(long userId, String token,
			final String groupId, final float walkingVelocity,
			final int totalStep, final double totalDistance,
			final ICMConnector executant) {
		// publish within group compete user walking info with group id, walking
		// velocity, total step and total distance
		((WithinGroupCompeteNetworkAdapter) NetworkAdapter
				.getInstance()
				.getWorkerNetworkAdapter(WithinGroupCompeteNetworkAdapter.class))
				.publishWithinGroupCompeteWalkingInfo(userId, token, groupId,
						walkingVelocity, totalStep, totalDistance,
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
								LOGGER.info("Publish within group compete user walking info failed, group id = "
										+ groupId
										+ ", walking velocity = "
										+ walkingVelocity
										+ ", total step = "
										+ totalStep
										+ ", distance = "
										+ totalDistance
										+ ", status code = "
										+ statusCode
										+ " and error message = "
										+ errorMsg);

								// publish within group compete user walking
								// info failed
								executant.onFailure(statusCode, errorMsg);
							}

						});
	}

	/**
	 * @title getWithinGroupCompeteWalkingInfo
	 * @descriptor get within group compete walking info including each member
	 *             walking velocity and his total distance
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param groupId
	 *            : the walk compete group id
	 * @param lastFetchTimestamp
	 *            : last fetched compete group attendees walking info timestamp
	 * @param executant
	 *            :
	 * @author Ares
	 */
	public void getWithinGroupCompeteWalkingInfo(long userId, String token,
			final String groupId, final long lastFetchTimestamp,
			final ICMConnector executant) {
		// get within group compete each member walking info including walking
		// velocities, total distance and total step with group id and last
		// fetch timestamp
		((WithinGroupCompeteNetworkAdapter) NetworkAdapter
				.getInstance()
				.getWorkerNetworkAdapter(WithinGroupCompeteNetworkAdapter.class))
				.getWithinGroupCompeteWalkingInfo(userId, token, groupId,
						lastFetchTimestamp, new AsyncHttpRespJSONHandler() {

							@Override
							public void onSuccess(int statusCode,
									JSONArray respJSONArray) {
								// nothing to do
							}

							@Override
							public void onSuccess(int statusCode,
									JSONObject respJSONObject) {
								LOGGER.info("Get within group compete each member walking info successful, status code = "
										+ statusCode
										+ " and response json object = "
										+ respJSONObject);

								// check get within group compete user walking
								// info response json object
								if (null != respJSONObject) {
									// get context
									Context _context = SSApplication
											.getContext();

									// define within group compete user walking
									// info latest timestamp and attendees temp
									// walking info list
									long _competeGroupAttendeesWalkingInfoLatestTimestamp = 0;
									List<UserInfoGroupResultBean> _competeGroupAttendeesTmpWalkingInfoList = new ArrayList<UserInfoGroupResultBean>();

									try {
										// get and check response within group
										// compete group attendees walking info
										// latest timestamp
										// within group compete group attendees
										// walking info
										// latest timestamp
										_competeGroupAttendeesWalkingInfoLatestTimestamp = Long.parseLong(JSONUtils
												.getStringFromJSONObject(
														respJSONObject,
														_context.getString(R.string.getWithinGroupCompeteWalkInfoReqResp_latestTimestamp)));
									} catch (NumberFormatException e) {
										LOGGER.error("Get within group compete each member walking info, group id = "
												+ groupId
												+ ", last fetch timestamp = "
												+ lastFetchTimestamp
												+ " failed, exception message = "
												+ e.getMessage());

										// get the within group compete group
										// attendees walking info failed
										//

										e.printStackTrace();
									}

									// get and check response within group
									// compete group attendees walking info json
									// array
									JSONArray _competeGroupAttendeesWalkingInfoJSONArray = JSONUtils.getJSONArrayFromJSONObject(
											respJSONObject,
											_context.getString(R.string.getWithinGroupCompeteWalkInfoReqResp_walkersWalkInfo));
									if (null != _competeGroupAttendeesTmpWalkingInfoList) {
										for (int i = 0; i < _competeGroupAttendeesWalkingInfoJSONArray
												.length(); i++) {
											// get and check within group
											// compete group attendee walking
											// info json object
											JSONObject _attendeeWalkingInfo = JSONUtils
													.getJSONObjectFromJSONArray(
															_competeGroupAttendeesWalkingInfoJSONArray,
															i);
											if (null != _attendeeWalkingInfo) {
												// generate attendee temp
												// walking info
												UserInfoGroupResultBean _attendeeTmpWalkingInfo = new UserInfoGroupResultBean(
														_attendeeWalkingInfo);

												// get and check attendee walk
												// velocity list json array
												JSONArray _attendeeWalkVelocityArray = JSONUtils
														.getJSONArrayFromJSONObject(
																JSONUtils
																		.getJSONObjectFromJSONObject(
																				_attendeeWalkingInfo,
																				_context.getString(R.string.getWithinGroupCompeteWalkInfoReqResp_walkerWalkInfo)),
																_context.getString(R.string.getWithinGroupCompeteWalkInfoReqResp_walkVelocity));
												if (null != _attendeeWalkVelocityArray) {
													for (int j = 0; j < _attendeeWalkVelocityArray
															.length(); j++) {
														// add attendee walk
														// velocity to attendee
														// temp walking info
														_attendeeTmpWalkingInfo
																.getResult()
																.addWalkVelocity(
																		new MemberWalkVelocityBean(
																				JSONUtils
																						.getJSONObjectFromJSONArray(
																								_attendeeWalkVelocityArray,
																								j)));
													}
												}

												// add it to list
												_competeGroupAttendeesTmpWalkingInfoList
														.add(_attendeeTmpWalkingInfo);
											}
										}
									}

									LOGGER.debug("Get within group compete each member walking info, group id = "
											+ groupId
											+ ", last fetch timestamp = "
											+ lastFetchTimestamp
											+ ", the attendees walking info latest timestamp = "
											+ _competeGroupAttendeesWalkingInfoLatestTimestamp
											+ " and attendees temp walking info list = "
											+ _competeGroupAttendeesTmpWalkingInfoList);

									// get within group compete user walking
									// info successful
									//
								} else {
									LOGGER.error("Get within group compete each member walking info response json object is null");
								}
							}

							@Override
							public void onFailure(int statusCode,
									String errorMsg) {
								LOGGER.info("Get within group compete each member walking info failed, group id = "
										+ groupId
										+ ", status code = "
										+ statusCode
										+ " and error message = "
										+ errorMsg);

								// get within group compete user walking info
								// failed
								executant.onFailure(statusCode, errorMsg);
							}

						});
	}

}
