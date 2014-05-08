/**
 * 
 */
package com.smartsport.spedometer.splash;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name AppLaunchModel
 * @descriptor application launching model
 * @author Ares
 * @version 1.0
 */
public class AppLaunchModel {

	// logger
	private static final SSLogger LOGGER = new SSLogger(AppLaunchModel.class);

	// application launch handler
	private Handler appLaunchHandler;

	// operator activity and target intent
	private static Activity operatorActivity;
	private static Intent targetIntent;

	/**
	 * @title AppLaunchModel
	 * @descriptor application launch model constructor
	 * @author Ares
	 */
	public AppLaunchModel() {
		super();

		// initialize application launch handler
		appLaunchHandler = new AppLaunchHandler();
	}

	/**
	 * @title isTargetIntentNull
	 * @descriptor check target intent is null or not
	 * @param activityContext
	 *            : operator activity context
	 * @param targetIntent
	 *            : target intent
	 * @return target intent null checked result
	 * @author Ares
	 */
	public boolean isTargetIntentNull(Activity activityContext,
			Intent targetIntent) {
		// define target intent null checked result
		boolean _targetIntentNullCheckedResult = true;

		// check target intent
		if (null == targetIntent) {
			// update target intent null checked result
			_targetIntentNullCheckedResult = false;

			LOGGER.error("Check target intent is null or not, target intent is null, please set a target intent for going first!");

			// check operator activity
			if (null == activityContext
					|| (null != activityContext && !(activityContext instanceof Activity))) {
				LOGGER.error("Check target intent is null or not, operator activity error, operator activity = "
						+ activityContext);
			} else {
				// save operator activity
				operatorActivity = activityContext;

				// send target intent null error message
				appLaunchHandler
						.sendEmptyMessage(AppLaunchMSG.TARGETINTENT_NULL.appLaunchMSGWhatCode);
			}
		}

		return _targetIntentNullCheckedResult;
	}

	/**
	 * @title go2TargetIntent
	 * @descriptor go to target intent
	 * @param activityContext
	 *            : operator activity context
	 * @param targetIntent
	 *            : target intent
	 * @author Ares
	 */
	public void go2TargetIntent(Activity activityContext, Intent targetIntent) {
		// check operator activity and target intent
		if (null == activityContext
				|| (null != activityContext && !(activityContext instanceof Activity))) {
			LOGGER.error("Go to target intent, operator activity error, operator activity = "
					+ activityContext);
		} else if (null == targetIntent) {
			LOGGER.error("Go to target intent, target intent is null, can't go anywhere!");
		} else {
			// save operator activity and target intent
			operatorActivity = activityContext;
			AppLaunchModel.targetIntent = targetIntent;

			// send go to target intent message
			appLaunchHandler
					.sendEmptyMessage(AppLaunchMSG.GO2TARGETINTENT.appLaunchMSGWhatCode);
		}
	}

	// inner class
	/**
	 * @name AppLaunchMSG
	 * @descriptor application launch message
	 * @author Ares
	 * @version 1.0
	 */
	enum AppLaunchMSG {

		// target intent is null and go to target intent message
		TARGETINTENT_NULL(111), GO2TARGETINTENT(222);

		// application launch message what code
		private int appLaunchMSGWhatCode;

		/**
		 * @title AppLaunchMSG
		 * @descriptor application launch message enum private constructor
		 * @param pMsgWhatCode
		 *            : message what code
		 * @author Ares
		 */
		private AppLaunchMSG(int pMsgWhatCode) {
			// save application launch message what code
			appLaunchMSGWhatCode = pMsgWhatCode;
		}

		/**
		 * @title getAppLaunchMSGWhatCode
		 * @descriptor get application launch message what code
		 * @return application launch message what code
		 * @author Ares
		 */
		public int getAppLaunchMSGWhatCode() {
			return appLaunchMSGWhatCode;
		}

		/**
		 * @title getAppLaunchMSG
		 * @descriptor get application launch message
		 * @param application
		 *            launch message what code
		 * @return application launch message
		 * @author Ares
		 */
		public static AppLaunchMSG getAppLaunchMSG(int pMsgWhatCode) {
			// define application launch message
			AppLaunchMSG _appLaunchMSG = null;

			// check message what code
			if (TARGETINTENT_NULL.appLaunchMSGWhatCode == pMsgWhatCode) {
				_appLaunchMSG = TARGETINTENT_NULL;
			} else if (GO2TARGETINTENT.appLaunchMSGWhatCode == pMsgWhatCode) {
				_appLaunchMSG = GO2TARGETINTENT;
			}

			return _appLaunchMSG;
		}

	}

	/**
	 * @name AppLaunchHandler
	 * @descriptor application launch handler
	 * @author Ares
	 * @version 1.0
	 */
	static class AppLaunchHandler extends Handler {

		// logger
		private static final SSLogger LOGGER = new SSLogger(
				AppLaunchHandler.class);

		@Override
		public void handleMessage(Message msg) {
			// get application launch message with message what code
			AppLaunchMSG _appLaunchMSG = AppLaunchMSG.getAppLaunchMSG(msg.what);

			// check application launch message
			if (null != _appLaunchMSG) {
				switch (_appLaunchMSG) {
				case TARGETINTENT_NULL:
					// create application launch target intent null alert dialog
					AlertDialog _nullTargetIntentAlertDialog = new AlertDialog.Builder(
							operatorActivity)
							.setTitle("Title")
							.setMessage(
									"Application launch target intent is null")
							.setNegativeButton(
									"ok",
									new NullTargetIntentAlterDlgNegativeBtnOnClickListener())
							.create();

					// show application launch target intent null alert dialog
					_nullTargetIntentAlertDialog.show();
					break;

				case GO2TARGETINTENT:
					// go to target intent
					operatorActivity.startActivity(targetIntent);

					// finish operator activity
					operatorActivity.finish();
					break;
				}
			} else {
				LOGGER.error("Get application launch message error, message = "
						+ msg);
			}

			super.handleMessage(msg);
		}

		// inner class
		/**
		 * @name NullTargetIntentAlterDlgNegativeBtnOnClickListener
		 * @descriptor null target intent alter dialog negative button on click
		 *             listener
		 * @author Ares
		 * @version 1.0
		 */
		class NullTargetIntentAlterDlgNegativeBtnOnClickListener implements
				OnClickListener {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// finish operator activity
				operatorActivity.finish();
			}

		}

	}

}
