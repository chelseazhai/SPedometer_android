/**
 * 
 */
package com.smartsport.spedometer.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.smartsport.spedometer.group.info.HistoryGroupInfoBean;
import com.smartsport.spedometer.group.info.ScheduleGroupInfoBean;
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.network.NetworkAdapter;
import com.smartsport.spedometer.network.handler.AsyncHttpRespJSONHandler;
import com.smartsport.spedometer.utils.JSONUtils;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name GroupInfoModel
 * @descriptor user walk or compete group info model
 * @author Ares
 * @version 1.0
 */
public class GroupInfoModel {

	// logger
	private static final SSLogger LOGGER = new SSLogger(GroupInfoModel.class);

	// singleton instance
	private static volatile GroupInfoModel _singletonInstance;

	// user schedule, history walk and compete group map(key: group id and
	// value: group object)
	private Map<String, GroupBean> walkOrCompeteGroupMap;

	/**
	 * @title GroupInfoModel
	 * @descriptor user walk or compete group info model private constructor
	 * @author Ares
	 */
	private GroupInfoModel() {
		super();

		// initialize user walk or compete group map
		walkOrCompeteGroupMap = new HashMap<String, GroupBean>();
	}

	// get user walk or compete group info model singleton instance
	public static GroupInfoModel getInstance() {
		if (null == _singletonInstance) {
			synchronized (GroupInfoModel.class) {
				if (null == _singletonInstance) {
					_singletonInstance = new GroupInfoModel();
				}
			}
		}

		return _singletonInstance;
	}

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
	public void getUserScheduleGroups(long userId, String token,
			final GroupType groupType, final ICMConnector executant) {
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
										+ " and response json array = "
										+ respJSONArray);

								// check get user schedule groups response json
								// array
								if (null != respJSONArray) {
									// define schedule walk or compete group
									// list
									List<GroupBean> _scheduleGroupList = new ArrayList<GroupBean>();

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

											// add user schedule group object to
											// list
											_scheduleGroupList
													.add(_scheduleGroupBean);
										}
									}

									// get user all schedule groups successful
									executant.onSuccess(groupType,
											_scheduleGroupList);
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
								executant.onFailure(statusCode, errorMsg);
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
	public void getUserScheduleGroupInfo(long userId, String token,
			final String groupId, final ICMConnector executant) {
		// get user schedule group info with user id, token and schedule group
		// id
		((GroupInfoNetworkAdapter) NetworkAdapter.getInstance()
				.getWorkerNetworkAdapter(GroupInfoNetworkAdapter.class))
				.getUserScheduleGroupInfo(userId, token, groupId,
						new AsyncHttpRespJSONHandler() {

							@Override
							public void onSuccess(int statusCode,
									JSONArray respJSONArray) {
								LOGGER.info("Get user schedule group info successful, group id = "
										+ groupId
										+ ", status code = "
										+ statusCode
										+ " and response json array = "
										+ respJSONArray);

								// check get user schedule group info response
								// json array
								if (null != respJSONArray) {
									// get user schedule group info successful
									executant
											.onSuccess(new ScheduleGroupInfoBean(
													walkOrCompeteGroupMap
															.get(groupId),
													respJSONArray));
								} else {
									LOGGER.error("Get user schedule group info response json array is null, group id = "
											+ groupId);
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
								LOGGER.info("Get user schedule group info failed, status code = "
										+ statusCode
										+ " and error message = "
										+ errorMsg);

								// get user schedule group info failed
								executant.onFailure(statusCode, errorMsg);
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
	 * @param groupType
	 *            : history group type(walk or compete)
	 * @param executant
	 *            :
	 * @author Ares
	 */
	public void getUserHistoryGroups(long userId, String token,
			final GroupType groupType, ICMConnector executant) {
		// get user all history groups with user id, token and history group
		// type
		((GroupInfoNetworkAdapter) NetworkAdapter.getInstance()
				.getWorkerNetworkAdapter(GroupInfoNetworkAdapter.class))
				.getUserHistoryGroups(userId, token, groupType,
						new AsyncHttpRespJSONHandler() {

							@Override
							public void onSuccess(int statusCode,
									JSONArray respJSONArray) {
								LOGGER.info("Get user history groups, type = "
										+ groupType
										+ " successful, status code = "
										+ statusCode
										+ " and response json array = "
										+ respJSONArray);

								// check get user history groups response json
								// array
								if (null != respJSONArray) {
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
									LOGGER.error("Get user history groups, type = "
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
								LOGGER.info("Get user all history groups, type = "
										+ groupType
										+ " failed, status code = "
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
	public void getUserHistoryGroupInfo(long userId, String token,
			final String groupId, ICMConnector executant) {
		// get user history group info with user id, token and history group id
		((GroupInfoNetworkAdapter) NetworkAdapter.getInstance()
				.getWorkerNetworkAdapter(GroupInfoNetworkAdapter.class))
				.getUserHistoryGroupInfo(userId, token, groupId,
						new AsyncHttpRespJSONHandler() {

							@Override
							public void onSuccess(int statusCode,
									JSONArray respJSONArray) {
								// nothing to do
							}

							@Override
							public void onSuccess(int statusCode,
									JSONObject respJSONObject) {
								LOGGER.info("Get user history group info successful, group id = "
										+ groupId
										+ ", status code = "
										+ statusCode
										+ " and response json object = "
										+ respJSONObject);

								// check get user history group info response
								// json object
								if (null != respJSONObject) {
									// generate history walk or compete group
									// info
									HistoryGroupInfoBean _historyGroupInfo = new HistoryGroupInfoBean(
											walkOrCompeteGroupMap.get(groupId),
											respJSONObject);

									LOGGER.debug("Get user history group info successful, group id = "
											+ groupId
											+ " and group info = "
											+ _historyGroupInfo);

									// get user history group info successful
									//
								} else {
									LOGGER.error("Get user history group info response json object is null, group id = "
											+ groupId);
								}
							}

							@Override
							public void onFailure(int statusCode,
									String errorMsg) {
								LOGGER.info("Get user history group info failed, group id = "
										+ groupId
										+ ", status code = "
										+ statusCode
										+ " and error message = "
										+ errorMsg);

								// get user history group info failed
								//
							}

						});
	}

}
