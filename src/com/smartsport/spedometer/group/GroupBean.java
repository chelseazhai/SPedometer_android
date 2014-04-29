/**
 * 
 */
package com.smartsport.spedometer.group;

import java.io.Serializable;

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

	// context
	protected transient Context context;

	// group id, type, invite info and member number
	private int groupId;
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

		// initialize context
		context = SSApplication.getContext();
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

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
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
			// set walk or compete group attributes
			try {
				// group id
				groupId = Integer.parseInt(JSONUtils.getStringFromJSONObject(
						info,
						context.getString(R.string.getGroupsReqResp_groupId)));
			} catch (NumberFormatException e) {
				LOGGER.error("Get walk or compete group id from json info object = "
						+ info
						+ " error, exception message = "
						+ e.getMessage());

				e.printStackTrace();
			}

			// type
			// check is or not has type
			if (JSONUtils
					.jsonObjectKeys(info)
					.contains(
							context.getString(R.string.getHistoryGroupsReqResp_groupType))) {
				type = GroupType
						.getGroupType(JSONUtils.getStringFromJSONObject(
								info,
								context.getString(R.string.getHistoryGroupsReqResp_groupType)));
			}

			// invite info
			inviteInfo = new GroupInviteInfoBean(
					JSONUtils.getJSONObjectFromJSONObject(
							info,
							context.getString(R.string.getGroupsReqResp_groupInviteInfo)));

			try {
				// member number
				memberNumber = Integer
						.parseInt(JSONUtils.getStringFromJSONObject(
								info,
								context.getString(R.string.getGroupsReqResp_groupMemNum)));
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
