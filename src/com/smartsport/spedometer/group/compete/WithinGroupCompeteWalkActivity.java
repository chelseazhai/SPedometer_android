/**
 * 
 */
package com.smartsport.spedometer.group.compete;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.services.core.LatLonPoint;
import com.smartsport.spedometer.R;
import com.smartsport.spedometer.customwidget.SSBNavBarButtonItem;
import com.smartsport.spedometer.customwidget.SSCountDownTimer;
import com.smartsport.spedometer.customwidget.SSCountDownTimer.OnFinishListener;
import com.smartsport.spedometer.customwidget.SSCountDownTimer.OnTickListener;
import com.smartsport.spedometer.customwidget.SSProgressDialog;
import com.smartsport.spedometer.group.GroupInfoModel;
import com.smartsport.spedometer.group.GroupType;
import com.smartsport.spedometer.group.GroupWalkResultActivity;
import com.smartsport.spedometer.group.GroupWalkResultActivity.GroupWalkResultExtraData;
import com.smartsport.spedometer.group.compete.CompeteAttendeesWalkTrendActivity.CompeteAttendeesWalkTrendExtraData;
import com.smartsport.spedometer.group.info.ScheduleGroupInfoBean;
import com.smartsport.spedometer.group.info.member.MemberStatus;
import com.smartsport.spedometer.group.info.member.UserInfoMemberStatusBean;
import com.smartsport.spedometer.group.info.result.UserInfoGroupResultBean;
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.pedometer.IWalkPathPointLocationChangedListener;
import com.smartsport.spedometer.pedometer.WalkInfoType;
import com.smartsport.spedometer.pedometer.WalkMyLocationStyle;
import com.smartsport.spedometer.pedometer.WalkStartPointLocationSource;
import com.smartsport.spedometer.user.UserGender;
import com.smartsport.spedometer.user.UserInfoBean;
import com.smartsport.spedometer.user.UserInfoModel;
import com.smartsport.spedometer.user.UserManager;
import com.smartsport.spedometer.user.UserPedometerExtBean;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name WithinGroupCompeteWalkActivity
 * @descriptor smartsport within group compete walk activity
 * @author Ares
 * @version 1.0
 */
