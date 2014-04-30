/**
 * 
 */
package com.smartsport.spedometer.group;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.SparseArray;

import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.network.NetworkAdapter;
import com.smartsport.spedometer.network.handler.AsyncHttpRespJSONHandler;
import com.smartsport.spedometer.strangersocial.pat.StrangerPatModel;
import com.smartsport.spedometer.utils.JSONUtils;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name StrangerPatModel
 * @descriptor user walk or compete group info model
 * @author Ares
 * @version 1.0
 */
public class GroupInfoModel {

	// logger
	private static final SSLogger LOGGER = new SSLogger(StrangerPatModel.class);

	// user schedule, history walk and compete group map(key: group id and
	// value: group object)
	private SparseArray<GroupBean> walkOrCompeteGroupMap;

	/**
	 * @title getUserScheduleGroups
	 * @descriptor get user all schedule groups
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param groupType
	 *            : schedule group type(walk or compete)
	 * @param executant
	 *            :
	 * @author Ares
	 */
	public void getUserScheduleGroups(int userId, String token,
			final GroupType groupType, ICMConnector executant) {
		// get user all schedule groups with user id, token and schedule group
		// type
		((GroupInfoNetworkAdapter) NetworkAdapter.getInstance()
				.getWorkerNetworkAdapter(GroupInfoNetworkAdapter.class))
				.getUserScheduleGroups(userId, token, groupType,
						new AsyncHttpRespJSONHandler() {

							@Override
							public void onSuccess(int statusCode,
									JSONArray respJSONArray) {
								LOGGER.info("Get user schedule groups, type = "
										+ groupType
										+ " successful, status code = "
										+ statusCode
										+ " and response json Array = "
										+ respJSONArray);

								// check get user schedule groups response json
								// array
								if (null != respJSONArray) {
									// check user walk or compete group map
									if (null == walkOrCompeteGroupMap) {
										walkOrCompeteGroupMap = new SparseArray<GroupBean>();
									}

									for (int i = 0; i < respJSONArray.length(); i++) {
										// get and check user schedule group
										// json object from response json array
										JSONObject _scheduleGroup = JSONUtils
												.getJSONObjectFromJSONArray(
														respJSONArray, i);
										if (null != _scheduleGroup) {
											// get user schedule group object
											GroupBean _scheduleGroupBean = new GroupBean(
													_scheduleGroup);

											// put user schedule group object to
											// map
											walkOrCompeteGroupMap.put(
													_scheduleGroupBean
															.getGroupId(),
													_scheduleGroupBean);
										}
									}

									// get user all schedule groups successful
									// check user schedule group type
									switch (groupType) {
									case WALK_GROUP:
										//
										break;

									case COMPETE_GROUP:
									default:
										//
										break;
									}
								} else {
									LOGGER.error("Get user schedule groups, type = "
											+ groupType
											+ " response json array is null");
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
								LOGGER.info("Get user all schedule groups, type = "
										+ groupType
										+ " failed, status code = "
										+ statusCode
										+ " and error message = "
										+ errorMsg);

								// get user all schedule groups failed
								//
							}

						});
	}

	/**
	 * @title getUserScheduleGroupInfo
	 * @descriptor get user schedule group info
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param groupId
	 *            : schedule group id
	 * @param executant
	 *            :
	 * @author Ares
	 */
	public void getUserScheduleGroupInfo(int userId, String token, int groupId,
			ICMConnector executant) {
		// get user schedule group info with user id, token and schedule group
		// id
		((GroupInfoNetworkAdapter) NetworkAdapter.getInstance()
				.getWorkerNetworkAdapter(GroupInfoNetworkAdapter.class))
				.getUserScheduleGroupInfo(userId, token, groupId,
						new AsyncHttpRespJSONHandler() {

							@Override
							public void onSuccess(int statusCode,
									JSONArray respJSONArray) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(int statusCode,
									JSONObject respJSONObject) {
								// nothing to do
							}

							@Override
							public void onFailure(int statusCode,
									String errorMsg) {
								LOGGER.info("Get user schedule group info failed, status code = "
										+ statusCode
										+ " and error message = "
										+ errorMsg);

								// get user schedule group info failed
								//
							}

						});
	}

	/**
	 * @title getUserHistoryGroups
	 * @descriptor get user all history groups
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param executant
	 *            :
	 * @author Ares
	 */
	public void getUserHistoryGroups(int userId, String token,
			ICMConnector executant) {
		// get user all history groups with user id and token
		((GroupInfoNetworkAdapter) NetworkAdapter.getInstance()
				.getWorkerNetworkAdapter(GroupInfoNetworkAdapter.class))
				.getUserHistoryGroups(userId, token,
						new AsyncHttpRespJSONHandler() {

							@Override
							public void onSuccess(int statusCode,
									JSONArray respJSONArray) {
								LOGGER.info("Get user history groups successful, status code = "
										+ statusCode
										+ " and response json Array = "
										+ respJSONArray);

								// check get user history groups response json
								// array
								if (null != respJSONArray) {
									// check user walk or compete group map
									if (null == walkOrCompeteGroupMap) {
										walkOrCompeteGroupMap = new SparseArray<GroupBean>();
									}

									for (int i = 0; i < respJSONArray.length(); i++) {
										// get and check user history group json
										// object from response json array
										JSONObject _historyGroup = JSONUtils
												.getJSONObjectFromJSONArray(
														respJSONArray, i);
										if (null != _historyGroup) {
											// get user history group object
											GroupBean _historyGroupBean = new GroupBean(
													_historyGroup);

											// put user history group object to
											// map
											walkOrCompeteGroupMap.put(
													_historyGroupBean
															.getGroupId(),
													_historyGroupBean);
										}
									}

									// get user all history groups successful
									//
								} else {
									LOGGER.error("Get user history groups response json array is null");
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
								LOGGER.info("Get user all history groups failed, status code = "
										+ statusCode
										+ " and error message = "
										+ errorMsg);

								// get user all history groups failed
								//
							}

						});
	}

	/**
	 * @title getUserHistoryGroupInfo
	 * @descriptor get user history group info
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param groupId
	 *            : history group id
	 * @param executant
	 *            :
	 * @author Ares
	 */
	public void getUserHistoryGroupInfo(int userId, String token, int groupId,
			ICMConnector executant) {
		// get user history group info with user id, token and history group id
		((GroupInfoNetworkAdapter) NetworkAdapter.getInstance()
				.getWorkerNetworkAdapter(GroupInfoNetworkAdapter.class))
				.getUserHistoryGroupInfo(userId, token, groupId,
						new AsyncHttpRespJSONHandler() {

							@Override
							public void onSuccess(int statusCode,
									JSONArray respJSONArray) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(int statusCode,
									JSONObject respJSONObject) {
								// nothing to do
							}

							@Override
							public void onFailure(int statusCode,
									String errorMsg) {
								LOGGER.info("Get user history group info failed, status code = "
										+ statusCode
										+ " and error message = "
										+ errorMsg);

								// get user history group info failed
								//
							}

						});
	}

}
