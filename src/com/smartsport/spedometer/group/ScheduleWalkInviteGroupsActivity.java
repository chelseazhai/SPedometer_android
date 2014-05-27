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
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.SSApplication;
import com.smartsport.spedometer.customwidget.SSBNavImageBarButtonItem;
import com.smartsport.spedometer.group.ScheduleWalkInviteGroupsActivity.ScheduleWalkInviteGroupListViewAdapter.ScheduleWalkInviteGroupListViewAdapterKey;
import com.smartsport.spedometer.group.walk.WalkInviteInfoSettingActivity;
import com.smartsport.spedometer.group.walk.WalkInviteInfoSettingActivity.WalkInviteInfoSettingExtraData;
import com.smartsport.spedometer.group.walk.WalkInviteInviteeSelectActivity;
import com.smartsport.spedometer.group.walk.WalkInviteWalkActivity;
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.mvc.ISSBaseActivityResult;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.user.UserInfoBean;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name ScheduleWalkInviteGroupsActivity
 * @descriptor smartsport schedule walk invite group list activity
 * @author Ares
 * @version 1.0
 */
public class ScheduleWalkInviteGroupsActivity extends SSBaseActivity {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			ScheduleWalkInviteGroupsActivity.class);

	// group info model
	private GroupInfoModel groupInfoModel;

	// schedule walk invite group list
	private List<GroupBean> scheduleWalkInviteGroupList;

	// schedule walk invite group listView adapter
	private ScheduleWalkInviteGroupListViewAdapter scheduleWalkInviteGroupListViewAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// initialize group info model and schedule walk invite group list
		groupInfoModel = new GroupInfoModel();
		scheduleWalkInviteGroupList = new ArrayList<GroupBean>();

		// test by ares
		for (int i = 0; i < 10; i++) {
			GroupBean _group = new GroupBean();

			GroupInviteInfoBean _inviteInfo = new GroupInviteInfoBean(null);
			_inviteInfo.setBeginTime(System.currentTimeMillis() / 1000L + 100
					* (i + 1));
			_inviteInfo
					.setDuration(0 == i ? 3000 : 0 == i % 2 ? 150000 : 50000);
			_inviteInfo.setEndTime(_inviteInfo.getBeginTime()
					+ _inviteInfo.getDuration());
			_inviteInfo.setTopic("欢迎加入我们的约走组" + i);

			_group.setGroupId(10000 + i);
			_group.setInviteInfo(_inviteInfo);
			_group.setType(GroupType.WALK_GROUP);
			_group.setMemberNumber(2);

			scheduleWalkInviteGroupList.add(_group);
		}

		// set content view
		setContentView(R.layout.activity_schedule_walkinvite_groups);

		// get schedule walk invite group list from remote server
		groupInfoModel.getUserScheduleGroups(123123, "token",
				GroupType.WALK_GROUP, new ICMConnector() {

					//

				});
	}

	@Override
	protected void initContentViewUI() {
		// navigation bar
		// set navigation bar color
		setNavbarBackgroundColor(getResources().getColor(
				android.R.color.holo_green_light));

		// set right bar button item
		setRightBarButtonItem(new SSBNavImageBarButtonItem(this,
				R.drawable.img_new_walkinvite_group,
				new NewWalkInviteGroupBarBtnItemOnClickListener()));

		// set title attributes
		setTitle(R.string.scheduleWalkInviteGroups_activity_title);
		setTitleColor(Color.WHITE);
		setTitleSize(22.0f);
		setShadow(1.0f, 0.6f, 0.8f, Color.GRAY);

		// get schedule walk invite group listView
		ListView _scheduleWalkInviteGroupListView = (ListView) findViewById(R.id.swigs_scheduleGroups_listView);

		// set its adapter
		_scheduleWalkInviteGroupListView
				.setAdapter(scheduleWalkInviteGroupListViewAdapter = new ScheduleWalkInviteGroupListViewAdapter(
						this,
						scheduleWalkInviteGroupList,
						R.layout.schedulewalkinvitegroup_listview_item_layout,
						new String[] {
								ScheduleWalkInviteGroupListViewAdapterKey.WALKINVITEGROUP_TOPIC_KEY
										.name(),
								ScheduleWalkInviteGroupListViewAdapterKey.WALKINVITEGROUP_SCHEDULETIME_KEY
										.name(),
								ScheduleWalkInviteGroupListViewAdapterKey.WALKINVITEGROUP_STATUS_KEY
										.name() },
						new int[] {
								R.id.swigi_walkInviteGroupTopic_textView,
								R.id.swigi_walkInviteGroupScheduleTime_textView,
								R.id.swigi_walkInviteGroupStatus_textView }));

		// set its on item click listener
		_scheduleWalkInviteGroupListView
				.setOnItemClickListener(new ScheduleWalkInviteGroupOnItemClickListener());
	}

	// inner class
	/**
	 * @name ScheduleWalkInviteGroupsRequestCode
	 * @descriptor schedule walk invite groups request code
	 * @author Ares
	 * @version 1.0
	 */
	class ScheduleWalkInviteGroupsRequestCode {

		// select walk invite invitee request code
		private static final int SWIG_SELECT_WALKINVITEINVITEE_REQCODE = 3000;

	}

	/**
	 * @name WalkInviteInviteeSelectExtraData
	 * @descriptor walk invite invitee select extra data constant
	 * @author Ares
	 * @version 1.0
	 */
	public static final class WalkInviteInviteeSelectExtraData {

		// selected walk invite invitee info bean
		public static final String WIIS_SELECTED_INVITEE_BEAN = "walkInviteInviteeSelect_selected_walkInviteInvitee_bean";

	}

	/**
	 * @name WIISSelectInviteeOnActivityResult
	 * @descriptor walk invite invitee select select invitee on activity result
	 * @author Ares
	 * @version 1.0
	 */
	class WIISSelectInviteeOnActivityResult implements ISSBaseActivityResult {

		@Override
		public void onActivityResult(int resultCode, Intent data) {
			// check the result code
			if (RESULT_OK == resultCode) {
				// get and check the extra data
				Bundle _extraData = data.getExtras();
				if (null != _extraData) {
					// get and check the selected walk invite invitee bean
					UserInfoBean _selectedInviteeBean = (UserInfoBean) _extraData
							.getSerializable(WalkInviteInviteeSelectExtraData.WIIS_SELECTED_INVITEE_BEAN);
					if (null != _selectedInviteeBean) {
						// define walk invite info setting extra data map
						final Map<String, Object> _extraMap = new HashMap<String, Object>();

						// put selected walk invite invitee bean to extra data
						// map as param
						_extraMap
								.put(WalkInviteInfoSettingExtraData.WIIS_SELECTED_INVITEE_BEAN,
										_selectedInviteeBean);

						// go to walk invite info setting activity with extra
						// data map delayed
						new Handler().postDelayed(
								new Runnable() {

									@Override
									public void run() {
										pushActivity(
												WalkInviteInfoSettingActivity.class,
												_extraMap);
									}

								},
								getResources().getInteger(
										R.integer.config_activityAnimTime) + 30/*
																				 * eye
																				 * residual
																				 * time
																				 */);
					} else {
						LOGGER.error("Select walk invite invitee error, the selected walk invite invitee is null");
					}
				} else {
					LOGGER.error("Select walk invite invitee error, the return extra data is null");
				}
			}
		}

	}

	/**
	 * @name NewWalkInviteGroupBarBtnItemOnClickListener
	 * @descriptor new schedule walk invite group bar button item on click
	 *             listener
	 * @author Ares
	 * @version 1.0
	 */
	class NewWalkInviteGroupBarBtnItemOnClickListener implements
			OnClickListener {

		@Override
		public void onClick(View v) {
			// go to walk invite invitee select activity
			presentActivityForResult(
					WalkInviteInviteeSelectActivity.class,
					null,
					ScheduleWalkInviteGroupsRequestCode.SWIG_SELECT_WALKINVITEINVITEE_REQCODE,
					new WIISSelectInviteeOnActivityResult());
		}

	}

	/**
	 * @name ScheduleWalkInviteGroupListViewAdapter
	 * @descriptor schedule walk invite group listView adapter
	 * @author Ares
	 * @version 1.0
	 */
	static class ScheduleWalkInviteGroupListViewAdapter extends SimpleAdapter {

		// logger
		private static final SSLogger LOGGER = new SSLogger(
				ScheduleWalkInviteGroupListViewAdapter.class);

		// milliseconds per second
		private final int MILLISECONDS_PER_SECOND = 1000;

		// timestamp long and short date format
		@SuppressLint("SimpleDateFormat")
		private final SimpleDateFormat TIMESTAMP_LONG_DATEFORMAT = new SimpleDateFormat(
				SSApplication.getContext().getString(R.string.long_dateFormat));
		@SuppressLint("SimpleDateFormat")
		private final SimpleDateFormat TIMESTAMP_SHORT_DATEFORMAT = new SimpleDateFormat(
				SSApplication.getContext().getString(R.string.short_dateFormat));

		// context
		private Context context;

		// schedule walk invite group listView adapter data list
		private static List<Map<String, Object>> _sDataList;

		/**
		 * @title ScheduleWalkInviteGroupListViewAdapter
		 * @descriptor schedule walk invite group listView adapter constructor
		 *             with context, schedule walk invite group list, content
		 *             view resource, content view subview data key, and content
		 *             view subview id
		 * @param context
		 *            : context
		 * @param scheduleWalkInviteGroups
		 *            : schedule walk invite group list
		 * @param resource
		 *            : resource id
		 * @param dataKeys
		 *            : content view subview data key array
		 * @param ids
		 *            : content view subview id array
		 */
		public ScheduleWalkInviteGroupListViewAdapter(Context context,
				List<GroupBean> scheduleWalkInviteGroups, int resource,
				String[] dataKeys, int[] ids) {
			super(context, _sDataList = new ArrayList<Map<String, Object>>(),
					resource, dataKeys, ids);

			// save context
			this.context = context;

			// set schedule walk invite group list
			setScheduleWalkInviteGroups(scheduleWalkInviteGroups);
		}

		/**
		 * @title setScheduleWalkInviteGroups
		 * @descriptor set new schedule walk invite group list
		 * @param scheduleWalkInviteGroups
		 *            : new schedule walk invite group list
		 */
		public void setScheduleWalkInviteGroups(
				List<GroupBean> scheduleWalkInviteGroups) {
			// check new schedule walk invite group list
			if (null != scheduleWalkInviteGroups) {
				// clear schedule walk invite group listView adapter data list
				// and add new data
				_sDataList.clear();

				// traversal new schedule walk invite group list
				for (GroupBean scheduleWalkInviteGroup : scheduleWalkInviteGroups) {
					// define schedule walk invite group listView adapter data
					Map<String, Object> _data = new HashMap<String, Object>();

					// get schedule walk invite group invite info
					GroupInviteInfoBean _scheduleWalkInviteInviteInfo = scheduleWalkInviteGroup
							.getInviteInfo();

					// generate holo green dark foreground color span
					ForegroundColorSpan _holoGreenDarkForegroundColorSpan = new ForegroundColorSpan(
							context.getResources().getColor(
									android.R.color.holo_green_dark));

					// check schedule walk invite group status
					SpannableString _status = new SpannableString("即将开始");
					_status.setSpan(_holoGreenDarkForegroundColorSpan, 0,
							_status.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

					// set data attributes
					_data.put(
							ScheduleWalkInviteGroupListViewAdapterKey.WALKINVITEGROUP_TOPIC_KEY
									.name(), _scheduleWalkInviteInviteInfo
									.getTopic());
					_data.put(
							ScheduleWalkInviteGroupListViewAdapterKey.WALKINVITEGROUP_SCHEDULETIME_KEY
									.name(),
							getScheduleTime(
									_scheduleWalkInviteInviteInfo
											.getBeginTime()
											* MILLISECONDS_PER_SECOND,
									_scheduleWalkInviteInviteInfo.getEndTime()
											* MILLISECONDS_PER_SECOND));
					_data.put(
							ScheduleWalkInviteGroupListViewAdapterKey.WALKINVITEGROUP_STATUS_KEY
									.name(), _status);

					// add data to list
					_sDataList.add(_data);
				}

				// notify data set changed
				notifyDataSetChanged();
			} else {
				LOGGER.error("Set new schedule walk invite group list error, the new set data list is null");
			}
		}

		/**
		 * @title getScheduleTime
		 * @descriptor get schedule walk invite group schedule time
		 * @param scheduleBeginTime
		 *            : schedule walk invite group schedule begin time
		 * @param scheduleEndTime
		 *            : schedule walk invite group schedule end time
		 * @return schedule walk invite group schedule time format
		 */
		private String getScheduleTime(long scheduleBeginTime,
				long scheduleEndTime) {
			String _scheduleTime = "";

			// check schedule begin and end time
			if (scheduleEndTime >= scheduleBeginTime) {
				// get default locale and default timeZone calendar
				Calendar _defaultCalendar = Calendar.getInstance();

				// define and get begin, end time calendar day
				_defaultCalendar.setTimeInMillis(scheduleBeginTime);
				int _beginTimeDay = _defaultCalendar.get(Calendar.DAY_OF_MONTH);
				_defaultCalendar.setTimeInMillis(scheduleEndTime);
				int _endTimeDay = _defaultCalendar.get(Calendar.DAY_OF_MONTH);

				// compare the schedule begin, end time calendar day and then
				// generate schedule time
				_scheduleTime = String
						.format(context
								.getString(R.string.scheduleWalkInviteGroup_scheduleTime_format),
								TIMESTAMP_LONG_DATEFORMAT
										.format(scheduleBeginTime),
								(_beginTimeDay == _endTimeDay ? TIMESTAMP_SHORT_DATEFORMAT
										: TIMESTAMP_LONG_DATEFORMAT)
										.format(scheduleEndTime));
			} else {
				LOGGER.error("Schedule walk invite group schedule time error, end time = "
						+ scheduleEndTime
						+ " is less than begin time = "
						+ scheduleBeginTime);
			}

			return _scheduleTime;
		}

		// inner class
		/**
		 * @name ScheduleWalkInviteGroupListViewAdapterKey
		 * @descriptor schedule walk invite group listView adapter key
		 *             enumeration
		 * @author Ares
		 * @version 1.0
		 */
		public enum ScheduleWalkInviteGroupListViewAdapterKey {

			// schedule walk invite group topic, schedule time(begin and end
			// time) and status key
			WALKINVITEGROUP_TOPIC_KEY, WALKINVITEGROUP_SCHEDULETIME_KEY, WALKINVITEGROUP_STATUS_KEY;

		}

	}

	/**
	 * @name ScheduleWalkInviteGroupOnItemClickListener
	 * @descriptor schedule walk invite group item on click listener
	 * @author Ares
	 * @version 1.0
	 */
	class ScheduleWalkInviteGroupOnItemClickListener implements
			OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub

			// define schedule walk invite group item extra data map
			Map<String, Object> _extraMap = new HashMap<String, Object>();

			// put the selected schedule walk invite group info to extra data
			// map as param
			//

			// go to walk invite walk activity with extra data map
			pushActivity(WalkInviteWalkActivity.class, _extraMap);
		}

	}

}
