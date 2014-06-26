/**
 * 
 */
package com.smartsport.spedometer.group.compete;

import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.mvc.SSBaseActivity;
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

	// get within group or groups compete walk info timer and timer task
	private final Timer GET_COMPETEWALKINFO_TIMER = new Timer();
	private TimerTask getCompeteWalkInfoTimerTask;

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
			// get the within group or groups compete group attendees user info
			// with status list
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
							// get, check the within group or groups compete
							// group id and then get within group compete
							// attendees walk info
							String _competeGroupId = _extraData
									.getString(CompeteAttendeesWalkTrendExtraData.CAWT_COMPETEGROUP_ID);
							if (null != _competeGroupId) {
								withinGroupCompeteModel
										.getWithinGroupCompeteWalkingInfo(
												123123, "token",
												_competeGroupId,
												new ICMConnector() {

													@Override
													public void onSuccess(
															Object... retValue) {
														// TODO Auto-generated
														// method stub

													}

													@Override
													public void onFailure(
															int errorCode,
															String errorMsg) {
														// TODO Auto-generated
														// method stub

													}

												});
							}
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
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// check and cancel get compete walk info timer task
		if (null != getCompeteWalkInfoTimerTask) {
			getCompeteWalkInfoTimerTask.cancel();

			getCompeteWalkInfoTimerTask = null;
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

		// the within group or groups compete group id and attendees user info
		// with status list
		public static final String CAWT_COMPETEGROUP_ID = "competeAttendeesWalkTrend_competeGroup_Id";
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

}
