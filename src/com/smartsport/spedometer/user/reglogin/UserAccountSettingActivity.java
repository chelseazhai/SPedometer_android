/**
 * 
 */
package com.smartsport.spedometer.user.reglogin;

import android.os.Bundle;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.mvc.SSBaseActivity;

/**
 * @name UserAccountSettingActivity
 * @descriptor smartsport user account setting activity
 * @author Ares
 * @version 1.0
 */
public class UserAccountSettingActivity extends SSBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO Auto-generated method stub

		// set content view
		setContentView(R.layout.activity_useraccount_setting);
	}

	@Override
	protected boolean hideNavBarWhenOnCreate() {
		// hide navigation bar when on create
		return true;
	}

	@Override
	protected void initContentViewUI() {
		// TODO Auto-generated method stub

	}

}
