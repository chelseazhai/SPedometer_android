/**
 * 
 */
package com.smartsport.spedometer.history.walk.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.customwidget.SSSimpleAdapterViewBinder;
import com.smartsport.spedometer.group.GroupBean;
import com.smartsport.spedometer.group.GroupInviteInfoBean;
import com.smartsport.spedometer.group.GroupType;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name HistoryGroupListViewAdapter
 * @descriptor history group listView adapter
 * @author Ares
 * @version 1.0
 */
public class HistoryGroupListViewAdapter extends SimpleAdapter {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			HistoryGroupListViewAdapter.class);

	// history group id, type, walk start time and stop time key
	private static final String HISTORYGROUP_ID_KEY = "historyGroup_id_key";
	private static final String HISTORYGROUP_TYPE_KEY = "historyGroup_type_key";
	private static final String HISTORYGROUP_WALKSTARTTIME_KEY = "historyGroup_walkStartTime_key";
	private static final String HISTORYGROUP_WALKSTOPTIME_KEY = "historyGroup_walkStopTime_key";

	// milliseconds per second and seconds per minute
	private final int MILLISECONDS_PER_SECOND = 1000;
	private final int SECONDS_PER_MINUTE = 60;

	// context
	private Context context;

	// history group listView adapter data list
	private static List<Map<String, Object>> _sDataList;

	/**
	 * @title HistoryGroupListViewAdapter
	 * @descriptor walk invite or within group compete history group listView
	 *             adapter constructor with context, history group list, content
	 *             view resource, content view subview data key, and content
	 *             view subview id
	 * @param context
	 *            : context
	 * @param historyGroups
	 *            : walk invite or within group compete history group list
	 * @param resource
	 *            : resource id
	 * @param dataKeys
	 *            : content view subview data key array
	 * @param ids
	 *            : content view subview id array
	 */
	public HistoryGroupListViewAdapter(Context context,
			List<GroupBean> historyGroups, int resource, String[] dataKeys,
			int[] ids) {
		super(context, _sDataList = new ArrayList<Map<String, Object>>(),
				resource, dataKeys, ids);

		// save context
		this.context = context;

		// set view binder
		setViewBinder(new HistoryGroupListViewAdapterViewBinder());

		// set history group list
		setHistoryGroups(historyGroups);
	}

	/**
	 * @title setHistoryGroups
	 * @descriptor set new walk invite or within group compete history group
	 *             list
	 * @param historyGroups
	 *            : new history group list
	 */
	public void setHistoryGroups(List<GroupBean> historyGroups) {
		// check new history group list
		if (null != historyGroups) {
			// clear history group listView adapter data list and add new data
			_sDataList.clear();

			// traversal new history group list
			for (GroupBean _historyGroup : historyGroups) {
				// define history group listView adapter data
				Map<String, Object> _data = new HashMap<String, Object>();

				// get history group type
				GroupType _historyGroupType = _historyGroup.getType();

				// get history group invite info
				GroupInviteInfoBean _historyGroupInviteInfo = _historyGroup
						.getInviteInfo();

				// get history group walk start and stop timestamp
				long _historyGroupWalkStartTimestamp = _historyGroupInviteInfo
						.getBeginTime() * MILLISECONDS_PER_SECOND;
				long _historyGroupWalkStopTimestamp = _historyGroupInviteInfo
						.getEndTime() * MILLISECONDS_PER_SECOND;

				// set data attributes
				_data.put(HISTORYGROUP_ID_KEY, _historyGroup.getGroupId());
				_data.put(HISTORYGROUP_TYPE_KEY, _historyGroupType);
				_data.put(HistoryGroupListViewAdapterKey.HISTORYGROUP_TOPIC_KEY
						.name(), _historyGroupInviteInfo.getTopic());
				_data.put(HISTORYGROUP_WALKSTARTTIME_KEY,
						_historyGroupWalkStartTimestamp);
				_data.put(HISTORYGROUP_WALKSTOPTIME_KEY,
						_historyGroupWalkStopTimestamp);
				_data.put(
						HistoryGroupListViewAdapterKey.HISTORYGROUP_WALKSTARTTIME_KEY
								.name(), "下午 14：22");
				_data.put(
						HistoryGroupListViewAdapterKey.HISTORYGROUP_DURATIONTIME_LABEL_KEY
								.name(),
						String.format(
								context.getString(R.string.historyGroup_durationTime_label_format),
								GroupType.WALK_GROUP == _historyGroupType ? context
										.getString(R.string.historyGroup_walkinvite_durationTime_header)
										: context
												.getString(R.string.historyGroup_withinGroupCompete_durationTime_header)));
				_data.put(
						HistoryGroupListViewAdapterKey.HISTORYGROUP_DURATIONTIME_KEY
								.name(),
						GroupType.WALK_GROUP == _historyGroupType ? "20 秒钟"
								: _historyGroupInviteInfo.getDuration()
										+ context
												.getString(R.string.historyGroup_withinGroupCompete_durationTime_unit));
				_data.put(
						HistoryGroupListViewAdapterKey.HISTORYGROUP_ATTENDEESNUMRELATIVELAYOUT_KEY
								.name(),
						Boolean.valueOf(GroupType.WALK_GROUP == _historyGroupType ? false
								: true));
				_data.put(
						HistoryGroupListViewAdapterKey.HISTORYGROUP_ATTENDEESNUM_KEY
								.name(),
						_historyGroup.getMemberNumber()
								+ context
										.getString(R.string.historyGroup_attendeesNum_unit));

				// add data to list
				_sDataList.add(_data);
			}

			// notify data set changed
			notifyDataSetChanged();
		} else {
			LOGGER.error("Set new history group list error, the new set data list is null");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, ?> getItem(int position) {
		return (Map<String, ?>) super.getItem(position);
	}

	/**
	 * @title getGroupId
	 * @descriptor get the selected history group item group id
	 * @param position
	 *            : history group listView selected item position
	 * @return the selected history group item group id
	 * @author Ares
	 */
	public String getGroupId(int position) {
		// return the history group id with position
		return (String) getItem(position).get(HISTORYGROUP_ID_KEY);
	}

	/**
	 * @title getGroupType
	 * @descriptor get the selected history group item group type
	 * @param position
	 *            : history group listView selected item position
	 * @return the selected history group item group type
	 * @author Ares
	 */
	public GroupType getGroupType(int position) {
		// return the history group type with position
		return (GroupType) getItem(position).get(HISTORYGROUP_TYPE_KEY);
	}

	/**
	 * @title getGroupTopic
	 * @descriptor get the selected history group item topic textView text
	 * @param position
	 *            : history group listView selected item position
	 * @return the selected history group item topic
	 * @author Ares
	 */
	public String getGroupTopic(int position) {
		// return the history group topic with position
		return getItem(position).get(
				HistoryGroupListViewAdapterKey.HISTORYGROUP_TOPIC_KEY.name())
				.toString();
	}

	/**
	 * @title getGroupWalkStartTime
	 * @descriptor get the selected history group item walk start time
	 * @param position
	 *            : history group listView selected item position
	 * @return the selected history group item walk start time
	 * @author Ares
	 */
	public Long getGroupWalkStartTime(int position) {
		// return the history group walk start time with position
		return (Long) getItem(position).get(HISTORYGROUP_WALKSTARTTIME_KEY);
	}

	/**
	 * @title getGroupWalkStopTime
	 * @descriptor get the selected history group item walk stop time
	 * @param position
	 *            : history group listView selected item position
	 * @return the selected history group item walk stop time
	 * @author Ares
	 */
	public Long getGroupWalkStopTime(int position) {
		// return the history group walk stop time with position
		return (Long) getItem(position).get(HISTORYGROUP_WALKSTOPTIME_KEY);
	}

	// inner class
	/**
	 * @name HistoryGroupListViewAdapterKey
	 * @descriptor walk invite or within group compete history group listView
	 *             adapter key enumeration
	 * @author Ares
	 * @version 1.0
	 */
	public enum HistoryGroupListViewAdapterKey {

		// history group topic, walk start time, duration time, duration time
		// label, attendees number relativeLayout and attendees number key
		HISTORYGROUP_TOPIC_KEY, HISTORYGROUP_WALKSTARTTIME_KEY, HISTORYGROUP_DURATIONTIME_LABEL_KEY, HISTORYGROUP_DURATIONTIME_KEY, HISTORYGROUP_ATTENDEESNUMRELATIVELAYOUT_KEY, HISTORYGROUP_ATTENDEESNUM_KEY;

	}

	/**
	 * @name HistoryGroupListViewAdapterViewBinder
	 * @descriptor walk invite or within group compete history group listView
	 *             adapter view binder
	 * @author Ares
	 * @version 1.0
	 */
	class HistoryGroupListViewAdapterViewBinder extends
			SSSimpleAdapterViewBinder {

		@Override
		public boolean setViewValue(View view, Object data,
				String textRepresentation) {
			boolean _ret = false;

			// check view type
			// relativeLayout
			if (view instanceof RelativeLayout && data instanceof Boolean) {
				// set relativeLayout visibility
				((RelativeLayout) view)
						.setVisibility((Boolean) data ? View.VISIBLE
								: View.GONE);

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
