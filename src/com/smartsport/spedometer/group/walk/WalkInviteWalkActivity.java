/**
 * 
 */
package com.smartsport.spedometer.group.walk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.smartsport.spedometer.R;
import com.smartsport.spedometer.customwidget.SSBNavBarButtonItem;
import com.smartsport.spedometer.group.GroupInfoModel;
import com.smartsport.spedometer.group.GroupType;
import com.smartsport.spedometer.group.GroupWalkResultActivity;
import com.smartsport.spedometer.group.GroupWalkResultActivity.GroupWalkResultExtraData;
import com.smartsport.spedometer.group.info.ScheduleGroupInfoBean;
import com.smartsport.spedometer.group.info.member.MemberStatus;
import com.smartsport.spedometer.group.info.member.UserInfoMemberStatusBean;
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.pedometer.WalkInfoType;
import com.smartsport.spedometer.pedometer.WalkStartPointLocationSource;
import com.smartsport.spedometer.user.UserInfoBean;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name WalkInviteWalkActivity
 * @descriptor smartsport walk invite invite activity
 * @author Ares
 * @version 1.0
 */
public class WalkInviteWalkActivity extends SSBaseActivity {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			WalkInviteWalkActivity.class);

	// milliseconds per second, seconds per minute and minutes per hour
	private final int MILLISECONDS_PER_SECOND = 1000;
	private final int SECONDS_PER_MINUTE = 60;
	private final int MINUTES_PER_HOUR = 60;

	// locate my location timer and timer task
	private Timer LOCATE_MYLOCATION_TIMER = new Timer();
	private TimerTask locateMyLocationTimerTask;

	// group info and walk invite model
	private GroupInfoModel groupInfoModel = GroupInfoModel.getInstance();
	private WalkInviteModel walkInviteModel = WalkInviteModel.getInstance();

	// walk invite walk saved instance state
	private Bundle walkInviteWalkSavedInstanceState;

	// walk invite attendee walk path mapView and autoNavi map
	private MapView attendeeWalkPathMapView;
	private AMap autoNaviMap;

	// autoNavi location source
	private WalkStartPointLocationSource autoNaviMapLocationSource;

	// the schedule walk invite group id, topic, schedule begin and end time
	private String scheduleWalkInviteGroupId;
	private String scheduleWalkInviteGroupTopic;
	private Long scheduleWalkInviteGroupBeginTime;
	private Long scheduleWalkInviteGroupEndTime;

	// invitee user info with status in the schedule walk invite group
	private UserInfoMemberStatusBean inviteeUserInfoWithMemberStatus;

	// walk invite inviter avatar imageView and walk path watch badger imageView
	private ImageView inviterAvatarImgView;
	private ImageView inviterWalkPathWatchBadgerImgView;

	// walk invite start remain or walk duration time textView
	private TextView walkStartRemainOrWalkDurationTimeTextView;

	// walk invite invitee avatar imageView, walk path watch badger imageView
	// and nickname textView
	private ImageView inviteeAvatarImgView;
	private ImageView inviteeWalkPathWatchBadgerImgView;
	private TextView inviteeNicknameTextView;

	// walk info: walk total distance, total steps count, energy, pace and speed
	// textView
	private TextView walkDistanceTextView, walkStepsCountTextView,
			walkEnergyTextView, walkPaceTextView, walkSpeedTextView;

	// attendee walk control flag
	private boolean isAttendeeWalkControlled;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(walkInviteWalkSavedInstanceState = savedInstanceState);

		// get and check the extra data
		Bundle _extraData = getIntent().getExtras();
		if (null != _extraData) {
			// get the schedule walk invite group id, topic, schedule begin and
			// end time
			scheduleWalkInviteGroupId = _extraData
					.getString(WalkInviteWalkExtraData.WIW_WALKINVITEGROUP_ID);
			scheduleWalkInviteGroupTopic = _extraData
					.getString(WalkInviteWalkExtraData.WIW_WALKINVITEGROUP_TOPIC);
			scheduleWalkInviteGroupBeginTime = _extraData
					.getLong(WalkInviteWalkExtraData.WIW_WALKINVITEGROUP_SCHEDULEBEGINTIME);
			scheduleWalkInviteGroupEndTime = _extraData
					.getLong(WalkInviteWalkExtraData.WIW_WALKINVITEGROUP_SCHEDULEENDTIME);
		}

		// set content view
		setContentView(R.layout.activity_walkinvite_walk);

		// check the schedule walk invite group id and then get its info from
		// remote server
		if (null != scheduleWalkInviteGroupId) {
			groupInfoModel.getUserScheduleGroupInfo(1002, "token",
					scheduleWalkInviteGroupId, new ICMConnector() {

						@Override
						public void onSuccess(Object... retValue) {
							// check return values
							if (null != retValue
									&& 0 < retValue.length
									&& retValue[retValue.length - 1] instanceof ScheduleGroupInfoBean) {
								// get the schedule group info
								ScheduleGroupInfoBean _scheduleGroupInfo = (ScheduleGroupInfoBean) retValue[retValue.length - 1];

								LOGGER.info("The schedule group info = "
										+ _scheduleGroupInfo);

								// traversal walk invite attendees user info
								// with member status
								for (UserInfoBean _userInfoMemberStatus : _scheduleGroupInfo
										.getMembersInfo()) {
									// update walk invite inviter avatar and
									// invitee user info
									if (1002 == _userInfoMemberStatus
											.getUserId()) {
										// inviter
										// update walk invite inviter avatar
										inviterAvatarImgView.setImageURI(Uri
												.parse(_userInfoMemberStatus
														.getAvatarUrl()));
									} else {
										// invitee
										inviteeUserInfoWithMemberStatus = (UserInfoMemberStatusBean) _userInfoMemberStatus;

										// update walk invite invitee user info
										// UI
										updateWalkInviteInviteeUserInfo();
									}
								}

								// // test by ares
								// inviteeUserInfoWithMemberStatus = new
								// UserInfoMemberStatusBean();
								// inviteeUserInfoWithMemberStatus
								// .setUserId(123321);
								// inviteeUserInfoWithMemberStatus
								// .setAvatarUrl("/img/jshd123");
								// inviteeUserInfoWithMemberStatus
								// .setNickname("小慧动");
								// inviteeUserInfoWithMemberStatus
								// .setGender(UserGender.FEMALE);
								// inviteeUserInfoWithMemberStatus.setAge(28);
								// inviteeUserInfoWithMemberStatus
								// .setHeight(165.0f);
								// inviteeUserInfoWithMemberStatus
								// .setWeight(55.0f);
								// inviteeUserInfoWithMemberStatus
								// .setMemberStatus(MemberStatus.MEM_OFFLINE);
								//
								// // update walk invite invitee user info UI
								// updateWalkInviteInviteeUserInfo();
							} else {
								LOGGER.error("Update walk invite walk invitee user info UI error");
							}
						}

						@Override
						public void onFailure(int errorCode, String errorMsg) {
							LOGGER.error("Get walk invite group info from remote server error, error code = "
									+ errorCode + " and message = " + errorMsg);

							//
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
		setTitle(null != scheduleWalkInviteGroupTopic ? scheduleWalkInviteGroupTopic
				: "");
		setTitleColor(Color.WHITE);
		setTitleSize(22.0f);
		setShadow(1.0f, 0.6f, 0.8f, Color.GRAY);

		// get walk invite attendee walk path mapView
		attendeeWalkPathMapView = (MapView) findViewById(R.id.wiw_attendee_walkPath_mapView);

		// attendee walk path mapView perform onCreate method
		attendeeWalkPathMapView.onCreate(walkInviteWalkSavedInstanceState);

		// get autoNavi map
		autoNaviMap = attendeeWalkPathMapView.getMap();

		// set its my location style
		autoNaviMap.setMyLocationStyle(new MyLocationStyle()
				.myLocationIcon(
						BitmapDescriptorFactory
								.fromResource(R.drawable.img_poi_mylocation))
				.radiusFillColor(
						getResources().getColor(
								R.color.quarter_black_transparent))
				.strokeWidth(10)
				.strokeColor(
						getResources().getColor(android.R.color.transparent)));

		// // set its location source after 250 milliseconds
		// LOCATE_MYLOCATION_TIMER.schedule(
		// locateMyLocationTimerTask = new TimerTask() {
		//
		// @Override
		// public void run() {
		// autoNaviMap
		// .setLocationSource(autoNaviMapLocationSource = new
		// WalkStartPointLocationSource(
		// WalkInviteWalkActivity.this,
		// autoNaviMap));
		// }
		//
		// }, 250);
		// set its location source
		autoNaviMap
				.setLocationSource(autoNaviMapLocationSource = new WalkStartPointLocationSource(
						this, autoNaviMap));

		// enable get my location, compass and hidden location, zoom controls
		// button
		autoNaviMap.getUiSettings().setMyLocationButtonEnabled(false);
		autoNaviMap.setMyLocationEnabled(true);
		autoNaviMap.getUiSettings().setCompassEnabled(true);
		autoNaviMap.getUiSettings().setZoomControlsEnabled(false);

		// get walk invite inviter avatar imageView
		inviterAvatarImgView = (ImageView) findViewById(R.id.wiw_inviterAvatar_imageView);

		// set its image
		inviterAvatarImgView.setImageURI(null);

		// get walk path watch badger imageView and set it as walk invite
		// inviter avatar imageView tag
		inviterWalkPathWatchBadgerImgView = (ImageView) findViewById(R.id.wiw_selfWalkPathWatchBadger_imageView);
		inviterAvatarImgView.setTag(inviterWalkPathWatchBadgerImgView);

		// get walk invite walk start remain or walk duration time textView
		walkStartRemainOrWalkDurationTimeTextView = (TextView) findViewById(R.id.wiw_walkStartRemainTime_or_walkDurationTime_textView);

		// define attendee walk flag
		boolean _isWalking = false;

		// get and check walk invite walk start remain time
		Long _walkStartRemainTime = getWalkStartRemainTime(
				scheduleWalkInviteGroupBeginTime,
				scheduleWalkInviteGroupEndTime);
		if (null == _walkStartRemainTime) {
			// invalid
			// generate holo red dark foreground color span and text absolute
			// size span
			ForegroundColorSpan _holoRedDarkForegroundColorSpan = new ForegroundColorSpan(
					getResources().getColor(android.R.color.holo_red_dark));
			AbsoluteSizeSpan _textAbsoluteSizeSpan = new AbsoluteSizeSpan(16,
					true);

			// set invalid walk invite group tip
			SpannableString _invalidWalkInviteGroup = new SpannableString(
					getString(R.string.walkInviteGroup_isInvalid));
			_invalidWalkInviteGroup.setSpan(_holoRedDarkForegroundColorSpan, 0,
					_invalidWalkInviteGroup.length(),
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			_invalidWalkInviteGroup.setSpan(_textAbsoluteSizeSpan, 0,
					_invalidWalkInviteGroup.length(),
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

			// set walk invite walk start remain or walk duration time textView
			// text
			walkStartRemainOrWalkDurationTimeTextView
					.setText(_invalidWalkInviteGroup);
		} else if (0 <= _walkStartRemainTime) {
			// waiting for start
			// set walk invite walk start remain or walk duration time textView
			// text
			walkStartRemainOrWalkDurationTimeTextView
					.setText(formatWalkStartRemainTime(_walkStartRemainTime));
		} else {
			// ready for walking
			// get attendee walk flag from local storage
			// test by ares
			_isWalking = false;

			// generate holo green light foreground color span
			ForegroundColorSpan _holoGreenLightForegroundColorSpan = new ForegroundColorSpan(
					getResources().getColor(android.R.color.holo_green_light));

			// get attendee walk duration time from local storage
			// test by ares
			SpannableString _walkDurationTime = new SpannableString(
					_isWalking ? "13'30\"" : "0'0\"");
			_walkDurationTime.setSpan(_holoGreenLightForegroundColorSpan, 0,
					_walkDurationTime.length(),
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

			// set walk invite walk start remain or walk duration time textView
			// text
			walkStartRemainOrWalkDurationTimeTextView
					.setText(_walkDurationTime);
		}

		// get walk invite invitee avatar imageView
		inviteeAvatarImgView = (ImageView) findViewById(R.id.wiw_inviteeAvatar_imageView);

		// get walk invite invitee walk path watch badger imageView and set it
		// as walk invite invitee avatar imageView tag
		inviteeWalkPathWatchBadgerImgView = (ImageView) findViewById(R.id.wiw_walkPartnerWalkPathWatchBadger_imageView);
		inviteeAvatarImgView.setTag(inviteeWalkPathWatchBadgerImgView);

		// get walk invite invitee nickname textView
		inviteeNicknameTextView = (TextView) findViewById(R.id.wiw_inviteeNickname_textView);

		// update walk invite invitee user info UI
		updateWalkInviteInviteeUserInfo();

		// check attendee walk flag and then set walk attendee(inviter and
		// invitee) avatar imageView on click listener
		if (_isWalking) {
			setWalkAttendeeAvatarImgViewOnClickListener();
		}

		// get walk info sliding up button
		Button _walkInfoSlidingUpBtn = (Button) findViewById(R.id.wiw_walkInfo_slidingUp_button);

		// get walk info relativeLayout and set as walk info sliding up button
		// tag
		_walkInfoSlidingUpBtn
				.setTag(findViewById(R.id.wiw_walkInfo_relativeLayout));

		// set walk info sliding up button on click listener
		_walkInfoSlidingUpBtn
				.setOnClickListener(new WalkInfoSlidingUpBtnOnClickListener());

		// check the walk invite group validity and then set its enable
		if (null == _walkStartRemainTime) {
			_walkInfoSlidingUpBtn.setEnabled(false);
		}

		// get walk total distance, total steps count, energy, pace and speed
		// textView
		walkDistanceTextView = (TextView) findViewById(R.id.wiw_walkTotalDistance_textView);
		walkStepsCountTextView = (TextView) findViewById(R.id.wiw_walkTotalStep_textView);
		walkEnergyTextView = (TextView) findViewById(R.id.wiw_walkEnergy_textView);
		walkPaceTextView = (TextView) findViewById(R.id.wiw_walkPace_textView);
		walkSpeedTextView = (TextView) findViewById(R.id.wiw_walkSpeed_textView);

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

		// get walk control button
		Button _walkControlBtn = (Button) findViewById(R.id.wiw_walkControl_button);

		// check attendee walk flag and update walk control button text and
		// background
		if (_isWalking) {
			_walkControlBtn.setText(R.string.walkStop_button_text);
			_walkControlBtn
					.setBackgroundResource(R.drawable.walk_stopbutton_bg);
		}

		// set its text as the tag
		_walkControlBtn.setTag(_walkControlBtn.getText());

		// set its on click listener
		_walkControlBtn.setOnClickListener(new WalkControlBtnOnClickListener());

		// check walk invite walk start remain time and show walk control button
		if (null != _walkStartRemainTime && 0 > _walkStartRemainTime) {
			// show walk control button
			_walkControlBtn.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		// attendee walk path mapView perform onResume method
		attendeeWalkPathMapView.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// attendee walk path mapView perform onSaveInstanceState method
		attendeeWalkPathMapView.onSaveInstanceState(outState);
	}

	@Override
	protected void onPause() {
		super.onPause();

		// attendee walk path mapView perform onPause method
		attendeeWalkPathMapView.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// attendee walk path mapView perform onDestroy method
		attendeeWalkPathMapView.onDestroy();

		// check autoNavi location source and then deactivate
		if (null != autoNaviMapLocationSource) {
			autoNaviMapLocationSource.deactivate();
		}

		// check and cancel locate my location timer task
		if (null != locateMyLocationTimerTask) {
			locateMyLocationTimerTask.cancel();

			locateMyLocationTimerTask = null;
		}
	}

	@Override
	protected void onBackBarButtonItemClick(SSBNavBarButtonItem backBarBtnItem) {
		// check attendee walk control flag
		if (isAttendeeWalkControlled) {
			LOGGER.debug("Alert user leave for a moment or misoperation");

			//
		} else {
			super.onBackBarButtonItemClick(backBarBtnItem);
		}
	}

	/**
	 * @title setWalkAttendeeAvatarImgViewOnClickListener
	 * @descriptor set walk attendee(inviter and invitee) avatar imageView on
	 *             click listener
	 * @author Ares
	 */
	private void setWalkAttendeeAvatarImgViewOnClickListener() {
		// define walk invite attendee avatar imageView on click listener and
		// set it as inviter and invitee avatar imageView on click listener
		AttendeeAvatarImgViewOnClickListener _attendeeAvatarImgViewOnClickListener = new AttendeeAvatarImgViewOnClickListener();
		inviterAvatarImgView
				.setOnClickListener(_attendeeAvatarImgViewOnClickListener);
		inviteeAvatarImgView
				.setOnClickListener(_attendeeAvatarImgViewOnClickListener);
	}

	/**
	 * @title getWalkStartRemainTime
	 * @descriptor get walk invite group walk start remain time
	 * @param scheduleBeginTime
	 *            : walk invite schedule begin time
	 * @param scheduleEndTime
	 *            : walk invite schedule end time
	 * @return the walk invite group walk start remain time
	 * @author Ares
	 */
	private Long getWalkStartRemainTime(Long scheduleBeginTime,
			Long scheduleEndTime) {
		// define walk start remain time
		Long _walkStartRemainTime = Long.MIN_VALUE;

		// check walk invite group schedule begin and end time
		if (null != scheduleBeginTime && null != scheduleEndTime
				&& scheduleBeginTime < scheduleEndTime) {
			// get system current timestamp
			long _currentTimestamp = System.currentTimeMillis();

			// compare current time with walk invite schedule begin and end time
			if (_currentTimestamp < scheduleBeginTime) {
				// get remain time
				_walkStartRemainTime = scheduleBeginTime - _currentTimestamp;
			} else if (_currentTimestamp >= scheduleEndTime) {
				// invalid
				_walkStartRemainTime = null;
			}
		} else {
			LOGGER.error("Get walk invite group walk start remain time error, walk invite group schedule end time = "
					+ scheduleEndTime
					+ " is less than begin time = "
					+ scheduleBeginTime);
		}

		return _walkStartRemainTime;
	}

	/**
	 * @title formatWalkStartRemainTime
	 * @descriptor format walk invite group walk start remain time
	 * @param walkStartRemainTime
	 *            : walk start remain time
	 * @return the walk invite group walk start remain time format
	 * @author Ares
	 */
	private SpannableString formatWalkStartRemainTime(long walkStartRemainTime) {
		// get walk start remain time(minutes)
		long _walkStartRemainTimeMinutes = walkStartRemainTime
				/ (SECONDS_PER_MINUTE * MILLISECONDS_PER_SECOND);

		// generate text absolute size span
		AbsoluteSizeSpan _textAbsoluteSizeSpan = new AbsoluteSizeSpan(16, true);

		// get walk start remain time format
		SpannableString _walkStartRemainTimeFormat;
		if (0 == _walkStartRemainTimeMinutes
				&& 0 != walkStartRemainTime / MILLISECONDS_PER_SECOND) {
			_walkStartRemainTimeFormat = new SpannableString(
					getString(R.string.walkInviteGroup_willStartSoon));

			_walkStartRemainTimeFormat.setSpan(_textAbsoluteSizeSpan, 0,
					_walkStartRemainTimeFormat.length(),
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		} else {
			_walkStartRemainTimeFormat = new SpannableString(String.format(
					getString(R.string.walkInviteGroup_startReaminTime_format),
					_walkStartRemainTimeMinutes / MINUTES_PER_HOUR,
					_walkStartRemainTimeMinutes % MINUTES_PER_HOUR));
		}

		return _walkStartRemainTimeFormat;
	}

	/**
	 * @title updateWalkInviteInviteeUserInfo
	 * @descriptor update walk invite invitee user info UI
	 * @author Ares
	 */
	private void updateWalkInviteInviteeUserInfo() {
		// check walk invite invitee user info with member status then set walk
		// invite invitee avatar imageView image and nickname textView text
		if (null != inviteeUserInfoWithMemberStatus) {
			// set its image
			inviteeAvatarImgView.setImageURI(Uri
					.parse(inviteeUserInfoWithMemberStatus.getAvatarUrl()));

			// set walk invite walk attendee avatar imgView tag view tag using
			// walk partner avatar imageView tag
			inviterWalkPathWatchBadgerImgView
					.setTag(inviteeWalkPathWatchBadgerImgView);
			inviteeWalkPathWatchBadgerImgView
					.setTag(inviterWalkPathWatchBadgerImgView);

			// check invitee member status in the schedule walk invite group
			if (MemberStatus.MEM_ONLINE == inviteeUserInfoWithMemberStatus
					.getMemberStatus()) {
				// generate holo green light foreground color span
				ForegroundColorSpan _holoGreenLightForegroundColorSpan = new ForegroundColorSpan(
						getResources().getColor(
								android.R.color.holo_green_light));

				// define invitee nickname spannable string and set its
				// attributes
				SpannableString _inviteeNicknameSpannableString = new SpannableString(
						inviteeUserInfoWithMemberStatus.getNickname());
				_inviteeNicknameSpannableString.setSpan(
						_holoGreenLightForegroundColorSpan, 0,
						_inviteeNicknameSpannableString.length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

				inviteeNicknameTextView
						.setText(_inviteeNicknameSpannableString);
			} else {
				inviteeNicknameTextView.setText(inviteeUserInfoWithMemberStatus
						.getNickname());
			}
		}
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
	 * @name WalkInviteWalkExtraData
	 * @descriptor walk invite walk extra data constant
	 * @author Ares
	 * @version 1.0
	 */
	public static final class WalkInviteWalkExtraData {

		// the schedule walk invite group id, topic, schedule begin and end time
		public static final String WIW_WALKINVITEGROUP_ID = "walkInviteWalk_walkInviteGroup_id";
		public static final String WIW_WALKINVITEGROUP_TOPIC = "walkInviteWalk_walkInviteGroup_topic";
		public static final String WIW_WALKINVITEGROUP_SCHEDULEBEGINTIME = "walkInviteWalk_walkInviteGroup_scheduleBeginTime";
		public static final String WIW_WALKINVITEGROUP_SCHEDULEENDTIME = "walkInviteWalk_walkInviteGroup_scheduleEndTime";

	}

	/**
	 * @name AttendeeAvatarImgViewOnClickListener
	 * @descriptor walk invite attendee avatar imageView on click listener
	 * @author Ares
	 * @version 1.0
	 */
	class AttendeeAvatarImgViewOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// get and the check clicked view tag
			Object _tag = v.getTag();
			if (null != _tag && _tag instanceof View) {
				// get the clicked view tag view
				View _tagView = (View) _tag;

				// check the tag view(walk path watch badger visibility)
				if (View.VISIBLE != _tagView.getVisibility()) {
					// show walk path watch badger
					_tagView.setVisibility(View.VISIBLE);

					// get and the check clicked view tag view tag
					Object _tagViewTag = _tagView.getTag();
					if (null != _tagViewTag && _tagViewTag instanceof View) {
						// hide the other walk attendee walk path watch badger
						((View) _tagViewTag).setVisibility(View.GONE);
					}
				}
			} else {
				LOGGER.error("");
			}
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
	 * @name WalkControlBtnOnClickListener
	 * @descriptor walk control button on click listener
	 * @author Ares
	 * @version 1.0
	 */
	class WalkControlBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(final View v) {
			// get and check walk control button tag
			Object _tag = v.getTag();
			if (null != _tag && _tag instanceof CharSequence) {
				// get walk control button text
				CharSequence _walkControlBtnText = (CharSequence) _tag;

				// get walk start and stop button text
				String _walkStartBtnText = getString(R.string.walkStart_button_text);
				final String _walkStopBtnText = getString(R.string.walkStop_button_text);

				// update walk control button background, text and tag
				if (_walkStartBtnText.equalsIgnoreCase(_walkControlBtnText
						.toString())) {
					// check walk invite group id and then set attendee start
					// walk
					if (null != scheduleWalkInviteGroupId) {
						walkInviteModel.startWalking(1002, "token",
								scheduleWalkInviteGroupId, new ICMConnector() {

									@Override
									public void onSuccess(Object... retValue) {
										// set walk attendee(inviter and
										// invitee) avatar imageView on click
										// listener
										setWalkAttendeeAvatarImgViewOnClickListener();

										// update walk control button text,
										// background resource and tag
										((Button) v).setText(_walkStopBtnText);
										v.setBackgroundResource(R.drawable.walk_stopbutton_bg);
										v.setTag(((Button) v).getText());

										// mark attendee walk started
										isAttendeeWalkControlled = true;
									}

									@Override
									public void onFailure(int errorCode,
											String errorMsg) {
										LOGGER.error("Schedule walk invite group start walk error, error code = "
												+ errorCode
												+ " and message = "
												+ errorMsg);

										//
									}

								});
					}
				} else if (getString(R.string.walkStop_button_text)
						.equalsIgnoreCase(_walkControlBtnText.toString())) {
					if (null != scheduleWalkInviteGroupId) {
						// check walk invite group id and then set attendee stop
						// walk
						walkInviteModel.stopWalking(1002, "token",
								scheduleWalkInviteGroupId, new ICMConnector() {

									@Override
									public void onSuccess(Object... retValue) {
										// check return values
										if (null != retValue
												&& 2 < retValue.length
												&& (retValue[0] instanceof Long && retValue[1] instanceof Long)
												&& retValue[retValue.length - 1] instanceof List) {
											// define walk invite walk extra
											// data map
											Map<String, Object> _extraMap = new HashMap<String, Object>();

											// put walk invite group type, walk
											// start, stop time and attendees
											// walk result to extra data map as
											// param
											_extraMap
													.put(GroupWalkResultExtraData.GWR_GROUP_TYPE,
															GroupType.WALK_GROUP);
											_extraMap
													.put(GroupWalkResultExtraData.GWR_GROUP_WALK_STARTTIME,
															retValue[0]);
											_extraMap
													.put(GroupWalkResultExtraData.GWR_GROUP_WALK_STARTTIME,
															retValue[1]);
											_extraMap
													.put(GroupWalkResultExtraData.GWR_GROUP_ATTENDEES_WALKRESULT,
															retValue[retValue.length - 1]);

											// go to walk invite walk result
											// activity with extra data map
											popPushActivityForResult(
													GroupWalkResultActivity.class,
													_extraMap);
										} else {
											LOGGER.error("Get walk invite group walk start, stop time and attendees walk result info error");
										}
									}

									@Override
									public void onFailure(int errorCode,
											String errorMsg) {
										LOGGER.error("Schedule walk invite group stop walk error, error code = "
												+ errorCode
												+ " and message = "
												+ errorMsg);

										//
									}

								});
					}
				}
			}
		}

	}

}
