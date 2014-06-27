/**
 * 
 */
package com.smartsport.spedometer.pedometer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.smartsport.spedometer.R;
import com.smartsport.spedometer.customwidget.SSBNavTitleBarButtonItem;
import com.smartsport.spedometer.group.info.result.UserInfoGroupResultBean;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.pedometer.PersonalWalkResultActivity.PersonalWalkResultExtraData;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name PersonalPedometerActivity
 * @descriptor smartsport personal pedometer activity
 * @author Ares
 * @version 1.0
 */
public class PersonalPedometerActivity extends SSBaseActivity {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			PersonalPedometerActivity.class);

	// milliseconds per second
	private final int MILLISECONDS_PER_SECOND = 1000;

	// user walk saved instance state
	private Bundle userWalkSavedInstanceState;

	// user walk path mapView and autoNavi map
	private MapView userWalkPathMapView;
	private AMap autoNaviMap;

	// autoNavi location source
	private WalkStartPointLocationSource autoNaviMapLocationSource;

	// walk info sliding up button
	private Button walkInfoSlidingUpBtn;

	// walk start time
	private long walkStratTime;

	// walk duration time chronometer
	private Chronometer walkDurationTimeChronometer;

	// walk info: walk total distance, total steps count and energy
	private double walkDistance;
	private int walkStepsCount;
	private float walkEnergy;

	// walk info: walk total distance, total steps count, energy, pace and speed
	// textView
	private TextView walkDistanceTextView, walkStepsCountTextView,
			walkEnergyTextView, walkPaceTextView, walkSpeedTextView;

	// walk control button
	private Button walkControlBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(userWalkSavedInstanceState = savedInstanceState);

		// set content view
		setContentView(R.layout.activity_personal_pedometer);
	}

	@Override
	protected void initContentViewUI() {
		// navigation bar
		// set navigation bar color
		setNavbarBackgroundColor(getResources().getColor(
				android.R.color.holo_green_light));

		// set left bar button item
		setLeftBarButtonItem(new SSBNavTitleBarButtonItem(this,
				R.string.cancel_walk_barbtnitem_title,
				new CancelWalkBarBtnItemOnClickListener()));

		// set title attributes
		setTitle(R.string.personalPedometer_activity_title);
		setTitleColor(Color.WHITE);
		setTitleSize(22.0f);
		setShadow(1.0f, 0.6f, 0.8f, Color.GRAY);

		// get user walk path mapView
		userWalkPathMapView = (MapView) findViewById(R.id.pp_user_walkPath_mapView);

		// walk walk path mapView perform onCreate method
		userWalkPathMapView.onCreate(userWalkSavedInstanceState);

		// get autoNavi map
		autoNaviMap = userWalkPathMapView.getMap();

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

		// set autoNavi map location source
		autoNaviMap
				.setLocationSource(autoNaviMapLocationSource = new WalkStartPointLocationSource(
						this, autoNaviMap) {

					// user walk path mask view
					private View userWalkPathMaskView = findViewById(R.id.pp_user_walkPath_mask_relativeLayout);

					// user walk info sensor imitate view
					private View userWalkInfoSensorImitateView = findViewById(R.id.pp_user_walkInfo_sensor_imitate_linearLayout);

					@Override
					protected void locateSuccess() {
						// show user walk path mask view if needed
						if (View.VISIBLE == userWalkPathMaskView
								.getVisibility()) {
							userWalkPathMaskView.setVisibility(View.GONE);
						}

						// check walk info sliding up button enable and click,
						// enable walk info sliding up button
						if (!walkInfoSlidingUpBtn.isEnabled()) {
							walkInfoSlidingUpBtn.performClick();
							walkInfoSlidingUpBtn.setEnabled(true);
						}

						// enable walk control button
						walkControlBtn.setEnabled(true);
					}

					@Override
					protected void locateTimeout() {
						// hide user walk path mask view if needed
						if (View.VISIBLE != userWalkPathMaskView
								.getVisibility()) {
							userWalkPathMaskView.setVisibility(View.VISIBLE);
						}

						// show walk info sensor imitate view
						userWalkInfoSensorImitateView
								.setVisibility(View.VISIBLE);

						// click and disable walk info sliding up button
						walkInfoSlidingUpBtn.performClick();
						walkInfoSlidingUpBtn.setEnabled(false);

						// enable walk control button
						walkControlBtn.setEnabled(true);
					}

				});

		// enable get my location, compass and hidden location, zoom controls
		// button
		autoNaviMap.getUiSettings().setMyLocationButtonEnabled(false);
		autoNaviMap.getUiSettings().setCompassEnabled(true);
		autoNaviMap.setMyLocationEnabled(true);
		autoNaviMap.getUiSettings().setZoomControlsEnabled(false);

		// get walk info sliding up button
		walkInfoSlidingUpBtn = (Button) findViewById(R.id.pp_walkInfo_slidingUp_button);

		// get walk info relativeLayout and set as walk info sliding up button
		// tag
		walkInfoSlidingUpBtn
				.setTag(findViewById(R.id.pp_walkInfo_relativeLayout));

		// set walk info sliding up button on click listener
		walkInfoSlidingUpBtn
				.setOnClickListener(new WalkInfoSlidingUpBtnOnClickListener());

		// get walk duration time chronometer
		walkDurationTimeChronometer = (Chronometer) findViewById(R.id.pp_walkDurationTime_chronometer);

		// set its text
		walkDurationTimeChronometer.setText(String.format(
				getString(R.string.walkInfo_walkDurationTime_format), 0, 0));

		// set its on chronometer tick listener
		walkDurationTimeChronometer
				.setOnChronometerTickListener(new WalkDurationTimeChronometerOnChronometerTickListener());

		// get walk total distance, total steps count, energy, pace and speed
		// textView
		walkDistanceTextView = (TextView) findViewById(R.id.pp_walkTotalDistance_textView);
		walkStepsCountTextView = (TextView) findViewById(R.id.pp_walkTotalStep_textView);
		walkEnergyTextView = (TextView) findViewById(R.id.pp_walkEnergy_textView);
		walkPaceTextView = (TextView) findViewById(R.id.pp_walkPace_textView);
		walkSpeedTextView = (TextView) findViewById(R.id.pp_walkSpeed_textView);

		// test by ares
		updateWalkInfoTextViewText(WalkInfoType.WALKINFO_WALKDISTANCE, "0.00");
		updateWalkInfoTextViewText(WalkInfoType.WALKINFO_WALKSTEPS, "0");
		updateWalkInfoTextViewText(WalkInfoType.WALKINFO_WALKENERGY, "0.0");
		updateWalkInfoTextViewText(WalkInfoType.WALKINFO_WALKPACE, "0");
		updateWalkInfoTextViewText(WalkInfoType.WALKINFO_WALKSPEED, "0.00");

		// get walk control button
		walkControlBtn = (Button) findViewById(R.id.pp_walkControl_button);

		// set its text as the tag
		walkControlBtn.setTag(walkControlBtn.getText());

		// set its on click listener
		walkControlBtn.setOnClickListener(new WalkControlBtnOnClickListener());
	}

	@Override
	protected void onResume() {
		super.onResume();

		// user walk path mapView perform onResume method
		userWalkPathMapView.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// user walk path mapView perform onSaveInstanceState method
		userWalkPathMapView.onSaveInstanceState(outState);
	}

	@Override
	protected void onPause() {
		super.onPause();

		// user walk path mapView perform onPause method
		userWalkPathMapView.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// user walk path mapView perform onDestroy method
		userWalkPathMapView.onDestroy();

		// check autoNavi location source and then deactivate
		if (null != autoNaviMapLocationSource) {
			autoNaviMapLocationSource.deactivate();
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
	 * @name CancelWalkBarBtnItemOnClickListener
	 * @descriptor cancel walk bar button item on click listener
	 * @author Ares
	 * @version 1.0
	 */
	class CancelWalkBarBtnItemOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// dismiss the activity
			dismissActivity();
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
	 * @name WalkDurationTimeChronometerOnChronometerTickListener
	 * @descriptor walk walk duration time chronometer on chronometer tick
	 *             listener
	 * @author Ares
	 * @version 1.0
	 */
	class WalkDurationTimeChronometerOnChronometerTickListener implements
			OnChronometerTickListener {

		// seconds per minute
		private final int SECONDS_PER_MINUTED = 60;

		@Override
		public void onChronometerTick(Chronometer chronometer) {
			// get chronometer run duration time(seconds)
			long _chronometerRunDurationTime = (SystemClock.elapsedRealtime() - chronometer
					.getBase()) / MILLISECONDS_PER_SECOND;

			// set user walk duration time chronometer text
			chronometer.setText(String.format(
					getString(R.string.walkInfo_walkDurationTime_format),
					_chronometerRunDurationTime / SECONDS_PER_MINUTED,
					_chronometerRunDurationTime % SECONDS_PER_MINUTED));
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
					// hide left bar button item
					setLeftBarButtonItem(null);

					// update walk control button text,
					// background resource and tag
					((Button) v).setText(_walkStopBtnText);
					v.setBackgroundResource(R.drawable.walk_stopbutton_bg);
					v.setTag(((Button) v).getText());

					// start walk
					// save user walk start time
					walkStratTime = System.currentTimeMillis()
							/ MILLISECONDS_PER_SECOND;

					// set walk duration time chronometer base and start it
					walkDurationTimeChronometer.setBase(SystemClock
							.elapsedRealtime());
					walkDurationTimeChronometer.start();

					// set user personal pedometer walk path point location
					// changed listener
					autoNaviMapLocationSource
							.setWalkPathPointLocationChangedListener(new PersonalWalkPathPointLocationChangedListener());

					// start mark user personal walk path
					autoNaviMapLocationSource.startMarkWalkPath();
				} else if (getString(R.string.walkStop_button_text)
						.equalsIgnoreCase(_walkControlBtnText.toString())) {
					// stop walk duration time chronometer
					walkDurationTimeChronometer.stop();

					// stop mark user personal walk path
					autoNaviMapLocationSource.stopMarkWalkPath();

					//

					// define walk invite walk extra data map
					Map<String, Object> _extraMap = new HashMap<String, Object>();

					// put walk start, stop time, walk path location point list
					// and user walk result to extra data map as param
					_extraMap
							.put(PersonalWalkResultExtraData.PWR_USER_WALK_STARTTIME,
									walkStratTime);
					_extraMap
							.put(PersonalWalkResultExtraData.PWR_USER_WALK_STOPTIME,
									walkStratTime
											+ (SystemClock.elapsedRealtime() - walkDurationTimeChronometer
													.getBase())
											/ MILLISECONDS_PER_SECOND);
					// test by ares
					_extraMap
							.put(PersonalWalkResultExtraData.PWR_USER_WALKPATH_LOCATIONPOINTS,
									new ArrayList<LatLonPoint>());
					_extraMap.put(
							PersonalWalkResultExtraData.PWR_USER_WALKRESULT,
							new UserInfoGroupResultBean());

					// go to personal pedometer user walk result activity with
					// extra data map
					dismissPushActivity(PersonalWalkResultActivity.class,
							_extraMap);
				}
			}
		}

		// inner class
		/**
		 * @name PersonalWalkPathPointLocationChangedListener
		 * @descriptor user personal pedometer walk path point location changed
		 *             listener
		 * @author Ares
		 * @version 1.0
		 */
		class PersonalWalkPathPointLocationChangedListener implements
				IWalkPathPointLocationChangedListener {

			@Override
			public void onLocationChanged(LatLonPoint walkPathPoint,
					double walkSpeed, double walkDistance) {
				LOGGER.info("User personal walk path point = " + walkPathPoint
						+ ", walk speed = " + walkSpeed
						+ " and walk distance = " + walkDistance);

				// increase walk total distance and total steps count
				PersonalPedometerActivity.this.walkDistance += walkDistance;
				PersonalPedometerActivity.this.walkStepsCount += walkDistance * 1000 / 0.62;

				// update energy
				PersonalPedometerActivity.this.walkEnergy = 123.5f;

				// update user personal walk info(walk total distance, total
				// steps count, energy, pace and speed) textView text
				updateWalkInfoTextViewText(
						WalkInfoType.WALKINFO_WALKDISTANCE,
						String.format(
								getString(R.string.walkInfo_distance_value_format),
								PersonalPedometerActivity.this.walkDistance));
				updateWalkInfoTextViewText(
						WalkInfoType.WALKINFO_WALKSTEPS,
						String.valueOf(PersonalPedometerActivity.this.walkStepsCount));
				updateWalkInfoTextViewText(
						WalkInfoType.WALKINFO_WALKENERGY,
						String.format(
								getString(R.string.walkInfo_energy_value_format),
								PersonalPedometerActivity.this.walkEnergy));
				updateWalkInfoTextViewText(WalkInfoType.WALKINFO_WALKPACE, "0");
				updateWalkInfoTextViewText(
						WalkInfoType.WALKINFO_WALKSPEED,
						String.format(
								getString(R.string.walkInfo_speed_value_format),
								walkSpeed));

				//
			}
		}

	}

}
