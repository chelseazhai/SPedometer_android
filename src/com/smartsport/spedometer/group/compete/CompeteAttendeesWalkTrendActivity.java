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
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewStub;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.customwidget.SSChartBarGraph;
import com.smartsport.spedometer.customwidget.SSChartBarGraph.SSChartBar;
import com.smartsport.spedometer.customwidget.SSChartLineGraph;
import com.smartsport.spedometer.customwidget.SSChartLineGraph.SSChartLine;
import com.smartsport.spedometer.customwidget.SSChartLineGraph.SSChartLinePoint;
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.user.UserManager;
import com.smartsport.spedometer.user.UserPedometerExtBean;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name CompeteAttendeesWalkTrendActivity
 * @descriptor smartsport within group or groups compete attendees walk trend
 *             activity
 * @author Ares
 * @version 1.0
 */
public class CompeteAttendeesWalkTrendActivity extends SSBaseActivity {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			CompeteAttendeesWalkTrendActivity.class);

	// milliseconds per second
	private final int MILLISECONDS_PER_SECOND = 1000;

	// get within group or groups compete walk info timer and handler
	private final Timer GET_COMPETEWALKINFO_TIMER = new Timer();
	private final Handler GET_COMPETEWALKINFO_HANDLER = new Handler();

	// get within group or groups compete walk info timer task
	private TimerTask getCompeteWalkInfoTimerTask;

	// within group or groups compete attendees walk duration time
	private int walkDurationTime;

	// within group or groups compete attendees walk duration time chronometer
	private Chronometer walkDurationTimeChronometer;

	// within group or groups compete model
	private WithinGroupCompeteModel withinGroupCompeteModel = WithinGroupCompeteModel
			.getInstance();

	// within group or groups compete attendees walk total distance bar and
	// speed line chart
	private View walkTotalDistanceBarChart;
	private View walkSpeedLineChart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// get and check the extra data
		final Bundle _extraData = getIntent().getExtras();
		if (null != _extraData) {
			// get the within group or groups compete group walk duration time
			// and attendees user info with status list
			walkDurationTime = _extraData
					.getInt(CompeteAttendeesWalkTrendExtraData.CAWT_COMPETEGROUP_WALKDURATIONTIME,
							0);
			//
		}

		// set content view
		setContentView(R.layout.activity_competeattendees_walktrend);

		// get the within group or groups compete attendees walk info
		// immediately and then every 30 seconds later
		GET_COMPETEWALKINFO_TIMER.schedule(
				getCompeteWalkInfoTimerTask = new TimerTask() {

					@Override
					public void run() {
						// check the extra data again
						if (null != _extraData) {
							GET_COMPETEWALKINFO_HANDLER.post(new Runnable() {

								@Override
								public void run() {
									// get, check the within group or groups
									// compete group id and then get within
									// group compete attendees walk info
									String _competeGroupId = _extraData
											.getString(CompeteAttendeesWalkTrendExtraData.CAWT_COMPETEGROUP_ID);
									if (null != _competeGroupId) {
										// get pedometer login user
										UserPedometerExtBean _loginUser = (UserPedometerExtBean) UserManager
												.getInstance().getLoginUser();

										withinGroupCompeteModel
												.getWithinGroupCompeteWalkingInfo(
														_loginUser.getUserId(),
														_loginUser.getUserKey(),
														_competeGroupId, 0L,
														new ICMConnector() {

															@Override
															public void onSuccess(
																	Object... retValue) {
																// check return
																// values
																if (null != retValue
																		&& 1 < retValue.length
																		&& retValue[0] instanceof Long
																		&& retValue[retValue.length - 1] instanceof List) {
																	LOGGER.info("@@, within group compete: timestamp = "
																			+ retValue[0]
																			+ " and attendees temp walking result list = "
																			+ retValue[retValue.length - 1]);

																	//
																} else {
																	LOGGER.error("Get walk invite group walk partner walking info error");
																}
															}

															@Override
															public void onFailure(
																	int errorCode,
																	String errorMsg) {
																LOGGER.error("Get within group compete attendees walk info error, error code = "
																		+ errorCode
																		+ " and message = "
																		+ errorMsg);

																//
															}

														});
									}
								}

							});
						}
					}

				},
				0,
				getResources()
						.getInteger(
								R.integer.config_withinGroupCompeteWalk_publishAndGetAttendees_walkInfo_period)
						* MILLISECONDS_PER_SECOND);
	}

	@Override
	protected void initContentViewUI() {
		// navigation bar
		// set navigation bar color
		setNavbarBackgroundColor(getResources().getColor(
				android.R.color.holo_green_light));

		// set title attributes
		setTitle("");
		setTitleColor(Color.WHITE);
		setTitleSize(22.0f);
		setShadow(1.0f, 0.6f, 0.8f, Color.GRAY);

		// get compete attendees walk trend segment redioGroup
		RadioGroup _attendeesWalkTrendSegRadioGroup = (RadioGroup) findViewById(R.id.cawt_attendees_walkTrend_segment_radioGroup);

		// set its on checked change listener
		_attendeesWalkTrendSegRadioGroup
				.setOnCheckedChangeListener(new CompeteAttendeesWalkTrendSegRadioGroupOnCheckedChangeListener());

		// update the within group or groups compete attendees walk trend
		updateAttendeesWalkTrend(_attendeesWalkTrendSegRadioGroup
				.getCheckedRadioButtonId());

		// get compete attendees walk duration time chronometer
		walkDurationTimeChronometer = (Chronometer) findViewById(R.id.cawt_walkDuration_chronometer);

		// set its text
		walkDurationTimeChronometer.setText(String.format(
				getString(R.string.competeAttendees_walkDurationTime_format),
				0, 0));

		// set its on chronometer tick listener
		walkDurationTimeChronometer
				.setOnChronometerTickListener(new CompeteAttendeesWalkDurationTimeChronometerOnChronometerTickListener());

		// set compete attendees walk duration time chronometer base and start
		// it
		walkDurationTimeChronometer.setBase(SystemClock.elapsedRealtime()
				- walkDurationTime * MILLISECONDS_PER_SECOND);
		walkDurationTimeChronometer.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// check and cancel get compete walk info timer task
		if (null != getCompeteWalkInfoTimerTask) {
			getCompeteWalkInfoTimerTask.cancel();

			getCompeteWalkInfoTimerTask = null;
		}

		// check and stop within group compete attendees walk duration time
		// chronometer
		if (null != walkDurationTimeChronometer) {
			walkDurationTimeChronometer.stop();
		}
	}

	/**
	 * @title updateAttendeesWalkTrend
	 * @descriptor update the within group or groups compete attendees walk
	 *             trend
	 * @param checkedSegmentId
	 *            : checked attendees walk trend segment radioButton id
	 * @author Ares
	 */
	private void updateAttendeesWalkTrend(int checkedSegmentId) {
		// check the checked attendees walk trend segment radioButton id
		switch (checkedSegmentId) {
		case R.id.cawt_attendees_walkTotalDistance_segment:
			// update title
			setTitle(R.string.competeAttendees_walk_totalDistance);

			// check, get compete attendees walk total distance bar chart and
			// then show it
			if (null == walkTotalDistanceBarChart) {
				walkTotalDistanceBarChart = ((ViewStub) findViewById(R.id.cawt_attendees_walkTotalDistance_barChart_viewStub))
						.inflate();

				// test by ares
				ArrayList<SSChartBar> aBars = new ArrayList<SSChartBar>();
				SSChartBar bar = new SSChartBar();
				bar.setColor(getResources().getColor(
						android.R.color.holo_green_light));
				bar.setSelectedColor(getResources().getColor(
						android.R.color.holo_orange_light));
				bar.setName("Test1");
				bar.setValue(1000);
				bar.setValueString("1,000");
				aBars.add(bar);
				bar = new SSChartBar();
				bar.setColor(getResources().getColor(
						android.R.color.holo_orange_dark));
				bar.setName("Test2");
				bar.setValue(500);
				bar.setValueString("500");
				aBars.add(bar);
				bar = new SSChartBar();
				bar.setColor(getResources().getColor(
						android.R.color.holo_purple));
				bar.setName("Test3");
				bar.setValue(1500);
				bar.setValueString("1,500");
				aBars.add(bar);
				bar = new SSChartBar();
				bar.setColor(getResources().getColor(
						android.R.color.holo_blue_dark));
				bar.setName("Test4");
				bar.setValue(900);
				bar.setValueString("900");
				aBars.add(bar);
				bar = new SSChartBar();
				bar.setColor(getResources().getColor(
						android.R.color.holo_red_dark));
				bar.setName("Test5");
				bar.setValue(1400);
				bar.setValueString("1,400");
				aBars.add(bar);
				bar = new SSChartBar();
				bar.setColor(getResources().getColor(
						android.R.color.darker_gray));
				bar.setName("Test6");
				bar.setValue(300);
				bar.setValueString("300");
				aBars.add(bar);

				final SSChartBarGraph barGraph = (SSChartBarGraph) walkTotalDistanceBarChart
						.findViewById(R.id.cwttd_attendees_walkTotalDistance_chartBarGraph);
				// bg = barGraph;
				barGraph.setBars(aBars);
			} else {
				walkTotalDistanceBarChart.setVisibility(View.VISIBLE);
			}

			// update it and then hide compete attendees walk speed line chart
			// if needed
			//
			if (null != walkSpeedLineChart) {
				walkSpeedLineChart.setVisibility(View.GONE);
			}
			break;

		case R.id.cawt_attendees_walkSpeed_segment:
			// update title
			setTitle(R.string.competeAttendees_walk_speed);

			// check, get compete attendees walk speed line chart and then show
			// it
			if (null == walkSpeedLineChart) {
				walkSpeedLineChart = ((ViewStub) findViewById(R.id.cawt_attendees_walkSpeed_lineChart_viewStub))
						.inflate();

				// test by ares
				SSChartLine l = new SSChartLine();
				l.setUsingDips(true);
				SSChartLinePoint p = new SSChartLinePoint();
				p.setX(0);
				p.setY(3);
				p.setSelectedColor(getResources().getColor(
						android.R.color.holo_orange_light));
				l.addPoint(p);
				p = new SSChartLinePoint();
				p.setX(4);
				p.setY(5);
				l.addPoint(p);
				p = new SSChartLinePoint();
				p.setX(8);
				p.setY(4);
				l.addPoint(p);
				p = new SSChartLinePoint();
				p.setX(12);
				p.setY(8);
				l.addPoint(p);
				l.setColor(getResources().getColor(
						android.R.color.holo_orange_dark));
				SSChartLine l1 = new SSChartLine();
				l1.setUsingDips(true);
				p = new SSChartLinePoint();
				p.setX(0);
				p.setY(1);
				l1.addPoint(p);
				p = new SSChartLinePoint();
				p.setX(4);
				p.setY(6);
				l1.addPoint(p);
				p = new SSChartLinePoint();
				p.setX(8);
				p.setY(8);
				l1.addPoint(p);
				p = new SSChartLinePoint();
				p.setX(12);
				p.setY(5);
				l1.addPoint(p);
				l1.setColor(getResources().getColor(
						android.R.color.holo_green_dark));

				SSChartLineGraph li = (SSChartLineGraph) walkSpeedLineChart
						.findViewById(R.id.cwts_attendees_walkSpeed_chartLineGraph);
				li.setUsingDips(true);
				li.addLine(l);
				li.addLine(l1);
				li.setRangeX(0, 15);
				li.setRangeY(0, 10);
			} else {
				walkSpeedLineChart.setVisibility(View.VISIBLE);
			}

			// update it and then hide compete attendees walk total distance bar
			// chart if needed
			//
			if (null != walkTotalDistanceBarChart) {
				walkTotalDistanceBarChart.setVisibility(View.GONE);
			}
			break;
		}
	}

	// inner class
	/**
	 * @name CompeteAttendeesWalkTrendExtraData
	 * @descriptor within group or groups compete attendees walk trend extra
	 *             data constant
	 * @author Ares
	 * @version 1.0
	 */
	public static final class CompeteAttendeesWalkTrendExtraData {

		// the within group or groups compete group id, walk duration time and
		// attendees user info with status list
		public static final String CAWT_COMPETEGROUP_ID = "competeAttendeesWalkTrend_competeGroup_Id";
		public static final String CAWT_COMPETEGROUP_WALKDURATIONTIME = "competeAttendeesWalkTrend_competeGroup_walkDurationTime";
		public static final String CAWT_COMPETEGROUP_ATTENDEES = "competeAttendeesWalkTrend_competeGroup_attendeesList";

	}

	/**
	 * @name CompeteAttendeesWalkTrendSegRadioGroupOnCheckedChangeListener
	 * @descriptor within group or groups compete attendees walk trend segment
	 *             radioGroup on checked change listener
	 * @author Ares
	 * @version 1.0
	 */
	class CompeteAttendeesWalkTrendSegRadioGroupOnCheckedChangeListener
			implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// update the within group or groups compete attendees walk trend
			updateAttendeesWalkTrend(checkedId);
		}

	}

	/**
	 * @name 
	 *       CompeteAttendeesWalkDurationTimeChronometerOnChronometerTickListener
	 * @descriptor within group compete attendees walk duration time chronometer
	 *             on chronometer tick listener
	 * @author Ares
	 * @version 1.0
	 */
	class CompeteAttendeesWalkDurationTimeChronometerOnChronometerTickListener
			implements OnChronometerTickListener {

		// seconds per minute
		private final int SECONDS_PER_MINUTED = 60;

		@Override
		public void onChronometerTick(Chronometer chronometer) {
			// get chronometer run duration time(seconds)
			long _chronometerRunDurationTime = (SystemClock.elapsedRealtime() - chronometer
					.getBase()) / MILLISECONDS_PER_SECOND;

			// set user walk duration time chronometer text
			chronometer
					.setText(String
							.format(getString(R.string.competeAttendees_walkDurationTime_format),
									_chronometerRunDurationTime
											/ SECONDS_PER_MINUTED,
									_chronometerRunDurationTime
											% SECONDS_PER_MINUTED));
		}

	}

}
