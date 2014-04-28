/**
 * 
 */
package com.smartsport.spedometer.group.compete;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.smartsport.spedometer.R;
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
	 * @param durationTime
	 *            : walk compete duration time
	 * @param topic
	 *            : walk compete invite topic
	 * @param asyncHttpRespJSONHandler
	 *            : asynchronous http response json handler
	 * @author Ares
	 */
	public void inviteWithinGroupCompete(int userId, String token,
			List<Integer> inviteesId, long durationTime, String topic,
			AsyncHttpRespJSONHandler asyncHttpRespJSONHandler) {
		// get user common request param
		Map<String, String> _inviteWithinGroupCompeteReqParam = NetworkUtils
				.genUserComReqParam(userId, token);

		// set user within group compete invite duration time and topic to param
		_inviteWithinGroupCompeteReqParam
				.put(NETWORK_ENGINE
						.getContext()
						.getString(
								R.string.withinGroupCompeteInviteReqParam_competeDuration),
						String.valueOf(durationTime));
		_inviteWithinGroupCompeteReqParam.put(
				NETWORK_ENGINE.getContext().getString(
						R.string.withinGroupCompeteInviteReqParam_inviteTopic),
				topic);

		// check user within group compete invite invitees id and set it to
		// param
		if (null != inviteesId) {
			// generate within group compete invite invitees id json array
			JSONArray _withinGroupCompeteInviteesIdJSONArray = new JSONArray();

			for (Integer _inviteeId : inviteesId) {
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
								_inviteeId);

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
	public void respondWithinGroupCompeteInvite(int userId, String token,
			int groupId, boolean isAgreed,
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
						String.valueOf(groupId));
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
	 * @param totalDistance
	 *            : walking total distance
	 * @param asyncHttpRespJSONHandler
	 *            : asynchronous http response json handler
	 * @author Ares
	 */
	public void publishWithinGroupCompeteWalkingInfo(int userId, String token,
			int groupId, float walkingVelocity, int totalDistance,
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
						String.valueOf(groupId));
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
	 * @param asyncHttpRespJSONHandler
	 *            : asynchronous http response json handler
	 * @author Ares
	 */
	public void getWithinGroupCompeteWalkingInfo(int userId, String token,
			int groupId, AsyncHttpRespJSONHandler asyncHttpRespJSONHandler) {
		// get user common request param
		Map<String, String> _getWithinGroupCompeteWalkingInfoReqParam = NetworkUtils
				.genUserComReqParam(userId, token);

		// set user within group compete walking group id to param
		_getWithinGroupCompeteWalkingInfoReqParam
				.put(NETWORK_ENGINE.getContext().getString(
						R.string.getWithinGroupCompeteWalkInfoReqParam_groupId),
						String.valueOf(groupId));

		// send get within group compete walking info asynchronous post http
		// request
		NETWORK_ENGINE.postWithAPI(
				NETWORK_ENGINE.getContext().getString(
						R.string.getWithinGroupCompeteWalkInfo_url),
				_getWithinGroupCompeteWalkingInfoReqParam,
				asyncHttpRespJSONHandler);
	}

}
