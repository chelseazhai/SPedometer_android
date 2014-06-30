/**
 * 
 */
package com.smartsport.spedometer.group.compete;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.group.GroupInviteInfoBean;
import com.smartsport.spedometer.network.INetworkAdapter;
import com.smartsport.spedometer.network.NetworkUtils;
import com.smartsport.spedometer.network.handler.AsyncHttpRespJSONHandler;
import com.smartsport.spedometer.utils.JSONUtils;

/**
 * @name WithinGroupCompeteNetworkAdapter
 * @descriptor within group compete network adapter
 * @author Ares
 * @version 1.0
 */
public class WithinGroupCompeteNetworkAdapter implements INetworkAdapter {

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
	 * @param asyncHttpRespJSONHandler
	 *            : asynchronous http response json handler
	 * @author Ares
	 */
	public void inviteWithinGroupCompete(long userId, String token,
			List<Long> inviteesId, GroupInviteInfoBean inviteInfo,
			AsyncHttpRespJSONHandler asyncHttpRespJSONHandler) {
		// get user common request param
		Map<String, String> _inviteWithinGroupCompeteReqParam = NetworkUtils
				.genUserComReqParam(userId, token);

		// check user within group compete invite invitees id and set it to
		// param
		if (null != inviteesId) {
			// generate within group compete invite invitees id json array
			JSONArray _withinGroupCompeteInviteesIdJSONArray = new JSONArray();

			for (Long _inviteeId : inviteesId) {
				// generate within group compete invitee id json object and set
				// invitee id to it
				JSONObject _withinGroupCompeteInviteeIdJSONObject = new JSONObject();
				JSONUtils
						.putObject2JSONObject(
								_withinGroupCompeteInviteeIdJSONObject,
								NETWORK_ENGINE
										.getContext()
										.getString(
												R.string.withinGroupCompeteInviteReqParam_inviteeId),
								String.valueOf(_inviteeId));

				// add it to list
				_withinGroupCompeteInviteesIdJSONArray
						.put(_withinGroupCompeteInviteeIdJSONObject);
			}

			_inviteWithinGroupCompeteReqParam
					.put(NETWORK_ENGINE
							.getContext()
							.getString(
									R.string.withinGroupCompeteInviteReqParam_inviteesInfo),
							_withinGroupCompeteInviteesIdJSONArray.toString());
		}

		// check and set user within group compete invite duration time and
		// topic to param
		if (null != inviteInfo) {
			_inviteWithinGroupCompeteReqParam
					.put(NETWORK_ENGINE
							.getContext()
							.getString(
									R.string.withinGroupCompeteInviteReqParam_competeDuration),
							String.valueOf(inviteInfo.getDuration()));
			_inviteWithinGroupCompeteReqParam
					.put(NETWORK_ENGINE
							.getContext()
							.getString(
									R.string.withinGroupCompeteInviteReqParam_inviteTopic),
							inviteInfo.getTopic());
		}

		// send invite more than one of user friends walk compete asynchronous
		// post http request
		NETWORK_ENGINE.postWithAPI(
				NETWORK_ENGINE.getContext().getString(
						R.string.withinGroupCompeteInvite_url),
				_inviteWithinGroupCompeteReqParam, asyncHttpRespJSONHandler);
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
	 * @param asyncHttpRespJSONHandler
	 *            : asynchronous http response json handler
	 * @author Ares
	 */
	public void respondWithinGroupCompeteInvite(long userId, String token,
			String groupId, boolean isAgreed,
			AsyncHttpRespJSONHandler asyncHttpRespJSONHandler) {
		// get user common request param
		Map<String, String> _respondWithinGroupCompeteInviteReqParam = NetworkUtils
				.genUserComReqParam(userId, token);

		// set user received within group walk compete invite group id and his
		// decision to param
		_respondWithinGroupCompeteInviteReqParam
				.put(NETWORK_ENGINE
						.getContext()
						.getString(
								R.string.respondWithinGroupCompeteInviteReqParam_groupId),
						groupId);
		_respondWithinGroupCompeteInviteReqParam
				.put(NETWORK_ENGINE
						.getContext()
						.getString(
								R.string.respondWithinGroupCompeteInviteReqParam_decision),
						NETWORK_ENGINE
								.getContext()
								.getString(
										isAgreed ? R.string.inviteRespondDecision_agree
												: R.string.inviteRespondDecision_disagree));

		// send respond within group walk compete invite asynchronous post http
		// request
		NETWORK_ENGINE.postWithAPI(
				NETWORK_ENGINE.getContext().getString(
						R.string.respondWithinGroupCompeteInvite_url),
				_respondWithinGroupCompeteInviteReqParam,
				asyncHttpRespJSONHandler);
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
	 *            : the compete walking group id
	 * @param walkingVelocity
	 *            : walking velocity
	 * @param totalStep
	 *            : walking total step
	 * @param totalDistance
	 *            : walking total distance
	 * @param asyncHttpRespJSONHandler
	 *            : asynchronous http response json handler
	 * @author Ares
	 */
	public void publishWithinGroupCompeteWalkingInfo(long userId, String token,
			String groupId, float walkingVelocity, int totalStep,
			double totalDistance,
			AsyncHttpRespJSONHandler asyncHttpRespJSONHandler) {
		// get user common request param
		Map<String, String> _publishWithinGroupCompeteWalkingInfoReqParam = NetworkUtils
				.genUserComReqParam(userId, token);

		// set user within group compete walking group id, velocity and total
		// distance to param
		_publishWithinGroupCompeteWalkingInfoReqParam
				.put(NETWORK_ENGINE
						.getContext()
						.getString(
								R.string.publishWithinGroupCompeteWalkInfoReqParam_groupId),
						groupId);
		_publishWithinGroupCompeteWalkingInfoReqParam
				.put(NETWORK_ENGINE
						.getContext()
						.getString(
								R.string.publishWithinGroupCompeteWalkInfoReqParam_walkVelocity),
						String.valueOf(walkingVelocity));
		_publishWithinGroupCompeteWalkingInfoReqParam
				.put(NETWORK_ENGINE
						.getContext()
						.getString(
								R.string.publishWithinGroupCompeteWalkInfoReqParam_walkTotalStep),
						String.valueOf(totalStep));
		_publishWithinGroupCompeteWalkingInfoReqParam
				.put(NETWORK_ENGINE
						.getContext()
						.getString(
								R.string.publishWithinGroupCompeteWalkInfoReqParam_walkTotalDistance),
						String.valueOf(totalDistance));

		// send publish within group compete walking info asynchronous post http
		// request
		NETWORK_ENGINE.postWithAPI(
				NETWORK_ENGINE.getContext().getString(
						R.string.publishWithinGroupCompeteWalkInfo_url),
				_publishWithinGroupCompeteWalkingInfoReqParam,
				asyncHttpRespJSONHandler);
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
	 *            : the compete walking group id
	 * @param lastFetchTimestamp
	 *            : last fetched compete group attendees walking info timestamp
	 * @param asyncHttpRespJSONHandler
	 *            : asynchronous http response json handler
	 * @author Ares
	 */
	public void getWithinGroupCompeteWalkingInfo(long userId, String token,
			String groupId, long lastFetchTimestamp,
			AsyncHttpRespJSONHandler asyncHttpRespJSONHandler) {
		// get user common request param
		Map<String, String> _getWithinGroupCompeteWalkingInfoReqParam = NetworkUtils
				.genUserComReqParam(userId, token);

		// set user within group compete walking group id and last fetched
		// compete attendees walking info timestamp to param
		_getWithinGroupCompeteWalkingInfoReqParam
				.put(NETWORK_ENGINE.getContext().getString(
						R.string.getWithinGroupCompeteWalkInfoReqParam_groupId),
						groupId);
		_getWithinGroupCompeteWalkingInfoReqParam
				.put(NETWORK_ENGINE
						.getContext()
						.getString(
								R.string.getWithinGroupCompeteWalkInfoReqParam_lastFetchTimestamp),
						String.valueOf(lastFetchTimestamp));

		// send get within group compete walking info asynchronous post http
		// request
		NETWORK_ENGINE.postWithAPI(
				NETWORK_ENGINE.getContext().getString(
						R.string.getWithinGroupCompeteWalkInfo_url),
				_getWithinGroupCompeteWalkingInfoReqParam,
				asyncHttpRespJSONHandler);
	}

}
