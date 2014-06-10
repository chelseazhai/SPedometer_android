/**
 * 
 */
package com.smartsport.spedometer.group;

import java.util.Map;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.network.INetworkAdapter;
import com.smartsport.spedometer.network.NetworkUtils;
import com.smartsport.spedometer.network.handler.AsyncHttpRespJSONHandler;

/**
 * @name GroupInfoNetworkAdapter
 * @descriptor walk and compete group info network adapter
 * @author Ares
 * @version 1.0
 */
public class GroupInfoNetworkAdapter implements INetworkAdapter {

	/**
	 * @title getUserScheduleGroups
	 * @descriptor get user all schedule groups with type from remote server
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param groupType
	 *            : walk or compete group type
	 * @param asyncHttpRespJSONHandler
	 *            : asynchronous http response json handler
	 * @author Ares
	 */
	public void getUserScheduleGroups(int userId, String token,
			GroupType groupType,
			AsyncHttpRespJSONHandler asyncHttpRespJSONHandler) {
		// get user common request param
		Map<String, String> _getUserScheduleGroupsReqParam = NetworkUtils
				.genUserComReqParam(userId, token);

		// set user schedule group type to param
		_getUserScheduleGroupsReqParam.put(NETWORK_ENGINE.getContext()
				.getString(R.string.getScheduleGroupsReqParam_groupType),
				String.valueOf(groupType.getValue()));

		// send get user schedule group list with type asynchronous post http
		// request
		NETWORK_ENGINE.postWithAPI(
				NETWORK_ENGINE.getContext().getString(
						R.string.getScheduleGroups_url),
				_getUserScheduleGroupsReqParam, asyncHttpRespJSONHandler);
	}

	/**
	 * @title getUserScheduleGroupInfo
	 * @descriptor get user schedule group info from remote server
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param groupId
	 *            : walk or compete group id
	 * @param asyncHttpRespJSONHandler
	 *            : asynchronous http response json handler
	 * @author Ares
	 */
	public void getUserScheduleGroupInfo(int userId, String token,
			String groupId, AsyncHttpRespJSONHandler asyncHttpRespJSONHandler) {
		// get user common request param
		Map<String, String> _getUserScheduleGroupInfoReqParam = NetworkUtils
				.genUserComReqParam(userId, token);

		// set user schedule group id to param
		_getUserScheduleGroupInfoReqParam.put(NETWORK_ENGINE.getContext()
				.getString(R.string.getScheduleGroupInfoReqParam_groupId),
				groupId);

		// send get user schedule group info asynchronous post http request
		NETWORK_ENGINE.postWithAPI(
				NETWORK_ENGINE.getContext().getString(
						R.string.getScheduleGroupInfo_url),
				_getUserScheduleGroupInfoReqParam, asyncHttpRespJSONHandler);
	}

	/**
	 * @title getUserHistoryGroups
	 * @descriptor get user all history groups from remote server
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param asyncHttpRespJSONHandler
	 *            : asynchronous http response json handler
	 * @author Ares
	 */
	public void getUserHistoryGroups(int userId, String token,
			AsyncHttpRespJSONHandler asyncHttpRespJSONHandler) {
		// send get user history group list asynchronous post http request
		NETWORK_ENGINE.postWithAPI(
				NETWORK_ENGINE.getContext().getString(
						R.string.getHistoryGroups_url),
				NetworkUtils.genUserComReqParam(userId, token),
				asyncHttpRespJSONHandler);
	}

	/**
	 * @title getUserHistoryGroupInfo
	 * @descriptor get user history group info from remote server
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param groupId
	 *            : walk or compete group id
	 * @param asyncHttpRespJSONHandler
	 *            : asynchronous http response json handler
	 * @author Ares
	 */
	public void getUserHistoryGroupInfo(int userId, String token,
			String groupId, AsyncHttpRespJSONHandler asyncHttpRespJSONHandler) {
		// get user common request param
		Map<String, String> _getUserHistoryGroupInfoReqParam = NetworkUtils
				.genUserComReqParam(userId, token);

		// set user history group id to param
		_getUserHistoryGroupInfoReqParam.put(NETWORK_ENGINE.getContext()
				.getString(R.string.getHistoryGroupInfoReqParam_groupId),
				groupId);

		// send get user history group info asynchronous post http request
		NETWORK_ENGINE.postWithAPI(
				NETWORK_ENGINE.getContext().getString(
						R.string.getHistoryGroupInfo_url),
				_getUserHistoryGroupInfoReqParam, asyncHttpRespJSONHandler);
	}

}
