package com.smartsport.spedometer.mvc;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.customwidget.SSBNavImageBarButtonItem;
import com.smartsport.spedometer.strangersocial.NearbyStrangersActivity;
import com.smartsport.spedometer.user.UserInfoSettingActivity;
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

	// walk invite, within group compete and nearby strangers button
	private Button walkInviteBtn;
	private Button withinGroupCompeteBtn;
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

		// get nearby stranger button
		nearbyStrangersBtn = (Button) findViewById(R.id.pfc_nearbyStrangers_button);

		// set its on click listener
		nearbyStrangersBtn
				.setOnClickListener(new GetNearbyStrangersOnClickListener());
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
	 * @name GetNearbyStrangersOnClickListener
	 * @descriptor get nearby strangers button on click listener
	 * @author Ares
	 * @version 1.0
	 */
	class GetNearbyStrangersOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// go to nearby strangers activity
			pushActivity(NearbyStrangersActivity.class);
		}

	}

}
