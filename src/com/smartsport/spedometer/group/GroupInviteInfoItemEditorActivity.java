/**
 * 
 */
package com.smartsport.spedometer.group;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.SSApplication;
import com.smartsport.spedometer.customwidget.SSBNavTitleBarButtonItem;
import com.smartsport.spedometer.group.compete.WithinGroupCompeteInviteInfoSettingActivity.WithinGroupCompeteInviteInfoSettingExtraData;
import com.smartsport.spedometer.group.walk.WalkInviteInfoSettingActivity.WalkInviteInfoSettingExtraData;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.utils.SSLogger;
import com.smartsport.spedometer.utils.StringUtils;

/**
 * @name GroupInviteInfoItemEditorActivity
 * @descriptor smartsport walk or within group compete invite info item editor
 *             activity
 * @author Ares
 * @version 1.0
 */
public class GroupInviteInfoItemEditorActivity extends SSBaseActivity {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			GroupInviteInfoItemEditorActivity.class);

	// input method manager
	private InputMethodManager inputMethodManager;

	// show soft input timer and timer task
	private final Timer SHOW_SOFTINPUT_TIMER = new Timer();
	private TimerTask showSoftInputTimerTask;

	// calendar
	private final Calendar CALENDAR = Calendar.getInstance();

	// milliseconds per minute
	private final int MILLISECONDS_PER_MINUTE = 60 * 1000;

	// timestamp long and short date format
	@SuppressLint("SimpleDateFormat")
	private final SimpleDateFormat TIMESTAMP_LONG_DATEFORMAT = new SimpleDateFormat(
			SSApplication.getContext().getString(R.string.long_dateFormat));
	@SuppressLint("SimpleDateFormat")
	private final SimpleDateFormat TIMESTAMP_SHORT_DATEFORMAT = new SimpleDateFormat(
			SSApplication.getContext().getString(R.string.short_dateFormat));

	// editor group type
	private GroupType editorGroupType;

	// group invite info attribute, editor info name and value
	private GroupInviteInfoAttr4Setting editorAttr;
	private String editorInfoName;
	private String editorInfoValue;

	// group invite info topic edittext, walk schedule time datePicker,
	// timePicker, duration numberPicker, within group compete duration time
	// numberPicker and selected time display textView
	private EditText topicEditText;
	private DatePicker walkScheduleTimeDatePicker;
	private TimePicker walkScheduleTimeTimePicker;
	private NumberPicker walkScheduleTimeDurationNumberPicker;
	private NumberPicker withinGroupCompeteDurationTimeNumberPicker;
	private TextView selectedTimeDisplayTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// get and check the extra data
		Bundle _extraData = getIntent().getExtras();
		if (null != _extraData) {
			// get the editor group type
			editorGroupType = (GroupType) _extraData
					.getSerializable(GroupInviteInfoItemEditorExtraData.GII_EDITORGROUPTYPE);

			// get the editor group invite info attribute, editor name and value
			editorAttr = (GroupInviteInfoAttr4Setting) _extraData
					.getSerializable(GroupInviteInfoItemEditorExtraData.GII_EI_ATTRIBUTE);
			editorInfoName = _extraData
					.getString(GroupInviteInfoItemEditorExtraData.GII_EI_NAME);
			editorInfoValue = _extraData
					.getString(GroupInviteInfoItemEditorExtraData.GII_EI_VALUE);
		}

		// get input method manager
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		// set content view
		setContentView(R.layout.activity_groupinviteinfo_item_editor);
	}

	@Override
	protected void initContentViewUI() {
		// navigation bar
		// set navigation bar color
		setNavbarBackgroundColor(getResources().getColor(
				android.R.color.holo_green_light));

		// set right bar button item
		setRightBarButtonItem(new SSBNavTitleBarButtonItem(this,
				R.string.save_editorGroupInviteInfo_barbtnitem_title,
				new EditorGroupInviteInfoSaveBarBtnItemOnClickListener()));

		// set title attributes
		setTitle(editorInfoName);
		setTitleColor(Color.WHITE);
		setTitleSize(22.0f);
		setShadow(1.0f, 0.6f, 0.8f, Color.GRAY);

		// check editor group invite info attribute and get the editor operator
		// widget
		if (null != editorAttr) {
			// check editor group invite info attribute again
			if (GroupInviteInfoAttr4Setting.GROUP_TOPIC != editorAttr) {
				// get group invite info time picker selected time display
				// textView
				selectedTimeDisplayTextView = (TextView) findViewById(R.id.giiie_selectedTime_display_textView);

				// show group invite info time picker relativeLayout
				findViewById(R.id.giiie_timePicker_relativeLayout)
						.setVisibility(View.VISIBLE);
			}

			switch (editorAttr) {
			case GROUP_TOPIC:
				// get group invite info topic edittext
				topicEditText = (EditText) findViewById(R.id.giiie_topic_editText);

				// check editor group invite info topic value and set it as its
				// text
				if (null != editorInfoValue) {
					topicEditText.setText(editorInfoValue);
				}

				// set its visible
				topicEditText.setVisibility(View.VISIBLE);
				break;

			case WALKINVITE_SCHEDULETIME:
				// get walk invite info schedule time datePicker
				walkScheduleTimeDatePicker = (DatePicker) findViewById(R.id.giiie_walk_scheduleTime_datePicker);

				// set the walk invite info schedule time datePicker descendant
				// focusability
				walkScheduleTimeDatePicker
						.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

				// initialize it
				walkScheduleTimeDatePicker
						.init(CALENDAR.get(Calendar.YEAR),
								CALENDAR.get(Calendar.MONTH),
								CALENDAR.get(Calendar.DAY_OF_MONTH),
								new WalkInviteScheduleTimeDatePickerOnDateChangedListener());

				// get walk invite info schedule time timePicker
				walkScheduleTimeTimePicker = (TimePicker) findViewById(R.id.giiie_walk_scheduleTime_timePicker);

				// set the walk invite info schedule time timePicker descendant
				// focusability
				walkScheduleTimeTimePicker
						.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);

				// set its as 24 hour view
				walkScheduleTimeTimePicker.setIs24HourView(true);

				// set current hour and minute
				walkScheduleTimeTimePicker.setCurrentHour(CALENDAR
						.get(Calendar.HOUR_OF_DAY));
				walkScheduleTimeTimePicker.setCurrentMinute(CALENDAR
						.get(Calendar.MINUTE));

				// set its on time changed listener
				walkScheduleTimeTimePicker
						.setOnTimeChangedListener(new WalkInviteScheduleTimeTimePickerOnTimeChangedListener());

				// get walk invite info schedule time duration numberPicker
				walkScheduleTimeDurationNumberPicker = (NumberPicker) findViewById(R.id.giiie_walk_scheduleTime_duration_numberPicker);

				// set the walk invite info schedule time duration numberPicker
				// descendant focusability
				walkScheduleTimeDurationNumberPicker
						.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

				// set its min and max value
				walkScheduleTimeDurationNumberPicker.setMinValue(1);
				walkScheduleTimeDurationNumberPicker.setMaxValue(120);

				// define walk invite info schedule time duration numberPicker
				// on value changed listener
				WalkInviteScheduleTimeDurationNumberPickerOnValueChangeListener _scheduleTimeDurationNumberPickerOnValueChangeListener = new WalkInviteScheduleTimeDurationNumberPickerOnValueChangeListener();

				// set its on value changed listener
				walkScheduleTimeDurationNumberPicker
						.setOnValueChangedListener(_scheduleTimeDurationNumberPickerOnValueChangeListener);

				// check editor walk invite schedule time and set the user input
				// value if needed
				if (null != editorInfoValue) {
					// define user input walk invite schedule time duration
					// value, using the default min value
					int _userInputWalkInviteScheduleTimeDurationValue = walkScheduleTimeDurationNumberPicker
							.getMinValue();

					// split the editor walk invite schedule time and check
					String[] _scheduleTimes = StringUtils
							.split(editorInfoValue,
									getString(R.string.walk_inviteInfo_scheduleTime_beginTimeDuration_splitWord));
					if (null != _scheduleTimes && 2 == _scheduleTimes.length) {
						try {
							// get user input walk invite schedule time begin
							// timestamp value
							long _userInputWalkInviteScheduleTimeBeginTimestampValue = Long
									.parseLong(_scheduleTimes[0]);

							// define and initialize walk invite schedule time
							// begin timestamp calendar
							Calendar _beginTimeCalendar = Calendar
									.getInstance();
							_beginTimeCalendar
									.setTimeInMillis(_userInputWalkInviteScheduleTimeBeginTimestampValue);

							// update walk invite schedule time datePicker date
							walkScheduleTimeDatePicker.updateDate(
									_beginTimeCalendar.get(Calendar.YEAR),
									_beginTimeCalendar.get(Calendar.MONTH),
									_beginTimeCalendar
											.get(Calendar.DAY_OF_MONTH));

							// update walk invite schedule time timePicker time
							walkScheduleTimeTimePicker
									.setCurrentHour(_beginTimeCalendar
											.get(Calendar.HOUR_OF_DAY));
							walkScheduleTimeTimePicker
									.setCurrentMinute(_beginTimeCalendar
											.get(Calendar.MINUTE));

							// get user input walk invite schedule time duration
							// value
							_userInputWalkInviteScheduleTimeDurationValue = Integer
									.parseInt(_scheduleTimes[_scheduleTimes.length - 1]);

							// set walk invite schedule time duration
							// numberPicker
							// value
							walkScheduleTimeDurationNumberPicker
									.setValue(_userInputWalkInviteScheduleTimeDurationValue);
						} catch (NumberFormatException e) {
							LOGGER.warning("Show walk invite schedule time error, because it is not set");

							e.printStackTrace();
						}
					} else {
						LOGGER.error("The editor walk invite schedule time error, the editor info value = "
								+ editorInfoValue);
					}

					// perform walk invite schedule time duration numberPicker
					// on value changed listener on value change method
					_scheduleTimeDurationNumberPickerOnValueChangeListener
							.onValueChange(
									walkScheduleTimeDurationNumberPicker,
									walkScheduleTimeDurationNumberPicker
											.getMinValue(),
									_userInputWalkInviteScheduleTimeDurationValue);
				}

				// show walk invite info schedule time picker linearLayout
				findViewById(R.id.giiie_walk_scheduleTimePicker_linearLayout)
						.setVisibility(View.VISIBLE);
				break;

			case WITHINGROUPCOMPETE_DURATIONTIME:
				// get within group compete invite info duration time
				// numberPicker
				withinGroupCompeteDurationTimeNumberPicker = (NumberPicker) findViewById(R.id.giiie_withinGroupCompete_durationTime_numberPicker);

				// set the within group compete invite info duration time
				// numberPicker descendant focusability
				withinGroupCompeteDurationTimeNumberPicker
						.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

				// set its min and max value
				withinGroupCompeteDurationTimeNumberPicker.setMinValue(5);
				withinGroupCompeteDurationTimeNumberPicker.setMaxValue(30);

				// define within group compete invite info duration time
				// numberPicker on value changed listener
				WithinGroupCompeteInviteDurationTimeNumberPickerOnValueChangeListener _durationTimeNumberPickerOnValueChangeListener = new WithinGroupCompeteInviteDurationTimeNumberPickerOnValueChangeListener();

				// set its on value changed listener
				withinGroupCompeteDurationTimeNumberPicker
						.setOnValueChangedListener(_durationTimeNumberPickerOnValueChangeListener);

				// check editor within group compete invite duration time and
				// set the user input value if needed
				if (null != editorInfoValue) {
					// define user input value, using the default min value
					int _userInputValue = withinGroupCompeteDurationTimeNumberPicker
							.getMinValue();

					try {
						// get user input value
						_userInputValue = Integer.parseInt(editorInfoValue);

						// set within group compete invite duration time
						// numberPicker value
						withinGroupCompeteDurationTimeNumberPicker
								.setValue(_userInputValue);
					} catch (NumberFormatException e) {
						LOGGER.warning("Show within group compete invite duration time error, because it is not set");

						e.printStackTrace();
					}

					// perform within group compete invite duration time
					// numberPicker on value changed listener on value change
					// method
					_durationTimeNumberPickerOnValueChangeListener
							.onValueChange(
									withinGroupCompeteDurationTimeNumberPicker,
									withinGroupCompeteDurationTimeNumberPicker
											.getMinValue(), _userInputValue);
				}

				// set its visible
				withinGroupCompeteDurationTimeNumberPicker
						.setVisibility(View.VISIBLE);
				break;

			default:
				// nothing to do
				LOGGER.warning("Group invite info attribute = " + editorAttr
						+ " for editor not implementation now");
				break;
			}
		} else {
			LOGGER.error("Edit group invite info error, the editor group invite info type is null");
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		// check editor group invite info attribute
		if (GroupInviteInfoAttr4Setting.GROUP_TOPIC == editorAttr) {
			// set editor group topic editText focusable
			topicEditText.setFocusable(true);
			topicEditText.setFocusableInTouchMode(true);
			topicEditText.requestFocus();

			// show soft input after 250 milliseconds
			SHOW_SOFTINPUT_TIMER.schedule(
					showSoftInputTimerTask = new TimerTask() {

						@Override
						public void run() {
							inputMethodManager.showSoftInput(topicEditText, 0);
						}

					}, 250);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		// check and cancel show soft input timer task
		if (null != showSoftInputTimerTask) {
			showSoftInputTimerTask.cancel();

			showSoftInputTimerTask = null;
		}
	}

	/**
	 * @title getWalkInviteScheduleTimeBeginTimestamp
	 * @descriptor get walk invite schedule time begin timestamp with date and
	 *             time picker
	 * @param scheduleTimeDatePicker
	 *            : walk invite schedule time datePicker
	 * @param scheduleTimeTimePicker
	 *            : walk invite schedule time timePicker
	 * @return walk invite schedule time begin timestamp
	 * @author Ares
	 */
	private long getWalkInviteScheduleTimeBeginTimestamp(
			DatePicker scheduleTimeDatePicker, TimePicker scheduleTimeTimePicker) {
		Calendar _beginTimeCalendar = Calendar.getInstance();
		_beginTimeCalendar.setTimeInMillis(0);

		// check walk invite schedule time date and time picker
		if (null != scheduleTimeDatePicker && null != scheduleTimeTimePicker) {
			// set walk invite schedule time begin time date and time
			_beginTimeCalendar.set(scheduleTimeDatePicker.getYear(),
					scheduleTimeDatePicker.getMonth(),
					scheduleTimeDatePicker.getDayOfMonth(),
					scheduleTimeTimePicker.getCurrentHour(),
					scheduleTimeTimePicker.getCurrentMinute());
		} else {
			LOGGER.error("Get walk invite schedule time begin timestamp error, schedule time datePicker = "
					+ scheduleTimeDatePicker
					+ " and timePicker = "
					+ scheduleTimeTimePicker);
		}

		return _beginTimeCalendar.getTimeInMillis();
	}

	/**
	 * @title getWalkInviteScheduleTimeFormat
	 * @descriptor get walk invite schedule time with begin timestamp and
	 *             duration
	 * @param scheduleTimeBeginTimestamp
	 *            : walk invite schedule time begin timestamp
	 * @param scheduleTimeDuration
	 *            : walk invite schedule time duration
	 * @return walk invite schedule time format
	 * @author Ares
	 */
	private String getWalkInviteScheduleTimeFormat(
			long scheduleTimeBeginTimestamp, int scheduleTimeDuration) {
		// get default locale and default timeZone calendar
		Calendar _defaultCalendar = Calendar.getInstance();

		// define and get begin time calendar day
		_defaultCalendar.setTimeInMillis(scheduleTimeBeginTimestamp);
		int _beginTimeDay = _defaultCalendar.get(Calendar.DAY_OF_MONTH);

		// define and get end time calendar day
		_defaultCalendar.setTimeInMillis(scheduleTimeBeginTimestamp
				+ scheduleTimeDuration * MILLISECONDS_PER_MINUTE);
		int _endTimeDay = _defaultCalendar.get(Calendar.DAY_OF_MONTH);

		// compare the schedule begin, end time calendar day, then generate
		// schedule time and return
		return String.format(
				getString(R.string.walk_inviteInfo_scheduleTime_format),
				TIMESTAMP_LONG_DATEFORMAT.format(scheduleTimeBeginTimestamp),
				(_beginTimeDay == _endTimeDay ? TIMESTAMP_SHORT_DATEFORMAT
						: TIMESTAMP_LONG_DATEFORMAT).format(_defaultCalendar
						.getTimeInMillis()));
	}

	// inner class
	/**
	 * @name GroupInviteInfoItemEditorExtraData
	 * @descriptor group invite info item editor extra data constant
	 * @author Ares
	 * @version 1.0
	 */
	public static final class GroupInviteInfoItemEditorExtraData {

		// editor group type
		public static final String GII_EDITORGROUPTYPE = "editorGroupType";

		// group invite info attribute, editor info name and info value
		public static final String GII_EI_ATTRIBUTE = "groupInviteInfo_attribute";
		public static final String GII_EI_NAME = "groupInviteInfo_editorInfo_name";
		public static final String GII_EI_VALUE = "groupInviteInfo_editorInfo_value";

	}

	/**
	 * @name EditorGroupInviteInfoSaveBarBtnItemOnClickListener
	 * @descriptor editor group invite info save bar button item on click
	 *             listener
	 * @author Ares
	 * @version 1.0
	 */
	class EditorGroupInviteInfoSaveBarBtnItemOnClickListener implements
			OnClickListener {

		@Override
		public void onClick(View v) {
			// check editor group type
			if (null != editorGroupType) {
				// define group invite info editor extra data map
				Map<String, Object> _extraMap = new HashMap<String, Object>();

				// check editor group type and then put editor group invite info
				// attribute to extra data map as param
				switch (editorGroupType) {
				case WALK_GROUP:
					_extraMap.put(
							WalkInviteInfoSettingExtraData.WIIS_EI_ATTRIBUTE,
							editorAttr);
					break;

				case COMPETE_GROUP:
					_extraMap
							.put(WithinGroupCompeteInviteInfoSettingExtraData.WIGCIIS_EI_ATTRIBUTE,
									editorAttr);
					break;
				}

				// check editor group type, invite info attribute and put editor
				// group invite info value to extra data map as param
				switch (editorAttr) {
				case GROUP_TOPIC:
					// group invite topic
					switch (editorGroupType) {
					case WALK_GROUP:
						_extraMap.put(
								WalkInviteInfoSettingExtraData.WIIS_EI_VALUE,
								topicEditText.getText().toString());
						break;

					case COMPETE_GROUP:
						_extraMap
								.put(WithinGroupCompeteInviteInfoSettingExtraData.WIGCIIS_EI_VALUE,
										topicEditText.getText().toString());
						break;
					}
					break;

				case WALKINVITE_SCHEDULETIME:
					// walk invite schedule time(schedule begin and end time)
					_extraMap
							.put(WalkInviteInfoSettingExtraData.WIIS_EI_VALUE,
									new StringBuilder()
											.append(getWalkInviteScheduleTimeBeginTimestamp(
													walkScheduleTimeDatePicker,
													walkScheduleTimeTimePicker))
											.append(getString(R.string.walk_inviteInfo_scheduleTime_beginTimeDuration_splitWord))
											.append(walkScheduleTimeDurationNumberPicker
													.getValue()).toString());
					break;

				case WITHINGROUPCOMPETE_DURATIONTIME:
					// within group compete duration time
					_extraMap
							.put(WithinGroupCompeteInviteInfoSettingExtraData.WIGCIIS_EI_VALUE,
									String.valueOf(withinGroupCompeteDurationTimeNumberPicker
											.getValue()));
					break;
				}

				// pop the activity with result code and extra map
				popActivityWithResult(RESULT_OK, _extraMap);
			} else {
				LOGGER.error("The editor group type is null, can't get and return back the editor group invite info");

				// pop the activity
				popActivityWithResult();
			}
		}

	}

	/**
	 * @name WalkInviteScheduleTimeDatePickerOnDateChangedListener
	 * @descriptor editor walk invite schedule time datePicker on date changed
	 *             listener
	 * @author Ares
	 * @version 1.0
	 */
	class WalkInviteScheduleTimeDatePickerOnDateChangedListener implements
			OnDateChangedListener {

		@Override
		public void onDateChanged(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// get walk invite schedule time and set it as user selected time
			// display textView text
			selectedTimeDisplayTextView
					.setText(getWalkInviteScheduleTimeFormat(
							getWalkInviteScheduleTimeBeginTimestamp(view,
									walkScheduleTimeTimePicker),
							walkScheduleTimeDurationNumberPicker.getValue()));
		}

	}

	/**
	 * @name WalkInviteScheduleTimeTimePickerOnTimeChangedListener
	 * @descriptor editor walk invite schedule time timePicker on time changed
	 *             listener
	 * @author Ares
	 * @version 1.0
	 */
	class WalkInviteScheduleTimeTimePickerOnTimeChangedListener implements
			OnTimeChangedListener {

		@Override
		public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
			// get walk invite schedule time and set it as user selected time
			// display textView text
			selectedTimeDisplayTextView
					.setText(getWalkInviteScheduleTimeFormat(
							getWalkInviteScheduleTimeBeginTimestamp(
									walkScheduleTimeDatePicker, view),
							walkScheduleTimeDurationNumberPicker.getValue()));
		}

	}

	/**
	 * @name WalkInviteScheduleTimeDurationNumberPickerOnValueChangeListener
	 * @descriptor editor walk invite schedule time duration numberPicker on
	 *             value changed listener
	 * @author Ares
	 * @version 1.0
	 */
	class WalkInviteScheduleTimeDurationNumberPickerOnValueChangeListener
			implements OnValueChangeListener {

		@Override
		public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
			// get walk invite schedule time and set it as user selected time
			// display textView text
			selectedTimeDisplayTextView
					.setText(getWalkInviteScheduleTimeFormat(
							getWalkInviteScheduleTimeBeginTimestamp(
									walkScheduleTimeDatePicker,
									walkScheduleTimeTimePicker), newVal));
		}

	}

	/**
	 * @name 
	 *       WithinGroupCompeteInviteDurationTimeNumberPickerOnValueChangeListener
	 * @descriptor editor within group compete invite duration time numberPicker
	 *             on value changed listener
	 * @author Ares
	 * @version 1.0
	 */
	class WithinGroupCompeteInviteDurationTimeNumberPickerOnValueChangeListener
			implements OnValueChangeListener {

		@Override
		public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
			// update within group compete schedule duration time display
			// textView text
			selectedTimeDisplayTextView
					.setText(String
							.format(getString(R.string.withinGroupCompete_inviteInfo_durationTime_format),
									newVal));
		}

	}

}
