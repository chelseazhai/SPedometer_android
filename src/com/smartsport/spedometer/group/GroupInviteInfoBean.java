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
 * @name GroupInviteInfoBean
 * @descriptor walk or compete group invite info bean
 * @author Ares
 * @version 1.0
 */
public class GroupInviteInfoBean implements Serializable {

	/**
	 * walk or compete group invite info bean serial version UID
	 */
	private static final long serialVersionUID = 6082559049291580684L;

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			GroupInviteInfoBean.class);

	// context
	private Context context;

	// group invite begin time, duration, end time and topic
	private long beginTime;
	private long duration;
	private long endTime;
	private String topic;

	/**
	 * @title GroupInviteInfoBean
	 * @descriptor walk or compete group invite info bean constructor
	 * @author Ares
	 */
	public GroupInviteInfoBean() {
		super();

		// get context
		context = SSApplication.getContext();
	}

	/**
	 * @title GroupInviteInfoBean
	 * @descriptor walk or compete group invite info bean constructor with json
	 *             object
	 * @param info
	 *            : walk or compete group invite info json object
	 * @author Ares
	 */
	public GroupInviteInfoBean(JSONObject info) {
		this();

		// check parsed walk or compete group invite info json object
		if (null != info) {
			// set walk or compete group invite info attributes
			try {
				// group invite begin time
				beginTime = Long.parseLong(JSONUtils.getStringFromJSONObject(
						info,
						context.getString(R.string.groupInviteInfo_beginTime)));
			} catch (NumberFormatException e) {
				LOGGER.error("Get group invite begin time from json info object = "
						+ info
						+ " error, exception message = "
						+ e.getMessage());

				e.printStackTrace();
			}

			// group invite duration
			// check is or not has invite duration
			if (JSONUtils.jsonObjectKeys(info).contains(
					context.getString(R.string.groupInviteInfo_duration))) {
				try {
					duration = Long
							.parseLong(JSONUtils.getStringFromJSONObject(
									info,
									context.getString(R.string.groupInviteInfo_duration)));
				} catch (NumberFormatException e) {
					LOGGER.error("Get group invite duration from json info object = "
							+ info
							+ " error, exception message = "
							+ e.getMessage());

					e.printStackTrace();
				}
			}

			// group invite end time
			// check is or not has invite end time
			if (JSONUtils.jsonObjectKeys(info).contains(
					context.getString(R.string.groupInviteInfo_endTime))) {
				try {
					endTime = Long
							.parseLong(JSONUtils.getStringFromJSONObject(
									info,
									context.getString(R.string.groupInviteInfo_endTime)));
				} catch (NumberFormatException e) {
					LOGGER.error("Get group invite end time from json info object = "
							+ info
							+ " error, exception message = "
							+ e.getMessage());

					e.printStackTrace();
				}
			}

			// topic
			topic = JSONUtils.getStringFromJSONObject(info,
					context.getString(R.string.groupInviteInfo_topic));
		} else {
			LOGGER.error("Constructor walk or compete group invite info with json object error, the info is null");
		}
	}

	public long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	@Override
	public String toString() {
		return "GroupInviteInfoBean [beginTime=" + beginTime + ", duration="
				+ duration + ", endTime=" + endTime + " and topic=" + topic
				+ "]";
	}

	/**
	 * @title getWalkInviteInfo
	 * @descriptor get walk invite info json object
	 * @return walk invite info json object
	 * @author Ares
	 */
	public JSONObject getWalkInviteInfo() {
		// define walk invite info
		JSONObject _walkInviteInfo = new JSONObject();

		// set schedule begin, end time and topic to walk invite info
		JSONUtils.putObject2JSONObject(_walkInviteInfo,
				context.getString(R.string.groupInviteInfo_beginTime),
				String.valueOf(beginTime));
		JSONUtils.putObject2JSONObject(_walkInviteInfo,
				context.getString(R.string.groupInviteInfo_endTime),
				String.valueOf(endTime));
		JSONUtils.putObject2JSONObject(_walkInviteInfo,
				context.getString(R.string.groupInviteInfo_topic), topic);

		return _walkInviteInfo;
	}

}
