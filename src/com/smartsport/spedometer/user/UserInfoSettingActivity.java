/**
 * 
 */
package com.smartsport.spedometer.user;

import android.graphics.Color;
import android.os.Bundle;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.mvc.SSBaseActivity;

/**
 * @name UserInfoSettingActivity
 * @descriptor smartsport user info setting activity
 * @author Ares
 * @version 1.0
 */
public class UserInfoSettingActivity extends SSBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.activity_userinfo_setting);

		// navigation bar
		// set navigation bar color
		setNavbarBackgroundColor(getResources().getColor(
				android.R.color.holo_green_light));

		// // set right bar button item
		// setRightBarButtonItem(new SSBNavImageBarButtonItem(this,
		// R.drawable.img_userinfo_setting,
		// new UserInfoSettingOnClickListener()));

		// set title attributes
		setTitle(R.string.userinfo_setting_activity_title);
		setTitleColor(Color.WHITE);
		setTitleSize(26.0f);

		//
	}

}