public class WithinGroupCompeteWalkActivity extends SSBaseActivity {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			WithinGroupCompeteWalkActivity.class);

	// milliseconds per second and seconds per minute
	private final int MILLISECONDS_PER_SECOND = 1000;
	private final int SECONDS_PER_MINUTE = 60;

	// pedometer login user
	private UserPedometerExtBean loginUser = (UserPedometerExtBean) UserManager
			.getInstance().getLoginUser();

	// group info and within group compete model
	private GroupInfoModel groupInfoModel = GroupInfoModel.getInstance();
	private WithinGroupCompeteModel withinGroupCompeteModel = WithinGroupCompeteModel
			.getInstance();

	// within group compete inviter walk saved instance state
	private Bundle withinGroupCompeteInviterWalkSavedInstanceState;

	// within group compete inviter walk path mapView and autoNavi map
	private MapView inviterWalkPathMapView;
	private AMap autoNaviMap;

	// autoNavi location source
	private WalkStartPointLocationSource autoNaviMapLocationSource;

	// within group compete group id, topic, start and duration time
	private String competeGroupId;
	private String competeGroupTopic;
	private Long competeGroupStartTime;
	private Integer competeGroupDurationTime;

	// invitees user info with status list in the within group compete group
	private List<UserInfoMemberStatusBean> inviteesUserInfoWithMemberStatusList;

	// walk start and stop remain time textView
	private TextView walkRemainTimeTextView;

	// walk start and stop remain time count down timer
	private SSCountDownTimer walkRemainTimeCountDownTimer;

	// walk info: walk total distance, total steps count and speed
	private double walkDistance;
	private int walkStepsCount;
	private float walkSpeed;

	// walk info: walk total distance, total steps count, energy, pace and speed
	// textView
	private TextView walkDistanceTextView, walkStepsCountTextView,
			walkEnergyTextView, walkPaceTextView, walkSpeedTextView;

	// walk info: attendees walk trend imageView
	private ImageView attendeesWalkTrendImgView;

	// within group compete walk progress dialog
	private SSProgressDialog withinGroupCompeteWalkProgDlg;

	// within group compete inviter walk path point list
	private List<LatLonPoint> walkPathPoints;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(withinGroupCompeteInviterWalkSavedInstanceState = savedInstanceState);

		// get and check the extra data
		Bundle _extraData = getIntent().getExtras();
		if (null != _extraData) {
			// get the within group compete group id, topic, start and duration
			// time
			competeGroupId = _extraData
					.getString(WithinGroupCompeteWalkExtraData.WIGCW_COMPETEGROUP_ID);
			competeGroupTopic = _extraData
					.getString(WithinGroupCompeteWalkExtraData.WIGCW_COMPETEGROUP_TOPIC);
			competeGroupStartTime = _extraData
					.getLong(WithinGroupCompeteWalkExtraData.WIGCW_COMPETEGROUP_STARTTIME);
			// test by ares
			competeGroupStartTime -= 4 * SECONDS_PER_MINUTE
					* MILLISECONDS_PER_SECOND;
			competeGroupDurationTime = _extraData
					.getInt(WithinGroupCompeteWalkExtraData.WIGCW_COMPETEGROUP_DURATIONTIME);
		}

		// initialize within group compete inviter walk path point list
		walkPathPoints = new ArrayList<LatLonPoint>();

		// set content view
		setContentView(R.layout.activity_withingroupcompete_walk);

		// check the within group compete group id and get its info from remote
		// server
		if (null != competeGroupId) {
			// show get schedule within group compete group info progress dialog
			withinGroupCompeteWalkProgDlg = SSProgressDialog.show(this,
					R.string.procMsg_getWithinGroupCompeteGroupInfo);

			groupInfoModel.getUserScheduleGroupInfo(loginUser.getUserId(),
					loginUser.getUserKey(), competeGroupId, new ICMConnector() {

						@Override
						public void onSuccess(Object... retValue) {
							// dismiss get schedule within group compete group
							// info progress dialog
							withinGroupCompeteWalkProgDlg.dismiss();

							// set autoNavi map location source
							autoNaviMap
									.setLocationSource(autoNaviMapLocationSource = new WalkStartPointLocationSource(
											WithinGroupCompeteWalkActivity.this,
											autoNaviMap));

							// enable get my location and hidden location button
							autoNaviMap.getUiSettings()
									.setMyLocationButtonEnabled(false);
							autoNaviMap.setMyLocationEnabled(true);

							// check return values
							if (null != retValue
									&& 0 < retValue.length
									&& retValue[retValue.length - 1] instanceof ScheduleGroupInfoBean) {
								// get the schedule group info
								ScheduleGroupInfoBean _scheduleGroupInfo = (ScheduleGroupInfoBean) retValue[retValue.length - 1];

								LOGGER.info("The schedule within group compete group info = "
										+ _scheduleGroupInfo);

								// // traversal within group compete attendees
								// user info with member status
								// for (UserInfoBean _userInfoMemberStatus :
								// _scheduleGroupInfo
								// .getMembersInfo()) {
								// // update walk invite inviter avatar and
								// // invitee user info
								// if (loginUser.getUserId() ==
								// _userInfoMemberStatus
								// .getUserId()) {
								// // inviter
								// // update walk invite inviter avatar
								// inviterAvatarImgView.setImageURI(Uri
								// .parse(_userInfoMemberStatus
								// .getAvatarUrl()));
								// } else {
								// // invitee
								// inviteeUserInfoWithMemberStatus =
								// (UserInfoMemberStatusBean)
								// _userInfoMemberStatus;
								//
								// // update walk invite invitee user info
								// // UI
								// updateWalkInviteInviteeUserInfo();
								// }
								// }

								// test by ares
								inviteesUserInfoWithMemberStatusList = new ArrayList<UserInfoMemberStatusBean>();
								for (int i = 0; i < 3; i++) {
									UserInfoMemberStatusBean _inviteeUserInfoWithMemberStatus = new UserInfoMemberStatusBean();
									_inviteeUserInfoWithMemberStatus
											.setUserId(12332 + i);
									_inviteeUserInfoWithMemberStatus
											.setAvatarUrl("/img/jshd123" + i);
									_inviteeUserInfoWithMemberStatus
											.setNickname("小慧动" + i);
									_inviteeUserInfoWithMemberStatus
											.setGender(0 == i % 2 ? UserGender.MALE
													: UserGender.FEMALE);
									_inviteeUserInfoWithMemberStatus
											.setMemberStatus(0 == i % 2 ? MemberStatus.MEM_ONLINE
													: MemberStatus.MEM_OFFLINE);

									inviteesUserInfoWithMemberStatusList
											.add(_inviteeUserInfoWithMemberStatus);
								}
							} else {
								LOGGER.error("Update within group compete walk invitee user info UI error");
							}
						}

						@Override
						public void onFailure(int errorCode, String errorMsg) {
							LOGGER.error("Get within group compete group info from remote server error, error code = "
									+ errorCode + " and message = " + errorMsg);

							// dismiss get schedule within group compete group
							// info progress dialog
							withinGroupCompeteWalkProgDlg.dismiss();

							// check error code and process hopeRun business
							// error
							if (errorCode < 100) {
								// show error message toast
								Toast.makeText(
										WithinGroupCompeteWalkActivity.this,
										errorMsg, Toast.LENGTH_SHORT).show();

								// test by ares
								//
							}
						}

					});
		}
	}

	@Override
	protected void initContentViewUI() {
		// navigation bar
		// set navigation bar color
		setNavbarBackgroundColor(getResources().getColor(
				android.R.color.holo_green_light));

		// set title attributes
		setTitle(null != competeGroupTopic ? competeGroupTopic : "");
		setTitleColor(Color.WHITE);
		setTitleSize(22.0f);
		setShadow(1.0f, 0.6f, 0.8f, Color.GRAY);

		// get within group compete inviter walk path mapView
		inviterWalkPathMapView = (MapView) findViewById(R.id.wigcw_inviter_walkPath_mapView);

		// inviter walk path mapView perform onCreate method
		inviterWalkPathMapView
				.onCreate(withinGroupCompeteInviterWalkSavedInstanceState);

		// get autoNavi map
		autoNaviMap = inviterWalkPathMapView.getMap();

		// set its my location style
		autoNaviMap
				.setMyLocationStyle(WalkMyLocationStyle.WALK_MYLOCATION_STYLE);

		// enable compass, scale controls and disable zoom controls
		autoNaviMap.getUiSettings().setCompassEnabled(true);
		autoNaviMap.getUiSettings().setScaleControlsEnabled(true);
		autoNaviMap.getUiSettings().setZoomControlsEnabled(false);

		// get walk start and stop remain time textView
		walkRemainTimeTextView = (TextView) findViewById(R.id.wigcw_walk_remainTime_textView);

		// define compete attendee walk flag
		boolean _isWalking = false;

		// get and check within group compete group walk start remain time
		Long _walkStartRemainTime = getWalkStartRemainTime(
				competeGroupStartTime, competeGroupDurationTime);
		if (null == _walkStartRemainTime) {
			// invalid
			// generate holo red dark foreground color span and text absolute
			// size span
			ForegroundColorSpan _holoRedDarkForegroundColorSpan = new ForegroundColorSpan(
					getResources().getColor(android.R.color.holo_red_dark));
			AbsoluteSizeSpan _textAbsoluteSizeSpan = new AbsoluteSizeSpan(20,
					true);

			// set invalid within group compete group tip
			SpannableString _invalidWithinGroupCompeteGroup = new SpannableString(
					getString(R.string.withinGroupCompeteGroup_isInvalid));
			_invalidWithinGroupCompeteGroup.setSpan(
					_holoRedDarkForegroundColorSpan, 0,
					_invalidWithinGroupCompeteGroup.length(),
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			_invalidWithinGroupCompeteGroup.setSpan(_textAbsoluteSizeSpan, 0,
					_invalidWithinGroupCompeteGroup.length(),
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

			// set within group compete walk start remain time textView text
			walkRemainTimeTextView.setText(_invalidWithinGroupCompeteGroup);
		} else if (0 <= _walkStartRemainTime) {
			// waiting for start
			// get walk start remain time(seconds)
			long _walkStartRemainTimeSeconds = _walkStartRemainTime
					/ MILLISECONDS_PER_SECOND;

			// set within group compete group walk start remain time textView
			// text
			walkRemainTimeTextView
					.setText(String
							.format(getString(R.string.withinGroupCompeteGroup_startReaminTime_format),
									_walkStartRemainTimeSeconds
											/ SECONDS_PER_MINUTE,
									_walkStartRemainTimeSeconds
											% SECONDS_PER_MINUTE));

			// generate walk start remain time count down timer on tick listener
			WalkStartRemainTimeCountDownTimerOnTickListener _walkStartRemainTimeCountDownTimerOnTickListener = new WalkStartRemainTimeCountDownTimerOnTickListener();

			// generate walk start remain time count down timer
			walkRemainTimeCountDownTimer = new SSCountDownTimer(
					_walkStartRemainTime);

			// set its on tick listener
			walkRemainTimeCountDownTimer
					.setOnTickListener(_walkStartRemainTimeCountDownTimerOnTickListener);

			// set its on finish listener
			walkRemainTimeCountDownTimer
					.setOnFinishListener(new WalkStartRemainTimeCountDownTimerOnFinishListener(
							_walkStartRemainTimeCountDownTimerOnTickListener));

			// start walk start remain time count down timer
			walkRemainTimeCountDownTimer.start();
		} else {
			// walking
			// set attendee walk flag
			_isWalking = true;

			// generate holo green light foreground color span
			ForegroundColorSpan _holoGreenLightForegroundColorSpan = new ForegroundColorSpan(
					getResources().getColor(android.R.color.holo_green_light));

			// get compete attendee walk remain time from local storage
			// test by ares
			SpannableString _walkRemainTime = new SpannableString("03'21\"");
			_walkRemainTime.setSpan(_holoGreenLightForegroundColorSpan, 0,
					_walkRemainTime.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

			// set within group compete group walk stop remain time textView
			// text
			walkRemainTimeTextView.setText(_walkRemainTime);
		}

		// get walk info sliding up button
		Button _walkInfoSlidingUpBtn = (Button) findViewById(R.id.wigcw_walkInfo_slidingUp_button);

		// get walk info relativeLayout and set as walk info sliding up button
		// tag
		_walkInfoSlidingUpBtn
				.setTag(findViewById(R.id.wigcw_walkInfo_relativeLayout));

		// set walk info sliding up button on click listener
		_walkInfoSlidingUpBtn
				.setOnClickListener(new WalkInfoSlidingUpBtnOnClickListener());

		// check the within group compete group validity and then set its enable
		if (null == _walkStartRemainTime) {
			_walkInfoSlidingUpBtn.setEnabled(false);
		}

		// get walk total distance, total steps count, energy, pace and speed
		// textView
		walkDistanceTextView = (TextView) findViewById(R.id.wigcw_walkTotalDistance_textView);
		walkStepsCountTextView = (TextView) findViewById(R.id.wigcw_walkTotalStep_textView);
		walkEnergyTextView = (TextView) findViewById(R.id.wigcw_walkEnergy_textView);
		walkPaceTextView = (TextView) findViewById(R.id.wigcw_walkPace_textView);
		walkSpeedTextView = (TextView) findViewById(R.id.wigcw_walkSpeed_textView);

		// get attendees walk trend imageView
		attendeesWalkTrendImgView = (ImageView) findViewById(R.id.wigcw_attendeesWalkTrend_imageView);

		// disable it default
		attendeesWalkTrendImgView.setEnabled(false);

		// set its on click listener
		attendeesWalkTrendImgView
				.setOnClickListener(new AttendeesWalkTrendBtnOnClickListener());

		// test by ares
		updateWalkInfoTextViewText(WalkInfoType.WALKINFO_WALKDISTANCE,
				_isWalking ? "5.25" : "0.00");
		updateWalkInfoTextViewText(WalkInfoType.WALKINFO_WALKSTEPS,
				_isWalking ? "623" : "0");
		updateWalkInfoTextViewText(WalkInfoType.WALKINFO_WALKENERGY,
				_isWalking ? "569.5" : "0.0");
		updateWalkInfoTextViewText(WalkInfoType.WALKINFO_WALKPACE,
				_isWalking ? "2000" : "0");
		updateWalkInfoTextViewText(WalkInfoType.WALKINFO_WALKSPEED,
				_isWalking ? "2.25" : "0.00");
	}

	@Override
	protected void onResume() {
		super.onResume();

		// inviter walk path mapView perform onResume method
		inviterWalkPathMapView.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// inviter walk path mapView perform onSaveInstanceState method
		inviterWalkPathMapView.onSaveInstanceState(outState);
	}

	@Override
	protected void onPause() {
		super.onPause();

		// inviter walk path mapView perform onPause method
		inviterWalkPathMapView.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// inviter walk path mapView perform onDestroy method
		inviterWalkPathMapView.onDestroy();

		// check autoNavi location source and then deactivate
		if (null != autoNaviMapLocationSource) {
			autoNaviMapLocationSource.deactivate();
		}

		// check and cancel walk start, stop remain time count down timer
		if (null != walkRemainTimeCountDownTimer) {
			walkRemainTimeCountDownTimer.cancel();

			walkRemainTimeCountDownTimer = null;
		}
	}

	@Override
	protected void onBackBarButtonItemClick(SSBNavBarButtonItem backBarBtnItem) {
		LOGGER.debug("Alert user leave for a moment or misoperation");

		//
	}

	/**
	 * @title getWalkStartRemainTime
	 * @descriptor get the within group compete group walk start remain time
	 * @param startTime
	 *            : within group compete group walk start time
	 * @param durationTime
	 *            : within group compete group duration time
	 * @return the within group compete group walk start remain time
	 * @author Ares
	 */
	private Long getWalkStartRemainTime(Long startTime, Integer durationTime) {
		// define walk start remain time
		Long _walkStartRemainTime = Long.MIN_VALUE;

		// check within group compete group walk start and duration time
		if (null != startTime && null != durationTime && 0 < durationTime) {
			// get system current timestamp
			long _currentTimestamp = System.currentTimeMillis();

			// compare current time with within group compete group walk start
			// and stop time
			if (_currentTimestamp < startTime) {
				// get remain time
				_walkStartRemainTime = startTime - _currentTimestamp;
			} else if (_currentTimestamp >= (startTime + durationTime
					* SECONDS_PER_MINUTE * MILLISECONDS_PER_SECOND)) {
				// invalid
				_walkStartRemainTime = null;
			}
		} else {
			LOGGER.error("Get within group compete group walk start remain time error, compete group walk start time = "
					+ startTime
					+ " is less than stop time = "
					+ (startTime + durationTime * SECONDS_PER_MINUTE
							* MILLISECONDS_PER_SECOND));
		}

		return _walkStartRemainTime;
	}

	/**
	 * @title updateWalkInfoTextViewText
	 * @descriptor update walk info(walk total distance, total steps count,
	 *             energy, pace and speed) textView text
	 * @param walkInfoType
	 *            : walk info type
	 * @param walkInfo
	 *            : walk info value
	 * @author Ares
	 */
	private void updateWalkInfoTextViewText(WalkInfoType walkInfoType,
			String walkInfo) {
		// check walk info
		if (null != walkInfo) {
			// define walk info textView text absolute size span
			AbsoluteSizeSpan _textAbsoluteSizeSpan = new AbsoluteSizeSpan(22,
					true);

			// define the need to update its text textView and its text
			// spannable string builder
			TextView _need2UpdateTextTextView = null;
			SpannableStringBuilder _textSpannableStringBuilder = new SpannableStringBuilder();

			try {
				// check walk info type then get the need to update its text
				// textView and generate its text spannable string builder
				switch (walkInfoType) {
				case WALKINFO_WALKSTEPS:
					_need2UpdateTextTextView = walkStepsCountTextView;
					_textSpannableStringBuilder.append(String.valueOf(Integer
							.parseInt(walkInfo)));
					_textSpannableStringBuilder.setSpan(_textAbsoluteSizeSpan,
							0, _textSpannableStringBuilder.length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					_textSpannableStringBuilder.append("\t").append(
							getString(R.string.walkInfo_step_unit));
					break;

				case WALKINFO_WALKENERGY:
					_need2UpdateTextTextView = walkEnergyTextView;
					_textSpannableStringBuilder.append(String.valueOf(Float
							.parseFloat(walkInfo)));
					_textSpannableStringBuilder.setSpan(_textAbsoluteSizeSpan,
							0, _textSpannableStringBuilder.length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					_textSpannableStringBuilder.append("\t").append(
							getString(R.string.walkInfo_energy_unit));
					break;

				case WALKINFO_WALKPACE:
					_need2UpdateTextTextView = walkPaceTextView;
					_textSpannableStringBuilder.append(String.valueOf(Integer
							.parseInt(walkInfo)));
					_textSpannableStringBuilder.setSpan(_textAbsoluteSizeSpan,
							0, _textSpannableStringBuilder.length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					_textSpannableStringBuilder.insert(0,
							getString(R.string.walkInfo_pace_label));
					_textSpannableStringBuilder.append("\t").append(
							getString(R.string.walkInfo_pace_unit));
					break;

				case WALKINFO_WALKSPEED:
					_need2UpdateTextTextView = walkSpeedTextView;
					_textSpannableStringBuilder.append(String.valueOf(Double
							.parseDouble(walkInfo)));
					_textSpannableStringBuilder.setSpan(_textAbsoluteSizeSpan,
							0, _textSpannableStringBuilder.length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					_textSpannableStringBuilder.insert(0,
							getString(R.string.walkInfo_speed_label));
					_textSpannableStringBuilder.append("\t").append(
							getString(R.string.walkInfo_speed_unit));
					break;

				case WALKINFO_WALKDISTANCE:
				default:
					_need2UpdateTextTextView = walkDistanceTextView;
					_textSpannableStringBuilder.append(
							String.valueOf(Double.parseDouble(walkInfo)))
							.append(getString(R.string.walkInfo_distance_unit));
					break;
				}
			} catch (NumberFormatException e) {
				LOGGER.error("Update walk info textView text error, get walk info value exception, exception message = "
						+ e.getMessage());

				e.printStackTrace();
			}

			// update walk info textView text
			_need2UpdateTextTextView.setText(_textSpannableStringBuilder);
		} else {
			LOGGER.error("Update walk info textView text error, walk info value is null");
		}
	}

	// inner class
	/**
	 * @name WithinGroupCompeteWalkExtraData
	 * @descriptor within group compete walk extra data constant
	 * @author Ares
	 * @version 1.0
	 */
	public static final class WithinGroupCompeteWalkExtraData {

		// the within group compete group id, topic, start and duration time
		public static final String WIGCW_COMPETEGROUP_ID = "withinGroupCompeteWalk_competeGroup_Id";
		public static final String WIGCW_COMPETEGROUP_TOPIC = "withinGroupCompeteWalk_competeGroup_topic";
		public static final String WIGCW_COMPETEGROUP_STARTTIME = "withinGroupCompeteWalk_competeGroup_startTime";
		public static final String WIGCW_COMPETEGROUP_DURATIONTIME = "withinGroupCompeteWalk_competeGroup_durationTime";

	}

	/**
	 * @name WalkStartRemainTimeCountDownTimerOnTickListener
	 * @descriptor walk start remain time count down timer on tick listener
	 * @author Ares
	 * @version 1.0
	 */
	class WalkStartRemainTimeCountDownTimerOnTickListener implements
			OnTickListener {

		@Override
		public void onTick(long remainMillis) {
			// get walk start remain time(seconds)
			long _walkStartRemainTimeSeconds = remainMillis
					/ MILLISECONDS_PER_SECOND;

			// update within group compete group walk start remain time textView
			// text
			walkRemainTimeTextView
					.setText(String
							.format(getString(R.string.withinGroupCompeteGroup_startReaminTime_format),
									_walkStartRemainTimeSeconds
											/ SECONDS_PER_MINUTE,
									_walkStartRemainTimeSeconds
											% SECONDS_PER_MINUTE));
		}

	}

	/**
	 * @name WalkStartRemainTimeCountDownTimerOnFinishListener
	 * @descriptor walk start remain time count down timer on finish listener
	 * @author Ares
	 * @version 1.0
	 */
	class WalkStartRemainTimeCountDownTimerOnFinishListener implements
			OnFinishListener {

		// walk start remain time count down timer on tick listener
		private WalkStartRemainTimeCountDownTimerOnTickListener walkStartRemainTimeCountDownTimerOnTickListener;

		/**
		 * @title WalkStartRemainTimeCountDownTimerOnFinishListener
		 * @descriptor walk start remain time count down timer on finish
		 *             listener constructor with its on tick listener
		 * @param walkStartRemainTimeCountDownTimerOnTickListener
		 *            : walk start remain time count down timer on tick listener
		 * @author Ares
		 */
		public WalkStartRemainTimeCountDownTimerOnFinishListener(
				WalkStartRemainTimeCountDownTimerOnTickListener walkStartRemainTimeCountDownTimerOnTickListener) {
			super();

			// save walk start remain time count down timer on tick listener
			this.walkStartRemainTimeCountDownTimerOnTickListener = walkStartRemainTimeCountDownTimerOnTickListener;
		}

		public void onFinish() {
			// clear compete walk remain time
			walkStartRemainTimeCountDownTimerOnTickListener.onTick(0);

			// generate walk stop remain time count down timer on tick listener
			final WalkStopRemainTimeCountDownTimerOnTickListener _walkStopRemainTimeCountDownTimerOnTickListener = new WalkStopRemainTimeCountDownTimerOnTickListener();

			// tick walk stop remain time count down timer delay one second
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					// get walk duration time
					long _walkDurationTime = competeGroupDurationTime
							* SECONDS_PER_MINUTE * MILLISECONDS_PER_SECOND;

					// tick walk stop remain time count down timer
					_walkStopRemainTimeCountDownTimerOnTickListener
							.onTick(_walkDurationTime);

					// generate walk stop remain time count down timer
					walkRemainTimeCountDownTimer = new SSCountDownTimer(
							_walkDurationTime);

					// set its on tick listener
					walkRemainTimeCountDownTimer
							.setOnTickListener(_walkStopRemainTimeCountDownTimerOnTickListener);

					// set its on finish listener
					walkRemainTimeCountDownTimer
							.setOnFinishListener(new WalkStopRemainTimeCountDownTimerOnFinishListener(
									_walkStopRemainTimeCountDownTimerOnTickListener));

					// start walk stop remain time count down timer delay one
					// second
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							// start walk stop remain time count down timer
							walkRemainTimeCountDownTimer.start();
						}

					}, MILLISECONDS_PER_SECOND);

					// set within group compete inviter walk path point location
					// changed listener
					autoNaviMapLocationSource
							.setWalkPathPointLocationChangedListener(new CompeteInviterWalkPathPointLocationChangedListener());

					// start mark within group compete inviter walk path
					autoNaviMapLocationSource.startMarkWalkPath();

					// enable it default
					attendeesWalkTrendImgView.setEnabled(true);
				}

			}, MILLISECONDS_PER_SECOND);
		}

		// inner class
		/**
		 * @name CompeteInviterWalkPathPointLocationChangedListener
		 * @descriptor within group compete inviter walk path point location
		 *             changed listener
		 * @author Ares
		 * @version 1.0
		 */
		class CompeteInviterWalkPathPointLocationChangedListener implements
				IWalkPathPointLocationChangedListener {

			// centimeters per meter
			private final int CENTIMETERS_PER_METER = 100;

			// energy calculate coefficient
			private final float ENERGY_CALCCOEFFICIENT = 1.036f;

			// walk energy
			private float walkEnergy;

			@Override
			public void onLocationChanged(LatLonPoint walkPathPoint,
					double walkSpeed, double walkDistance) {
				// add walk path point to it
				walkPathPoints.add(walkPathPoint);

				// get local storage user step length
				// test by ares
				Float _userStepLength = Float.parseFloat("62.0");

				// increase walk total distance and total steps count
				WithinGroupCompeteWalkActivity.this.walkDistance += walkDistance;
				WithinGroupCompeteWalkActivity.this.walkStepsCount += walkDistance
						* WalkStartPointLocationSource.METERS_PER_KILOMETER
						/ (_userStepLength / CENTIMETERS_PER_METER);

				// get user info
				UserInfoBean _userInfo = UserInfoModel.getInstance()
						.getUserInfo();

				// update energy
				// test by ares
				// walkEnergy = 123.5f;
				walkEnergy = (float) (_userInfo.getWeight()
						* WithinGroupCompeteWalkActivity.this.walkDistance * ENERGY_CALCCOEFFICIENT);

				// update within group compete inviter walk info(walk total
				// distance, total steps count, energy, pace and speed) textView
				// text
				updateWalkInfoTextViewText(
						WalkInfoType.WALKINFO_WALKDISTANCE,
						String.format(
								getString(R.string.walkInfo_distance_value_format),
								WithinGroupCompeteWalkActivity.this.walkDistance));
				updateWalkInfoTextViewText(
						WalkInfoType.WALKINFO_WALKSTEPS,
						String.valueOf(WithinGroupCompeteWalkActivity.this.walkStepsCount));
				updateWalkInfoTextViewText(
						WalkInfoType.WALKINFO_WALKENERGY,
						String.format(
								getString(R.string.walkInfo_energy_value_format),
								walkEnergy));
				updateWalkInfoTextViewText(WalkInfoType.WALKINFO_WALKPACE, "0");
				updateWalkInfoTextViewText(
						WalkInfoType.WALKINFO_WALKSPEED,
						String.format(
								getString(R.string.walkInfo_speed_value_format),
								WithinGroupCompeteWalkActivity.this.walkSpeed = (float) walkSpeed));
			}
		}

	}

	/**
	 * @name WalkStopRemainTimeCountDownTimerOnTickListener
	 * @descriptor walk stop remain time count down timer on tick listener
	 * @author Ares
	 * @version 1.0
	 */
	class WalkStopRemainTimeCountDownTimerOnTickListener implements
			OnTickListener {

		// holo green light foreground color span
		private final ForegroundColorSpan HOLOGREENLIGHT_FOREGROUNDCOLOR_SPAN = new ForegroundColorSpan(
				getResources().getColor(android.R.color.holo_green_light));

		// publish self walk info period
		private final int PUBLISH_SELFWALKINFO_PERIOD = getResources()
				.getInteger(
						R.integer.config_withinGroupCompeteWalk_publishAndGetAttendees_walkInfo_period);

		@Override
		public void onTick(long remainMillis) {
			// get walk stop remain time(seconds)
			long _walkStopRemainTimeSeconds = remainMillis
					/ MILLISECONDS_PER_SECOND;

			// generate within group compete group walk stop remain time
			SpannableString _walkStopRemainTime = new SpannableString(
					String.format(
							getString(R.string.withinGroupCompeteGroup_stopReaminTime_format),
							_walkStopRemainTimeSeconds / SECONDS_PER_MINUTE,
							_walkStopRemainTimeSeconds % SECONDS_PER_MINUTE));
			_walkStopRemainTime.setSpan(HOLOGREENLIGHT_FOREGROUNDCOLOR_SPAN, 0,
					_walkStopRemainTime.length(),
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

			// update within group compete group walk stop remain time textView
			// text
			walkRemainTimeTextView.setText(_walkStopRemainTime);

			// schedule publish self walk info immediately and repeat every
			// period
			if (0 == _walkStopRemainTimeSeconds % PUBLISH_SELFWALKINFO_PERIOD) {
				// publish self walk info
				withinGroupCompeteModel.publishWithinGroupCompeteWalkingInfo(
						loginUser.getUserId(), loginUser.getUserKey(),
						competeGroupId, walkSpeed, walkStepsCount,
						walkDistance, new ICMConnector() {

							@Override
							public void onSuccess(Object... retValue) {
								// nothing to do
							}

							@Override
							public void onFailure(int errorCode, String errorMsg) {
								LOGGER.error("Publish self walk info error, error code = "
										+ errorCode
										+ " and message = "
										+ errorMsg);

								//
							}

						});
			}
		}

	}

	/**
	 * @name WalkStopRemainTimeCountDownTimerOnFinishListener
	 * @descriptor walk stop remain time count down timer on finish listener
	 * @author Ares
	 * @version 1.0
	 */
	class WalkStopRemainTimeCountDownTimerOnFinishListener implements
			OnFinishListener {

		// walk stop remain time count down timer on tick listener
		private WalkStopRemainTimeCountDownTimerOnTickListener walkStopRemainTimeCountDownTimerOnTickListener;

		/**
		 * @title WalkStopRemainTimeCountDownTimerOnFinishListener
		 * @descriptor walk stop remain time count down timer on finish listener
		 *             constructor with its on tick listener
		 * @param walkStopRemainTimeCountDownTimerOnTickListener
		 *            : walk stop remain time count down timer on tick listener
		 * @author Ares
		 */
		public WalkStopRemainTimeCountDownTimerOnFinishListener(
				WalkStopRemainTimeCountDownTimerOnTickListener walkStopRemainTimeCountDownTimerOnTickListener) {
			super();

			// save walk stop remain time count down timer on tick listener
			this.walkStopRemainTimeCountDownTimerOnTickListener = walkStopRemainTimeCountDownTimerOnTickListener;
		}

		@Override
		public void onFinish() {
			// clear compete walk remain time
			walkStopRemainTimeCountDownTimerOnTickListener.onTick(0);

			// stop mark user personal walk path
			autoNaviMapLocationSource.stopMarkWalkPath();

			// define within group compete walk extra data map
			Map<String, Object> _extraMap = new HashMap<String, Object>();

			// put within group compete group type, walk start, stop time and
			// attendees walk result to extra data map as param
			_extraMap.put(GroupWalkResultExtraData.GWR_GROUP_TYPE,
					GroupType.COMPETE_GROUP);
			_extraMap.put(GroupWalkResultExtraData.GWR_GROUP_WALK_STARTTIME,
					competeGroupStartTime);
			_extraMap.put(GroupWalkResultExtraData.GWR_GROUP_WALK_STOPTIME,
					competeGroupStartTime + competeGroupDurationTime
							* SECONDS_PER_MINUTE * MILLISECONDS_PER_SECOND);
			// test by ares
			_extraMap.put(
					GroupWalkResultExtraData.GWR_GROUP_ATTENDEES_WALKRESULT,
					new ArrayList<UserInfoGroupResultBean>());

			// go to within group compete walk result activity with extra data
			// map
			popPushActivity(GroupWalkResultActivity.class, _extraMap);
		}

	}

	/**
	 * @name WalkInfoSlidingUpBtnOnClickListener
	 * @descriptor walk info sliding up button on click listener
	 * @author Ares
	 * @version 1.0
	 */
	class WalkInfoSlidingUpBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// get and check walk info sliding up button tag
			Object _tag = v.getTag();
			if (null != _tag && _tag instanceof RelativeLayout) {
				// get walk info relativeLayout
				RelativeLayout _walkInfoRelativeLayout = (RelativeLayout) _tag;

				// get its visibility and check
				if (View.VISIBLE != _walkInfoRelativeLayout.getVisibility()) {
					// sliding up walk info relativeLayout
					_walkInfoRelativeLayout.setVisibility(View.VISIBLE);

					// test by ares
					//
				} else {
					// sliding down walk info relativeLayout
					_walkInfoRelativeLayout.setVisibility(View.GONE);

					// test by ares
					//
				}
			}
		}

	}

	/**
	 * @name AttendeesWalkTrendBtnOnClickListener
	 * @descriptor attendees walk trend button on click listener
	 * @author Ares
	 * @version 1.0
	 */
	class AttendeesWalkTrendBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// define within group compete attendees walk trend extra data map
			Map<String, Object> _extraMap = new HashMap<String, Object>();

			// put within group compete group id and attendees user info with
			// status list to extra data map as param
			_extraMap.put(
					CompeteAttendeesWalkTrendExtraData.CAWT_COMPETEGROUP_ID,
					competeGroupId);
			_extraMap
					.put(CompeteAttendeesWalkTrendExtraData.CAWT_COMPETEGROUP_ATTENDEES,
							inviteesUserInfoWithMemberStatusList);

			// go to within group compete attendees walk trend activity with
			// extra data map
			pushActivity(CompeteAttendeesWalkTrendActivity.class, _extraMap);
		}

	}

}
