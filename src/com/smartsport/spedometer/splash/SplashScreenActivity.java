package com.smartsport.spedometer.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.mvc.PedometerActivity;
import com.smartsport.spedometer.user.UserManager;
import com.smartsport.spedometer.user.UserPedometerExtBean;
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
		LOGGER.debug("Fake sleep 2 seconds");

		// sleep 2 seconds
		try {
			Thread.sleep(2 * 1000L);

			// test by ares
			// generate pedometer login user object
			UserPedometerExtBean _pedometerLoginUser = new UserPedometerExtBean();
			_pedometerLoginUser.setLoginName("18001582338");
			_pedometerLoginUser.setLoginPwd("123123");
			_pedometerLoginUser.setUserKey("token@ares");
			_pedometerLoginUser.setUserId(447376200952006L);
			_pedometerLoginUser
					.setAvatarUrl("http://218.104.116.66:8095/qmjsMSS/loadpic.jsp?path=/qmjs_files/person/447376200952006/31b8da0.jpg");

			// set pedometer login user
			UserManager.getInstance().setLoginUser(_pedometerLoginUser);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public Intent targetIntent() {
		// test by ares
		return new Intent(SplashScreenActivity.this, /* DemoActivity */
		PedometerActivity.class);
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
