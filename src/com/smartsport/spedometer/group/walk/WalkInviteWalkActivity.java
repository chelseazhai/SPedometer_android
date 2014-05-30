/**
 * 
 */
package com.smartsport.spedometer.group.walk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Color;
import android.location.Location;
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

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.LocationSource.OnLocationChangedListener;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.smartsport.spedometer.R;
import com.smartsport.spedometer.customwidget.SSBNavBarButtonItem;
import com.smartsport.spedometer.group.GroupInfoModel;
import com.smartsport.spedometer.group.GroupType;
import com.smartsport.spedometer.group.GroupWalkResultActivity;
import com.smartsport.spedometer.group.GroupWalkResultActivity.GroupWalkResultExtraData;
import com.smartsport.spedometer.group.info.member.MemberStatus;
import com.smartsport.spedometer.group.info.member.UserInfoMemberStatusBean;
import com.smartsport.spedometer.group.info.result.UserInfoGroupResultBean;
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.stepcounter.WalkInfoType;
import com.smartsport.spedometer.user.UserGender;
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

	// group info and walk invite model
	private GroupInfoModel groupInfoModel;
	private WalkInviteModel walkInviteModel;

	// walk invite walk saved instance state
	private Bundle walkInviteWalkSavedInstanceState;

	// walk invite inviter walk path mapView and autoNavi map
	private MapView inviterWalkPathMapView;
	private AMap autoNaviMap;

	// autoNavi location source
	private WalkStartPointLocationSource autoNaviMapLocationSource;

	// autoNavi map location manager proxy
	private LocationManagerProxy locationManagerProxy;

	// inviter walk location changed listener
	private OnLocationChangedListener inviterWalkLocationChangedListener;

	// the schedule walk invite group topic, schedule begin and end time
	private String scheduleWalkInviteGroupTopic;
	private Long scheduleWalkInviteGroupBeginTime;
	private Long scheduleWalkInviteGroupEndTime;

	// invitee user info with status in the schedule walk invite group
	private UserInfoMemberStatusBean inviteeUserInfoWithMemberStatus;

	// walk invite start remain or walk duration time textView
	private TextView walkStartRemainOrWalkDurationTimeTextView;

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
			// get the schedule walk invite group topic, schedule begin and end
			// time
			scheduleWalkInviteGroupTopic = _extraData
					.getString(WalkInviteWalkExtraData.WIW_WALKINVITEGROUP_TOPIC);
			scheduleWalkInviteGroupBeginTime = _extraData
					.getLong(WalkInviteWalkExtraData.WIW_WALKINVITEGROUP_SCHEDULEBEGINTIME);
			scheduleWalkInviteGroupEndTime = _extraData
					.getLong(WalkInviteWalkExtraData.WIW_WALKINVITEGROUP_SCHEDULEENDTIME);
		}

		// initialize group info and walk invite model
		groupInfoModel = new GroupInfoModel();
		walkInviteModel = new WalkInviteModel();

		// test by ares
		inviteeUserInfoWithMemberStatus = new UserInfoMemberStatusBean();
		inviteeUserInfoWithMemberStatus.setUserId(123321);
		inviteeUserInfoWithMemberStatus.setAvatarUrl("/img/jshd123");
		inviteeUserInfoWithMemberStatus.setNickname("小慧动");
		inviteeUserInfoWithMemberStatus.setGender(UserGender.FEMALE);
		inviteeUserInfoWithMemberStatus.setAge(28);
		inviteeUserInfoWithMemberStatus.setHeight(165.0f);
		inviteeUserInfoWithMemberStatus.setWeight(55.0f);
		inviteeUserInfoWithMemberStatus
				.setMemberSratus(MemberStatus.MEM_OFFLINE);

		// set content view
		setContentView(R.layout.activity_walkinvite_walk);

		// get the schedule walk invite group info from remote server
		groupInfoModel.getUserScheduleGroupInfo(123123, "token", 0,
				new ICMConnector() {

					//

				});
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

		// get walk invite inviter walk path mapView
		inviterWalkPathMapView = (MapView) findViewById(R.id.wiw_inviter_walkPath_mapView);

		// inviter walk path mapView perform onCreate method
		inviterWalkPathMapView.onCreate(walkInviteWalkSavedInstanceState);

		// get autoNavi map
		autoNaviMap = inviterWalkPathMapView.getMap();

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

		// set its location source
		autoNaviMap
				.setLocationSource(autoNaviMapLocationSource = new WalkStartPointLocationSource());

		// enable get my location, compass and hidden location, zoom controls
		// button
		autoNaviMap.getUiSettings().setMyLocationButtonEnabled(false);
		autoNaviMap.setMyLocationEnabled(true);
		autoNaviMap.getUiSettings().setCompassEnabled(true);
		autoNaviMap.getUiSettings().setZoomControlsEnabled(false);

		// get walk invite inviter avatar imageView
		ImageView _inviterAvatarImgView = (ImageView) findViewById(R.id.wiw_inviterAvatar_imageView);

		// set its image
		_inviterAvatarImgView.setImageURI(null);

		// get walk path watch badger imageView and set it as walk invite
		// inviter avatar imageView tag
		ImageView _selfWalkPathWatchBadgerImgView = (ImageView) findViewById(R.id.wiw_selfWalkPathWatchBadger_imageView);
		_inviterAvatarImgView.setTag(_selfWalkPathWatchBadgerImgView);

		// get walk invite walk start remain or walk duration time textView
		walkStartRemainOrWalkDurationTimeTextView = (TextView) findViewById(R.id.wiw_walkStartRemainTime_or_walkDurationTime_textView);

		// define attendee walk flag
		boolean _isWalking = false;

		// get and check walk invite walk start remain time
		Long _walkStartRemainTime = getWalkStartRemainTime(
				scheduleWalkInviteGroupBeginTime,
				scheduleWalkInviteGroupEndTime);
		if (null == _walkStartRemainTime) {
			// walking
			// set attendee walk flag
			_isWalking = true;

			// generate holo green light foreground color span
			ForegroundColorSpan _holoGreenLightForegroundColorSpan = new ForegroundColorSpan(
					getResources().getColor(android.R.color.holo_green_light));

			// get attendee walk duration time from local storage
			// test by ares
			SpannableString _walkDurationTime = new SpannableString("123'30\"");
			_walkDurationTime.setSpan(_holoGreenLightForegroundColorSpan, 0,
					_walkDurationTime.length(),
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

			// set walk invite walk start remain or walk duration time textView
			// text
			walkStartRemainOrWalkDurationTimeTextView
					.setText(_walkDurationTime);
		} else if (0 <= _walkStartRemainTime) {
			// waiting for start
			// set walk invite walk start remain or walk duration time textView
			// text
			walkStartRemainOrWalkDurationTimeTextView
					.setText(formatWalkStartRemainTime(_walkStartRemainTime));
		} else {
			// invalid
			// generate holo red dark foreground color span and text absolute
			// size span
			ForegroundColorSpan _holoRedDarkForegroundColorSpan = new ForegroundColorSpan(
					getResources().getColor(android.R.color.holo_red_dark));
			AbsoluteSizeSpan _textAbsoluteSizeSpan = new AbsoluteSizeSpan(16,
					true);

			// set invalid walk invite group tip
			SpannableString _invalidWalkInviteGroup = new SpannableString(
					getString(R.string.scheduleWalkInviteGroup_status_invalid));
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
		}

		// get walk invite invitee avatar imageView
		ImageView _inviteeAvatarImgView = (ImageView) findViewById(R.id.wiw_inviteeAvatar_imageView);

		// check walk invite invitee user info with member status then set walk
		// invite invitee avatar imageView image and nickname textView text
		if (null != inviteeUserInfoWithMemberStatus) {
			// set its image
			_inviteeAvatarImgView.setImageURI(Uri
					.parse(inviteeUserInfoWithMemberStatus.getAvatarUrl()));

			// get walk path watch badger imageView and set it as walk invite
			// invitee avatar imageView tag
			ImageView _walkPartnerWalkPathWatchBadgerImgView = (ImageView) findViewById(R.id.wiw_walkPartnerWalkPathWatchBadger_imageView);
			_inviteeAvatarImgView
					.setTag(_walkPartnerWalkPathWatchBadgerImgView);

			// set walk invite walk attendee avatar imgView tag view tag using
			// walk partner avatar imageView tag
			_selfWalkPathWatchBadgerImgView
					.setTag(_walkPartnerWalkPathWatchBadgerImgView);
			_walkPartnerWalkPathWatchBadgerImgView
					.setTag(_selfWalkPathWatchBadgerImgView);

			// get walk invite invitee nickname textView
			TextView _inviteeNicknameTextView = (TextView) findViewById(R.id.wiw_inviteeNickname_textView);

			// check invitee member status in the schedule walk invite group
			if (MemberStatus.MEM_ONLINE == inviteeUserInfoWithMemberStatus
					.getMemberSratus()) {
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

				_inviteeNicknameTextView
						.setText(_inviteeNicknameSpannableString);
			} else {
				_inviteeNicknameTextView
						.setText(inviteeUserInfoWithMemberStatus.getNickname());
			}
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

		// check attendee walk flag and update walk control button text,
		// background and show walk control button
		if (_isWalking) {
			_walkControlBtn.setText(R.string.walkStop_button_text);
			_walkControlBtn
					.setBackgroundResource(R.drawable.walk_stopbutton_bg);
			_walkControlBtn.setVisibility(View.VISIBLE);
		}

		// set its text as the tag
		_walkControlBtn.setTag(_walkControlBtn.getText());

		// set its on click listener
		_walkControlBtn.setOnClickListener(new WalkControlBtnOnClickListener());

		// check walk invite walk start remain time
		if (null != _walkStartRemainTime && 0 == _walkStartRemainTime) {
			// define walk invite attendee avatar imageView on click listener
			AttendeeAvatarImgViewOnClickListener _attendeeAvatarImgViewOnClickListener = new AttendeeAvatarImgViewOnClickListener();

			// set walk attendee(inviter and invitee) avatar imageView on click
			// listener
			_inviterAvatarImgView
					.setOnClickListener(_attendeeAvatarImgViewOnClickListener);
			_inviteeAvatarImgView
					.setOnClickListener(_attendeeAvatarImgViewOnClickListener);

			// show walk control button
			_walkControlBtn.setVisibility(View.VISIBLE);
		}
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
		Long _walkStartRemainTime = 0L;

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
					getString(R.string.scheduleWalkInviteGroup_status_startSoon));

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

		// the schedule walk invite group topic, schedule begin and end time
		public static final String WIW_WALKINVITEGROUP_TOPIC = "walkInviteWalk_walkInviteGroup_topic";
		public static final String WIW_WALKINVITEGROUP_SCHEDULEBEGINTIME = "walkInviteWalk_walkInviteGroup_scheduleBeginTime";
		public static final String WIW_WALKINVITEGROUP_SCHEDULEENDTIME = "walkInviteWalk_walkInviteGroup_scheduleEndTime";

	}

	/**
	 * @name WalkStartPointLocationSource
	 * @descriptor smartsport walk start point location source
	 * @author Ares
	 * @version 1.0
	 */
	class WalkStartPointLocationSource implements LocationSource {

		// logger
		private final SSLogger LOGGER = new SSLogger(
				WalkStartPointLocationSource.class);

		// autoNavi map location listener
		private AutoNaviMapLocationListener autoNaviMapLocationListener;

		@Override
		public void activate(OnLocationChangedListener locationChangedListener) {
			// save inviter walk location changed listener
			inviterWalkLocationChangedListener = locationChangedListener;

			// check autoNavi map location manager proxy and initialize
			if (null == locationManagerProxy) {
				locationManagerProxy = LocationManagerProxy
						.getInstance(WalkInviteWalkActivity.this);

				// request location update
				locationManagerProxy
						.requestLocationUpdates(
								LocationProviderProxy.AMapNetwork,
								2000,
								10,
								autoNaviMapLocationListener = new AutoNaviMapLocationListener());
			}
		}

		@Override
		public void deactivate() {
			// clear inviter walk location changed listener
			inviterWalkLocationChangedListener = null;

			// check autoNavi map location manager proxy and clear
			if (null != locationManagerProxy) {
				locationManagerProxy.removeUpdates(autoNaviMapLocationListener);
				locationManagerProxy.destory();
			}
			locationManagerProxy = null;
		}

		// inner class
		/**
		 * @name AutoNaviMapLocationListener
		 * @descriptor smartsport walk invite autoNavi map location listener
		 * @author Ares
		 * @version 1.0
		 */
		class AutoNaviMapLocationListener implements AMapLocationListener {

			@Override
			@Deprecated
			public void onLocationChanged(Location location) {
				// nothing to do
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// nothing to do
			}

			@Override
			public void onProviderEnabled(String provider) {
				// nothing to do
			}

			@Override
			public void onProviderDisabled(String provider) {
				// nothing to do
			}

			@Override
			public void onLocationChanged(AMapLocation autoNaviMapLocation) {
				LOGGER.info("@@@, autoNaviMapLocation = " + autoNaviMapLocation);

				// check inviter walk location changed listener and autoNavi map
				// location
				if (null != inviterWalkLocationChangedListener
						&& null != autoNaviMapLocation) {
					// show location
					inviterWalkLocationChangedListener
							.onLocationChanged(autoNaviMapLocation);

					// animate camera
					autoNaviMap
							.animateCamera(CameraUpdateFactory.newLatLngZoom(
									new LatLng(autoNaviMapLocation
											.getLatitude(), autoNaviMapLocation
											.getLongitude()),
									getResources()
											.getInteger(
													R.integer.config_autoNaviMap_zoomLevel)));
				} else {
					LOGGER.error("AutoNavi mapView location error, inviter walk location changed listener = "
							+ inviterWalkLocationChangedListener
							+ " and autoNavi map location = "
							+ autoNaviMapLocation);
				}
			}

		}

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
		public void onClick(View v) {
			// get and check walk control button tag
			Object _tag = v.getTag();
			if (null != _tag && _tag instanceof CharSequence) {
				// get walk control button text
				CharSequence _walkControlBtnText = (CharSequence) _tag;

				// get walk start and stop button text
				String _walkStartBtnText = getString(R.string.walkStart_button_text);
				String _walkStopBtnText = getString(R.string.walkStop_button_text);

				// update walk control button background, text and tag
				if (_walkStartBtnText.equalsIgnoreCase(_walkControlBtnText
						.toString())) {
					// walk invite group attendee start walk
					walkInviteModel.startWalking(123123, "token", 0,
							new ICMConnector() {

								//

							});

					// test by ares
					v.setBackgroundResource(R.drawable.walk_stopbutton_bg);
					((Button) v).setText(_walkStopBtnText);
					v.setTag(((Button) v).getText());
					isAttendeeWalkControlled = true;
				} else if (getString(R.string.walkStop_button_text)
						.equalsIgnoreCase(_walkControlBtnText.toString())) {
					// walk invite group attendee stop walk
					walkInviteModel.stopWalking(123123, "token", 0,
							new ICMConnector() {

								//

							});

					// test by ares
					// define walk invite walk extra data map
					Map<String, Object> _extraMap = new HashMap<String, Object>();

					// put walk invite group type, walk start, stop time and
					// attendees walk result to extra data map as param
					_extraMap.put(GroupWalkResultExtraData.GWR_GROUP_TYPE,
							GroupType.WALK_GROUP);
					_extraMap.put(
							GroupWalkResultExtraData.GWR_GROUP_WALK_STARTTIME,
							Long.valueOf(123456789));
					_extraMap.put(
							GroupWalkResultExtraData.GWR_GROUP_WALK_STARTTIME,
							Long.valueOf(132465798));
					_extraMap
							.put(GroupWalkResultExtraData.GWR_GROUP_ATTENDEES_WALKRESULT,
									new ArrayList<UserInfoGroupResultBean>());

					// go to walk invite walk result activity with extra data
					// map
					popPushActivityForResult(GroupWalkResultActivity.class,
							_extraMap);
				}
			}
		}

	}

}
