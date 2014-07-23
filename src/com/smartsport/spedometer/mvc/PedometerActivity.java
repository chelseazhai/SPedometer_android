package com.smartsport.spedometer.mvc;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.customwidget.SSAlterDialog;
import com.smartsport.spedometer.customwidget.SSBNavImageBarButtonItem;
import com.smartsport.spedometer.group.ScheduleWalkInviteGroupsActivity;
import com.smartsport.spedometer.group.compete.WithinGroupCompeteInviteInfoSettingActivity;
import com.smartsport.spedometer.history.HistoryInfoActivity;
import com.smartsport.spedometer.strangersocial.NearbyStrangersActivity;
import com.smartsport.spedometer.user.info.setting.UserInfoSettingActivity;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name PedometerActivity
 * @descriptor smartsport pedometer activity
 * @author Ares
 * @version 1.0
 */
public class PedometerActivity extends SSBaseActivity {

	// logger
	private static final SSLogger LOGGER = new SSLogger(PedometerActivity.class);

	// walk invite and nearby strangers button
	private Button walkInviteBtn;
	private Button nearbyStrangersBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.activity_pedometer);
	}

	@Override
	protected void initContentViewUI() {
		// navigation bar
		// set navigation bar color
		setNavbarBackgroundColor(getResources().getColor(
				android.R.color.holo_green_light));

		// set right bar button item
		setRightBarButtonItem(new SSBNavImageBarButtonItem(this,
				R.drawable.img_userinfo_setting,
				new UserInfoSettingOnClickListener()));

		// set title attributes
		setTitle(R.string.pedometer_activity_title);
		setTitleColor(Color.WHITE);
		setTitleSize(26.0f);
		setShadow(1.0f, 0.6f, 0.8f, Color.GRAY);

		// get personal pedometer button
		Button _personalPedometerBtn = (Button) findViewById(R.id.p_personalPedometer_button);

		// set its on click listener
		_personalPedometerBtn
				.setOnClickListener(new PersonalPedometerBtnOnClickListener());

		// define pedometer social function button on click listener
		SocialFunctionBtnOnClickListener _pedometerSocialFunctionBtnOnClickListener = new SocialFunctionBtnOnClickListener();

		// get walk invite button
		walkInviteBtn = (Button) findViewById(R.id.pfc_walkInvite_button);

		// set its on click listener
		walkInviteBtn
				.setOnClickListener(_pedometerSocialFunctionBtnOnClickListener);

		// get within group compete invite button
		Button _withinGroupCompeteBtn = (Button) findViewById(R.id.pfc_withinGroupCompete_button);

		// set its on click listener
		_withinGroupCompeteBtn
				.setOnClickListener(_pedometerSocialFunctionBtnOnClickListener);

		// get nearby stranger button
		nearbyStrangersBtn = (Button) findViewById(R.id.pfc_nearbyStrangers_button);

		// set its on click listener
		nearbyStrangersBtn
				.setOnClickListener(_pedometerSocialFunctionBtnOnClickListener);

		// get history info button
		Button _historyInfoBtn = (Button) findViewById(R.id.pfc_historyRecord_button);

		// set its on click listener
		_historyInfoBtn.setOnClickListener(new HistoryInfoBtnOnClickListener());
	}

	// inner class
	/**
	 * @name UserInfoSettingOnClickListener
	 * @descriptor user info setting bar button item on click listener
	 * @author Ares
	 * @version 1.0
	 */
	class UserInfoSettingOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// go to user info setting activity
			pushActivity(UserInfoSettingActivity.class);
		}

	}

	/**
	 * @name PersonalPedometerBtnOnClickListener
	 * @descriptor personal pedometer button on click listener
	 * @author Ares
	 * @version 1.0
	 */
	class PersonalPedometerBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			//
			new SSAlterDialog.SSAlterDialogBuilder(PedometerActivity.this)
					.setView(R.layout.ss_alterdialog_layout).show();

			// go to personal pedometer activity
			// presentActivity(PersonalPedometerActivity.class);
		}

	}

	/**
	 * @name SocialFunctionBtnOnClickListener
	 * @descriptor social function button on click listener
	 * @author Ares
	 * @version 1.0
	 */
	class SocialFunctionBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// define pedometer social function activity class
			Class<? extends Activity> _pedometerSFACls = null;

			// check clicked button id
			switch (v.getId()) {
			case R.id.pfc_walkInvite_button:
				// schedule walk invite groups activity
				_pedometerSFACls = ScheduleWalkInviteGroupsActivity.class;
				break;

			case R.id.pfc_withinGroupCompete_button:
				// within group compete invite info setting activity
				_pedometerSFACls = WithinGroupCompeteInviteInfoSettingActivity.class;
				break;

			case R.id.pfc_nearbyStrangers_button:
				// nearby strangers activity
				_pedometerSFACls = NearbyStrangersActivity.class;
				break;
			}

			// go to pedometer social function activity
			pushActivity(_pedometerSFACls);
		}

	}

	/**
	 * @name HistoryInfoBtnOnClickListener
	 * @descriptor history info button on click listener
	 * @author Ares
	 * @version 1.0
	 */
	class HistoryInfoBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// go to history info activity
			pushActivity(HistoryInfoActivity.class);
		}

	}

}
