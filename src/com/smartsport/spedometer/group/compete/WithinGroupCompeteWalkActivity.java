/**
 * 
 */
package com.smartsport.spedometer.group.compete;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Color;
import android.os.Bundle;
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
import com.smartsport.spedometer.group.GroupInfoModel;
import com.smartsport.spedometer.group.info.member.MemberStatus;
import com.smartsport.spedometer.group.info.member.UserInfoMemberStatusBean;
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.pedometer.WalkStartPointLocationSource;
import com.smartsport.spedometer.stepcounter.WalkInfoType;
import com.smartsport.spedometer.user.UserGender;
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

	// locate my location timer and timer task
	private Timer LOCATE_MYLOCATION_TIMER = new Timer();
	private TimerTask locateMyLocationTimerTask;

	// group info and within group compete model
	private GroupInfoModel groupInfoModel;
	private WithinGroupCompeteModel withinGroupCompeteModel;

	// within group compete inviter walk saved instance state
	private Bundle withinGroupCompeteInviterWalkSavedInstanceState;

	// within group compete inviter walk path mapView and autoNavi map
	private MapView inviterWalkPathMapView;
	private AMap autoNaviMap;

	// autoNavi location source
	private WalkStartPointLocationSource autoNaviMapLocationSource;

	// within group compete group id, topic and start time
	private Integer competeGroupId;
	private String competeGroupTopic;
	private Long competeGroupStartTime;

	// invitees user info with status list in the within group compete group
	private List<UserInfoMemberStatusBean> inviteesUserInfoWithMemberStatusList;

	// walk info: walk total distance, total steps count, energy, pace and speed
	// textView
	private TextView walkDistanceTextView, walkStepsCountTextView,
			walkEnergyTextView, walkPaceTextView, walkSpeedTextView;

	// walk info: attendees walk trend imageView
	private ImageView attendeesWalkTrendImgView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(withinGroupCompeteInviterWalkSavedInstanceState = savedInstanceState);

		// get and check the extra data
		Bundle _extraData = getIntent().getExtras();
		if (null != _extraData) {
			// get the within group compete group id, topic and start time
			competeGroupId = _extraData
					.getInt(WithinGroupCompeteWalkExtraData.WIGCW_COMPETEGROUP_ID);
			competeGroupTopic = _extraData
					.getString(WithinGroupCompeteWalkExtraData.WIGCW_COMPETEGROUP_TOPIC);
			competeGroupStartTime = _extraData
					.getLong(WithinGroupCompeteWalkExtraData.WIGCW_COMPETEGROUP_STARTTIME);
		}

		// initialize group info and within group compete model
		groupInfoModel = new GroupInfoModel();
		withinGroupCompeteModel = new WithinGroupCompeteModel();

		// set content view
		setContentView(R.layout.activity_withingroupcompete_walk);

		// check the within group compete group id and get its info from remote
		// server
		if (null != competeGroupId) {
			groupInfoModel.getUserScheduleGroupInfo(123123, "token",
					competeGroupId, new ICMConnector() {

						//

					});

			// test by ares
			inviteesUserInfoWithMemberStatusList = new ArrayList<UserInfoMemberStatusBean>();
			for (int i = 0; i < 3; i++) {
				UserInfoMemberStatusBean _inviteeUserInfoWithMemberStatus = new UserInfoMemberStatusBean();
				_inviteeUserInfoWithMemberStatus.setUserId(12332 + i);
				_inviteeUserInfoWithMemberStatus.setAvatarUrl("/img/jshd123"
						+ i);
				_inviteeUserInfoWithMemberStatus.setNickname("小慧动" + i);
				_inviteeUserInfoWithMemberStatus
						.setGender(0 == i % 2 ? UserGender.MALE
								: UserGender.FEMALE);
				_inviteeUserInfoWithMemberStatus
						.setMemberSratus(0 == i % 2 ? MemberStatus.MEM_ONLINE
								: MemberStatus.MEM_OFFLINE);

				inviteesUserInfoWithMemberStatusList
						.add(_inviteeUserInfoWithMemberStatus);
			}
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
		// WithinGroupCompeteWalkActivity.this,
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
		
		// define compete attendee walk flag
				boolean _isWalking = false;

		// get walk info sliding up button
		Button _walkInfoSlidingUpBtn = (Button) findViewById(R.id.wigcw_walkInfo_slidingUp_button);

		// get walk info relativeLayout and set as walk info sliding up button
		// tag
		_walkInfoSlidingUpBtn
				.setTag(findViewById(R.id.wigcw_walkInfo_relativeLayout));

		// set walk info sliding up button on click listener
		_walkInfoSlidingUpBtn
				.setOnClickListener(new WalkInfoSlidingUpBtnOnClickListener());

		// get walk total distance, total steps count, energy, pace and speed
		// textView
		walkDistanceTextView = (TextView) findViewById(R.id.wigcw_walkTotalDistance_textView);
		walkStepsCountTextView = (TextView) findViewById(R.id.wigcw_walkTotalStep_textView);
		walkEnergyTextView = (TextView) findViewById(R.id.wigcw_walkEnergy_textView);
		walkPaceTextView = (TextView) findViewById(R.id.wigcw_walkPace_textView);
		walkSpeedTextView = (TextView) findViewById(R.id.wigcw_walkSpeed_textView);
		
		// get attendees walk trend imageView
		attendeesWalkTrendImgView = (ImageView) findViewById(R.id.wigcw_attendeesWalkTrend_imageView);

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

		//
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

		// check and cancel locate my location timer task
		if (null != locateMyLocationTimerTask) {
			locateMyLocationTimerTask.cancel();

			locateMyLocationTimerTask = null;
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

		// the within group compete group id, topic and start time
		public static final String WIGCW_COMPETEGROUP_ID = "withinGroupCompeteWalk_competeGroup_Id";
		public static final String WIGCW_COMPETEGROUP_TOPIC = "withinGroupCompeteWalk_competeGroup_topic";
		public static final String WIGCW_COMPETEGROUP_STARTTIME = "withinGroupCompeteWalk_competeGroup_startTime";

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

}
