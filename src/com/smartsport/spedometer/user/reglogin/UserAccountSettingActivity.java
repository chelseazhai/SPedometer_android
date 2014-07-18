/**
 * 
 */
package com.smartsport.spedometer.user.reglogin;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.customwidget.SSProgressDialog;
import com.smartsport.spedometer.customwidget.SSUserLoginFormItem;
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.mvc.PedometerActivity;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.user.UserBean;
import com.smartsport.spedometer.user.UserModel;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name UserAccountSettingActivity
 * @descriptor smartsport user account setting activity
 * @author Ares
 * @version 1.0
 */
public class UserAccountSettingActivity extends SSBaseActivity {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			UserAccountSettingActivity.class);

	// input method manager
	private InputMethodManager inputMethodManager;

	// user model
	private UserModel userModel = UserModel.getInstance();

	// user account setting user login name and password item
	private SSUserLoginFormItem userLoginNameItem;
	private SSUserLoginFormItem userLoginPwdItem;

	// user account setting user login progress dialog
	private SSProgressDialog userLoginProgDlg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// get input method manager
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

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
		// set smartsport pedometer application version textView text
		((TextView) findViewById(R.id.uas_appVersion_textView)).setText(String
				.format(getString(R.string.appVersion_format), "0.1.0"));

		// get user account setting user login name and password item
		userLoginNameItem = (SSUserLoginFormItem) findViewById(R.id.uas_userLoginName_formItem);
		userLoginPwdItem = (SSUserLoginFormItem) findViewById(R.id.uas_userLoginPwd_formItem);

		// get user login button
		Button _userLoginBtn = (Button) findViewById(R.id.uas_userLogin_button);

		// set its on click listener
		_userLoginBtn.setOnClickListener(new UserLoginBtnOnClickListener());

		// get my Nanjing user login button
		Button _myNanjingUserLoginBtn = (Button) findViewById(R.id.uas_myNanjing_userLogin_button);

		// set its on click listener
		_myNanjingUserLoginBtn
				.setOnClickListener(new MyNanjingUserLoginBtnOnClickListener());

		// generate user assistant operate textView on click listener
		OnClickListener _userAssistantOperateTextViewOnClickListener = new UserAssistantOperateTextViewOnClickListener();

		// get new user register textView
		TextView _newUserRegisterTextView = (TextView) findViewById(R.id.uas_newUserRegister_textView);

		// set its on click listener
		_newUserRegisterTextView
				.setOnClickListener(_userAssistantOperateTextViewOnClickListener);

		// get user forget password textView
		TextView _userForgetPwdTextView = (TextView) findViewById(R.id.uas_userForgetPwd_textView);

		// set its on click listener
		_userForgetPwdTextView
				.setOnClickListener(_userAssistantOperateTextViewOnClickListener);

		// get anonymous try textView
		TextView _anonymousTryTextView = (TextView) findViewById(R.id.uas_anonymousTry_textView);

		// set its on click listener
		_anonymousTryTextView
				.setOnClickListener(_userAssistantOperateTextViewOnClickListener);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// set user account setting user login name item as focus
		userLoginNameItem.setAsFocus();
	}

	// inner class
	// // hide soft input
	// inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
	/**
	 * @name UserLoginBtnOnClickListener
	 * @descriptor user login button on click listener
	 * @author Ares
	 * @version 1.0
	 */
	class UserLoginBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// get and check user login name
			String _userLoginName = userLoginNameItem.getUserInputEditText();
			if (null == _userLoginName || "".equalsIgnoreCase(_userLoginName)) {
				LOGGER.error("User login name is null");

				// show user login name is null toast
				Toast.makeText(UserAccountSettingActivity.this,
						R.string.toast_userLogin_loginName_null,
						Toast.LENGTH_SHORT).show();

				return;
			}

			// get and check user login password
			String _userLoginPwd = userLoginPwdItem.getUserInputEditText();
			if (null == _userLoginPwd || "".equalsIgnoreCase(_userLoginPwd)) {
				LOGGER.error("User login password is null");

				// show user login password is null toast
				Toast.makeText(UserAccountSettingActivity.this,
						R.string.toast_userLogin_loginPwd_null,
						Toast.LENGTH_SHORT).show();

				return;
			}

			// show user login progress dialog
			userLoginProgDlg = SSProgressDialog
					.show(UserAccountSettingActivity.this,
							R.string.procMsg_userLogin);

			// user login to remote server
			userModel.userLogin(_userLoginName, _userLoginPwd,
					new ICMConnector() {

						@Override
						public void onSuccess(Object... retValue) {
							// dismiss user login progress dialog
							userLoginProgDlg.dismiss();

							// check return values
							if (null != retValue
									&& 0 < retValue.length
									&& retValue[retValue.length - 1] instanceof UserBean) {
								// go to smartsport pedometer activity
								LOGGER.info("Login user = "
										+ (UserBean) retValue[retValue.length - 1]);
								startActivity(new Intent(
										UserAccountSettingActivity.this,
										PedometerActivity.class));
								dismissActivity();
							} else {
								LOGGER.error("Update login user info error");
							}
						}

						@Override
						public void onFailure(int errorCode, String errorMsg) {
							LOGGER.error("User login error, error code = "
									+ errorCode + " and message = " + errorMsg);

							// dismiss user login progress dialog
							userLoginProgDlg.dismiss();

							// check error code and process hopeRun
							// business error
							if (errorCode < 100) {
								// show error message toast
								Toast.makeText(UserAccountSettingActivity.this,
										errorMsg, Toast.LENGTH_SHORT).show();

								// test by ares
								//
							}
						}

					});
		}

	}

	/**
	 * @name MyNanjingUserLoginBtnOnClickListener
	 * @descriptor my Nanjing user login button on click listener
	 * @author Ares
	 * @version 1.0
	 */
	class MyNanjingUserLoginBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// user my Nanjing account login
			// define my Nanjing application authority intent
			Intent _myNanjingAppAuthorityIntent = new Intent();

			// set extra parameter, action and add flag
			_myNanjingAppAuthorityIntent.putExtra("isFromOther", true);
			_myNanjingAppAuthorityIntent
					.setAction("com.hoperun.intelligenceportal.AuthorityActivity");
			_myNanjingAppAuthorityIntent
					.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);

			// go to my Nanjing application authority activity
			try {
				startActivity(_myNanjingAppAuthorityIntent);
			} catch (ActivityNotFoundException e) {
				LOGGER.error("Use my Nanjing account login error, exception message = "
						+ e.getMessage());

				// test by ares
				// go to my Nanjing application download intent
				startActivity(new Intent(Intent.ACTION_VIEW,
						Uri.parse("http://58.213.141.214:80/version/myNj.apk")));

				e.printStackTrace();
			}
		}

	}

	/**
	 * @name UserAssistantOperateTextViewOnClickListener
	 * @descriptor my assistant operate textView on click listener
	 * @author Ares
	 * @version 1.0
	 */
	class UserAssistantOperateTextViewOnClickListener implements
			OnClickListener {

		@Override
		public void onClick(View v) {
			// check clicked textView id
			switch (v.getId()) {
			case R.id.uas_newUserRegister_textView:
				// test by ares
				Toast.makeText(UserAccountSettingActivity.this,
						"新用户注册成功(测试@Ares)", Toast.LENGTH_LONG).show();

				// TODO Auto-generated method stub
				//
				break;

			case R.id.uas_userForgetPwd_textView:
				// test by ares
				Toast.makeText(UserAccountSettingActivity.this,
						"新密码设置成功(测试@Ares)", Toast.LENGTH_LONG).show();

				// TODO Auto-generated method stub
				//
				break;

			case R.id.uas_anonymousTry_textView:
			default:
				// TODO Auto-generated method stub
				//
				break;
			}
		}

	}

}
