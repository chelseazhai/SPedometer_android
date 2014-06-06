/**
 * 
 */
package com.smartsport.spedometer.group;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.SSApplication;
import com.smartsport.spedometer.customwidget.SSSimpleAdapterViewBinder;
import com.smartsport.spedometer.utils.SSLogger;
import com.smartsport.spedometer.utils.StringUtils;

/**
 * @name GroupInviteInfoListViewAdapter
 * @descriptor walk or within group compete invite info listView adapter
 * @author Ares
 * @version 1.0
 */
public class GroupInviteInfoListViewAdapter extends SimpleAdapter {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			GroupInviteInfoListViewAdapter.class);

	// walk or within group compete invite info for setting attribute and walk
	// invite info for setting schedule time(begin time and duration) key
	private static final String GROUPINVITEINFO4SETTING_ATTRIBUTE_KEY = "walkOrWithinGroupCompete_inviteInfo_attribute_key";
	private static final String WALKINVITEINFO4SETTING_SCHEDULETIME_KEY = "walk_inviteInfo_scheduleTime_key";

	// milliseconds per minute
	private final int MILLISECONDS_PER_MINUTE = 60 * 1000;

	// timestamp long and short date format
	@SuppressLint("SimpleDateFormat")
	private final SimpleDateFormat TIMESTAMP_LONG_DATEFORMAT = new SimpleDateFormat(
			SSApplication.getContext().getString(R.string.long_dateFormat));
	@SuppressLint("SimpleDateFormat")
	private final SimpleDateFormat TIMESTAMP_SHORT_DATEFORMAT = new SimpleDateFormat(
			SSApplication.getContext().getString(R.string.short_dateFormat));

	// context
	private Context context;

	// walk or within group compete invite info listView adapter data list
	private static List<Map<String, Object>> _sDataList;

	/**
	 * @title GroupInviteInfoListViewAdapter
	 * @descriptor walk or within group compete invite info for setting listView
	 *             adapter constructor with context, label array, content view
	 *             resource, content view subview data key, and content view
	 *             subview id
	 * @param context
	 *            : context
	 * @param labels
	 *            : walk or within group compete invite info for setting label
	 *            array
	 * @param resource
	 *            : resource id
	 * @param dataKeys
	 *            : content view subview data key array
	 * @param ids
	 *            : content view subview id array
	 */
	public GroupInviteInfoListViewAdapter(Context context, String[] labels,
			int resource, String[] dataKeys, int[] ids) {
		super(context, _sDataList = new ArrayList<Map<String, Object>>(),
				resource, dataKeys, ids);

		// save context
		this.context = context;

		// set view binder
		setViewBinder(new GroupInviteInfo4SettingListViewAdapterViewBinder());

		// check the for setting walk or within group compete invite info label
		// array
		if (null != labels) {
			// define walk or within group compete invite info for setting
			// attribute, value hint string resource id and value
			GroupInviteInfoAttr4Setting _attribute = null;
			Integer _valueHintResId = null;
			String _value = null;

			for (int i = 0; i < labels.length; i++) {
				// define walk or within group compete invite info for setting
				// listView adapter data
				Map<String, Object> _data = new HashMap<String, Object>();

				// get walk or within group compete invite info for setting
				// attribute, label and value
				String _label = labels[i];
				if (context.getString(R.string.walk_inviteInfo_topic_label)
						.equalsIgnoreCase(_label)
						|| context
								.getString(
										R.string.withinGroupCompete_inviteInfo_topic_label)
								.equalsIgnoreCase(_label)) {
					_attribute = GroupInviteInfoAttr4Setting.GROUP_TOPIC;
					_valueHintResId = R.string.walkOrWithinGroupCompete_inviteInfo_topic_textView_hint;
				} else if (context.getString(
						R.string.walk_inviteInfo_scheduleTime_label)
						.equalsIgnoreCase(_label)) {
					_attribute = GroupInviteInfoAttr4Setting.WALKINVITE_SCHEDULETIME;
					_valueHintResId = R.string.walk_inviteInfo_scheduleTime_textView_hint;

					// set walk invite schedule time(begin time and duration) as
					// data attributes
					_data.put(WALKINVITEINFO4SETTING_SCHEDULETIME_KEY, _value);

					// format walk invite schedule time
					_value = getWalkInviteScheduleTime(_value);
				} else if (context
						.getString(
								R.string.withinGroupCompete_inviteInfo_durationTime_label)
						.equalsIgnoreCase(_label)) {
					_attribute = GroupInviteInfoAttr4Setting.WITHINGROUPCOMPETE_DURATIONTIME;
					_valueHintResId = R.string.withinGroupCompete_inviteInfo_durationTime_textView_hint;
				}

				// set data attributes
				_data.put(GROUPINVITEINFO4SETTING_ATTRIBUTE_KEY, _attribute);
				_data.put(
						GroupInviteInfo4SettingListViewAdapterKey.GROUPINVITEINFO_LABEL_KEY
								.name(), _label);
				if (null != _valueHintResId) {
					_data.put(
							GroupInviteInfo4SettingListViewAdapterKey.GROUPINVITEINFO_VALUEHINT_KEY
									.name(), _valueHintResId);
				}
				_data.put(
						GroupInviteInfo4SettingListViewAdapterKey.GROUPINVITEINFO_VALUE_KEY
								.name(), _value);

				// add data to list
				_sDataList.add(_data);
			}

			// notify data set changed
			notifyDataSetChanged();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, ?> getItem(int position) {
		return (Map<String, ?>) super.getItem(position);
	}

	/**
	 * @title getItemLabel
	 * @descriptor get editor walk or within group compete invite info item
	 *             label textView text
	 * @param position
	 *            : editor walk or within group compete invite info listView
	 *            position
	 * @return editor walk or within group compete invite info item label
	 * @author Ares
	 */
	public String getItemLabel(int position) {
		// get label with position
		String _label = (String) getItem(position)
				.get(GroupInviteInfo4SettingListViewAdapterKey.GROUPINVITEINFO_LABEL_KEY
						.name());

		// return the label
		return StringUtils
				.sTrim(_label,
						context.getString(R.string.walkOrWithinGroupCompete_inviteInfo_label_suffix));
	}

	/**
	 * @title getItemAttribute
	 * @descriptor get editor walk or within group compete invite info attribute
	 * @param position
	 *            : editor walk or within group compete invite info listView
	 *            position
	 * @return editor walk or within group compete invite info attribute
	 * @author Ares
	 */
	public GroupInviteInfoAttr4Setting getItemAttribute(int position) {
		// return the attribute
		return (GroupInviteInfoAttr4Setting) getItem(position).get(
				GROUPINVITEINFO4SETTING_ATTRIBUTE_KEY);
	}

	/**
	 * @title getItemValue
	 * @descriptor get editor walk or within group compete invite info item info
	 *             textView text
	 * @param position
	 *            : editor walk or within group compete invite info listView
	 *            position
	 * @return editor walk or within group compete invite info item value
	 * @author Ares
	 */
	public String getItemValue(int position) {
		// get value with position
		String _value = (String) getItem(position)
				.get(GroupInviteInfoAttr4Setting.WALKINVITE_SCHEDULETIME == getItemAttribute(position) ? WALKINVITEINFO4SETTING_SCHEDULETIME_KEY
						: GroupInviteInfo4SettingListViewAdapterKey.GROUPINVITEINFO_VALUE_KEY
								.name());

		// return the value
		return null == _value ? ""
				: StringUtils
						.cStrip(_value,
								context.getString(R.string.withinGroupCompete_inviteInfo_durationTime_unit));
	}

	/**
	 * @title getGroupTopic
	 * @descriptor get editor walk or within group compete invite topic
	 * @return editor walk or within group compete invite topic
	 * @author Ares
	 */
	public String getGroupTopic() {
		return getItemValue(0);
	}

	/**
	 * @title getWalkInviteScheduleBeginTime
	 * @descriptor get editor walk invite schedule begin time
	 * @return editor walk invite schedule begin time
	 * @author Ares
	 */
	public String getWalkInviteScheduleBeginTime() {
		// define schedule begin time
		String _scheduleBeginTime = "";

		// get walk invite schedule time
		String _scheduleTime = getItemValue(1);

		// check schedule time
		if (null != _scheduleTime) {
			// split the walk invite schedule time and check
			String[] _scheduleTimes = StringUtils
					.split(_scheduleTime,
							context.getString(R.string.walk_inviteInfo_scheduleTime_beginTimeDuration_splitWord));
			if (null != _scheduleTimes && 2 == _scheduleTimes.length) {
				_scheduleBeginTime = _scheduleTimes[0];
			} else {
				LOGGER.error("Get walk invite schedule begin time error, the schedule time = "
						+ _scheduleTime);
			}
		} else {
			LOGGER.error("Get walk invite schedule begin time error, schedule time is null");
		}

		return _scheduleBeginTime;
	}

	/**
	 * @title getWalkInviteScheduleEndTime
	 * @descriptor get editor walk invite schedule end time
	 * @return editor walk invite schedule end time
	 * @author Ares
	 */
	public String getWalkInviteScheduleEndTime() {
		// define schedule end time
		String _scheduleEndTime = "";

		// get walk invite schedule time
		String _scheduleTime = getItemValue(1);

		// check schedule time
		if (null != _scheduleTime) {
			// split the walk invite schedule time and check
			String[] _scheduleTimes = StringUtils
					.split(_scheduleTime,
							context.getString(R.string.walk_inviteInfo_scheduleTime_beginTimeDuration_splitWord));
			if (null != _scheduleTimes && 2 == _scheduleTimes.length) {
				try {
					_scheduleEndTime = String
							.valueOf(Long.parseLong(_scheduleTimes[0])
									+ Integer
											.parseInt(_scheduleTimes[_scheduleTimes.length - 1])
									* MILLISECONDS_PER_MINUTE);
				} catch (NumberFormatException e) {
					LOGGER.warning("Get walk invite schedule end time error, because it is not set");

					e.printStackTrace();
				}
			} else {
				LOGGER.error("Get walk invite schedule end time error, the schedule time = "
						+ _scheduleTime);
			}
		} else {
			LOGGER.error("Get walk invite schedule end time error, schedule time is null");
		}

		return _scheduleEndTime;
	}

	/**
	 * @title getWithinGroupCompeteInviteDurationTime
	 * @descriptor get editor within group compete invite duration time
	 * @return editor within group compete invite duration time
	 * @author Ares
	 */
	public String getWithinGroupCompeteInviteDurationTime() {
		return getItemValue(1);
	}

	/**
	 * @title setData
	 * @descriptor set editor walk or within group compete invite info data
	 * @param attr
	 *            : editor walk or within group compete invite info attribute
	 * @param value
	 *            : editor walk or within group compete invite info value
	 * @author Ares
	 */
	public void setData(GroupInviteInfoAttr4Setting attr, String value) {
		// traversal editor walk or within group compete invite info data list
		for (Map<String, Object> _editorGroupInfoData : _sDataList) {
			// get and check editor walk or within group compete invite info
			// attribute
			if (null != attr
					&& (GroupInviteInfoAttr4Setting) _editorGroupInfoData
							.get(GROUPINVITEINFO4SETTING_ATTRIBUTE_KEY) == attr) {
				// check the value
				if (null != value && !"".equalsIgnoreCase(value)) {
					// update editor walk or within group compete invite info
					// value with attribute
					switch (attr) {
					case WALKINVITE_SCHEDULETIME:
						// update editor walk invite info with attribute(walk
						// invite schedule time(begin time and duration))
						_editorGroupInfoData.put(
								WALKINVITEINFO4SETTING_SCHEDULETIME_KEY, value);

						// format walk invite schedule time
						value = getWalkInviteScheduleTime(value);
						break;

					case WITHINGROUPCOMPETE_DURATIONTIME:
						value += context
								.getString(R.string.withinGroupCompete_inviteInfo_durationTime_unit);
						break;

					case GROUP_TOPIC:
					default:
						// nothing to do
						break;
					}
				}
				_editorGroupInfoData
						.put(GroupInviteInfo4SettingListViewAdapterKey.GROUPINVITEINFO_VALUE_KEY
								.name(), value);

				// break immediately
				break;
			}
		}

		// notify data set changed
		notifyDataSetChanged();
	}

	/**
	 * @title getWalkInviteScheduleTime
	 * @descriptor get walk invite schedule time
	 * @param scheduleTime
	 *            : walk invite schedule time(begin time and duration)
	 * @return walk invite schedule time format
	 * @author Ares
	 */
	private String getWalkInviteScheduleTime(String scheduleTime) {
		String _scheduleTime = "";

		// check schedule time
		if (null != scheduleTime) {
			// split the walk invite schedule time and check
			String[] _scheduleTimes = StringUtils
					.split(scheduleTime,
							context.getString(R.string.walk_inviteInfo_scheduleTime_beginTimeDuration_splitWord));
			if (null != _scheduleTimes && 2 == _scheduleTimes.length) {
				try {
					// get user input walk invite schedule time begin timestamp
					// value
					long _userInputWalkInviteScheduleTimeBeginTimestampValue = Long
							.parseLong(_scheduleTimes[0]);

					// get default locale and default timeZone calendar
					Calendar _defaultCalendar = Calendar.getInstance();

					// define and get begin time calendar day
					_defaultCalendar
							.setTimeInMillis(_userInputWalkInviteScheduleTimeBeginTimestampValue);
					int _beginTimeDay = _defaultCalendar
							.get(Calendar.DAY_OF_MONTH);

					// define and get end time calendar day
					_defaultCalendar
							.setTimeInMillis(_userInputWalkInviteScheduleTimeBeginTimestampValue
									+ Integer
											.parseInt(_scheduleTimes[_scheduleTimes.length - 1])
									* MILLISECONDS_PER_MINUTE);
					int _endTimeDay = _defaultCalendar
							.get(Calendar.DAY_OF_MONTH);

					// compare the schedule begin, end time calendar day and
					// then generate schedule time
					_scheduleTime = String
							.format(context
									.getString(R.string.scheduleWalkInviteGroup_scheduleTime_format),
									TIMESTAMP_LONG_DATEFORMAT
											.format(_userInputWalkInviteScheduleTimeBeginTimestampValue),
									(_beginTimeDay == _endTimeDay ? TIMESTAMP_SHORT_DATEFORMAT
											: TIMESTAMP_LONG_DATEFORMAT)
											.format(_defaultCalendar
													.getTimeInMillis()));
				} catch (NumberFormatException e) {
					LOGGER.warning("Show walk invite schedule time error, because it is not set");

					e.printStackTrace();
				}
			} else {
				LOGGER.error("Walk invite schedule time error, the schedule time = "
						+ scheduleTime);
			}
		} else {
			LOGGER.error("Walk invite schedule time is null");
		}

		return _scheduleTime;
	}

	// inner class
	/**
	 * @name GroupInviteInfo4SettingListViewAdapterKey
	 * @descriptor walk or within group compete invite info for setting listView
	 *             adapter key enumeration
	 * @author Ares
	 * @version 1.0
	 */
	public enum GroupInviteInfo4SettingListViewAdapterKey {

		// walk or within group compete invite info label, value hint and value
		// key
		GROUPINVITEINFO_LABEL_KEY, GROUPINVITEINFO_VALUEHINT_KEY, GROUPINVITEINFO_VALUE_KEY;

	}

	/**
	 * @name GroupInviteInfo4SettingListViewAdapter
	 * @descriptor walk or within group compete invite info for setting listView
	 *             adapter view binder
	 * @author Ares
	 * @version 1.0
	 */
	class GroupInviteInfo4SettingListViewAdapterViewBinder extends
			SSSimpleAdapterViewBinder {

		@Override
		public boolean setViewValue(View view, Object data,
				String textRepresentation) {
			boolean _ret = false;

			// check view type
			// textView with its hint string resource id
			if (view instanceof TextView && data instanceof Integer) {
				// convert data to integer then set the textView hint
				((TextView) view).setHint((Integer) data);

				// update return flag
				_ret = true;
			} else {
				// update return flag
				_ret = super.setViewValue(view, data, textRepresentation);
			}

			return _ret;
		}

	}

}
