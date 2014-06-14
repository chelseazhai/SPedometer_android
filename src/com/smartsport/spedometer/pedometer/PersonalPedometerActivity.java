/**
 * 
 */
package com.smartsport.spedometer.pedometer;

import android.graphics.Color;
import android.os.Bundle;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.mvc.SSBaseActivity;

/**
 * @name PersonalPedometerActivity
 * @descriptor smartsport personal pedometer activity
 * @author Ares
 * @version 1.0
 */
public class PersonalPedometerActivity extends SSBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO Auto-generated method stub

		// set content view
		setContentView(R.layout.activity_personal_pedometer);

		//
	}

	@Override
	protected void initContentViewUI() {
		// navigation bar
		// set navigation bar color
		setNavbarBackgroundColor(getResources().getColor(
				android.R.color.holo_green_light));

		// set title attributes
		setTitle(R.string.personalPedometer_activity_title);
		setTitleColor(Color.WHITE);
		setTitleSize(22.0f);
		setShadow(1.0f, 0.6f, 0.8f, Color.GRAY);

		//
	}

}
