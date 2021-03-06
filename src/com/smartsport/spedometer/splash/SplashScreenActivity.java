package com.smartsport.spedometer.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.localstorage.AppInterPriSharedPreferencesHelper;
import com.smartsport.spedometer.localstorage.pedometer.SPUserLocalStorageAttributes;
import com.smartsport.spedometer.mvc.PedometerActivity;
import com.smartsport.spedometer.user.UserManager;
import com.smartsport.spedometer.user.UserPedometerExtBean;
import com.smartsport.spedometer.user.reglogin.UserAccountSettingActivity;
import com.smartsport.spedometer.user.reglogin.UserAccountSettingActivity.UserAccountSettingExtraData;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name SplashScreenActivity
 * @descriptor splash screen activity
 * @author Ares
 * @version 1.0
 */
public class SplashScreenActivity extends Activity implements IAppLaunch {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			SplashScreenActivity.class);

	// application launching work thread
	private AppLaunchingThread appLaunchingWorkThread;

	// application launch model
	private AppLaunchModel appLaunchModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.activity_splash_screen);

		// get application launch imageView
		ImageView _appLaunchImgView = (ImageView) findViewById(R.id.ss_appLaunch_imageView);

		// set application launch imageView image resource
		_appLaunchImgView.setImageResource(R.drawable.img_splash);

		// initialize application launch model
		appLaunchModel = new AppLaunchModel();

		// application launching using work thread
		appLaunchingWorkThread = new AppLaunchingThread();
		appLaunchingWorkThread.start();
	}

	@Override
	protected void onDestroy() {
		// check application launching work thread is or not running
		if (appLaunchingWorkThread.isRunning()) {
			appLaunchingWorkThread.stopRunning();
		}

		super.onDestroy();
	}

	@Override
	public boolean didFinishLaunching() {
		LOGGER.debug("Fake sleep 3 seconds");

		// sleep 2 seconds
		try {
			Thread.sleep(3 * 1000L);

			// // test by ares
			// // generate pedometer login user object
			// UserPedometerExtBean _pedometerLoginUser = new
			// UserPedometerExtBean();
			// _pedometerLoginUser.setLoginName("18001582338");
			// _pedometerLoginUser.setLoginPwd("123");
			// _pedometerLoginUser.setUserKey("token@ares");
			// // _pedometerLoginUser.setUserId(5196787426436882L); // 58
			// _pedometerLoginUser.setUserId(1745891595710907L); // 218
			// _pedometerLoginUser
			// .setAvatarUrl("http://218.104.116.66:8095/qmjsMSS/loadpic.jsp?path=/qmjs_files/person/1745891595710907/ebeb705.jpg");
			//
			// // set pedometer login user
			// UserManager.getInstance().setLoginUser(_pedometerLoginUser);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public Intent targetIntent() {
		// define target intent
		Intent _targetIntent = null;

		// get application internal private shared preferences helper instance
		AppInterPriSharedPreferencesHelper _appInterPriSharedPreferencesHelper = AppInterPriSharedPreferencesHelper
				.getInstance();

		// get and check last login user id from local storage
		Long _lastLoginUserId = _appInterPriSharedPreferencesHelper
				.getLong(SPUserLocalStorageAttributes.LASTLOGIN_USERID.name());
		if (null != _lastLoginUserId) {
			// generate user local storage shared preferences file name, using
			// logined user id
			String _userLSSPFileName = String.valueOf(_lastLoginUserId);

			// get and check last login user name
			String _userLoginName = _appInterPriSharedPreferencesHelper
					.getString(SPUserLocalStorageAttributes.PF_USER_LOGINNAME
							.name(), _userLSSPFileName);
			if (null != _userLoginName && !"".equalsIgnoreCase(_userLoginName)) {
				// get and check logined user key
				String _loginedUserKey = _appInterPriSharedPreferencesHelper
						.getString(
								SPUserLocalStorageAttributes.PF_USER_LOGINED_USERKEY
										.name(), _userLSSPFileName);
				if (null != _loginedUserKey
						&& !"".equalsIgnoreCase(_loginedUserKey)) {
					// load logined user from local storage and save to user
					// manager
					UserPedometerExtBean _loginedUser = new UserPedometerExtBean();
					_loginedUser.setLoginName(_userLoginName);
					_loginedUser.setUserKey(_loginedUserKey);
					_loginedUser.setUserId(_lastLoginUserId);
					_loginedUser
							.setAvatarUrl(_appInterPriSharedPreferencesHelper
									.getString(
											SPUserLocalStorageAttributes.PF_USER_LOGINED_USERAVATARURL
													.name(), _userLSSPFileName));
					UserManager.getInstance().setLoginUser(_loginedUser);

					// go to pedometer activity
					_targetIntent = new Intent(SplashScreenActivity.this,
							PedometerActivity.class);
				} else {
					// go to user account setting activity with last login user
					// name
					_targetIntent = new Intent(SplashScreenActivity.this,
							UserAccountSettingActivity.class);
					_targetIntent
							.putExtra(
									UserAccountSettingExtraData.UAS_LASTOPERATEUSER_LOGINNAME,
									_userLoginName);
				}
			} else {
				// go to user account setting activity
				_targetIntent = new Intent(SplashScreenActivity.this,
						UserAccountSettingActivity.class);
			}
		} else {
			// go to user account setting activity
			_targetIntent = new Intent(SplashScreenActivity.this,
					UserAccountSettingActivity.class);
		}

		return _targetIntent;
	}

	// inner class
	/**
	 * @name AppLaunchingThread
	 * @descriptor application launching work thread
	 * @author Ares
	 * @version 1.0
	 */
	class AppLaunchingThread extends Thread {

		// application launching work thread running flag
		private boolean running = true;

		@Override
		public void run() {
			// get target intent
			Intent _targetIntent = targetIntent();

			// check target intent
			if (appLaunchModel.isTargetIntentNull(SplashScreenActivity.this,
					_targetIntent)) {
				// launch application
				didFinishLaunching();

				// go to target intent if needed
				if (running) {
					appLaunchModel.go2TargetIntent(SplashScreenActivity.this,
							_targetIntent);

					// application launching work thread finished
					running = false;
				}
			}

			super.run();
		}

		public boolean isRunning() {
			return running;
		}

		/**
		 * @title stopRunning
		 * @descriptor don't go to target intent
		 * @author Ares
		 */
		public void stopRunning() {
			running = false;
		}

	}

}
