/**
 * 
 */
package com.smartsport.spedometer.group;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.SSApplication;
import com.smartsport.spedometer.utils.JSONUtils;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name GroupBean
 * @descriptor walk or compete group bean
 * @author Ares
 * @version 1.0
 */
public class GroupBean implements Serializable {

	/**
	 * walk or compete group bean serial version UID
	 */
	private static final long serialVersionUID = 785383406092524895L;

	// logger
	private static final SSLogger LOGGER = new SSLogger(GroupBean.class);

	// group id, type, invite info and member number
	private String groupId;
	private GroupType type;
	private GroupInviteInfoBean inviteInfo;
	private int memberNumber;

	/**
	 * @title GroupBean
	 * @descriptor walk or compete group bean constructor
	 * @author Ares
	 */
	public GroupBean() {
		super();
	}

	/**
	 * @title GroupBean
	 * @descriptor walk or compete group bean constructor with json object
	 * @param info
	 *            : walk or compete group json object
	 * @author Ares
	 */
	public GroupBean(JSONObject info) {
		this();

		// parse walk or compete group json object
		parseGroup(info);
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public GroupType getType() {
		return type;
	}

	public void setType(GroupType type) {
		this.type = type;
	}

	public GroupInviteInfoBean getInviteInfo() {
		return inviteInfo;
	}

	public void setInviteInfo(GroupInviteInfoBean inviteInfo) {
		this.inviteInfo = inviteInfo;
	}

	public int getMemberNumber() {
		return memberNumber;
	}

	public void setMemberNumber(int memberNumber) {
		this.memberNumber = memberNumber;
	}

	@Override
	public String toString() {
		return "GroupBean [groupId=" + groupId + ", type=" + type
				+ ", inviteInfo=" + inviteInfo + " and memberNumber="
				+ memberNumber + "]";
	}

	/**
	 * @title parseGroup
	 * @descriptor parse walk or compete group json object to group object
	 * @param info
	 *            : walk or compete group json object
	 * @return walk or compete group object
	 * @author Ares
	 */
	public GroupBean parseGroup(JSONObject info) {
		// check parsed walk or compete group json object
		if (null != info) {
			// get context
			Context _context = SSApplication.getContext();

			// set walk or compete group attributes
			// group id
			groupId = JSONUtils.getStringFromJSONObject(info,
					_context.getString(R.string.getGroupsReqResp_groupId));

			// type
			// check is or not has type
			if (JSONUtils
					.jsonObjectKeys(info)
					.contains(
							_context.getString(R.string.getHistoryGroupsReqResp_groupType))) {
				type = GroupType
						.getGroupType(JSONUtils.getStringFromJSONObject(
								info,
								_context.getString(R.string.getHistoryGroupsReqResp_groupType)));
			}

			// invite info
			// inviteInfo = new GroupInviteInfoBean(
			// JSONUtils.getJSONObjectFromJSONObject(
			// info,
			// _context.getString(R.string.getGroupsReqResp_groupInviteInfo)));

			// test by ares
			// define group invite info topic, begin, duration and end time key
			String _groupInviteTopicKey = _context
					.getString(R.string.groupInviteInfo_topic);
			String _groupInviteBeginTimeKey = _context
					.getString(R.string.groupInviteInfo_beginTime);
			String _groupInviteDurationTimeKey = _context
					.getString(R.string.groupInviteInfo_duration);
			String _groupInviteEndTimeKey = _context
					.getString(R.string.groupInviteInfo_endTime);

			// get group invite info: topic, begin, duration and end time object
			String _groupInviteTopic = JSONUtils.getStringFromJSONObject(info,
					_groupInviteTopicKey);
			String _groupInviteBeginTime = JSONUtils.getStringFromJSONObject(
					info, _groupInviteBeginTimeKey);
			String _groupInviteDurationTime = JSONUtils
					.getStringFromJSONObject(info, _groupInviteDurationTimeKey);
			String _groupInviteEndTime = JSONUtils.getStringFromJSONObject(
					info, _groupInviteEndTimeKey);

			// generate group invite info json object
			JSONObject _groupInviteInfo = new JSONObject();
			try {
				// topic
				if (null != _groupInviteTopic) {
					_groupInviteInfo.put(_groupInviteTopicKey,
							_groupInviteTopic);

				}
				// begin time
				if (null != _groupInviteBeginTime) {
					_groupInviteInfo.put(_groupInviteBeginTimeKey,
							_groupInviteBeginTime);

				}
				// duration time
				if (null != _groupInviteDurationTime) {
					_groupInviteInfo.put(_groupInviteDurationTimeKey,
							_groupInviteDurationTime);

				}
				// end time
				if (null != _groupInviteEndTime) {
					_groupInviteInfo.put(_groupInviteEndTimeKey,
							_groupInviteEndTime);

				}
			} catch (JSONException e) {
				LOGGER.error("Generate group invite info json object error, exception message = "
						+ e.getMessage());

				e.printStackTrace();
			}

			// invite info
			inviteInfo = new GroupInviteInfoBean(_groupInviteInfo);

			try {
				// member number
				memberNumber = Integer
						.parseInt(JSONUtils.getStringFromJSONObject(
								info,
								_context.getString(R.string.getGroupsReqResp_groupMemNum)));
			} catch (NumberFormatException e) {
				LOGGER.error("Get walk or compete group member number from json info object = "
						+ info
						+ " error, exception message = "
						+ e.getMessage());

				e.printStackTrace();
			}
		} else {
			LOGGER.error("Parse walk or compete group json object error, the info is null");
		}

		return this;
	}

}
