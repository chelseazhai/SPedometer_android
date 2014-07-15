/**
 * 
 */
package com.smartsport.spedometer.group.info;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.smartsport.spedometer.group.GroupBean;
import com.smartsport.spedometer.group.GroupInviteInfoBean;
import com.smartsport.spedometer.group.GroupType;
import com.smartsport.spedometer.user.info.UserInfoBean;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name GroupInfoBean
 * @descriptor walk or compete group info bean
 * @author Ares
 * @version 1.0
 */
public abstract class GroupInfoBean implements Serializable {

	/**
	 * walk or compete group info bean serial version UID
	 */
	private static final long serialVersionUID = 2252869760843739642L;

	// logger
	private static final SSLogger LOGGER = new SSLogger(GroupInfoBean.class);

	// context
	protected transient Context context;

	// group id, type, invite info and members info
	private String groupId;
	private GroupType type;
	private GroupInviteInfoBean inviteInfo;
	private List<UserInfoBean> membersInfo;

	/**
	 * @title GroupInfoBean
	 * @descriptor walk or compete group info bean constructor with group object
	 * @param group
	 *            : walk or compete group object
	 * @author Ares
	 */
	public GroupInfoBean(GroupBean group) {
		super();

		// check walk or compete group object
		if (null != group) {
			// set walk or compete group info attributes
			// group id
			groupId = group.getGroupId();

			// type
			type = group.getType();

			// invite info
			inviteInfo = group.getInviteInfo();

			// members info
			membersInfo = new ArrayList<UserInfoBean>(group.getMemberNumber());
		} else {
			LOGGER.error("Constructor walk or compete group info with group object error, the info is null");
		}
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

	public List<UserInfoBean> getMembersInfo() {
		return membersInfo;
	}

	public void setMembersInfo(List<UserInfoBean> membersInfo) {
		this.membersInfo = membersInfo;
	}

	/**
	 * @title addMemberInfo
	 * @descriptor add walk or compete group member info object to group members
	 *             info list
	 * @param info
	 *            : walk or compete group member info object
	 * @author Ares
	 */
	public void addMemberInfo(UserInfoBean memberInfo) {
		// check member info for adding to list
		if (null != memberInfo) {
			membersInfo.add(memberInfo);
		} else {
			LOGGER.equals("Add walk or compete group member info to list error, member info is null");
		}
	}

	@Override
	public String toString() {
		return "GroupInfoBean [groupId=" + groupId + ", type=" + type
				+ ", inviteInfo=" + inviteInfo + " and membersInfo="
				+ membersInfo + "]";
	}

	/**
	 * @title parseGroupInfo
	 * @descriptor parse walk or compete group info json object to group info
	 *             object
	 * @param info
	 *            : walk or compete group info json object
	 * @return group info object
	 * @author Ares
	 */
	protected abstract GroupInfoBean parseGroupInfo(JSONObject info);

	/**
	 * @title parseGroupInfo
	 * @descriptor parse walk or compete group info json array to group info
	 *             object
	 * @param info
	 *            : walk or compete group info json array
	 * @return group info object
	 * @author Ares
	 */
	protected abstract GroupInfoBean parseGroupInfo(JSONArray info);

}
