/**
 * 
 */
package com.smartsport.spedometer.group.info;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.SparseArray;

import com.smartsport.spedometer.group.GroupBean;
import com.smartsport.spedometer.group.GroupType;
import com.smartsport.spedometer.group.info.member.MemberStatus;
import com.smartsport.spedometer.group.info.member.UserInfoMemberStatusBean;
import com.smartsport.spedometer.user.info.UserInfoBean;
import com.smartsport.spedometer.utils.JSONUtils;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name ScheduleGroupInfoBean
 * @descriptor schedule walk or compete group info with member status bean
 * @author Ares
 * @version 1.0
 */
public class ScheduleGroupInfoBean extends GroupInfoBean {

	/**
	 * schedule walk or compete group info with member status bean serial
	 * version UID
	 */
	private static final long serialVersionUID = -2485753287306386371L;

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			ScheduleGroupInfoBean.class);

	// member status in the schedule walk or compete group map
	private SparseArray<MemberStatus> memberStatusMap;

	public ScheduleGroupInfoBean(GroupBean group) {
		super(group);

		// check group object
		if (null != group) {
			// initialize member status in the schedule walk or compete group
			// map
			memberStatusMap = new SparseArray<MemberStatus>(
					group.getMemberNumber());
		}
	}

	/**
	 * @title ScheduleGroupInfoBean
	 * @descriptor schedule walk or compete group info bean constructor with
	 *             group object and member status
	 * @param group
	 *            : walk or compete group object
	 * @param info
	 *            : member status in the schedule walk or compete group
	 * @author Ares
	 */
	public ScheduleGroupInfoBean(GroupBean group, JSONArray info) {
		this(group);

		// check group object
		if (null != group) {
			// parse schedule walk or compete group info with members status
			parseGroupInfo(info);
		}
	}

	public SparseArray<MemberStatus> getMemberStatusMap() {
		return memberStatusMap;
	}

	/**
	 * @title getMemberStatus
	 * @descriptor get schedule walk or compete group member status
	 * @param memberInfo
	 *            : member info in the schedule walk or compete group
	 * @return member status of the schedule walk or compete group
	 * @author Ares
	 */
	public MemberStatus getMemberStatus(UserInfoBean memberInfo) {
		MemberStatus _memberStatus = null;

		// check member status map and member info
		if (null != memberStatusMap && null != memberInfo) {
			_memberStatus = memberStatusMap.get(memberInfo.hashCode());
		} else {
			LOGGER.error("Get schedule walk or compete group member status error, member status map = "
					+ memberStatusMap + " and member info = " + memberInfo);
		}

		return _memberStatus;
	}

	public void setMemberStatusMap(SparseArray<MemberStatus> memberStatusMap) {
		this.memberStatusMap = memberStatusMap;
	}

	@Override
	public String toString() {
		return "ScheduleGroupInfoBean [group info=" + super.toString()
				+ " and memberStatusMap=" + memberStatusMap + "]";
	}

	@Override
	@Deprecated
	protected GroupInfoBean parseGroupInfo(JSONObject info) {
		// nothing to do
		return null;
	}

	@Override
	protected GroupInfoBean parseGroupInfo(JSONArray info) {
		// check schedule walk or compete group member status array
		if (null != info) {
			for (int i = 0; i < info.length(); i++) {
				// get members info with status in the schedule walk or compete
				// group
				UserInfoMemberStatusBean _memberInfoWithStatus = new UserInfoMemberStatusBean(
						JSONUtils.getJSONObjectFromJSONArray(info, i));

				// check the schedule walk or compete group type and set
				// schedule compete group member status online
				if (GroupType.COMPETE_GROUP == getType()) {
					_memberInfoWithStatus
							.setMemberStatus(MemberStatus.MEM_ONLINE);
				}

				// set schedule walk or compete group info attributes
				// members info
				addMemberInfo(_memberInfoWithStatus);

				// member status
				memberStatusMap.append(_memberInfoWithStatus.hashCode(),
						_memberInfoWithStatus.getMemberStatus());
			}
		} else {
			LOGGER.error("Parse schedule walk or compete group info with members status error, the info is null");
		}

		return this;
	}

}
