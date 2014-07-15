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
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.SSApplication;
import com.smartsport.spedometer.customwidget.SSBNavImageBarButtonItem;
import com.smartsport.spedometer.customwidget.SSProgressDialog;
import com.smartsport.spedometer.customwidget.SSSimpleAdapterViewBinder;
import com.smartsport.spedometer.group.ScheduleWalkInviteGroupsActivity.ScheduleWalkInviteGroupListViewAdapter.ScheduleWalkInviteGroupListViewAdapterKey;
import com.smartsport.spedometer.group.walk.WalkInviteInfoSettingActivity;
import com.smartsport.spedometer.group.walk.WalkInviteInfoSettingActivity.WalkInviteInfoSettingExtraData;
import com.smartsport.spedometer.group.walk.WalkInviteInviteeSelectActivity;
import com.smartsport.spedometer.group.walk.WalkInviteWalkActivity;
import com.smartsport.spedometer.group.walk.WalkInviteWalkActivity.WalkInviteWalkExtraData;
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.mvc.ISSBaseActivityResult;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.user.UserManager;
import com.smartsport.spedometer.user.UserPedometerExtBean;
import com.smartsport.spedometer.user.info.UserInfoBean;
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
	private GroupInfoModel groupInfoModel = GroupInfoModel.getInstance();

	// schedule walk invite group list
	private List<GroupBean> scheduleWalkInviteGroupList;

	// schedule walk invite group listView adapter
	private ScheduleWalkInviteGroupListViewAdapter scheduleWalkInviteGroupListViewAdapter;

	// schedule walk invite group progress dialog
	private SSProgressDialog scheduleWalkInviteGroupProgDlg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// initialize schedule walk invite group list
		scheduleWalkInviteGroupList = new ArrayList<GroupBean>();

		// // test by ares
		// for (int i = 0; i < 10; i++) {
		// GroupBean _group = new GroupBean();
		//
		// GroupInviteInfoBean _inviteInfo = new GroupInviteInfoBean(null);
		// _inviteInfo.setBeginTime(System.currentTimeMillis() / 1000L
		// + (0 == i ? 0 : 100 * i));
		// _inviteInfo.setDuration(0 == i ? 300 : (1 == i ? 100
		// : 0 == i % 2 ? 150000 : 50000));
		// _inviteInfo.setEndTime(_inviteInfo.getBeginTime()
		// + _inviteInfo.getDuration());
		// _inviteInfo.setTopic("欢迎加入我们的约走组" + i);
		//
		// _group.setGroupId("10000" + i);
		// _group.setInviteInfo(_inviteInfo);
		// _group.setType(GroupType.WALK_GROUP);
		// _group.setMemberNumber(2);
		//
		// scheduleWalkInviteGroupList.add(_group);
		// }

		// set content view
		setContentView(R.layout.activity_schedule_walkinvite_groups);

		// get pedometer login user
		UserPedometerExtBean _loginUser = (UserPedometerExtBean) UserManager
				.getInstance().getLoginUser();

		// show get schedule walk invite groups progress dialog
		scheduleWalkInviteGroupProgDlg = SSProgressDialog.show(this,
				R.string.procMsg_getScheduleWalkInviteGroups);

		// get schedule walk invite group list from remote server
		groupInfoModel.getUserScheduleGroups(_loginUser.getUserId(),
				_loginUser.getUserKey(), GroupType.WALK_GROUP,
				new ICMConnector() {

					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(Object... retValue) {
						// dismiss get schedule walk invite groups progress
						// dialog
						scheduleWalkInviteGroupProgDlg.dismiss();

						// check return values
						if (null != retValue && 1 < retValue.length) {
							// get and check group type, the update walk invite
							// group list
							if (retValue[0] instanceof GroupType) {
								if (GroupType.WALK_GROUP == (GroupType) retValue[0]
										&& retValue[retValue.length - 1] instanceof List) {
									// reset schedule walk invite group list
									scheduleWalkInviteGroupList.clear();
									scheduleWalkInviteGroupList
											.addAll((List<GroupBean>) retValue[retValue.length - 1]);
									scheduleWalkInviteGroupListViewAdapter
											.setScheduleWalkInviteGroups(scheduleWalkInviteGroupList);
								}
							}
						} else {
							LOGGER.error("Update schedule walk invite groups UI error");
						}
					}

					@Override
					public void onFailure(int errorCode, String errorMsg) {
						LOGGER.error("Get schedule walk invite groups from remote server error, error code = "
								+ errorCode + " and message = " + errorMsg);

						// dismiss get schedule walk invite groups progress
						// dialog
						scheduleWalkInviteGroupProgDlg.dismiss();

						// check error code and process hopeRun business error
						if (errorCode < 100) {
							// show error message toast
							Toast.makeText(
									ScheduleWalkInviteGroupsActivity.this,
									errorMsg, Toast.LENGTH_SHORT).show();

							// test by ares
							//
						}
					}

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
								ScheduleWalkInviteGroupListViewAdapterKey.WALKINVITEGROUP_TOPIC_LABEL_KEY
										.name(),
								ScheduleWalkInviteGroupListViewAdapterKey.WALKINVITEGROUP_TOPIC_KEY
										.name(),
								ScheduleWalkInviteGroupListViewAdapterKey.WALKINVITEGROUP_SCHEDULETIME_LABEL_KEY
										.name(),
								ScheduleWalkInviteGroupListViewAdapterKey.WALKINVITEGROUP_SCHEDULETIME_KEY
										.name(),
								ScheduleWalkInviteGroupListViewAdapterKey.WALKINVITEGROUP_STATUS_LABEL_KEY
										.name(),
								ScheduleWalkInviteGroupListViewAdapterKey.WALKINVITEGROUP_STATUS_KEY
										.name() },
						new int[] {
								R.id.swigi_walkInviteGroupTopic_label_textView,
								R.id.swigi_walkInviteGroupTopic_textView,
								R.id.swigi_walkInviteGroupScheduleTime_label_textView,
								R.id.swigi_walkInviteGroupScheduleTime_textView,
								R.id.swigi_walkInviteGroupStatus_label_textView,
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

		// walk invite walk control request code
		private static final int SWIG_WALK_CONTROL_REQCODE = 3010;

	}

	/**
	 * @name WalkInviteInviteeSelectOrWalkControlExtraData
	 * @descriptor walk invite invitee select extra data constant
	 * @author Ares
	 * @version 1.0
	 */
	public static final class WalkInviteInviteeSelectOrWalkControlExtraData {

		// selected walk invite invitee info bean
		public static final String WIIS_SELECTED_INVITEE_BEAN = "walkInviteInviteeSelect_selected_walkInviteInvitee_bean";

		// start or stop walking flag
		public static final String WIWC_WALK_STARTORSTOP_FLAG = "walkInviteWalkControl_walk_startOrStop_flag";

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
							.getSerializable(WalkInviteInviteeSelectOrWalkControlExtraData.WIIS_SELECTED_INVITEE_BEAN);
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
	 * @name WIWWalkControlOnActivityResult
	 * @descriptor walk invite walk Walk control on activity result
	 * @author Ares
	 * @version 1.0
	 */
	class WIWWalkControlOnActivityResult implements ISSBaseActivityResult {

		@Override
		public void onActivityResult(int resultCode, Intent data) {
			// check the result code
			if (RESULT_OK == resultCode) {
				// get and check the extra data
				Bundle _extraData = data.getExtras();
				if (null != _extraData) {
					// get and check walk start or stop flag
					if (_extraData
							.getBoolean(WalkInviteInviteeSelectOrWalkControlExtraData.WIWC_WALK_STARTORSTOP_FLAG)) {
						LOGGER.info("@@@@");

						//
					}
				} else {
					LOGGER.error("Walk control error, the return extra data is null");
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

		// schedule walk invite group id, schedule begin time and end time key
		private static final String WALKINVITEGROUP_ID_KEY = "walkInviteGroup_id_key";
		private static final String WALKINVITEGROUP_SCHEDULEBEGINTIME_KEY = "walkInviteGroup_scheduleBeginTime_key";
		private static final String WALKINVITEGROUP_SCHEDULEENDTIME_KEY = "walkInviteGroup_scheduleEndTime_key";

		// milliseconds per second, seconds per day, hour and minute
		private final int MILLISECONDS_PER_SECOND = 1000;
		private final int SECONDS_PER_DAY = 24 * 60 * 60;
		private final int SECONDS_PER_HOUR = 60 * 60;
		private final int SECONDS_PER_MINUTE = 60;

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

			// set view binder
			setViewBinder(new ScheduleWalkInviteGroupListViewAdapterViewBinder());

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
				for (GroupBean _scheduleWalkInviteGroup : scheduleWalkInviteGroups) {
					// define schedule walk invite group listView adapter data
					Map<String, Object> _data = new HashMap<String, Object>();

					// get schedule walk invite group invite info
					GroupInviteInfoBean _scheduleWalkInviteInviteInfo = _scheduleWalkInviteGroup
							.getInviteInfo();

					// get schedule walk invite group begin and end timestamp
					long _scheduleWalkInviteGroupBeginTimestamp = _scheduleWalkInviteInviteInfo
							.getBeginTime() * MILLISECONDS_PER_SECOND;
					long _scheduleWalkInviteGroupEndTimestamp = _scheduleWalkInviteInviteInfo
							.getEndTime() * MILLISECONDS_PER_SECOND;

					// get schedule walk invite group status format spannable
					// string builder
					SpannableStringBuilder _scheduleWalkInviteGroupStatusSpannableStringBuilder = formatGroupStatus(
							_scheduleWalkInviteGroupBeginTimestamp,
							_scheduleWalkInviteGroupEndTimestamp);

					// set data attributes
					_data.put(WALKINVITEGROUP_ID_KEY,
							_scheduleWalkInviteGroup.getGroupId());
					_data.put(
							ScheduleWalkInviteGroupListViewAdapterKey.WALKINVITEGROUP_TOPIC_LABEL_KEY
									.name(),
							context.getString(R.string.scheduleWalkInviteGroup_topic_label));
					_data.put(
							ScheduleWalkInviteGroupListViewAdapterKey.WALKINVITEGROUP_TOPIC_KEY
									.name(), _scheduleWalkInviteInviteInfo
									.getTopic());
					_data.put(WALKINVITEGROUP_SCHEDULEBEGINTIME_KEY,
							_scheduleWalkInviteGroupBeginTimestamp);
					_data.put(WALKINVITEGROUP_SCHEDULEENDTIME_KEY,
							_scheduleWalkInviteGroupEndTimestamp);
					_data.put(
							ScheduleWalkInviteGroupListViewAdapterKey.WALKINVITEGROUP_SCHEDULETIME_LABEL_KEY
									.name(),
							context.getString(R.string.scheduleWalkInviteGroup_scheduleTime_label));
					_data.put(
							ScheduleWalkInviteGroupListViewAdapterKey.WALKINVITEGROUP_SCHEDULETIME_KEY
									.name(),
							getScheduleTime(
									_scheduleWalkInviteGroupBeginTimestamp,
									_scheduleWalkInviteGroupEndTimestamp));
					_data.put(
							ScheduleWalkInviteGroupListViewAdapterKey.WALKINVITEGROUP_STATUS_LABEL_KEY
									.name(),
							context.getString(R.string.scheduleWalkInviteGroup_status_label));
					_data.put(
							ScheduleWalkInviteGroupListViewAdapterKey.WALKINVITEGROUP_STATUS_KEY
									.name(),
							_scheduleWalkInviteGroupStatusSpannableStringBuilder);

					// get and check schedule walk invite group status format
					// spannable string builder foreground color spans array
					ForegroundColorSpan[] _foregroundColorSpans = _scheduleWalkInviteGroupStatusSpannableStringBuilder
							.getSpans(0,
									_scheduleWalkInviteGroupStatusSpannableStringBuilder
											.length(),
									ForegroundColorSpan.class);
					if (null != _foregroundColorSpans
							&& 0 < _foregroundColorSpans.length
							&& context.getResources().getColor(
									R.color.holo_green_middle) == _foregroundColorSpans[0]
									.getForegroundColor()) {
						// generate holo green dark foreground color span
						ForegroundColorSpan _holoGreenDarkForegroundColorSpan = new ForegroundColorSpan(
								context.getResources().getColor(
										android.R.color.holo_green_dark));

						// define schedule walk invite group topic, schedule
						// time, their label and status label spannable string
						SpannableString _scheduleWalkInviteGroupTopicLabelSpannableString = new SpannableString(
								(CharSequence) _data
										.get(ScheduleWalkInviteGroupListViewAdapterKey.WALKINVITEGROUP_TOPIC_LABEL_KEY
												.name()));
						SpannableString _scheduleWalkInviteGroupTopicSpannableString = new SpannableString(
								_scheduleWalkInviteInviteInfo.getTopic());
						SpannableString _scheduleWalkInviteGroupScheduleTimeLabelSpannableString = new SpannableString(
								(CharSequence) _data
										.get(ScheduleWalkInviteGroupListViewAdapterKey.WALKINVITEGROUP_SCHEDULETIME_LABEL_KEY
												.name()));
						SpannableString _scheduleWalkInviteGroupScheduleTimeSpannableString = new SpannableString(
								(CharSequence) _data
										.get(ScheduleWalkInviteGroupListViewAdapterKey.WALKINVITEGROUP_SCHEDULETIME_KEY
												.name()));
						SpannableString _scheduleWalkInviteGroupStatusLabelSpannableString = new SpannableString(
								(CharSequence) _data
										.get(ScheduleWalkInviteGroupListViewAdapterKey.WALKINVITEGROUP_STATUS_LABEL_KEY
												.name()));

						// set their foreground color span
						_scheduleWalkInviteGroupTopicLabelSpannableString
								.setSpan(_holoGreenDarkForegroundColorSpan, 0,
										_scheduleWalkInviteGroupTopicLabelSpannableString
												.length(),
										Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						_scheduleWalkInviteGroupTopicSpannableString.setSpan(
								_foregroundColorSpans[0], 0,
								_scheduleWalkInviteGroupTopicSpannableString
										.length(),
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						_scheduleWalkInviteGroupScheduleTimeLabelSpannableString
								.setSpan(_holoGreenDarkForegroundColorSpan, 0,
										_scheduleWalkInviteGroupScheduleTimeLabelSpannableString
												.length(),
										Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						_scheduleWalkInviteGroupScheduleTimeSpannableString
								.setSpan(_foregroundColorSpans[0], 0,
										_scheduleWalkInviteGroupScheduleTimeSpannableString
												.length(),
										Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						_scheduleWalkInviteGroupStatusLabelSpannableString
								.setSpan(_holoGreenDarkForegroundColorSpan, 0,
										_scheduleWalkInviteGroupStatusLabelSpannableString
												.length(),
										Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

						// add schedule walk invite group topic, schedule time,
						// status label and update topic, schedule time
						_data.put(
								ScheduleWalkInviteGroupListViewAdapterKey.WALKINVITEGROUP_TOPIC_LABEL_KEY
										.name(),
								_scheduleWalkInviteGroupTopicLabelSpannableString);
						_data.put(
								ScheduleWalkInviteGroupListViewAdapterKey.WALKINVITEGROUP_TOPIC_KEY
										.name(),
								_scheduleWalkInviteGroupTopicSpannableString);
						_data.put(
								ScheduleWalkInviteGroupListViewAdapterKey.WALKINVITEGROUP_SCHEDULETIME_LABEL_KEY
										.name(),
								_scheduleWalkInviteGroupScheduleTimeLabelSpannableString);
						_data.put(
								ScheduleWalkInviteGroupListViewAdapterKey.WALKINVITEGROUP_SCHEDULETIME_KEY
										.name(),
								_scheduleWalkInviteGroupScheduleTimeSpannableString);
						_data.put(
								ScheduleWalkInviteGroupListViewAdapterKey.WALKINVITEGROUP_STATUS_LABEL_KEY
										.name(),
								_scheduleWalkInviteGroupStatusLabelSpannableString);
					}

					// add data to list
					_sDataList.add(_data);
				}

				// notify data set changed
				notifyDataSetChanged();
			} else {
				LOGGER.error("Set new schedule walk invite group list error, the new set data list is null");
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public Map<String, ?> getItem(int position) {
			return (Map<String, ?>) super.getItem(position);
		}

		/**
		 * @title getGroupId
		 * @descriptor get the selected schedule walk invite group item group id
		 * @param position
		 *            : schedule walk invite group listView selected item
		 *            position
		 * @return the selected schedule walk invite group item group id
		 * @author Ares
		 */
		public String getGroupId(int position) {
			// return the schedule walk invite group id with position
			return (String) getItem(position).get(WALKINVITEGROUP_ID_KEY);
		}

		/**
		 * @title getGroupTopic
		 * @descriptor get the selected schedule walk invite group item topic
		 *             textView text
		 * @param position
		 *            : schedule walk invite group listView selected item
		 *            position
		 * @return the selected schedule walk invite group item topic
		 * @author Ares
		 */
		public String getGroupTopic(int position) {
			// return the schedule walk invite group topic with position
			return getItem(position)
					.get(ScheduleWalkInviteGroupListViewAdapterKey.WALKINVITEGROUP_TOPIC_KEY
							.name()).toString();
		}

		/**
		 * @title getGroupScheduleBeginTime
		 * @descriptor get the selected schedule walk invite group item schedule
		 *             begin time
		 * @param position
		 *            : schedule walk invite group listView selected item
		 *            position
		 * @return the selected schedule walk invite group item schedule begin
		 *         time
		 * @author Ares
		 */
		public Long getGroupScheduleBeginTime(int position) {
			// return the schedule walk invite group schedule begin time with
			// position
			return (Long) getItem(position).get(
					WALKINVITEGROUP_SCHEDULEBEGINTIME_KEY);
		}

		/**
		 * @title getGroupScheduleEndTime
		 * @descriptor get the selected schedule walk invite group item schedule
		 *             end time
		 * @param position
		 *            : schedule walk invite group listView selected item
		 *            position
		 * @return the selected schedule walk invite group item schedule end
		 *         time
		 * @author Ares
		 */
		public Long getGroupScheduleEndTime(int position) {
			// return the schedule walk invite group schedule end time with
			// position
			return (Long) getItem(position).get(
					WALKINVITEGROUP_SCHEDULEENDTIME_KEY);
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

		/**
		 * @title formatGroupStatus
		 * @descriptor format schedule walk invite group status
		 * @param scheduleBeginTime
		 *            : schedule walk invite group schedule begin time
		 * @param scheduleEndTime
		 *            : schedule walk invite group schedule end time
		 * @return schedule walk invite group status format
		 */
		private SpannableStringBuilder formatGroupStatus(
				long scheduleBeginTime, long scheduleEndTime) {
			// define schedule walk invite group status spannable string builder
			SpannableStringBuilder _groupStatus = new SpannableStringBuilder();

			// generate holo green middle foreground color span
			ForegroundColorSpan _holoGreenMiddleForegroundColorSpan = new ForegroundColorSpan(
					context.getResources().getColor(R.color.holo_green_middle));

			// check schedule walk invite group begin and end time
			if (scheduleBeginTime < scheduleEndTime) {
				// get system current timestamp
				long _currentTimestamp = System.currentTimeMillis();

				// get schedule walk invite group start remain time
				Long _remainTime = (scheduleBeginTime - _currentTimestamp)
						/ MILLISECONDS_PER_SECOND;

				// check it and initialize schedule walk invite group status
				// string format
				if (0 > _remainTime) {
					if (scheduleEndTime < _currentTimestamp) {
						_groupStatus
								.append(context
										.getString(R.string.scheduleWalkInviteGroup_status_invalid));
					} else {
						_groupStatus
								.append(context
										.getString(R.string.scheduleWalkInviteGroup_status_walking));
						_groupStatus.setSpan(
								_holoGreenMiddleForegroundColorSpan, 0,
								_groupStatus.length(),
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				} else {
					// define schedule walk invite group status value
					String _groupStatusValue = null;

					// days
					if (SECONDS_PER_DAY <= _remainTime) {
						_groupStatusValue = _remainTime
								/ SECONDS_PER_DAY
								+ context
										.getResources()
										.getString(
												R.string.scheduleWalkInviteGroup_status_startReaminTime_day_unit);
					}
					// hours
					else if (SECONDS_PER_HOUR <= _remainTime) {
						_groupStatusValue = _remainTime
								/ SECONDS_PER_HOUR
								+ context
										.getResources()
										.getString(
												R.string.scheduleWalkInviteGroup_status_startReaminTime_hour_unit);
					}
					// minutes
					else {
						_groupStatusValue = _remainTime
								/ SECONDS_PER_MINUTE
								+ context
										.getResources()
										.getString(
												R.string.scheduleWalkInviteGroup_status_startReaminTime_minute_unit);
					}

					// generate schedule walk invite group status format
					_groupStatus
							.append(String.format(
									context.getString(R.string.scheduleWalkInviteGroup_status_startReaminTime_format),
									_groupStatusValue));
				}
			} else {
				LOGGER.error("Format schedule walk invite group status error, schedule walk invite group schedule end time = "
						+ scheduleEndTime
						+ " is less than begin time = "
						+ scheduleBeginTime);
			}

			return _groupStatus;
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

			// schedule walk invite group indicate, topic, schedule time(begin
			// and end time), status and their label key
			WALKINVITEGROUP_INDICATE_KEY, WALKINVITEGROUP_TOPIC_LABEL_KEY, WALKINVITEGROUP_TOPIC_KEY, WALKINVITEGROUP_SCHEDULETIME_LABEL_KEY, WALKINVITEGROUP_SCHEDULETIME_KEY, WALKINVITEGROUP_STATUS_LABEL_KEY, WALKINVITEGROUP_STATUS_KEY;

		}

		/**
		 * @name ScheduleWalkInviteGroupListViewAdapter
		 * @descriptor schedule walk invite group listView adapter view binder
		 * @author Ares
		 * @version 1.0
		 */
		class ScheduleWalkInviteGroupListViewAdapterViewBinder extends
				SSSimpleAdapterViewBinder {

			@Override
			public boolean setViewValue(View view, Object data,
					String textRepresentation) {
				boolean _ret = false;

				// check view type
				// textView
				if (view instanceof TextView) {
					// set view text
					if (null != data) {
						((TextView) view)
								.setText(data instanceof Spannable ? (Spannable) data
										: data.toString());
					} else {
						// nothing to do
					}

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
			// define schedule walk invite group item extra data map
			Map<String, Object> _extraMap = new HashMap<String, Object>();

			// put the selected schedule walk invite group id, topic, schedule
			// begin and end time to extra data map as param
			_extraMap
					.put(WalkInviteWalkExtraData.WIW_WALKINVITEGROUP_ID,
							scheduleWalkInviteGroupListViewAdapter
									.getGroupId(position));
			_extraMap.put(WalkInviteWalkExtraData.WIW_WALKINVITEGROUP_TOPIC,
					scheduleWalkInviteGroupListViewAdapter
							.getGroupTopic(position));
			_extraMap
					.put(WalkInviteWalkExtraData.WIW_WALKINVITEGROUP_SCHEDULEBEGINTIME,
							scheduleWalkInviteGroupListViewAdapter
									.getGroupScheduleBeginTime(position));
			_extraMap
					.put(WalkInviteWalkExtraData.WIW_WALKINVITEGROUP_SCHEDULEENDTIME,
							scheduleWalkInviteGroupListViewAdapter
									.getGroupScheduleEndTime(position));

			// go to walk invite walk activity with extra data map
			pushActivityForResult(
					WalkInviteWalkActivity.class,
					_extraMap,
					ScheduleWalkInviteGroupsRequestCode.SWIG_WALK_CONTROL_REQCODE,
					new WIWWalkControlOnActivityResult());
		}

	}

}
