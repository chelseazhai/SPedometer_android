/**
 * 
 */
package com.smartsport.spedometer.group.walk;

import java.util.Map;

import com.amap.api.services.core.LatLonPoint;
import com.smartsport.spedometer.R;
import com.smartsport.spedometer.group.GroupInviteInfoBean;
import com.smartsport.spedometer.network.INetworkAdapter;
import com.smartsport.spedometer.network.NetworkUtils;
import com.smartsport.spedometer.network.handler.AsyncHttpRespJSONHandler;

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
	 * @param inviteInfo
	 *            : walk invite info
	 * @param asyncHttpRespJSONHandler
	 *            : asynchronous http response json handler
	 * @author Ares
	 */
	public void inviteWalk(long userId, String token, long inviteeId,
			GroupInviteInfoBean inviteInfo,
			AsyncHttpRespJSONHandler asyncHttpRespJSONHandler) {
		// get user common request param
		Map<String, String> _inviteWalkReqParam = NetworkUtils
				.genUserComReqParam(userId, token);

		// set user walk invite invitee id to param
		_inviteWalkReqParam.put(
				NETWORK_ENGINE.getContext().getString(
						R.string.walkInviteReqParam_inviteeId),
				String.valueOf(inviteeId));

		// check and set user walk invite info to param
		if (null != inviteInfo) {
			_inviteWalkReqParam.put(
					NETWORK_ENGINE.getContext().getString(
							R.string.walkInviteReqParam_inviteInfo), inviteInfo
							.getWalkInviteInfo().toString());
		}

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
	public void respondWalkInvite(long userId, String token, String tmpGroupId,
			boolean isAgreed, AsyncHttpRespJSONHandler asyncHttpRespJSONHandler) {
		// get user common request param
		Map<String, String> _respondWalkInviteReqParam = NetworkUtils
				.genUserComReqParam(userId, token);

		// set user received walk invite temp group id and his decision to param
		_respondWalkInviteReqParam.put(
				NETWORK_ENGINE.getContext().getString(
						R.string.respondWalkInviteReqParam_tmpGroupId),
				tmpGroupId);
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
	public void startWalking(long userId, String token, String groupId,
			AsyncHttpRespJSONHandler asyncHttpRespJSONHandler) {
		// get user common request param
		Map<String, String> _startWalkingReqParam = NetworkUtils
				.genUserComReqParam(userId, token);

		// set user start walking group id to param
		_startWalkingReqParam.put(
				NETWORK_ENGINE.getContext().getString(
						R.string.walkStartOrStopReqParam_groupId), groupId);

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
	public void stopWalking(long userId, String token, String groupId,
			AsyncHttpRespJSONHandler asyncHttpRespJSONHandler) {
		// get user common request param
		Map<String, String> _stopWalkingReqParam = NetworkUtils
				.genUserComReqParam(userId, token);

		// set user stop walking group id to param
		_stopWalkingReqParam.put(
				NETWORK_ENGINE.getContext().getString(
						R.string.walkStartOrStopReqParam_groupId), groupId);

		// send stop walking asynchronous post http request
		NETWORK_ENGINE.postWithAPI(
				NETWORK_ENGINE.getContext().getString(R.string.walkStop_url),
				_stopWalkingReqParam, asyncHttpRespJSONHandler);
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
	 * @param asyncHttpRespJSONHandler
	 *            : asynchronous http response json handler
	 * @author Ares
	 */
	public void publishWalkingInfo(long userId, String token, String groupId,
			LatLonPoint walkingLocation, int totalStep, double totalDistance,
			AsyncHttpRespJSONHandler asyncHttpRespJSONHandler) {
		// get user common request param
		Map<String, String> _publishWalkingInfoReqParam = NetworkUtils
				.genUserComReqParam(userId, token);

		// set user walking group id, total step and distance to param
		_publishWalkingInfoReqParam.put(
				NETWORK_ENGINE.getContext().getString(
						R.string.publishWalkInfoReqParam_groupId), groupId);
		_publishWalkingInfoReqParam.put(
				NETWORK_ENGINE.getContext().getString(
						R.string.publishWalkInfoReqParam_walkTotalStep),
				String.valueOf(totalStep));
		_publishWalkingInfoReqParam.put(
				NETWORK_ENGINE.getContext().getString(
						R.string.publishWalkInfoReqParam_walkTotalDistance),
				String.valueOf(totalDistance));

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
	public void getPartnerWalkingInfo(long userId, String token,
			String groupId, long lastFetchTimestamp,
			AsyncHttpRespJSONHandler asyncHttpRespJSONHandler) {
		// get user common request param
		Map<String, String> _getPartnerWalkingInfoReqParam = NetworkUtils
				.genUserComReqParam(userId, token);

		// set user walking group id and last fetched walk partner walking info
		// to param
		_getPartnerWalkingInfoReqParam.put(NETWORK_ENGINE.getContext()
				.getString(R.string.getPartnerWalkInfoReqParam_groupId),
				groupId);
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
