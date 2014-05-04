/**
 * 
 */
package com.smartsport.spedometer.group.info;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.SparseArray;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.SSApplication;
import com.smartsport.spedometer.group.GroupBean;
import com.smartsport.spedometer.group.info.result.GroupResultInfoBean;
import com.smartsport.spedometer.group.info.result.UserInfoGroupResultBean;
import com.smartsport.spedometer.user.UserInfoBean;
import com.smartsport.spedometer.utils.JSONUtils;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name HistoryGroupInfoBean
 * @descriptor history walk or compete group info with group result info bean
 * @author Ares
 * @version 1.0
 */
public class HistoryGroupInfoBean extends GroupInfoBean {

	/**
	 * history walk or compete group info with group result info bean serial
	 * version UID
	 */
	private static final long serialVersionUID = -6915296742292958798L;

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			HistoryGroupInfoBean.class);

	// history walk or compete group start time, duration, stop time and member
	// result info map
	private long startTime;
	private long duration;
	private long stopTime;
	private SparseArray<GroupResultInfoBean> memberResultInfoMap;

	public HistoryGroupInfoBean(GroupBean group) {
		super(group);

		// check group object
		if (null != group) {
			// initialize member result info in the history walk or compete
			// group map
			memberResultInfoMap = new SparseArray<GroupResultInfoBean>(
					group.getMemberNumber());
		}
	}

	/**
	 * @title HistoryGroupInfoBean
	 * @descriptor history walk or compete group info bean constructor with
	 *             group object and member result info
	 * @param group
	 *            : walk or compete group object
	 * @param info
	 *            : member result info in the history walk or compete group
	 * @author Ares
	 */
	public HistoryGroupInfoBean(GroupBean group, JSONObject info) {
		this(group);

		// check group object
		if (null != group) {
			// parse history walk or compete group info with members result info
			parseGroupInfo(info);
		}
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public long getStopTime() {
		return stopTime;
	}

	public void setStopTime(long stopTime) {
		this.stopTime = stopTime;
	}

	public SparseArray<GroupResultInfoBean> getMemberResultInfoMap() {
		return memberResultInfoMap;
	}

	/**
	 * @title getMemberResult
	 * @descriptor get history walk or compete group member result
	 * @param memberInfo
	 *            : member info in the history walk or compete group
	 * @return member result info of the history walk or compete group
	 * @author Ares
	 */
	public GroupResultInfoBean getMemberResult(UserInfoBean memberInfo) {
		GroupResultInfoBean _memberResultInfo = null;

		// check member result info map and member info
		if (null != memberResultInfoMap && null != memberInfo) {
			_memberResultInfo = memberResultInfoMap.get(memberInfo.hashCode());
		} else {
			LOGGER.error("Get history walk or compete group member result info error, member result info map = "
					+ _memberResultInfo + " and member info = " + memberInfo);
		}

		return _memberResultInfo;
	}

	public void setMemberResultInfoMap(
			SparseArray<GroupResultInfoBean> memberResultInfoMap) {
		this.memberResultInfoMap = memberResultInfoMap;
	}

	@Override
	public String toString() {
		return "HistoryGroupInfoBean [group info=" + super.toString()
				+ ", startTime=" + startTime + ", duration=" + duration
				+ ", stopTime=" + stopTime + " and memberResultInfoMap="
				+ memberResultInfoMap + "]";
	}

	@Override
	protected GroupInfoBean parseGroupInfo(JSONObject info) {
		// check history walk or compete group result info
		if (null != info) {
			// get context
			Context _context = SSApplication.getContext();

			// set history walk or compete group info attributes
			try {
				// walk start time, duration and stop time
				startTime = Long
						.parseLong(JSONUtils.getStringFromJSONObject(
								info,
								_context.getString(R.string.getHistoryGroupInfoResp_walkStartTime)));
				duration = Long
						.parseLong(JSONUtils.getStringFromJSONObject(
								info,
								_context.getString(R.string.getHistoryGroupInfoResp_walkDuration)));
				stopTime = Long
						.parseLong(JSONUtils.getStringFromJSONObject(
								info,
								_context.getString(R.string.getHistoryGroupInfoResp_walkStopTime)));
			} catch (NumberFormatException e) {
				LOGGER.error("Parse history walk or compete group walk start time, duration and stop time from group json info object = "
						+ info
						+ " error, exception message = "
						+ e.getMessage());

				e.printStackTrace();
			}

			// member result info
			// get and check members result json array info in the history walk
			// or compete group
			JSONArray _membersResultInfoJSONArray = JSONUtils
					.getJSONArrayFromJSONObject(
							info,
							_context.getString(R.string.getHistoryGroupInfoResp_walkResult));
			if (null != _membersResultInfoJSONArray) {
				for (int i = 0; i < _membersResultInfoJSONArray.length(); i++) {
					// get members info with result info in the history walk or
					// compete group
					UserInfoGroupResultBean _memberInfoWithResultInfo = new UserInfoGroupResultBean(
							JSONUtils.getJSONObjectFromJSONArray(
									_membersResultInfoJSONArray, i));

					// members info
					addMemberInfo(_memberInfoWithResultInfo);

					// member result
					memberResultInfoMap.append(
							_memberInfoWithResultInfo.hashCode(),
							_memberInfoWithResultInfo.getResult());
				}
			} else {
				LOGGER.error("Parse history walk or compete group info with result error, the history walk or compete group member result info is null");
			}
		} else {
			LOGGER.error("Parse history walk or compete group info with result error, the info is null");
		}

		return this;
	}

	@Override
	@Deprecated
	protected GroupInfoBean parseGroupInfo(JSONArray info) {
		// nothing to do
		return null;
	}

}
