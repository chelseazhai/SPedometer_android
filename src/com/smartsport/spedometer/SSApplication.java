package com.smartsport.spedometer;

import android.app.Application;
import android.content.Context;

import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name SSApplication
 * @descriptor smartsport application
 * @author Ares
 * @version 1.0
 */
public class SSApplication extends Application {

	// logger
	private static final SSLogger LOGGER = new SSLogger(SSApplication.class);

	// singleton instance
	private static volatile SSApplication _sSingletonApplication;

	/**
	 * @title SSApplication
	 * @descriptor SSApplication constructor
	 * @author Ares
	 */
	public SSApplication() {
		super();

		// initialize singleton smart sport application instance
		_sSingletonApplication = this;
	}

	/**
	 * @title getContext
	 * @descriptor retrieve application context
	 * @return context
	 * @author Ares
	 */
	public static Context getContext() {
		return _sSingletonApplication;
	}

	@Override
	public void onCreate() {
		LOGGER.debug("SSApplication - onCreate");

		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		LOGGER.debug("SSApplication - onTerminate");

		// TODO Auto-generated method stub
		super.onTerminate();
	}

	@Override
	public void onLowMemory() {
		LOGGER.debug("SSApplication - onLowMemory");

		// TODO Auto-generated method stub
		super.onLowMemory();
	}

	@Override
	public void onTrimMemory(int level) {
		LOGGER.debug("SSApplication - onTrimMemory, level = " + level);

		// TODO Auto-generated method stub
		super.onTrimMemory(level);
	}

}
