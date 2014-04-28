/**
 * 
 */
package com.smartsport.spedometer.group.walk;

import java.util.Map;

import org.json.JSONObject;

import com.amap.api.services.core.LatLonPoint;
import com.smartsport.spedometer.R;
import com.smartsport.spedometer.network.INetworkAdapter;
import com.smartsport.spedometer.network.NetworkUtils;
import com.smartsport.spedometer.network.handler.AsyncHttpRespJSONHandler;
import com.smartsport.spedometer.utils.JSONUtils;

/**
 * @name WalkInviteNetworkAdapter
 * @descriptor walk invite network adapter
 * @author Ares
 * @version 1.0
 */
public class WalkInviteNetworkAdapter implements INetworkAdapter {

	/**
	 * @title inviteWalk
	 * @descriptor invite one of user friends to walk together
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param inviteeId
	 *            : the friend id who been invite for walking together
	 * @param scheduleBeginTime
	 *            : walk invite schedule begin time
	 * @param scheduleEndTime
	 *            : walk invite schedule end time
	 * @param topic
	 *            : walk invite topic
	 * @param asyncHttpRespJSONHandler
	 *            : asynchronous http response json handler
	 * @author Ares
	 */
	public void inviteWalk(int userId, String token, int inviteeId,
			long scheduleBeginTime, long scheduleEndTime, String topic,
			AsyncHttpRespJSONHandler asyncHttpRespJSONHandler) {
		// get user common request param
		Map<String, String> _inviteWalkReqParam = NetworkUtils
				.genUserComReqParam(userId, token);

		// set user walk invite invitee id to param
		_inviteWalkReqParam.put(
				NETWORK_ENGINE.getContext().getString(
						R.string.walkInviteReqParam_inviteeId),
				String.valueOf(inviteeId));

		// generate walk invite info
		JSONObject _walkInviteInfo = new JSONObject();

		// set schedule begin, end time and topic to walk invite info
		JSONUtils
				.putObject2JSONObject(
						_walkInviteInfo,
						NETWORK_ENGINE
								.getContext()
								.getString(
										R.string.walkInviteReqParam_inviteInfo_scheduleBeginTime),
						String.valueOf(scheduleBeginTime));
		JSONUtils
				.putObject2JSONObject(
						_walkInviteInfo,
						NETWORK_ENGINE
								.getContext()
								.getString(
										R.string.walkInviteReqParam_inviteInfo_scheduleEndTime),
						String.valueOf(scheduleEndTime));
		JSONUtils.putObject2JSONObject(
				_walkInviteInfo,
				NETWORK_ENGINE.getContext().getString(
						R.string.walkInviteReqParam_inviteInfo_topic), topic);

		// set user walk invite info to param
		_inviteWalkReqParam.put(
				NETWORK_ENGINE.getContext().getString(
						R.string.walkInviteReqParam_inviteInfo),
				_walkInviteInfo.toString());

		// send invite one of user friends walk together asynchronous post http
		// request
		NETWORK_ENGINE.postWithAPI(
				NETWORK_ENGINE.getContext().getString(R.string.walkInvite_url),
				_inviteWalkReqParam, asyncHttpRespJSONHandler);
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
	 * @param asyncHttpRespJSONHandler
	 *            : asynchronous http response json handler
	 * @author Ares
	 */
	public void respondWalkInvite(int userId, String token, int tmpGroupId,
			boolean isAgreed, AsyncHttpRespJSONHandler asyncHttpRespJSONHandler) {
		// get user common request param
		Map<String, String> _respondWalkInviteReqParam = NetworkUtils
				.genUserComReqParam(userId, token);

		// set user received walk invite temp group id and his decision to param
		_respondWalkInviteReqParam.put(
				NETWORK_ENGINE.getContext().getString(
						R.string.respondWalkInviteReqParam_tmpGroupId),
				String.valueOf(tmpGroupId));
		_respondWalkInviteReqParam.put(
				NETWORK_ENGINE.getContext().getString(
						R.string.respondWalkInviteReqParam_decision),
				NETWORK_ENGINE.getContext().getString(
						isAgreed ? R.string.inviteRespondDecision_agree
								: R.string.inviteRespondDecision_disagree));

		// send respond walk invite asynchronous post http request
		NETWORK_ENGINE.postWithAPI(
				NETWORK_ENGINE.getContext().getString(
						R.string.respondWalkInvite_url),
				_respondWalkInviteReqParam, asyncHttpRespJSONHandler);
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
	 * @param asyncHttpRespJSONHandler
	 *            : asynchronous http response json handler
	 * @author Ares
	 */
	public void startWalking(int userId, String token, int groupId,
			AsyncHttpRespJSONHandler asyncHttpRespJSONHandler) {
		// get user common request param
		Map<String, String> _startWalkingReqParam = NetworkUtils
				.genUserComReqParam(userId, token);

		// set user start walking group id to param
		_startWalkingReqParam.put(
				NETWORK_ENGINE.getContext().getString(
						R.string.walkStartOrStopReqParam_groupId),
				String.valueOf(groupId));

		// send start walking asynchronous post http request
		NETWORK_ENGINE.postWithAPI(
				NETWORK_ENGINE.getContext().getString(R.string.walkStart_url),
				_startWalkingReqParam, asyncHttpRespJSONHandler);
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
	 * @param asyncHttpRespJSONHandler
	 *            : asynchronous http response json handler
	 * @author Ares
	 */
	public void stopWalking(int userId, String token, int groupId,
			AsyncHttpRespJSONHandler asyncHttpRespJSONHandler) {
		// get user common request param
		Map<String, String> _stopWalkingReqParam = NetworkUtils
				.genUserComReqParam(userId, token);

		// set user stop walking group id to param
		_stopWalkingReqParam.put(
				NETWORK_ENGINE.getContext().getString(
						R.string.walkStartOrStopReqParam_groupId),
				String.valueOf(groupId));

		// send stop walking asynchronous post http request
		NETWORK_ENGINE.postWithAPI(
				NETWORK_ENGINE.getContext().getString(R.string.walkStop_url),
				_stopWalkingReqParam, asyncHttpRespJSONHandler);
	}

	/**
	 * @title publishWalkingInfo
	 * @descriptor publish walking info including walking location and total
	 *             step
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
	 * @param asyncHttpRespJSONHandler
	 *            : asynchronous http response json handler
	 * @author Ares
	 */
	public void publishWalkingInfo(int userId, String token, int groupId,
			LatLonPoint walkingLocation, int totalStep,
			AsyncHttpRespJSONHandler asyncHttpRespJSONHandler) {
		// get user common request param
		Map<String, String> _publishWalkingInfoReqParam = NetworkUtils
				.genUserComReqParam(userId, token);

		// set user walking group id and total step to param
		_publishWalkingInfoReqParam.put(
				NETWORK_ENGINE.getContext().getString(
						R.string.publishWalkInfoReqParam_groupId),
				String.valueOf(groupId));
		_publishWalkingInfoReqParam.put(
				NETWORK_ENGINE.getContext().getString(
						R.string.publishWalkInfoReqParam_walkTotalStep),
				String.valueOf(totalStep));

		// check user walking location longitude and latitude and set it to
		// param
		if (null != walkingLocation) {
			_publishWalkingInfoReqParam.put(NETWORK_ENGINE.getContext()
					.getString(R.string.walkLocationInfo_longitude), String
					.valueOf(walkingLocation.getLongitude()));
			_publishWalkingInfoReqParam.put(NETWORK_ENGINE.getContext()
					.getString(R.string.walkLocationInfo_latitude), String
					.valueOf(walkingLocation.getLatitude()));
		}

		// send publish walking info asynchronous post http request
		NETWORK_ENGINE.postWithAPI(
				NETWORK_ENGINE.getContext().getString(
						R.string.publishWalkInfo_url),
				_publishWalkingInfoReqParam, asyncHttpRespJSONHandler);
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
	 * @param asyncHttpRespJSONHandler
	 *            : asynchronous http response json handler
	 * @author Ares
	 */
	public void getPartnerWalkingInfo(int userId, String token, int groupId,
			long lastFetchTimestamp,
			AsyncHttpRespJSONHandler asyncHttpRespJSONHandler) {
		// get user common request param
		Map<String, String> _getPartnerWalkingInfoReqParam = NetworkUtils
				.genUserComReqParam(userId, token);

		// set user walking group id and last fetched walk partner walking info
		// to param
		_getPartnerWalkingInfoReqParam.put(NETWORK_ENGINE.getContext()
				.getString(R.string.getPartnerWalkInfoReqParam_groupId), String
				.valueOf(groupId));
		_getPartnerWalkingInfoReqParam
				.put(NETWORK_ENGINE.getContext().getString(
						R.string.getPartnerWalkInfoReqParam_lastFetchTimestamp),
						String.valueOf(lastFetchTimestamp));

		// send get walk partner walking info asynchronous post http request
		NETWORK_ENGINE.postWithAPI(
				NETWORK_ENGINE.getContext().getString(
						R.string.getPartnerWalkInfo_url),
				_getPartnerWalkingInfoReqParam, asyncHttpRespJSONHandler);
	}

}
