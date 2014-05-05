/**
 * 
 */
package com.smartsport.spedometer.group.compete;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.SSApplication;
import com.smartsport.spedometer.group.GroupInviteInfoBean;
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
	public void inviteWithinGroupCompete(int userId, String token,
			final List<Integer> inviteesId, GroupInviteInfoBean inviteInfo,
			ICMConnector executant) {
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
										// get and check response group id and
										// compete start time
										// within group compete group id
										int _withinGroupCompeteGroupId = Integer.parseInt(JSONUtils
												.getStringFromJSONObject(
														respJSONObject,
														_context.getString(R.string.withinGroupCompeteInviteReqResp_groupId)));

										// within group compete start time
										long _withinGroupCompeteStartTime = Long.parseLong(JSONUtils
												.getStringFromJSONObject(
														respJSONObject,
														_context.getString(R.string.withinGroupCompeteInviteReqResp_competeStartTime)));

										LOGGER.debug("Invite more than one of user friends, their user id = "
												+ inviteesId
												+ " to walk compete, the schedule within group compete group id = "
												+ _withinGroupCompeteGroupId
												+ " and start time = "
												+ _withinGroupCompeteStartTime);

										// invite more than one of user friends
										// to walk compete successful
										//
									} catch (NumberFormatException e) {
										LOGGER.error("Invite more than one of user friends, their user id = "
												+ inviteesId
												+ " to walk compete failed, exception message = "
												+ e.getMessage());

										// invite more than one of user friends
										// to walk compete failed
										//

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
								//
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
	public void respondWithinGroupCompeteInvite(int userId, String token,
			final int groupId, final boolean isAgreed, ICMConnector executant) {
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
									// get within group walk compete group
									// invite info
									GroupInviteInfoBean _withinGroupWalkCompeteInviteInfo = new GroupInviteInfoBean(
											JSONUtils
													.getJSONObjectFromJSONObject(
															respJSONObject,
															SSApplication
																	.getContext()
																	.getString(
																			R.string.respondWithinGroupCompeteInviteReqResp_groupInfo)));

									LOGGER.debug("Respond the within group walk compete invite, its group id = "
											+ groupId
											+ ", my decision agreed = "
											+ isAgreed
											+ " and group invite info = "
											+ _withinGroupWalkCompeteInviteInfo);

									// respond the within group walk compete
									// invite successful
									//
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
								//
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
	 * @param totalDistance
	 *            : walking total distance
	 * @param executant
	 *            :
	 * @author Ares
	 */
	public void publishWithinGroupCompeteWalkingInfo(int userId, String token,
			final int groupId, final float walkingVelocity,
			final int totalDistance, ICMConnector executant) {
		// publish within group compete user walking info with group id, walking
		// velocity and total distance
		((WithinGroupCompeteNetworkAdapter) NetworkAdapter
				.getInstance()
				.getWorkerNetworkAdapter(WithinGroupCompeteNetworkAdapter.class))
				.publishWithinGroupCompeteWalkingInfo(userId, token, groupId,
						walkingVelocity, totalDistance,
						new AsyncHttpRespJSONHandler() {

							@Override
							public void onSuccess(int statusCode,
									JSONArray respJSONArray) {
								// nothing to do
							}

							@Override
							public void onSuccess(int statusCode,
									JSONObject respJSONObject) {
								// nothing to do
							}

							@Override
							public void onFailure(int statusCode,
									String errorMsg) {
								LOGGER.info("Publish within group compete user walking info failed, group id = "
										+ groupId
										+ ", walking velocity = "
										+ walkingVelocity
										+ ", total distance = "
										+ totalDistance
										+ ", status code = "
										+ statusCode
										+ " and error message = " + errorMsg);

								// publish within group compete user walking
								// info failed
								//
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
	 * @param executant
	 *            :
	 * @author Ares
	 */
	public void getWithinGroupCompeteWalkingInfo(int userId, String token,
			final int groupId, ICMConnector executant) {
		// get within group compete each member walking info including walking
		// velocity and total distance with group id
		((WithinGroupCompeteNetworkAdapter) NetworkAdapter
				.getInstance()
				.getWorkerNetworkAdapter(WithinGroupCompeteNetworkAdapter.class))
				.getWithinGroupCompeteWalkingInfo(userId, token, groupId,
						new AsyncHttpRespJSONHandler() {

							@Override
							public void onSuccess(int statusCode,
									JSONArray respJSONArray) {
								// nothing to do
							}

							@Override
							public void onSuccess(int statusCode,
									JSONObject respJSONObject) {
								// TODO Auto-generated method stub

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
								//
							}

						});
	}

}
